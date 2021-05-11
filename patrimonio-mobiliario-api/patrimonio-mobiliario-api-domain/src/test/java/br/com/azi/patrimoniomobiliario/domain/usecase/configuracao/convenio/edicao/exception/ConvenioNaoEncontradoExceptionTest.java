package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenio.edicao.exception;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.exception.ConvenioNaoEncontradoException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConvenioNaoEncontradoExceptionTest {
    private final String message = "Convênio não encontrado!";

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(ConvenioNaoEncontradoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        ConvenioNaoEncontradoException convenioNaoEncontradoException = new ConvenioNaoEncontradoException();
        Assert.assertEquals(convenioNaoEncontradoException.getMessage(), this.message);
    }
}
