package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.temporaria;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.edicao.EditarMovimentacaoTemporariaInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.edicao.EditarMovimentacaoTemporariaOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.edicao.EditarMovimentacaoTemporariaUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas/temporaria/{id}")
public class EditarMovimentacaoTemporariaController {

    @Autowired
    private EditarMovimentacaoTemporariaUseCase useCase;

    @PutMapping
    @Transactional
    public EditarMovimentacaoTemporariaOutputData executar(@PathVariable Long id, @RequestBody EditarMovimentacaoTemporariaInputData inputData) {
        inputData.setId(id);
        return useCase.executar(inputData);
    }
}
