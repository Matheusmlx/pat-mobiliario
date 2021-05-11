package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.concedente;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.edicao.EditarConcedenteInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.edicao.EditarConcedenteOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.edicao.EditarConcedenteUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concedentes/{id}")
public class EditarConcedenteController {

    @Autowired
    private EditarConcedenteUseCase useCase;

    @PutMapping
    public EditarConcedenteOutputData editar(@PathVariable Long id, @RequestBody EditarConcedenteInputData inputData) {
        inputData.setId(id);
        return useCase.executar(inputData);
    }
}
