package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.item;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.remocao.RemoverItemMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.remocao.RemoverItemMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas/{movimentacaoId}/itens/{patrimonioId}")
public class RemoverItemMovimentacaoController {

    @Autowired
    private RemoverItemMovimentacaoUseCase useCase;

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerMovimentacaoItem(RemoverItemMovimentacaoInputData inputData) {
        useCase.executar(inputData);
    }

}
