package br.com.azi.patrimoniomobiliario.entrypoint.estruturaorganizacional;

import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarfundos.BuscarFundosInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarfundos.BuscarFundosOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarfundos.BuscarFundosUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/unidades-organizacionais/{idOrgao}/fundos")
public class BuscarFundosController {

    @Autowired
    private BuscarFundosUseCase usecase;

    @GetMapping
    public BuscarFundosOutputData executar(@PathVariable("idOrgao") Long idOrgao) {
        return usecase.executar(new BuscarFundosInputData(idOrgao));
    }
}
