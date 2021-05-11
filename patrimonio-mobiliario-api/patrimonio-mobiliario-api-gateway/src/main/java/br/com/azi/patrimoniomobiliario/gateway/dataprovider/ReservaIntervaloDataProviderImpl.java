package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.FiltroConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.ReservaIntervaloConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.database.DatabaseLocker;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QReservaIntervaloEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReservaIntervaloEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.ReservaIntervaloRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReservaIntervaloDataProviderImpl implements ReservaIntervaloDataProvider {

    private final ReservaIntervaloRepository repository;

    private final ReservaIntervaloConverter converter;

    private final EntityManager entityManager;

    private final DatabaseLocker databaseLocker;

    @Override
    public List<ReservaIntervalo> buscarPorReserva(Long reservaId) {
        final List<ReservaIntervaloEntity> entidades = repository.findByReservaIdOrderByOrgaoSigla(reservaId);
        return entidades.stream().map(converter::to).collect(Collectors.toList());
    }

    @Override
    public List<ReservaIntervalo> buscarPorReservaESituacao(Long reservaId, Reserva.Situacao situacao) {
        final List<ReservaIntervaloEntity> entidades = repository.findByReservaIdAndSituacao(reservaId, situacao.name());
        return entidades.stream().map(converter::to).collect(Collectors.toList());
    }

    @Override
    public Optional<ReservaIntervalo> buscarPorId(Long reservaIntervaloId) {
        return repository.findById(reservaIntervaloId).map(converter::to);
    }

    @Override
    public Boolean existeIntervalosPorReserva(Reserva reserva) {
        Long quantidade = repository.countAllByReservaId(reserva.getId());
        return quantidade >= 1;
    }

    @Override
    public Boolean existe(Long reservaId, ReservaIntervalo.Situacao situacao) {
        return repository.existsByReservaIdAndSituacao(reservaId, situacao.name());
    }

    @Override
    public Boolean existe(Long reservaIntervaloId) {
        return repository.existsById(reservaIntervaloId);
    }

    @Override
    public Optional<ReservaIntervalo> buscarIntervaloComMaiorNumeroReserva(Long reservaId) {
        return repository
            .findFirstByReserva_IdOrderByNumeroFimDesc(reservaId)
            .map(converter::to);
    }

    @Override
    public Optional<ReservaIntervalo> buscarIntervaloComMaiorNumeroTermoResponsabilidade() {
        return repository
            .findFirstByNumeroTermoIsNotNullOrderByNumeroTermoDesc()
            .map(converter::to);
    }

    @Override
    public List<ReservaIntervalo> salvar(List<ReservaIntervalo> reservaIntervalos) {
        List<ReservaIntervaloEntity> entidades = reservaIntervalos.stream()
            .map(converter::from)
            .collect(Collectors.toList());

        List<ReservaIntervaloEntity> entidadesSalva = repository.saveAll(entidades);

        return entidadesSalva.stream().map(converter::to).collect(Collectors.toList());
    }

    @Override
    public ReservaIntervalo salvar(ReservaIntervalo reservaIntervalo) {
        ReservaIntervaloEntity entidadeSalva = repository.save(converter.from(reservaIntervalo));
        return converter.to(entidadeSalva);
    }

    public Boolean existeIntervaloNaReserva(Long numeroInicio, Long numeroFim, Long reservaId) {
        QReservaIntervaloEntity qReservaIntervaloEntity = QReservaIntervaloEntity.reservaIntervaloEntity;

        BooleanExpression expression = qReservaIntervaloEntity.id.isNotNull();
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<ReservaIntervaloEntity> query = jpaQueryFactory.selectFrom(qReservaIntervaloEntity)
            .where(expression
                .and(qReservaIntervaloEntity.numeroInicio.goe(numeroInicio).and(qReservaIntervaloEntity.numeroInicio.loe(numeroFim))
                    .or(qReservaIntervaloEntity.numeroFim.goe(numeroInicio).and(qReservaIntervaloEntity.numeroFim.loe(numeroFim)))
                    .and(qReservaIntervaloEntity.reserva.id.eq(reservaId))
                ));
        query.offset(0L);
        query.limit(1L);

        QueryResults<ReservaIntervaloEntity> results = query.fetchResults();

        return results.getResults().size() == 1;
    }

    public Optional<ReservaIntervalo> retornaIntervaloComNumero(Integer numero, Long reservaId) {
        QReservaIntervaloEntity qReservaIntervaloEntity = QReservaIntervaloEntity.reservaIntervaloEntity;

        BooleanExpression expression = qReservaIntervaloEntity.id.isNotNull();
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<ReservaIntervaloEntity> query = jpaQueryFactory.selectFrom(qReservaIntervaloEntity)
            .where(expression
                .and(qReservaIntervaloEntity.numeroInicio.loe(numero).and(qReservaIntervaloEntity.numeroFim.goe(numero))
                    .and(qReservaIntervaloEntity.reserva.id.eq(reservaId))
                ));
        query.offset(0L);
        query.limit(1L);

        ReservaIntervaloEntity result = query.fetchOne();

        if (Objects.nonNull(result)) {
            return Optional.of(converter.to(result));
        }
        return Optional.empty();
    }

    @Override
    public ListaPaginada<ReservaIntervalo> buscarIntervalosPorReserva(ReservaIntervalo.Filtro filtro) {
        filtro.setDirection("ASC");
        QReservaIntervaloEntity qReservaIntervaloEntity = QReservaIntervaloEntity.reservaIntervaloEntity;

        BooleanExpression expression = qReservaIntervaloEntity.id.isNotNull()
            .and(qReservaIntervaloEntity.reserva.id.eq(filtro.getReservaId()));

        Page<ReservaIntervaloEntity> intervalosEncontrados = repository.findAll(expression, FiltroConverter.extrairPaginacao(filtro));

        return montarListaPaginada(intervalosEncontrados);
    }

    @Override
    public void remover(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Modifying
    public void remover(List<Long> ids) {
        repository.deleteByIdIn(ids);
    }

    private ListaPaginada<ReservaIntervalo> montarListaPaginada(Page<ReservaIntervaloEntity> entidadesEncontradas) {
        List<ReservaIntervalo> entidades = entidadesEncontradas
            .getContent()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());

        return ListaPaginada
            .<ReservaIntervalo>builder()
            .items(entidades)
            .totalElements(entidadesEncontradas.getTotalElements())
            .totalPages((long) entidadesEncontradas.getTotalPages())
            .build();
    }

    public Optional<ReservaIntervalo> buscarReservaIntervaloComMaiorCodigo() {
        return repository
            .findFirstByCodigoIsNotNullOrderByCodigoDesc()
            .map(converter::to);
    }

    @Override
    public Optional<ReservaIntervalo> buscarIntervaloComMaiorCodigoPorOrgao(Long orgaoId) {
        return repository
            .findFirstByOrgao_IdAndCodigoIsNotNullOrderByCodigoDesc(orgaoId)
            .map(converter::to);
    }

    @Override
    public void bloquearEntidade() {
        databaseLocker.lockTableByClass(ReservaIntervaloEntity.class);
    }

}
