package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import br.com.azi.hal.core.generic.entity.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "TB_CONFIG_DEPRECIACAO", schema = "PAT_MOBILIARIO")
@SequenceGenerator(name = "SEQ_CONFIG_DEPRECIACAO", sequenceName = "PAT_MOBILIARIO.SEQ_CONFIG_DEPRECIACAO", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name = "dataCadastro", column = @Column(name = "CD_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "CD_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "CD_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "CD_USUARIO_ALTERACAO"))
})
public class ConfiguracaoDepreciacaoEntity extends BaseObject{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "PAT_MOBILIARIO.SEQ_CONFIG_DEPRECIACAO")
    @Column(name = "CD_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "CD_VIDA_UTIL_MESES")
    private Integer vidaUtil;

    @Column(name = "CD_PERCENTUAL_RESIDUAL")
    private BigDecimal percentualResidual;

    @Column(name = "CD_DEPRECIAVEL")
    private Boolean depreciavel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CC_ID")
    private ContaContabilEntity contaContabil;
}
