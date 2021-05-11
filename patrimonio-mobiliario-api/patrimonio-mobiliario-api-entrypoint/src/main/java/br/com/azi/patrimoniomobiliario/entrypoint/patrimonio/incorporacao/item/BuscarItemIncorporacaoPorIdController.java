package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.item;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid.BuscarItemIncorporacaoPorIdInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid.BuscarItemIncorporacaoPorIdOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid.BuscarItemIncorporacaoPorIdUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/{idIncorporacao}/item/{idItem}")
public class BuscarItemIncorporacaoPorIdController {

    @Autowired
    private BuscarItemIncorporacaoPorIdUseCase useCase;

    @GetMapping
    public BuscarItemIncorporacaoPorIdOutputData executar(@PathVariable Long idItem, @PathVariable Long idIncorporacao){
        BuscarItemIncorporacaoPorIdInputData inputData = new BuscarItemIncorporacaoPorIdInputData();
        inputData.setIdIncorporacao(idIncorporacao);
        inputData.setIdItem(idItem);
        return useCase.executar(inputData);
    }
}
