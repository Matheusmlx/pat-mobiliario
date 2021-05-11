package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.FiltroConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.MovimentacaoItemConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.MovimentacaoItemEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QMovimentacaoItemEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.MovimentacaoItemRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MovimentacaoItemDataProviderImpl implements MovimentacaoItemDataProvider {

    @Autowired
    private MovimentacaoItemRepository repository;

    @Autowired
    private MovimentacaoItemConverter converter;

    @Override
    public List<MovimentacaoItem> buscarPorPatrimonio(Long id) {
        List<MovimentacaoItemEntity> entidades = repository.findAllByMovimentacaoItemPK_Patrimonio_IdOrderByMovimentacaoItemPK_Movimentacao_DataFinalizacaoDesc(id);
        return entidades
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    @Override
    public ListaPaginada<MovimentacaoItem> buscarPorMovimentacaoId(MovimentacaoItem.Filtro filtro) {
        QMovimentacaoItemEntity qMovimentacaoItemEntity = QMovimentacaoItemEntity.movimentacaoItemEntity;
        BooleanExpression expression = qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.id.eq(filtro.getMovimentacaoId());

        filtro.setSort("movimentacaoItemPK.patrimonio.numero");
        filtro.setDirection("ASC");

        final Pageable pageable = FiltroConverter.extrairPaginacao(filtro);

        final Page<MovimentacaoItemEntity> movimentacaoItemPage = repository.findAll(expression, pageable);
        return ListaPaginada.<MovimentacaoItem>builder()
            .items(movimentacaoItemPage.getContent()
                .stream()
                .map(converter::to)
                .collect(Collectors.toList())
            )
            .totalPages((long) movimentacaoItemPage.getTotalPages())
            .totalElements(movimentacaoItemPage.getTotalElements())
            .build();
    }

    @Override
    public List<MovimentacaoItem> buscarPorMovimentacaoId(Long movimentacaoId) {
        final List<MovimentacaoItemEntity> itens = repository.findAllByMovimentacaoItemPK_Movimentacao_Id(movimentacaoId);
        return itens.stream().map(converter::to).collect(Collectors.toList());
    }

    @Override
    public Optional<MovimentacaoItem> buscarPorMovimentacaoEPatrimonio(Long movimentacaoId, Long patrimonioId) {
        Optional<MovimentacaoItemEntity> entidade = repository.findByMovimentacaoItemPK_Movimentacao_IdAndMovimentacaoItemPK_Patrimonio_Id(movimentacaoId, patrimonioId);

        return entidade.map(converter::to);
    }

    @Override
    public MovimentacaoItem salvar(MovimentacaoItem movimentacaoItem) {
        MovimentacaoItemEntity entidade = repository.save(converter.from(movimentacaoItem));
        return converter.to(entidade);
    }

    @Override
    public List<MovimentacaoItem> salvar(List<MovimentacaoItem> movimentacaoItens) {
        final List<MovimentacaoItemEntity> entidades = movimentacaoItens.stream()
            .map(converter::from)
            .collect(Collectors.toList());

        final List<MovimentacaoItemEntity> entidadesSalva = repository.saveAll(entidades);

        return entidadesSalva.stream().map(converter::to).collect(Collectors.toList());
    }

    @Override
    public void remover(MovimentacaoItem movimentacaoItem) {
        repository.delete(converter.from(movimentacaoItem));
    }

    @Transactional
    @Modifying(flushAutomatically = true)
    @Override
    public void removerPorMovimentacao(Long id) {
        repository.deleteAllByMovimentacaoItemPK_Movimentacao_Id(id);
    }

    @Override
    public Long buscarQuantidadeItensPorMovimentacaoId(Long movimentacaoId) {
        return repository.countByMovimentacaoItemPK_Movimentacao_Id(movimentacaoId);
    }

    @Override
    public boolean existePorMovimentacaoId(Long movimentacaoId) {
        return repository.existsByMovimentacaoItemPK_Movimentacao_Id(movimentacaoId);
    }

    @Override
    public boolean aguardandoDevolucao(Long movimentacaoId, Long patrimonioId) {
        return repository.existsByMovimentacaoItemPK_Movimentacao_IdAndMovimentacaoItemPK_Patrimonio_IdAndDataDevolucaoIsNull(movimentacaoId, patrimonioId);
    }

    @Override
    public boolean existeItemAguardandoDevolucaoPorMovimentacao(Long movimentacaoId) {
        return repository.existsByMovimentacaoItemPK_Movimentacao_IdAndDataDevolucaoIsNull(movimentacaoId);
    }

    @Override
    public List<MovimentacaoItem> buscarItensAguardandoDevolucao(Long movimentacaoId) {
        List<MovimentacaoItemEntity> itens = repository.findAllByMovimentacaoItemPK_Movimentacao_IdAndDataDevolucaoIsNull(movimentacaoId);
        return itens.stream().map(converter::to).collect(Collectors.toList());
    }

    @Override
    public Long buscarQuantidadeItensDevolvidos(Long movimentacaoId) {
        return repository.countByMovimentacaoItemPK_Movimentacao_IdAndDataDevolucaoIsNotNull(movimentacaoId);
    }

    @Override
    public boolean existeDistribuicaoFinalizadaParaPatrimonio(Long patrimonioId) {
        return repository.existeDistribuicaoFinalizadaParaPatrimonio(patrimonioId);
    }

    @Override
    public Optional<MovimentacaoItem> buscarUltimaMovimentacaoPatrimonioNoPeriodo(Long patrimonioId, Date dataInicial, Date dataFinal) {
        QMovimentacaoItemEntity qMovimentacaoItemEntity = QMovimentacaoItemEntity.movimentacaoItemEntity;
        BooleanExpression expression = qMovimentacaoItemEntity.movimentacaoItemPK.patrimonio.id.eq(patrimonioId)
            .and(qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.situacao.eq(Movimentacao.Situacao.FINALIZADO.name()))
            .and(qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.dataFinalizacao.between(dataInicial, dataFinal));

        Pageable pageable = PageRequest.of(0, 1, new Sort(Sort.Direction.DESC, "movimentacaoItemPK.movimentacao.dataFinalizacao"));

        final Page<MovimentacaoItemEntity> page = repository.findAll(expression, pageable);
        return page.getContent().isEmpty() ? Optional.empty() : Optional.of(converter.to(page.getContent().get(0)));
    }

    @Override
    public Optional<MovimentacaoItem> buscarUltimaMovimentacaoPatrimonioPorTipoNoPeriodo(Long patrimonioId, TipoMovimentacaoEnum tipo, Date dataInicial, Date dataFinal) {
        QMovimentacaoItemEntity qMovimentacaoItemEntity = QMovimentacaoItemEntity.movimentacaoItemEntity;
        BooleanExpression expression = qMovimentacaoItemEntity.movimentacaoItemPK.patrimonio.id.eq(patrimonioId)
            .and(qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.situacao.eq(Movimentacao.Situacao.FINALIZADO.name()))
            .and(qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.dataFinalizacao.between(dataInicial, dataFinal))
            .and(qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.tipo.eq(tipo.name()));

        Pageable pageable = PageRequest.of(0, 1, new Sort(Sort.Direction.DESC, "movimentacaoItemPK.movimentacao.dataFinalizacao"));

        final Page<MovimentacaoItemEntity> page = repository.findAll(expression, pageable);
        return page.getContent().isEmpty() ? Optional.empty() : Optional.of(converter.to(page.getContent().get(0)));
    }


    @Override
    public Optional<MovimentacaoItem> buscarUltimaMovimentacaoPatrimonioAntesDe(Long patrimonioId, Date dataReferencia) {
        QMovimentacaoItemEntity qMovimentacaoItemEntity = QMovimentacaoItemEntity.movimentacaoItemEntity;
        BooleanExpression expression = qMovimentacaoItemEntity.movimentacaoItemPK.patrimonio.id.eq(patrimonioId)
            .and(qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.situacao.eq(Movimentacao.Situacao.FINALIZADO.name()))
            .and(qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.dataFinalizacao.before(dataReferencia));

        Pageable pageable = PageRequest.of(0, 1, new Sort(Sort.Direction.DESC, "movimentacaoItemPK.movimentacao.dataFinalizacao"));

        final Page<MovimentacaoItemEntity> page = repository.findAll(expression, pageable);
        return page.getContent().isEmpty() ? Optional.empty() : Optional.of(converter.to(page.getContent().get(0)));
    }

    @Override
    public boolean existeDistribuicaoFinalizadaParaPatrimonioNoPeriodo(Long patrimonioId, Date dataInicial, Date dataFinal) {
        QMovimentacaoItemEntity qMovimentacaoItemEntity = QMovimentacaoItemEntity.movimentacaoItemEntity;
        BooleanExpression expression = qMovimentacaoItemEntity.movimentacaoItemPK.patrimonio.id.eq(patrimonioId)
            .and(qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.situacao.eq(Movimentacao.Situacao.FINALIZADO.name()))
            .and(qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.tipo.eq(TipoMovimentacaoEnum.DISTRIBUICAO.name()))
            .and(qMovimentacaoItemEntity.movimentacaoItemPK.movimentacao.dataFinalizacao.between(dataInicial, dataFinal));

        return repository.exists(expression);
    }

}
