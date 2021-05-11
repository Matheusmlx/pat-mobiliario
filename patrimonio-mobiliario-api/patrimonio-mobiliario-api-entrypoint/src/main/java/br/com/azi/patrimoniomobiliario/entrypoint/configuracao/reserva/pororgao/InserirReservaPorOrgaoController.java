package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reserva.pororgao;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.InserirReservaPorOrgaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.InserirReservaPorOrgaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.InserirReservaPorOrgaoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/configuracao/reservas")
@ConditionalOnProperty(name = "az.patrimonio-mobiliario.reserva-patrimonial-global", havingValue = "false")
public class InserirReservaPorOrgaoController {

    @Autowired
    private InserirReservaPorOrgaoUseCase useCase;

    @PostMapping
    @Transactional
    public InserirReservaPorOrgaoOutputData executar(@RequestBody InserirReservaPorOrgaoInputData inputData) {
        return useCase.executar(inputData);
    }
}
