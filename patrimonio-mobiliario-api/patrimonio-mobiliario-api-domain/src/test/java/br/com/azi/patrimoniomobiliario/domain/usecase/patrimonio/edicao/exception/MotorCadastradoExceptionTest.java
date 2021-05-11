package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MotorCadastradoExceptionTest {

    private static final String message = "Número do motor já cadastrado";

    @Test
    public void deveEstenderRuntimeException() {
        Assert.assertEquals(MotorCadastradoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        MotorCadastradoException motorCadastradoExceptionTest = new MotorCadastradoException();
        Assert.assertEquals(motorCadastradoExceptionTest.getMessage(), message);
    }
}
