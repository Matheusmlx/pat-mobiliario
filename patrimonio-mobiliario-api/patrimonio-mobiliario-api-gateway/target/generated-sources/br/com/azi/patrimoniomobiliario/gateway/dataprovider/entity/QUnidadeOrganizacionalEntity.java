package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUnidadeOrganizacionalEntity is a Querydsl query type for UnidadeOrganizacionalEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUnidadeOrganizacionalEntity extends EntityPathBase<UnidadeOrganizacionalEntity> {

    private static final long serialVersionUID = 1430817070L;

    public static final QUnidadeOrganizacionalEntity unidadeOrganizacionalEntity = new QUnidadeOrganizacionalEntity("unidadeOrganizacionalEntity");

    public final BooleanPath almoxarifado = createBoolean("almoxarifado");

    public final StringPath bairro = createString("bairro");

    public final StringPath cep = createString("cep");

    public final StringPath codigoHierarquia = createString("codigoHierarquia");

    public final StringPath codigoOrgao = createString("codigoOrgao");

    public final StringPath complemento = createString("complemento");

    public final DatePath<java.time.LocalDate> dataDecreto = createDate("dataDecreto", java.time.LocalDate.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> estruturaAdministrativaId = createNumber("estruturaAdministrativaId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> idExterno = createNumber("idExterno", Integer.class);

    public final StringPath justificativa = createString("justificativa");

    public final StringPath logradouro = createString("logradouro");

    public final StringPath motivoAlteracao = createString("motivoAlteracao");

    public final NumberPath<Long> municipioId = createNumber("municipioId", Long.class);

    public final StringPath nome = createString("nome");

    public final StringPath nomeResponsavel = createString("nomeResponsavel");

    public final StringPath nomeSubstituto = createString("nomeSubstituto");

    public final StringPath numero = createString("numero");

    public final StringPath numeroDecreto = createString("numeroDecreto");

    public final NumberPath<Long> origemId = createNumber("origemId", Long.class);

    public final StringPath sigla = createString("sigla");

    public final StringPath situacao = createString("situacao");

    public final StringPath tipo = createString("tipo");

    public final StringPath tipoAdministracao = createString("tipoAdministracao");

    public final NumberPath<Long> unidadeSuperiorId = createNumber("unidadeSuperiorId", Long.class);

    public QUnidadeOrganizacionalEntity(String variable) {
        super(UnidadeOrganizacionalEntity.class, forVariable(variable));
    }

    public QUnidadeOrganizacionalEntity(Path<? extends UnidadeOrganizacionalEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUnidadeOrganizacionalEntity(PathMetadata metadata) {
        super(UnidadeOrganizacionalEntity.class, metadata);
    }

}

