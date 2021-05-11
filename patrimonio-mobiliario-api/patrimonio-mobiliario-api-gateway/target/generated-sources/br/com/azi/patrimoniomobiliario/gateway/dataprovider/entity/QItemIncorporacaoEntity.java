package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemIncorporacaoEntity is a Querydsl query type for ItemIncorporacaoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QItemIncorporacaoEntity extends EntityPathBase<ItemIncorporacaoEntity> {

    private static final long serialVersionUID = 553301738L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemIncorporacaoEntity itemIncorporacaoEntity = new QItemIncorporacaoEntity("itemIncorporacaoEntity");

    public final br.com.azi.hal.core.generic.entity.QBaseObject _super = new br.com.azi.hal.core.generic.entity.QBaseObject(this);

    public final StringPath anoFabricacaoModelo = createString("anoFabricacaoModelo");

    public final StringPath categoria = createString("categoria");

    public final StringPath codigo = createString("codigo");

    public final StringPath combustivel = createString("combustivel");

    public final QConfiguracaoDepreciacaoEntity configDepreciacao;

    public final QContaContabilEntity contaContabil;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataAlteracao = _super.dataAlteracao;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dataCadastro = _super.dataCadastro;

    public final BooleanPath depreciavel = createBoolean("depreciavel");

    public final StringPath descricao = createString("descricao");

    public final QEstadoConservacaoEntity estadoConservacao;

    public final StringPath fabricante = createString("fabricante");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QIncorporacaoEntity incorporacao;

    public final StringPath marca = createString("marca");

    public final StringPath modelo = createString("modelo");

    public final QNaturezaDespesaEntity naturezaDespesa;

    public final StringPath numeracaoPatrimonial = createString("numeracaoPatrimonial");

    public final NumberPath<Integer> quantidade = createNumber("quantidade", Integer.class);

    public final StringPath situacao = createString("situacao");

    public final StringPath tipoBem = createString("tipoBem");

    public final StringPath uriImagem = createString("uriImagem");

    //inherited
    public final StringPath usuarioAlteracao = _super.usuarioAlteracao;

    //inherited
    public final StringPath usuarioCadastro = _super.usuarioCadastro;

    public final NumberPath<java.math.BigDecimal> valorTotal = createNumber("valorTotal", java.math.BigDecimal.class);

    public QItemIncorporacaoEntity(String variable) {
        this(ItemIncorporacaoEntity.class, forVariable(variable), INITS);
    }

    public QItemIncorporacaoEntity(Path<? extends ItemIncorporacaoEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemIncorporacaoEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemIncorporacaoEntity(PathMetadata metadata, PathInits inits) {
        this(ItemIncorporacaoEntity.class, metadata, inits);
    }

    public QItemIncorporacaoEntity(Class<? extends ItemIncorporacaoEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.configDepreciacao = inits.isInitialized("configDepreciacao") ? new QConfiguracaoDepreciacaoEntity(forProperty("configDepreciacao"), inits.get("configDepreciacao")) : null;
        this.contaContabil = inits.isInitialized("contaContabil") ? new QContaContabilEntity(forProperty("contaContabil")) : null;
        this.estadoConservacao = inits.isInitialized("estadoConservacao") ? new QEstadoConservacaoEntity(forProperty("estadoConservacao")) : null;
        this.incorporacao = inits.isInitialized("incorporacao") ? new QIncorporacaoEntity(forProperty("incorporacao"), inits.get("incorporacao")) : null;
        this.naturezaDespesa = inits.isInitialized("naturezaDespesa") ? new QNaturezaDespesaEntity(forProperty("naturezaDespesa"), inits.get("naturezaDespesa")) : null;
    }

}

