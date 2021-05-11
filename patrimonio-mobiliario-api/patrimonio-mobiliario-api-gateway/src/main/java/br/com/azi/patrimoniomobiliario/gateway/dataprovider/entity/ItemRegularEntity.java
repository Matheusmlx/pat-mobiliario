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
@Table(name = "TB_ITEM_MATERIAL_SERVICO", schema = "comum_siga")

public class ItemRegularEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "IMS_ID", insertable = false, updatable = false)
    private Long id;

    @Column(name = "IMS_SITUACAO", insertable = false, updatable = false)
    private String situacao;

    @Column(name = "IMS_CODIGO", insertable = false, updatable = false)
    private String codigo;

    @Column(name = "IMS_TIPO", insertable = false, updatable = false)
    private String tipo;

    @Column(name = "IMS_DESCRICAO", insertable = false, updatable = false)
    private String descricao;

    @Column(name = "IMS_STATUS", insertable = false, updatable = false)
    private String status;

    @Column(name = "MS_ID", insertable = false, updatable = false)
    private Long materialServicoId;
}
