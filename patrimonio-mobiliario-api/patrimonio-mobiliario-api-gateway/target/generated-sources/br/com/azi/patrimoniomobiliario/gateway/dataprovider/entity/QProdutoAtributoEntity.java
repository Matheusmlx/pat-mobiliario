package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QProdutoAtributoEntity is a Querydsl query type for ProdutoAtributoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProdutoAtributoEntity extends EntityPathBase<ProdutoAtributoEntity> {

    private static final long serialVersionUID = -1526922974L;

    public static final QProdutoAtributoEntity produtoAtributoEntity = new QProdutoAtributoEntity("produtoAtributoEntity");

    public final StringPath atributo = createString("atributo");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> produtoId = createNumber("produtoId", Long.class);

    public final StringPath tipo = createString("tipo");

    public final StringPath valor = createString("valor");

    public QProdutoAtributoEntity(String variable) {
        super(ProdutoAtributoEntity.class, forVariable(variable));
    }

    public QProdutoAtributoEntity(Path<? extends ProdutoAtributoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProdutoAtributoEntity(PathMetadata metadata) {
        super(ProdutoAtributoEntity.class, metadata);
    }

}

