package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.ContaContabilConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.FiltroConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ContaContabilEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QContaContabilEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QContaContabilProdutoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QProdutoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.ContaContabilRepository;
import br.com.azi.patrimoniomobiliario.utils.AcetuacaoUtils;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ContaContabilDataProviderImpl implements ContaContabilDataProvider {

    @Autowired
    private ContaContabilRepository repository;

    @Autowired
    private ContaContabilConverter converter;

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<ContaContabil> buscarPorId(Long id) {
        Optional<ContaContabilEntity> encontrada = repository.findById(id);
        return encontrada.map(contaContabilEntity -> converter.to(contaContabilEntity));
    }

    @Override
    public Optional<ContaContabil> buscarPorCodigo(String codigo) {
        Optional<ContaContabilEntity> encontrada = repository.findByCodigo(codigo);
        return encontrada.map(contaContabilEntity -> converter.to(contaContabilEntity));
    }

    @Override
    public ListaPaginada<ContaContabil> buscarPorFiltro(ContaContabil.Filtro filtro) {
        QContaContabilEntity contaContabilQuery = QContaContabilEntity.contaContabilEntity;

        BooleanExpression expression = contaContabilQuery.id.isNotNull().and(contaContabilQuery.situacao.eq("ATIVO"));

        if (!StringUtils.isEmpty(filtro.getConteudo())) {
            BooleanExpression conteudoExp = contaContabilQuery.codigo.trim().containsIgnoreCase(filtro.getConteudo().trim())
                .or(contaContabilQuery.codigo.trim().contains(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo().trim())))
                .or(contaContabilQuery.situacao.trim().containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo()).trim()))
                .or(contaContabilQuery.descricao.trim().contains(filtro.getConteudo().toUpperCase()))
                .or(compararSemAcentuacao(contaContabilQuery.descricao, filtro.getConteudo()));
            expression = expression.and(conteudoExp);
        }

        Page<ContaContabilEntity> entidadesEncontradas = buscarEntidades(expression, filtro);

        List<ContaContabil> entidades = entidadesEncontradas
            .getContent()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
        return ListaPaginada.<ContaContabil>builder()
            .items(entidades)
            .totalElements(entidadesEncontradas.getTotalElements())
            .totalPages((long) entidadesEncontradas.getTotalPages())
            .build();
    }

    private Page<ContaContabilEntity> buscarEntidades(BooleanExpression expression, ContaContabil.Filtro filtro) {
        OrderSpecifier<?>[] ordemPriorizacao = new OrderSpecifier[] {
            new OrderSpecifier<>(Order.ASC, QContaContabilEntity.contaContabilEntity.codigo)
        };

        return buscarContasContabeis(expression, ordemPriorizacao, filtro);
    }

    private Page<ContaContabilEntity> buscarContasContabeis(BooleanExpression expression, OrderSpecifier<?>[] ordemPriorizacao, ContaContabil.Filtro filtro) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        QContaContabilEntity contaContabilEntity = QContaContabilEntity.contaContabilEntity;
        QContaContabilProdutoEntity contaContabilProdutoEntity = QContaContabilProdutoEntity.contaContabilProdutoEntity;
        QProdutoEntity produtoEntity = QProdutoEntity.produtoEntity;

        JPAQuery<ContaContabilEntity> query = jpaQueryFactory.selectFrom(contaContabilEntity)
            .innerJoin(contaContabilProdutoEntity)
                .on(contaContabilEntity.eq(contaContabilProdutoEntity.contaContabilProdutoId.contaContabilId))
            .innerJoin(produtoEntity)
                .on(contaContabilProdutoEntity.contaContabilProdutoId.produtoId.eq(produtoEntity))
            .where(produtoEntity.nome.eq("patrimonio-mobiliario"))
            .where(expression).orderBy(ordemPriorizacao).fetchJoin();

        query.offset(filtro.getPage() * filtro.getSize());
        query.limit(filtro.getSize());

        QueryResults<ContaContabilEntity> results = query.fetchResults();

        return construirPaginacao(results.getResults(), FiltroConverter.extrairPaginacao(filtro), results.getTotal());
    }

    private static Page<ContaContabilEntity> construirPaginacao(List<ContaContabilEntity> contasContabeis, Pageable pageable, Long totalRegistros) {
        return new PageImpl<>(contasContabeis, pageable, totalRegistros);
    }

    private BooleanExpression compararSemAcentuacao(StringExpression path, String value) {
        StringExpression expr = Expressions.stringTemplate("upper(translate({0}, 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç', 'AAAAAAAAEEEEIIOOOOOOUUUUCC'))", path.trim());
        return expr.containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(value.trim()));
    }
}
