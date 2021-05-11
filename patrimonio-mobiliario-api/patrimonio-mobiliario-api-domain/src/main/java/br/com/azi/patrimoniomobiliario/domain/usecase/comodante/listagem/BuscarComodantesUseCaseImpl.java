package br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem;

import br.com.azi.patrimoniomobiliario.domain.entity.Comodante;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ComodanteDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.converter.BuscarComodantesFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.converter.BuscarComodantesOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class BuscarComodantesUseCaseImpl implements BuscarComodantesUseCase {

    private final ComodanteDataProvider comodanteDataProvider;

    private final BuscarComodantesOutputDataConverter outputDataConverter;

    private final BuscarComodantesFiltroConverter filtroConverter;

    @Override
    public BuscarComodantesOutputData executar(BuscarComodantesInputData inputData) {
        validarDadosEntrada(inputData);

        Comodante.Filtro filtro = criarFiltro(inputData);
        final ListaPaginada<Comodante> comodantes = buscar(filtro);

        return outputDataConverter.to(comodantes);
    }

    private Comodante.Filtro criarFiltro(BuscarComodantesInputData inputData) {
        return filtroConverter.to(inputData);
    }

    private void validarDadosEntrada(BuscarComodantesInputData entrada) {
        Validator.of(entrada)
            .validate(BuscarComodantesInputData::getSize, size -> Objects.nonNull(size) && size > 0, "Ausência da quantidade de registros por página.")
            .validate(BuscarComodantesInputData::getPage, Objects::nonNull, "Ausência do número da página.")
            .get();
    }

    private ListaPaginada<Comodante> buscar(Comodante.Filtro filtro) {
        return comodanteDataProvider.buscarPorFiltro(filtro);
    }

}
