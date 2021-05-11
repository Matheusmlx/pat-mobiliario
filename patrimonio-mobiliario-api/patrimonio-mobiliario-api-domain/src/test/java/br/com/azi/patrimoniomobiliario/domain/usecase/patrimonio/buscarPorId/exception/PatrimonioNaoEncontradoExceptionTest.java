package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarPorId.exception;

import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.exception.PatrimonioNaoEncontradoException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PatrimonioNaoEncontradoExceptionTest {

    @Test
    public void deveEstenderRuntimeException() {
        Assert.assertEquals(RuntimeException.class, PatrimonioNaoEncontradoException.class.getSuperclass());
    }

    @Test
    public void deveRetornarMensagem() {
        Assert.assertEquals("Patrimônio não encontrado.", (new PatrimonioNaoEncontradoException()).getMessage());
    }
}
