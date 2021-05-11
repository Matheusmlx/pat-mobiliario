package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.editar;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.editar.converter.EditarVisualizacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.editar.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import br.com.azi.patrimoniomobiliario.utils.string.StringUtils;
import br.com.azi.patrimoniomobiliario.utils.validate.NotaLancamentoContabilValidate;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class EditarVisualizacaoUseCaseImpl implements EditarVisualizacaoUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    private final EditarVisualizacaoOutputDataConverter converter;

    @Override
    public EditarVisualizacaoOutputData executar(EditarVisualizacaoInputData inputData) {
        validarDadosEntrada(inputData);
        validarMovimentacaoExistente(inputData);

        final Movimentacao movimentacao = buscar(inputData);

        atualizarDadosNotaLancamentoContabil(inputData, movimentacao);
        final Movimentacao movimentacaoAtualizada = salvar(movimentacao);

        return converter.to(movimentacaoAtualizada);
    }

    private void validarDadosEntrada(EditarVisualizacaoInputData inputData) {
        Validator.of(inputData)
            .validate(EditarVisualizacaoInputData::getId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private void validarMovimentacaoExistente(EditarVisualizacaoInputData inputData) {
        if (!movimentacaoDataProvider.existe(inputData.getId())) {
            throw new MovimentacaoNaoEncontradaException();
        }
    }

    private Movimentacao buscar(EditarVisualizacaoInputData inputData) {
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(inputData.getId());
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void atualizarDadosNotaLancamentoContabil(EditarVisualizacaoInputData inputData, Movimentacao movimentacao) {
        NotaLancamentoContabil notaLancamentoContabil = movimentacao.getNotaLancamentoContabil();

        if (validarNotaLancamentoContabilExistenteSemInformacoes(notaLancamentoContabil, inputData)) {
            notaLancamentoContabilDataProvider.remover(notaLancamentoContabil.getId());
            movimentacao.setNotaLancamentoContabil(null);
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
        movimentacao.setNotaLancamentoContabil(notaLancamentoContabilDataProvider.salvar(notaLancamentoContabil));
    }

    private boolean validarNotaLancamentoContabilExistenteSemInformacoes(NotaLancamentoContabil notaLancamentoContabil, EditarVisualizacaoInputData inputData) {
        return Objects.nonNull(notaLancamentoContabil)
            && Objects.nonNull(notaLancamentoContabil.getId())
            && StringUtils.isEmpty(inputData.getNumeroNotaLancamentoContabil())
            && Objects.isNull(inputData.getDataNotaLancamentoContabil());
    }

    private boolean validarInformacoesNotaLancamentoContabilPreenchidas(NotaLancamentoContabil notaLancamentoContabil, EditarVisualizacaoInputData inputData) {
        return Objects.nonNull(notaLancamentoContabil) ||
            StringUtils.isNotEmpty(inputData.getNumeroNotaLancamentoContabil()) ||
            Objects.nonNull(inputData.getDataNotaLancamentoContabil());
    }

    private Movimentacao salvar(Movimentacao movimentacao) {
        return movimentacaoDataProvider.salvar(movimentacao);
    }
}
