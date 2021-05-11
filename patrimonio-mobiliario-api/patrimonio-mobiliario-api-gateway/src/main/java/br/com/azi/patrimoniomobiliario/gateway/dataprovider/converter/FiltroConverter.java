package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.FiltroBase;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FiltroConverter {

    private FiltroConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static Pageable extrairPaginacao(FiltroBase filtro) {
        return PageRequest.of(
            filtro.getPage().intValue(), filtro.getSize().intValue(),
            new Sort(Sort.Direction.valueOf(filtro.getDirection()), Objects.nonNull(filtro.getSort()) ? filtro.getSort() : "id")
        );
    }

    public static Pageable extrairPaginacao(FiltroBase filtro, String sortFirst, String sortSecond) {
        List<String> sort = new ArrayList<>();
        sort.add(sortFirst);
        sort.add(sortSecond);

        return PageRequest.of(
            filtro.getPage().intValue(), filtro.getSize().intValue(),
            new Sort(Sort.Direction.valueOf(filtro.getDirection()), sort)
        );
    }

    public static Pageable extrairPaginacaoComSegundaOrdenacao(FiltroBase filtro, String sortSecond) {
        return PageRequest.of(
            filtro.getPage().intValue(), filtro.getSize().intValue(),
            new Sort(Sort.Direction.valueOf("ASC"), "situacao").and( Sort.by(new Sort.Order(Sort.Direction.valueOf(filtro.getDirection()),sortSecond, Sort.NullHandling.NULLS_LAST)))
        );
    }

    public static Pageable extrairPaginacaoCadastroEdicao(FiltroBase filtro, String sortSecond) {
        return PageRequest.of(
            filtro.getPage().intValue(), filtro.getSize().intValue(),
            new Sort(Sort.Direction.valueOf(filtro.getDirection()), "situacao").and( Sort.by(new Sort.Order(Sort.Direction.valueOf("DESC"),sortSecond, Sort.NullHandling.NULLS_LAST)))
        );
    }
}
