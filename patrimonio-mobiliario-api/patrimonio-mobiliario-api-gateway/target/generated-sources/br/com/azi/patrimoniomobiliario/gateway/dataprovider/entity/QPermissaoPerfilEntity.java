package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPermissaoPerfilEntity is a Querydsl query type for PermissaoPerfilEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPermissaoPerfilEntity extends EntityPathBase<PermissaoPerfilEntity> {

    private static final long serialVersionUID = -1426137852L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPermissaoPerfilEntity permissaoPerfilEntity = new QPermissaoPerfilEntity("permissaoPerfilEntity");

    public final QFuncaoEntity funcao;

    public final QPerfilEntity perfil;

    public final EnumPath<br.com.azi.hal.core.generic.enumeration.EnumSimNao> permissao = createEnum("permissao", br.com.azi.hal.core.generic.enumeration.EnumSimNao.class);

    public final QPermissaoPerfilPK permissaoPerfilPK;

    public QPermissaoPerfilEntity(String variable) {
        this(PermissaoPerfilEntity.class, forVariable(variable), INITS);
    }

    public QPermissaoPerfilEntity(Path<? extends PermissaoPerfilEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPermissaoPerfilEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPermissaoPerfilEntity(PathMetadata metadata, PathInits inits) {
        this(PermissaoPerfilEntity.class, metadata, inits);
    }

    public QPermissaoPerfilEntity(Class<? extends PermissaoPerfilEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.funcao = inits.isInitialized("funcao") ? new QFuncaoEntity(forProperty("funcao")) : null;
        this.perfil = inits.isInitialized("perfil") ? new QPerfilEntity(forProperty("perfil"), inits.get("perfil")) : null;
        this.permissaoPerfilPK = inits.isInitialized("permissaoPerfilPK") ? new QPermissaoPerfilPK(forProperty("permissaoPerfilPK")) : null;
    }

}

