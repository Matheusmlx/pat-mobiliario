package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContratacaoSubelementoEntity is a Querydsl query type for ContratacaoSubelementoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContratacaoSubelementoEntity extends EntityPathBase<ContratacaoSubelementoEntity> {

    private static final long serialVersionUID = -980300699L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContratacaoSubelementoEntity contratacaoSubelementoEntity = new QContratacaoSubelementoEntity("contratacaoSubelementoEntity");

    public final QElementoSubelemenEntity elementoSubElemento;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QNaturezaContratacaoEntity naturezaContratacao;

    public QContratacaoSubelementoEntity(String variable) {
        this(ContratacaoSubelementoEntity.class, forVariable(variable), INITS);
    }

    public QContratacaoSubelementoEntity(Path<? extends ContratacaoSubelementoEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContratacaoSubelementoEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContratacaoSubelementoEntity(PathMetadata metadata, PathInits inits) {
        this(ContratacaoSubelementoEntity.class, metadata, inits);
    }

    public QContratacaoSubelementoEntity(Class<? extends ContratacaoSubelementoEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.elementoSubElemento = inits.isInitialized("elementoSubElemento") ? new QElementoSubelemenEntity(forProperty("elementoSubElemento")) : null;
        this.naturezaContratacao = inits.isInitialized("naturezaContratacao") ? new QNaturezaContratacaoEntity(forProperty("naturezaContratacao")) : null;
    }

}

