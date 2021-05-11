package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QuantidadeExcedidaExceptionTest {

    @Test
    public void deveEstenderDeRuntimeException() {
        Assert.assertEquals(RuntimeException.class, QuantidadeExcedidaException.class.getSuperclass());
    }

    @Test
    public void deveMostrarMensagem() {
        Assert.assertEquals("Não é possível cadastrar mais que 10 empenhos!", new QuantidadeExcedidaException().getMessage());
    }
}
