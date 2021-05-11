package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.item;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.BuscarPatrimoniosParaSelecaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.BuscarPatrimoniosParaSelecaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.BuscarPatrimoniosParaSelecaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas/{movimentacaoId}/patrimoniosParaSelecao")
public class BuscarPatrimoniosParaSelecaoController {

    @Autowired
    private BuscarPatrimoniosParaSelecaoUseCase useCase;

    @GetMapping
    public BuscarPatrimoniosParaSelecaoOutputData buscarPatrimonios(BuscarPatrimoniosParaSelecaoInputData inputData) {
        return useCase.executar(inputData);
    }

}
