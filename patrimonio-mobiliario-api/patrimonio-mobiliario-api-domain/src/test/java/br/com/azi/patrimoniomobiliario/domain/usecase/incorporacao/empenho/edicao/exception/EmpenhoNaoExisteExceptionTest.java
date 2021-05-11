package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.exception;

import org.junit.Assert;
import org.junit.Test;

public class EmpenhoNaoExisteExceptionTest {
    @Test
    public void deveEstenderDeRuntimeException() {
        Assert.assertEquals(RuntimeException.class, EmpenhoNaoExisteException.class.getSuperclass());
    }

    @Test
    public void deveMostrarMensagem() {
        Assert.assertEquals("Empenho não existe", new EmpenhoNaoExisteException().getMessage());
    }
}
