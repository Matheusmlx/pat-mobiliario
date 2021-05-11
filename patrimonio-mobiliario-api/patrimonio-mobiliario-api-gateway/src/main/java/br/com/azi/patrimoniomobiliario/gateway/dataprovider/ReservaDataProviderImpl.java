package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.ReservaConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QDominioEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QDominioPerfilEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QFuncaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QPerfilEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QPermissaoPerfilEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QReservaEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QReservaIntervaloEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QUnidadeOrganizacionalEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReservaEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReservaIntervaloEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.database.DatabaseLocker;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.ReservaRepository;
import br.com.azi.patrimoniomobiliario.utils.AcetuacaoUtils;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReservaDataProviderImpl implements ReservaDataProvider {

    private final ReservaRepository repository;

    private final ReservaConverter converter;

    private final EntityManager entityManager;

    private final DatabaseLocker databaseLocker;

    @Override
    public Optional<Reserva> buscarPorId(Long id, Reserva.Filtro filtro) {
        QReservaEntity qReservaEntity = QReservaEntity.reservaEntity;

        BooleanExpression expression = qReservaEntity.id.isNotNull().and(qReservaEntity.id.eq(id));
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        expression = construirVerificaoPermissaoUsuario(expression, filtro);

        JPAQuery<ReservaEntity> query = jpaQueryFactory.selectFrom(qReservaEntity)
                .where(expression);

        ReservaEntity reservaEncontrada = query.fetchFirst();

        if (Objects.isNull(reservaEncontrada)) {
            return Optional.empty();
        }

        return Optional.of(converter.to(reservaEncontrada));
    }

    public Optional<Reserva> buscarPorId(Long id) {
        return repository.findById(id).map(converter::to);
    }

    public Optional<Reserva> buscarReservaComMaiorNumeroFim() {
        return repository
            .findFirstByNumeroFimIsNotNullOrderByNumeroFimDesc()
            .map(converter::to);
    }

    public Optional<Reserva> buscarReservaComMaiorNumeroFimParaOrgao(Long orgaoId) {
        QReservaEntity qReservaEntity = QReservaEntity.reservaEntity;
        JPAQuery<ReservaEntity> query = construirQueryBaseReservaPorOrgao(orgaoId);

        query.orderBy(qReservaEntity.numeroFim.desc());

        ReservaEntity reservaEncontrada = query.fetchFirst();

        if (Objects.isNull(reservaEncontrada)) {
            return Optional.empty();
        }

        return Optional.of(converter.to(reservaEncontrada));
    }

    @Override
    public Optional<Reserva> buscarReservaComMaiorCodigoPorOrgao(Long orgaoId) {
        QReservaEntity qReservaEntity = QReservaEntity.reservaEntity;
        JPAQuery<ReservaEntity> query = construirQueryBaseReservaPorOrgao(orgaoId);

        query.orderBy(qReservaEntity.codigo.desc());

        ReservaEntity reservaEncontrada = query.fetchFirst();

        if (Objects.isNull(reservaEncontrada)) {
            return Optional.empty();
        }

        return Optional.of(converter.to(reservaEncontrada));
    }

    @Override
    public Optional<Reserva> buscarReservaComMaiorNumeroDiferenteDe(Reserva reserva) {
        QReservaEntity qReservaEntity = QReservaEntity.reservaEntity;

        BooleanExpression expression = qReservaEntity.id.isNotNull();
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<ReservaEntity> query = jpaQueryFactory.selectFrom(qReservaEntity)
            .where(expression.and(qReservaEntity.id.ne(reserva.getId())))
            .orderBy(qReservaEntity.numeroFim.desc());
        query.offset(0L);
        query.limit(1L);

        ReservaEntity reservaEncontrada = query.fetchFirst();

        if (Objects.isNull(reservaEncontrada)) {
            return Optional.empty();
        }

        return Optional.of(converter.to(reservaEncontrada));
    }

    public Optional<Reserva> buscarReservaComMaiorCodigo() {
        return repository
            .findFirstByCodigoIsNotNullOrderByCodigoDesc()
            .map(converter::to);
    }

    public Boolean existe(Long id) {
        return repository.existsById(id);
    }

    @Override
    public Boolean existeEmIntervalo(Reserva.Filtro filtro) {
        QReservaEntity qReservaEntity = QReservaEntity.reservaEntity;

        BooleanExpression expression = qReservaEntity.id.isNotNull();

        if (Objects.nonNull(filtro.getReservaId())) {
            expression = expression.and(qReservaEntity.id.ne(filtro.getReservaId()));
        }

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<ReservaEntity> query = jpaQueryFactory.selectFrom(qReservaEntity)
            .where(expression
                .and(qReservaEntity.numeroInicio.goe(filtro.getNumeroInicio())
                    .and(qReservaEntity.numeroInicio.loe(filtro.getNumeroFim()))
                    .or(qReservaEntity.numeroFim.goe(filtro.getNumeroInicio())
                        .and(qReservaEntity.numeroFim.loe(filtro.getNumeroFim())))
                ));
        query.offset(0L);
        query.limit(1L);

        QueryResults<ReservaEntity> results = query.fetchResults();

        return results.getResults().size() == 1;
    }

    @Override
    public Boolean existeEmIntervaloPorOrgao(Reserva.Filtro filtro) {
        QReservaEntity qReservaEntity = QReservaEntity.reservaEntity;
        JPAQuery<ReservaEntity> query = construirQueryBaseReservaPorOrgao(filtro.getOrgaoId());

        query.where(qReservaEntity.numeroInicio.goe(filtro.getNumeroInicio())
            .and(qReservaEntity.numeroInicio.loe(filtro.getNumeroFim()))
            .or(qReservaEntity.numeroFim.goe(filtro.getNumeroInicio())
                .and(qReservaEntity.numeroFim.loe(filtro.getNumeroFim())))
        );
        query.offset(0L);
        query.limit(1L);

        QueryResults<ReservaEntity> results = query.fetchResults();

        return results.getResults().size() == 1;
    }

    public Boolean existeEmIntervaloDeOutraReserva(Long numeroInicio, Long numeroFim, Long reservaId) {
        QReservaEntity qReservaEntity = QReservaEntity.reservaEntity;

        BooleanExpression expression = qReservaEntity.id.isNotNull();
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<ReservaEntity> query = jpaQueryFactory.selectFrom(qReservaEntity)
                .where(expression
                        .and(qReservaEntity.numeroInicio.goe(numeroInicio).and(qReservaEntity.numeroInicio.loe(numeroFim))
                                .or(qReservaEntity.numeroFim.goe(numeroInicio).and(qReservaEntity.numeroFim.loe(numeroFim)))
                                .and(qReservaEntity.id.eq(reservaId).not())
                        ));
        query.offset(0L);
        query.limit(1L);

        QueryResults<ReservaEntity> results = query.fetchResults();

        return results.getResults().size() == 1;
    }

    public Reserva salvar(Reserva reserva) {
        ReservaEntity entidade = repository.save(converter.from(reserva));
        return converter.to(entidade);
    }

    @Override
    public ListaPaginada<Reserva> buscarReservas(Reserva.Filtro filtro) {
        Page<ReservaEntity> entidadesEncontradas = buscarComOrdenacao(
            construirFiltro(filtro),
            construirOrdenacao(),
            filtro);

        List<Reserva> entidades = entidadesEncontradas.getContent()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());

        return ListaPaginada.<Reserva>builder()
            .items(entidades)
            .totalElements(entidadesEncontradas.getTotalElements())
            .totalPages((long) entidadesEncontradas.getTotalPages())
            .build();
    }

    @Override
    public void remover(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void bloquearEntidade() {
        databaseLocker.lockTableByClass(ReservaEntity.class);
    }

    private BooleanExpression construirFiltro(Reserva.Filtro filtro) {
        BooleanExpression expression = construirFiltroConteudo(filtro);
        return construirVerificaoPermissaoUsuario(expression, filtro);
    }

    private BooleanExpression construirFiltroConteudo(Reserva.Filtro filtro) {
        QReservaEntity qReservaEntity = QReservaEntity.reservaEntity;
        QReservaIntervaloEntity qReservaIntervaloEntity = QReservaIntervaloEntity.reservaIntervaloEntity;

        BooleanExpression expression = qReservaEntity.id.isNotNull();
        if (!StringUtils.isEmpty(filtro.getConteudo())) {
            BooleanExpression conteudoExpression = qReservaEntity.codigo.trim().containsIgnoreCase(filtro.getConteudo().trim())
                .or(qReservaEntity.situacao.trim().containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo().trim().replace(" ", "_"))))
                .or(qReservaEntity.quantidadeReservada.like(filtro.getConteudo()));

            JPAQuery<ReservaIntervaloEntity> siglaOrgaoExpression = new JPAQueryFactory(entityManager)
                .selectFrom(qReservaIntervaloEntity)
                .where(qReservaIntervaloEntity.reserva.id.eq(qReservaEntity.id)
                    .and(qReservaIntervaloEntity.orgao.sigla.trim().containsIgnoreCase(filtro.getConteudo().trim())));

            expression = expression.and(conteudoExpression.or(siglaOrgaoExpression.exists()));
        }

        return expression;
    }

    private BooleanExpression construirVerificaoPermissaoUsuario(BooleanExpression expression, Reserva.Filtro filtro) {
        QReservaEntity qReservaEntity = QReservaEntity.reservaEntity;
        QReservaIntervaloEntity qReservaIntervaloEntity = QReservaIntervaloEntity.reservaIntervaloEntity;
        QUnidadeOrganizacionalEntity qUnidadeOrganizacionalEntity = QUnidadeOrganizacionalEntity.unidadeOrganizacionalEntity;
        QDominioEntity qDominioEntity = QDominioEntity.dominioEntity;
        QDominioPerfilEntity qDominioPerfilEntity = QDominioPerfilEntity.dominioPerfilEntity;
        QPerfilEntity qPerfilEntity = QPerfilEntity.perfilEntity;
        QPermissaoPerfilEntity qPermissaoPerfilEntity = QPermissaoPerfilEntity.permissaoPerfilEntity;
        QFuncaoEntity qFuncaoEntity = QFuncaoEntity.funcaoEntity;

        JPAQuery<Long> orgaosReserva = new JPAQueryFactory(entityManager)
            .selectFrom(qReservaIntervaloEntity)
            .select(qReservaIntervaloEntity.orgao.id)
            .where(qReservaIntervaloEntity.reserva.id.eq(qReservaEntity.id));

        BooleanExpression possuiAcessoOrgaosReserva = new JPAQueryFactory(entityManager)
            .selectFrom(qDominioEntity)
            .select(qUnidadeOrganizacionalEntity.id).distinct()
            .leftJoin(qDominioPerfilEntity).on(qDominioPerfilEntity.dominioPerfilPK.dominioId.eq(qDominioEntity.id))
            .leftJoin(qPerfilEntity).on(qPerfilEntity.id.eq(qDominioPerfilEntity.dominioPerfilPK.perfilId))
            .leftJoin(qPermissaoPerfilEntity).on(qPermissaoPerfilEntity.permissaoPerfilPK.perfilId.eq(qPerfilEntity.id))
            .leftJoin(qFuncaoEntity).on(qFuncaoEntity.id.eq(qPermissaoPerfilEntity.funcao.id))
            .where(qDominioEntity.usuario.id.eq(filtro.getUsuarioId())
                .and(qDominioEntity.tipoCliente.eq("ESTRUTURA_ORGANIZACIONAL"))
                .and(qDominioEntity.chaveCliente.in(orgaosReserva))
                .and(qFuncaoEntity.nome.in(filtro.getFuncoes()))
            )
            .exists();

        return expression.and(possuiAcessoOrgaosReserva.or(orgaosReserva.notExists()));
    }

    private OrderSpecifier[] construirOrdenacao() {
        Expression<Integer> cases = new CaseBuilder()
            .when(QReservaEntity.reservaEntity.situacao.eq(Reserva.Situacao.EM_ELABORACAO.name())).then(1)
            .when(QReservaEntity.reservaEntity.situacao.eq(Reserva.Situacao.PARCIAL.name())).then(2)
            .otherwise(3);

        return new OrderSpecifier[]{
            new OrderSpecifier(Order.valueOf("ASC"), cases),
            new OrderSpecifier(Order.DESC, QReservaEntity.reservaEntity.dataAlteracao)
        };
    }

    private Page<ReservaEntity> buscarComOrdenacao(BooleanExpression filtroExpression, OrderSpecifier[] ordenacao, Reserva.Filtro filtro) {
        QReservaEntity qReservaEntity = QReservaEntity.reservaEntity;

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<ReservaEntity> query = jpaQueryFactory.selectFrom(qReservaEntity)
            .where(filtroExpression)
            .orderBy(ordenacao)
            .limit(filtro.getSize())
            .offset(filtro.getPage() * filtro.getSize());

        QueryResults<ReservaEntity> results = query.fetchResults();

        Pageable pageable = PageRequest.of(filtro.getPage().intValue(), filtro.getSize().intValue());

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private JPAQuery<ReservaEntity> construirQueryBaseReservaPorOrgao(Long orgaoId) {
        QReservaEntity qReservaEntity = QReservaEntity.reservaEntity;
        QReservaIntervaloEntity qReservaIntervaloEntity = QReservaIntervaloEntity.reservaIntervaloEntity;

        BooleanExpression expression = qReservaEntity.id.isNotNull();
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        return jpaQueryFactory.selectFrom(qReservaEntity)
            .leftJoin(qReservaIntervaloEntity).on(qReservaEntity.id.eq(qReservaIntervaloEntity.reserva.id))
            .where(expression.and(qReservaIntervaloEntity.orgao.id.eq(orgaoId)));
    }
}
