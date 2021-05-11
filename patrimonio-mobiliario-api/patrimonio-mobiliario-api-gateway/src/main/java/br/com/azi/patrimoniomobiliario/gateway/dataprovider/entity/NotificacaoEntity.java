package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import br.com.azi.hal.core.generic.entity.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
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
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_NOTIFICACAO", schema = "PAT_MOBILIARIO")
@SequenceGenerator(name = "SEQ_NOTIFICACAO", sequenceName = "PAT_MOBILIARIO.SEQ_NOTIFICACAO", allocationSize = 1)
@AttributeOverride(name = "dataCadastro", column = @Column(name = "NO_DTHR_CADASTRO"))
@AttributeOverride(name = "dataAlteracao", column = @Column(name = "NO_DTHR_ALTERACAO"))
@AttributeOverride(name = "usuarioCadastro", column = @Column(name = "NO_USUARIO_CADASTRO"))
@AttributeOverride(name = "usuarioAlteracao", column = @Column(name = "NO_USUARIO_ALTERACAO"))
public class NotificacaoEntity extends BaseObject {

    @Id
    @Column(name = "NO_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NOTIFICACAO")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "NO_ORIGEM")
    private String origem;

    @Column(name = "NO_ORIGEM_ID")
    private Long origemId;

    @Column(name = "NO_ASSUNTO")
    private String assunto;

    @Column(name = "NO_MENSAGEM")
    private String mensagem;

    @Column(name = "NO_DTHR_CRIACAO", columnDefinition = "TIMESTAMP")
    private Date dataCriacao;

    @Column(name = "NO_VISUALIZADA")
    private Boolean visualizada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "US_ID")
    private UsuarioEntity usuario;

}
