package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EmpenhoNaoEncontradoExceptionTest {
    @Test
    public void deveEstenderDeRuntimeException() {
        Assert.assertEquals(RuntimeException.class, EmpenhoNaoEncontradoException.class.getSuperclass());
    }

    @Test
    public void deveMostrarMensagem() {
        Assert.assertEquals("Empenho não encontrado", new EmpenhoNaoEncontradoException().getMessage());
    }
}
