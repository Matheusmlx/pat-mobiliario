package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_matser_subelem", schema = "comum_siga")
public class VinculoMaterialServicoSubelementoEntity {

    @EmbeddedId
    private VinculoMaterialServicoSubElementoId vinculoMaterialServicoSubElementoId;

}
