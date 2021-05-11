package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.documento;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.remover.RemoverDocumentoMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.remover.RemoverDocumentoMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas/{movimentacao}/documentos/{id}")
public class RemoverDocumentoMovimentacaoController {

    @Autowired
    private RemoverDocumentoMovimentacaoUseCase usecase;

    @DeleteMapping
    public ResponseEntity executar(RemoverDocumentoMovimentacaoInputData inputData) {
        usecase.executar(inputData);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
