package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.concedente;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarlistagem.BuscarConcedentesInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarlistagem.BuscarConcedentesOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarlistagem.BuscarConcedentesUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concedentes")
public class BuscarConcedentesController {

    @Autowired
    private BuscarConcedentesUseCase useCase;

    @GetMapping
    public BuscarConcedentesOutputData buscarTodos(BuscarConcedentesInputData inputData) {
        return useCase.executar(inputData);
    }
}
