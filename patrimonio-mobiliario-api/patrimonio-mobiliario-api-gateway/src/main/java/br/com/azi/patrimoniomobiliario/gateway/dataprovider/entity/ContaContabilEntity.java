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

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_conta_contabil", schema = "comum_siga")
public class ContaContabilEntity {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "cc_id", insertable = false, updatable = false)
    private Long id;

    @Column(name = "cc_codigo", insertable = false, updatable = false)
    private String codigo;

    @Column(name = "cc_descricao", insertable = false, updatable = false)
    private String descricao;

    @Column(name = "cc_situacao", insertable = false, updatable = false)
    private String situacao;
}
