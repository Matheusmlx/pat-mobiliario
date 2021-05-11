package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMovimentacaoItemEntity is a Querydsl query type for MovimentacaoItemEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMovimentacaoItemEntity extends EntityPathBase<MovimentacaoItemEntity> {

    private static final long serialVersionUID = 1222462187L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMovimentacaoItemEntity movimentacaoItemEntity = new QMovimentacaoItemEntity("movimentacaoItemEntity");

    public final DateTimePath<java.util.Date> dataDevolucao = createDateTime("dataDevolucao", java.util.Date.class);

    public final QMovimentacaoItemPK movimentacaoItemPK;

    public QMovimentacaoItemEntity(String variable) {
        this(MovimentacaoItemEntity.class, forVariable(variable), INITS);
    }

    public QMovimentacaoItemEntity(Path<? extends MovimentacaoItemEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMovimentacaoItemEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMovimentacaoItemEntity(PathMetadata metadata, PathInits inits) {
        this(MovimentacaoItemEntity.class, metadata, inits);
    }

    public QMovimentacaoItemEntity(Class<? extends MovimentacaoItemEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.movimentacaoItemPK = inits.isInitialized("movimentacaoItemPK") ? new QMovimentacaoItemPK(forProperty("movimentacaoItemPK"), inits.get("movimentacaoItemPK")) : null;
    }

}

