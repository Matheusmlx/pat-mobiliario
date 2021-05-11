package br.com.azi.patrimoniomobiliario.entrypoint.notificacao;

import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem.BuscarNotificacoesUsuarioInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem.BuscarNotificacoesUsuarioOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem.BuscarNotificacoesUsuarioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notificacoes")
public class BuscarNotificacoesUsuariosController {

    @Autowired
    private BuscarNotificacoesUsuarioUseCase useCase;

    @GetMapping
    public BuscarNotificacoesUsuarioOutputData buscarNotificacoes(@RequestParam Long page) {
        return useCase.executar(new BuscarNotificacoesUsuarioInputData(page));
    }

}
