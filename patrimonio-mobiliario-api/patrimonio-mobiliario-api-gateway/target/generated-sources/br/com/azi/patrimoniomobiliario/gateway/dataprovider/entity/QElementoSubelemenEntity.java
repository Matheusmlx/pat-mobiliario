package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QElementoSubelemenEntity is a Querydsl query type for ElementoSubelemenEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QElementoSubelemenEntity extends EntityPathBase<ElementoSubelemenEntity> {

    private static final long serialVersionUID = -405680938L;

    public static final QElementoSubelemenEntity elementoSubelemenEntity = new QElementoSubelemenEntity("elementoSubelemenEntity");

    public final StringPath codigo = createString("codigo");

    public final NumberPath<Long> elementoPai = createNumber("elementoPai", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath justificativa = createString("justificativa");

    public final StringPath nome = createString("nome");

    public final StringPath situacao = createString("situacao");

    public final StringPath tipo = createString("tipo");

    public QElementoSubelemenEntity(String variable) {
        super(ElementoSubelemenEntity.class, forVariable(variable));
    }

    public QElementoSubelemenEntity(Path<? extends ElementoSubelemenEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QElementoSubelemenEntity(PathMetadata metadata) {
        super(ElementoSubelemenEntity.class, metadata);
    }

}

