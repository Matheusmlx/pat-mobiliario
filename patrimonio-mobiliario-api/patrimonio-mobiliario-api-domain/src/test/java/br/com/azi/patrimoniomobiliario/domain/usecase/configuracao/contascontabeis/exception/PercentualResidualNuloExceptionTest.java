package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contascontabeis.exception;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.PercentualResidualNuloException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PercentualResidualNuloExceptionTest {
    @Test
    public void deveEstenderDeRuntimeException() {
        Assert.assertEquals(RuntimeException.class, PercentualResidualNuloException.class.getSuperclass());
    }

    @Test
    public void deveMostrarMensagem() {
        Assert.assertEquals("Percentual residual não pode ser nulo para conta do tipo depreciável.", new PercentualResidualNuloException().getMessage());
    }
}
