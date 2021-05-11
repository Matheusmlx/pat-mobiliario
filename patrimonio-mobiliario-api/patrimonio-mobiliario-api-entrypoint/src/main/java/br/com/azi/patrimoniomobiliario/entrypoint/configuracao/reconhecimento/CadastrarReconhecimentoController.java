package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reconhecimento;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.cadastro.CadastrarReconhecimentoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.cadastro.CadastrarReconhecimentoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.cadastro.CadastrarReconhecimentoUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reconhecimentos")
public class CadastrarReconhecimentoController {

    @Autowired
    CadastrarReconhecimentoUsecase usecase;

    @PostMapping
    public CadastrarReconhecimentoOutputData executar(@RequestBody CadastrarReconhecimentoInputData inputData) {
        return usecase.executar(inputData);
    }
}
