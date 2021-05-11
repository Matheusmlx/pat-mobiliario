package br.com.azi.patrimoniomobiliario.entrypoint.naturezadespesa;

import br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.BuscarNaturezasDespesaInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.BuscarNaturezasDespesaOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.BuscarNaturezasDespesaUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/natureza-despesa")
public class BuscarNaturezasDespesaController {

    @Autowired
    private BuscarNaturezasDespesaUseCase usecase;

    @GetMapping
    public BuscarNaturezasDespesaOutputData executar(BuscarNaturezasDespesaInputData inputData) {
        return usecase.executar(inputData);
    }
}
