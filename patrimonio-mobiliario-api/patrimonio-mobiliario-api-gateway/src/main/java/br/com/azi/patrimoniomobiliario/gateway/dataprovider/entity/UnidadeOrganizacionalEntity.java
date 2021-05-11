package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_UNIDADE_ORGANIZACIONAL", schema = "comum_siga")
public class UnidadeOrganizacionalEntity implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "UO_ID", insertable = false, updatable = false)
    private Long id;

    @Column(name = "UO_NOME", insertable = false, updatable = false)
    private String nome;

    @Column(name = "UO_SIGLA", insertable = false, updatable = false)
    private String sigla;

    @Column(name = "UO_SITUACAO", insertable = false, updatable = false)
    private String situacao;

    @Column(name = "UO_TIPO", insertable = false, updatable = false)
    private String tipo;

    @Column(name = "UO_TIPO_ADM", insertable = false, updatable = false)
    private String tipoAdministracao;

    @Column(name = "UO_COD_HIERARQUIA", insertable = false, updatable = false)
    private String codigoHierarquia;

    @Column(name = "UO_RESPONSAVEL", insertable = false, updatable = false)
    private String nomeResponsavel;

    @Column(name = "UO_SUBSTITUTO", insertable = false, updatable = false)
    private String nomeSubstituto;

    @Column(name = "UO_LOGRADOURO", insertable = false, updatable = false)
    private String logradouro;

    @Column(name = "UO_NUMERO", insertable = false, updatable = false)
    private String numero;

    @Column(name = "UO_COMPLEMENTO", insertable = false, updatable = false)
    private String complemento;

    @Column(name = "UO_BAIRRO", insertable = false, updatable = false)
    private String bairro;

    @Column(name = "UO_CEP", insertable = false, updatable = false)
    private String cep;

    @Column(name = "UO_EMAIL", insertable = false, updatable = false)
    private String email;

    @Column(name = "UO_ALMOXARIFADO", insertable = false, updatable = false)
    private Boolean almoxarifado;

    @Column(name = "UO_COD_ORGAO", insertable = false, updatable = false)
    private String codigoOrgao;

    @Column(name = "UO_DATA_DECRETO", insertable = false, updatable = false)
    private LocalDate dataDecreto;

    @Column(name = "UO_NUMERO_DECRETO", insertable = false, updatable = false)
    private String numeroDecreto;

    @Column(name = "UO_MOTIVO_ALTERACAO", insertable = false, updatable = false)
    private String motivoAlteracao;

    @Column(name = "UO_JUSTIFICATIVA", insertable = false, updatable = false)
    private String justificativa;

    @Column(name = "UO_ID_ORIGEM", insertable = false, updatable = false)
    private Long origemId;

    @Column(name = "UO_ESTRUTURA_ADMINISTRATIVA", insertable = false, updatable = false)
    protected Long estruturaAdministrativaId;

    @Column(name = "UO_ID_SUPERIOR", insertable = false, updatable = false)
    private Long unidadeSuperiorId;

    @Column(name = "MU_ID", insertable = false, updatable = false)
    private Long municipioId;

    @Column(name = "UO_ID_EXTERNO", insertable = false, updatable = false)
    private Integer idExterno;

    public UnidadeOrganizacionalEntity(Long id, String nome, String sigla, String codigoHierarquia) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
        this.codigoHierarquia = codigoHierarquia;
    }
}
