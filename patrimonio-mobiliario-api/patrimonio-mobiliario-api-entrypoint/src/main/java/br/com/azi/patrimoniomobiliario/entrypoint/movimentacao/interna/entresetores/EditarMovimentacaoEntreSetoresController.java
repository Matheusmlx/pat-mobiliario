package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.entresetores;


import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.edicao.EditarMovimentacaoEntreSetoresInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.edicao.EditarMovimentacaoEntreSetoresOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.edicao.EditarMovimentacaoEntreSetoresUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas/definitiva/{id}")
public class EditarMovimentacaoEntreSetoresController {

    @Autowired
    private EditarMovimentacaoEntreSetoresUseCase useCase;

    @PutMapping
    @Transactional
    public EditarMovimentacaoEntreSetoresOutputData executar(@PathVariable Long id, @RequestBody EditarMovimentacaoEntreSetoresInputData inputData) {
        inputData.setId(id);
        return useCase.executar(inputData);
    }
}
