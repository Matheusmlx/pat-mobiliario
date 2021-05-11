package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDocumentoEntity is a Querydsl query type for DocumentoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDocumentoEntity extends EntityPathBase<DocumentoEntity> {

    private static final long serialVersionUID = 36704069L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDocumentoEntity documentoEntity = new QDocumentoEntity("documentoEntity");

    public final br.com.azi.hal.core.generic.entity.QBaseObject _super = new br.com.azi.hal.core.generic.entity.QBaseObject(this);

    public final DateTimePath<java.util.Date> data = createDateTime("data", java.util.Date.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = _super.dataAlteracao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataCadastro = _super.dataCadastro;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QIncorporacaoEntity incorporacao;

    public final QMovimentacaoEntity movimentacao;

    public final StringPath numero = createString("numero");

    public final QTipoDocumentoEntity tipoDocumento;

    public final StringPath uriAnexo = createString("uriAnexo");

    //inherited
    public final StringPath usuarioAlteracao = _super.usuarioAlteracao;

    //inherited
    public final StringPath usuarioCadastro = _super.usuarioCadastro;

    public final NumberPath<java.math.BigDecimal> valor = createNumber("valor", java.math.BigDecimal.class);

    public QDocumentoEntity(String variable) {
        this(DocumentoEntity.class, forVariable(variable), INITS);
    }

    public QDocumentoEntity(Path<? extends DocumentoEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDocumentoEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDocumentoEntity(PathMetadata metadata, PathInits inits) {
        this(DocumentoEntity.class, metadata, inits);
    }

    public QDocumentoEntity(Class<? extends DocumentoEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.incorporacao = inits.isInitialized("incorporacao") ? new QIncorporacaoEntity(forProperty("incorporacao"), inits.get("incorporacao")) : null;
        this.movimentacao = inits.isInitialized("movimentacao") ? new QMovimentacaoEntity(forProperty("movimentacao"), inits.get("movimentacao")) : null;
        this.tipoDocumento = inits.isInitialized("tipoDocumento") ? new QTipoDocumentoEntity(forProperty("tipoDocumento")) : null;
    }

}

