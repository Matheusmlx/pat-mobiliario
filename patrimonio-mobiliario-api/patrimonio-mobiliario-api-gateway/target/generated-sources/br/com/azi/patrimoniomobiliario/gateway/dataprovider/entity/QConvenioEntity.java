package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QConvenioEntity is a Querydsl query type for ConvenioEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QConvenioEntity extends EntityPathBase<ConvenioEntity> {

    private static final long serialVersionUID = -1310323400L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QConvenioEntity convenioEntity = new QConvenioEntity("convenioEntity");

    public final br.com.azi.hal.core.generic.entity.QBaseObject _super = new br.com.azi.hal.core.generic.entity.QBaseObject(this);

    public final QConcedenteEntity concedente;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = _super.dataAlteracao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataCadastro = _super.dataCadastro;

    public final DateTimePath<java.util.Date> dataVigenciaFim = createDateTime("dataVigenciaFim", java.util.Date.class);

    public final DateTimePath<java.util.Date> dataVigenciaInicio = createDateTime("dataVigenciaInicio", java.util.Date.class);

    public final StringPath fonteRecurso = createString("fonteRecurso");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nome = createString("nome");

    public final StringPath numero = createString("numero");

    public final StringPath situacao = createString("situacao");

    public final StringPath tipo = createString("tipo");

    //inherited
    public final StringPath usuarioAlteracao = _super.usuarioAlteracao;

    //inherited
    public final StringPath usuarioCadastro = _super.usuarioCadastro;

    public QConvenioEntity(String variable) {
        this(ConvenioEntity.class, forVariable(variable), INITS);
    }

    public QConvenioEntity(Path<? extends ConvenioEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QConvenioEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QConvenioEntity(PathMetadata metadata, PathInits inits) {
        this(ConvenioEntity.class, metadata, inits);
    }

    public QConvenioEntity(Class<? extends ConvenioEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.concedente = inits.isInitialized("concedente") ? new QConcedenteEntity(forProperty("concedente")) : null;
    }

}

