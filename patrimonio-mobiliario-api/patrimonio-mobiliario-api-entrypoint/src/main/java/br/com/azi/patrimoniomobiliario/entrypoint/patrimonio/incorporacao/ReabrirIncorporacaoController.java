package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.reabrir.ReabrirIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.reabrir.ReabrirIncorporacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/reabrir/{incorporacaoId}")
public class ReabrirIncorporacaoController {

    @Autowired
    private ReabrirIncorporacaoUseCase useCase;

    @PatchMapping
    public void reabrirIncorporacao(ReabrirIncorporacaoInputData inputData) {
        useCase.executar(inputData);
    }

}
