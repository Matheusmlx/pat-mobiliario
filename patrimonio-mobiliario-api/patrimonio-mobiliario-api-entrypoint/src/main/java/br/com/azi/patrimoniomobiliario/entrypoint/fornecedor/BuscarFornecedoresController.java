package br.com.azi.patrimoniomobiliario.entrypoint.fornecedor;

import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarlistagem.BuscarFornecedoresInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarlistagem.BuscarFornecedoresOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarlistagem.BuscarFornecedoresUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fornecedores")
public class BuscarFornecedoresController {

    @Autowired
    private BuscarFornecedoresUseCase useCase;

    @GetMapping
    public BuscarFornecedoresOutputData buscarTodos(BuscarFornecedoresInputData inputData) {
        return useCase.executar(inputData);
    }
}
