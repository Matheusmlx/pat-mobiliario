package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.possuinumerosutilizados.VerificarReservaPossuiNumerosUtilizadosInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.possuinumerosutilizados.VerificarReservaPossuiNumerosUtilizadosOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.possuinumerosutilizados.VerificarReservaPossuiNumerosUtilizadosUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configuracao/reservas/{id}/possuiNumerosUtilizados")
@ConditionalOnProperty(name = "az.patrimonio-mobiliario.reserva-patrimonial-global", havingValue = "true")
public class VerificarReservaPossuiNumerosUtilizadosController {

    @Autowired
    private VerificarReservaPossuiNumerosUtilizadosUseCase useCase;

    @GetMapping
    public VerificarReservaPossuiNumerosUtilizadosOutputData verificarPossuiNumerosUtilizados(@PathVariable("id") Long id) {
        return useCase.executar(new VerificarReservaPossuiNumerosUtilizadosInputData(id));
    }

}
