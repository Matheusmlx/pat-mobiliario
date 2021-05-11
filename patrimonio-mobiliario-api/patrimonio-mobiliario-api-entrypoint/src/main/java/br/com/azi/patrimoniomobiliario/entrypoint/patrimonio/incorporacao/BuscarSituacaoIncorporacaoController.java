package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarsituacao.BuscarSituacaoIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarsituacao.BuscarSituacaoIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarsituacao.BuscarSituacaoIncorporacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/{incorporacaoId}/situacao")
public class BuscarSituacaoIncorporacaoController {

    @Autowired
    private BuscarSituacaoIncorporacaoUseCase useCase;

    @GetMapping
    public BuscarSituacaoIncorporacaoOutputData buscarSituacaoIncorporacao(BuscarSituacaoIncorporacaoInputData inputData) {
        return useCase.executar(inputData);
    }
}
