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
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_DEPRECIACAO", schema = "PAT_MOBILIARIO")
@SequenceGenerator(name = "SEQ_DEPRECIACAO", sequenceName = "PAT_MOBILIARIO.SEQ_DEPRECIACAO", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name = "dataCadastro", column = @Column(name = "DE_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "DE_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "DE_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "DE_USUARIO_ALTERACAO"))
})
public class DepreciacaoEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_DEPRECIACAO")
    @Column(name = "DE_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "DE_VALOR_ANTERIOR", precision = 14, scale = 3)
    private BigDecimal valorAnterior;

    @Column(name = "DE_VALOR_SUBTRAIDO", precision = 14, scale = 3)
    private BigDecimal valorSubtraido;

    @Column(name = "DE_VALOR_POSTERIOR", precision = 14, scale = 3)
    private BigDecimal valorPosterior;

    @Column(name = "DE_TAXA_APLICADA", precision = 5, scale = 3)
    private BigDecimal taxaAplicada;

    @Column(name = "DE_DTHR_INICIAL", columnDefinition = "TIMESTAMP")
    private Date dataInicial;

    @Column(name = "DE_DTHR_FINAL", columnDefinition = "TIMESTAMP")
    private Date dataFinal;

    @Column(name = "DE_MES_REFERENCIA", length = 2)
    private String mesReferencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PA_ID")
    private PatrimonioEntity patrimonio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_ORGAO")
    private UnidadeOrganizacionalEntity orgao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_SETOR")
    private UnidadeOrganizacionalEntity setor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CC_ID")
    private ContaContabilEntity contaContabil;
}
