package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.listagem.BuscarMovimentacoesInternasInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.listagem.BuscarMovimentacoesInternasOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.listagem.BuscarMovimentacoesInternasUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas")
public class BuscarMovimentacoesInternasController {

    @Autowired
    public BuscarMovimentacoesInternasUseCase useCase;

    @GetMapping
    public BuscarMovimentacoesInternasOutputData buscarMovimentacoesInternas(BuscarMovimentacoesInternasInputData inputData) {
        return useCase.executar(inputData);
    }

}
