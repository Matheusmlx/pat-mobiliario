package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem;

import br.com.azi.patrimoniomobiliario.domain.constant.permissoes.PermissaoMobiliarioConstants;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.converter.BuscarReservasFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.exception.FiltroIncompletoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.converter.BuscarReservasOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class BuscarReservasUseCaseImpl implements BuscarReservasUseCase {

    private final ReservaDataProvider reservaDataProvider;

    private final ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private final BuscarReservasFiltroConverter filtroConverter;

    private final BuscarReservasOutputDataConverter outputDataConverter;

    @Override
    public BuscarReservasOutputData executar(BuscarReservasInputData inputData) {
        validarDadosEntrada(inputData);
        Reserva.Filtro filtro = criarFiltro(inputData);

        ListaPaginada<Reserva> entidadesEncontradas = buscar(filtro);

        entidadesEncontradas.getItems().forEach(reserva -> {
            final List<ReservaIntervalo> intervalos = reservaIntervaloDataProvider.buscarPorReserva(reserva.getId());
            reserva.setIntervalos(intervalos);
        });

        return outputDataConverter.to(entidadesEncontradas);
    }

    private void validarDadosEntrada(BuscarReservasInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarReservasInputData::getPage, Objects::nonNull, new FiltroIncompletoException("Ausência do número da página"))
            .validate(BuscarReservasInputData::getSize, size -> Objects.nonNull(size) && size > 0, new FiltroIncompletoException("Ausência da quantidade de itens por página"))
            .get();
    }

    private Reserva.Filtro criarFiltro(BuscarReservasInputData inputData) {
        Reserva.Filtro filtro = filtroConverter.to(inputData);

        List<String> funcoes = new ArrayList<>();
        funcoes.add(PermissaoMobiliarioConstants.NORMAL.getDescription());
        funcoes.add(PermissaoMobiliarioConstants.CONSULTA.getDescription());

        final SessaoUsuario sessaoUsuario = sessaoUsuarioDataProvider.get();
        filtro.setUsuarioId(sessaoUsuario.getId());
        filtro.setFuncoes(funcoes);

        return filtro;
    }

    private ListaPaginada<Reserva> buscar(Reserva.Filtro filtro) {
        return reservaDataProvider.buscarReservas(filtro);
    }
}
