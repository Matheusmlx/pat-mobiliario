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
@Table(name = "TB_EMPENHO" ,schema = "PAT_MOBILIARIO")
@SequenceGenerator(name = "SEQ_EMPENHO", sequenceName = "PAT_MOBILIARIO.SEQ_EMPENHO", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name ="dataCadastro" , column = @Column(name = "EM_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "EM_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "EM_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "EM_USUARIO_ALTERACAO")),
})
public class EmpenhoEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_EMPENHO")
    @Column(name = "EM_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "EM_NUMERO")
    private String numeroEmpenho;

    @Column(name = "EM_DTHR", columnDefinition = "TIMESTAMP")
    private Date dataEmpenho;

    @Column(name = "EM_VALOR")
    private BigDecimal valorEmpenho;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IN_ID")
    private IncorporacaoEntity incorporacao;
}
