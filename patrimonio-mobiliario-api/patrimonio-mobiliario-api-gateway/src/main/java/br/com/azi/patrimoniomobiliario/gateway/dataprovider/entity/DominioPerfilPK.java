package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class DominioPerfilPK implements Serializable {

    @Column(name = "PF_ID", insertable = false, updatable = false)
    private Long perfilId;

    @Column(name = "DM_ID", insertable = false, updatable = false)
    private Long dominioId;

}
