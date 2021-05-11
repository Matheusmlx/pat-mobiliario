package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.item;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarquantidadeitens.BuscarQuantidadeItensMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarquantidadeitens.BuscarQuantidadeItensMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarquantidadeitens.BuscarQuantidadeItensMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/movimentacoes/internas/{id}/itens/quantidade")
public class BuscarQuantidadeItensMovimentacaoController {

    @Autowired
    private BuscarQuantidadeItensMovimentacaoUseCase useCase;

    @GetMapping
    public BuscarQuantidadeItensMovimentacaoOutputData buscarQuantidadeItensMovimentacao(@PathVariable Long id) {
        return useCase.executar(new BuscarQuantidadeItensMovimentacaoInputData(id));
    }
}
