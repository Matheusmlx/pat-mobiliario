package br.com.azi.patrimoniomobiliario.domain.integrarion.exception;

import br.com.azi.patrimoniomobiliario.domain.gateway.integration.exception.UploadArquivoException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UploadArquivoExceptionTest{

    private final String message = "EXCEPTION";
    private final Throwable throwable = new Throwable();

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(UploadArquivoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        UploadArquivoException uploadArquivoException = new UploadArquivoException(this.message);
        Assert.assertEquals(uploadArquivoException.getMessage(), this.message);
    }

    @Test
    public void deveSetarMensagemECausa() {
        UploadArquivoException uploadArquivoException = new UploadArquivoException(this.message, this.throwable);
        Assert.assertEquals(uploadArquivoException.getMessage(), this.message);
        Assert.assertEquals(uploadArquivoException.getCause(), this.throwable);
    }
}
