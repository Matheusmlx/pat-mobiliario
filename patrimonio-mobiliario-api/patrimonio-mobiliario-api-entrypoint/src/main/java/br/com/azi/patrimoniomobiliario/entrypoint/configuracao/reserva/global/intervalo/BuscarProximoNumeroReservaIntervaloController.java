package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.BuscarProximoNumeroReservaIntervaloInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.BuscarProximoNumeroReservaIntervaloOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.BuscarProximoNumeroReservaIntervaloUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/configuracao/reservas/{id}/intervalos/proximoNumero")
@ConditionalOnProperty(name = "az.patrimonio-mobiliario.reserva-patrimonial-global", havingValue = "true")
public class BuscarProximoNumeroReservaIntervaloController {

    private final BuscarProximoNumeroReservaIntervaloUseCase useCase;

    @PostMapping
    public BuscarProximoNumeroReservaIntervaloOutputData buscarProximoNumero(@PathVariable(name = "id") Long reservaId, @RequestBody BuscarProximoNumeroReservaIntervaloInputData inputData) {
        inputData.setReservaId(reservaId);
        return useCase.executar(inputData);
    }

}
