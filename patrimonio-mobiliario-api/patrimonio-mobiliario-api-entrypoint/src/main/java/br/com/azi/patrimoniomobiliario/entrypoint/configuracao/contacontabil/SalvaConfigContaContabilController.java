package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.contacontabil;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.EditarContaContabilInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.EditarContaContabilOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.EditarContaContabilUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequestMapping("/configuracao/contas-contabeis/{contaContabilId}/config-depreciacao")
public class SalvaConfigContaContabilController {

    @Autowired
    private EditarContaContabilUseCase usecase;

    @PostMapping
    public EditarContaContabilOutputData executar(@PathVariable Long contaContabilId, @RequestBody EditarContaContabilInputData inputData) {
        inputData.setContaContabil(contaContabilId);
        return usecase.executar(inputData);
    }
}
