package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReconhecimentoDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.FiltroConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.ReconhecimentoConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QReconhecimentoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReconhecimentoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.ReconhecimentoRepository;
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
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ReconhecimentoDataProviderImpl implements ReconhecimentoDataProvider {

    @Autowired
    private ReconhecimentoConverter converter;

    @Autowired
    private ReconhecimentoRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    @Modifying(flushAutomatically = true)
    public Reconhecimento atualizar(Reconhecimento reconhecimento) {
        ReconhecimentoEntity atualizado = repository.save(converter.from(reconhecimento));
        return converter.to(atualizado);
    }

    @Override
    public Optional<Reconhecimento> buscarPorId(Long reconhecimentoId) {
        Optional<ReconhecimentoEntity> encontrado = repository.findById(reconhecimentoId);
        return encontrado.map(reconhecimentoEntity -> converter.to(reconhecimentoEntity));
    }

    @Override
    public ListaPaginada<Reconhecimento> buscarPorFiltro(Reconhecimento.Filtro filtro) {
        QReconhecimentoEntity reconhecimentoQuery = QReconhecimentoEntity.reconhecimentoEntity;
        BooleanExpression expression = reconhecimentoQuery.id.isNotNull();

        if (!StringUtils.isEmpty(filtro.getConteudo())) {
            BooleanExpression conteudoExp = reconhecimentoQuery.nome.containsIgnoreCase(filtro.getConteudo().trim())
                .or(compararSemAcentuacao(reconhecimentoQuery.nome, filtro.getConteudo().trim()))
                .or(reconhecimentoQuery.situacao.containsIgnoreCase(filtro.getConteudo().trim()));

            if (filtro.getConteudo().trim().equalsIgnoreCase("sim")) {
                conteudoExp = conteudoExp.or(reconhecimentoQuery.execucaoOrcamentaria.eq(true));
            } else if (AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo().trim()).equalsIgnoreCase("nao")) {
                conteudoExp = conteudoExp.or(reconhecimentoQuery.execucaoOrcamentaria.eq(false));
            }

            expression = expression.and(conteudoExp);
        }

        if(Objects.nonNull(filtro.getSituacao()) && filtro.getSituacao().equalsIgnoreCase("ATIVO")){
            BooleanExpression filtraAtivo = reconhecimentoQuery.situacao.equalsIgnoreCase("ATIVO");
            expression = expression.and(filtraAtivo);
        }

        Page<ReconhecimentoEntity> entidadesEncontradas = buscarComOrdenacao(expression, filtro);

        List<Reconhecimento> entidades = entidadesEncontradas
            .getContent()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());

        return ListaPaginada.<Reconhecimento>builder()
            .items(entidades)
            .totalElements(entidadesEncontradas.getTotalElements())
            .totalPages((long) entidadesEncontradas.getTotalPages())
            .build();
    }

    private Page<ReconhecimentoEntity> buscarComOrdenacao(BooleanExpression expression, Reconhecimento.Filtro filtro) {
        if (filtro.getSort().equalsIgnoreCase("situacao")) {
            return repository.findAll(expression, FiltroConverter.extrairPaginacaoCadastroEdicao(filtro, "dataAlteracao"));
        }
        if (filtro.getSort().equalsIgnoreCase("nome")) {
            return buscarComOrdenacaoPorNome(expression, filtro);
        }
        return repository.findAll(expression, FiltroConverter.extrairPaginacaoComSegundaOrdenacao(filtro, filtro.getSort()));
    }

    @Override
    public Optional<Reconhecimento> buscarReconhecimentoComNome(Long idReconhecimento, String nome) {
        Optional<ReconhecimentoEntity> encontrada = repository.findByNomeAndId(nome, idReconhecimento);
        return encontrada.map(reconhecimentoEntity -> converter.to(reconhecimentoEntity));
    }

    @Override
    public boolean existe(Long id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existePorNome(String nome) {
        return repository.existsByNome(nome);
    }

    @Override
    @Transactional
    @Modifying(flushAutomatically = true)
    public Reconhecimento salvar(Reconhecimento reconhecimento) {
        ReconhecimentoEntity reconhecimentoEntitySalvo = repository.save(converter.from(reconhecimento));
        return converter.to(reconhecimentoEntitySalvo);
    }

    private Page<ReconhecimentoEntity> buscarComOrdenacaoPorNome(BooleanExpression expression, Reconhecimento.Filtro filtro) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        OrderSpecifier<?> ordemPrioridade = filtro.getDirection().equalsIgnoreCase("asc")
            ? obterStringExpression(QReconhecimentoEntity.reconhecimentoEntity.nome).asc()
            : obterStringExpression(QReconhecimentoEntity.reconhecimentoEntity.nome).desc();

        JPAQuery<ReconhecimentoEntity> query = jpaQueryFactory.selectFrom(QReconhecimentoEntity.reconhecimentoEntity)
            .where(expression)
            .orderBy(QReconhecimentoEntity.reconhecimentoEntity.situacao.asc())
            .orderBy(ordemPrioridade);

        query.offset(filtro.getPage() * filtro.getSize());
        query.limit(filtro.getSize());

        QueryResults<ReconhecimentoEntity> results = query.fetchResults();
        return construirPaginacao(results.getResults(), FiltroConverter.extrairPaginacao(filtro), results.getTotal());
    }

    private static Page<ReconhecimentoEntity> construirPaginacao(List<ReconhecimentoEntity> incorporacoes, Pageable pageable, Long totalRegistros) {
        return new PageImpl<>(incorporacoes, pageable, totalRegistros);
    }

    private BooleanExpression compararSemAcentuacao(StringExpression path, String value) {
        StringExpression expr = obterStringExpression(path);
        return expr.containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(value.trim()));
    }

    private StringExpression obterStringExpression(StringExpression path) {
        return Expressions.stringTemplate("upper(translate({0}, 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç', 'AAAAAAAAEEEEIIOOOOOOUUUUCC'))", path.trim());
    }
}
