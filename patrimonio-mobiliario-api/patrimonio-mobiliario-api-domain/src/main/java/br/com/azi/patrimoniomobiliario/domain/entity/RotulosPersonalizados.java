package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RotulosPersonalizados {
    private I18n i18n;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class I18n {
        private TelaContaContabil contaContabil;
        private TelaConvenio convenio;
        private TelaConcedente concedente;
        private TelaReconhecimento reconhecimento;
        private TelaItemIncorporacaoDadosGerais telaItemIncorporacaoDadosGerais;
        private TelaIncorporacaoDadosGerais incorporacaoDadosGerais;
        private TelaItemIncorporacaoListagemPatrimonios itemIncorporacaoListagemPatrimonios;
        private TelaDocumentos documentos;
        private TelaFichaIndividualPatrimonio telaFichaIndividualPatrimonio;
        private TelaIncorporacaoEstorno incorporacaoEstorno;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TelaContaContabil {
        private CamposContaContabil campos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CamposContaContabil {
        private Campo codigoContabil;
        private Campo contaContabil;
        private Campo tipoBem;
        private Campo tipoConta;
        private Campo vidaUtil;
        private Campo residual;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TelaConvenio {
        private CamposConvenio campos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CamposConvenio {
        private Campo numero;
        private Campo nome;
        private Campo concedente;
        private Campo fonteRecurso;
        private Campo dataVigenciaInicio;
        private Campo dataVigenciaFim;
        private Campo situacao;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TelaConcedente {
        private CamposConcedente campos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CamposConcedente {
        private Campo cpfCnpj;
        private Campo nome;
        private Campo situacao;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TelaReconhecimento {
        private CamposReconhecimento campos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CamposReconhecimento {
        private Campo nome;
        private Campo situacao;
        private Campo execucaoOrcamentaria;
        private Campo camposObrigatorios;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TelaIncorporacaoDadosGerais {
        private CamposIncorporacaoDadosGerais campos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CamposIncorporacaoDadosGerais {
        private Campo reconhecimento;
        private Campo numProcesso;
        private Campo fornecedor;
        private Campo numeroEmpenho;
        private Campo dataEmpenho;
        private Campo valorEmpenho;
        private Campo dataRecebimento;
        private Campo nota;
        private Campo dataNota;
        private Campo valorNota;
        private Campo orgao;
        private Campo setor;
        private Campo origem;
        private Campo convenio;
        private Campo fundo;
        private Campo dataFinalizacao;
        private Campo codigo;
        private Campo situacao;
        private Campo projeto;
        private Campo numeroNotaLancamentoContabil;
        private Campo dataNotaLancamentoContabil;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TelaItemIncorporacaoListagemPatrimonios {
        private CamposItemIncorporacaoListagemPatrimonios campos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CamposItemIncorporacaoListagemPatrimonios{
        private Campo numero;
        private Campo valorAquisicao;
        private Campo numeroSerie;
        private Campo placa;
        private Campo renavam;
        private Campo licenciamento;
        private Campo motor;
        private Campo chassi;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TelaItemIncorporacaoDadosGerais{
        private CamposModalItemIncorporacaoDadosGerais campos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CamposModalItemIncorporacaoDadosGerais {
        private Campo marca;
        private Campo modelo;
        private Campo fabricante;
        private Campo quantidade;
        private Campo codigo;
        private Campo descricao;
        private Campo valorTotal;
        private Campo numeracaoPatrimonial;
        private Campo naturezaDespesa;
        private Campo contaContabil;
        private Campo estadoConservacao;
        private Campo tipoBem;
        private Campo uriImagem;
        private Campo anoFabricacaoModelo;
        private Campo combustivel;
        private Campo categoria;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TelaDocumentos {
        private CamposTelaDocumentos campos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CamposTelaDocumentos{
        private Campo numero;
        private Campo valor;
        private Campo tipo;
        private Campo data;
        private Campo uriAnexo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TelaFichaIndividualPatrimonio {
        private CamposTelaFichaIndividualPatrimonio campos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CamposTelaFichaIndividualPatrimonio {
        private Campo contaContabilAtual;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TelaIncorporacaoEstorno {
        private CamposTelaIncorporacaoEstorno campos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CamposTelaIncorporacaoEstorno {
        private Campo motivo;
        private Campo dataHora;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Campo {
        private String nome;
        private Boolean obrigatorio;
        private String tooltip;
    }
}
