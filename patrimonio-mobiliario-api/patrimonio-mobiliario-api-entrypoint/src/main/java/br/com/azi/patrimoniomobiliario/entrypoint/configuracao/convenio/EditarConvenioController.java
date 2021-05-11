package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.convenio;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.EditarConvenioInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.EditarConvenioOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.EditarConvenioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/convenios/{id}")
public class EditarConvenioController {

    @Autowired
    EditarConvenioUseCase usecase;

    @PutMapping
    public EditarConvenioOutputData executar(@PathVariable Long id, @RequestBody EditarConvenioInputData inputData) {
        inputData.setId(id);
        return usecase.executar(inputData);
    }
}
