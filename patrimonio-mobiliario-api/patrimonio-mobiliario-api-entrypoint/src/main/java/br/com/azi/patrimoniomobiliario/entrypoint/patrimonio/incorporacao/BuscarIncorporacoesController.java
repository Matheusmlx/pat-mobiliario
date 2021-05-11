package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.listagem.BuscarIncorporacoesInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.listagem.BuscarIncorporacoesOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.listagem.BuscarIncorporacoesUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao")
public class BuscarIncorporacoesController {

    @Autowired
    private BuscarIncorporacoesUseCase useCase;

    @GetMapping
    public BuscarIncorporacoesOutputData executar(BuscarIncorporacoesInputData inputData){
        return useCase.executar(inputData);
    }
}
