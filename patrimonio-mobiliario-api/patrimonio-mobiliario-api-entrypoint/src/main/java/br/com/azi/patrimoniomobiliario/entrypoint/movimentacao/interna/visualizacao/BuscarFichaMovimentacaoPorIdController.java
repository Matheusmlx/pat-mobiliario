package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.visualizacao;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.buscarporid.BuscarFichaMovimentacaoPorIdInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.buscarporid.BuscarFichaMovimentacaoPorIdOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.buscarporid.BuscarFichaMovimentacaoPorIdUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas/visualizacao/{movimentacaoId}")
public class BuscarFichaMovimentacaoPorIdController {

    @Autowired
    private BuscarFichaMovimentacaoPorIdUseCase useCase;

    @GetMapping
    public BuscarFichaMovimentacaoPorIdOutputData buscarMovimentacoesPorPatrimonio(@PathVariable Long movimentacaoId) {
        return useCase.executar(new BuscarFichaMovimentacaoPorIdInputData(movimentacaoId));
    }
}
