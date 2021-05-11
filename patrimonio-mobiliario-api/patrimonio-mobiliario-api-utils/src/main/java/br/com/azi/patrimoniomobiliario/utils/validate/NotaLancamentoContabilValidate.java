package br.com.azi.patrimoniomobiliario.utils.validate;

import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import br.com.azi.patrimoniomobiliario.utils.validate.exception.NotaLancamentoContabilComDataNoFuturoException;
import br.com.azi.patrimoniomobiliario.utils.validate.exception.NotaLancamentoContabilComNumeroDuplicadoException;
import br.com.azi.patrimoniomobiliario.utils.validate.exception.NotaLancamentoContabilComNumeroInvalidoException;

import java.time.LocalDateTime;
import java.util.Date;

public class NotaLancamentoContabilValidate {

    private static final String FORMATO_NUMERO_NOTA_LANCAMENTO = "[0-9]{4}NL[0-9]{6}";

    public static void validarFormatoNumeroNotaLancamentoContabil(String numero) {
        if (!numero.matches(FORMATO_NUMERO_NOTA_LANCAMENTO)) {
            throw new NotaLancamentoContabilComNumeroInvalidoException();
        }
    }

    public static void validarNumeroDuplicado(boolean possuiNumeroDuplicado) {
        if (possuiNumeroDuplicado) {
            throw new NotaLancamentoContabilComNumeroDuplicadoException();
        }
    }

    public static void validarDataNotaLancamentoContabil(Date dataNota) {
        LocalDateTime hoje = DateUtils.asLocalDateTime(new Date());
        if (DateUtils.asLocalDateTime(dataNota).isAfter(hoje)) {
            throw new NotaLancamentoContabilComDataNoFuturoException();
        }
    }
}
