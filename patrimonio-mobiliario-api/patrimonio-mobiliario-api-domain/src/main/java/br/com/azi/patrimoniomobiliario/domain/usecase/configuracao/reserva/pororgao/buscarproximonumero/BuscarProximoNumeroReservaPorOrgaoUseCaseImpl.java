package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximonumero;

import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximonumero.converter.BuscarProximoNumeroReservaPorOrgaoOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BuscarProximoNumeroReservaPorOrgaoUseCaseImpl implements BuscarProximoNumeroReservaPorOrgaoUseCase {

    private final ReservaDataProvider reservaDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final BuscarProximoNumeroReservaPorOrgaoOutputDataConverter outputDataConverter;

    @Override
    public BuscarProximoNumeroReservaPorOrgaoOutputData executar(BuscarProximoNumeroReservaPorOrgaoInputData inputData) {
        validarDadosDeEntrada(inputData);
        Long proximoNumero = retornarProximoNumero(inputData);
        return outputDataConverter.to(proximoNumero);
    }

    private void validarDadosDeEntrada(BuscarProximoNumeroReservaPorOrgaoInputData inputData) {
        Validator.of(inputData)
                .validate(BuscarProximoNumeroReservaPorOrgaoInputData::getOrgaoId, Objects::nonNull, "O órgão é nulo")
                .get();
    }

    private Long retornarProximoNumero(BuscarProximoNumeroReservaPorOrgaoInputData inputData) {
        Long ultimoNumeroReserva = retornarUltimoNumeroReservaParaOrgao(inputData);
        Long ultimoNumeroPatrimonio = retornarUltimoNumeroPatrimonioCadastrado(inputData);

        if (ultimoNumeroReserva.compareTo(ultimoNumeroPatrimonio) > 0) {
            return ultimoNumeroReserva + 1;
        }

        return ultimoNumeroPatrimonio + 1;
    }

    private Long retornarUltimoNumeroReservaParaOrgao(BuscarProximoNumeroReservaPorOrgaoInputData inputData) {
        Optional<Reserva> reserva = reservaDataProvider.buscarReservaComMaiorNumeroFimParaOrgao(inputData.getOrgaoId());

        return reserva
                .map(Reserva::getNumeroFim)
                .orElse(0L);
    }

    private Long retornarUltimoNumeroPatrimonioCadastrado(BuscarProximoNumeroReservaPorOrgaoInputData inputData) {
        Optional<Patrimonio> patrimonio = patrimonioDataProvider.buscarPatrimonioComMaiorNumeroPorOrgao(inputData.getOrgaoId());

        return patrimonio
                .map(value -> Long.valueOf(value.getNumero()))
                .orElse(0L);
    }

}
