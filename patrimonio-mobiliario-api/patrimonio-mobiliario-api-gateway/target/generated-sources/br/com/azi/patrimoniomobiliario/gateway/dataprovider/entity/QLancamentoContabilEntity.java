package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLancamentoContabilEntity is a Querydsl query type for LancamentoContabilEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QLancamentoContabilEntity extends EntityPathBase<LancamentoContabilEntity> {

    private static final long serialVersionUID = 193010437L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLancamentoContabilEntity lancamentoContabilEntity = new QLancamentoContabilEntity("lancamentoContabilEntity");

    public final br.com.azi.hal.core.generic.entity.QBaseObject _super = new br.com.azi.hal.core.generic.entity.QBaseObject(this);

    public final QContaContabilEntity contaContabil;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = _super.dataAlteracao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataCadastro = _super.dataCadastro;

    public final DateTimePath<java.util.Date> dataLancamento = createDateTime("dataLancamento", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QIncorporacaoEntity incorporacao;

    public final QMovimentacaoEntity movimentacao;

    public final QUnidadeOrganizacionalEntity orgao;

    public final QPatrimonioEntity patrimonio;

    public final QUnidadeOrganizacionalEntity setor;

    public final StringPath tipoLancamento = createString("tipoLancamento");

    public final StringPath tipoMovimentacao = createString("tipoMovimentacao");

    //inherited
    public final StringPath usuarioAlteracao = _super.usuarioAlteracao;

    //inherited
    public final StringPath usuarioCadastro = _super.usuarioCadastro;

    public final NumberPath<java.math.BigDecimal> valor = createNumber("valor", java.math.BigDecimal.class);

    public QLancamentoContabilEntity(String variable) {
        this(LancamentoContabilEntity.class, forVariable(variable), INITS);
    }

    public QLancamentoContabilEntity(Path<? extends LancamentoContabilEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLancamentoContabilEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLancamentoContabilEntity(PathMetadata metadata, PathInits inits) {
        this(LancamentoContabilEntity.class, metadata, inits);
    }

    public QLancamentoContabilEntity(Class<? extends LancamentoContabilEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contaContabil = inits.isInitialized("contaContabil") ? new QContaContabilEntity(forProperty("contaContabil")) : null;
        this.incorporacao = inits.isInitialized("incorporacao") ? new QIncorporacaoEntity(forProperty("incorporacao"), inits.get("incorporacao")) : null;
        this.movimentacao = inits.isInitialized("movimentacao") ? new QMovimentacaoEntity(forProperty("movimentacao"), inits.get("movimentacao")) : null;
        this.orgao = inits.isInitialized("orgao") ? new QUnidadeOrganizacionalEntity(forProperty("orgao")) : null;
        this.patrimonio = inits.isInitialized("patrimonio") ? new QPatrimonioEntity(forProperty("patrimonio"), inits.get("patrimonio")) : null;
        this.setor = inits.isInitialized("setor") ? new QUnidadeOrganizacionalEntity(forProperty("setor")) : null;
    }

}

