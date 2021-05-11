package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_ESTADO_CONSERVACAO", schema = "PAT_MOBILIARIO")
@SequenceGenerator(name = "SEQ_ESTADO_CONSERVACAO", sequenceName = "PAT_MOBILIARIO.SEQ_ESTADO_CONSERVACAO", allocationSize = 1)
public class EstadoConservacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_ESTADO_CONSERVACAO")
    @Column(name = "EC_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "EC_NOME")
    private String nome;

    @Column(name = "EC_PRIORIDADE")
    private Integer prioridade;
}
