package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.item;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.visualizar.VisualizarItemIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.visualizar.VisualizarItemIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.visualizar.VisualizarItemIncorporacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/incorporacao/item/{id}/visualizar")
public class VisualizarItemIncorporacaoController {

    @Autowired
    private VisualizarItemIncorporacaoUseCase useCase;

    @GetMapping
    public VisualizarItemIncorporacaoOutputData executar(@PathVariable Long id){
        VisualizarItemIncorporacaoInputData inputData = new VisualizarItemIncorporacaoInputData(id);
        return useCase.executar(inputData);
    }
}
