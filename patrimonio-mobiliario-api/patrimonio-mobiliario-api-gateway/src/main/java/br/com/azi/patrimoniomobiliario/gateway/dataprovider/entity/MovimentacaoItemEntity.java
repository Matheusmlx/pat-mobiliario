package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_MOVIMENTACAO_ITEM", schema = "PAT_MOBILIARIO")
public class MovimentacaoItemEntity{

    @Id
    @EmbeddedId
    private MovimentacaoItemPK movimentacaoItemPK;

    @Column(name = "MI_DTHR_DEVOLUCAO", columnDefinition = "TIMESTAMP")
    private Date dataDevolucao;
}
