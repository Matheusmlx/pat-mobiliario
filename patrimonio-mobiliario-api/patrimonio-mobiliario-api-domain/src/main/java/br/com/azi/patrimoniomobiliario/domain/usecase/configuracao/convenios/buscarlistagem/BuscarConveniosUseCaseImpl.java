package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem;

import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConvenioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.converter.BuscarConveniosFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.converter.BuscarConveniosOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.exception.FiltroIncompletoException;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class BuscarConveniosUseCaseImpl implements BuscarConveniosUseCase{

    private ConvenioDataProvider convenioDataProvider;

    private BuscarConveniosOutputDataConverter conveniosOutputDataConverter;

    private BuscarConveniosFiltroConverter conveniosFiltroConverter;

    @Override
    public BuscarConveniosOutputData executar(BuscarConveniosInputData inputData) {
        validarDadosEntrada(inputData);
        Convenio.Filtro filtro = criarFiltro(inputData);
        ListaPaginada<Convenio> entidadesEncontradas = buscar(filtro);

        return conveniosOutputDataConverter.to(entidadesEncontradas);
    }

    private void validarDadosEntrada(BuscarConveniosInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarConveniosInputData::getSize, size -> Objects.nonNull(size) && size > 0, new FiltroIncompletoException("Ausência da quantidade de registros por página."))
            .validate(BuscarConveniosInputData::getPage, Objects::nonNull, new FiltroIncompletoException("Ausência do número da página."))
            .get();
    }

    private Convenio.Filtro criarFiltro(BuscarConveniosInputData inputData) {
        return this.conveniosFiltroConverter.to(inputData);
    }

    private ListaPaginada<Convenio> buscar(Convenio.Filtro filtro) {
        return convenioDataProvider.buscarPorFiltro(filtro);
    }
}
