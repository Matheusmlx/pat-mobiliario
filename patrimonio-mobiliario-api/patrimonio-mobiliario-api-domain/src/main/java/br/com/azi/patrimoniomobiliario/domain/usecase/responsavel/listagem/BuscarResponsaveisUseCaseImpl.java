package br.com.azi.patrimoniomobiliario.domain.usecase.responsavel.listagem;

import br.com.azi.patrimoniomobiliario.domain.entity.Responsavel;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ResponsavelDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.responsavel.listagem.converter.BuscarResponsaveisOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class BuscarResponsaveisUseCaseImpl implements BuscarResponsaveisUseCase {

    private final ResponsavelDataProvider responsavelDataProvider;

    private final BuscarResponsaveisOutputDataConverter outputDataConverter;

    @Override
    public BuscarResponsaveisOutputData executar() {
        List<Responsavel> responsaveis = buscar();

        return outputDataConverter.to(responsaveis);
    }

    private List<Responsavel> buscar() {
        return responsavelDataProvider.buscarResponsaveis();
    }

}
