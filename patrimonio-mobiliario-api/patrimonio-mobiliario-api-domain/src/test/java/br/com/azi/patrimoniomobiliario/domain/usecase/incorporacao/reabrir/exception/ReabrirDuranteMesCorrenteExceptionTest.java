package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.reabrir.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReabrirDuranteMesCorrenteExceptionTest {

    private final ReabrirDuranteMesCorrenteException exception = new ReabrirDuranteMesCorrenteException();

    @Test
    public void deveEstenderRuntimeException() {
        Assert.assertEquals(RuntimeException.class, exception.getClass().getSuperclass());
    }

    @Test
    public void deveRetornarMensagem() {
        Assert.assertEquals("Não é possível reabrir incorporação fora do mês corrente", exception.getMessage());
    }
}
