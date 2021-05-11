package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMovimentacaoItemPK is a Querydsl query type for MovimentacaoItemPK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QMovimentacaoItemPK extends BeanPath<MovimentacaoItemPK> {

    private static final long serialVersionUID = -2049165085L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMovimentacaoItemPK movimentacaoItemPK = new QMovimentacaoItemPK("movimentacaoItemPK");

    public final QMovimentacaoEntity movimentacao;

    public final QPatrimonioEntity patrimonio;

    public QMovimentacaoItemPK(String variable) {
        this(MovimentacaoItemPK.class, forVariable(variable), INITS);
    }

    public QMovimentacaoItemPK(Path<? extends MovimentacaoItemPK> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMovimentacaoItemPK(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMovimentacaoItemPK(PathMetadata metadata, PathInits inits) {
        this(MovimentacaoItemPK.class, metadata, inits);
    }

    public QMovimentacaoItemPK(Class<? extends MovimentacaoItemPK> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.movimentacao = inits.isInitialized("movimentacao") ? new QMovimentacaoEntity(forProperty("movimentacao"), inits.get("movimentacao")) : null;
        this.patrimonio = inits.isInitialized("patrimonio") ? new QPatrimonioEntity(forProperty("patrimonio"), inits.get("patrimonio")) : null;
    }

}

