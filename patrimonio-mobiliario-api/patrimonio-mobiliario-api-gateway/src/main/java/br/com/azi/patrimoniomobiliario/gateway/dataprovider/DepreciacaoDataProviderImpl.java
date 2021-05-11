package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.Depreciacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.DepreciacaoConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.DepreciacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QDepreciacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.DepreciacaoRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DepreciacaoDataProviderImpl implements DepreciacaoDataProvider {

    private final DepreciacaoRepository repository;

    private final DepreciacaoConverter converter;

    private final EntityManager entityManager;

    @Override
    public Depreciacao salvar(Depreciacao depreciacao) {
        DepreciacaoEntity depreciacaoEntity = repository.save(converter.from(depreciacao));
        return converter.to(depreciacaoEntity);
    }

    @Override
    public void salvar(List<Depreciacao> depreciacoes) {
        List<DepreciacaoEntity> entityList = depreciacoes.stream().map(converter::from).collect(Collectors.toList());
        repository.saveAll(entityList);
    }

    @Override
    public List<Depreciacao> buscarDepreciacoesPorPatrimonioId(Long patrimonioId) {
        List<DepreciacaoEntity> depreciacaoEntityList = repository.findByPatrimonio_IdOrderByDataFinal(patrimonioId);
        return depreciacaoEntityList.stream().map(converter::to).collect(Collectors.toList());
    }

    @Override
    public Boolean existePorPatrimonioNoPeriodo(Long patrimonioId, LocalDateTime mesReferencia) {
        QDepreciacaoEntity depreciacaoQuery = QDepreciacaoEntity.depreciacaoEntity;
        BooleanExpression expression = depreciacaoQuery.patrimonio.id.isNotNull();
        BooleanExpression conteudoExp = depreciacaoQuery.patrimonio.id.eq(patrimonioId)
            .and(depreciacaoQuery.dataInicial.month().eq(mesReferencia.getMonthValue()))
            .and(depreciacaoQuery.dataInicial.year().eq(mesReferencia.getYear()));

        expression = expression.and(conteudoExp);

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<DepreciacaoEntity> query = jpaQueryFactory.selectFrom(QDepreciacaoEntity.depreciacaoEntity).where(expression);
        QueryResults<DepreciacaoEntity> results = query.fetchResults();

        return results.getTotal() > 0;
    }

    @Override
    public Optional<Depreciacao> buscarUltimaPorPatrimonio(Long patrimonioId) {
        Optional<DepreciacaoEntity> encontrada = repository.findFirstByPatrimonio_IdOrderByDataFinalDesc(patrimonioId);
        return encontrada.map(converter::to);
    }

    @Override
    public Boolean existePorPatrimonio(Long patrimonioId) {
        return repository.existsByPatrimonio_Id(patrimonioId);
    }

}
