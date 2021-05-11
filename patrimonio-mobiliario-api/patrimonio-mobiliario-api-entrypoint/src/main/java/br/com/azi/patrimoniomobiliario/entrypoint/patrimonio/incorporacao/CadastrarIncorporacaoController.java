package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.cadastro.CadastrarIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.cadastro.CadastrarIncorporacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao")
public class CadastrarIncorporacaoController {

    @Autowired
    CadastrarIncorporacaoUseCase usecase;

    @PostMapping
    public CadastrarIncorporacaoOutputData executar() {
        return usecase.executar();
    }
}
