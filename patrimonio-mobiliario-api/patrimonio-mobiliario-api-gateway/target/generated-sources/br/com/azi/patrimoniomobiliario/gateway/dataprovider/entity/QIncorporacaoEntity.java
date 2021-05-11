package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QIncorporacaoEntity is a Querydsl query type for IncorporacaoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIncorporacaoEntity extends EntityPathBase<IncorporacaoEntity> {

    private static final long serialVersionUID = -448649L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QIncorporacaoEntity incorporacaoEntity = new QIncorporacaoEntity("incorporacaoEntity");

    public final br.com.azi.hal.core.generic.entity.QBaseObject _super = new br.com.azi.hal.core.generic.entity.QBaseObject(this);

    public final StringPath codigo = createString("codigo");

    public final QComodanteEntity comodanteEntity;

    public final QConvenioEntity convenio;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = _super.dataAlteracao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataCadastro = _super.dataCadastro;

    public final DateTimePath<java.util.Date> dataFimProcessamento = createDateTime("dataFimProcessamento", java.util.Date.class);

    public final DateTimePath<java.util.Date> dataFinalizacao = createDateTime("dataFinalizacao", java.util.Date.class);

    public final DateTimePath<java.util.Date> dataInicioProcessamento = createDateTime("dataInicioProcessamento", java.util.Date.class);

    public final DateTimePath<java.util.Date> dataNota = createDateTime("dataNota", java.util.Date.class);

    public final DateTimePath<java.util.Date> dataRecebimento = createDateTime("dataRecebimento", java.util.Date.class);

    public final StringPath erroProcessamento = createString("erroProcessamento");

    public final QFornecedorEntity fornecedor;

    public final QUnidadeOrganizacionalEntity fundo;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nota = createString("nota");

    public final QNotaLancamentoContabilEntity notaLancamentoContabil;

    public final StringPath numProcesso = createString("numProcesso");

    public final QUnidadeOrganizacionalEntity orgao;

    public final BooleanPath origemComodato = createBoolean("origemComodato");

    public final BooleanPath origemConvenio = createBoolean("origemConvenio");

    public final BooleanPath origemFundo = createBoolean("origemFundo");

    public final BooleanPath origemProjeto = createBoolean("origemProjeto");

    public final StringPath projeto = createString("projeto");

    public final QReconhecimentoEntity reconhecimento;

    public final QUnidadeOrganizacionalEntity setor;

    public final StringPath situacao = createString("situacao");

    //inherited
    public final StringPath usuarioAlteracao = _super.usuarioAlteracao;

    //inherited
    public final StringPath usuarioCadastro = _super.usuarioCadastro;

    public final StringPath usuarioFinalizacao = createString("usuarioFinalizacao");

    public final NumberPath<java.math.BigDecimal> valorNota = createNumber("valorNota", java.math.BigDecimal.class);

    public QIncorporacaoEntity(String variable) {
        this(IncorporacaoEntity.class, forVariable(variable), INITS);
    }

    public QIncorporacaoEntity(Path<? extends IncorporacaoEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QIncorporacaoEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QIncorporacaoEntity(PathMetadata metadata, PathInits inits) {
        this(IncorporacaoEntity.class, metadata, inits);
    }

    public QIncorporacaoEntity(Class<? extends IncorporacaoEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.comodanteEntity = inits.isInitialized("comodanteEntity") ? new QComodanteEntity(forProperty("comodanteEntity")) : null;
        this.convenio = inits.isInitialized("convenio") ? new QConvenioEntity(forProperty("convenio"), inits.get("convenio")) : null;
        this.fornecedor = inits.isInitialized("fornecedor") ? new QFornecedorEntity(forProperty("fornecedor")) : null;
        this.fundo = inits.isInitialized("fundo") ? new QUnidadeOrganizacionalEntity(forProperty("fundo")) : null;
        this.notaLancamentoContabil = inits.isInitialized("notaLancamentoContabil") ? new QNotaLancamentoContabilEntity(forProperty("notaLancamentoContabil")) : null;
        this.orgao = inits.isInitialized("orgao") ? new QUnidadeOrganizacionalEntity(forProperty("orgao")) : null;
        this.reconhecimento = inits.isInitialized("reconhecimento") ? new QReconhecimentoEntity(forProperty("reconhecimento")) : null;
        this.setor = inits.isInitialized("setor") ? new QUnidadeOrganizacionalEntity(forProperty("setor")) : null;
    }

}

