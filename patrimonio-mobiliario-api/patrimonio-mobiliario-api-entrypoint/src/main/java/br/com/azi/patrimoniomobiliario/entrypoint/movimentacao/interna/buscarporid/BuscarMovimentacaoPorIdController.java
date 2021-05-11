package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.buscarporid;


import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarporid.BuscarMovimentacaoPorIdInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarporid.BuscarMovimentacaoPorIdOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarporid.BuscarMovimentacaoPorIdUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas/movimentacao/{id}")
public class BuscarMovimentacaoPorIdController {

    @Autowired
    private BuscarMovimentacaoPorIdUseCase useCase;

    @GetMapping
    public BuscarMovimentacaoPorIdOutputData buscar(BuscarMovimentacaoPorIdInputData inputData) {
        return useCase.executar(inputData);
    }
}
