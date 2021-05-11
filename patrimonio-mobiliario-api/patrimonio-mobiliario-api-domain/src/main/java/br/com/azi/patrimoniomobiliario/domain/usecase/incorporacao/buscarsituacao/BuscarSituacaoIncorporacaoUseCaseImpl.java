package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarsituacao;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarsituacao.converter.BuscarSituacaoIncorporacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarsituacao.exception.IncorporacaoNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BuscarSituacaoIncorporacaoUseCaseImpl implements BuscarSituacaoIncorporacaoUseCase {

    private IncorporacaoDataProvider incorporacaoDataProvider;

    private BuscarSituacaoIncorporacaoOutputDataConverter outputDataConverter;

    @Override
    public BuscarSituacaoIncorporacaoOutputData executar(BuscarSituacaoIncorporacaoInputData inputData) {
        validarDadosEntrada(inputData);
        Incorporacao incorporacaoEncontrada = buscar(inputData);
        return outputDataConverter.to(incorporacaoEncontrada);
    }

    private void validarDadosEntrada(BuscarSituacaoIncorporacaoInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarSituacaoIncorporacaoInputData::getIncorporacaoId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private Incorporacao buscar(BuscarSituacaoIncorporacaoInputData inputData) {
        Optional<Incorporacao> entidade = incorporacaoDataProvider.buscarPorId(inputData.getIncorporacaoId());
        return entidade.orElseThrow(IncorporacaoNaoEncontradaException::new);
    }
}
