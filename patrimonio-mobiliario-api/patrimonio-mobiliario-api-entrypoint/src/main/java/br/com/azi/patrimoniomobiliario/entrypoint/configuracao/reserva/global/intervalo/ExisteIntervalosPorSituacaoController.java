package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.existeporsituacao.ExisteIntervalosPorSituacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.existeporsituacao.ExisteIntervalosPorSituacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.existeporsituacao.ExisteIntervalosPorSituacaoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/configuracao/reservas/{id}/intervalos/existe/{situacao}")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "az.patrimonio-mobiliario.reserva-patrimonial-global", havingValue = "true")
public class ExisteIntervalosPorSituacaoController {

    private final ExisteIntervalosPorSituacaoUseCase useCase;

    @GetMapping
    public ExisteIntervalosPorSituacaoOutputData existeIntervalosPorSituacao(@PathVariable("id") Long reservaId, @PathVariable ReservaIntervalo.Situacao situacao) {
        return useCase.executar(ExisteIntervalosPorSituacaoInputData.builder()
            .reservaId(reservaId)
            .situacao(situacao)
            .build()
        );
    }
}
