package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.completa.RemoverMovimentacaoInternaInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.completa.RemoverMovimentacaoInternaUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas/{movimentacaoId}/completa")
public class RemoverMovimentacaoInternaController {

    @Autowired
    private RemoverMovimentacaoInternaUseCase useCase;

    @DeleteMapping
    public void removerMovimentacao(RemoverMovimentacaoInternaInputData inputData) {
        useCase.executar(inputData);
    }

}
