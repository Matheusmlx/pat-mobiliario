package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUsuarioEntity is a Querydsl query type for UsuarioEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUsuarioEntity extends EntityPathBase<UsuarioEntity> {

    private static final long serialVersionUID = -853575745L;

    public static final QUsuarioEntity usuarioEntity = new QUsuarioEntity("usuarioEntity");

    public final StringPath docChave = createString("docChave");

    public final StringPath email = createString("email");

    public final StringPath emailContato = createString("emailContato");

    public final BooleanPath exibirRepresentacoes = createBoolean("exibirRepresentacoes");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> idiomaId = createNumber("idiomaId", Long.class);

    public final StringPath imagem = createString("imagem");

    public final StringPath nome = createString("nome");

    public final NumberPath<Long> paisId = createNumber("paisId", Long.class);

    public final StringPath situacao = createString("situacao");

    public final StringPath telefone = createString("telefone");

    public final StringPath tema = createString("tema");

    public final StringPath tipoUsuario = createString("tipoUsuario");

    public QUsuarioEntity(String variable) {
        super(UsuarioEntity.class, forVariable(variable));
    }

    public QUsuarioEntity(Path<? extends UsuarioEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUsuarioEntity(PathMetadata metadata) {
        super(UsuarioEntity.class, metadata);
    }

}

