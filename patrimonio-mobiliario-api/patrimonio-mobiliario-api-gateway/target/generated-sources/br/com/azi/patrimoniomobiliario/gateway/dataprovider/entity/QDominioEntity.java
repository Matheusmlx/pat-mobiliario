package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDominioEntity is a Querydsl query type for DominioEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDominioEntity extends EntityPathBase<DominioEntity> {

    private static final long serialVersionUID = -2118538594L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDominioEntity dominioEntity = new QDominioEntity("dominioEntity");

    public final NumberPath<Long> chaveCliente = createNumber("chaveCliente", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath tipoCliente = createString("tipoCliente");

    public final QUsuarioEntity usuario;

    public QDominioEntity(String variable) {
        this(DominioEntity.class, forVariable(variable), INITS);
    }

    public QDominioEntity(Path<? extends DominioEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDominioEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDominioEntity(PathMetadata metadata, PathInits inits) {
        this(DominioEntity.class, metadata, inits);
    }

    public QDominioEntity(Class<? extends DominioEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.usuario = inits.isInitialized("usuario") ? new QUsuarioEntity(forProperty("usuario")) : null;
    }

}

