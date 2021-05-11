package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscartipo.BuscarTipoMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscartipo.BuscarTipoMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscartipo.BuscarTipoMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas/{movimentacaoId}/tipo")
public class BuscarTipoMovimentacaoController {

    @Autowired
    private BuscarTipoMovimentacaoUseCase useCase;

    @GetMapping
    public BuscarTipoMovimentacaoOutputData buscarTipoMovimentacao(BuscarTipoMovimentacaoInputData inputData) {
        return useCase.executar(inputData);
    }

}
