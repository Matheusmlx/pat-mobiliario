package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarquantidadeitens;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarquantidadeitens.converter.BuscarQuantidadeItensMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarquantidadeitens.exception.MovimentacaoNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class BuscarQuantidadeItensMovimentacaoUseCaseImpl implements BuscarQuantidadeItensMovimentacaoUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final BuscarQuantidadeItensMovimentacaoOutputDataConverter converter;

    @Override
    public BuscarQuantidadeItensMovimentacaoOutputData executar(BuscarQuantidadeItensMovimentacaoInputData inputData) {
        validarDadosEntrada(inputData);
        validarMovimentacaoExistente(inputData);

        final Long quantidadeItens = movimentacaoItemDataProvider.buscarQuantidadeItensPorMovimentacaoId(inputData.getMovimentacaoId());

        return converter.to(inputData.getMovimentacaoId(), quantidadeItens);
    }

    private void validarDadosEntrada(BuscarQuantidadeItensMovimentacaoInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarQuantidadeItensMovimentacaoInputData::getMovimentacaoId, Objects::nonNull, "O id da movimentação é nulo.")
            .get();
    }

    private void validarMovimentacaoExistente(BuscarQuantidadeItensMovimentacaoInputData inputData) {
        if (!movimentacaoDataProvider.existe(inputData.getMovimentacaoId())) {
            throw new MovimentacaoNaoEncontradaException();
        }
    }
}
