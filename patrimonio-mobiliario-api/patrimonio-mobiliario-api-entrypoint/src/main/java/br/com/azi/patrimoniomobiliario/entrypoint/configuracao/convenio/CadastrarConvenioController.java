package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.convenio;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.CadastrarConvenioInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.CadastrarConvenioOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.CadastrarConvenioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/convenios")
public class CadastrarConvenioController {

    @Autowired
    CadastrarConvenioUseCase useCase;

    @PostMapping
    public CadastrarConvenioOutputData executar(@RequestBody CadastrarConvenioInputData inputData) {
        return useCase.executar(inputData);
    }
}
