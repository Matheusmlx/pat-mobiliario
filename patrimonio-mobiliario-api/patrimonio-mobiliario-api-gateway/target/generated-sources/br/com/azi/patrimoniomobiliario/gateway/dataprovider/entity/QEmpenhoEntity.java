package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmpenhoEntity is a Querydsl query type for EmpenhoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QEmpenhoEntity extends EntityPathBase<EmpenhoEntity> {

    private static final long serialVersionUID = -427782839L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmpenhoEntity empenhoEntity = new QEmpenhoEntity("empenhoEntity");

    public final br.com.azi.hal.core.generic.entity.QBaseObject _super = new br.com.azi.hal.core.generic.entity.QBaseObject(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = _super.dataAlteracao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataCadastro = _super.dataCadastro;

    public final DateTimePath<java.util.Date> dataEmpenho = createDateTime("dataEmpenho", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QIncorporacaoEntity incorporacao;

    public final StringPath numeroEmpenho = createString("numeroEmpenho");

    //inherited
    public final StringPath usuarioAlteracao = _super.usuarioAlteracao;

    //inherited
    public final StringPath usuarioCadastro = _super.usuarioCadastro;

    public final NumberPath<java.math.BigDecimal> valorEmpenho = createNumber("valorEmpenho", java.math.BigDecimal.class);

    public QEmpenhoEntity(String variable) {
        this(EmpenhoEntity.class, forVariable(variable), INITS);
    }

    public QEmpenhoEntity(Path<? extends EmpenhoEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmpenhoEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmpenhoEntity(PathMetadata metadata, PathInits inits) {
        this(EmpenhoEntity.class, metadata, inits);
    }

    public QEmpenhoEntity(Class<? extends EmpenhoEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.incorporacao = inits.isInitialized("incorporacao") ? new QIncorporacaoEntity(forProperty("incorporacao"), inits.get("incorporacao")) : null;
    }

}

