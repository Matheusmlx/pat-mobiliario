package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.distribuicao;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.edicao.EditarDistribuicaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.edicao.EditarDistribuicaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.edicao.EditarDistribuicaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas/distribuicao/{id}")
public class EditarDistribuicaoController {

    @Autowired
    private EditarDistribuicaoUseCase useCase;

    @PutMapping
    @Transactional
    public EditarDistribuicaoOutputData editarDistribuicao(@PathVariable Long id, @RequestBody EditarDistribuicaoInputData inputData) {
        inputData.setId(id);
        return useCase.executar(inputData);
    }
}
