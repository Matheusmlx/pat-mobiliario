package br.com.azi.patrimoniomobiliario.domain.integrarion.exception;

import br.com.azi.patrimoniomobiliario.domain.gateway.integration.exception.UsuarioNaoAutenticadoException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioNaoAutenticadoExceptionTest {
    private final String message = "EXCEPTION";

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(UsuarioNaoAutenticadoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        UsuarioNaoAutenticadoException usuarioNaoAutenticadoException = new UsuarioNaoAutenticadoException(this.message);
        Assert.assertEquals(usuarioNaoAutenticadoException.getMessage(), this.message);
    }

    @Test
    public void deveSetarMensagemECausa() {
        UsuarioNaoAutenticadoException usuarioNaoAutenticadoException = new UsuarioNaoAutenticadoException(this.message);
        Assert.assertEquals(usuarioNaoAutenticadoException.getMessage(), this.message);
    }
}
