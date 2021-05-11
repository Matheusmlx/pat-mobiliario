package br.com.azi.patrimoniomobiliario.domain.validation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GenericValidationExceptionTest{

    private final String message = "EXCEPTION";
    private final List<String> args = new ArrayList<String>();

    @Test
    public void deveExtenderRuntimException() {
        Assert.assertEquals(ExceptionList.class.getSuperclass().getName(), RuntimeException.class.getName());
    }

    @Test
    public void deveSetarMensagem() {
        GenericValidationException genericValidationException = new GenericValidationException(this.message);
        Assert.assertEquals(genericValidationException.getMessage(), this.message);
    }

    @Test
    public void deveSetarArgs() {
        GenericValidationException genericValidationException = new GenericValidationException(this.message);
        args.add("arg");
        genericValidationException.setArgs(args);
        Assert.assertEquals(args, genericValidationException.getArgs());
    }
}
