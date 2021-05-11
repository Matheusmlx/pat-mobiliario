package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.patrimonios;

import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao.BuscarPatrimoniosIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao.BuscarPatrimoniosIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao.BuscarPatrimoniosIncorporacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/{incorporacaoId}/patrimonios")
public class BuscarPatrimoniosIncorporacaoController {

    @Autowired
    private BuscarPatrimoniosIncorporacaoUseCase useCase;

    @GetMapping
    public BuscarPatrimoniosIncorporacaoOutputData buscarPatrimoniosIncorporacao(BuscarPatrimoniosIncorporacaoInputData inputData) {
        return useCase.executar(inputData);
    }

}
