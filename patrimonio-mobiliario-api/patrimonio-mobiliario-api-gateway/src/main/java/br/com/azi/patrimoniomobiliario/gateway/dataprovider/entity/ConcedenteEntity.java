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

@Data
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_CONCEDENTE", schema = "PAT_MOBILIARIO")
@SequenceGenerator(name = "SEQ_CONCEDENTE", sequenceName = "PAT_MOBILIARIO.SEQ_CONCEDENTE", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name = "dataCadastro", column = @Column(name = "COC_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "COC_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "COC_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "COC_USUARIO_ALTERACAO"))
})
public class ConcedenteEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CONCEDENTE")
    @Column(name = "COC_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "COC_NOME", length = 45, nullable = false)
    private String nome;

    @Column(name = "COC_SITUACAO")
    private String situacao;

    @Column(name = "COC_TIPO")
    private String tipoPessoa;

    @Column(name = "COC_CPF_CNPJ")
    private String cpfCnpj;

}
