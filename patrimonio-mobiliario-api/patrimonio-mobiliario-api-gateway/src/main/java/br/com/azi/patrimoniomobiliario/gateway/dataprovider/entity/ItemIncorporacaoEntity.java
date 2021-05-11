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

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_INCORPORACAO_ITEM" ,schema = "PAT_MOBILIARIO")
@SequenceGenerator(name = "SEQ_INCORPORACAO_ITEM", sequenceName = "PAT_MOBILIARIO.SEQ_INCORPORACAO_ITEM", allocationSize = 1)
@AttributeOverrides({
    @AttributeOverride(name ="dataCadastro" , column = @Column(name = "INI_DTHR_CADASTRO")),
    @AttributeOverride(name = "dataAlteracao", column = @Column(name = "INI_DTHR_ALTERACAO")),
    @AttributeOverride(name = "usuarioCadastro", column = @Column(name = "INI_USUARIO_CADASTRO")),
    @AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "INI_USUARIO_ALTERACAO")),
})
public class ItemIncorporacaoEntity extends BaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_INCORPORACAO_ITEM")
    @Column(name = "INI_ID")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "INI_CODIGO", length = 100)
    private String codigo;

    @Column(name = "INI_DESCRICAO")
    private String descricao;

    @Column(name = "INI_MARCA", length = 500)
    private String marca;

    @Column(name = "INI_MODELO", length = 500)
    private String modelo;

    @Column(name = "INI_FABRICANTE", length = 500)
    private String fabricante;

    @Column(name = "INI_VALOR_TOTAL")
    private BigDecimal valorTotal;

    @Column(name = "INI_TOTAL_UNIDADES")
    private Integer quantidade;

    @Column(name = "INI_NUMERACAO_PATRIMONIAL")
    private String numeracaoPatrimonial;

    @Column(name = "INI_URI_IMAGEM", length = 500)
    private String uriImagem;

    @Column(name = "INI_ANO_FABRICACAO_MODELO")
    private String anoFabricacaoModelo;

    @Column(name = "INI_COMBUSTIVEL")
    private String combustivel;

    @Column(name = "INI_CATEGORIA")
    private String categoria;

    @Column(name = "INI_SITUACAO", length = 20)
    private String situacao;

    @Column(name = "INI_TIPO", length = 50)
    private String tipoBem;

    @Column(name = "INI_DEPRECIAVEL")
    private Boolean depreciavel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CC_ID")
    private ContaContabilEntity contaContabil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ND_ID")
    private NaturezaDespesaEntity naturezaDespesa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EC_ID")
    private EstadoConservacaoEntity estadoConservacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="CD_ID")
    private ConfiguracaoDepreciacaoEntity configDepreciacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IN_ID")
    private IncorporacaoEntity incorporacao;
}
