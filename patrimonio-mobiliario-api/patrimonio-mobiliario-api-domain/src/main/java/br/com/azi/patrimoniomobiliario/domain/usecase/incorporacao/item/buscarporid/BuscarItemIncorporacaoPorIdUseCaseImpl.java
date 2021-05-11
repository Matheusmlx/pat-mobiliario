package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid.converter.BuscarItemIncorporacaoPorIdOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid.exception.ItemIncorporacaoNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid.exception.ItemNaoPertenceAIncorporacaoException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BuscarItemIncorporacaoPorIdUseCaseImpl implements BuscarItemIncorporacaoPorIdUseCase {

    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    private BuscarItemIncorporacaoPorIdOutputDataConverter outputDataConverter;

    @Override
    public BuscarItemIncorporacaoPorIdOutputData executar(BuscarItemIncorporacaoPorIdInputData inputData) {
        validarDadosEntrada(inputData);
        ItemIncorporacao itemIncorporacao = buscar(inputData);
        verificaSeItemPertenceIncorporacao(itemIncorporacao, inputData);

        return outputDataConverter.to(itemIncorporacao);
    }

    private void validarDadosEntrada(BuscarItemIncorporacaoPorIdInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarItemIncorporacaoPorIdInputData::getIdItem, Objects::nonNull, "O id é nulo")
            .validate(BuscarItemIncorporacaoPorIdInputData::getIdIncorporacao, Objects::nonNull, "O id da incorporação é nulo")
            .get();
    }

    private ItemIncorporacao buscar(BuscarItemIncorporacaoPorIdInputData inputData) {
        Optional<ItemIncorporacao> itemIncorporacao = itemIncorporacaoDataProvider.buscarPorId(inputData.getIdItem());
        return itemIncorporacao.orElseThrow(ItemIncorporacaoNaoEncontradoException::new);
    }

    private void verificaSeItemPertenceIncorporacao(ItemIncorporacao item, BuscarItemIncorporacaoPorIdInputData inputData) {
       if (! item.getIncorporacao().getId().equals(inputData.getIdIncorporacao())) {
            throw new ItemNaoPertenceAIncorporacaoException();
       }
    }
}
