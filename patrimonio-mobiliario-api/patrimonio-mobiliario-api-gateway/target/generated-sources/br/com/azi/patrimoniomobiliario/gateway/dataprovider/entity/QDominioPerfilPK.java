package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QDominioPerfilPK is a Querydsl query type for DominioPerfilPK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QDominioPerfilPK extends BeanPath<DominioPerfilPK> {

    private static final long serialVersionUID = -1862978366L;

    public static final QDominioPerfilPK dominioPerfilPK = new QDominioPerfilPK("dominioPerfilPK");

    public final NumberPath<Long> dominioId = createNumber("dominioId", Long.class);

    public final NumberPath<Long> perfilId = createNumber("perfilId", Long.class);

    public QDominioPerfilPK(String variable) {
        super(DominioPerfilPK.class, forVariable(variable));
    }

    public QDominioPerfilPK(Path<? extends DominioPerfilPK> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDominioPerfilPK(PathMetadata metadata) {
        super(DominioPerfilPK.class, metadata);
    }

}

