package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarporid;

import br.com.azi.patrimoniomobiliario.domain.entity.Concedente;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConcedenteDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarporid.converter.BuscarConcedentePorIdOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarporid.exception.ConcedenteNaoEncontradoException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BuscarConcedentePorIdUseCaseImpl implements BuscarConcedentePorIdUseCase{
    private ConcedenteDataProvider concedenteDataProvider;

    private BuscarConcedentePorIdOutputDataConverter outputDataConverter;

    @Override
    public BuscarConcedentePorIdOutputData executar(BuscarConcedentePorIdInputData inputData) {
        validarDadosEntrada(inputData);
        Concedente concedente = buscar(inputData);

        return this.outputDataConverter.to(concedente);
    }

    private void validarDadosEntrada(BuscarConcedentePorIdInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarConcedentePorIdInputData::getId, Objects::nonNull, "O id é nulo")
            .get();
    }

    private Concedente buscar(BuscarConcedentePorIdInputData inputData) {
        Optional<Concedente> concedente = concedenteDataProvider.buscarPorId(inputData.getId());
        return concedente.orElseThrow(ConcedenteNaoEncontradoException::new);
    }
}
