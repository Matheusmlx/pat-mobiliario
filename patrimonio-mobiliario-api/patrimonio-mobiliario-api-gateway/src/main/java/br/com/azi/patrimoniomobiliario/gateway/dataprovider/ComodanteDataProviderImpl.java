package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.Comodante;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ComodanteDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.ComodanteConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.FiltroConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ComodanteEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QComodanteEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.ComodanteRepository;
import br.com.azi.patrimoniomobiliario.utils.AcetuacaoUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ComodanteDataProviderImpl implements ComodanteDataProvider {

    @Autowired
    private ComodanteRepository repository;

    @Autowired
    private ComodanteConverter converter;

    @Override
    public ListaPaginada<Comodante> buscarPorFiltro(Comodante.Filtro filtro) {
        QComodanteEntity qComodanteEntity = QComodanteEntity.comodanteEntity;
        BooleanExpression expression = qComodanteEntity.id.isNotNull();

        filtro.setDirection("ASC");
        filtro.setSort("nome");

        if(!StringUtils.isEmpty(filtro.getConteudo())){
            BooleanExpression conteudoExp = compararSemAcentuacao(qComodanteEntity.nome, filtro.getConteudo());

            expression = expression.and(conteudoExp);
        }

        Page<ComodanteEntity> entidadesEncontradas = repository.findAll(expression, FiltroConverter.extrairPaginacao(filtro));

        List<Comodante> entidades = entidadesEncontradas
            .getContent()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());

        return ListaPaginada.<Comodante>builder()
            .items(entidades)
            .totalElements(entidadesEncontradas.getTotalElements())
            .totalPages((long) entidadesEncontradas.getTotalPages())
            .build();
    }

    private BooleanExpression compararSemAcentuacao(StringExpression path, String value) {
        StringExpression expr = Expressions.stringTemplate("upper(translate({0}, 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç', 'AAAAAAAAEEEEIIOOOOOOUUUUCC'))", path.trim());
        return expr.containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(value.trim()));
    }
}
