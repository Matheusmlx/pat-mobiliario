package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenio.cadastro.exception;


import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.exception.ConvenioJaCadastradoException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConvenioJaCadastradoExceptionTest {
    private final String message = "Convênio já cadastrado!";

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(ConvenioJaCadastradoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        ConvenioJaCadastradoException convenioJaCadastradoException = new ConvenioJaCadastradoException();
        Assert.assertEquals(convenioJaCadastradoException.getMessage(), this.message);
    }
}
