package br.com.azi.patrimoniomobiliario.entrypoint.estruturaorganizacional;

import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgaoporfiltro.BuscarOrgaoPorFiltroInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgaoporfiltro.BuscarOrgaoPorFiltroOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgaoporfiltro.BuscarOrgaoPorFiltroUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/unidades-organizacionais/orgaos")
@RequiredArgsConstructor
public class BuscarOrgaosPorFiltroController {

    private final BuscarOrgaoPorFiltroUseCase useCase;

    @GetMapping
    public BuscarOrgaoPorFiltroOutputData buscarOrgaosPaginado(BuscarOrgaoPorFiltroInputData inputData) {
        return useCase.executar(inputData);
    }

}
