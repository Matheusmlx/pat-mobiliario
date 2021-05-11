package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.FiltroConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.IncorporacaoConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.IncorporacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QDominioEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QDominioPerfilEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QFornecedorEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QFuncaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QIncorporacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QPerfilEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QPermissaoPerfilEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QUnidadeOrganizacionalEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.IncorporacaoRepository;
import br.com.azi.patrimoniomobiliario.utils.AcetuacaoUtils;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class IncorporacaoDataProviderImpl implements IncorporacaoDataProvider {

    @Autowired
    private IncorporacaoConverter converter;

    @Autowired
    private IncorporacaoRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Incorporacao> buscarPorId(Long id) {
        Optional<IncorporacaoEntity> encontrado = repository.findById(id);
        return encontrado.map(incorporacaoEntity -> converter.to(incorporacaoEntity));
    }

    @Override
    public Boolean existe(Long id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional
    @Modifying(flushAutomatically = true)
    public Incorporacao salvar(Incorporacao incorporacao) {
        IncorporacaoEntity incorporacaoEntitySalvo = repository.save(converter.from(incorporacao));
        return converter.to(incorporacaoEntitySalvo);
    }

    @Override
    @Modifying(flushAutomatically = true)
    @Transactional
    public void remover(Long id) {
        repository.deleteById(id);
    }

    private static Page<IncorporacaoEntity> construirPaginacao(List<IncorporacaoEntity> incorporacoes, Pageable pageable, Long totalRegistros) {
        return new PageImpl<>(incorporacoes, pageable, totalRegistros);
    }

    @Override
    public ListaPaginada<Incorporacao> buscarPorFiltro(Incorporacao.Filtro filtro) {
        QIncorporacaoEntity incorporacaoEntity = QIncorporacaoEntity.incorporacaoEntity;

        BooleanExpression expression = incorporacaoEntity.id.isNotNull();

        if (!StringUtils.isEmpty(filtro.getConteudo())) {
            BooleanExpression conteudoExp = incorporacaoEntity.codigo.trim().containsIgnoreCase(filtro.getConteudo().trim())
                .or(
                    incorporacaoEntity.fornecedor.razaoSocial.trim().containsIgnoreCase(filtro.getConteudo().trim())
                        .or(incorporacaoEntity.situacao.trim().containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo().trim().replace(" ", "_")).trim()))
                        .or(incorporacaoEntity.codigo.trim().contains(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo().trim())))
                        .or(incorporacaoEntity.id.like(filtro.getConteudo().trim()))
                        .or(incorporacaoEntity.fornecedor.cpfCnpj.trim().contains(filtro.getConteudo()))
                        .or(incorporacaoEntity.orgao.sigla.trim().containsIgnoreCase(filtro.getConteudo().trim()))
                );

            expression = expression.and(conteudoExp);
        }

        expression = construirVerificaoPermissaoUsuario(expression, filtro);

        Page<IncorporacaoEntity> entidadesEncontradas = buscarComOrdenacao(expression, filtro);

        List<Incorporacao> entidades = entidadesEncontradas
            .getContent()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());

        return ListaPaginada.<Incorporacao>builder()
            .items(entidades)
            .totalElements(entidadesEncontradas.getTotalElements())
            .totalPages((long) entidadesEncontradas.getTotalPages())
            .build();
    }

    @Override
    public Boolean existePorId(Long id) {
        return repository.existsById(id);
    }

    private Page<IncorporacaoEntity> buscarComOrdenacao(BooleanExpression expression, Incorporacao.Filtro filtro) {
        OrderSpecifier[] ordemPriorizacao;
        Expression<Integer> cases = new CaseBuilder()
            .when(QIncorporacaoEntity.incorporacaoEntity.situacao.eq(Incorporacao.Situacao.EM_ELABORACAO.name())).then(1)
            .when(QIncorporacaoEntity.incorporacaoEntity.situacao.eq(Incorporacao.Situacao.FINALIZADO.name())).then(2)
            .otherwise(3);

        if (filtro.getSort().equalsIgnoreCase("situacao")) {
            ordemPriorizacao = new OrderSpecifier[]{
                new OrderSpecifier(Order.valueOf(filtro.getDirection()), cases),
                new OrderSpecifier(Order.DESC, QIncorporacaoEntity.incorporacaoEntity.dataAlteracao)
            };
        } else {
            ordemPriorizacao = new OrderSpecifier[]{
                new OrderSpecifier(Order.valueOf("ASC"), cases),
                new OrderSpecifier(Order.valueOf(filtro.getDirection()), (Expression) retornaOrdenacao(filtro.getSort())),
                new OrderSpecifier(Order.DESC, QIncorporacaoEntity.incorporacaoEntity.dataAlteracao)
            };
        }

        return buscaIncorporacoes(expression, ordemPriorizacao, filtro);
    }

    private BooleanExpression construirVerificaoPermissaoUsuario(BooleanExpression expression, Incorporacao.Filtro filtro) {
        QIncorporacaoEntity qIncorporacaoEntity = QIncorporacaoEntity.incorporacaoEntity;
        QUnidadeOrganizacionalEntity qUnidadeOrganizacionalEntity = QUnidadeOrganizacionalEntity.unidadeOrganizacionalEntity;
        QDominioEntity qDominioEntity = QDominioEntity.dominioEntity;
        QDominioPerfilEntity qDominioPerfilEntity = QDominioPerfilEntity.dominioPerfilEntity;
        QPerfilEntity qPerfilEntity = QPerfilEntity.perfilEntity;
        QPermissaoPerfilEntity qPermissaoPerfilEntity = QPermissaoPerfilEntity.permissaoPerfilEntity;
        QFuncaoEntity qFuncaoEntity = QFuncaoEntity.funcaoEntity;

        BooleanExpression possuiAcessoOrgaoSetorIncorporacao = new JPAQueryFactory(entityManager)
            .selectFrom(qDominioEntity)
            .select(qUnidadeOrganizacionalEntity.id).distinct()
            .where(qDominioEntity.usuario.id.eq(filtro.getUsuarioId())
                .and(qDominioEntity.tipoCliente.eq("ESTRUTURA_ORGANIZACIONAL"))
                .and(qDominioEntity.chaveCliente.eq(qIncorporacaoEntity.orgao.id)
                    .or(qDominioEntity.chaveCliente.eq(qIncorporacaoEntity.setor.id)))
            )
            .leftJoin(qDominioPerfilEntity).on(qDominioPerfilEntity.dominioPerfilPK.dominioId.eq(qDominioEntity.id))
            .leftJoin(qPerfilEntity).on(qPerfilEntity.id.eq(qDominioPerfilEntity.dominioPerfilPK.perfilId))
            .leftJoin(qPermissaoPerfilEntity).on(qPermissaoPerfilEntity.permissaoPerfilPK.perfilId.eq(qPerfilEntity.id))
            .leftJoin(qFuncaoEntity).on(qFuncaoEntity.id.eq(qPermissaoPerfilEntity.funcao.id))
            .where(qFuncaoEntity.nome.in(filtro.getFuncoes()))
            .exists();

        BooleanExpression orgaoSetorMovimentacaoNulos = qIncorporacaoEntity.orgao.isNull()
            .and(qIncorporacaoEntity.setor.isNull());

        return expression.and(possuiAcessoOrgaoSetorIncorporacao.or(orgaoSetorMovimentacaoNulos));
    }

    private Page<IncorporacaoEntity> buscaIncorporacoes(BooleanExpression expression, OrderSpecifier[] ordemPriorizacao, Incorporacao.Filtro filtro) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        QIncorporacaoEntity incorporacaoEntity = QIncorporacaoEntity.incorporacaoEntity;
        QFornecedorEntity fornecedorEntity = QFornecedorEntity.fornecedorEntity;
        QUnidadeOrganizacionalEntity unidadeOrganizacionalEntity = QUnidadeOrganizacionalEntity.unidadeOrganizacionalEntity;

        JPAQuery<IncorporacaoEntity> query = jpaQueryFactory.selectFrom(incorporacaoEntity)
            .leftJoin(incorporacaoEntity.fornecedor, fornecedorEntity)
            .leftJoin(incorporacaoEntity.orgao, unidadeOrganizacionalEntity)
            .where(expression).orderBy(ordemPriorizacao).fetchJoin();
        query.offset(filtro.getPage() * filtro.getSize());
        query.limit(filtro.getSize());

        QueryResults<IncorporacaoEntity> results = query.fetchResults();

        return construirPaginacao(results.getResults(), FiltroConverter.extrairPaginacao(filtro), results.getTotal());
    }

    private Object retornaOrdenacao(String sort) {
        switch (sort) {
            case "id":
                return QIncorporacaoEntity.incorporacaoEntity.id;
            case "fornecedor":
                return QIncorporacaoEntity.incorporacaoEntity.fornecedor.razaoSocial;
            case "orgao":
                return QIncorporacaoEntity.incorporacaoEntity.orgao.sigla;
            default:
                return QIncorporacaoEntity.incorporacaoEntity.dataRecebimento;
        }
    }
}
