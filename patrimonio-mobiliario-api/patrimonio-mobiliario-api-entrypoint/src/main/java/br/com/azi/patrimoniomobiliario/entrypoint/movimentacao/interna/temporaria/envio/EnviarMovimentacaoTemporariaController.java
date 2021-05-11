package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.temporaria.envio;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.EnviarMovimentacaoTemporariaInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.EnviarMovimentacaoTemporariaOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.EnviarMovimentacaoTemporariaUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas/temporaria/{id}/enviar")
public class EnviarMovimentacaoTemporariaController {

    @Autowired
    private EnviarMovimentacaoTemporariaUseCase useCase;

    @PatchMapping
    public EnviarMovimentacaoTemporariaOutputData enviarMovimentacaoTemporaria(@PathVariable Long id) {
        return useCase.executar(new EnviarMovimentacaoTemporariaInputData(id));
    }

}
