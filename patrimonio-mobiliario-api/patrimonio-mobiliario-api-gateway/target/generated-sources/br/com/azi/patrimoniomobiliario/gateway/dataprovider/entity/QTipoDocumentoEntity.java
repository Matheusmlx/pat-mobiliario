package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTipoDocumentoEntity is a Querydsl query type for TipoDocumentoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTipoDocumentoEntity extends EntityPathBase<TipoDocumentoEntity> {

    private static final long serialVersionUID = -762007951L;

    public static final QTipoDocumentoEntity tipoDocumentoEntity = new QTipoDocumentoEntity("tipoDocumentoEntity");

    public final StringPath descricao = createString("descricao");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath identificacaoDocumento = createString("identificacaoDocumento");

    public final BooleanPath permiteAnexo = createBoolean("permiteAnexo");

    public QTipoDocumentoEntity(String variable) {
        super(TipoDocumentoEntity.class, forVariable(variable));
    }

    public QTipoDocumentoEntity(Path<? extends TipoDocumentoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTipoDocumentoEntity(PathMetadata metadata) {
        super(TipoDocumentoEntity.class, metadata);
    }

}

