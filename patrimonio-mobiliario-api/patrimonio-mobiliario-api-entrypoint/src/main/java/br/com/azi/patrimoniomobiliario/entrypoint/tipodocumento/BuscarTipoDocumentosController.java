package br.com.azi.patrimoniomobiliario.entrypoint.tipodocumento;

import br.com.azi.patrimoniomobiliario.domain.usecase.tipodocumento.buscar.BuscarTipoDocumentosOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.tipodocumento.buscar.BuscarTipoDocumentosUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tiposdocumentos")
public class BuscarTipoDocumentosController {

    @Autowired
    private BuscarTipoDocumentosUseCase useCase;

    @GetMapping
    public BuscarTipoDocumentosOutputData buscarTodos() {
        return useCase.executar();
    }
}
