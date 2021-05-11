package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.convenio;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.BuscarConveniosInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.BuscarConveniosOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.BuscarConveniosUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/convenios")
public class BuscarConveniosController {

    @Autowired
    private BuscarConveniosUseCase useCase;

    @GetMapping
    public BuscarConveniosOutputData buscarTodos(BuscarConveniosInputData inputData) {
        return useCase.executar(inputData);
    }
}
