package br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemCatalogo;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemCatalogoInterface;
import br.com.azi.patrimoniomobiliario.domain.entity.NaturezaDespesa;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemCatalogoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NaturezaDespesaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.conveter.BuscarNaturezasDespesaOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.exception.ItemCatalogoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.exception.VinculoMaterialServicoException;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BuscarNaturezasDespesaUseCaseImpl implements BuscarNaturezasDespesaUseCase {

    private NaturezaDespesaDataProvider naturezaDespesaDataProvider;

    private ItemCatalogoDataProvider itemCatalogoDataProvider;

    private BuscarNaturezasDespesaOutputDataConverter outputDataConverter;

    @Override
    public BuscarNaturezasDespesaOutputData executar(BuscarNaturezasDespesaInputData inputData) {
        validarDadosEntrada(inputData);

        ItemCatalogoInterface item = buscarItemCatalogo(inputData);
        ItemCatalogo itemCatalogo = converterItemCatalogo(item);

        verificaVinculoItemCatalogoMaterialServico(itemCatalogo);

        List<NaturezaDespesa> entidadesEncontradas = buscar(itemCatalogo);

        return outputDataConverter.to(entidadesEncontradas);
    }

    private void validarDadosEntrada(BuscarNaturezasDespesaInputData inputData){
        Validator.of(inputData)
            .validate(BuscarNaturezasDespesaInputData::getItemCatalogoCodigo, Objects::nonNull, "O código do catalogo é nulo")
            .get();
    }

    private ItemCatalogoInterface buscarItemCatalogo(BuscarNaturezasDespesaInputData inputData) {
        Optional<Object> itemCatalogo = itemCatalogoDataProvider.buscarPorCodigo(inputData.getItemCatalogoCodigo());
        return itemCatalogo.map(o -> (ItemCatalogoInterface) o).orElseThrow(ItemCatalogoException::new);
    }

    private void verificaVinculoItemCatalogoMaterialServico(ItemCatalogo itemCatalogo) {
        if (Objects.isNull(itemCatalogo.getMaterialServicoId())) {
            throw new VinculoMaterialServicoException();
        }
    }

    private List<NaturezaDespesa> buscar(ItemCatalogo itemCatalogo){
        return naturezaDespesaDataProvider.buscarPorFiltro(itemCatalogo.getMaterialServicoId());
    }

    private ItemCatalogo converterItemCatalogo(ItemCatalogoInterface itemCatalogo) {
        ItemCatalogoConverter itemCatalogoConverter = new ItemCatalogoConverter();
        return  itemCatalogoConverter.to(itemCatalogo);
    }

    private static class ItemCatalogoConverter extends GenericConverter<ItemCatalogoInterface, ItemCatalogo> {
    }

}
