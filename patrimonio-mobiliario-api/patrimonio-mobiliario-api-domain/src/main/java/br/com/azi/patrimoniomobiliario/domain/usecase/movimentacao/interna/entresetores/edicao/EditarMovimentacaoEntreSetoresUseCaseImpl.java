package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.edicao;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Responsavel;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.edicao.converter.EditarMovimentacaoEntreSetoresOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.edicao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.edicao.exception.MovimentacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.edicao.exception.SetorOrigemEDestinoSaoIguaisException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.edicao.exception.TipoDeMovimentacaoException;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import br.com.azi.patrimoniomobiliario.utils.string.StringUtils;
import br.com.azi.patrimoniomobiliario.utils.validate.NotaLancamentoContabilValidate;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class EditarMovimentacaoEntreSetoresUseCaseImpl implements EditarMovimentacaoEntreSetoresUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    private final EditarMovimentacaoEntreSetoresOutputDataConverter converter;

    @Override
    public EditarMovimentacaoEntreSetoresOutputData executar(EditarMovimentacaoEntreSetoresInputData inputData) {
        validarDadosEntrada(inputData);
        validarSeMovimentacaoExiste(inputData.getId());

        Movimentacao definitiva = buscarMovimentacao(inputData.getId());
        validarSeSetorOrigemEDestinoSaoIguais(inputData);
        validarMovimentacaoEmElaboracao(definitiva);
        validarSeMovimentacaoDoTipoDefinitiva(definitiva);

        atualizarDadosMovimentacao(inputData, definitiva);
        Movimentacao definitivaAtualizada = salvar(definitiva);
        return converter.to(definitivaAtualizada);
    }

    private void validarDadosEntrada(EditarMovimentacaoEntreSetoresInputData inputData) {
        Validator.of(inputData)
            .validate(EditarMovimentacaoEntreSetoresInputData::getId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private void validarSeMovimentacaoExiste(Long movimentacaoId) {
        if (!movimentacaoDataProvider.existe(movimentacaoId)) {
            throw new MovimentacaoNaoEncontradaException();
        }
    }

    private Movimentacao buscarMovimentacao(Long movimentacaoId) {
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(movimentacaoId);
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void validarSeSetorOrigemEDestinoSaoIguais(EditarMovimentacaoEntreSetoresInputData inputData) {
        if (Objects.nonNull(inputData.getSetorOrigem()) && Objects.nonNull(inputData.getSetorDestino()) && inputData.getSetorOrigem().equals(inputData.getSetorDestino())) {
            throw new SetorOrigemEDestinoSaoIguaisException();
        }
    }

    private void validarMovimentacaoEmElaboracao(Movimentacao definitiva) {
        if (!Movimentacao.Situacao.EM_ELABORACAO.equals(definitiva.getSituacao())) {
            throw new MovimentacaoNaoEstaEmElaboracaoException();
        }
    }

    private void validarSeMovimentacaoDoTipoDefinitiva(Movimentacao movimentacao) {
        if (!TipoMovimentacaoEnum.ENTRE_SETORES.equals(movimentacao.getTipo())) {
            throw new TipoDeMovimentacaoException("Movimentação não é entre setores.");
        }
    }

    private void atualizarDadosMovimentacao(EditarMovimentacaoEntreSetoresInputData inputData, Movimentacao movimentacao) {
        atualizarDadosOrigemDestino(inputData, movimentacao);
        atualizarDadosNotaLancamentoContabil(inputData, movimentacao);
        movimentacao.setMotivoObservacao(inputData.getMotivoObservacao());
        movimentacao.setNumeroProcesso(inputData.getNumeroProcesso());
        movimentacao.setResponsavel(Responsavel.builder().id(inputData.getResponsavel()).build());
    }

    private boolean validarSeTrocouSetorOrigem(EditarMovimentacaoEntreSetoresInputData inputData, Movimentacao movimentacao) {
        if (Objects.nonNull(inputData.getSetorOrigem())) {
            return  Objects.isNull(movimentacao.getSetorOrigem()) || (
                Objects.nonNull(movimentacao.getSetorOrigem()) &&
                    !inputData.getSetorOrigem().equals(movimentacao.getSetorOrigem().getId())
            );
        } else {
            return Objects.nonNull(movimentacao.getSetorOrigem());
        }
    }

    private boolean validarSeExistemPatrimoniosNaMovimentacao(Movimentacao movimentacao) {
        return movimentacaoItemDataProvider.existePorMovimentacaoId(movimentacao.getId());
    }

    private void atualizarSetores(EditarMovimentacaoEntreSetoresInputData inputData, Movimentacao movimentacao) {
        if (Objects.nonNull(inputData.getSetorOrigem())) {
            movimentacao.setSetorOrigem(UnidadeOrganizacional.builder().id(inputData.getSetorOrigem()).build());
        } else {
            movimentacao.setSetorOrigem(null);
        }

        if (Objects.nonNull(inputData.getSetorDestino())) {
            movimentacao.setSetorDestino(UnidadeOrganizacional.builder().id(inputData.getSetorDestino()).build());
        } else {
            movimentacao.setSetorDestino(null);
        }
    }

    private boolean validarSeUnidadesOrganizacionaisOrigemAlteradas(EditarMovimentacaoEntreSetoresInputData inputData, Movimentacao movimentacao) {
            if (Objects.isNull(movimentacao.getOrgaoOrigem()) || (
                Objects.nonNull(movimentacao.getOrgaoOrigem()) &&
                    !inputData.getOrgao().equals(movimentacao.getOrgaoOrigem().getId())
            )) {
                return true;
            }
            return validarSeTrocouSetorOrigem(inputData, movimentacao);
    }

    private void atualizarDadosOrigemDestino(EditarMovimentacaoEntreSetoresInputData inputData, Movimentacao movimentacao) {
        if (Objects.nonNull(inputData.getOrgao())) {
            if (validarSeUnidadesOrganizacionaisOrigemAlteradas(inputData, movimentacao)
                && movimentacaoItemDataProvider.existePorMovimentacaoId(movimentacao.getId())) {
                movimentacaoItemDataProvider.removerPorMovimentacao(movimentacao.getId());
            }

            movimentacao.setOrgaoOrigem(UnidadeOrganizacional.builder().id(inputData.getOrgao()).build());
            movimentacao.setOrgaoDestino(UnidadeOrganizacional.builder().id(inputData.getOrgao()).build());

            atualizarSetores(inputData, movimentacao);
        } else {
            limparDadosMovimentacao(movimentacao);
        }
    }

    private void limparDadosMovimentacao(Movimentacao movimentacao) {
        final boolean possuiOrgaoOrigem = Objects.nonNull(movimentacao.getOrgaoOrigem());

        movimentacao.setOrgaoOrigem(null);
        movimentacao.setOrgaoDestino(null);
        movimentacao.setSetorOrigem(null);
        movimentacao.setSetorDestino(null);

        if (possuiOrgaoOrigem && validarSeExistemPatrimoniosNaMovimentacao(movimentacao)) {
            movimentacaoItemDataProvider.removerPorMovimentacao(movimentacao.getId());
        }
    }

    private void atualizarDadosNotaLancamentoContabil(EditarMovimentacaoEntreSetoresInputData inputData, Movimentacao definitiva) {
        NotaLancamentoContabil notaLancamentoContabil = definitiva.getNotaLancamentoContabil();

        if (validarNotaLancamentoContabilExistenteSemInformacoes(notaLancamentoContabil, inputData)) {
            notaLancamentoContabilDataProvider.remover(notaLancamentoContabil.getId());
            definitiva.setNotaLancamentoContabil(null);
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
        definitiva.setNotaLancamentoContabil(notaLancamentoContabilDataProvider.salvar(notaLancamentoContabil));
    }

    private boolean validarNotaLancamentoContabilExistenteSemInformacoes(NotaLancamentoContabil notaLancamentoContabil, EditarMovimentacaoEntreSetoresInputData inputData) {
        return Objects.nonNull(notaLancamentoContabil)
            && Objects.nonNull(notaLancamentoContabil.getId())
            && StringUtils.isEmpty(inputData.getNumeroNotaLancamentoContabil())
            && Objects.isNull(inputData.getDataNotaLancamentoContabil());
    }

    private boolean validarInformacoesNotaLancamentoContabilPreenchidas(NotaLancamentoContabil notaLancamentoContabil, EditarMovimentacaoEntreSetoresInputData inputData) {
        return Objects.nonNull(notaLancamentoContabil) ||
            StringUtils.isNotEmpty(inputData.getNumeroNotaLancamentoContabil()) ||
            Objects.nonNull(inputData.getDataNotaLancamentoContabil());
    }

    private Movimentacao salvar(Movimentacao definitiva) {
        return movimentacaoDataProvider.salvar(definitiva);
    }
}
