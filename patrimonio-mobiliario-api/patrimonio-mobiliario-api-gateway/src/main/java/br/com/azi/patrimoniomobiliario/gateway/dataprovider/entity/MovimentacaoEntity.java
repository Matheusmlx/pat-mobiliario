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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "TB_MOVIMENTACAO", schema = "PAT_MOBILIARIO")
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "SEQ_MOVIMENTACAO", sequenceName = "PAT_MOBILIARIO.SEQ_MOVIMENTACAO", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name = "dataCadastro", column = @Column(name = "MO_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "MO_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "MO_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "MO_USUARIO_ALTERACAO"))
})
public class MovimentacaoEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_MOVIMENTACAO")
    @EqualsAndHashCode.Include
    @Column(name = "MO_ID")
    private Long id;

    @Column(name = "MO_CODIGO")
    private String codigo;

    @Column(name = "MO_TIPO")
    private String tipo;

    @Column(name = "MO_SITUACAO")
    private String situacao;

    @Column(name = "MO_NUMERO_PROCESSO")
    private String numeroProcesso;

    @Column(name = "MO_MOTIVO_OBS")
    private String motivoObservacao;

    @Column(name = "MO_USUARIO_CRIACAO")
    private String usuarioCriacao;

    @Column(name = "MO_USUARIO_FINALIZACAO")
    private String usuarioFinalizacao;

    @Column(name = "MO_DTHR_FINALIZACAO", columnDefinition = "TIMESTAMP")
    private Date dataFinalizacao;

    @Column(name = "MO_DTHR_DEVOLUCAO", columnDefinition = "TIMESTAMP")
    private Date dataDevolucao;

    @Column(name = "MO_DTHR_ENVIO", columnDefinition = "TIMESTAMP")
    private Date dataEnvio;

    @Column(name = "MO_NUMERO_TERMO")
    private String numeroTermoResponsabilidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_ORGAO_ORIGEM")
    private UnidadeOrganizacionalEntity orgaoOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_ORGAO_DESTINO")
    private UnidadeOrganizacionalEntity orgaoDestino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_SETOR_ORIGEM")
    private UnidadeOrganizacionalEntity setorOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UO_ID_SETOR_DESTINO")
    private UnidadeOrganizacionalEntity setorDestino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NLC_ID")
    private NotaLancamentoContabilEntity notaLancamentoContabil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RES_ID")
    private ResponsavelEntity responsavel;

    @Column(name = "MO_DTHR_INI_PROC")
    private Date dataInicioProcessamento;

    @Column(name = "MO_DTHR_FIM_PROC")
    private Date dataFimProcessamento;

    @Column(name = "MO_ERRO_PROC")
    private String erroProcessamento;
}
