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
@Table(name = "TB_NOTA_LANCAMENTO_CONTABIL" ,schema = "PAT_MOBILIARIO")
@SequenceGenerator(name = "SEQ_NOTA_LANCTO_CONTABIL", sequenceName = "PAT_MOBILIARIO.SEQ_NOTA_LANCTO_CONTABIL", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name ="dataCadastro" , column = @Column(name = "NLC_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "NLC_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "NLC_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "NLC_USUARIO_ALTERACAO")),
})
public class NotaLancamentoContabilEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_NOTA_LANCTO_CONTABIL")
    @Column(name = "NLC_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "NLC_NUMERO")
    private String numero;

    @Column(name = "NLC_DTHR_LANCAMENTO", columnDefinition = "TIMESTAMP")
    private Date dataLancamento;
}
