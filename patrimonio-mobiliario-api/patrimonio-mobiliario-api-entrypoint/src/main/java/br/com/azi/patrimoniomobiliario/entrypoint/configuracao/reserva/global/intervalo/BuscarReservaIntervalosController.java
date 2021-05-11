package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.BuscarReservaIntervalosInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.BuscarReservaIntervalosOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.BuscarReservaIntervalosUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configuracao/reservas/{id}/intervalos")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "az.patrimonio-mobiliario.reserva-patrimonial-global", havingValue = "true")
public class BuscarReservaIntervalosController {

    private final BuscarReservaIntervalosUseCase useCase;

    @GetMapping
    public BuscarReservaIntervalosOutputData buscarOrgaosAdicionadosReserva(
        @PathVariable("id") Long reservaId, BuscarReservaIntervalosInputData inputData) {

        inputData.setReservaId(reservaId);
        return useCase.executar(inputData);
    }

}
