package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximointervalo;

import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximointervalo.converter.BuscarProximoIntervaloDisponivelPorOrgaoOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;
@AllArgsConstructor
public class BuscarProximoIntervaloDisponivelPorOrgaoUseCaseImpl implements BuscarProximoIntervaloDisponivelPorOrgaoUseCase {

    private final ReservaDataProvider reservaDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final BuscarProximoIntervaloDisponivelPorOrgaoOutputDataConverter converter;

    @Override
    public BuscarProximoIntervaloDisponivelPorOrgaoOutputData executar(BuscarProximoIntervaloDisponivelPorOrgaoInputData inputData) {
        validarDadosEntrada(inputData);

        Long numeroInicio = buscarProximoNumero(inputData);
        Long numeroFim = (numeroInicio + inputData.getQuantidadeReservada() - 1);

        return converter.to(numeroInicio,numeroFim);
    }

    private void validarDadosEntrada(BuscarProximoIntervaloDisponivelPorOrgaoInputData inputData){
        Validator.of(inputData)
            .validate(BuscarProximoIntervaloDisponivelPorOrgaoInputData::getQuantidadeReservada, Objects::nonNull, "Quantidade é nula")
            .validate(BuscarProximoIntervaloDisponivelPorOrgaoInputData::getOrgaoId, Objects::nonNull, "Id do órgão é nulo")
            .get();
    }

    private Long buscarProximoNumero(BuscarProximoIntervaloDisponivelPorOrgaoInputData inputData){
       Long ultimoNumeroReserva =  buscarNumeroReservaComMaiorNumeroFimPorOrgao(inputData);
       Long ultimoNumeroPatrimonio =buscarProximoNumeroPatrimonioDisponivel(inputData.getOrgaoId());

       if(ultimoNumeroReserva.compareTo(ultimoNumeroPatrimonio) > 0){
           return ultimoNumeroReserva + 1;
       }

       return ultimoNumeroPatrimonio + 1;

    }

    private Long buscarNumeroReservaComMaiorNumeroFimPorOrgao(BuscarProximoIntervaloDisponivelPorOrgaoInputData inputData){
        Optional<Reserva> reservaIntervalo = reservaDataProvider.buscarReservaComMaiorNumeroFimParaOrgao(inputData.getOrgaoId());

        return reservaIntervalo
            .map(Reserva::getNumeroFim)
            .orElse(0L);
    }

    private Long buscarProximoNumeroPatrimonioDisponivel(Long orgaoId) {
        Optional<Patrimonio> patrimonio = patrimonioDataProvider.buscarPatrimonioComMaiorNumeroPorOrgao(orgaoId);

        return patrimonio
                .map(value -> Long.valueOf(value.getNumero()))
                .orElse(0L);
    }
}
