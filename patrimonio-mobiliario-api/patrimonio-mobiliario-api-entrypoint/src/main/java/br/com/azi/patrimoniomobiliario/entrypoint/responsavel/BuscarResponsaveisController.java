package br.com.azi.patrimoniomobiliario.entrypoint.responsavel;

import br.com.azi.patrimoniomobiliario.domain.usecase.responsavel.listagem.BuscarResponsaveisOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.responsavel.listagem.BuscarResponsaveisUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/responsavel")
@RestController
public class BuscarResponsaveisController {

    @Autowired
    private BuscarResponsaveisUseCase useCase;

    @GetMapping
    public BuscarResponsaveisOutputData buscarResponsaveis() {
        return useCase.executar();
    }

}
