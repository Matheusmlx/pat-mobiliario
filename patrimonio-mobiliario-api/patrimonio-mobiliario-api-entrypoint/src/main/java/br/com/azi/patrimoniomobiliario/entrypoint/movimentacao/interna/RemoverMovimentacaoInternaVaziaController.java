package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.vazia.RemoverMovimentacaoInternaVaziaInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.vazia.RemoverMovimentacaoInternaVaziaUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas/{movimentacaoId}/vazia")
public class RemoverMovimentacaoInternaVaziaController {

    @Autowired
    private RemoverMovimentacaoInternaVaziaUseCase useCase;

    @DeleteMapping
    public void remover(RemoverMovimentacaoInternaVaziaInputData inputData) {
        useCase.executar(inputData);
    }
}
