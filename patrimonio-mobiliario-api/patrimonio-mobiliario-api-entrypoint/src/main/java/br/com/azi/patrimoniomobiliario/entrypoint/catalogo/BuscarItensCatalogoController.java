package br.com.azi.patrimoniomobiliario.entrypoint.catalogo;

import br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.BuscarItensCatalogoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.BuscarItensCatalogoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.BuscarItensCatalogoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/catalogo/itens")
public class BuscarItensCatalogoController {

    @Autowired
    private BuscarItensCatalogoUseCase useCase;

    @GetMapping
    public BuscarItensCatalogoOutputData executar(BuscarItensCatalogoInputData inputData){
       return useCase.executar(inputData);
    }
}
