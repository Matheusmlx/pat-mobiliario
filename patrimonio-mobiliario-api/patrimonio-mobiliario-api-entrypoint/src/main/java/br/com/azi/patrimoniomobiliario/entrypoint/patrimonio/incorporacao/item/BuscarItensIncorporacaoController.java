package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.item;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.BuscarItensIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.BuscarItensIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.BuscarItensIncorporacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/itens/{incorporacaoId}")
public class BuscarItensIncorporacaoController {

    @Autowired
    private BuscarItensIncorporacaoUseCase useCase;

    @GetMapping
    public BuscarItensIncorporacaoOutputData executar(@PathVariable Long incorporacaoId,BuscarItensIncorporacaoInputData inputData){
        inputData.setIncorporacaoId(incorporacaoId);
        return useCase.executar(inputData);
    }
}
