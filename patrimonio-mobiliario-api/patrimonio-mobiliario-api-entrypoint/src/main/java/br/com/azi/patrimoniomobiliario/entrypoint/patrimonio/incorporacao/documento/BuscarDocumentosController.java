package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.documento;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.buscar.BuscarDocumentosInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.buscar.BuscarDocumentosOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.buscar.BuscarDocumentosUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/{incorporacao}/documentos")
public class BuscarDocumentosController {

    @Autowired
    private BuscarDocumentosUseCase useCase;

    @GetMapping
    public BuscarDocumentosOutputData buscarTodos(BuscarDocumentosInputData inputData) {
        return useCase.executar(inputData);
    }
}
