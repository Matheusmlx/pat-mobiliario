package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.convenio;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarporid.BuscarConvenioPorIdInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarporid.BuscarConvenioPorIdOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarporid.BuscarConvenioPorIdUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/convenios/{id}")
public class BuscarConvenioPorIdController {

    @Autowired
    private BuscarConvenioPorIdUseCase usecase;

    @GetMapping
    public BuscarConvenioPorIdOutputData buscar(BuscarConvenioPorIdInputData inputData) {
        return usecase.executar(inputData);
    }
}
