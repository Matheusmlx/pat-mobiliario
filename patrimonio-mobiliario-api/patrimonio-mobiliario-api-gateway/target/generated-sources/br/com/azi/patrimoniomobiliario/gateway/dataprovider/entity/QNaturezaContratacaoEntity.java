package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QNaturezaContratacaoEntity is a Querydsl query type for NaturezaContratacaoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QNaturezaContratacaoEntity extends EntityPathBase<NaturezaContratacaoEntity> {

    private static final long serialVersionUID = -458263194L;

    public static final QNaturezaContratacaoEntity naturezaContratacaoEntity = new QNaturezaContratacaoEntity("naturezaContratacaoEntity");

    public final StringPath descricao = createString("descricao");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath situacao = createString("situacao");

    public final StringPath tipo = createString("tipo");

    public QNaturezaContratacaoEntity(String variable) {
        super(NaturezaContratacaoEntity.class, forVariable(variable));
    }

    public QNaturezaContratacaoEntity(Path<? extends NaturezaContratacaoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNaturezaContratacaoEntity(PathMetadata metadata) {
        super(NaturezaContratacaoEntity.class, metadata);
    }

}

