package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.devolucaoalmoxarifado;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.edicao.EditarDevolucaoAlmoxarifadoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.edicao.EditarDevolucaoAlmoxarifadoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.edicao.EditarDevolucaoAlmoxarifadoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas/devolucao-almoxarifado/{id}")
public class EditarDevolucaoAlmoxarifadoController {

    @Autowired
    private EditarDevolucaoAlmoxarifadoUseCase useCase;

    @PutMapping
    @Transactional
    public EditarDevolucaoAlmoxarifadoOutputData executar(@PathVariable Long id, @RequestBody EditarDevolucaoAlmoxarifadoInputData inputData) {
        inputData.setId(id);
        return useCase.executar(inputData);
    }
}
