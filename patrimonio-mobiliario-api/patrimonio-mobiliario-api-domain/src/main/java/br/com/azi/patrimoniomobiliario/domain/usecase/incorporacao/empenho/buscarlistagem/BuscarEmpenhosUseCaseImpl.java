package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.buscarlistagem;

import br.com.azi.patrimoniomobiliario.domain.entity.Empenho;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EmpenhoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.buscarlistagem.converter.BuscarEmpenhosFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.buscarlistagem.converter.BuscarEmpenhosOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class BuscarEmpenhosUseCaseImpl implements BuscarEmpenhosUseCase {

    private EmpenhoDataProvider empenhoDataProvider;

    private BuscarEmpenhosOutputDataConverter outputDataConverter;

    private BuscarEmpenhosFiltroConverter patrimonioBuscarEmpenhosFiltroConverter;

    @Override
    public BuscarEmpenhosOutputData executar(BuscarEmpenhosInputData inputData) {
        validarDadosEntrada(inputData);
        Empenho.Filtro filtro = criarFiltro(inputData);
        List<Empenho> entidadesEncontradas = buscar(filtro);

        return outputDataConverter.to(entidadesEncontradas);
    }

    private void validarDadosEntrada(BuscarEmpenhosInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarEmpenhosInputData::getIncorporacaoId, Objects::nonNull, "Incorporação é nula")
            .get();
    }

    private Empenho.Filtro criarFiltro(BuscarEmpenhosInputData inputData) {
        return patrimonioBuscarEmpenhosFiltroConverter.to(inputData);
    }

    private List<Empenho> buscar(Empenho.Filtro filtro) {
        return empenhoDataProvider.buscarPorIncorporacao(filtro);
    }
}
