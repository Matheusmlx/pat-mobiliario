package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarporid;

import br.com.azi.patrimoniomobiliario.domain.constant.permissoes.PermissaoMobiliarioConstants;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarporid.converter.BuscarReservaPorIdOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarporid.exception.ReservaNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class BuscarReservaPorIdUseCaseImpl implements BuscarReservaPorIdUseCase {

    private final ReservaDataProvider reservaDataProvider;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private final BuscarReservaPorIdOutputDataConverter outputDataConverter;

    @Override
    public BuscarReservaPorIdOutputData executar(BuscarReservaPorIdInputData inputData) {
        validarDadosEntrada(inputData);
        Reserva.Filtro filtro = criarFiltro();
        Reserva reserva = buscarReserva(inputData, filtro);
        return outputDataConverter.to(reserva);
    }

    private void validarDadosEntrada(BuscarReservaPorIdInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarReservaPorIdInputData::getId, Objects::nonNull, "Id da reserva é nulo")
            .get();
    }

    private Reserva.Filtro criarFiltro() {
        Reserva.Filtro filtro = new Reserva.Filtro();

        final SessaoUsuario sessaoUsuario = sessaoUsuarioDataProvider.get();
        filtro.setUsuarioId(sessaoUsuario.getId());
        filtro.setFuncoes(List.of(
                PermissaoMobiliarioConstants.NORMAL.getDescription(),
                PermissaoMobiliarioConstants.CONSULTA.getDescription()
        ));

        return filtro;
    }

    private Reserva buscarReserva(BuscarReservaPorIdInputData inputData, Reserva.Filtro filtro) {
        return reservaDataProvider.buscarPorId(inputData.getId(), filtro).orElseThrow(ReservaNaoEncontradaException::new);
    }
}
