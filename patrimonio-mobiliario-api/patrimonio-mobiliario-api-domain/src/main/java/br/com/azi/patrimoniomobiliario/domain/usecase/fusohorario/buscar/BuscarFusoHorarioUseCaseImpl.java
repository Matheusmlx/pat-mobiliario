package br.com.azi.patrimoniomobiliario.domain.usecase.fusohorario.buscar;

import br.com.azi.patrimoniomobiliario.domain.usecase.fusohorario.buscar.converter.BuscarFusoHorarioOutputDataConverter;
import lombok.AllArgsConstructor;

import java.time.ZoneId;

@AllArgsConstructor
public class BuscarFusoHorarioUseCaseImpl implements BuscarFusoHorarioUseCase {

    private BuscarFusoHorarioOutputDataConverter converter;

    @Override
    public BuscarFusoHorarioOutputData executar() {
        return converter.to(ZoneId.systemDefault().toString());
    }
}
