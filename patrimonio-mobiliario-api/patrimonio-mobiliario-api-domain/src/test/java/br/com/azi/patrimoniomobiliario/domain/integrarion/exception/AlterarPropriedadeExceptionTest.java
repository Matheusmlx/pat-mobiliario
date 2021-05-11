package br.com.azi.patrimoniomobiliario.domain.integrarion.exception;

import br.com.azi.patrimoniomobiliario.domain.gateway.integration.exception.AlterarPropriedadeException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AlterarPropriedadeExceptionTest {

    private final String message = "EXCEPTION";
    private final Throwable throwable = new Throwable();

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(AlterarPropriedadeException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        AlterarPropriedadeException alterarPropriedadeException = new AlterarPropriedadeException(this.message);
        Assert.assertEquals(alterarPropriedadeException.getMessage(), this.message);
    }

    @Test
    public void deveSetarMensagemECausa() {
        AlterarPropriedadeException alterarPropriedadeException = new AlterarPropriedadeException(this.message, this.throwable);
        Assert.assertEquals(alterarPropriedadeException.getMessage(), this.message);
        Assert.assertEquals(alterarPropriedadeException.getCause(), this.throwable);
    }
}
