package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.empenho;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro.CadastrarEmpenhoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro.CadastrarEmpenhoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro.CadastrarEmpenhoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/empenhos")
public class CadastrarEmpenhoController {

    @Autowired
    private CadastrarEmpenhoUseCase useCase;

    @PostMapping
    public CadastrarEmpenhoOutputData cadastrar(@RequestBody CadastrarEmpenhoInputData inputData) {
        return useCase.executar(inputData);
    }
}
