package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid.BuscarIncorporacaoPorIdInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid.BuscarIncorporacaoPorIdOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid.BuscarIncorporacaoPorIdUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/{id}")
public class BuscarIncorporacaoPorIdController {

    @Autowired
    private BuscarIncorporacaoPorIdUseCase useCase;

    @GetMapping
    public BuscarIncorporacaoPorIdOutputData buscar(BuscarIncorporacaoPorIdInputData inputData) {
        return useCase.executar(inputData);
    }
}
