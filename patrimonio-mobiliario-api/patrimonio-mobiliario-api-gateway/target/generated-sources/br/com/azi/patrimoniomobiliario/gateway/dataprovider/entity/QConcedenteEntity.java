package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QConcedenteEntity is a Querydsl query type for ConcedenteEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QConcedenteEntity extends EntityPathBase<ConcedenteEntity> {

    private static final long serialVersionUID = -858349809L;

    public static final QConcedenteEntity concedenteEntity = new QConcedenteEntity("concedenteEntity");

    public final br.com.azi.hal.core.generic.entity.QBaseObject _super = new br.com.azi.hal.core.generic.entity.QBaseObject(this);

    public final StringPath cpfCnpj = createString("cpfCnpj");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = _super.dataAlteracao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataCadastro = _super.dataCadastro;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nome = createString("nome");

    public final StringPath situacao = createString("situacao");

    public final StringPath tipoPessoa = createString("tipoPessoa");

    //inherited
    public final StringPath usuarioAlteracao = _super.usuarioAlteracao;

    //inherited
    public final StringPath usuarioCadastro = _super.usuarioCadastro;

    public QConcedenteEntity(String variable) {
        super(ConcedenteEntity.class, forVariable(variable));
    }

    public QConcedenteEntity(Path<? extends ConcedenteEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QConcedenteEntity(PathMetadata metadata) {
        super(ConcedenteEntity.class, metadata);
    }

}

