package br.com.azi.patrimoniomobiliario.entrypoint.estruturaorganizacional;

import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresnaoalmoxarifado.BuscarSetoresNaoAlmoxarifadoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresnaoalmoxarifado.BuscarSetoresNaoAlmoxarifadoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresnaoalmoxarifado.BuscarSetoresNaoAlmoxarifadoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/unidades-organizacionais/{orgaoId}/setores-nao-almoxarifado")
public class BuscarSetoresNaoAlmoxarifadoPorOrgaoController {

    @Autowired
    private BuscarSetoresNaoAlmoxarifadoUseCase useCase;

    @GetMapping
    public BuscarSetoresNaoAlmoxarifadoOutputData executar(@PathVariable Long orgaoId) {
        return useCase.executar(new BuscarSetoresNaoAlmoxarifadoInputData(orgaoId));
    }
}
