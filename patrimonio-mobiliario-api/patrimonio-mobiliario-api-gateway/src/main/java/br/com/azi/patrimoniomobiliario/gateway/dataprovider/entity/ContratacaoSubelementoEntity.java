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

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_contratacao_subelem", schema = "comum_siga")
public class ContratacaoSubelementoEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "NS_ID", insertable = false, updatable = false)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ES_ID", insertable = false, updatable = false)
    private ElementoSubelemenEntity elementoSubElemento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NC_ID", insertable = false, updatable = false)
    private NaturezaContratacaoEntity naturezaContratacao;
}
