package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.concedente;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarporid.BuscarConcedentePorIdInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarporid.BuscarConcedentePorIdOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarporid.BuscarConcedentePorIdUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/concedentes/{id}")
public class BuscarConcedentePorIdController {

    @Autowired
    private BuscarConcedentePorIdUseCase usecase;

    @GetMapping
    public BuscarConcedentePorIdOutputData buscar(BuscarConcedentePorIdInputData inputData) {
        return usecase.executar(inputData);
    }
}
