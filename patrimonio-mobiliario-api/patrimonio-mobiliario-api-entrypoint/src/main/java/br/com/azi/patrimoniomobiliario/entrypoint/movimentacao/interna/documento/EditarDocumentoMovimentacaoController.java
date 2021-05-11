package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.documento;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.EditarDocumentoMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.EditarDocumentoMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.EditarDocumentoMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/movimentacoes/internas/{id}/documentos/{documentoId}")
public class EditarDocumentoMovimentacaoController {

    @Autowired
    private EditarDocumentoMovimentacaoUseCase useCase;


    @PutMapping
    public EditarDocumentoMovimentacaoOutputData editarDocumentoMovimentacao(
        @PathVariable Long id, @PathVariable Long documentoId,
        @RequestBody EditarDocumentoMovimentacaoInputData inputData) {

        inputData.setMovimentacao(id);
        inputData.setId(documentoId);

        return useCase.executar(inputData);
    }
}
