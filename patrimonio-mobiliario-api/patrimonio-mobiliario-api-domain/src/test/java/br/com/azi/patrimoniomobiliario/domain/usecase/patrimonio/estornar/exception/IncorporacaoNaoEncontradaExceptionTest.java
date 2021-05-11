package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.estornar.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IncorporacaoNaoEncontradaExceptionTest {
    @Test
    public void deveEstenderRuntimeException() {
        Assert.assertEquals(RuntimeException.class, IncorporacaoNaoEncontradaException.class.getSuperclass());
    }

    @Test
    public void deveRetornarMensagem() {
        Assert.assertEquals("Incorporação não encontrada", (new IncorporacaoNaoEncontradaException()).getMessage());
    }
}
