package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReservaIntervaloEntity is a Querydsl query type for ReservaIntervaloEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReservaIntervaloEntity extends EntityPathBase<ReservaIntervaloEntity> {

    private static final long serialVersionUID = -2130441817L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReservaIntervaloEntity reservaIntervaloEntity = new QReservaIntervaloEntity("reservaIntervaloEntity");

    public final br.com.azi.hal.core.generic.entity.QBaseObject _super = new br.com.azi.hal.core.generic.entity.QBaseObject(this);

    public final StringPath codigo = createString("codigo");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = _super.dataAlteracao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataCadastro = _super.dataCadastro;

    public final DateTimePath<java.util.Date> dataFinalizacao = createDateTime("dataFinalizacao", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> numeroFim = createNumber("numeroFim", Long.class);

    public final NumberPath<Long> numeroInicio = createNumber("numeroInicio", Long.class);

    public final StringPath numeroTermo = createString("numeroTermo");

    public final QUnidadeOrganizacionalEntity orgao;

    public final StringPath preenchimento = createString("preenchimento");

    public final NumberPath<Long> quantidadeReservada = createNumber("quantidadeReservada", Long.class);

    public final QReservaEntity reserva;

    public final StringPath situacao = createString("situacao");

    //inherited
    public final StringPath usuarioAlteracao = _super.usuarioAlteracao;

    //inherited
    public final StringPath usuarioCadastro = _super.usuarioCadastro;

    public QReservaIntervaloEntity(String variable) {
        this(ReservaIntervaloEntity.class, forVariable(variable), INITS);
    }

    public QReservaIntervaloEntity(Path<? extends ReservaIntervaloEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReservaIntervaloEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReservaIntervaloEntity(PathMetadata metadata, PathInits inits) {
        this(ReservaIntervaloEntity.class, metadata, inits);
    }

    public QReservaIntervaloEntity(Class<? extends ReservaIntervaloEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.orgao = inits.isInitialized("orgao") ? new QUnidadeOrganizacionalEntity(forProperty("orgao")) : null;
        this.reserva = inits.isInitialized("reserva") ? new QReservaEntity(forProperty("reserva")) : null;
    }

}

