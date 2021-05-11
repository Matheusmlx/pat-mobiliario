package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid.converter.BuscarIncorporacaoPorIdOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid.exception.IncorporacaoNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BuscarIncorporacaoPorIdUseCaseImpl implements BuscarIncorporacaoPorIdUseCase {

    private IncorporacaoDataProvider incorporacaoDataProvider;

    private BuscarIncorporacaoPorIdOutputDataConverter outputDataConverter;

    @Override
    public BuscarIncorporacaoPorIdOutputData executar(BuscarIncorporacaoPorIdInputData inputData) {
        validarDadosEntrada(inputData);
        Incorporacao incorporacaoEncontrada = buscar(inputData);
        return outputDataConverter.to(incorporacaoEncontrada);
    }

    private void validarDadosEntrada(BuscarIncorporacaoPorIdInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarIncorporacaoPorIdInputData::getId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private Incorporacao buscar(BuscarIncorporacaoPorIdInputData inputData) {
        Optional<Incorporacao> entidade = incorporacaoDataProvider.buscarPorId(inputData.getId());
        return entidade.orElseThrow(IncorporacaoNaoEncontradaException::new);
    }
}
