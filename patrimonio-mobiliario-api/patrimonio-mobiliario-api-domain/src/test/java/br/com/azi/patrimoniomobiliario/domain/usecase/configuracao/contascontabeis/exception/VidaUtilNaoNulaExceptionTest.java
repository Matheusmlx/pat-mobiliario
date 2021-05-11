package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contascontabeis.exception;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.VidaUtilNaoNulaException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VidaUtilNaoNulaExceptionTest {
    @Test
    public void deveEstenderDeRuntimeException() {
        Assert.assertEquals(RuntimeException.class, VidaUtilNaoNulaException.class.getSuperclass());
    }

    @Test
    public void deveMostrarMensagem() {
        Assert.assertEquals("Vida útil não pode ser inserida para conta do tipo não depreciável.", new VidaUtilNaoNulaException().getMessage());
    }
}
