package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.entreestoques;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.FinalizarMovimentacaoEntreEstoquesInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.FinalizarMovimentacaoEntreEstoquesOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.FinalizarMovimentacaoEntreEstoquesUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas/entre-estoques/{id}/finalizar")
public class FinalizarMovimentacaoEntreEstoquesController {

    @Autowired
    private FinalizarMovimentacaoEntreEstoquesUseCase useCase;

    @PatchMapping
    public FinalizarMovimentacaoEntreEstoquesOutputData finalizarMovimentacao(FinalizarMovimentacaoEntreEstoquesInputData inputData) {
        return useCase.executar(inputData);
    }

}
