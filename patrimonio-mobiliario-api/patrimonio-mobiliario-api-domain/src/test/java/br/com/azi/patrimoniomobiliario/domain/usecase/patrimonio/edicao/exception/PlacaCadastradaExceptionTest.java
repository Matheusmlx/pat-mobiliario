package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PlacaCadastradaExceptionTest {

    private static final String message = "Placa já cadastrada";

    @Test
    public void deveEstenderRuntimeException() {
        Assert.assertEquals(PlacaCadastradaException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        PlacaCadastradaException placaCadastradaException = new PlacaCadastradaException();
        Assert.assertEquals(placaCadastradaException.getMessage(), message);
    }
}
