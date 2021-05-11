package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import br.com.azi.hal.core.generic.enumeration.EnumSimNao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_PERMISSAO_PERFIL", schema = "HAL")
public class PermissaoPerfilEntity {

    @Id
    @EmbeddedId
    @EqualsAndHashCode.Include
    private PermissaoPerfilPK permissaoPerfilPK;

    @Enumerated(EnumType.STRING)
    @Column(name = "PP_PERMISSAO", length = 1, insertable = false, updatable = false)
    private EnumSimNao permissao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FU_ID", insertable = false, updatable = false)
    private FuncaoEntity funcao;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PF_ID", insertable = false, updatable = false)
    private PerfilEntity perfil;

}
