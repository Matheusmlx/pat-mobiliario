package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QItemRegularEntity is a Querydsl query type for ItemRegularEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QItemRegularEntity extends EntityPathBase<ItemRegularEntity> {

    private static final long serialVersionUID = -1273860518L;

    public static final QItemRegularEntity itemRegularEntity = new QItemRegularEntity("itemRegularEntity");

    public final StringPath codigo = createString("codigo");

    public final StringPath descricao = createString("descricao");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> materialServicoId = createNumber("materialServicoId", Long.class);

    public final StringPath situacao = createString("situacao");

    public final StringPath status = createString("status");

    public final StringPath tipo = createString("tipo");

    public QItemRegularEntity(String variable) {
        super(ItemRegularEntity.class, forVariable(variable));
    }

    public QItemRegularEntity(Path<? extends ItemRegularEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QItemRegularEntity(PathMetadata metadata) {
        super(ItemRegularEntity.class, metadata);
    }

}

