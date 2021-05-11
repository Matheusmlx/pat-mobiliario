package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ChassiInvalidoExceptionTest {

    private static final String message = "Numeração do chassi inválida";

    @Test
    public void deveEstenderRuntimeException() {
        Assert.assertEquals(ChassiInvalidoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        ChassiInvalidoException chassiInvalidoException = new ChassiInvalidoException();
        Assert.assertEquals(chassiInvalidoException.getMessage(), message);
    }
}
