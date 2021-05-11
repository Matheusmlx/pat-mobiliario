package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QConfigContaContabilEntity is a Querydsl query type for ConfigContaContabilEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QConfigContaContabilEntity extends EntityPathBase<ConfigContaContabilEntity> {

    private static final long serialVersionUID = 458036084L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QConfigContaContabilEntity configContaContabilEntity = new QConfigContaContabilEntity("configContaContabilEntity");

    public final br.com.azi.hal.core.generic.entity.QBaseObject _super = new br.com.azi.hal.core.generic.entity.QBaseObject(this);

    public final QContaContabilEntity contaContabil;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = _super.dataAlteracao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataCadastro = _super.dataCadastro;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath metodo = createString("metodo");

    public final NumberPath<java.math.BigDecimal> percentualResidual = createNumber("percentualResidual", java.math.BigDecimal.class);

    public final StringPath tipo = createString("tipo");

    public final StringPath tipoBem = createString("tipoBem");

    //inherited
    public final StringPath usuarioAlteracao = _super.usuarioAlteracao;

    //inherited
    public final StringPath usuarioCadastro = _super.usuarioCadastro;

    public final NumberPath<Integer> vidaUtil = createNumber("vidaUtil", Integer.class);

    public QConfigContaContabilEntity(String variable) {
        this(ConfigContaContabilEntity.class, forVariable(variable), INITS);
    }

    public QConfigContaContabilEntity(Path<? extends ConfigContaContabilEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QConfigContaContabilEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QConfigContaContabilEntity(PathMetadata metadata, PathInits inits) {
        this(ConfigContaContabilEntity.class, metadata, inits);
    }

    public QConfigContaContabilEntity(Class<? extends ConfigContaContabilEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contaContabil = inits.isInitialized("contaContabil") ? new QContaContabilEntity(forProperty("contaContabil")) : null;
    }

}

