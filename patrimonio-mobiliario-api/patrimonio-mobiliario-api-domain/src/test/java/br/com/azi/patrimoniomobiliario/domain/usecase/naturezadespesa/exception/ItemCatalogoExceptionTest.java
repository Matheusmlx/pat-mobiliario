package br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.exception;

import br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.exception.ItemCatalogoException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ItemCatalogoExceptionTest {

    @Test
    public void deveEstenderDeRuntimeException() {
        Assert.assertEquals(RuntimeException.class, ItemCatalogoException.class.getSuperclass());
    }

    @Test
    public void deveMostrarMensagem() {
        Assert.assertEquals("Não foi possível encontar o item catálogo!", new ItemCatalogoException().getMessage());
    }
}


