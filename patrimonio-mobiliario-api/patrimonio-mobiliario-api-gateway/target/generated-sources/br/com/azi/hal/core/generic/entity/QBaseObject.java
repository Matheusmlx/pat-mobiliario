package br.com.azi.hal.core.generic.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseObject is a Querydsl query type for BaseObject
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QBaseObject extends EntityPathBase<BaseObject> {

    private static final long serialVersionUID = -1844044263L;

    public static final QBaseObject baseObject = new QBaseObject("baseObject");

    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = createDateTime("dataAlteracao", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> dataCadastro = createDateTime("dataCadastro", java.time.LocalDateTime.class);

    public final StringPath usuarioAlteracao = createString("usuarioAlteracao");

    public final StringPath usuarioCadastro = createString("usuarioCadastro");

    public QBaseObject(String variable) {
        super(BaseObject.class, forVariable(variable));
    }

    public QBaseObject(Path<? extends BaseObject> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseObject(PathMetadata metadata) {
        super(BaseObject.class, metadata);
    }

}

