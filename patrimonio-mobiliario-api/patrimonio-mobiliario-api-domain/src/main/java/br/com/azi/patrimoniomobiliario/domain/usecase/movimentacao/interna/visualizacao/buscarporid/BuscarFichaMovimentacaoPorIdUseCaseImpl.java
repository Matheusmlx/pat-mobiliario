package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.buscarporid;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.buscarporid.converter.BuscarFichaMovimentacaoPorIdOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.buscarporid.exception.MovimentacaoNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class BuscarFichaMovimentacaoPorIdUseCaseImpl implements BuscarFichaMovimentacaoPorIdUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final BuscarFichaMovimentacaoPorIdOutputDataConverter outputDataConverter;

    @Override
    public BuscarFichaMovimentacaoPorIdOutputData executar(BuscarFichaMovimentacaoPorIdInputData inputData) {
        validarDadosEntrada(inputData);

        Movimentacao movimentacao = buscarMovimentacao(inputData);

        return outputDataConverter.to(movimentacao);
    }

    private void validarDadosEntrada(BuscarFichaMovimentacaoPorIdInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarFichaMovimentacaoPorIdInputData::getIdMovimentacao, Objects::nonNull, "O Id é nulo")
            .get();
    }

    private Movimentacao buscarMovimentacao(BuscarFichaMovimentacaoPorIdInputData inputData) {
        return movimentacaoDataProvider.buscarPorId(inputData.getIdMovimentacao())
            .orElseThrow(MovimentacaoNaoEncontradaException::new);
    }
}
