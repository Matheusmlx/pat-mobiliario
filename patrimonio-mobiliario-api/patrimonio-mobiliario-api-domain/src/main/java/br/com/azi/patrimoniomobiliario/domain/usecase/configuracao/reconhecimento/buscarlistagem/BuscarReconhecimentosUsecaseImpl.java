package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReconhecimentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem.converter.BuscarReconhecimentosFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem.converter.BuscarReconhecimentosOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class BuscarReconhecimentosUsecaseImpl implements BuscarReconhecimentosUsecase {

    private ReconhecimentoDataProvider reconhecimentoDataProvider;

    private BuscarReconhecimentosOutputDataConverter reconhecimentosOutputDataConverter;

    private BuscarReconhecimentosFiltroConverter reconhecimentosFiltroConverter;

    @Override
    public BuscarReconhecimentosOutputData executar(BuscarReconhecimentosInputData inputData) {
        validarDadosEntrada(inputData);
        Reconhecimento.Filtro filtro = criarFiltro(inputData);
        ListaPaginada<Reconhecimento> entidadesEncontradas = buscar(filtro);

        return reconhecimentosOutputDataConverter.to(entidadesEncontradas);
    }

    private void validarDadosEntrada(BuscarReconhecimentosInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarReconhecimentosInputData::getSize, size -> Objects.nonNull(size) && size > 0, "Ausência da quantidade de registro por página.")
            .validate(BuscarReconhecimentosInputData::getPage, Objects::nonNull, "Ausência do número da página.")
            .get();
    }

    private Reconhecimento.Filtro criarFiltro(BuscarReconhecimentosInputData inputData) {
        return this.reconhecimentosFiltroConverter.to(inputData);
    }

    private ListaPaginada<Reconhecimento> buscar(Reconhecimento.Filtro filtro) {
        return reconhecimentoDataProvider.buscarPorFiltro(filtro);
    }
}
