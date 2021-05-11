package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVinculoMaterialServicoSubelementoEntity is a Querydsl query type for VinculoMaterialServicoSubelementoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QVinculoMaterialServicoSubelementoEntity extends EntityPathBase<VinculoMaterialServicoSubelementoEntity> {

    private static final long serialVersionUID = -1282206212L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVinculoMaterialServicoSubelementoEntity vinculoMaterialServicoSubelementoEntity = new QVinculoMaterialServicoSubelementoEntity("vinculoMaterialServicoSubelementoEntity");

    public final QVinculoMaterialServicoSubElementoId vinculoMaterialServicoSubElementoId;

    public QVinculoMaterialServicoSubelementoEntity(String variable) {
        this(VinculoMaterialServicoSubelementoEntity.class, forVariable(variable), INITS);
    }

    public QVinculoMaterialServicoSubelementoEntity(Path<? extends VinculoMaterialServicoSubelementoEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVinculoMaterialServicoSubelementoEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVinculoMaterialServicoSubelementoEntity(PathMetadata metadata, PathInits inits) {
        this(VinculoMaterialServicoSubelementoEntity.class, metadata, inits);
    }

    public QVinculoMaterialServicoSubelementoEntity(Class<? extends VinculoMaterialServicoSubelementoEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.vinculoMaterialServicoSubElementoId = inits.isInitialized("vinculoMaterialServicoSubElementoId") ? new QVinculoMaterialServicoSubElementoId(forProperty("vinculoMaterialServicoSubElementoId"), inits.get("vinculoMaterialServicoSubElementoId")) : null;
    }

}

