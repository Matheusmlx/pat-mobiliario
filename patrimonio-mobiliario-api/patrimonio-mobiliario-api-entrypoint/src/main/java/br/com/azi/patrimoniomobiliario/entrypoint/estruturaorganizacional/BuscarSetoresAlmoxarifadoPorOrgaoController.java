package br.com.azi.patrimoniomobiliario.entrypoint.estruturaorganizacional;

import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresalmoxarifado.BuscarSetoresAlmoxarifadoPorOrgaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresalmoxarifado.BuscarSetoresAlmoxarifadoPorOrgaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresalmoxarifado.BuscarSetoresAlmoxarifadoPorOrgaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/unidades-organizacionais/{idOrgao}/setores-almoxarifado")
public class BuscarSetoresAlmoxarifadoPorOrgaoController {

    @Autowired
    private BuscarSetoresAlmoxarifadoPorOrgaoUseCase usecase;

    @GetMapping
    public BuscarSetoresAlmoxarifadoPorOrgaoOutputData executar(BuscarSetoresAlmoxarifadoPorOrgaoInputData inputData) {
        return usecase.executar(inputData);
    }
}
