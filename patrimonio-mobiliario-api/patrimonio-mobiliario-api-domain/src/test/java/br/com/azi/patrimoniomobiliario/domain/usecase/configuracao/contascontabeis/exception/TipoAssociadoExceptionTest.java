package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contascontabeis.exception;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.TipoAssociadoException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TipoAssociadoExceptionTest {
    @Test
    public void deveEstenderDeRuntimeException() {
        Assert.assertEquals(RuntimeException.class, TipoAssociadoException.class.getSuperclass());
    }

    @Test
    public void deveMostrarMensagem() {
        Assert.assertEquals("Tipo de bem associado inválido!", new TipoAssociadoException().getMessage());
    }
}
