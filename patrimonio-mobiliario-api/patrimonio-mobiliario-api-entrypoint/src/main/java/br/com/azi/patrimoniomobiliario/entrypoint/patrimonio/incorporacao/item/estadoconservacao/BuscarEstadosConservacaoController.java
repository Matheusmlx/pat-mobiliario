package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.item.estadoconservacao;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.estadoconservacao.listagem.BuscarEstadosConservacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.estadoconservacao.listagem.BuscarEstadosConservacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/itens/estadosconservacao")
public class BuscarEstadosConservacaoController {

    @Autowired
    private BuscarEstadosConservacaoUseCase useCase;

    @GetMapping
    public BuscarEstadosConservacaoOutputData buscarEstadosConservacao() {
        return useCase.executar();
    }
}
