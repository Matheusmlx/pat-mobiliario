package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.exception;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception.ContaContabilException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ContaContabilExceptionTest {

    @Test
    public void deveEstenderDeRuntimeException() {
        Assert.assertEquals(RuntimeException.class, ContaContabilException.class.getSuperclass());
    }

    @Test
    public void deveMostrarMensagem() {
        Assert.assertEquals("Não foi possível vincular conta contábil!", new ContaContabilException().getMessage());
    }
}
