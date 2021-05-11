package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.patrimonios;

import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.BuscarPatrimonioPorIdInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.BuscarPatrimonioPorIdOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.BuscarPatrimonioPorIdUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonio/{id}/ficha")
public class BuscarPatrimonioPorIdController {

    @Autowired
    private BuscarPatrimonioPorIdUseCase useCase;

    @GetMapping
    public BuscarPatrimonioPorIdOutputData buscar(@PathVariable Long id) {
        return useCase.executar(new BuscarPatrimonioPorIdInputData(id));
    }
}
