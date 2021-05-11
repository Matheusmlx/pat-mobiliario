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
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_ELEMENTO_SUBELEMEN", schema = "COMUM_SIGA")
@AttributeOverrides({
    @AttributeOverride(name = "dataCadastro", column = @Column(name = "ES_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "ES_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "ES_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "ES_USUARIO_ALTERACAO"))
})
public class ElementoSubelemenEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "ES_ID", insertable = false, updatable = false)
    private Long id;

    @Column(name = "ES_CODIGO", insertable = false, updatable = false)
    private String codigo;

    @Column(name = "ES_NOME", insertable = false, updatable = false)
    private String nome;

    @Column(name = "ES_PAI", insertable = false, updatable = false)
    private Long elementoPai;

    @Column(name = "ES_SITUACAO", insertable = false, updatable = false)
    private String situacao;

    @Column(name = "ES_TIPO", insertable = false, updatable = false)
    private String tipo;

    @Column(name = "ES_JUSTIFICATIVA", insertable = false, updatable = false)
    private String justificativa;
}
