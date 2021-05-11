package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ChassiCadastradoExceptionTest {

    private static final String message = "Número de chassi já cadastrado";

    @Test
    public void deveEstenderRuntimeException() {
        Assert.assertEquals(ChassiCadastradoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
         ChassiCadastradoException chassiCadastradoException = new ChassiCadastradoException();
        Assert.assertEquals(chassiCadastradoException.getMessage(), message);
    }
}
