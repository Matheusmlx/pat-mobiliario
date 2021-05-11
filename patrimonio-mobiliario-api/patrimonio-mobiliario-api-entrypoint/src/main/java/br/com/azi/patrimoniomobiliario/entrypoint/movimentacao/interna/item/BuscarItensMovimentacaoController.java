package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.item;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.BuscarItensMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.BuscarItensMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.BuscarItensMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/movimentacoes/internas/{id}/itens")
public class BuscarItensMovimentacaoController {

    @Autowired
    private BuscarItensMovimentacaoUseCase useCase;

    @GetMapping
    public BuscarItensMovimentacaoOutputData buscarItensMovimentacao(@PathVariable Long id, BuscarItensMovimentacaoInputData inputData) {
        inputData.setMovimentacaoId(id);
        return useCase.executar(inputData);
    }
}
