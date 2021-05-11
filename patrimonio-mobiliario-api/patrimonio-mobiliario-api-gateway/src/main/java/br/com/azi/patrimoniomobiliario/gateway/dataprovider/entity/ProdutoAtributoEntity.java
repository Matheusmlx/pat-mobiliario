package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_PRODUTO_ATRIBUTO", schema = "COMUM")
public class ProdutoAtributoEntity {

    @Id
    @Column(name = "PA_ID", insertable = false, updatable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "PA_ATRIBUTO", nullable = false, insertable = false, updatable = false)
    private String atributo;

    @Column(name = "PA_VALOR", nullable = false, insertable = false, updatable = false)
    private String valor;

    @Column(name = "PR_ID", nullable = false, insertable = false, updatable = false)
    private Long produtoId;

    @Column(name = "PA_TIPO", nullable = false, insertable = false, updatable = false)
    private String tipo;
}
