package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.item;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.cadastrar.CadastrarItemIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.cadastrar.CadastrarItemIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.cadastrar.CadastrarItemIncorporacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
@RequestMapping("/patrimonios/incorporacao/{idIncorporacao}/item")
public class CadastrarItemIncorporacaoController {

    @Autowired
    private CadastrarItemIncorporacaoUseCase useCase;

    @PostMapping
    public CadastrarItemIncorporacaoOutputData executar(@PathVariable Long idIncorporacao, @RequestBody CadastrarItemIncorporacaoInputData inputData){
        inputData.setIncorporacaoId(idIncorporacao);
        return useCase.executar(inputData);
    }

}
