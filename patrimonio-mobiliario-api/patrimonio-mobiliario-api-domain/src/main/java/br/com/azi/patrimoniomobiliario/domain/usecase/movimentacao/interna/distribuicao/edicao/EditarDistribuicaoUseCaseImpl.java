package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.edicao;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Responsavel;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.edicao.converter.EditarDistribuicaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.edicao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.edicao.exception.MovimentacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.edicao.exception.TipoDeMovimentacaoException;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import br.com.azi.patrimoniomobiliario.utils.string.StringUtils;
import br.com.azi.patrimoniomobiliario.utils.validate.NotaLancamentoContabilValidate;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class EditarDistribuicaoUseCaseImpl implements EditarDistribuicaoUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    private final EditarDistribuicaoOutputDataConverter converter;

    @Override
    public EditarDistribuicaoOutputData executar(EditarDistribuicaoInputData inputData) {
        validarDadosEntrada(inputData);
        validarDistribuicaoExistente(inputData.getId());

        final Movimentacao distribuicao = buscar(inputData.getId());
        validarMovimentacaoEmModoElaboracao(distribuicao);
        validarMovimentacaoDoTipoDistribuicao(distribuicao);

        atualizarDadosDistribuicao(inputData, distribuicao);
        final Movimentacao distribuicaoAtualizada = salvar(distribuicao);
        return converter.to(distribuicaoAtualizada);
    }

    private void validarDadosEntrada(EditarDistribuicaoInputData inputData) {
        Validator.of(inputData)
            .validate(EditarDistribuicaoInputData::getId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private void validarDistribuicaoExistente(Long id) {
        if (!movimentacaoDataProvider.existe(id)) {
            throw new MovimentacaoNaoEncontradaException("Distribuição não encontrada.");
        }
    }

    private Movimentacao buscar(Long id) {
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(id);
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void validarMovimentacaoEmModoElaboracao(Movimentacao distribuicao) {
        if (!Movimentacao.Situacao.EM_ELABORACAO.equals(distribuicao.getSituacao()) &&
            !Movimentacao.Situacao.ERRO_PROCESSAMENTO.equals(distribuicao.getSituacao())) {
            throw new MovimentacaoNaoEstaEmElaboracaoException();
        }
    }

    private void validarMovimentacaoDoTipoDistribuicao(Movimentacao movimentacao) {
        if (!TipoMovimentacaoEnum.DISTRIBUICAO.equals(movimentacao.getTipo())) {
            throw new TipoDeMovimentacaoException("Movimentação não é uma distribuição.");
        }
    }

    private void atualizarDadosDistribuicao(EditarDistribuicaoInputData inputData, Movimentacao distribuicao) {
        atualizarDadosOrigemDestino(inputData, distribuicao);
        atualizarDadosNotaLancamentoContabil(inputData, distribuicao);
        distribuicao.setMotivoObservacao(inputData.getMotivoObservacao());
        distribuicao.setNumeroProcesso(inputData.getNumeroProcesso());
        distribuicao.setResponsavel(Responsavel.builder().id(inputData.getResponsavel()).build());
    }

    private void atualizarDadosOrigemDestino(EditarDistribuicaoInputData inputData, Movimentacao distribuicao) {
        if (Objects.nonNull(inputData.getOrgao())) {
            final UnidadeOrganizacional orgaoOrigemDestino = UnidadeOrganizacional.builder()
                .id(inputData.getOrgao())
                .build();

            final boolean trocouOrgaoOrigem = Objects.isNull(distribuicao.getOrgaoOrigem()) || (
                Objects.nonNull(distribuicao.getOrgaoOrigem()) &&
                    !inputData.getOrgao().equals(distribuicao.getOrgaoOrigem().getId())
            );

            distribuicao.setOrgaoOrigem(orgaoOrigemDestino);
            distribuicao.setOrgaoDestino(orgaoOrigemDestino);

            boolean trocouSetorOrigem;
            if (Objects.nonNull(inputData.getSetorOrigem())) {
                trocouSetorOrigem = Objects.isNull(distribuicao.getSetorOrigem()) || (
                    Objects.nonNull(distribuicao.getSetorOrigem()) &&
                        !inputData.getSetorOrigem().equals(distribuicao.getSetorOrigem().getId())
                );

                distribuicao.setSetorOrigem(UnidadeOrganizacional.builder()
                    .id(inputData.getSetorOrigem())
                    .build());
            } else {
                trocouSetorOrigem = Objects.nonNull(distribuicao.getSetorOrigem());
                distribuicao.setSetorOrigem(null);
            }

            if (Objects.nonNull(inputData.getSetorDestino())) {
                distribuicao.setSetorDestino(UnidadeOrganizacional.builder()
                    .id(inputData.getSetorDestino())
                    .build());
            } else {
                distribuicao.setSetorDestino(null);
            }

            if ((trocouOrgaoOrigem || trocouSetorOrigem) && movimentacaoItemDataProvider.existePorMovimentacaoId(distribuicao.getId())) {
                movimentacaoItemDataProvider.removerPorMovimentacao(distribuicao.getId());
            }

        } else {
            final boolean possuiOrgaoOrigem = Objects.nonNull(distribuicao.getOrgaoOrigem());

            distribuicao.setOrgaoOrigem(null);
            distribuicao.setOrgaoDestino(null);
            distribuicao.setSetorOrigem(null);
            distribuicao.setSetorDestino(null);

            if (possuiOrgaoOrigem && movimentacaoItemDataProvider.existePorMovimentacaoId(distribuicao.getId())) {
                movimentacaoItemDataProvider.removerPorMovimentacao(distribuicao.getId());
            }
        }
    }

    private void atualizarDadosNotaLancamentoContabil(EditarDistribuicaoInputData inputData, Movimentacao distribuicao) {
        NotaLancamentoContabil notaLancamentoContabil = distribuicao.getNotaLancamentoContabil();

        if (validarNotaLancamentoContabilExistenteSemInformacoes(notaLancamentoContabil, inputData)) {
            notaLancamentoContabilDataProvider.remover(notaLancamentoContabil.getId());
            distribuicao.setNotaLancamentoContabil(null);
            return;
        }

        if (!validarInformacoesNotaLancamentoContabilPreenchidas(notaLancamentoContabil, inputData)) {
            return;
        }

        if (Objects.isNull(notaLancamentoContabil)) {
            notaLancamentoContabil = new NotaLancamentoContabil();
        }

        if (StringUtils.isNotEmpty(inputData.getNumeroNotaLancamentoContabil())) {
            NotaLancamentoContabilValidate.validarFormatoNumeroNotaLancamentoContabil(inputData.getNumeroNotaLancamentoContabil());
            if (!inputData.getNumeroNotaLancamentoContabil().equals(notaLancamentoContabil.getNumero())) {
                NotaLancamentoContabilValidate.validarNumeroDuplicado(notaLancamentoContabilDataProvider
                    .existePorNumero(inputData.getNumeroNotaLancamentoContabil()));
            }
        }

        if (Objects.nonNull(inputData.getDataNotaLancamentoContabil())) {
            NotaLancamentoContabilValidate.validarDataNotaLancamentoContabil(inputData.getDataNotaLancamentoContabil());
        }

        notaLancamentoContabil.setNumero(inputData.getNumeroNotaLancamentoContabil());
        notaLancamentoContabil.setDataLancamento(DateUtils.asLocalDateTime(inputData.getDataNotaLancamentoContabil()));
        distribuicao.setNotaLancamentoContabil(notaLancamentoContabilDataProvider.salvar(notaLancamentoContabil));
    }

    private boolean validarNotaLancamentoContabilExistenteSemInformacoes(NotaLancamentoContabil notaLancamentoContabil, EditarDistribuicaoInputData inputData) {
        return Objects.nonNull(notaLancamentoContabil)
            && Objects.nonNull(notaLancamentoContabil.getId())
            && StringUtils.isEmpty(inputData.getNumeroNotaLancamentoContabil())
            && Objects.isNull(inputData.getDataNotaLancamentoContabil());
    }

    private boolean validarInformacoesNotaLancamentoContabilPreenchidas(NotaLancamentoContabil notaLancamentoContabil, EditarDistribuicaoInputData inputData) {
        return Objects.nonNull(notaLancamentoContabil) ||
            StringUtils.isNotEmpty(inputData.getNumeroNotaLancamentoContabil()) ||
            Objects.nonNull(inputData.getDataNotaLancamentoContabil());
    }

    private Movimentacao salvar(Movimentacao distribuicao) {
        return movimentacaoDataProvider.salvar(distribuicao);
    }
}
