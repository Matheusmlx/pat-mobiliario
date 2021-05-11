package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.item;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.CadastrarItemMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.CadastrarItemMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.CadastrarItemMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/movimentacoes/internas/{id}/itens")
public class CadastrarItemMovimentacaoController {

    @Autowired
    private CadastrarItemMovimentacaoUseCase useCase;

    @PostMapping
    public CadastrarItemMovimentacaoOutputData cadastrarItensMovimentacao(@PathVariable Long id, @RequestBody CadastrarItemMovimentacaoInputData inputData) {
        inputData.setMovimentacaoId(id);
        return useCase.executar(inputData);
    }
}
