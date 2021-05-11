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
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_CONVENIO", schema = "PAT_MOBILIARIO")
@SequenceGenerator(name = "SEQ_CONVENIO", sequenceName = "PAT_MOBILIARIO.SEQ_CONVENIO", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name = "dataCadastro", column = @Column(name = "CO_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "CO_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "CO_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "CO_USUARIO_ALTERACAO"))
})
public class ConvenioEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CONVENIO")
    @Column(name = "CO_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "CO_NOME")
    private String nome;

    @Column(name = "CO_DTHR_INICIO", columnDefinition = "TIMESTAMP")
    private Date dataVigenciaInicio;

    @Column(name = "CO_DTHR_FIM", columnDefinition = "TIMESTAMP")
    private Date dataVigenciaFim;

    @Column(name = "CO_NUMERO")
    private String numero;

    @Column(name = "CO_FONTE_RECURSO")
    private String fonteRecurso;

    @Column(name = "CO_TIPO")
    private String tipo;

    @Column(name = "CO_SITUACAO")
    private String situacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COC_ID")
    private ConcedenteEntity concedente;

}
