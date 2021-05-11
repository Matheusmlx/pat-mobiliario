package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.Concedente;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConcedenteDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.ConcedenteConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.FiltroConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ConcedenteEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QConcedenteEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.ConcedenteRepository;
import br.com.azi.patrimoniomobiliario.utils.AcetuacaoUtils;
import com.querydsl.core.QueryResults;
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
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ConcedenteDataProviderImpl implements ConcedenteDataProvider {

    @Autowired
    ConcedenteRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    ConcedenteConverter converter;

    @Override
    public Boolean existePorCpf(String cpfCnpj) {
        return repository.existsByCpfCnpj(cpfCnpj);
    }

    @Override
    public Concedente salvar(Concedente concedente) {
        ConcedenteEntity salvo = repository.save(converter.from(concedente));
        return converter.to(salvo);
    }

    @Override
    @Transactional
    public Optional<Concedente> buscarPorId(Long concedenteId) {
        Optional<ConcedenteEntity> encontrado = repository.findById(concedenteId);
        return encontrado.map(concedenteEntity -> converter.to(concedenteEntity));
    }

    @Override
    public ListaPaginada<Concedente> buscarPorFiltro(Concedente.Filtro filtro) {
        QConcedenteEntity concedenteQuery = QConcedenteEntity.concedenteEntity;
        BooleanExpression expression = concedenteQuery.id.isNotNull();

        if(!StringUtils.isEmpty(filtro.getConteudo())){
            BooleanExpression conteudoExp = compararSemAcentuacao(concedenteQuery.nome, filtro.getConteudo())
                .or(concedenteQuery.situacao.containsIgnoreCase(filtro.getConteudo().trim()))
                .or(compararSemPontuacao(concedenteQuery.cpfCnpj, filtro.getConteudo()));

            expression = expression.and(conteudoExp);
        }

        if(Objects.nonNull(filtro.getSituacao()) && filtro.getSituacao().equalsIgnoreCase("ATIVO")){
            BooleanExpression filtraAtivo = concedenteQuery.situacao.equalsIgnoreCase("ATIVO");
            expression = expression.and(filtraAtivo);
        }

        Page<ConcedenteEntity> entidadesEncontradas = buscarComOrdenacao(expression, filtro);

        List<Concedente> entidades = entidadesEncontradas
            .getContent()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());

        return ListaPaginada.<Concedente>builder()
            .items(entidades)
            .totalElements(entidadesEncontradas.getTotalElements())
            .totalPages((long) entidadesEncontradas.getTotalPages())
            .build();

    }

    public Boolean existe(Long id) {
        return repository.existsById(id);
    }

    private Page<ConcedenteEntity> buscarComOrdenacao(BooleanExpression expression, Concedente.Filtro filtro) {
        if (filtro.getSort().equalsIgnoreCase("situacao")) {
            return repository.findAll(expression, FiltroConverter.extrairPaginacaoCadastroEdicao(filtro,"dataAlteracao"));
        }
        if (filtro.getSort().equalsIgnoreCase("nome")) {
            return buscarComOrdenacaoPorNome(expression, filtro);
        }
        return repository.findAll(expression, FiltroConverter.extrairPaginacaoComSegundaOrdenacao(filtro,filtro.getSort()));
    }

    private Page<ConcedenteEntity> buscarComOrdenacaoPorNome(BooleanExpression expression, Concedente.Filtro filtro) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        OrderSpecifier<?> ordemPrioridade = filtro.getDirection().equalsIgnoreCase("asc")
            ? obterStringExpression(QConcedenteEntity.concedenteEntity.nome).asc()
            : obterStringExpression(QConcedenteEntity.concedenteEntity.nome).desc();

        JPAQuery<ConcedenteEntity> query = jpaQueryFactory.selectFrom(QConcedenteEntity.concedenteEntity)
            .where(expression)
            .orderBy(QConcedenteEntity.concedenteEntity.situacao.asc())
            .orderBy(ordemPrioridade);

        query.offset(filtro.getPage() * filtro.getSize());
        query.limit(filtro.getSize());

        QueryResults<ConcedenteEntity> results = query.fetchResults();
        return construirPaginacao(results.getResults(), FiltroConverter.extrairPaginacao(filtro), results.getTotal());
    }

    private static Page<ConcedenteEntity> construirPaginacao(List<ConcedenteEntity> incorporacoes, Pageable pageable, Long totalRegistros) {
        return new PageImpl<>(incorporacoes, pageable, totalRegistros);
    }

    private BooleanExpression compararSemAcentuacao(StringExpression path, String value) {
        StringExpression expr = obterStringExpression(path);
        return expr.containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(value.trim()));
    }

    private StringExpression obterStringExpression(StringExpression path) {
        return Expressions.stringTemplate("upper(translate({0}, 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç', 'AAAAAAAAEEEEIIOOOOOOUUUUCC'))", path.trim());
    }

    private BooleanExpression compararSemPontuacao(StringExpression path, String value) {
        StringExpression expr = Expressions.stringTemplate("upper(translate({0}, '.-/', ''))", path.trim());
        return expr.containsIgnoreCase(removerPontuacoes(value.trim()));
    }

    private String removerPontuacoes(String value) {
        return value.replaceAll("[./\\-]", "");
    }
}
