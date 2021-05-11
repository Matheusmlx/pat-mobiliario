package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarporid;

import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConvenioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarporid.converter.BuscarConvenioPorIdOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarporid.exception.ConvenioNaoEncontradoException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BuscarConvenioPorIdUseCaseImpl implements BuscarConvenioPorIdUseCase{

    private ConvenioDataProvider convenioDataProvider;

    private BuscarConvenioPorIdOutputDataConverter outputDataConverter;

    @Override
    public BuscarConvenioPorIdOutputData executar(BuscarConvenioPorIdInputData inputData) {
        validarDadosEntrada(inputData);
        Convenio convenio = buscar(inputData);

        return this.outputDataConverter.to(convenio);
    }

    private void validarDadosEntrada(BuscarConvenioPorIdInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarConvenioPorIdInputData::getId, Objects::nonNull, "O id é nulo")
            .get();
    }

    private Convenio buscar(BuscarConvenioPorIdInputData inputData) {
        Optional<Convenio> convenio = convenioDataProvider.buscarPorId(inputData.getId());
        return convenio.orElseThrow(ConvenioNaoEncontradoException::new);
    }
}
