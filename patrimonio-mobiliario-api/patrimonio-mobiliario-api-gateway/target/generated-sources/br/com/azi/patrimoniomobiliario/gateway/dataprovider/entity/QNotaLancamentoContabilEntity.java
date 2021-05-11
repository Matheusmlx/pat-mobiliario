package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QNotaLancamentoContabilEntity is a Querydsl query type for NotaLancamentoContabilEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QNotaLancamentoContabilEntity extends EntityPathBase<NotaLancamentoContabilEntity> {

    private static final long serialVersionUID = -2059396077L;

    public static final QNotaLancamentoContabilEntity notaLancamentoContabilEntity = new QNotaLancamentoContabilEntity("notaLancamentoContabilEntity");

    public final br.com.azi.hal.core.generic.entity.QBaseObject _super = new br.com.azi.hal.core.generic.entity.QBaseObject(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = _super.dataAlteracao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataCadastro = _super.dataCadastro;

    public final DateTimePath<java.util.Date> dataLancamento = createDateTime("dataLancamento", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath numero = createString("numero");

    //inherited
    public final StringPath usuarioAlteracao = _super.usuarioAlteracao;

    //inherited
    public final StringPath usuarioCadastro = _super.usuarioCadastro;

    public QNotaLancamentoContabilEntity(String variable) {
        super(NotaLancamentoContabilEntity.class, forVariable(variable));
    }

    public QNotaLancamentoContabilEntity(Path<? extends NotaLancamentoContabilEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNotaLancamentoContabilEntity(PathMetadata metadata) {
        super(NotaLancamentoContabilEntity.class, metadata);
    }

}

