package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.patrimonios;

import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.EditarPatrimonioInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.EditarPatrimonioOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.EditarPatrimonioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/items/patrimonios/{patrimonioId}")
public class EditarPatrimonioController {

    @Autowired
    private EditarPatrimonioUseCase useCase;

    @PutMapping
    public EditarPatrimonioOutputData editar(@PathVariable(name="patrimonioId") Long id, @RequestBody EditarPatrimonioInputData inputData) {
        inputData.setId(id);
        return useCase.executar(inputData);
    }
}
