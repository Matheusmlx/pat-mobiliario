package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.FiltroConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.PatrimonioConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.database.DatabaseLocker;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ItemIncorporacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.PatrimonioEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QMovimentacaoItemEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QPatrimonioEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.PatrimonioRepository;
import br.com.azi.patrimoniomobiliario.utils.AcetuacaoUtils;
import br.com.azi.patrimoniomobiliario.utils.string.StringUtils;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PatrimonioDataProviderImpl implements PatrimonioDataProvider {

    private final PatrimonioRepository repository;

    private final PatrimonioConverter converter;

    private final EntityManager entityManager;

    private final DatabaseLocker databaseLocker;

    @Transactional
    @Modifying(flushAutomatically = true)
    @Override
    public void salvar(List<Patrimonio> patrimonios) {
        List<PatrimonioEntity> entities = new ArrayList<>();
        for (Patrimonio patrimonio : patrimonios) {
            entities.add(converter.from(patrimonio));
        }
        repository.saveAll(entities);
    }

    public void atualizarTodos(List<Patrimonio> patrimonios) {
        for (Patrimonio patrimonio : patrimonios) {
            PatrimonioEntity entity = converter.from(patrimonio);
            repository.save(entity);
        }
    }

    @Transactional
    @Override
    public Patrimonio atualizar(Patrimonio patrimonio) {
        PatrimonioEntity entity = repository.save(converter.from(patrimonio));
        return converter.to(entity);
    }

    @Override
    public Optional<Patrimonio> buscarPorId(Long id) {
        Optional<PatrimonioEntity> patrimonioSalvo = repository.findById(id);
        return patrimonioSalvo.map(patrimonio -> converter.to(patrimonio));
    }

    @Override
    public ListaPaginada<Patrimonio> buscarPatrimoniosIncorporados(Patrimonio.Filtro filtro) {
        QPatrimonioEntity patrimonioEntity = QPatrimonioEntity.patrimonioEntity;

        BooleanExpression expression = patrimonioEntity.id.isNotNull()
            .and(patrimonioEntity.situacao.eq(Patrimonio.Situacao.ATIVO.toString())
                .or(patrimonioEntity.situacao.eq(Patrimonio.Situacao.ESTORNADO.toString())));

        if (Objects.nonNull(filtro.getConteudo())) {
            BooleanExpression conteudoExp = compararSemAcentuacao(patrimonioEntity.orgao.nome, filtro.getConteudo())
                .or(patrimonioEntity.orgao.sigla.trim().containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo().trim())))
                .or(compararSemAcentuacao(patrimonioEntity.itemIncorporacao.descricao, filtro.getConteudo()))
                .or(patrimonioEntity.numero.trim().containsIgnoreCase(filtro.getConteudo().trim()))
                .or(compararSemAcentuacao(patrimonioEntity.setor.nome, filtro.getConteudo()))
                .or(patrimonioEntity.setor.sigla.trim().containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo().trim())))
                .or(compararSemAcentuacao(patrimonioEntity.situacao, filtro.getConteudo().trim()));
            expression = expression.and(conteudoExp);
        }

        if (Objects.nonNull(filtro.getOrgaos())) {
            BooleanExpression conteudoExp = patrimonioEntity.orgao.id.in(filtro.getOrgaos())
                .or(patrimonioEntity.orgao.isNull());

            expression = expression.and(conteudoExp);
        }

        if (Objects.nonNull(filtro.getSetores())) {
            BooleanExpression conteudoExp = patrimonioEntity.setor.id.in(filtro.getSetores())
                .or(patrimonioEntity.setor.isNull());

            expression = expression.and(conteudoExp);
        }

        Page<PatrimonioEntity> entidadesEncontradas = buscar(expression, filtro);

        return montarListaPaginada(entidadesEncontradas);
    }

    @Override
    public ListaPaginada<Patrimonio> buscarPatrimoniosEstornados(Patrimonio.Filtro filtro) {
        QPatrimonioEntity patrimonioEntity = QPatrimonioEntity.patrimonioEntity;

        BooleanExpression expression = patrimonioEntity.id.isNotNull()
            .and(patrimonioEntity.situacao.eq(Patrimonio.Situacao.ESTORNADO.toString()));

        if (Objects.nonNull(filtro.getItensIncorporacaoId())) {
            BooleanExpression conteudoExp = patrimonioEntity.itemIncorporacao.id.in(filtro.getItensIncorporacaoId());
            expression = expression.and(conteudoExp);
        }

        Page<PatrimonioEntity> entidadesEncontradas = buscaPatrimoniosEstornados(expression, filtro);

        return montarListaPaginada(entidadesEncontradas);
    }

    @Override
    public ListaPaginada<Patrimonio> buscarPatrimoniosPorItensIncorporacao(Patrimonio.Filtro filtro) {
        QPatrimonioEntity patrimonioEntity = QPatrimonioEntity.patrimonioEntity;

        BooleanExpression expression = patrimonioEntity.id.isNotNull().and(patrimonioEntity.situacao.eq(Patrimonio.Situacao.ATIVO.toString()));

        if (Objects.nonNull(filtro.getConteudo())) {
            BooleanExpression conteudoExp = patrimonioEntity.numero.containsIgnoreCase(filtro.getConteudo().trim().toLowerCase())
                .or(patrimonioEntity.itemIncorporacao.codigo.toLowerCase().containsIgnoreCase(filtro.getConteudo().trim().toLowerCase()))
                .or(compararSemAcentuacao(patrimonioEntity.itemIncorporacao.descricao, filtro.getConteudo()));

            expression = expression.and(conteudoExp);
        }

        if (Objects.nonNull(filtro.getItensIncorporacaoId())) {
            BooleanExpression conteudoExp = patrimonioEntity.itemIncorporacao.id.in(filtro.getItensIncorporacaoId());
            expression = expression.and(conteudoExp);
        }

        Page<PatrimonioEntity> entidadesEncontradas = buscarPatrimoniosIncorporacao(expression, filtro);

        return montarListaPaginada(entidadesEncontradas);
    }

    @Override
    public ListaPaginada<Patrimonio> buscarPatrimoniosQueNaoEstaoEmOutraMovimentacaoEmElaboracao(Patrimonio.Filtro filtro) {
        QPatrimonioEntity qPatrimonioEntity = QPatrimonioEntity.patrimonioEntity;
        QMovimentacaoItemEntity qMovimentacaoItemEntity = QMovimentacaoItemEntity.movimentacaoItemEntity;

        BooleanExpression expression = qPatrimonioEntity.id.isNotNull().and(qPatrimonioEntity.situacao.eq(Patrimonio.Situacao.ATIVO.toString()));

        if (Objects.nonNull(filtro.getConteudo())) {
            BooleanExpression conteudoExp = qPatrimonioEntity.numero.containsIgnoreCase(filtro.getConteudo().trim().toLowerCase())
                .or(compararSemAcentuacao(qPatrimonioEntity.itemIncorporacao.descricao, filtro.getConteudo()))
                .or(qPatrimonioEntity.itemIncorporacao.incorporacao.codigo.containsIgnoreCase(filtro.getConteudo().trim().toLowerCase()));

            expression = expression.and(conteudoExp);
        }

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<PatrimonioEntity> query = jpaQueryFactory.selectFrom(qPatrimonioEntity)
            .where(expression
                .and(jpaQueryFactory
                    .from(qMovimentacaoItemEntity)
                    .where((qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.situacao.eq(Movimentacao.Situacao.EM_ELABORACAO.name())
                        .and(qMovimentacaoItemEntity.movimentacaoItemPK.patrimonio.eq(qPatrimonioEntity)))
                        .or(qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.situacao.eq(Movimentacao.Situacao.EM_PROCESSAMENTO.name()))
                        .and(qMovimentacaoItemEntity.movimentacaoItemPK.patrimonio.eq(qPatrimonioEntity))
                        .or(qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.situacao.eq(Movimentacao.Situacao.ERRO_PROCESSAMENTO.name()))
                        .and(qMovimentacaoItemEntity.movimentacaoItemPK.patrimonio.eq(qPatrimonioEntity))
                        .or(qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.situacao.eq(Movimentacao.Situacao.AGUARDANDO_DEVOLUCAO.name()))
                        .and(qMovimentacaoItemEntity.movimentacaoItemPK.patrimonio.eq(qPatrimonioEntity))
                        .or(qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.situacao.eq(Movimentacao.Situacao.DEVOLVIDO_PARCIAL.name()))
                        .and(qMovimentacaoItemEntity.dataDevolucao.isNull())
                        .and(qMovimentacaoItemEntity.movimentacaoItemPK.patrimonio.eq(qPatrimonioEntity))

                    ).notExists()
                )
                .and(qPatrimonioEntity.orgao.id.eq(filtro.getOrgao()))
                .and(qPatrimonioEntity.setor.id.eq(filtro.getSetor())))
            .orderBy(qPatrimonioEntity.numero.asc());
        query.offset(filtro.getPage() * filtro.getSize());
        query.limit(filtro.getSize());

        QueryResults<PatrimonioEntity> results = query.fetchResults();

        Page<PatrimonioEntity> entidadesEncontradas = construirPaginacao(results.getResults(),
            FiltroConverter.extrairPaginacao(filtro), results.getTotal());

        return montarListaPaginada(entidadesEncontradas);
    }

    @Modifying(flushAutomatically = true)
    @Override
    public Optional<Patrimonio> buscarPatrimonioComMaiorNumero() {
        Optional<PatrimonioEntity> encontrada = repository.findFirstByNumeroNotNullOrderByNumeroDesc();
        return encontrada.map(patrimonioEntity -> converter.to(patrimonioEntity));
    }

    @Modifying(flushAutomatically = true)
    @Override
    public Optional<Patrimonio> buscarPatrimonioComMaiorNumeroPorOrgao(Long orgaoId) {
        Optional<PatrimonioEntity> encontrada = repository.findFirstByNumeroNotNullAndOrgao_IdOrderByNumeroDesc(orgaoId);
        return encontrada.map(patrimonioEntity -> converter.to(patrimonioEntity));
    }

    @Override
    public Boolean existeEmIntervalo(Patrimonio.Filtro filtro) {
        String numeroInicioFormatado = StringUtils.padLeftZeros(filtro.getNumeroInicio().toString(), 10);
        String numeroFimFormatado = StringUtils.padLeftZeros(filtro.getNumeroFim().toString(), 10);

        QPatrimonioEntity qPatrimonioEntity = QPatrimonioEntity.patrimonioEntity;

        BooleanExpression expression = qPatrimonioEntity.id.isNotNull();

        if (Objects.nonNull(filtro.getOrgao())) {
            expression = expression.and(qPatrimonioEntity.orgao.id.eq(filtro.getOrgao()));
        }

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<PatrimonioEntity> query = jpaQueryFactory.selectFrom(qPatrimonioEntity)
            .where(expression
                .and(qPatrimonioEntity.numero.goe(numeroInicioFormatado)
                    .and(qPatrimonioEntity.numero.loe(numeroFimFormatado))
                ));
        query.offset(0L);
        query.limit(1L);

        QueryResults<PatrimonioEntity> results = query.fetchResults();

        return results.getResults().size() == 1;
    }

    @Override
    public ListaPaginada<Patrimonio> buscarPorFiltro(Patrimonio.Filtro filtro) {
        Page<PatrimonioEntity> entidadesEncontradas = buscarComOrdenacao(
            ItemIncorporacaoEntity
                .builder()
                .id(filtro.getItemIncorporacaoId())
                .build(),
            filtro
        );

        return montarListaPaginada(entidadesEncontradas);
    }

    public List<Patrimonio> buscarTodos(Long itemIncorporacaoId) {
        return repository.findAllByItemIncorporacao_Id(itemIncorporacaoId)
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    @Override
    public Boolean existe(Long id) {
        return repository.existsById(id);
    }

    @Override
    public Boolean existePorChassi(String chassi, Long id) {
        return repository.existsByChassiAndIdNot(chassi, id);
    }

    @Override
    public Boolean existePorNumeroSerie(String numeroSerie, Long id) {
        return repository.existsByNumeroSerieAndIdNot(numeroSerie, id);
    }

    @Override
    public Boolean existePorMotor(String motor, Long id) {
        return repository.existsByMotorAndIdNot(motor, id);
    }

    @Override
    public Boolean existePorPlaca(String placa, Long id) {
        return repository.existsByPlacaAndIdNot(placa, id);
    }

    @Override
    public Boolean existePorRenavam(String renavam, Long id) {
        return repository.existsByRenavamAndIdNot(renavam, id);
    }

    private Page<PatrimonioEntity> buscarComOrdenacao(ItemIncorporacaoEntity itemIncorporacaoId, Patrimonio.Filtro filtro) {
        if (Objects.nonNull(filtro.getSituacao())) {
            return repository.findAllByItemIncorporacaoEqualsAndSituacaoEquals(itemIncorporacaoId, FiltroConverter.extrairPaginacao(filtro), filtro.getSituacao());
        }
        return repository.findAllByItemIncorporacaoEquals(itemIncorporacaoId, FiltroConverter.extrairPaginacao(filtro));
    }

    private Page<PatrimonioEntity> buscar(BooleanExpression expression, Patrimonio.Filtro filtro) {
        return buscaPatrimoniosIncorporados(expression, filtro);
    }

    @Override
    public Long quantidadePorItemIncorporacao(Long itemIncorporacaoId) {
        return repository.countAllByItemIncorporacao_Id(itemIncorporacaoId);
    }

    @Override
    public void excluiUltimosPatrimonios(Long qtdPatrimoniosASeremExcluidos, Long itemIncorporacaoId) {
        for (int i = 0; i < qtdPatrimoniosASeremExcluidos; i++) {
            Optional<PatrimonioEntity> patrimonioEntity = repository.findFirstByItemIncorporacao_IdOrderByNumeroDesc(itemIncorporacaoId);
            repository.deleteById(patrimonioEntity.get().getId());
        }
    }

    @Override
    public List<Patrimonio> buscarPatrimoniosPorItemIncorporacaoId(Long itemIncorporacaoId) {
        List<PatrimonioEntity> patrimonioEntityList = repository.findAllByItemIncorporacao_Id(itemIncorporacaoId);

        return patrimonioEntityList
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    @Override
    public List<Patrimonio> buscarPatrimoniosAtivosPorIncorporacaoId(Long incorporacaoId, List<Long> patrimoniosExcecao) {
        List<PatrimonioEntity> patrimonioEntityList = repository.buscarPatrimoniosAtivosPorIncorporacaoId(incorporacaoId, patrimoniosExcecao);

        return patrimonioEntityList
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    @Override
    public Long buscarQuantidadePatrimoniosPorIncorporacaoId(Long incorporacaoId) {
        return repository.buscarQuantidadePatrimoniosPorIncorporacaoId(incorporacaoId);
    }

    @Override
    public Long buscarQuantidadePatrimoniosAtivosPorIncorporacaoId(Long incorporacaoId) {
        return repository.buscarQuantidadePatrimoniosAtivosPorIncorporacaoId(incorporacaoId);
    }

    private Page<PatrimonioEntity> buscaPatrimoniosIncorporados(BooleanExpression expression, Patrimonio.Filtro filtro) {
        return repository.findAll(expression, FiltroConverter.extrairPaginacao(filtro, "orgao.sigla", "numero"));
    }

    private Page<PatrimonioEntity> buscaPatrimoniosEstornados(BooleanExpression expression, Patrimonio.Filtro filtro) {
        return repository.findAll(expression, FiltroConverter.extrairPaginacao(filtro));
    }

    private Page<PatrimonioEntity> buscarPatrimoniosIncorporacao(BooleanExpression expression, Patrimonio.Filtro filtro) {
        return repository.findAll(expression, FiltroConverter.extrairPaginacao(filtro));
    }

    private static Page<PatrimonioEntity> construirPaginacao(List<PatrimonioEntity> patrimonios, Pageable pageable, Long totalRegistros) {
        return new PageImpl<>(patrimonios, pageable, totalRegistros);
    }

    private BooleanExpression compararSemAcentuacao(StringExpression path, String value) {
        StringExpression expr = Expressions.stringTemplate("upper(translate({0}, 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç', 'AAAAAAAAEEEEIIOOOOOOUUUUCC'))", path.trim());
        return expr.containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(value.trim()));
    }

    private ListaPaginada<Patrimonio> montarListaPaginada(Page<PatrimonioEntity> entidadesEncontradas) {
        List<Patrimonio> entidades = entidadesEncontradas
            .getContent()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());

        return ListaPaginada
            .<Patrimonio>builder()
            .items(entidades)
            .totalElements(entidadesEncontradas.getTotalElements())
            .totalPages((long) entidadesEncontradas.getTotalPages())
            .build();
    }

    @Override
    public List<Patrimonio> buscarTodosPatrimonios(List<Long> patrimoniosId) {
        List<PatrimonioEntity> patrimonioEntityList = repository.findAllByIdIn(patrimoniosId);

        return patrimonioEntityList
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    @Override
    public Long buscarQuantidadePatrimoniosEmOutrasMovimentacoesPorIncorporacaoId(Long incorporacaoId) {
        return repository.buscarQuantidadePatrimoniosEmOutrasMovimentacoesPorIncorporacaoId(incorporacaoId);
    }

    @Override
    public List<Patrimonio> buscarPatrimoniosAtivosPorOrgaoSetorQueNaoEstaoEmOutraMovimentacao(List<Long> patrimoniosNaoConsiderar, Long orgaoId, Long setorId) {
        QPatrimonioEntity qPatrimonioEntity = QPatrimonioEntity.patrimonioEntity;
        QMovimentacaoItemEntity qMovimentacaoItemEntity = QMovimentacaoItemEntity.movimentacaoItemEntity;

        final BooleanExpression notExistsMovimentacaoEmElaboracao = new JPAQueryFactory(entityManager)
            .from(qMovimentacaoItemEntity)
            .where(qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.situacao.eq(Movimentacao.Situacao.EM_ELABORACAO.name())
                .and(qMovimentacaoItemEntity.movimentacaoItemPK.patrimonio.eq(qPatrimonioEntity)))
            .notExists();

        final List<PatrimonioEntity> results = new JPAQueryFactory(entityManager)
            .selectFrom(qPatrimonioEntity)
            .where(qPatrimonioEntity.orgao.id.eq(orgaoId)
                .and(qPatrimonioEntity.setor.id.eq(setorId))
                .and(qPatrimonioEntity.situacao.eq(Patrimonio.Situacao.ATIVO.name()))
                .and(qPatrimonioEntity.id.notIn(patrimoniosNaoConsiderar))
                .and(notExistsMovimentacaoEmElaboracao))
            .fetchResults()
            .getResults();

        return results.stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    @Override
    public List<Patrimonio> buscarPatrimoniosAtivosPorIdQueNaoEstaoEmOutraMovimentacao(List<Long> patrimoniosId, Long orgaoId, Long setorId) {
        QPatrimonioEntity qPatrimonioEntity = QPatrimonioEntity.patrimonioEntity;
        QMovimentacaoItemEntity qMovimentacaoItemEntity = QMovimentacaoItemEntity.movimentacaoItemEntity;

        final BooleanExpression notExistsMovimentacaoEmElaboracao = new JPAQueryFactory(entityManager)
            .from(qMovimentacaoItemEntity)
            .where(qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.situacao.eq(Movimentacao.Situacao.EM_ELABORACAO.name())
                .and(qMovimentacaoItemEntity.movimentacaoItemPK.patrimonio.eq(qPatrimonioEntity)))
            .notExists();

        final List<PatrimonioEntity> results = new JPAQueryFactory(entityManager)
            .selectFrom(qPatrimonioEntity)
            .where(qPatrimonioEntity.id.in(patrimoniosId)
                .and(qPatrimonioEntity.orgao.id.eq(orgaoId))
                .and(qPatrimonioEntity.setor.id.eq(setorId))
                .and(qPatrimonioEntity.situacao.eq(Patrimonio.Situacao.ATIVO.name()))
                .and(notExistsMovimentacaoEmElaboracao))
            .fetchResults()
            .getResults();

        return results.stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    @Override
    public ListaPaginada<Patrimonio> buscarPatrimoniosAguardandoDevolucaoPorMovimentacao(Patrimonio.Filtro filtro, Long movimentacaoId) {
        QPatrimonioEntity qPatrimonioEntity = QPatrimonioEntity.patrimonioEntity;
        QMovimentacaoItemEntity qMovimentacaoItemEntity = QMovimentacaoItemEntity.movimentacaoItemEntity;

        BooleanExpression expression = qPatrimonioEntity.id.isNotNull();

        if (Objects.nonNull(filtro.getConteudo())) {
            BooleanExpression conteudoExp = qPatrimonioEntity.numero.containsIgnoreCase(filtro.getConteudo().trim().toLowerCase())
                .or(compararSemAcentuacao(qPatrimonioEntity.itemIncorporacao.descricao, filtro.getConteudo()));
            expression = expression.and(conteudoExp);
        }

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<PatrimonioEntity> query = jpaQueryFactory.selectFrom(qPatrimonioEntity)
            .innerJoin(qMovimentacaoItemEntity)
            .on(qMovimentacaoItemEntity.movimentacaoItemPK.patrimonio.eq(qPatrimonioEntity))
            .where(expression
                .and(qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.id.eq(movimentacaoId))
                .and(qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.tipo.equalsIgnoreCase(TipoMovimentacaoEnum.TEMPORARIA.name()))
                .and(qMovimentacaoItemEntity.dataDevolucao.isNull())
            )
            .orderBy(qPatrimonioEntity.numero.asc());

        query.offset(filtro.getPage() * filtro.getSize());
        query.limit(filtro.getSize());

        QueryResults<PatrimonioEntity> results = query.fetchResults();

        Page<PatrimonioEntity> entidadesEncontradas = construirPaginacao(results.getResults(), FiltroConverter.extrairPaginacao(filtro), results.getTotal());

        return montarListaPaginada(entidadesEncontradas);

    }

    @Override
    public List<Patrimonio> buscarPatrimoniosPorIncorporacao(Patrimonio.Filtro filtro) {
        Pageable pageable = FiltroConverter.extrairPaginacao(filtro);
        Page<PatrimonioEntity> results = repository.buscarPorIncorporacaoPaginado(filtro.getIncorporacaoId(), pageable);

        return results.getContent().stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    @Override
    public List<Patrimonio> buscarPatrimoniosPorIncorporacao(Long incorporacaoId) {
        List<PatrimonioEntity> results = repository.findAllByItemIncorporacao_Incorporacao_Id(incorporacaoId);
        return results.stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    @Override
    public void bloquearEntidade() {
        databaseLocker.lockTableByClass(PatrimonioEntity.class);
    }
}
