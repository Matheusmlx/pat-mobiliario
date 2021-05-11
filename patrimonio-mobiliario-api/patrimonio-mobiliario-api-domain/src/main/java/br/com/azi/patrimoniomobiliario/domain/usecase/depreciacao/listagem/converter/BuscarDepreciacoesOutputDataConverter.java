package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Depreciacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.listagem.BuscarDepreciacoesOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarDepreciacoesOutputDataConverter {

    public BuscarDepreciacoesOutputData to(List<Depreciacao> source) {
        List<BuscarDepreciacoesOutputData.Depreciacao> itens = source.stream().map(this::converterItem).collect(Collectors.toList());
        BuscarDepreciacoesOutputData target = new BuscarDepreciacoesOutputData();
        target.setItems(itens);

        return target;
    }

    private BuscarDepreciacoesOutputData.Depreciacao converterItem(Depreciacao source) {
        BuscarDepreciacoesOutputData.Depreciacao item = new BuscarDepreciacoesOutputDataConverter.ItemOutputDataConverter().to(source);

        if (Objects.nonNull(source.getOrgao())) {
            item.setOrgaoSigla(source.getOrgao().getSigla());
        }

        if (Objects.nonNull(source.getSetor())) {
            item.setSetorSigla(source.getSetor().getSigla());
        }

        return item;
    }

    private static class ItemOutputDataConverter extends GenericConverter<Depreciacao, BuscarDepreciacoesOutputData.Depreciacao> {
    }
}
