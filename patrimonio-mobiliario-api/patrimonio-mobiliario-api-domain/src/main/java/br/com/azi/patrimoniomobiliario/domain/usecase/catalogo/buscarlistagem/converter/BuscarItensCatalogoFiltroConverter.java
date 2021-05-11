package br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemCatalogo;
import br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.BuscarItensCatalogoInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

public class BuscarItensCatalogoFiltroConverter extends GenericConverter<BuscarItensCatalogoInputData, ItemCatalogo.Filtro> {
    @Override
    public ItemCatalogo.Filtro to(BuscarItensCatalogoInputData source) {
        ItemCatalogo.Filtro target = super.to(source);
        target.setPage(target.getPage() - 1);
        return target;
    }
}
