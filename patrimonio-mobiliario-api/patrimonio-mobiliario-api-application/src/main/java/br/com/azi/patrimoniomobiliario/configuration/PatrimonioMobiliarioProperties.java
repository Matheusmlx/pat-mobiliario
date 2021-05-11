package br.com.azi.patrimoniomobiliario.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "az.patrimonio-mobiliario")
public class PatrimonioMobiliarioProperties {
    Integracao integracao = new Integracao();
    Map<String, Object> rotulosPersonalizados;
    String quantidadeDigitosNumeroPatrimonio;
    Boolean bloquearValoresDivergentesAoFinalizar;
    Boolean patrimonioParaContaAlmoxarifado;
    String codigoContaContabilAlmoxarifado;
    String urlChatSuporte;
    Relatorio relatorio = new Relatorio();
    Long limiteRegistrosProcessamentoSincrono;
    String diaMensalDepreciacao;
    List<String> feriadosNacionais;
    List<String> feriadosLocais;
    Boolean depreciaEmAlmoxarifado;
    Boolean reservaPatrimonialGlobal;

    @Data
    public static class Relatorio {
        Header header = new Header();
        Footer footer = new Footer();

        @Data
        public static class Header {
            String titulo1;
            String titulo2;
        }

        @Data
        public static class Footer {
            String rodape1;
            String rodape2;
        }
    }

    @Data
    public static class Integracao {
        String sistemaDeArquivos;
        String sistemaDeGestaoAdministrativa;
        String sistemaDeFornecedores;

        Hal hal = new Hal();
        HalConfig halConfig = new HalConfig();
        Usuario usuario = new Usuario();
        Efornecedor efornecedor = new Efornecedor();

        @Data
        public static class Hal {
            String repository;
            String url;
        }

        @Data
        public static class HalConfig {
            String url;
        }

        @Data
        public static class Usuario {
            String login;
            String senha;
        }

        @Data
        public static class Efornecedor {
            String url;
        }
    }

}
