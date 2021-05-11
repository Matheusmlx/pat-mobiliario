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

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_natureza_contratacao", schema = "comum_siga")
public class NaturezaContratacaoEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "nc_id", insertable = false, updatable = false)
    private Long id;

    @Column(name = "nc_descricao", insertable = false, updatable = false)
    private String descricao;

    @Column(name = "nc_situacao", insertable = false, updatable = false)
    private String situacao;

    @Column(name = "nc_tipo_natureza_contratacao", insertable = false, updatable = false)
    private String tipo;
}
