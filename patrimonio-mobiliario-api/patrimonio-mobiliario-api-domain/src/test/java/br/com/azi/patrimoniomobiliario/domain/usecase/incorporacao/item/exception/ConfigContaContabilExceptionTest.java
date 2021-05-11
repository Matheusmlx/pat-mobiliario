package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.exception;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception.ConfigContaContabilException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigContaContabilExceptionTest {

    @Test
    public void deveEstenderDeRuntimeException() {
        Assert.assertEquals(RuntimeException.class, ConfigContaContabilException.class.getSuperclass());
    }

    @Test
    public void deveMostrarMensagem() {
        Assert.assertEquals("Configure o tipo de conta contábil antes de usá-la em uma incorporação.", new ConfigContaContabilException().getMessage());
    }
}
