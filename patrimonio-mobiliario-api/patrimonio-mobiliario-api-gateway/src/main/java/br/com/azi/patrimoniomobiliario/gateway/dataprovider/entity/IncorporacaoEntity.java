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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_INCORPORACAO" ,schema = "PAT_MOBILIARIO")
@SequenceGenerator(name = "SEQ_INCORPORACAO", sequenceName = "PAT_MOBILIARIO.SEQ_INCORPORACAO", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name ="dataCadastro" , column = @Column(name = "IN_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "IN_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "IN_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "IN_USUARIO_ALTERACAO")),
})
public class IncorporacaoEntity extends  BaseObject{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_INCORPORACAO")
    @Column(name = "IN_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "IN_CODIGO")
    private String codigo;

    @Column(name = "IN_DTHR_RECEBIMENTO")
    private Date dataRecebimento;

    @Column(name = "IN_SITUACAO")
    private String situacao;

    @Column(name = "IN_NUM_PROCESSO")
    private String numProcesso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PE_ID")
    private FornecedorEntity fornecedor;

    @Column(name = "IN_NOTA")
    private String nota;

    @Column(name = "IN_VALOR_NOTA")
    private BigDecimal valorNota;

    @Column(name = "IN_DTHR_NOTA", columnDefinition = "TIMESTAMP")
    private Date dataNota;

    @Column(name = "IN_ORIGEM_CONVENIO")
    private Boolean origemConvenio;

    @Column(name = "IN_ORIGEM_FUNDO")
    private Boolean origemFundo;

    @Column(name = "IN_ORIGEM_PROJETO")
    private Boolean origemProjeto;

    @Column(name = "IN_ORIGEM_COMODATO")
    private Boolean origemComodato;

    @Column(name = "IN_USUARIO_FINALIZACAO")
    private String usuarioFinalizacao;

    @Column(name = "IN_DTHR_FINALIZACAO")
    private Date dataFinalizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_ORGAO")
    private UnidadeOrganizacionalEntity orgao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_SETOR")
    private UnidadeOrganizacionalEntity setor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_FUNDO")
    private UnidadeOrganizacionalEntity fundo;

    @Column(name = "IN_PROJETO")
    private String projeto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CO_ID")
    private ConvenioEntity convenio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RE_ID")
    private ReconhecimentoEntity reconhecimento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NLC_ID")
    private NotaLancamentoContabilEntity notaLancamentoContabil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COM_ID")
    private ComodanteEntity comodanteEntity;

    @Column(name = "IN_DTHR_INI_PROC")
    private Date dataInicioProcessamento;

    @Column(name = "IN_DTHR_FIM_PROC")
    private Date dataFimProcessamento;

    @Column(name = "IN_ERRO_PROC")
    private String erroProcessamento;
}
