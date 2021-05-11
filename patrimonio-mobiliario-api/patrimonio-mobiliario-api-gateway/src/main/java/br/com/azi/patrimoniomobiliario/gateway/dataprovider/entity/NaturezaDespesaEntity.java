package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_NATUREZA_DESPESA", schema = "COMUM_SIGA")
@AttributeOverrides({
    @AttributeOverride(name = "dataCadastro", column = @Column(name = "ND_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "ND_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "ND_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "ND_USUARIO_ALTERACAO"))
})
public class NaturezaDespesaEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "ND_ID", insertable = false, updatable = false)
    private Long id;

    @Column(name = "ND_DESPESA", insertable = false, updatable = false)
    private String despesa;

    @Column(name = "ND_DESCRICAO", insertable = false, updatable = false)
    private String descricao;

    @Column(name = "ND_JUSTIFICATIVA", insertable = false, updatable = false)
    private String justificativa;

    @Column(name = "ND_SITUACAO", insertable = false, updatable = false)
    private String situacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ES_ID", insertable = false, updatable = false)
    private ElementoSubelemenEntity elementoSubElemento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CC_ID", insertable = false, updatable = false)
    private ContaContabilEntity contaContabil;
}
