package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QPermissaoPerfilPK is a Querydsl query type for PermissaoPerfilPK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QPermissaoPerfilPK extends BeanPath<PermissaoPerfilPK> {

    private static final long serialVersionUID = 1933885308L;

    public static final QPermissaoPerfilPK permissaoPerfilPK = new QPermissaoPerfilPK("permissaoPerfilPK");

    public final NumberPath<Long> funcaoId = createNumber("funcaoId", Long.class);

    public final NumberPath<Long> perfilId = createNumber("perfilId", Long.class);

    public final NumberPath<Long> planoId = createNumber("planoId", Long.class);

    public QPermissaoPerfilPK(String variable) {
        super(PermissaoPerfilPK.class, forVariable(variable));
    }

    public QPermissaoPerfilPK(Path<? extends PermissaoPerfilPK> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPermissaoPerfilPK(PathMetadata metadata) {
        super(PermissaoPerfilPK.class, metadata);
    }

}

