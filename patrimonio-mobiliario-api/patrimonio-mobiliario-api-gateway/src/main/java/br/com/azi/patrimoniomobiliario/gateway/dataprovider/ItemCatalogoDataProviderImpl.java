package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemCatalogo;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemRegular;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemSimplificado;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemCatalogoDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.FiltroConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.ItemCatalogoConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.ItemRegularConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.ItemSimplificadoConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ItemRegularEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ItemSimplificadoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.ItemRegularReadOnlyRepository;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.ItemSimplificadoReadOnlyRepository;
import br.com.azi.patrimoniomobiliario.utils.AcetuacaoUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ItemCatalogoDataProviderImpl implements ItemCatalogoDataProvider {

    @Autowired
    private ItemSimplificadoReadOnlyRepository itemSimplificadoReadOnlyRepository;

    @Autowired
    private ItemRegularReadOnlyRepository itemRegularReadOnlyRepository;

    @Autowired
    private ItemCatalogoConverter itemCatalogoConverter;

    @Autowired
    private ItemRegularConverter itemRegularConverter;

    @Autowired
    private ItemSimplificadoConverter itemSimplificadoConverter;

    @Override
    @Transactional
    public Optional<Object> buscarPorId(Long itemId) {
        Optional<ItemSimplificadoEntity> simplificado = itemSimplificadoReadOnlyRepository.findById(itemId);
        Optional<ItemRegularEntity> regular = itemRegularReadOnlyRepository.findById(itemId);

        if (simplificado.isPresent()) {
            return Optional.of(itemCatalogoConverter.to(simplificado.get()));
        }
        if (regular.isPresent()) {
            return Optional.of(itemCatalogoConverter.to(regular.get()));
        }

        return Optional.empty();
    }

    @Override
    public Optional<Object> buscarPorCodigo(String codigo) {

        Optional<ItemSimplificadoEntity> simplificado = itemSimplificadoReadOnlyRepository.findByCodigo(codigo);
        Optional<ItemRegularEntity> regular = itemRegularReadOnlyRepository.findByCodigo(codigo);

        if (simplificado.isPresent()) {
            return Optional.of(itemCatalogoConverter.to(simplificado.get()));
        }
        if (regular.isPresent()) {
            return Optional.of(itemCatalogoConverter.to(regular.get()));
        }

        return Optional.empty();
    }

    @Override
    public ListaPaginada<ItemCatalogo> buscarPorFiltro(ItemCatalogo.Filtro filtro) {
        ListaPaginada<ItemRegular> itensRegulares = buscarItensRegularesPorFiltro(filtro);
        ListaPaginada<ItemSimplificado> itensSimplificados = buscarItensSimplificadosPorFiltro(filtro);

        List<ItemCatalogo> listaItensObject = retornaListaComItemRegularESimplificado(itensRegulares.getItems(), itensSimplificados.getItems());

        List<ItemCatalogo> listaOrdenada = new ArrayList<>();
        if (filtro.getSort().equalsIgnoreCase("codigo") && filtro.getDirection().equalsIgnoreCase("asc")) {
            listaOrdenada = listaItensObject.stream().sorted(Comparator.comparing(ItemCatalogo::getCodigo)).collect(Collectors.toList());
        } else if (filtro.getSort().equalsIgnoreCase("codigo") && !filtro.getDirection().equalsIgnoreCase("asc")) {
            listaOrdenada = listaItensObject.stream().sorted(Comparator.comparing(ItemCatalogo::getCodigo).reversed()).collect(Collectors.toList());
        } else if (filtro.getSort().equalsIgnoreCase("descricao") && filtro.getDirection().equalsIgnoreCase("asc")) {
            listaOrdenada = listaItensObject.stream().sorted(Comparator.comparing(ItemCatalogo::getDescricao)).collect(Collectors.toList());
        } else if (filtro.getSort().equalsIgnoreCase("descricao") && !filtro.getDirection().equalsIgnoreCase("asc")) {
            listaOrdenada = listaItensObject.stream().sorted(Comparator.comparing(ItemCatalogo::getDescricao).reversed()).collect(Collectors.toList());
        }

        return constroiListaPaginadaCatalogo(itensRegulares, itensSimplificados, listaOrdenada, filtro);
    }

    private ListaPaginada<ItemRegular> buscarItensRegularesPorFiltro(ItemCatalogo.Filtro filtro) {
        Page<ItemRegularEntity> entidadesEncontradas;

        if (!StringUtils.isEmpty(filtro.getConteudo())) {
            filtro.setConteudo("%" + AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo()) + "%");
            entidadesEncontradas = itemRegularReadOnlyRepository.findAllByNaturezaContratacaoPermanenteFiltro(filtro.getConteudo(), FiltroConverter.extrairPaginacao(filtro));
        } else {
            entidadesEncontradas = itemRegularReadOnlyRepository.findAllByNaturezaContratacaoPermanente(FiltroConverter.extrairPaginacao(filtro));
        }

        List<ItemRegular> entidades = entidadesEncontradas
            .getContent()
            .stream()
            .map(itemRegularConverter::to)
            .collect(Collectors.toList());
        return ListaPaginada.<ItemRegular>builder()
            .items(entidades)
            .totalElements(entidadesEncontradas.getTotalElements())
            .totalPages((long) entidadesEncontradas.getTotalPages())
            .build();
    }

    private ListaPaginada<ItemSimplificado> buscarItensSimplificadosPorFiltro(ItemCatalogo.Filtro filtro) {
        Page<ItemSimplificadoEntity> entidadesEncontradas;

        if (!StringUtils.isEmpty(filtro.getConteudo())) {
            filtro.setConteudo("%" + AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo()) + "%");
            entidadesEncontradas = itemSimplificadoReadOnlyRepository.findAllByNaturezaContratacaoPermanenteFiltro(filtro.getConteudo(), FiltroConverter.extrairPaginacao(filtro));
        } else {
            entidadesEncontradas = itemSimplificadoReadOnlyRepository.findAllByNaturezaContratacaoPermanente(FiltroConverter.extrairPaginacao(filtro));
        }

        List<ItemSimplificado> entidades = entidadesEncontradas
            .getContent()
            .stream()
            .map(itemSimplificadoConverter::to)
            .collect(Collectors.toList());
        return ListaPaginada.<ItemSimplificado>builder()
            .items(entidades)
            .totalElements(entidadesEncontradas.getTotalElements())
            .totalPages((long) entidadesEncontradas.getTotalPages())
            .build();
    }

    private List<ItemCatalogo> retornaListaComItemRegularESimplificado(List<ItemRegular> itensRegulares, List<ItemSimplificado> itensSimplificados) {
        List<ItemCatalogo> listaItensCatalogo = new ArrayList<>();
        for (ItemRegular item : itensRegulares) {
            ItemCatalogo itemObject = itemCatalogoConverter.to(item);
            listaItensCatalogo.add(itemObject);
        }

        for (ItemSimplificado item : itensSimplificados) {
            ItemCatalogo itemObject = itemCatalogoConverter.to(item);
            listaItensCatalogo.add(itemObject);
        }
        return listaItensCatalogo;
    }

    private ListaPaginada<ItemCatalogo> constroiListaPaginadaCatalogo(ListaPaginada<ItemRegular> itensRegulares, ListaPaginada<ItemSimplificado> itensSimplificados, List<ItemCatalogo> listaOrdenada, ItemCatalogo.Filtro filtro) {
        Long totalElements = itensRegulares.getTotalElements() + itensSimplificados.getTotalElements();

        return ListaPaginada.<ItemCatalogo>
            builder()
            .items(listaOrdenada)
            .totalPages(calculaTotalPages(totalElements, filtro.getSize()))
            .totalElements(totalElements)
            .build();
    }

    private Long calculaTotalPages(Long totalElements, Long qntItensPorPagina) {
        return (long) Math.ceil(totalElements / qntItensPorPagina);
    }

    private BooleanExpression compararSemAcentuacao(StringExpression path, String value) {
        StringExpression expr = Expressions.stringTemplate("upper(translate({0}, 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç', 'AAAAAAAAEEEEIIOOOOOOUUUUCC'))", path.trim());
        return expr.containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(value.trim()));
    }
}
