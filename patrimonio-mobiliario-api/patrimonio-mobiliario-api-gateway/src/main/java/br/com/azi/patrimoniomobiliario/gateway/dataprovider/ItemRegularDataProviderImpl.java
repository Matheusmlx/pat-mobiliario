package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemRegular;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemRegularDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.FiltroConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.ItemRegularConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ItemRegularEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QItemRegularEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.ItemRegularReadOnlyRepository;
import br.com.azi.patrimoniomobiliario.utils.AcetuacaoUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemRegularDataProviderImpl implements ItemRegularDataProvider {

    @Autowired
    private ItemRegularReadOnlyRepository repository;

    @Autowired
    private ItemRegularConverter converter;

    @Override
    public ListaPaginada<ItemRegular> buscarPorFiltro(ItemRegular.Filtro filtro) {
        QItemRegularEntity itemRegularQuery = QItemRegularEntity.itemRegularEntity;

        BooleanExpression expression = itemRegularQuery.id.isNotNull().and(itemRegularQuery.situacao.eq("ATIVO"));

        if (!StringUtils.isEmpty(filtro.getConteudo())) {
            BooleanExpression conteudoExp = itemRegularQuery.codigo.trim().containsIgnoreCase(filtro.getConteudo().trim())
                .or(itemRegularQuery.codigo.trim().containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo().trim())))
                .or(itemRegularQuery.descricao.trim().containsIgnoreCase(filtro.getConteudo()))
                .or(itemRegularQuery.descricao.trim().containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo()).trim()));
            expression = expression.and(conteudoExp);
        }

        Page<ItemRegularEntity> entidadesEncontradas = repository.findAll(expression, FiltroConverter.extrairPaginacao(filtro));

        List<ItemRegular> entidades = entidadesEncontradas
            .getContent()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
        return ListaPaginada.<ItemRegular>builder()
            .items(entidades)
            .totalElements(entidadesEncontradas.getTotalElements())
            .totalPages((long) entidadesEncontradas.getTotalPages())
            .build();
    }
}
