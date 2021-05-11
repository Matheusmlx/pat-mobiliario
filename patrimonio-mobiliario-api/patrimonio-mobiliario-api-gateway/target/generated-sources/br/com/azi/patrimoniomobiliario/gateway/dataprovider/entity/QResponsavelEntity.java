package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QResponsavelEntity is a Querydsl query type for ResponsavelEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QResponsavelEntity extends EntityPathBase<ResponsavelEntity> {

    private static final long serialVersionUID = 1039541841L;

    public static final QResponsavelEntity responsavelEntity = new QResponsavelEntity("responsavelEntity");

    public final br.com.azi.hal.core.generic.entity.QBaseObject _super = new br.com.azi.hal.core.generic.entity.QBaseObject(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = _super.dataAlteracao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataCadastro = _super.dataCadastro;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nome = createString("nome");

    //inherited
    public final StringPath usuarioAlteracao = _super.usuarioAlteracao;

    //inherited
    public final StringPath usuarioCadastro = _super.usuarioCadastro;

    public QResponsavelEntity(String variable) {
        super(ResponsavelEntity.class, forVariable(variable));
    }

    public QResponsavelEntity(Path<? extends ResponsavelEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QResponsavelEntity(PathMetadata metadata) {
        super(ResponsavelEntity.class, metadata);
    }

}

