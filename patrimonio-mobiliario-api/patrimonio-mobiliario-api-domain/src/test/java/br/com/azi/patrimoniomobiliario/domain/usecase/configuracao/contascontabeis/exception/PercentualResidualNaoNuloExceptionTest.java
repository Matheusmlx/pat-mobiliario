package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contascontabeis.exception;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.PercentualResidualNaoNuloException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PercentualResidualNaoNuloExceptionTest {
    @Test
    public void deveEstenderDeRuntimeException() {
        Assert.assertEquals(RuntimeException.class, PercentualResidualNaoNuloException.class.getSuperclass());
    }

    @Test
    public void deveMostrarMensagem() {
        Assert.assertEquals("Percentual residual não pode ser inserido para conta do tipo não depreciável.", new PercentualResidualNaoNuloException().getMessage());
    }
}
