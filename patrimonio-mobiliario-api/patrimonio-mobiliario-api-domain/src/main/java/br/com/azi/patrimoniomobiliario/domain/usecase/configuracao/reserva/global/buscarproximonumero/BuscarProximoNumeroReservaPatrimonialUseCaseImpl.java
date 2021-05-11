package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarproximonumero;

import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarproximonumero.converter.BuscarProximoNumeroReservaPatrimonialOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class BuscarProximoNumeroReservaPatrimonialUseCaseImpl implements BuscarProximoNumeroReservaPatrimonialUseCase {

    private final ReservaDataProvider reservaDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final BuscarProximoNumeroReservaPatrimonialOutputDataConverter outputDataConverter;

    @Override
    public BuscarProximoNumeroReservaPatrimonialOutputData executar() {
        Long numeroInicioIntervalo = obterNumeroInicioIntervalo();
        return outputDataConverter.to(numeroInicioIntervalo);
    }

    private Long obterNumeroInicioIntervalo() {
        Long ultimoNumeroReserva = obterUltimoNumeroReserva();
        Long ultimoNumeroPatrimonio = obterUltimoNumeroPatrimonioCadastrado();

        Long proximoNumeroReserva = ultimoNumeroReserva + 1;
        if (proximoNumeroReserva.compareTo(ultimoNumeroPatrimonio) > 0) {
            return proximoNumeroReserva;
        }

        return ultimoNumeroPatrimonio + 1;
    }

    private Long obterUltimoNumeroReserva() {
        Optional<Reserva> reserva = reservaDataProvider.buscarReservaComMaiorNumeroFim();

        return reserva
            .map(Reserva::getNumeroFim)
            .orElse(0L);
    }

    private Long obterUltimoNumeroPatrimonioCadastrado() {
        Optional<Patrimonio> patrimonio = patrimonioDataProvider.buscarPatrimonioComMaiorNumero();

        return patrimonio
            .map(value -> Long.valueOf(value.getNumero()))
            .orElse(0L);
    }

}
