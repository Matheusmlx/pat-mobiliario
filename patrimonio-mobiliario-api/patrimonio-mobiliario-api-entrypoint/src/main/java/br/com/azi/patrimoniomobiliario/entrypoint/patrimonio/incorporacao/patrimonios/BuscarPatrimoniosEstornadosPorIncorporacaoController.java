package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.patrimonios;

import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemestornados.BuscarPatrimoniosEstornadosPorIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemestornados.BuscarPatrimoniosEstornadosPorIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemestornados.BuscarPatrimoniosEstornadosPorIncorporacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/{incorporacaoId}/patrimonios/estornados")
public class BuscarPatrimoniosEstornadosPorIncorporacaoController {

    @Autowired
    private BuscarPatrimoniosEstornadosPorIncorporacaoUseCase useCase;

    @GetMapping
    public BuscarPatrimoniosEstornadosPorIncorporacaoOutputData buscarPatrimoniosEstornados(BuscarPatrimoniosEstornadosPorIncorporacaoInputData inputData) {
        return useCase.executar(inputData);
    }
}
