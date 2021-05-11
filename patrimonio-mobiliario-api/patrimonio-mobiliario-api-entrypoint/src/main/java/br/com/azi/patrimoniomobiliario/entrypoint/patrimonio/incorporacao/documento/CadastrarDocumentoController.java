package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.documento;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.CadastrarDocumentoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.CadastrarDocumentoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.CadastrarDocumentoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/{incorporacaoId}/documentos")
public class CadastrarDocumentoController {

    @Autowired
    CadastrarDocumentoUseCase useCase;

    @PostMapping
    public CadastrarDocumentoOutputData executar(@RequestBody CadastrarDocumentoInputData inputData) {
        return useCase.executar(inputData);
    }
}
