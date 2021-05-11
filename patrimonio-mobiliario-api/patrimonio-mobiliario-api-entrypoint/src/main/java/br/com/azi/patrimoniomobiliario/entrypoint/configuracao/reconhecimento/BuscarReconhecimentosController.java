package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reconhecimento;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem.BuscarReconhecimentosInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem.BuscarReconhecimentosOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem.BuscarReconhecimentosUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reconhecimentos")
public class BuscarReconhecimentosController {

    @Autowired
    private BuscarReconhecimentosUsecase usecase;

    @GetMapping
    public BuscarReconhecimentosOutputData executar(BuscarReconhecimentosInputData inputData) {
        return usecase.executar(inputData);
    }
}
