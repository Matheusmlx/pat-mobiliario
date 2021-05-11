package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.documento;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.buscar.BuscarDocumentosMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.buscar.BuscarDocumentosMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.buscar.BuscarDocumentosMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/movimentacoes/internas/{id}/documentos")
public class BuscarDocumentosMovimentacaoController {

    @Autowired
    private BuscarDocumentosMovimentacaoUseCase useCase;

    @GetMapping
    public BuscarDocumentosMovimentacaoOutputData buscarDocumentosMovimentacao(@PathVariable Long id) {
        return useCase.executar(new BuscarDocumentosMovimentacaoInputData(id));
    }
}
