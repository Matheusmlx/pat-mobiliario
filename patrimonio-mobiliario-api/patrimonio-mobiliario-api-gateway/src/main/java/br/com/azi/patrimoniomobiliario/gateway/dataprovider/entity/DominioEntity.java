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
@Table(name = "TB_DOMINIO", schema = "HAL")
public class DominioEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "DM_ID", insertable = false, updatable = false)
    private Long id;

    @Column(name = "DM_TIPO_CLIENTE", insertable = false, updatable = false)
    private String tipoCliente;

    @Column(name = "DM_ID_CLIENTE", insertable = false, updatable = false)
    private Long chaveCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "US_ID", insertable = false, updatable = false)
    private UsuarioEntity usuario;
}
