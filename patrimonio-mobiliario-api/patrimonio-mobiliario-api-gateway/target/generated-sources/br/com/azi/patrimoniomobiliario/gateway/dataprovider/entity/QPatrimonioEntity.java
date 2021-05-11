package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPatrimonioEntity is a Querydsl query type for PatrimonioEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPatrimonioEntity extends EntityPathBase<PatrimonioEntity> {

    private static final long serialVersionUID = 471849869L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPatrimonioEntity patrimonioEntity = new QPatrimonioEntity("patrimonioEntity");

    public final br.com.azi.hal.core.generic.entity.QBaseObject _super = new br.com.azi.hal.core.generic.entity.QBaseObject(this);

    public final StringPath chassi = createString("chassi");

    public final QComodanteEntity comodante;

    public final QContaContabilEntity contaContabilAtual;

    public final QContaContabilEntity contaContabilClassificacao;

    public final QConvenioEntity convenio;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = _super.dataAlteracao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataCadastro = _super.dataCadastro;

    public final DateTimePath<java.util.Date> dataEstorno = createDateTime("dataEstorno", java.util.Date.class);

    public final BooleanPath depreciavel = createBoolean("depreciavel");

    public final BooleanPath diferencaDizima = createBoolean("diferencaDizima");

    public final QEstadoConservacaoEntity estadoConservacao;

    public final DateTimePath<java.util.Date> fimVidaUtil = createDateTime("fimVidaUtil", java.util.Date.class);

    public final QUnidadeOrganizacionalEntity fundo;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> inicioVidaUtil = createDateTime("inicioVidaUtil", java.util.Date.class);

    public final QItemIncorporacaoEntity itemIncorporacao;

    public final StringPath licenciamento = createString("licenciamento");

    public final StringPath motivoEstorno = createString("motivoEstorno");

    public final StringPath motor = createString("motor");

    public final QNaturezaDespesaEntity naturezaDespesa;

    public final StringPath numero = createString("numero");

    public final StringPath numeroSerie = createString("numeroSerie");

    public final QUnidadeOrganizacionalEntity orgao;

    public final StringPath placa = createString("placa");

    public final StringPath projeto = createString("projeto");

    public final StringPath renavam = createString("renavam");

    public final QUnidadeOrganizacionalEntity setor;

    public final StringPath situacao = createString("situacao");

    public final StringPath uriImagem = createString("uriImagem");

    //inherited
    public final StringPath usuarioAlteracao = _super.usuarioAlteracao;

    //inherited
    public final StringPath usuarioCadastro = _super.usuarioCadastro;

    public final StringPath usuarioEstorno = createString("usuarioEstorno");

    public final NumberPath<java.math.BigDecimal> valorAquisicao = createNumber("valorAquisicao", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> valorDepreciacaoMensal = createNumber("valorDepreciacaoMensal", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> valorEntrada = createNumber("valorEntrada", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> valorLiquido = createNumber("valorLiquido", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> valorResidual = createNumber("valorResidual", java.math.BigDecimal.class);

    public QPatrimonioEntity(String variable) {
        this(PatrimonioEntity.class, forVariable(variable), INITS);
    }

    public QPatrimonioEntity(Path<? extends PatrimonioEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPatrimonioEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPatrimonioEntity(PathMetadata metadata, PathInits inits) {
        this(PatrimonioEntity.class, metadata, inits);
    }

    public QPatrimonioEntity(Class<? extends PatrimonioEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.comodante = inits.isInitialized("comodante") ? new QComodanteEntity(forProperty("comodante")) : null;
        this.contaContabilAtual = inits.isInitialized("contaContabilAtual") ? new QContaContabilEntity(forProperty("contaContabilAtual")) : null;
        this.contaContabilClassificacao = inits.isInitialized("contaContabilClassificacao") ? new QContaContabilEntity(forProperty("contaContabilClassificacao")) : null;
        this.convenio = inits.isInitialized("convenio") ? new QConvenioEntity(forProperty("convenio"), inits.get("convenio")) : null;
        this.estadoConservacao = inits.isInitialized("estadoConservacao") ? new QEstadoConservacaoEntity(forProperty("estadoConservacao")) : null;
        this.fundo = inits.isInitialized("fundo") ? new QUnidadeOrganizacionalEntity(forProperty("fundo")) : null;
        this.itemIncorporacao = inits.isInitialized("itemIncorporacao") ? new QItemIncorporacaoEntity(forProperty("itemIncorporacao"), inits.get("itemIncorporacao")) : null;
        this.naturezaDespesa = inits.isInitialized("naturezaDespesa") ? new QNaturezaDespesaEntity(forProperty("naturezaDespesa"), inits.get("naturezaDespesa")) : null;
        this.orgao = inits.isInitialized("orgao") ? new QUnidadeOrganizacionalEntity(forProperty("orgao")) : null;
        this.setor = inits.isInitialized("setor") ? new QUnidadeOrganizacionalEntity(forProperty("setor")) : null;
    }

}

