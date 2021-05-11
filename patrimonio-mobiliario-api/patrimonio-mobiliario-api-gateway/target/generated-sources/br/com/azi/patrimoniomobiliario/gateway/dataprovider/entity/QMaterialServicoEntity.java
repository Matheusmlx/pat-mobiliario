package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QMaterialServicoEntity is a Querydsl query type for MaterialServicoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMaterialServicoEntity extends EntityPathBase<MaterialServicoEntity> {

    private static final long serialVersionUID = -1626583383L;

    public static final QMaterialServicoEntity materialServicoEntity = new QMaterialServicoEntity("materialServicoEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QMaterialServicoEntity(String variable) {
        super(MaterialServicoEntity.class, forVariable(variable));
    }

    public QMaterialServicoEntity(Path<? extends MaterialServicoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMaterialServicoEntity(PathMetadata metadata) {
        super(MaterialServicoEntity.class, metadata);
    }

}

