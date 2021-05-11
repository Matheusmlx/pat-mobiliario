package br.com.azi.patrimoniomobiliario.entrypoint.depreciacao;

import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.listagem.BuscarDepreciacoesInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.listagem.BuscarDepreciacoesOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.listagem.BuscarDepreciacoesUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonio/{id}/depreciacoes")
public class BuscarDepreciacoesController {

    @Autowired
    private BuscarDepreciacoesUseCase useCase;

    @GetMapping
    public BuscarDepreciacoesOutputData buscar(@PathVariable Long id) {
        return useCase.executar(new BuscarDepreciacoesInputData(id));
    }
}
