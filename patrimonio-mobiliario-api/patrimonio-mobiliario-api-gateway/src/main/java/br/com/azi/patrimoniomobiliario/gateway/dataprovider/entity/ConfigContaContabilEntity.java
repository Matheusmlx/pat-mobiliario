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


@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_CONFIG_CONTA_CONTABIL", schema = "PAT_MOBILIARIO")
@SequenceGenerator(name = "SEQ_CONFIG_CONTA_CONTABIL", sequenceName = "PAT_MOBILIARIO.SEQ_CONFIG_CONTA_CONTABIL", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name = "dataCadastro", column = @Column(name = "CCC_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "CCC_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "CCC_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "CCC_USUARIO_ALTERACAO"))
})
public class ConfigContaContabilEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "PAT_MOBILIARIO.SEQ_CONFIG_CONTA_CONTABIL")
    @Column(name = "CCC_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "CCC_METODO")
    private String metodo;

    @Column(name = "CCC_TIPO")
    private String tipo;

    @Column(name = "CCC_TIPO_BEM")
    private String tipoBem;

    @Column(name = "CCC_VIDA_UTIL_MESES")
    private Integer vidaUtil;

    @Column(name = "CCC_PERCENTUAL_RESIDUAL")
    private BigDecimal percentualResidual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CC_ID")
    private ContaContabilEntity contaContabil;

}
