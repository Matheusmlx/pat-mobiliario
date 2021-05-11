package br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemCatalogo;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemCatalogoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.converter.BuscarItensCatalogoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.converter.BuscarItensCatalogoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.exception.FiltroIncompletoException;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class BuscarItensCatalogoUseCaseImpl implements BuscarItensCatalogoUseCase{

    private ItemCatalogoDataProvider itemCatalogoDataProvider;

    private BuscarItensCatalogoFiltroConverter buscarItensCatalogoFiltroConverter;

    private BuscarItensCatalogoOutputDataConverter outputDataConverter;

    @Override
    public BuscarItensCatalogoOutputData executar(BuscarItensCatalogoInputData inputdata) {
        validarDadosEntrada(inputdata);

        ItemCatalogo.Filtro filtro = criarFiltro(inputdata);

        ListaPaginada<ItemCatalogo> entidadesEncontrada = buscar(filtro);

        return outputDataConverter.to(entidadesEncontrada);
    }

    private void validarDadosEntrada(BuscarItensCatalogoInputData entrada){
        Validator.of(entrada)
            .validate(BuscarItensCatalogoInputData::getSize, size -> Objects.nonNull(size) && size > 0, new FiltroIncompletoException("Ausência da quantidade de registros por página."))
            .validate(BuscarItensCatalogoInputData::getPage, Objects::nonNull, new FiltroIncompletoException("Ausência do número da página."))
            .get();
    }

    private ItemCatalogo.Filtro criarFiltro(BuscarItensCatalogoInputData inputData){
        return buscarItensCatalogoFiltroConverter.to(inputData);
    }

    private ListaPaginada<ItemCatalogo> buscar(ItemCatalogo.Filtro filtro) {
        return itemCatalogoDataProvider.buscarPorFiltro(filtro);
    }
}
