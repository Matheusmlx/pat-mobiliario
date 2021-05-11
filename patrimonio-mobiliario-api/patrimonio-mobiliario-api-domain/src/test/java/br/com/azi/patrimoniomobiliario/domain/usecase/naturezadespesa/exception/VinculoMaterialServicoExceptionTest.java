package br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.exception;

import br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.exception.VinculoMaterialServicoException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VinculoMaterialServicoExceptionTest {

    @Test
    public void deveEstenderDeRuntimeException() {
        Assert.assertEquals(RuntimeException.class, VinculoMaterialServicoException.class.getSuperclass());
    }

    @Test
    public void deveMostrarMensagem() {
        Assert.assertEquals("Registro de material serviço não encontrado!", new VinculoMaterialServicoException().getMessage());
    }
}
