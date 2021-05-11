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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_RECONHECIMENTO", schema = "PAT_MOBILIARIO")
@SequenceGenerator(name = "SEQ_RECONHECIMENTO", sequenceName = "PAT_MOBILIARIO.SEQ_RECONHECIMENTO", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name = "dataCadastro", column = @Column(name = "RE_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "RE_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "RE_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "RE_USUARIO_ALTERACAO"))
})
public class ReconhecimentoEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RECONHECIMENTO")
    @EqualsAndHashCode.Include
    @Column(name = "RE_ID")
    private Long id;

    @Column(name = "RE_NOME")
    private String nome;

    @Column(name = "RE_SITUACAO")
    private String situacao;

    @Column(name = "RE_EXECUCAO_ORCAMENTARIA")
    private Boolean execucaoOrcamentaria;

    @Column(name = "RE_NOTA_FICAL")
    private Boolean notaFiscal;

    @Column(name = "RE_EMPENHO")
    private Boolean empenho;
}
