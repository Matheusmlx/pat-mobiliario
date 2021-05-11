package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Responsavel;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.converter.EditarMovimentacaoEntreEstoquesOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.exception.MovimentacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.exception.SetorOrigemEDestinoSaoIguaisException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.exception.TipoDeMovimentacaoException;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import br.com.azi.patrimoniomobiliario.utils.string.StringUtils;
import br.com.azi.patrimoniomobiliario.utils.validate.NotaLancamentoContabilValidate;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class EditarMovimentacaoEntreEstoquesUseCaseImpl implements EditarMovimentacaoEntreEstoquesUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    private final EditarMovimentacaoEntreEstoquesOutputDataConverter converter;

    @Override
    public EditarMovimentacaoEntreEstoquesOutputData executar(EditarMovimentacaoEntreEstoquesInputData inputData) {
        validarDadosEntrada(inputData);
        validarSeMovimentacaoExiste(inputData);

        Movimentacao movimentacao = buscarMovimentacao(inputData);

        validarSeSetorOrigemEDestinoSaoDiferentes(inputData);
        validarSeMovimentacaoEmElaboracao(movimentacao);
        validarSeMovimentacaoDoTipoEntreEstoques(movimentacao);

        atualizarDadosMovimentacao(inputData, movimentacao);
        Movimentacao entreEstoquesAtualizado = salvar(movimentacao);
        return converter.to(entreEstoquesAtualizado);
    }

    private void validarDadosEntrada(EditarMovimentacaoEntreEstoquesInputData inputData) {
        Validator.of(inputData)
            .validate(EditarMovimentacaoEntreEstoquesInputData::getId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private void validarSeMovimentacaoExiste(EditarMovimentacaoEntreEstoquesInputData inputData) {
        if (!movimentacaoDataProvider.existe(inputData.getId())) {
            throw new MovimentacaoNaoEncontradaException();
        }
    }

    private Movimentacao buscarMovimentacao(EditarMovimentacaoEntreEstoquesInputData inputData) {
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(inputData.getId());
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void validarSeSetorOrigemEDestinoSaoDiferentes(EditarMovimentacaoEntreEstoquesInputData inputData) {
        if (Objects.nonNull(inputData.getSetorOrigem()) && Objects.nonNull(inputData.getSetorDestino()) && inputData.getSetorOrigem().equals(inputData.getSetorDestino())) {
            throw new SetorOrigemEDestinoSaoIguaisException();
        }
    }

    private void validarSeMovimentacaoEmElaboracao(Movimentacao entreEstoques) {
        if (!Movimentacao.Situacao.EM_ELABORACAO.equals(entreEstoques.getSituacao())) {
            throw new MovimentacaoNaoEstaEmElaboracaoException();
        }
    }

    private void validarSeMovimentacaoDoTipoEntreEstoques(Movimentacao movimentacao) {
        if (!TipoMovimentacaoEnum.ENTRE_ESTOQUES.equals(movimentacao.getTipo())) {
            throw new TipoDeMovimentacaoException("Movimentação não é entre estoques.");
        }
    }

    private void atualizarDadosMovimentacao(EditarMovimentacaoEntreEstoquesInputData inputData, Movimentacao movimentacao) {
        atualizarDadosOrigemDestino(inputData, movimentacao);
        atualizarDadosNotaLancamentoContabil(inputData, movimentacao);
        movimentacao.setMotivoObservacao(inputData.getMotivoObservacao());
        movimentacao.setNumeroProcesso(inputData.getNumeroProcesso());
        movimentacao.setResponsavel(Responsavel.builder().id(inputData.getResponsavel()).build());
    }

    private boolean validarSeTrocouSetorOrigem(EditarMovimentacaoEntreEstoquesInputData inputData, Movimentacao movimentacao) {
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

    private void atualizarSetores(EditarMovimentacaoEntreEstoquesInputData inputData, Movimentacao movimentacao) {
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

    private boolean validarSeUnidadesOrganizacionaisOrigemAlteradas(EditarMovimentacaoEntreEstoquesInputData inputData, Movimentacao movimentacao) {
            if (Objects.isNull(movimentacao.getOrgaoOrigem()) || (
                Objects.nonNull(movimentacao.getOrgaoOrigem()) &&
                    !inputData.getOrgao().equals(movimentacao.getOrgaoOrigem().getId())
            )) {
                return true;
            }
            return validarSeTrocouSetorOrigem(inputData, movimentacao);
    }

    private void atualizarDadosOrigemDestino(EditarMovimentacaoEntreEstoquesInputData inputData, Movimentacao movimentacao) {
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

    private void atualizarDadosNotaLancamentoContabil(EditarMovimentacaoEntreEstoquesInputData inputData, Movimentacao entreEstoques) {
        NotaLancamentoContabil notaLancamentoContabil = entreEstoques.getNotaLancamentoContabil();

        if (validarNotaLancamentoContabilExistenteSemInformacoes(notaLancamentoContabil, inputData)) {
            notaLancamentoContabilDataProvider.remover(notaLancamentoContabil.getId());
            entreEstoques.setNotaLancamentoContabil(null);
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
        entreEstoques.setNotaLancamentoContabil(notaLancamentoContabilDataProvider.salvar(notaLancamentoContabil));
    }

    private boolean validarNotaLancamentoContabilExistenteSemInformacoes(NotaLancamentoContabil notaLancamentoContabil, EditarMovimentacaoEntreEstoquesInputData inputData) {
        return Objects.nonNull(notaLancamentoContabil)
            && Objects.nonNull(notaLancamentoContabil.getId())
            && StringUtils.isEmpty(inputData.getNumeroNotaLancamentoContabil())
            && Objects.isNull(inputData.getDataNotaLancamentoContabil());
    }

    private boolean validarInformacoesNotaLancamentoContabilPreenchidas(NotaLancamentoContabil notaLancamentoContabil, EditarMovimentacaoEntreEstoquesInputData inputData) {
        return Objects.nonNull(notaLancamentoContabil) ||
            StringUtils.isNotEmpty(inputData.getNumeroNotaLancamentoContabil()) ||
            Objects.nonNull(inputData.getDataNotaLancamentoContabil());
    }

    private Movimentacao salvar(Movimentacao entreEstoques) {
        return movimentacaoDataProvider.salvar(entreEstoques);
    }
}
