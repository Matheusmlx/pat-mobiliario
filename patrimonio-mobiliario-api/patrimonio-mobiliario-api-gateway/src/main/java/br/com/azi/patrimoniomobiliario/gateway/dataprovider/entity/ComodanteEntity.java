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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_COMODANTE", schema = "PAT_MOBILIARIO")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@SequenceGenerator(name = "SEQ_COMODANTE", sequenceName = "PAT_MOBILIARIO.SEQ_COMODANTE", allocationSize = 1)
@AttributeOverride(name = "dataCadastro", column = @Column(name = "COM_DTHR_CADASTRO"))
@AttributeOverride(name = "dataAlteracao", column = @Column(name = "COM_DTHR_ALTERACAO"))
@AttributeOverride(name = "usuarioCadastro", column = @Column(name = "COM_USUARIO_CADASTRO"))
@AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "COM_USUARIO_ALTERACAO"))
public class ComodanteEntity extends BaseObject {

    @Id
    @GeneratedValue(generator = "SEQ_COMODANTE", strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    @Column(name = "COM_ID")
    private Long id;

    @Column(name = "COM_NOME")
    private String nome;
}

