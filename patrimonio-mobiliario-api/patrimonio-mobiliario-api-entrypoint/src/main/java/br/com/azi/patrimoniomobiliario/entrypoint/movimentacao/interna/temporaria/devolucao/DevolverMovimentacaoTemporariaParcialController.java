package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.temporaria.devolucao;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.DevolverMovimentacaoTemporariaInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.DevolverMovimentacaoTemporariaOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.DevolverMovimentacaoTemporariaUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Transactional
@RestController
@RequestMapping("/movimentacoes/internas/temporaria/{id}/devolver-parcial")
public class DevolverMovimentacaoTemporariaParcialController {

    @Autowired
    private DevolverMovimentacaoTemporariaUseCase useCase;

    @PutMapping
    public DevolverMovimentacaoTemporariaOutputData executar(@PathVariable Long id,
                                                             @RequestParam List<Long>  patrimoniosId) {

        DevolverMovimentacaoTemporariaInputData inputData = DevolverMovimentacaoTemporariaInputData
            .builder()
            .movimentacaoId(id)
            .patrimoniosId(patrimoniosId)
            .build();

        return useCase.executar(inputData);
    }
}
