package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarporid;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarporid.converter.BuscarMovimentacaoPorIdOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarporid.exception.MovimentacaoNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BuscarMovimentacaoPorIdUseCaseImpl implements BuscarMovimentacaoPorIdUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final BuscarMovimentacaoPorIdOutputDataConverter outputDataConverter;

    @Override
    public BuscarMovimentacaoPorIdOutputData executar(BuscarMovimentacaoPorIdInputData inputData) {
        validarDadosEntrada(inputData);
        validarMovimentacaoExistente(inputData.getId());
        Movimentacao movimentacao = buscarMovimentacao(inputData);
        return outputDataConverter.to(movimentacao);
    }

    private void validarDadosEntrada(BuscarMovimentacaoPorIdInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarMovimentacaoPorIdInputData::getId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private void validarMovimentacaoExistente(Long id) {
        if (!movimentacaoDataProvider.existe(id)) {
            throw new MovimentacaoNaoEncontradaException();
        }
    }

    private Movimentacao buscarMovimentacao(BuscarMovimentacaoPorIdInputData inputData) {
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(inputData.getId());
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }
}
