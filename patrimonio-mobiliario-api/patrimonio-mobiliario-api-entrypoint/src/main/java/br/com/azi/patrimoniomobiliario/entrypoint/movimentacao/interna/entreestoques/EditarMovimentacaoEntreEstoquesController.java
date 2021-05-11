package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.entreestoques;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.EditarMovimentacaoEntreEstoquesInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.EditarMovimentacaoEntreEstoquesOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.EditarMovimentacaoEntreEstoquesUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas/entre-estoques/{id}")
public class EditarMovimentacaoEntreEstoquesController {

    @Autowired
    private EditarMovimentacaoEntreEstoquesUseCase useCase;

    @PutMapping
    @Transactional
    public EditarMovimentacaoEntreEstoquesOutputData executar(@PathVariable Long id, @RequestBody EditarMovimentacaoEntreEstoquesInputData inputData) {
        inputData.setId(id);
        return useCase.executar(inputData);
    }
}
