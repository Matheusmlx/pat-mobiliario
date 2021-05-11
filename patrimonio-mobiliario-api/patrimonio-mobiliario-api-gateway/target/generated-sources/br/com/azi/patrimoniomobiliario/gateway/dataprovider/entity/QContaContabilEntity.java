package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QContaContabilEntity is a Querydsl query type for ContaContabilEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContaContabilEntity extends EntityPathBase<ContaContabilEntity> {

    private static final long serialVersionUID = 415272086L;

    public static final QContaContabilEntity contaContabilEntity = new QContaContabilEntity("contaContabilEntity");

    public final StringPath codigo = createString("codigo");

    public final StringPath descricao = createString("descricao");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath situacao = createString("situacao");

    public QContaContabilEntity(String variable) {
        super(ContaContabilEntity.class, forVariable(variable));
    }

    public QContaContabilEntity(Path<? extends ContaContabilEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContaContabilEntity(PathMetadata metadata) {
        super(ContaContabilEntity.class, metadata);
    }

}

