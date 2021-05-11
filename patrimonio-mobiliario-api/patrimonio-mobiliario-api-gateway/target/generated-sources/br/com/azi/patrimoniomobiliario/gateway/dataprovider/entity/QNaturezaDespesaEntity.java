package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNaturezaDespesaEntity is a Querydsl query type for NaturezaDespesaEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QNaturezaDespesaEntity extends EntityPathBase<NaturezaDespesaEntity> {

    private static final long serialVersionUID = -1437981704L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNaturezaDespesaEntity naturezaDespesaEntity = new QNaturezaDespesaEntity("naturezaDespesaEntity");

    public final QContaContabilEntity contaContabil;

    public final StringPath descricao = createString("descricao");

    public final StringPath despesa = createString("despesa");

    public final QElementoSubelemenEntity elementoSubElemento;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath justificativa = createString("justificativa");

    public final StringPath situacao = createString("situacao");

    public QNaturezaDespesaEntity(String variable) {
        this(NaturezaDespesaEntity.class, forVariable(variable), INITS);
    }

    public QNaturezaDespesaEntity(Path<? extends NaturezaDespesaEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNaturezaDespesaEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNaturezaDespesaEntity(PathMetadata metadata, PathInits inits) {
        this(NaturezaDespesaEntity.class, metadata, inits);
    }

    public QNaturezaDespesaEntity(Class<? extends NaturezaDespesaEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contaContabil = inits.isInitialized("contaContabil") ? new QContaContabilEntity(forProperty("contaContabil")) : null;
        this.elementoSubElemento = inits.isInitialized("elementoSubElemento") ? new QElementoSubelemenEntity(forProperty("elementoSubElemento")) : null;
    }

}

