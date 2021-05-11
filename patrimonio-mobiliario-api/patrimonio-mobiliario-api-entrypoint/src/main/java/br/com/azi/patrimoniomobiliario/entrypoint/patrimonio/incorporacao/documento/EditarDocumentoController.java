package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.documento;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.EditarDocumentoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.EditarDocumentoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.EditarDocumentoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/{incorporacaoId}/documentos/{id}")
public class EditarDocumentoController {

    @Autowired
    EditarDocumentoUseCase useCase;

    @PutMapping
    public EditarDocumentoOutputData executar(@PathVariable Long id, @RequestBody EditarDocumentoInputData inputData) {
        inputData.setId(id);
        return useCase.executar(inputData);
    }
}
