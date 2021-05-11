package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NumeroSerieCadastradoExceptionTest {

    private static final String message = "Número série já cadastrado";

    @Test
    public void deveEstenderRuntimeException() {
        Assert.assertEquals(NumeroSerieCadastradoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        NumeroSerieCadastradoException numeroSerieCadastradoException = new NumeroSerieCadastradoException();
        Assert.assertEquals(numeroSerieCadastradoException.getMessage(), message);
    }
}
