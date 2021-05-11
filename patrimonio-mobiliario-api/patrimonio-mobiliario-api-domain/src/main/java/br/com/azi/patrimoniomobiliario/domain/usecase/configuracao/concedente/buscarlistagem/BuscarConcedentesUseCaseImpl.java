package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarlistagem;

import br.com.azi.patrimoniomobiliario.domain.entity.Concedente;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConcedenteDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarlistagem.converter.BuscarConcedentesFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarlistagem.converter.BuscarConcedentesOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarlistagem.exception.FiltroIncompletoException;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class BuscarConcedentesUseCaseImpl implements BuscarConcedentesUseCase {

    private ConcedenteDataProvider concedenteDataProvider;

    private BuscarConcedentesOutputDataConverter concedentesOutputDataConverter;

    private BuscarConcedentesFiltroConverter concedentesFiltroConverter;

    @Override
    public BuscarConcedentesOutputData executar(BuscarConcedentesInputData inputData) {
        validarDadosEntrada(inputData);
        Concedente.Filtro filtro = criarFiltro(inputData);
        ListaPaginada<Concedente> entidadesEncontradas = buscar(filtro);

        return concedentesOutputDataConverter.to(entidadesEncontradas);
    }

    private void validarDadosEntrada(BuscarConcedentesInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarConcedentesInputData::getSize, size -> Objects.nonNull(size) && size > 0, new FiltroIncompletoException("Ausência da quantidade de registros por página."))
            .validate(BuscarConcedentesInputData::getPage, Objects::nonNull, new FiltroIncompletoException("Ausência do número da página."))
            .get();
    }

    private Concedente.Filtro criarFiltro(BuscarConcedentesInputData inputData) {
        return this.concedentesFiltroConverter.to(inputData);
    }

    private ListaPaginada<Concedente> buscar(Concedente.Filtro filtro) {
        return concedenteDataProvider.buscarPorFiltro(filtro);
    }
}
