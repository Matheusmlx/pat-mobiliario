package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.visualizar.VisualizarIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.visualizar.VisualizarIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.visualizar.VisualizarIncorporacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/{id}/visualizar")
public class VisualizarIncorporacaoController {

    @Autowired
    private VisualizarIncorporacaoUseCase useCase;

    @GetMapping
    public VisualizarIncorporacaoOutputData buscar(VisualizarIncorporacaoInputData inputData) {
        return useCase.executar(inputData);
    }
}
