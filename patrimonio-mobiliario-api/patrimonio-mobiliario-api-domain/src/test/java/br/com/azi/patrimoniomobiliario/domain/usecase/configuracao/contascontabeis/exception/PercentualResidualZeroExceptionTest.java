package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contascontabeis.exception;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.PercentualResidualZeroException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PercentualResidualZeroExceptionTest {
    @Test
    public void deveEstenderDeRuntimeException() {
        Assert.assertEquals(RuntimeException.class, PercentualResidualZeroException.class.getSuperclass());
    }

    @Test
    public void deveMostrarMensagem() {
        Assert.assertEquals("Percentual residual não deve ser menor ou igual a zero.", new PercentualResidualZeroException().getMessage());
    }
}
