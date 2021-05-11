package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import br.com.azi.hal.core.generic.entity.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_RESERVA_INTERVALO_NUMERO", schema = "PAT_MOBILIARIO")
@SequenceGenerator(name = "SEQ_RESERVA_INTERVALO_NUMERO", sequenceName = "PAT_MOBILIARIO.SEQ_RESERVA_INTERVALO_NUMERO", allocationSize = 1)
@AttributeOverride(name = "dataCadastro", column = @Column(name = "RIN_DTHR_CADASTRO"))
@AttributeOverride(name = "dataAlteracao", column = @Column(name = "RIN_DTHR_ALTERACAO"))
@AttributeOverride(name = "usuarioCadastro", column = @Column(name = "RIN_USUARIO_CADASTRO"))
@AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "RIN_USUARIO_ALTERACAO"))
public class ReservaIntervaloNumeroEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESERVA_INTERVALO_NUMERO")
    @EqualsAndHashCode.Include
    @Column(name = "RIN_ID")
    private Long id;

    @Column(name = "RIN_NUMERO")
    private Long numero;

    @Column(name = "RIN_UTILIZADO")
    private Boolean utilizado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RI_ID")
    private ReservaIntervaloEntity reservaIntervalo;
}
