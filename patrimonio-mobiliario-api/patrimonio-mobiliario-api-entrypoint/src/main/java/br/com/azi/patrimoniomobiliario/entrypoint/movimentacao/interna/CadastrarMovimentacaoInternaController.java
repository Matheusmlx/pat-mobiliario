package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.cadastro.CadastrarMovimentacaoInternaInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.cadastro.CadastrarMovimentacaoInternaOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.cadastro.CadastrarMovimentacaoInternaUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas")
public class CadastrarMovimentacaoInternaController {

    @Autowired
    private CadastrarMovimentacaoInternaUseCase useCase;

    @PostMapping
    public CadastrarMovimentacaoInternaOutputData cadastrarDistribuicao(@RequestBody CadastrarMovimentacaoInternaInputData inputData) {
        return useCase.executar(inputData);
    }
}
