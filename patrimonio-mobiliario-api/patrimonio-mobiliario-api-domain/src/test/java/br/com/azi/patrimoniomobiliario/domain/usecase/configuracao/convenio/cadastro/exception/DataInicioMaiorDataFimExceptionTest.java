package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenio.cadastro.exception;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.exception.DataInicioMaiorDataFimException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataInicioMaiorDataFimExceptionTest {
    private final String message = "Data Inicial maior que data final do convênio!";

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(DataInicioMaiorDataFimException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        DataInicioMaiorDataFimException dataInicioMaiorDataFimException = new DataInicioMaiorDataFimException();
        Assert.assertEquals(dataInicioMaiorDataFimException.getMessage(), this.message);
    }
}
