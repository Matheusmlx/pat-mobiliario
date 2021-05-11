package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.documento;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao.DeletarDocumentoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao.DeletarDocumentoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/{incorporacao}/documentos/{id}")
public class RemoverDocumentoController {

    @Autowired
    private DeletarDocumentoUseCase usecase;

    @DeleteMapping
    public ResponseEntity executar(DeletarDocumentoInputData inputData) {
        usecase.executar(inputData);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
