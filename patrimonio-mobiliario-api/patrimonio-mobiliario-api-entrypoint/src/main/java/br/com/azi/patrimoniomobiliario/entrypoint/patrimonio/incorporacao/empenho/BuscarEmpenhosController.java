package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.empenho;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.buscarlistagem.BuscarEmpenhosInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.buscarlistagem.BuscarEmpenhosOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.buscarlistagem.BuscarEmpenhosUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/{incorporacaoId}/empenhos")
public class BuscarEmpenhosController {

    @Autowired
    private BuscarEmpenhosUseCase useCase;

    @GetMapping
    public BuscarEmpenhosOutputData buscarPorIncorporacao(BuscarEmpenhosInputData inputData) {
        return useCase.executar(inputData);
    }
}
