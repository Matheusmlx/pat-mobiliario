package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QConfiguracaoDepreciacaoEntity is a Querydsl query type for ConfiguracaoDepreciacaoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QConfiguracaoDepreciacaoEntity extends EntityPathBase<ConfiguracaoDepreciacaoEntity> {

    private static final long serialVersionUID = 541160794L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QConfiguracaoDepreciacaoEntity configuracaoDepreciacaoEntity = new QConfiguracaoDepreciacaoEntity("configuracaoDepreciacaoEntity");

    public final br.com.azi.hal.core.generic.entity.QBaseObject _super = new br.com.azi.hal.core.generic.entity.QBaseObject(this);

    public final QContaContabilEntity contaContabil;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = _super.dataAlteracao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataCadastro = _super.dataCadastro;

    public final BooleanPath depreciavel = createBoolean("depreciavel");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> percentualResidual = createNumber("percentualResidual", java.math.BigDecimal.class);

    //inherited
    public final StringPath usuarioAlteracao = _super.usuarioAlteracao;

    //inherited
    public final StringPath usuarioCadastro = _super.usuarioCadastro;

    public final NumberPath<Integer> vidaUtil = createNumber("vidaUtil", Integer.class);

    public QConfiguracaoDepreciacaoEntity(String variable) {
        this(ConfiguracaoDepreciacaoEntity.class, forVariable(variable), INITS);
    }

    public QConfiguracaoDepreciacaoEntity(Path<? extends ConfiguracaoDepreciacaoEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QConfiguracaoDepreciacaoEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QConfiguracaoDepreciacaoEntity(PathMetadata metadata, PathInits inits) {
        this(ConfiguracaoDepreciacaoEntity.class, metadata, inits);
    }

    public QConfiguracaoDepreciacaoEntity(Class<? extends ConfiguracaoDepreciacaoEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contaContabil = inits.isInitialized("contaContabil") ? new QContaContabilEntity(forProperty("contaContabil")) : null;
    }

}

