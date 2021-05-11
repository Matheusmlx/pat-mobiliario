package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVinculoMaterialServicoSubElementoId is a Querydsl query type for VinculoMaterialServicoSubElementoId
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QVinculoMaterialServicoSubElementoId extends BeanPath<VinculoMaterialServicoSubElementoId> {

    private static final long serialVersionUID = -1109520428L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVinculoMaterialServicoSubElementoId vinculoMaterialServicoSubElementoId = new QVinculoMaterialServicoSubElementoId("vinculoMaterialServicoSubElementoId");

    public final QContratacaoSubelementoEntity contratacaoSubelemento;

    public final QMaterialServicoEntity materialServico;

    public QVinculoMaterialServicoSubElementoId(String variable) {
        this(VinculoMaterialServicoSubElementoId.class, forVariable(variable), INITS);
    }

    public QVinculoMaterialServicoSubElementoId(Path<? extends VinculoMaterialServicoSubElementoId> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVinculoMaterialServicoSubElementoId(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVinculoMaterialServicoSubElementoId(PathMetadata metadata, PathInits inits) {
        this(VinculoMaterialServicoSubElementoId.class, metadata, inits);
    }

    public QVinculoMaterialServicoSubElementoId(Class<? extends VinculoMaterialServicoSubElementoId> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contratacaoSubelemento = inits.isInitialized("contratacaoSubelemento") ? new QContratacaoSubelementoEntity(forProperty("contratacaoSubelemento"), inits.get("contratacaoSubelemento")) : null;
        this.materialServico = inits.isInitialized("materialServico") ? new QMaterialServicoEntity(forProperty("materialServico")) : null;
    }

}

