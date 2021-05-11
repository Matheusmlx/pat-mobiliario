package br.com.azi.patrimoniomobiliario.utils.string;

import br.com.azi.patrimoniomobiliario.utils.string.exception.CpfCnpjIncorretoException;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;

import java.util.Objects;

public class StringUtils {

    public static boolean verificaCPF(String cpf) {
        if (Objects.isNull(cpf)) {
            return false;
        }

        final CPFValidator cpfValidator = new CPFValidator();
        try {
            cpfValidator.assertValid(cpf);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean verificaCNPJ(String cnpj) {
        if (Objects.isNull(cnpj)) {
            return false;
        }

        CNPJValidator cnpjValidator = new CNPJValidator();
        try {
            cnpjValidator.assertValid(cnpj);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static TipoPessoaEnum verificaPessoa(String cpfCnpj) {
        if (StringUtils.verificaCPF(cpfCnpj)) {
            return TipoPessoaEnum.FISICA;
        } else if (StringUtils.verificaCNPJ(cpfCnpj)) {
            return TipoPessoaEnum.JURIDICA;
        }
        throw new CpfCnpjIncorretoException();
    }

    public static boolean isEmpty(String string) {
        return Objects.isNull(string) || string.isEmpty();
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    public static String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }

    public static String capitalize(String string) {
        return org.springframework.util.StringUtils.capitalize(string);
    }

}
