package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.EditaItemIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.FiltroConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.ItemIncorporacaoConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ItemIncorporacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QItemIncorporacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.ItemIncorporacaoRepository;
import br.com.azi.patrimoniomobiliario.utils.AcetuacaoUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ItemIncorporacaoDataProviderImpl implements ItemIncorporacaoDataProvider {

    @Autowired
    private ItemIncorporacaoRepository repository;

    @Autowired
    private ItemIncorporacaoConverter converter;

    @Override
    public Optional<ItemIncorporacao> buscarPorId(Long id) {
        Optional<ItemIncorporacaoEntity> encontrada = repository.findById(id);
        return encontrada.map(itemIncorporacaoEntity -> converter.to(itemIncorporacaoEntity));
    }

    @Override
    public Boolean existe(Long id) {
        return repository.existsById(id);
    }

    @Override
    public Boolean existeItemComMesmaNatureza(EditaItemIncorporacaoInputData inputData){
       return repository.existsByIncorporacao_IdAndNaturezaDespesa_IdAndCodigoLikeAndIdNot(inputData.getIdIncorporacao(),inputData.getNaturezaDespesa(),inputData.getCodigo(),inputData.getId());
    }

    @Override
    @Modifying(flushAutomatically = true)
    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Modifying(flushAutomatically = true)
    @Transactional
    public ItemIncorporacao atualizar(ItemIncorporacao business) {
        ItemIncorporacaoEntity salvo = repository.save(converter.from(business));
        return converter.to(salvo);
    }

    @Override
    @Modifying(flushAutomatically = true)
    @Transactional
    public ItemIncorporacao salvar(ItemIncorporacao item) {
        ItemIncorporacaoEntity salvo = repository.save(converter.from(item));
        return converter.to(salvo);
    }

    @Override
    public void salvarTodos(List<ItemIncorporacao> itens) {
        List<ItemIncorporacaoEntity> entities = new ArrayList<>();
        for (ItemIncorporacao itemIncorporacao : itens) {
            entities.add(converter.from(itemIncorporacao));
        }
        repository.saveAll(entities);
    }

    @Override
    public BigDecimal buscaSomaDeValorTotalDosItens(Long incorporacaoId) {
        return repository.buscaSomaDeValorTotalDosItens(incorporacaoId);
    }

    @Override
    @Modifying(flushAutomatically = true)
    @Transactional
    public List<ItemIncorporacao> buscarItensPorIncorporacaoId(Long incorporacaoId) {
        List<ItemIncorporacaoEntity> itemIncorporacaoEntityList = repository.findAllByIncorporacao_Id(incorporacaoId);

        return itemIncorporacaoEntityList
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    @Override
    public ListaPaginada<ItemIncorporacao> buscarPorIncorporacaoId(ItemIncorporacao.Filtro filtro) {
        QItemIncorporacaoEntity itemIncorporacaoQuery = QItemIncorporacaoEntity.itemIncorporacaoEntity;
        BooleanExpression expression = itemIncorporacaoQuery.id.isNotNull().and(itemIncorporacaoQuery.incorporacao.id.eq(filtro.getIncorporacaoId()));

        if(!StringUtils.isEmpty(filtro.getConteudo())){
            BooleanExpression conteudoExp = itemIncorporacaoQuery.codigo.trim().containsIgnoreCase(filtro.getConteudo().trim())
                .or(itemIncorporacaoQuery.codigo.trim().containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo().trim())))
                    .or(itemIncorporacaoQuery.descricao.trim().containsIgnoreCase(filtro.getConteudo().trim()))
                    .or(itemIncorporacaoQuery.descricao.trim().containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo().trim())));

            expression = expression.and(conteudoExp);
        }

        Page<ItemIncorporacaoEntity> entidadesEncontradas = buscaComOrdenacao(expression,filtro);

        List<ItemIncorporacao> entidades = entidadesEncontradas
            .getContent()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());

        return ListaPaginada.<ItemIncorporacao>builder()
            .items(entidades)
            .totalElements(entidadesEncontradas.getTotalElements())
            .totalPages((long) entidadesEncontradas.getTotalPages())
            .build();
    }

    private Page<ItemIncorporacaoEntity> buscaComOrdenacao(BooleanExpression expression, ItemIncorporacao.Filtro filtro){
        if (Objects.isNull(filtro.getSort())) {
            filtro.setSort("situacao");
            filtro.setDirection("ASC");

            return repository.findAll(expression, FiltroConverter.extrairPaginacao(filtro));
        } else {
            final Pageable pageable = FiltroConverter.extrairPaginacaoComSegundaOrdenacao(filtro, filtro.getSort());
            return repository.findAll(expression, pageable);
        }
    }
}
