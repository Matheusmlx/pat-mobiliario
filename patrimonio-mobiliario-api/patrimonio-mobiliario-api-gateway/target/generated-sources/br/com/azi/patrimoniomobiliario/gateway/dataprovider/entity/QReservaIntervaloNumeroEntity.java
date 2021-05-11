package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReservaIntervaloNumeroEntity is a Querydsl query type for ReservaIntervaloNumeroEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReservaIntervaloNumeroEntity extends EntityPathBase<ReservaIntervaloNumeroEntity> {

    private static final long serialVersionUID = 1006106083L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReservaIntervaloNumeroEntity reservaIntervaloNumeroEntity = new QReservaIntervaloNumeroEntity("reservaIntervaloNumeroEntity");

    public final br.com.azi.hal.core.generic.entity.QBaseObject _super = new br.com.azi.hal.core.generic.entity.QBaseObject(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = _super.dataAlteracao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataCadastro = _super.dataCadastro;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> numero = createNumber("numero", Long.class);

    public final QReservaIntervaloEntity reservaIntervalo;

    //inherited
    public final StringPath usuarioAlteracao = _super.usuarioAlteracao;

    //inherited
    public final StringPath usuarioCadastro = _super.usuarioCadastro;

    public final BooleanPath utilizado = createBoolean("utilizado");

    public QReservaIntervaloNumeroEntity(String variable) {
        this(ReservaIntervaloNumeroEntity.class, forVariable(variable), INITS);
    }

    public QReservaIntervaloNumeroEntity(Path<? extends ReservaIntervaloNumeroEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReservaIntervaloNumeroEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReservaIntervaloNumeroEntity(PathMetadata metadata, PathInits inits) {
        this(ReservaIntervaloNumeroEntity.class, metadata, inits);
    }

    public QReservaIntervaloNumeroEntity(Class<? extends ReservaIntervaloNumeroEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reservaIntervalo = inits.isInitialized("reservaIntervalo") ? new QReservaIntervaloEntity(forProperty("reservaIntervalo"), inits.get("reservaIntervalo")) : null;
    }

}

