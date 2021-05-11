package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EditarReconhecimentoExceptionTest {

    private final String message = "Não foi possivel editar!";

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(EditarReconhecimentoException.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        EditarReconhecimentoException editarReconhecimentoException = new EditarReconhecimentoException();
        Assert.assertEquals(editarReconhecimentoException.getMessage(), this.message);
    }
}
