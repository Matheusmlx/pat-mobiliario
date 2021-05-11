package br.com.azi.patrimoniomobiliario.utils.string;

import br.com.azi.patrimoniomobiliario.utils.string.exception.CpfCnpjIncorretoException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringUtilsTest {

    @RunWith(MockitoJUnitRunner.class)
    public static class VerificaCPF {
        @Test
        public void deveRetornarTrueQuandoCPFValido() {
            final String numeroDocumento = "46137564649";

            assertTrue(StringUtils.verificaCPF(numeroDocumento));
        }

        @Test
        public void deveRetornarFalseQuandoCPFInvalido() {
            final String numeroDocumento = "46136584649";

            assertFalse(StringUtils.verificaCPF(numeroDocumento));
        }

        @Test
        public void deveRetornarFalseQuandoCPFNull() {
            final String numeroDocumento = null;

            assertFalse(StringUtils.verificaCPF(numeroDocumento));
        }

        @Test
        public void deveRetornarFalseQuandoCPFVazio() {
            final String numeroDocumento = "";

            assertFalse(StringUtils.verificaCPF(numeroDocumento));
        }

        @Test
        public void deveRetornarFalseQuandoCPFPossuirLetras() {
            final String numeroDocumento = "46136A84649";

            assertFalse(StringUtils.verificaCPF(numeroDocumento));
        }

        @Test
        public void deveRetornarFalseQuandoCPFPossuiTamanhoErrado() {
            final String numeroDocumento = "46136A846499";

            assertFalse(StringUtils.verificaCPF(numeroDocumento));
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class VerificaCNPJ {
        @Test
        public void deveRetornarTrueQuandoCNPJValido() {
            final String numeroDocumento = "76477808000126";

            assertTrue(StringUtils.verificaCNPJ(numeroDocumento));
        }

        @Test
        public void deveRetornarFalseQuandoCNPJInvalido() {
            final String numeroDocumento = "7647780000126";

            assertFalse(StringUtils.verificaCNPJ(numeroDocumento));
        }

        @Test
        public void deveRetornarFalseQuandoCNPJNull() {
            final String numeroDocumento = null;

            assertFalse(StringUtils.verificaCNPJ(numeroDocumento));
        }

        @Test
        public void deveRetornarFalseQuandoCNPJVazio() {
            final String numeroDocumento = "";

            assertFalse(StringUtils.verificaCNPJ(numeroDocumento));
        }

        @Test
        public void deveRetornarFalseQuandoCNPJPossuirLetras() {
            final String numeroDocumento = "46136A84649";

            assertFalse(StringUtils.verificaCNPJ(numeroDocumento));
        }

        @Test
        public void deveRetornarFalseQuandoCNPJPossuiTamanhoErrado() {
            final String numeroDocumento = "46136A846499";

            assertFalse(StringUtils.verificaCNPJ(numeroDocumento));
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class VerificaPessoa {
        @Test
        public void deveRetornarPessoaFisicaQuandoReceberUmCPFComoParametro() {
            final String numeroDocumento = "46137564649";
            assertEquals(StringUtils.verificaPessoa(numeroDocumento), TipoPessoaEnum.FISICA);
        }

        @Test
        public void deveRetornarPessoaJuridicaQuandoReceberUmCNPJComoParametro() {
            final String numeroDocumento = "76477808000126";
            assertEquals(StringUtils.verificaPessoa(numeroDocumento), TipoPessoaEnum.JURIDICA);
        }

        @Test(expected = CpfCnpjIncorretoException.class)
        public void deveLancarExcecaoQuandoReceberNumeroDocumentoInvalido() {
            final String numeroDocumento = "461375646499";
            StringUtils.verificaPessoa(numeroDocumento);
        }

        @Test(expected = CpfCnpjIncorretoException.class)
        public void deveLancarExcecaoQuandoReceberNumeroDocumentoNulo() {
            final String numeroDocumento = null;
            StringUtils.verificaPessoa(numeroDocumento);
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class IsNotEmpty {
        @Test
        public void deveRetornarTrueQuandoAStringNaoForVazia() {
            final String string = "ABC";
            assertTrue(StringUtils.isNotEmpty(string));
        }

        @Test
        public void deveRetornarFalseQuandoAStringForVazia() {
            final String string = "";
            assertFalse(StringUtils.isNotEmpty(string));
        }

        @Test
        public void deveRetornarFalseQuandoAStringForNula() {
            final String string = null;
            assertFalse(StringUtils.isNotEmpty(string));
        }
    }
}
