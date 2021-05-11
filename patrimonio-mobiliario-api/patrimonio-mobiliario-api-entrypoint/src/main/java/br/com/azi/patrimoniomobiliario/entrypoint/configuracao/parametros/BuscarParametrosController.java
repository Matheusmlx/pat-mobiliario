package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.parametros;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.parametros.buscar.BuscarParametrosOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.parametros.buscar.BuscarParametrosUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parametros-sistema")
public class BuscarParametrosController {

    @Autowired
    private BuscarParametrosUsecase usecase;

    @GetMapping()
    public BuscarParametrosOutputData buscarParametrosSistema() throws NoSuchMethodException {
        return usecase.executar();
    }
}
