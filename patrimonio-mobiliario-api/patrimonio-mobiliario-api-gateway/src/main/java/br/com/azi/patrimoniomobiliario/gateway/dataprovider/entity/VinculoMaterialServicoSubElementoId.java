package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class VinculoMaterialServicoSubElementoId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MS_ID")
    private MaterialServicoEntity materialServico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NS_ID")
    private ContratacaoSubelementoEntity contratacaoSubelemento;
}
