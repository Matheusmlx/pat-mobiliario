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

@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_TIPO_DOCUMENTO", schema = "COMUM_SIGA")
public class TipoDocumentoEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "TD_ID", insertable = false, updatable = false)
    private Long id;

    @Column(name = "TD_DESC", insertable = false, updatable = false)
    private String descricao;

    @Column(name = "TD_PERMITE_ANEXO", insertable = false, updatable = false)
    private Boolean permiteAnexo;

    @Column(name = "TD_IDENTIFICACAO_DOCUMENTO", insertable = false, updatable = false)
    private String identificacaoDocumento;

}
