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
import java.util.Date;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_RESERVA_INTERVALO", schema = "PAT_MOBILIARIO")
@SequenceGenerator(name = "SEQ_RESERVA_INTERVALO", sequenceName = "PAT_MOBILIARIO.SEQ_RESERVA_INTERVALO", allocationSize = 1)
@AttributeOverride(name = "dataCadastro", column = @Column(name = "RI_DTHR_CADASTRO"))
@AttributeOverride(name = "dataAlteracao", column = @Column(name = "RI_DTHR_ALTERACAO"))
@AttributeOverride(name = "usuarioCadastro", column = @Column(name = "RI_USUARIO_CADASTRO"))
@AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "RI_USUARIO_ALTERACAO"))
public class ReservaIntervaloEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESERVA_INTERVALO")
    @EqualsAndHashCode.Include
    @Column(name = "RI_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RV_ID")
    private ReservaEntity reserva;

    @Column(name = "RI_CODIGO")
    private String codigo;

    @Column(name = "RI_SITUACAO")
    private String situacao;

    @Column(name = "RI_PREENCHIMENTO")
    private String preenchimento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_ORGAO")
    private UnidadeOrganizacionalEntity orgao;

    @Column(name = "RI_QTD_RESERVADA")
    private Long quantidadeReservada;

    @Column(name = "RI_NRO_INICIO")
    private Long numeroInicio;

    @Column(name = "RI_NRO_FIM")
    private Long numeroFim;

    @Column(name = "RI_NRO_TERMO")
    private String numeroTermo;

    @Column(name = "RI_DTHR_FINALIZACAO", columnDefinition = "timestamp")
    private Date dataFinalizacao;

}
