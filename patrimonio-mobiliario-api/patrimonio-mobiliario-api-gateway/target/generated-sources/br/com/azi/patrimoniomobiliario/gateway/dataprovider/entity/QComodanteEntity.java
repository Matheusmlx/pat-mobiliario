package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QComodanteEntity is a Querydsl query type for ComodanteEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QComodanteEntity extends EntityPathBase<ComodanteEntity> {

    private static final long serialVersionUID = -1390761979L;

    public static final QComodanteEntity comodanteEntity = new QComodanteEntity("comodanteEntity");

    public final br.com.azi.hal.core.generic.entity.QBaseObject _super = new br.com.azi.hal.core.generic.entity.QBaseObject(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = _super.dataAlteracao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataCadastro = _super.dataCadastro;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nome = createString("nome");

    //inherited
    public final StringPath usuarioAlteracao = _super.usuarioAlteracao;

    //inherited
    public final StringPath usuarioCadastro = _super.usuarioCadastro;

    public QComodanteEntity(String variable) {
        super(ComodanteEntity.class, forVariable(variable));
    }

    public QComodanteEntity(Path<? extends ComodanteEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QComodanteEntity(PathMetadata metadata) {
        super(ComodanteEntity.class, metadata);
    }

}

