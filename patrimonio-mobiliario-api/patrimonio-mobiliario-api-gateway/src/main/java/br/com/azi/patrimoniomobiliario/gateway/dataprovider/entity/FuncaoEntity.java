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
@Table(name = "TB_FUNCAO", schema = "HAL")
public class FuncaoEntity {

    @Id
    @Column(name = "FU_ID", insertable = false, updatable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "FU_NOME", insertable = false, updatable = false)
    private String nome;

    @Column(name = "FU_DESCRICAO", insertable = false, updatable = false)
    private String descricao;
}
