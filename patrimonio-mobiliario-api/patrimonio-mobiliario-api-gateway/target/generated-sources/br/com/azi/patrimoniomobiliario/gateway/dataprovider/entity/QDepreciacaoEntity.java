package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDepreciacaoEntity is a Querydsl query type for DepreciacaoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDepreciacaoEntity extends EntityPathBase<DepreciacaoEntity> {

    private static final long serialVersionUID = 306167881L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDepreciacaoEntity depreciacaoEntity = new QDepreciacaoEntity("depreciacaoEntity");

    public final br.com.azi.hal.core.generic.entity.QBaseObject _super = new br.com.azi.hal.core.generic.entity.QBaseObject(this);

    public final QContaContabilEntity contaContabil;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = _super.dataAlteracao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataCadastro = _super.dataCadastro;

    public final DateTimePath<java.util.Date> dataFinal = createDateTime("dataFinal", java.util.Date.class);

    public final DateTimePath<java.util.Date> dataInicial = createDateTime("dataInicial", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath mesReferencia = createString("mesReferencia");

    public final QUnidadeOrganizacionalEntity orgao;

    public final QPatrimonioEntity patrimonio;

    public final QUnidadeOrganizacionalEntity setor;

    public final NumberPath<java.math.BigDecimal> taxaAplicada = createNumber("taxaAplicada", java.math.BigDecimal.class);

    //inherited
    public final StringPath usuarioAlteracao = _super.usuarioAlteracao;

    //inherited
    public final StringPath usuarioCadastro = _super.usuarioCadastro;

    public final NumberPath<java.math.BigDecimal> valorAnterior = createNumber("valorAnterior", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> valorPosterior = createNumber("valorPosterior", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> valorSubtraido = createNumber("valorSubtraido", java.math.BigDecimal.class);

    public QDepreciacaoEntity(String variable) {
        this(DepreciacaoEntity.class, forVariable(variable), INITS);
    }

    public QDepreciacaoEntity(Path<? extends DepreciacaoEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDepreciacaoEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDepreciacaoEntity(PathMetadata metadata, PathInits inits) {
        this(DepreciacaoEntity.class, metadata, inits);
    }

    public QDepreciacaoEntity(Class<? extends DepreciacaoEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contaContabil = inits.isInitialized("contaContabil") ? new QContaContabilEntity(forProperty("contaContabil")) : null;
        this.orgao = inits.isInitialized("orgao") ? new QUnidadeOrganizacionalEntity(forProperty("orgao")) : null;
        this.patrimonio = inits.isInitialized("patrimonio") ? new QPatrimonioEntity(forProperty("patrimonio"), inits.get("patrimonio")) : null;
        this.setor = inits.isInitialized("setor") ? new QUnidadeOrganizacionalEntity(forProperty("setor")) : null;
    }

}

