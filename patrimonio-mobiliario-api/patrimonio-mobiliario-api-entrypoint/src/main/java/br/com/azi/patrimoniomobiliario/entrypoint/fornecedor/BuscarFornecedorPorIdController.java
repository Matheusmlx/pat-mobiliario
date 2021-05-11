package br.com.azi.patrimoniomobiliario.entrypoint.fornecedor;

import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarporid.BuscarFornecedorPorIdInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarporid.BuscarFornecedorPorIdOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarporid.BuscarFornecedorPorIdUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fornecedores/{id}")
public class BuscarFornecedorPorIdController {

    @Autowired
    private BuscarFornecedorPorIdUseCase useCase;

    @GetMapping
    public BuscarFornecedorPorIdOutputData buscarTodos(BuscarFornecedorPorIdInputData inputData) {
        return useCase.executar(inputData);
    }
}
