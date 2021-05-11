package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.rotulosPersonalizados;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.rotulospersonalizados.BuscarRotulosPersonalizadosOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.rotulospersonalizados.BuscarRotulosPersonalizadosUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rotulosPersonalizados")
public class BuscarRotulosPersonalizadosController {

    @Autowired
    private BuscarRotulosPersonalizadosUseCase useCase;

    @GetMapping
    public BuscarRotulosPersonalizadosOutputData buscarCamposPersonalizadosOutputData() throws NoSuchMethodException {
        return useCase.executar();
    }
}
