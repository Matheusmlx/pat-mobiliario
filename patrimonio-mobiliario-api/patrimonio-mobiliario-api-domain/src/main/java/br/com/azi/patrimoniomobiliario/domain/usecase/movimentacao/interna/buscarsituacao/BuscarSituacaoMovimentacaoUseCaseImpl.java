package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarsituacao;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarsituacao.converter.BuscarSitucacaoMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarsituacao.exception.MovimentacaoNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BuscarSituacaoMovimentacaoUseCaseImpl implements BuscarSituacaoMovimentacaoUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;
    private final BuscarSitucacaoMovimentacaoOutputDataConverter outputDataConverter;

    @Override
    public BuscarSituacaoMovimentacaoOutputData executar(BuscarSituacaoMovimentacaoInputData inputData) {
        validarDadosEntrada(inputData);
        validarMovimentacaoExistente(inputData);
        Movimentacao movimentacao = buscarMovimentacao(inputData);

        return outputDataConverter.to(movimentacao);
    }

    private void validarDadosEntrada(BuscarSituacaoMovimentacaoInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarSituacaoMovimentacaoInputData::getMovimentacaoId, Objects::nonNull, "Id da movimentação é nulo")
            .get();
    }

    private void validarMovimentacaoExistente(BuscarSituacaoMovimentacaoInputData inputData) {
        if (!movimentacaoDataProvider.existe(inputData.getMovimentacaoId())) {
            throw new MovimentacaoNaoEncontradaException();
        }
    }

    private Movimentacao buscarMovimentacao(BuscarSituacaoMovimentacaoInputData inputData) {
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(inputData.getMovimentacaoId());
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }
}
