package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFuncaoEntity is a Querydsl query type for FuncaoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFuncaoEntity extends EntityPathBase<FuncaoEntity> {

    private static final long serialVersionUID = 402864039L;

    public static final QFuncaoEntity funcaoEntity = new QFuncaoEntity("funcaoEntity");

    public final StringPath descricao = createString("descricao");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nome = createString("nome");

    public QFuncaoEntity(String variable) {
        super(FuncaoEntity.class, forVariable(variable));
    }

    public QFuncaoEntity(Path<? extends FuncaoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFuncaoEntity(PathMetadata metadata) {
        super(FuncaoEntity.class, metadata);
    }

}

