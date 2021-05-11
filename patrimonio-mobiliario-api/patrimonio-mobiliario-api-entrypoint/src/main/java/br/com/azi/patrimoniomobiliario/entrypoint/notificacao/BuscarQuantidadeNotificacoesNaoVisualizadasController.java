package br.com.azi.patrimoniomobiliario.entrypoint.notificacao;

import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.buscarquantidadenaovisualizadas.BuscarQuantidadeNotificacoesNaoVisualizadasOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.buscarquantidadenaovisualizadas.BuscarQuantidadeNotificacoesNaoVisualizadasUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notificacoes/quantidade-nao-visualizadas")
public class BuscarQuantidadeNotificacoesNaoVisualizadasController {

    @Autowired
    private BuscarQuantidadeNotificacoesNaoVisualizadasUseCase useCase;

    @GetMapping
    public BuscarQuantidadeNotificacoesNaoVisualizadasOutputData buscarQuantidade() {
        return useCase.executar();
    }

}
