package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RenavamInvalidoExceptionTest {

    private static final String message = "Renavam inválido";

    @Test
    public void deveEstenderRuntimeException() {
        Assert.assertEquals(RenavamInvalidoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        RenavamInvalidoException renavamInvalidoException = new RenavamInvalidoException();
        Assert.assertEquals(renavamInvalidoException.getMessage(), message);
    }
}
