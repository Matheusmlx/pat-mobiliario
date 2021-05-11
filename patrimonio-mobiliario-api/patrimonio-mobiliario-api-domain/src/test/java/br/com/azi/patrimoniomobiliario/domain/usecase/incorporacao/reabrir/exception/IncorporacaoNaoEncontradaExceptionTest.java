package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.reabrir.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IncorporacaoNaoEncontradaExceptionTest {

    private final IncorporacaoNaoEncontradaException exception = new IncorporacaoNaoEncontradaException();

    @Test
    public void deveEstenderRuntimeException() {
        Assert.assertEquals(RuntimeException.class, exception.getClass().getSuperclass());
    }

    @Test
    public void deveRetornarMensagem() {
        Assert.assertEquals("Incorporação não encontrada!", exception.getMessage());
    }
}
