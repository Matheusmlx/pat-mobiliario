package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QEstadoConservacaoEntity is a Querydsl query type for EstadoConservacaoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QEstadoConservacaoEntity extends EntityPathBase<EstadoConservacaoEntity> {

    private static final long serialVersionUID = -1380728013L;

    public static final QEstadoConservacaoEntity estadoConservacaoEntity = new QEstadoConservacaoEntity("estadoConservacaoEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nome = createString("nome");

    public final NumberPath<Integer> prioridade = createNumber("prioridade", Integer.class);

    public QEstadoConservacaoEntity(String variable) {
        super(EstadoConservacaoEntity.class, forVariable(variable));
    }

    public QEstadoConservacaoEntity(Path<? extends EstadoConservacaoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEstadoConservacaoEntity(PathMetadata metadata) {
        super(EstadoConservacaoEntity.class, metadata);
    }

}

