package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.FiltroConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.MovimentacaoConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.MovimentacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QDominioEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QDominioPerfilEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QFuncaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QMovimentacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QPerfilEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QPermissaoPerfilEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QUnidadeOrganizacionalEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.MovimentacaoRepository;
import br.com.azi.patrimoniomobiliario.utils.AcetuacaoUtils;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MovimentacaoDataProviderImpl implements MovimentacaoDataProvider {

    @Autowired
    private MovimentacaoRepository repository;

    @Autowired
    private MovimentacaoConverter converter;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    @Override
    public Movimentacao salvar(Movimentacao movimentacao) {
        MovimentacaoEntity entidadeSalva = repository.save(converter.from(movimentacao));
        return converter.to(entidadeSalva);
    }

    @Override
    public ListaPaginada<Movimentacao> buscarMovimentacaoInternaPorFiltro(Movimentacao.Filtro filtro) {
        QMovimentacaoEntity qMovimentacaoEntity = QMovimentacaoEntity.movimentacaoEntity;
        QUnidadeOrganizacionalEntity qUnidadeOrganizacionalEntity = QUnidadeOrganizacionalEntity.unidadeOrganizacionalEntity;

        OrderSpecifier<?>[] ordemPriorizacao;

        BooleanExpression expression = qMovimentacaoEntity.id.isNotNull();

        if (!StringUtils.isEmpty(filtro.getConteudo())) {
            BooleanExpression conteudoExp = qMovimentacaoEntity.codigo.trim().containsIgnoreCase(filtro.getConteudo().trim())
                .or(compararSemAcentuacao(qMovimentacaoEntity.tipo, filtro.getConteudo()))
                .or(qMovimentacaoEntity.orgaoOrigem.sigla.containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo()).trim()))
                .or(compararSemAcentuacao(qMovimentacaoEntity.setorOrigem.nome, filtro.getConteudo()))
                .or(compararSemAcentuacao(qMovimentacaoEntity.setorDestino.nome, filtro.getConteudo()))
                .or(qMovimentacaoEntity.situacao.containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo().trim()).replace(" ", "_")));

            expression = expression.and(conteudoExp);
        }

        expression = construirVerificaoPermissaoUsuario(expression, filtro);

        Expression<Integer> cases = new CaseBuilder()
            .when(qMovimentacaoEntity.situacao.eq(Movimentacao.Situacao.EM_ELABORACAO.name())).then(1)
            .when(qMovimentacaoEntity.situacao.eq(Movimentacao.Situacao.FINALIZADO.name())).then(2)
            .when(qMovimentacaoEntity.situacao.eq(Movimentacao.Situacao.DEVOLVIDO_PARCIAL.name())).then(3)
            .when(qMovimentacaoEntity.situacao.eq(Movimentacao.Situacao.DEVOLVIDO.name())).then(4)
            .otherwise(5);

        if (filtro.getSort().equalsIgnoreCase("situacao")) {
            ordemPriorizacao = new OrderSpecifier[]{
                new OrderSpecifier<>(Order.valueOf(filtro.getDirection()), cases),
                new OrderSpecifier<>(Order.DESC, QMovimentacaoEntity.movimentacaoEntity.dataAlteracao)
            };
        } else {
            ordemPriorizacao = new OrderSpecifier[]{
                new OrderSpecifier<>(Order.valueOf("ASC"), cases),
                new OrderSpecifier<>(Order.valueOf(filtro.getDirection()), retornaOrdenacao(filtro.getSort())),
                new OrderSpecifier<>(Order.DESC, QMovimentacaoEntity.movimentacaoEntity.dataAlteracao)
            };
        }

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<MovimentacaoEntity> query = jpaQueryFactory.selectFrom(qMovimentacaoEntity)
            .leftJoin(qMovimentacaoEntity.orgaoOrigem, qUnidadeOrganizacionalEntity)
            .leftJoin(qMovimentacaoEntity.setorOrigem, qUnidadeOrganizacionalEntity)
            .leftJoin(qMovimentacaoEntity.setorDestino, qUnidadeOrganizacionalEntity)
            .where(expression)
            .orderBy(ordemPriorizacao)
            .offset(filtro.getPage() * filtro.getSize())
            .limit(filtro.getSize());

        QueryResults<MovimentacaoEntity> results = query.fetchResults();

        Page<MovimentacaoEntity> entidadesEncontradas = construirPaginacao(results.getResults(), FiltroConverter.extrairPaginacao(filtro), results.getTotal());

        return montarListaPaginada(entidadesEncontradas);
    }

    @Autowired
    public String buscarUltimoCodigoCadastrado() {
        Optional<MovimentacaoEntity> movimentacao = repository.findFirstByOrderByCodigoDesc();
        if (movimentacao.isPresent()) {
            return movimentacao.get().getCodigo();
        }
        return "0";
    }

    @Override
    public Optional<Movimentacao> buscarPorId(Long id) {
        final Optional<MovimentacaoEntity> movimentacaoEntity = repository.findById(id);
        return movimentacaoEntity.map(movimentacao -> converter.to(movimentacao));
    }

    @Override
    public boolean existe(Long id) {
        return repository.existsById(id);
    }

    @Override
    public void remover(Movimentacao movimentacao) {
        repository.delete(converter.from(movimentacao));
    }

    @Override
    public void removerPorId(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Movimentacao> buscarUltimoNumeroTermoResponsabilidade() {
        Optional<MovimentacaoEntity> movimentacaoEntity =  repository.findFirstByNumeroTermoResponsabilidadeNotNullOrderByNumeroTermoResponsabilidadeDesc();
        return movimentacaoEntity.map(movimentacao -> converter.to(movimentacao));
    }

    private BooleanExpression compararSemAcentuacao(StringExpression path, String value) {
        StringExpression expr = Expressions.stringTemplate("upper(translate({0}, 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç', 'AAAAAAAAEEEEIIOOOOOOUUUUCC'))", path.trim());
        return expr.containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(value.trim()));
    }

    private BooleanExpression construirVerificaoPermissaoUsuario(BooleanExpression expression, Movimentacao.Filtro filtro) {
        QMovimentacaoEntity qMovimentacaoEntity = QMovimentacaoEntity.movimentacaoEntity;
        QUnidadeOrganizacionalEntity qUnidadeOrganizacionalEntity = QUnidadeOrganizacionalEntity.unidadeOrganizacionalEntity;
        QDominioEntity qDominioEntity = QDominioEntity.dominioEntity;
        QDominioPerfilEntity qDominioPerfilEntity = QDominioPerfilEntity.dominioPerfilEntity;
        QPerfilEntity qPerfilEntity = QPerfilEntity.perfilEntity;
        QPermissaoPerfilEntity qPermissaoPerfilEntity = QPermissaoPerfilEntity.permissaoPerfilEntity;
        QFuncaoEntity qFuncaoEntity = QFuncaoEntity.funcaoEntity;

        BooleanExpression possuiAcessoOrgaoSetorMovimentacao = new JPAQueryFactory(entityManager)
            .selectFrom(qDominioEntity)
            .select(qUnidadeOrganizacionalEntity.id).distinct()
            .where(qDominioEntity.usuario.id.eq(filtro.getUsuarioId())
                .and(qDominioEntity.tipoCliente.eq("ESTRUTURA_ORGANIZACIONAL"))
                .and(qDominioEntity.chaveCliente.eq(qMovimentacaoEntity.orgaoOrigem.id)
                    .or(qDominioEntity.chaveCliente.eq(qMovimentacaoEntity.orgaoDestino.id))
                    .or(qDominioEntity.chaveCliente.eq(qMovimentacaoEntity.setorOrigem.id))
                    .or(qDominioEntity.chaveCliente.eq(qMovimentacaoEntity.setorDestino.id))
                ))
            .leftJoin(qDominioPerfilEntity).on(qDominioPerfilEntity.dominioPerfilPK.dominioId.eq(qDominioEntity.id))
            .leftJoin(qPerfilEntity).on(qPerfilEntity.id.eq(qDominioPerfilEntity.dominioPerfilPK.perfilId))
            .leftJoin(qPermissaoPerfilEntity).on(qPermissaoPerfilEntity.permissaoPerfilPK.perfilId.eq(qPerfilEntity.id))
            .leftJoin(qFuncaoEntity).on(qFuncaoEntity.id.eq(qPermissaoPerfilEntity.funcao.id))
            .where(qFuncaoEntity.nome.in(filtro.getFuncoes()))
            .exists();

        BooleanExpression orgaoSetorMovimentacaoNulos = qMovimentacaoEntity.orgaoOrigem.isNull()
            .and(qMovimentacaoEntity.orgaoDestino.isNull())
            .and(qMovimentacaoEntity.setorOrigem.isNull())
            .and(qMovimentacaoEntity.setorDestino.isNull());

        return expression.and(possuiAcessoOrgaoSetorMovimentacao.or(orgaoSetorMovimentacaoNulos));
    }

    private StringPath retornaOrdenacao(String sort) {
        switch (sort) {
            case "codigo":
                return QMovimentacaoEntity.movimentacaoEntity.codigo;
            case "tipo":
                return QMovimentacaoEntity.movimentacaoEntity.tipo;
            case "orgao":
                return QMovimentacaoEntity.movimentacaoEntity.orgaoOrigem.sigla;
            case "origem":
                return QMovimentacaoEntity.movimentacaoEntity.setorOrigem.nome;
            default:
                return QMovimentacaoEntity.movimentacaoEntity.setorDestino.nome;
        }
    }

    private Page<MovimentacaoEntity> construirPaginacao(List<MovimentacaoEntity> movimentacoes, Pageable pageable, Long totalRegistros) {
        return new PageImpl<>(movimentacoes, pageable, totalRegistros);
    }

    private ListaPaginada<Movimentacao> montarListaPaginada(Page<MovimentacaoEntity> entidadesEncontradas) {
        List<Movimentacao> entidades = entidadesEncontradas
            .getContent()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());

        return ListaPaginada
            .<Movimentacao>builder()
            .items(entidades)
            .totalElements(entidadesEncontradas.getTotalElements())
            .totalPages((long) entidadesEncontradas.getTotalPages())
            .build();
    }
}
