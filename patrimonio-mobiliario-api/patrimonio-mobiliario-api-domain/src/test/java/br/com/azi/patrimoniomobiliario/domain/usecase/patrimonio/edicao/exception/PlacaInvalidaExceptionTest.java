package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PlacaInvalidaExceptionTest {

    private static final String message = "Placa inválida";

    @Test
    public void deveEstenderRuntimeException() {
        Assert.assertEquals(PlacaInvalidaException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        PlacaInvalidaException placaInvalidaException = new PlacaInvalidaException();
        Assert.assertEquals(placaInvalidaException.getMessage(), message);
    }
}
