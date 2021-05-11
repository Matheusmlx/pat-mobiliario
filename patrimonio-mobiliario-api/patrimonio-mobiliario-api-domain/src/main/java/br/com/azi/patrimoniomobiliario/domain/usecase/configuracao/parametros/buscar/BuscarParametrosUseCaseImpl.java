package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.parametros.buscar;

import br.com.azi.patrimoniomobiliario.domain.entity.ParametrosSistema;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.parametros.buscar.converter.BuscarParametrosOutputDataConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BuscarParametrosUseCaseImpl implements BuscarParametrosUsecase {

    private final ParametrosSistema parametrosSistema;

    private final BuscarParametrosOutputDataConverter converter;

    @Override
    public BuscarParametrosOutputData executar() {
        return converter.to(parametrosSistema);
    }
}
