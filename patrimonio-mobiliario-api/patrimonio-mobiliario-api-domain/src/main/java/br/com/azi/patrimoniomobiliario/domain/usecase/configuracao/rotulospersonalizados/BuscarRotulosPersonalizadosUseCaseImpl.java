package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.rotulospersonalizados;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.rotulospersonalizados.converter.RotulosPersonalizadosConverter;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class BuscarRotulosPersonalizadosUseCaseImpl implements BuscarRotulosPersonalizadosUseCase {

    private final Map<String, Object> rotulosPersonalizados;

    private final RotulosPersonalizadosConverter converter;

    @Override
    public BuscarRotulosPersonalizadosOutputData executar() {
        return converter.to(rotulosPersonalizados);
    }

}
