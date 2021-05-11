package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reconhecimento;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao.EditarReconhecimentoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao.EditarReconhecimentoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao.EditarReconhecimentoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reconhecimentos/{id}")
public class EditarReconhecimentoController {

    @Autowired
    EditarReconhecimentoUseCase usecase;

    @PutMapping
    public EditarReconhecimentoOutputData executar(@PathVariable Long id, @RequestBody EditarReconhecimentoInputData inputData) {
        inputData.setId(id);
        return usecase.executar(inputData);
    }
}
