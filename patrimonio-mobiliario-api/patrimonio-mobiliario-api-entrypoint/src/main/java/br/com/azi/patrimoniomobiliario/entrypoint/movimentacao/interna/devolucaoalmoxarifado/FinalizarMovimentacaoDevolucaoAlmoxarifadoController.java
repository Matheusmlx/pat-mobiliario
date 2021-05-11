package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.devolucaoalmoxarifado;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.FinalizarMovimentacaoDevolucaoAlmoxarifadoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.FinalizarMovimentacaoDevolucaoAlmoxarifadoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas/devolucao-almoxarifado/{id}/finalizar")
public class FinalizarMovimentacaoDevolucaoAlmoxarifadoController {

    @Autowired
    private FinalizarMovimentacaoDevolucaoAlmoxarifadoUseCase useCase;

    @PatchMapping
    public FinalizarMovimentacaoDevolucaoAlmoxarifadoOutputData finalizarMovimentacao(FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData inputData) {
        return useCase.executar(inputData);
    }

}
