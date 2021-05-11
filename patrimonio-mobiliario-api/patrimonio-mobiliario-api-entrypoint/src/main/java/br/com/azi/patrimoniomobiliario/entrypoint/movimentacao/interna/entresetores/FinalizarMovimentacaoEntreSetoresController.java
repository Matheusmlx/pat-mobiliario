package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.entresetores;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.FinalizarMovimentacaoEntreSetoresInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.FinalizarMovimentacaoEntreSetoresOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.FinalizarMovimentacaoEntreSetoresUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequestMapping("/movimentacoes/internas/definitiva/{id}/finalizar")
public class FinalizarMovimentacaoEntreSetoresController {

    @Autowired
    private FinalizarMovimentacaoEntreSetoresUseCase useCase;

    @PatchMapping
    public FinalizarMovimentacaoEntreSetoresOutputData executar(@PathVariable Long id) {
        return useCase.executar(new FinalizarMovimentacaoEntreSetoresInputData(id));
    }
}
