package br.com.azi.patrimoniomobiliario.entrypoint.estruturaorganizacional;


import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetores.BuscarSetoresOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetores.BuscarSetoresUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/unidades-organizacionais/setores")
public class BuscarSetoresController {

    @Autowired
    private BuscarSetoresUseCase usecase;

    @GetMapping
    public BuscarSetoresOutputData executar() {
        return usecase.executar();
    }
}
