package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDominioPerfilEntity is a Querydsl query type for DominioPerfilEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDominioPerfilEntity extends EntityPathBase<DominioPerfilEntity> {

    private static final long serialVersionUID = -448315574L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDominioPerfilEntity dominioPerfilEntity = new QDominioPerfilEntity("dominioPerfilEntity");

    public final QDominioPerfilPK dominioPerfilPK;

    public QDominioPerfilEntity(String variable) {
        this(DominioPerfilEntity.class, forVariable(variable), INITS);
    }

    public QDominioPerfilEntity(Path<? extends DominioPerfilEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDominioPerfilEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDominioPerfilEntity(PathMetadata metadata, PathInits inits) {
        this(DominioPerfilEntity.class, metadata, inits);
    }

    public QDominioPerfilEntity(Class<? extends DominioPerfilEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dominioPerfilPK = inits.isInitialized("dominioPerfilPK") ? new QDominioPerfilPK(forProperty("dominioPerfilPK")) : null;
    }

}

