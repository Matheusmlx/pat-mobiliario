package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.exception;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception.ItemNaoEncontradoException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ItemNaoEncontradoExceptionTest {

    @Test
    public void deveEstenderDeRuntimeException() {
        Assert.assertEquals(RuntimeException.class, ItemNaoEncontradoException.class.getSuperclass());
    }

    @Test
    public void deveMostrarMensagem() {
        Assert.assertEquals("Item não encontrado!", new ItemNaoEncontradoException().getMessage());
    }
}
