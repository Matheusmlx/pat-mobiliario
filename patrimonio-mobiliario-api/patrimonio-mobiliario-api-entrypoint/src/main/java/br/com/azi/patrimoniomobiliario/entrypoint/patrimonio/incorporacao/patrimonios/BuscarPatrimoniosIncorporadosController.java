package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.patrimonios;

import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados.BuscarPatrimoniosIncorporadosInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados.BuscarPatrimoniosIncorporadosOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados.BuscarPatrimoniosIncorporadosUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/patrimonios")
public class BuscarPatrimoniosIncorporadosController {

    @Autowired
    private BuscarPatrimoniosIncorporadosUseCase useCase;

    @GetMapping
    public BuscarPatrimoniosIncorporadosOutputData buscarPatrimoniosIncorporados(BuscarPatrimoniosIncorporadosInputData inputData) {
        return useCase.executar(inputData);
    }
}
