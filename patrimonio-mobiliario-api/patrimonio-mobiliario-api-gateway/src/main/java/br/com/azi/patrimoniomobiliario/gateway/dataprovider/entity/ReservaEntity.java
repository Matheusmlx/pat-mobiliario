package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import br.com.azi.hal.core.generic.entity.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_RESERVA", schema = "PAT_MOBILIARIO")
@SequenceGenerator(name = "SEQ_RESERVA", sequenceName = "PAT_MOBILIARIO.SEQ_RESERVA", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name = "dataCadastro", column = @Column(name = "RV_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "RV_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "RV_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "RV_USUARIO_ALTERACAO")),
})
public class ReservaEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESERVA")
    @EqualsAndHashCode.Include
    @Column(name = "RV_ID")
    private Long id;

    @Column(name = "RV_CODIGO")
    private String codigo;

    @Column(name = "RV_SITUACAO")
    private String situacao;

    @Column(name = "RV_PREENCHIMENTO")
    private String preenchimento;

    @Column(name = "RV_DTHR_CRIACAO")
    private Date dataCriacao;

    @Column(name = "RV_QTD_RESERVADA")
    private Long quantidadeReservada;

    @Column(name = "RV_QTD_RESTANTE")
    private Long quantidadeRestante;

    @Column(name = "RV_NRO_INICIO")
    private Long numeroInicio;

    @Column(name = "RV_NRO_FIM")
    private Long numeroFim;

}
