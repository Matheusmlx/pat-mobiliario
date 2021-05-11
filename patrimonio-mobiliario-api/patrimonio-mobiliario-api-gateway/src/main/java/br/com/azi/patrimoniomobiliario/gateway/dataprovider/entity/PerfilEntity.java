package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_PERFIL", schema = "COMUM")
public class PerfilEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "PF_ID", insertable = false, updatable = false)
    private Long id;

    @Column(name = "PF_NOME", insertable = false, updatable = false)
    private String nome;

    @Column(name = "PF_DESCRICAO", insertable = false, updatable = false)
    private String descricao;

    @Column(name = "PF_TIPO", insertable = false, updatable = false)
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PR_ID", insertable = false, updatable = false)
    private ProdutoEntity produtoEntity;

}
