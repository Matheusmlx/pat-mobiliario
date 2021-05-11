package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QItemSimplificadoEntity is a Querydsl query type for ItemSimplificadoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QItemSimplificadoEntity extends EntityPathBase<ItemSimplificadoEntity> {

    private static final long serialVersionUID = 176608650L;

    public static final QItemSimplificadoEntity itemSimplificadoEntity = new QItemSimplificadoEntity("itemSimplificadoEntity");

    public final StringPath codigo = createString("codigo");

    public final StringPath descricao = createString("descricao");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> materialServicoId = createNumber("materialServicoId", Long.class);

    public final StringPath situacao = createString("situacao");

    public final StringPath status = createString("status");

    public final StringPath tipo = createString("tipo");

    public QItemSimplificadoEntity(String variable) {
        super(ItemSimplificadoEntity.class, forVariable(variable));
    }

    public QItemSimplificadoEntity(Path<? extends ItemSimplificadoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QItemSimplificadoEntity(PathMetadata metadata) {
        super(ItemSimplificadoEntity.class, metadata);
    }

}

