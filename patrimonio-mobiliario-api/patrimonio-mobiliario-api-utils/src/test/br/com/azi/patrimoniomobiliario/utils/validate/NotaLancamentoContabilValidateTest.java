package br.com.azi.patrimoniomobiliario.utils.validate;

import br.com.azi.patrimoniomobiliario.utils.validate.exception.NotaLancamentoContabilComDataNoFuturoException;
import br.com.azi.patrimoniomobiliario.utils.validate.exception.NotaLancamentoContabilComNumeroInvalidoException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.Date;

public class NotaLancamentoContabilValidateTest {

    @RunWith(MockitoJUnitRunner.class)
    public static class ValidarFormatoNumeroNotaLancamentoContabil {

        @Test
        public void deveValidarONumeroDaNotaLancamentoContabilQuandoForValido() {
            NotaLancamentoContabilValidate.validarFormatoNumeroNotaLancamentoContabil("2020NL123456");
        }

        @Test(expected = NotaLancamentoContabilComNumeroInvalidoException.class)
        public void deveLancarExcecaoQuandoONumeroDaNotaLancamentoContabilQuandoForInvalido() {
            NotaLancamentoContabilValidate.validarFormatoNumeroNotaLancamentoContabil("2020NA14");
        }

    }

    @RunWith(MockitoJUnitRunner.class)
    public static class ValidarDataNotaLancamentoContabil {
        @Test
        public void deveValidarADataDaNotaLancamentoContabilQuandoForMenorOuIgualADataAtual() {
            final Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DATE, (new Date()).getDate() - 1);

            final Date ontem = calendar.getTime();
            NotaLancamentoContabilValidate.validarDataNotaLancamentoContabil(ontem);
        }

        @Test(expected = NotaLancamentoContabilComDataNoFuturoException.class)
        public void deveLancarExcecaoQuandoADataDaNotaLancamentoContabilForUmaDataNoFuturo() {
            final Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DATE, (new Date()).getDate() + 1);

            final Date amanha = calendar.getTime();
            NotaLancamentoContabilValidate.validarDataNotaLancamentoContabil(amanha);
        }
    }
}
