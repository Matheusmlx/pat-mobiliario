package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNotificacaoEntity is a Querydsl query type for NotificacaoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QNotificacaoEntity extends EntityPathBase<NotificacaoEntity> {

    private static final long serialVersionUID = 512188331L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNotificacaoEntity notificacaoEntity = new QNotificacaoEntity("notificacaoEntity");

    public final br.com.azi.hal.core.generic.entity.QBaseObject _super = new br.com.azi.hal.core.generic.entity.QBaseObject(this);

    public final StringPath assunto = createString("assunto");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = _super.dataAlteracao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataCadastro = _super.dataCadastro;

    public final DateTimePath<java.util.Date> dataCriacao = createDateTime("dataCriacao", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath mensagem = createString("mensagem");

    public final StringPath origem = createString("origem");

    public final NumberPath<Long> origemId = createNumber("origemId", Long.class);

    public final QUsuarioEntity usuario;

    //inherited
    public final StringPath usuarioAlteracao = _super.usuarioAlteracao;

    //inherited
    public final StringPath usuarioCadastro = _super.usuarioCadastro;

    public final BooleanPath visualizada = createBoolean("visualizada");

    public QNotificacaoEntity(String variable) {
        this(NotificacaoEntity.class, forVariable(variable), INITS);
    }

    public QNotificacaoEntity(Path<? extends NotificacaoEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNotificacaoEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNotificacaoEntity(PathMetadata metadata, PathInits inits) {
        this(NotificacaoEntity.class, metadata, inits);
    }

    public QNotificacaoEntity(Class<? extends NotificacaoEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.usuario = inits.isInitialized("usuario") ? new QUsuarioEntity(forProperty("usuario")) : null;
    }

}

