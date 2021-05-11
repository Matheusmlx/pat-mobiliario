package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.empenho;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.EditarEmpenhoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.EditarEmpenhoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.EditarEmpenhoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/empenhos/{id}")
public class EditarEmpenhoController {

    @Autowired
    private EditarEmpenhoUseCase useCase;

    @PutMapping
    public EditarEmpenhoOutputData editar(@PathVariable Long id, @RequestBody EditarEmpenhoInputData inputData) {
        inputData.setId(id);
        return useCase.executar(inputData);
    }
}
