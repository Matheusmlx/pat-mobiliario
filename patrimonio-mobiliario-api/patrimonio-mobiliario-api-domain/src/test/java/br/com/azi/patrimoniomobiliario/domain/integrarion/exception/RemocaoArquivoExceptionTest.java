package br.com.azi.patrimoniomobiliario.domain.integrarion.exception;

import br.com.azi.patrimoniomobiliario.domain.gateway.integration.exception.RemocaoArquivoException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RemocaoArquivoExceptionTest {

    private final String message = "EXCEPTION";
    private final Throwable throwable = new Throwable();

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(RemocaoArquivoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        RemocaoArquivoException remocaoArquivoException = new RemocaoArquivoException(this.message);
        Assert.assertEquals(remocaoArquivoException.getMessage(), this.message);
    }

    @Test
    public void deveSetarMensagemECausa() {
        RemocaoArquivoException remocaoArquivoException = new RemocaoArquivoException(this.message, this.throwable);
        Assert.assertEquals(remocaoArquivoException.getMessage(), this.message);
        Assert.assertEquals(remocaoArquivoException.getCause(), this.throwable);
    }
}
