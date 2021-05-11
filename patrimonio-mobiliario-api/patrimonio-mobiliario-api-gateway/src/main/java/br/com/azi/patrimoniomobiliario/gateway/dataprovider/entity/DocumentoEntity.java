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
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_DOCUMENTO", schema = "PAT_MOBILIARIO")
@SequenceGenerator(name = "SEQ_DOCUMENTO", sequenceName = "PAT_MOBILIARIO.SEQ_DOCUMENTO", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name = "dataCadastro", column = @Column(name = "DO_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "DO_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "DO_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "DO_USUARIO_ALTERACAO"))
})
public class DocumentoEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_DOCUMENTO")
    @Column(name = "DO_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "DO_NUMERO", length = 45)
    private String numero;

    @Column(name = "DO_DT", columnDefinition = "TIMESTAMP")
    private Date data;

    @Column(name = "DO_VALOR", precision = 20, scale = 6)
    private BigDecimal valor;

    @Column(name = "DO_URI_ANEXO", length = 500)
    private String uriAnexo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IN_ID")
    private IncorporacaoEntity incorporacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TD_ID")
    private TipoDocumentoEntity tipoDocumento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MO_ID")
    private MovimentacaoEntity movimentacao;
}
