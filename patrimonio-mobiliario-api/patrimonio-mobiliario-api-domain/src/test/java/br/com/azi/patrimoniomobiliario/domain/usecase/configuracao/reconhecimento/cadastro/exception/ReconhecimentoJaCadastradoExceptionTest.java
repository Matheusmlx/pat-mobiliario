package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.cadastro.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReconhecimentoJaCadastradoExceptionTest {

    private final String message = "Nome de reconhecimento já cadastrado!";

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(ReconhecimentoJaCadastradoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        ReconhecimentoJaCadastradoException reconhecimentoJaCadastradoException = new ReconhecimentoJaCadastradoException();
        Assert.assertEquals(reconhecimentoJaCadastradoException.getMessage(), this.message);
    }
}
