package br.com.azi.patrimoniomobiliario.entrypoint.comodante;

import br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.BuscarComodantesInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.BuscarComodantesOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.BuscarComodantesUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comodantes")
public class BuscarComodantesController {

    @Autowired
    private BuscarComodantesUseCase useCase;

    @GetMapping
    public BuscarComodantesOutputData buscarComodantes(BuscarComodantesInputData inputData) {
        return useCase.executar(inputData);
    }
}
