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

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_PRODUTO", schema = "COMUM")
public class ProdutoEntity implements Serializable {

    @Id
    @Column(name = "PR_ID", updatable = false, insertable = false)
    private Long id;

    @Column(name = "PR_NOME", updatable = false, insertable = false)
    private String nome;

}
