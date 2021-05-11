package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPerfilEntity is a Querydsl query type for PerfilEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPerfilEntity extends EntityPathBase<PerfilEntity> {

    private static final long serialVersionUID = 1156728833L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPerfilEntity perfilEntity = new QPerfilEntity("perfilEntity");

    public final StringPath descricao = createString("descricao");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nome = createString("nome");

    public final QProdutoEntity produtoEntity;

    public final StringPath tipo = createString("tipo");

    public QPerfilEntity(String variable) {
        this(PerfilEntity.class, forVariable(variable), INITS);
    }

    public QPerfilEntity(Path<? extends PerfilEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPerfilEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPerfilEntity(PathMetadata metadata, PathInits inits) {
        this(PerfilEntity.class, metadata, inits);
    }

    public QPerfilEntity(Class<? extends PerfilEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.produtoEntity = inits.isInitialized("produtoEntity") ? new QProdutoEntity(forProperty("produtoEntity")) : null;
    }

}

