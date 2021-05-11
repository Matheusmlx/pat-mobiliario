package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.concedente;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro.CadastrarConcedenteInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro.CadastrarConcedenteOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro.CadastrarConcedenteUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concedentes")
public class CadastrarConcedenteController {

    @Autowired
    CadastrarConcedenteUseCase useCase;

    @PostMapping
    public CadastrarConcedenteOutputData executar(@RequestBody CadastrarConcedenteInputData inputData) {
        return useCase.executar(inputData);
    }
}
