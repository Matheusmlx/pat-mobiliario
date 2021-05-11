package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.temporaria.devolucao;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.BuscarPatrimoniosDevolucaoMovimentacaoTemporariaUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequestMapping("/movimentacoes/internas/temporaria/{id}/devolver-patrimonios")
public class BuscarPatrimoniosDevolucaoMovimentacaoTemporariaController {


    @Autowired
    private BuscarPatrimoniosDevolucaoMovimentacaoTemporariaUseCase useCase;

    @GetMapping
    public BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData executar(@PathVariable Long id,
                                                                               @RequestParam Long page,
                                                                               @RequestParam Long size,
                                                                               BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData inputData) {

        inputData.setMovimentacaoId(id);
        inputData.setSize(size);
        inputData.setPage(page);
        inputData.setDirection("ASC");
        return useCase.executar(inputData);
    }
}
