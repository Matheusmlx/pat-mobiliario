package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMovimentacaoEntity is a Querydsl query type for MovimentacaoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMovimentacaoEntity extends EntityPathBase<MovimentacaoEntity> {

    private static final long serialVersionUID = 2126188664L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMovimentacaoEntity movimentacaoEntity = new QMovimentacaoEntity("movimentacaoEntity");

    public final br.com.azi.hal.core.generic.entity.QBaseObject _super = new br.com.azi.hal.core.generic.entity.QBaseObject(this);

    public final StringPath codigo = createString("codigo");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = _super.dataAlteracao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataCadastro = _super.dataCadastro;

    public final DateTimePath<java.util.Date> dataDevolucao = createDateTime("dataDevolucao", java.util.Date.class);

    public final DateTimePath<java.util.Date> dataEnvio = createDateTime("dataEnvio", java.util.Date.class);

    public final DateTimePath<java.util.Date> dataFimProcessamento = createDateTime("dataFimProcessamento", java.util.Date.class);

    public final DateTimePath<java.util.Date> dataFinalizacao = createDateTime("dataFinalizacao", java.util.Date.class);

    public final DateTimePath<java.util.Date> dataInicioProcessamento = createDateTime("dataInicioProcessamento", java.util.Date.class);

    public final StringPath erroProcessamento = createString("erroProcessamento");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath motivoObservacao = createString("motivoObservacao");

    public final QNotaLancamentoContabilEntity notaLancamentoContabil;

    public final StringPath numeroProcesso = createString("numeroProcesso");

    public final StringPath numeroTermoResponsabilidade = createString("numeroTermoResponsabilidade");

    public final QUnidadeOrganizacionalEntity orgaoDestino;

    public final QUnidadeOrganizacionalEntity orgaoOrigem;

    public final QResponsavelEntity responsavel;

    public final QUnidadeOrganizacionalEntity setorDestino;

    public final QUnidadeOrganizacionalEntity setorOrigem;

    public final StringPath situacao = createString("situacao");

    public final StringPath tipo = createString("tipo");

    //inherited
    public final StringPath usuarioAlteracao = _super.usuarioAlteracao;

    //inherited
    public final StringPath usuarioCadastro = _super.usuarioCadastro;

    public final StringPath usuarioCriacao = createString("usuarioCriacao");

    public final StringPath usuarioFinalizacao = createString("usuarioFinalizacao");

    public QMovimentacaoEntity(String variable) {
        this(MovimentacaoEntity.class, forVariable(variable), INITS);
    }

    public QMovimentacaoEntity(Path<? extends MovimentacaoEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMovimentacaoEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMovimentacaoEntity(PathMetadata metadata, PathInits inits) {
        this(MovimentacaoEntity.class, metadata, inits);
    }

    public QMovimentacaoEntity(Class<? extends MovimentacaoEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.notaLancamentoContabil = inits.isInitialized("notaLancamentoContabil") ? new QNotaLancamentoContabilEntity(forProperty("notaLancamentoContabil")) : null;
        this.orgaoDestino = inits.isInitialized("orgaoDestino") ? new QUnidadeOrganizacionalEntity(forProperty("orgaoDestino")) : null;
        this.orgaoOrigem = inits.isInitialized("orgaoOrigem") ? new QUnidadeOrganizacionalEntity(forProperty("orgaoOrigem")) : null;
        this.responsavel = inits.isInitialized("responsavel") ? new QResponsavelEntity(forProperty("responsavel")) : null;
        this.setorDestino = inits.isInitialized("setorDestino") ? new QUnidadeOrganizacionalEntity(forProperty("setorDestino")) : null;
        this.setorOrigem = inits.isInitialized("setorOrigem") ? new QUnidadeOrganizacionalEntity(forProperty("setorOrigem")) : null;
    }

}

