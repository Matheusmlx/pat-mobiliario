package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReconhecimentoEntity is a Querydsl query type for ReconhecimentoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReconhecimentoEntity extends EntityPathBase<ReconhecimentoEntity> {

    private static final long serialVersionUID = -248272140L;

    public static final QReconhecimentoEntity reconhecimentoEntity = new QReconhecimentoEntity("reconhecimentoEntity");

    public final br.com.azi.hal.core.generic.entity.QBaseObject _super = new br.com.azi.hal.core.generic.entity.QBaseObject(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = _super.dataAlteracao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataCadastro = _super.dataCadastro;

    public final BooleanPath empenho = createBoolean("empenho");

    public final BooleanPath execucaoOrcamentaria = createBoolean("execucaoOrcamentaria");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nome = createString("nome");

    public final BooleanPath notaFiscal = createBoolean("notaFiscal");

    public final StringPath situacao = createString("situacao");

    //inherited
    public final StringPath usuarioAlteracao = _super.usuarioAlteracao;

    //inherited
    public final StringPath usuarioCadastro = _super.usuarioCadastro;

    public QReconhecimentoEntity(String variable) {
        super(ReconhecimentoEntity.class, forVariable(variable));
    }

    public QReconhecimentoEntity(Path<? extends ReconhecimentoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReconhecimentoEntity(PathMetadata metadata) {
        super(ReconhecimentoEntity.class, metadata);
    }

}

