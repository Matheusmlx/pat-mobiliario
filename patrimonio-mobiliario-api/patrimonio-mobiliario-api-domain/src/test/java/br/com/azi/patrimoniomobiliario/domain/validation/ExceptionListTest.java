package br.com.azi.patrimoniomobiliario.domain.validation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ExceptionListTest{

    private final String message = "EXCEPTION";
    private final List<GenericValidationException> exceptions = new ArrayList<GenericValidationException>(2);

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(ExceptionList.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() throws Exception {
        ExceptionList exceptionList = new ExceptionList(this.message, this.exceptions);
        Assert.assertEquals(exceptionList.getMessage(), this.message);
    }

    @Test
    public void deveSetarExcepetionsNoConstrutor() throws Exception {
        exceptions.add(new GenericValidationException(this.message));
        ExceptionList exceptionList = new ExceptionList(this.message, this.exceptions);
        Assert.assertEquals(exceptionList.getExceptions().size(), 1);
    }

    @Test
    public void deveSetarExcepetionsPeloSetter() throws Exception {
        ExceptionList exceptionList = new ExceptionList(this.message, this.exceptions);
        Assert.assertEquals(exceptionList.getExceptions().size(), 0);
        exceptions.add(new GenericValidationException(this.message));
        exceptionList.setExceptions(exceptions);
        Assert.assertEquals(exceptionList.getExceptions().size(), 1);
    }
}
