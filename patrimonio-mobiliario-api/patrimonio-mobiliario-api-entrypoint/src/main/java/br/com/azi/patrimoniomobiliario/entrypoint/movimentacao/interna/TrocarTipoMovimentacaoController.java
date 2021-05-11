package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.trocartipo.TrocarTipoMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.trocartipo.TrocarTipoMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.trocartipo.TrocarTipoMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/movimentacoes/internas/{movimentacaoId}/tipo")
public class TrocarTipoMovimentacaoController {

    @Autowired
    private TrocarTipoMovimentacaoUseCase useCase;

    @PutMapping
    @Transactional
    public TrocarTipoMovimentacaoOutputData trocarTipoMovimentacao(@PathVariable Long movimentacaoId, @RequestBody TrocarTipoMovimentacaoInputData inputData) {
        inputData.setId(movimentacaoId);
        return useCase.executar(inputData);
    }
}
