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
@Entity
@Table(name = "TB_LANCAMENTO_CONTABIL", schema = "PAT_MOBILIARIO")
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "SEQ_LANCAMENTO_CONTABIL", sequenceName = "PAT_MOBILIARIO.SEQ_LANCAMENTO_CONTABIL", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name = "dataCadastro", column = @Column(name = "LC_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "LC_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "LC_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "LC_USUARIO_ALTERACAO"))
})
public class LancamentoContabilEntity extends BaseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_LANCAMENTO_CONTABIL")
    @EqualsAndHashCode.Include
    @Column(name = "LC_ID")
    private Long id;

    @Column(name = "LC_TIPO_LANCAMENTO")
    private String tipoLancamento;

    @Column(name = "LC_VALOR")
    private BigDecimal valor;

    @Column(name = "LC_TIPO_MOVIMENTACAO")
    private String tipoMovimentacao;

    @Column(name = "LC_DATA_LANCAMENTO")
    private Date dataLancamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_ORGAO")
    private UnidadeOrganizacionalEntity orgao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_SETOR")
    private UnidadeOrganizacionalEntity setor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CC_ID")
    private ContaContabilEntity contaContabil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PA_ID")
    private PatrimonioEntity patrimonio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IN_ID")
    private IncorporacaoEntity incorporacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MO_ID")
    private MovimentacaoEntity movimentacao;
}
