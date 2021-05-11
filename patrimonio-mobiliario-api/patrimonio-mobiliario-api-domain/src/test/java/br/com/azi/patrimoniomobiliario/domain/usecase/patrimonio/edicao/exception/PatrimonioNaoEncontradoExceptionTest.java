package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PatrimonioNaoEncontradoExceptionTest {

    private static final String message = "Patrimônio não encontrado";

    @Test
    public void deveEstenderRuntimeException() {
        Assert.assertEquals(PatrimonioNaoEncontradoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        PatrimonioNaoEncontradoException patrimonioNaoEncontradoException = new PatrimonioNaoEncontradoException();
        Assert.assertEquals(patrimonioNaoEncontradoException.getMessage(), message);
    }
}
