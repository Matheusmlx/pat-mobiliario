package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reserva.pororgao;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximonumero.BuscarProximoNumeroReservaPorOrgaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximonumero.BuscarProximoNumeroReservaPorOrgaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximonumero.BuscarProximoNumeroReservaPorOrgaoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/configuracao/reservas/proximoNumero")
@ConditionalOnProperty(name = "az.patrimonio-mobiliario.reserva-patrimonial-global", havingValue = "false")
public class BuscarProximoNumeroReservaPorOrgaoController {

    private final BuscarProximoNumeroReservaPorOrgaoUseCase useCase;

    @PostMapping
    public BuscarProximoNumeroReservaPorOrgaoOutputData buscarProximoNumero(@RequestBody BuscarProximoNumeroReservaPorOrgaoInputData inputData) {
        return useCase.executar(inputData);
    }

}
