package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContaContabilProdutoEntityPK is a Querydsl query type for ContaContabilProdutoEntityPK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QContaContabilProdutoEntityPK extends BeanPath<ContaContabilProdutoEntityPK> {

    private static final long serialVersionUID = 1085479908L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContaContabilProdutoEntityPK contaContabilProdutoEntityPK = new QContaContabilProdutoEntityPK("contaContabilProdutoEntityPK");

    public final QContaContabilEntity contaContabilId;

    public final QProdutoEntity produtoId;

    public QContaContabilProdutoEntityPK(String variable) {
        this(ContaContabilProdutoEntityPK.class, forVariable(variable), INITS);
    }

    public QContaContabilProdutoEntityPK(Path<? extends ContaContabilProdutoEntityPK> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContaContabilProdutoEntityPK(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContaContabilProdutoEntityPK(PathMetadata metadata, PathInits inits) {
        this(ContaContabilProdutoEntityPK.class, metadata, inits);
    }

    public QContaContabilProdutoEntityPK(Class<? extends ContaContabilProdutoEntityPK> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contaContabilId = inits.isInitialized("contaContabilId") ? new QContaContabilEntity(forProperty("contaContabilId")) : null;
        this.produtoId = inits.isInitialized("produtoId") ? new QProdutoEntity(forProperty("produtoId")) : null;
    }

}

