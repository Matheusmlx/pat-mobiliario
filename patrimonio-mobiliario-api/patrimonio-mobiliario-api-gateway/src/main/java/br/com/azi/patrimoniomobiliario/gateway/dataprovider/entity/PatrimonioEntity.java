package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import br.com.azi.hal.core.generic.entity.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_PATRIMONIO" ,schema = "PAT_MOBILIARIO")
@SequenceGenerator(name = "SEQ_PATRIMONIO", sequenceName = "PAT_MOBILIARIO.SEQ_PATRIMONIO", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name ="dataCadastro" , column = @Column(name = "PA_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "PA_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "PA_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "PA_USUARIO_ALTERACAO")),
})
public class PatrimonioEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_PATRIMONIO")
    @Column(name = "PA_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "PA_PLACA")
    private String placa;

    @Column(name = "PA_RENAVAM")
    private String renavam;

    @Column(name = "PA_URI_IMAGEM", length = 500)
    private String uriImagem;

    @Column(name = "PA_LICENCIAMENTO")
    private String licenciamento;

    @Column(name = "PA_MOTOR")
    private String motor;

    @Column(name = "PA_SITUACAO")
    private String situacao;

    @Column(name = "PA_CHASSI")
    private String chassi;

    @Column(name = "PA_NUMERO")
    private String numero;

    @Column(name = "PA_NUMERO_SERIE")
    private String numeroSerie;

    @Column(name = "PA_VALOR_LIQUIDO")
    private BigDecimal valorLiquido;

    @Column(name = "PA_VALOR_ENTRADA")
    private BigDecimal valorEntrada;

    @Column(name = "PA_VALOR_RESIDUAL")
    private BigDecimal valorResidual;

    @Column(name = "PA_VALOR_AQUISICAO")
    private BigDecimal valorAquisicao;

    @Column(name = "PA_VALOR_DEPRECIACAO_MENSAL")
    private BigDecimal valorDepreciacaoMensal;

    @Column(name = "PA_DTHR_INICIO_VIDA_UTIL", columnDefinition = "TIMESTAMP")
    private Date inicioVidaUtil;

    @Column(name = "PA_DTHR_FIM_VIDA_UTIL", columnDefinition = "TIMESTAMP")
    private Date fimVidaUtil;

    @Column(name = "PA_DEPRECIAVEL")
    private Boolean depreciavel;

    @Column(name = "PA_DIFERENCA_DIZIMA")
    private Boolean diferencaDizima;

    @Column(name = "PA_MOTIVO_ESTORNO", length = 500)
    private String motivoEstorno;

    @Column(name = "PA_DTHR_ESTORNO", columnDefinition = "TIMESTAMP")
    private Date dataEstorno;

    @Column(name = "PA_USUARIO_ESTORNO")
    private String usuarioEstorno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_ORGAO")
    private UnidadeOrganizacionalEntity orgao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_SETOR")
    private UnidadeOrganizacionalEntity setor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_FUNDO")
    private UnidadeOrganizacionalEntity fundo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CO_ID")
    private ConvenioEntity convenio;

    @Column(name = "PA_PROJETO")
    private String projeto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ND_ID")
    private NaturezaDespesaEntity naturezaDespesa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EC_ID")
    private EstadoConservacaoEntity estadoConservacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CC_ID_CLASSIFICACAO")
    private ContaContabilEntity contaContabilClassificacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CC_ID_ATUAL")
    private ContaContabilEntity contaContabilAtual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INI_ID")
    private ItemIncorporacaoEntity itemIncorporacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COM_ID")
    private ComodanteEntity comodante;
}
