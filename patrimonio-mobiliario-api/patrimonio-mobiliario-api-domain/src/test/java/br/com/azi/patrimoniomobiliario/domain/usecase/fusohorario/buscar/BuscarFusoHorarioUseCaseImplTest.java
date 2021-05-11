package br.com.azi.patrimoniomobiliario.domain.usecase.fusohorario.buscar;

import br.com.azi.patrimoniomobiliario.domain.usecase.fusohorario.buscar.converter.BuscarFusoHorarioOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;

import java.time.ZoneId;

public class BuscarFusoHorarioUseCaseImplTest {

    @Test
    public void deveRetornarFusoHorarioDefinido() {
        BuscarFusoHorarioOutputDataConverter outputDataConverter = new BuscarFusoHorarioOutputDataConverter();
        BuscarFusoHorarioUseCaseImpl useCase = new BuscarFusoHorarioUseCaseImpl(
                outputDataConverter
        );

        BuscarFusoHorarioOutputData outputData = useCase.executar();

        Assert.assertTrue(outputData.getFusoHorario() instanceof String);
        Assert.assertEquals(ZoneId.systemDefault().toString(), outputData.getFusoHorario());
    }
}

