package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patrimonio {
    private Long id;
    private String placa;
    private String renavam;
    private String uriImagem;
    private String imagem;
    private String licenciamento;
    private String motor;
    private String chassi;
    private String numero;
    private String numeroSerie;
    private Situacao situacao;
    private BigDecimal valorLiquido;
    private BigDecimal valorResidual;
    private BigDecimal valorDepreciacaoMensal;
    private BigDecimal valorAquisicao;
    private BigDecimal valorEntrada;
    private Integer prazoVidaUtil;
    private LocalDateTime inicioVidaUtil;
    private LocalDateTime fimVidaUtil;
    private Boolean depreciavel;
    private Boolean diferencaDizima;
    private UnidadeOrganizacional orgao;
    private UnidadeOrganizacional setor;
    private UnidadeOrganizacional fundo;
    private Convenio convenio;
    private String projeto;
    private Comodante comodante;
    private NaturezaDespesa naturezaDespesa;
    private EstadoConservacao estadoConservacao;
    private ContaContabil contaContabilClassificacao;
    private ContaContabil contaContabilAtual;
    private ItemIncorporacao itemIncorporacao;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAlteracao;
    private String usuarioCadastro;
    private String usuarioAlteracao;
    private String motivoEstorno;
    private LocalDateTime dataEstorno;
    private String usuarioEstorno;

    public enum Situacao {
        ATIVO,
        ESTORNADO
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Filtro extends FiltroBase {
        private Long incorporacaoId;
        private Long itemIncorporacaoId;
        private List<Long> itensIncorporacaoId;
        private String conteudo;
        private String situacao;
        private List<Long> orgaos;
        private List<Long> setores;
        private Long orgao;
        private Long setor;
        private Long numeroInicio;
        private Long numeroFim;
    }
}
