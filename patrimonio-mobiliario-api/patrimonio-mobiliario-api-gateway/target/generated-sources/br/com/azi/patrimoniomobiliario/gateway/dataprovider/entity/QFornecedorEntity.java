package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFornecedorEntity is a Querydsl query type for FornecedorEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFornecedorEntity extends EntityPathBase<FornecedorEntity> {

    private static final long serialVersionUID = -2124496934L;

    public static final QFornecedorEntity fornecedorEntity = new QFornecedorEntity("fornecedorEntity");

    public final StringPath cpfCnpj = createString("cpfCnpj");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nomeFantasia = createString("nomeFantasia");

    public final StringPath razaoSocial = createString("razaoSocial");

    public final StringPath situacao = createString("situacao");

    public QFornecedorEntity(String variable) {
        super(FornecedorEntity.class, forVariable(variable));
    }

    public QFornecedorEntity(Path<? extends FornecedorEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFornecedorEntity(PathMetadata metadata) {
        super(FornecedorEntity.class, metadata);
    }

}

