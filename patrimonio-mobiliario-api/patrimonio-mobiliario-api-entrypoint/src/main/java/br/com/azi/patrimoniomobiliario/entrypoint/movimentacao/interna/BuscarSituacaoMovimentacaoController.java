package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarsituacao.BuscarSituacaoMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarsituacao.BuscarSituacaoMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarsituacao.BuscarSituacaoMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas/{movimentacaoId}/situacao")
public class BuscarSituacaoMovimentacaoController {

    @Autowired
    private BuscarSituacaoMovimentacaoUseCase useCase;

    @GetMapping
    public BuscarSituacaoMovimentacaoOutputData buscarSituacaoMovimentacao(BuscarSituacaoMovimentacaoInputData inputData) {
        return useCase.executar(inputData);
    }
}
