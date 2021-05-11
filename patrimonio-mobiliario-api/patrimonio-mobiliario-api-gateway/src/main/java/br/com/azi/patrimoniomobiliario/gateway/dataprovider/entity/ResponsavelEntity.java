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


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_RESPONSAVEL", schema = "PAT_MOBILIARIO")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@SequenceGenerator(name = "SEQ_RESPONSAVEL", sequenceName = "PAT_MOBILIARIO.SEQ_RESPONSAVEL",allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name = "dataCadastro", column = @Column(name = "RES_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "RES_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "RES_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "RES_USUARIO_ALTERACAO"))
})
public class ResponsavelEntity extends BaseObject {

    @Id
    @GeneratedValue(generator = "SEQ_RESPONSAVEL", strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    @Column(name = "RES_ID")
    private Long id;

    @Column(name = "RES_NOME")
    private String nome;
}
