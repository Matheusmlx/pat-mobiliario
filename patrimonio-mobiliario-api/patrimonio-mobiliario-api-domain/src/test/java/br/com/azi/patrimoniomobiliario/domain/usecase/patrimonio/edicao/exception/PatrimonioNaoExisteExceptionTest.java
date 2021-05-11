package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PatrimonioNaoExisteExceptionTest {

    private static final String message = "Patrimônio não existe";

    @Test
    public void deveEstenderRuntimeException() {
        Assert.assertEquals(PatrimonioNaoExisteException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        PatrimonioNaoExisteException patrimonioNaoExisteException = new PatrimonioNaoExisteException();
        Assert.assertEquals(patrimonioNaoExisteException.getMessage(), message);
    }
}
