package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QProdutoEntity is a Querydsl query type for ProdutoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProdutoEntity extends EntityPathBase<ProdutoEntity> {

    private static final long serialVersionUID = -1344729270L;

    public static final QProdutoEntity produtoEntity = new QProdutoEntity("produtoEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nome = createString("nome");

    public QProdutoEntity(String variable) {
        super(ProdutoEntity.class, forVariable(variable));
    }

    public QProdutoEntity(Path<? extends ProdutoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProdutoEntity(PathMetadata metadata) {
        super(ProdutoEntity.class, metadata);
    }

}

