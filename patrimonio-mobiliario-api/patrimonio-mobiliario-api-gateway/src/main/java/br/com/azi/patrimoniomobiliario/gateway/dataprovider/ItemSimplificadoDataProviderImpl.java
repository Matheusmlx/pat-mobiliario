package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemSimplificado;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemSimplificadoDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.FiltroConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.ItemSimplificadoConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ItemSimplificadoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QItemSimplificadoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.ItemSimplificadoReadOnlyRepository;
import br.com.azi.patrimoniomobiliario.utils.AcetuacaoUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemSimplificadoDataProviderImpl implements ItemSimplificadoDataProvider {


    @Autowired
    private ItemSimplificadoReadOnlyRepository repository;

    @Autowired
    private ItemSimplificadoConverter converter;

    @Override
    public ListaPaginada<ItemSimplificado> buscarPorFiltro(ItemSimplificado.Filtro filtro) {
        QItemSimplificadoEntity itemSimplificadoQuery = QItemSimplificadoEntity.itemSimplificadoEntity;

        BooleanExpression expression = itemSimplificadoQuery.id.isNotNull().and(itemSimplificadoQuery.situacao.eq("ATIVO"));

        if (!StringUtils.isEmpty(filtro.getConteudo())) {
            BooleanExpression conteudoExp = itemSimplificadoQuery.codigo.trim().containsIgnoreCase(filtro.getConteudo().trim())
                .or(itemSimplificadoQuery.codigo.trim().containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo().trim())))
                .or(itemSimplificadoQuery.descricao.trim().containsIgnoreCase(filtro.getConteudo()))
                .or(itemSimplificadoQuery.descricao.trim().containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo()).trim()));
            expression = expression.and(conteudoExp);
        }

        Page<ItemSimplificadoEntity> entidadesEncontradas = repository.findAll(expression, FiltroConverter.extrairPaginacao(filtro));

        List<ItemSimplificado> entidades = entidadesEncontradas
            .getContent()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
        return ListaPaginada.<ItemSimplificado>builder()
            .items(entidades)
            .totalElements(entidadesEncontradas.getTotalElements())
            .totalPages((long) entidadesEncontradas.getTotalPages())
            .build();
    }
}
