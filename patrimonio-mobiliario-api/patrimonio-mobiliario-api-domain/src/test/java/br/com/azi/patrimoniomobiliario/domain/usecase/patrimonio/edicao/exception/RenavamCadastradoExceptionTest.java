package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RenavamCadastradoExceptionTest {

    private static final String message = "Renavam já cadastrado";

    @Test
    public void deveEstenderRuntimeException() {
        Assert.assertEquals(RenavamCadastradoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        RenavamCadastradoException renavamCadastradoException = new RenavamCadastradoException();
        Assert.assertEquals(renavamCadastradoException.getMessage(), message);
    }
}
