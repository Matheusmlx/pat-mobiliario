package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.edicao;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Responsavel;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.edicao.converter.EditarMovimentacaoTemporariaOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.edicao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.edicao.exception.MovimentacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.edicao.exception.SetorOrigemEDestinoSaoIguaisException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.edicao.exception.TipoDeMovimentacaoException;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import br.com.azi.patrimoniomobiliario.utils.string.StringUtils;
import br.com.azi.patrimoniomobiliario.utils.validate.NotaLancamentoContabilValidate;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class EditarMovimentacaoTemporariaUseCaseImpl implements EditarMovimentacaoTemporariaUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    private final EditarMovimentacaoTemporariaOutputDataConverter converter;

    @Override
    public EditarMovimentacaoTemporariaOutputData executar(EditarMovimentacaoTemporariaInputData inputData) {
        validarDadosEntrada(inputData);
        validarSeMovimentacaoExiste(inputData);

        final Movimentacao temporaria = buscarMovimentacao(inputData);

        validarSeSetorOrigemEDestinoSaoDiferentes(inputData);
        validarSeMovimentacaoEmElaboracao(temporaria);
        validarSeMovimentacaoDoTipoTemporaria(temporaria);

        atualizarDadosMovimentacao(inputData, temporaria);
        final Movimentacao temporariaAtualizada = salvar(temporaria);
        return converter.to(temporariaAtualizada);
    }

    private void validarDadosEntrada(EditarMovimentacaoTemporariaInputData inputData) {
        Validator.of(inputData)
            .validate(EditarMovimentacaoTemporariaInputData::getId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private void validarSeMovimentacaoExiste(EditarMovimentacaoTemporariaInputData inputData) {
        if (!movimentacaoDataProvider.existe(inputData.getId())) {
            throw new MovimentacaoNaoEncontradaException();
        }
    }

    private Movimentacao buscarMovimentacao(EditarMovimentacaoTemporariaInputData inputData) {
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(inputData.getId());
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void validarSeSetorOrigemEDestinoSaoDiferentes(EditarMovimentacaoTemporariaInputData inputData) {
        if (Objects.nonNull(inputData.getSetorOrigem()) && Objects.nonNull(inputData.getSetorDestino()) && inputData.getSetorOrigem().equals(inputData.getSetorDestino())) {
            throw new SetorOrigemEDestinoSaoIguaisException();
        }
    }

    private void validarSeMovimentacaoEmElaboracao(Movimentacao temporaria) {
        if (!Movimentacao.Situacao.EM_ELABORACAO.equals(temporaria.getSituacao())) {
            throw new MovimentacaoNaoEstaEmElaboracaoException();
        }
    }

    private void validarSeMovimentacaoDoTipoTemporaria(Movimentacao temporaria) {
        if (!TipoMovimentacaoEnum.TEMPORARIA.equals(temporaria.getTipo())) {
            throw new TipoDeMovimentacaoException("Movimentação não é temporária.");
        }
    }

    private void atualizarDadosMovimentacao(EditarMovimentacaoTemporariaInputData inputData, Movimentacao temporaria) {
        atualizarDadosOrigemDestino(inputData, temporaria);
        atualizarDadosNotaLancamentoContabil(inputData, temporaria);
        temporaria.setMotivoObservacao(inputData.getMotivoObservacao());
        temporaria.setNumeroProcesso(inputData.getNumeroProcesso());
        temporaria.setResponsavel(Responsavel.builder().id(inputData.getResponsavel()).build());
        if (Objects.nonNull(inputData.getDataDevolucao())) {
            temporaria.setDataDevolucao(DateUtils.asLocalDateTime(inputData.getDataDevolucao()));
        }
    }

    private boolean validarSeTrocouSetorOrigem(EditarMovimentacaoTemporariaInputData inputData, Movimentacao temporaria) {
        if (Objects.nonNull(inputData.getSetorOrigem())) {
            return Objects.isNull(temporaria.getSetorOrigem()) || (
                Objects.nonNull(temporaria.getSetorOrigem()) &&
                    !inputData.getSetorOrigem().equals(temporaria.getSetorOrigem().getId())
            );
        } else {
            return Objects.nonNull(temporaria.getSetorOrigem());
        }
    }

    private boolean validarSeExistemPatrimoniosNaMovimentacao(Movimentacao temporaria) {
        return movimentacaoItemDataProvider.existePorMovimentacaoId(temporaria.getId());
    }

    private void atualizarSetores(EditarMovimentacaoTemporariaInputData inputData, Movimentacao temporaria) {
        if (Objects.nonNull(inputData.getSetorOrigem())) {
            temporaria.setSetorOrigem(UnidadeOrganizacional.builder().id(inputData.getSetorOrigem()).build());
        } else {
            temporaria.setSetorOrigem(null);
        }

        if (Objects.nonNull(inputData.getSetorDestino())) {
            temporaria.setSetorDestino(UnidadeOrganizacional.builder().id(inputData.getSetorDestino()).build());
        } else {
            temporaria.setSetorDestino(null);
        }
    }

    private boolean validarSeUnidadesOrganizacionaisOrigemAlteradas(EditarMovimentacaoTemporariaInputData inputData, Movimentacao temporaria) {
        if (Objects.isNull(temporaria.getOrgaoOrigem()) || (
            Objects.nonNull(temporaria.getOrgaoOrigem()) &&
                !inputData.getOrgao().equals(temporaria.getOrgaoOrigem().getId())
        )) {
            return true;
        }
        return validarSeTrocouSetorOrigem(inputData, temporaria);
    }

    private void atualizarDadosOrigemDestino(EditarMovimentacaoTemporariaInputData inputData, Movimentacao temporaria) {
        if (Objects.nonNull(inputData.getOrgao())) {
            if (validarSeUnidadesOrganizacionaisOrigemAlteradas(inputData, temporaria)
                && movimentacaoItemDataProvider.existePorMovimentacaoId(temporaria.getId())) {
                movimentacaoItemDataProvider.removerPorMovimentacao(temporaria.getId());
            }

            temporaria.setOrgaoOrigem(UnidadeOrganizacional.builder().id(inputData.getOrgao()).build());
            temporaria.setOrgaoDestino(UnidadeOrganizacional.builder().id(inputData.getOrgao()).build());

            atualizarSetores(inputData, temporaria);
        } else {
            limparDadosMovimentacao(temporaria);
        }
    }

    private void limparDadosMovimentacao(Movimentacao temporaria) {
        final boolean possuiOrgaoOrigem = Objects.nonNull(temporaria.getOrgaoOrigem());

        temporaria.setOrgaoOrigem(null);
        temporaria.setOrgaoDestino(null);
        temporaria.setSetorOrigem(null);
        temporaria.setSetorDestino(null);

        if (possuiOrgaoOrigem && validarSeExistemPatrimoniosNaMovimentacao(temporaria)) {
            movimentacaoItemDataProvider.removerPorMovimentacao(temporaria.getId());
        }
    }

    private void atualizarDadosNotaLancamentoContabil(EditarMovimentacaoTemporariaInputData inputData, Movimentacao temporaria) {
        NotaLancamentoContabil notaLancamentoContabil = temporaria.getNotaLancamentoContabil();

        if (validarNotaLancamentoContabilExistenteSemInformacoes(notaLancamentoContabil, inputData)) {
            notaLancamentoContabilDataProvider.remover(notaLancamentoContabil.getId());
            temporaria.setNotaLancamentoContabil(null);
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
        temporaria.setNotaLancamentoContabil(notaLancamentoContabilDataProvider.salvar(notaLancamentoContabil));
    }

    private boolean validarNotaLancamentoContabilExistenteSemInformacoes(NotaLancamentoContabil notaLancamentoContabil, EditarMovimentacaoTemporariaInputData inputData) {
        return Objects.nonNull(notaLancamentoContabil)
            && Objects.nonNull(notaLancamentoContabil.getId())
            && StringUtils.isEmpty(inputData.getNumeroNotaLancamentoContabil())
            && Objects.isNull(inputData.getDataNotaLancamentoContabil());
    }

    private boolean validarInformacoesNotaLancamentoContabilPreenchidas(NotaLancamentoContabil notaLancamentoContabil, EditarMovimentacaoTemporariaInputData inputData) {
        return Objects.nonNull(notaLancamentoContabil) ||
            StringUtils.isNotEmpty(inputData.getNumeroNotaLancamentoContabil()) ||
            Objects.nonNull(inputData.getDataNotaLancamentoContabil());
    }

    private Movimentacao salvar(Movimentacao temporaria) {
        return movimentacaoDataProvider.salvar(temporaria);
    }
}
