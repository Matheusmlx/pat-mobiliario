package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContaContabilProdutoEntity is a Querydsl query type for ContaContabilProdutoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContaContabilProdutoEntity extends EntityPathBase<ContaContabilProdutoEntity> {

    private static final long serialVersionUID = 988837929L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContaContabilProdutoEntity contaContabilProdutoEntity = new QContaContabilProdutoEntity("contaContabilProdutoEntity");

    public final QContaContabilProdutoEntityPK contaContabilProdutoId;

    public QContaContabilProdutoEntity(String variable) {
        this(ContaContabilProdutoEntity.class, forVariable(variable), INITS);
    }

    public QContaContabilProdutoEntity(Path<? extends ContaContabilProdutoEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContaContabilProdutoEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContaContabilProdutoEntity(PathMetadata metadata, PathInits inits) {
        this(ContaContabilProdutoEntity.class, metadata, inits);
    }

    public QContaContabilProdutoEntity(Class<? extends ContaContabilProdutoEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contaContabilProdutoId = inits.isInitialized("contaContabilProdutoId") ? new QContaContabilProdutoEntityPK(forProperty("contaContabilProdutoId"), inits.get("contaContabilProdutoId")) : null;
    }

}

