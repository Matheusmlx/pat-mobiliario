package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.empenho;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.remocao.RemoverEmpenhoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.remocao.RemoverEmpenhoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/empenhos/{id}")
public class RemoverEmpenhoController {

    @Autowired
    private RemoverEmpenhoUseCase useCase;

    @DeleteMapping
    public void remover(RemoverEmpenhoInputData inputData) {
        useCase.executar(inputData);
    }
}
