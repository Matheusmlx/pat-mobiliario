package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.visualizacao;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.editar.EditarVisualizacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.editar.EditarVisualizacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.editar.EditarVisualizacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas/visualizacao/{id}")
public class EditarVisualizacaoController {

    @Autowired
    private EditarVisualizacaoUseCase useCase;

    @PutMapping
    @Transactional
    public EditarVisualizacaoOutputData editarVisualizacao(@PathVariable Long id, @RequestBody EditarVisualizacaoInputData inputData) {
        inputData.setId(id);
        return useCase.executar(inputData);
    }
}
