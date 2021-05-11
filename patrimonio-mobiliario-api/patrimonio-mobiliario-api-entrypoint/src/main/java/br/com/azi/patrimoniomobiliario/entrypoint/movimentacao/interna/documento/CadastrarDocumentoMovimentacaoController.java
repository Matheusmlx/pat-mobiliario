package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.documento;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.CadastrarDocumentoMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.CadastrarDocumentoMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.CadastrarDocumentoMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/movimentacoes/internas/{id}/documentos")
public class CadastrarDocumentoMovimentacaoController {

    @Autowired
    private CadastrarDocumentoMovimentacaoUseCase useCase;

    @PostMapping
    public CadastrarDocumentoMovimentacaoOutputData cadastrarDocumentoMovimentacao(@PathVariable Long id, @RequestBody CadastrarDocumentoMovimentacaoInputData inputData) {
        inputData.setMovimentacao(id);
        return useCase.executar(inputData);
    }
}
