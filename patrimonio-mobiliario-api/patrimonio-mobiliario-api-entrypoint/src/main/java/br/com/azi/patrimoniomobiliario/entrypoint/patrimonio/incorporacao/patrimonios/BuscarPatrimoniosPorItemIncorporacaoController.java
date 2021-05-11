package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.patrimonios;

import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.BuscarPatrimoniosPorItemIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.BuscarPatrimoniosPorItemIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.BuscarPatrimoniosPorItemIncorporacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/items/{itemIncorporacaoId}/patrimonios")
public class BuscarPatrimoniosPorItemIncorporacaoController {

    @Autowired
    private BuscarPatrimoniosPorItemIncorporacaoUseCase useCase;

    @GetMapping
    public BuscarPatrimoniosPorItemIncorporacaoOutputData buscarPorItemIncorporacao(@PathVariable Long itemIncorporacaoId, BuscarPatrimoniosPorItemIncorporacaoInputData inputData) {
        inputData.setItemIncorporacaoId(itemIncorporacaoId);
        return useCase.executar(inputData);
    }
}
