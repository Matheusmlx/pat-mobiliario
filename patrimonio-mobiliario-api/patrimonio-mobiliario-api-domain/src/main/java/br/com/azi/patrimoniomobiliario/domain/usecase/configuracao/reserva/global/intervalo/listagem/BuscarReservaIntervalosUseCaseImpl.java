package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.converter.BuscarReservaIntervalosFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.converter.BuscarReservaIntervalosOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.exception.ReservaNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class BuscarReservaIntervalosUseCaseImpl implements BuscarReservaIntervalosUseCase {

    private final ReservaDataProvider reservaDataProvider;

    private final ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    private final BuscarReservaIntervalosFiltroConverter filtroConverter;

    private final BuscarReservaIntervalosOutputDataConverter outpuDataConverter;

    @Override
    public BuscarReservaIntervalosOutputData executar(BuscarReservaIntervalosInputData inputData) {
        validarDadosEntrada(inputData);

        verificarSeReservaExiste(inputData);
        ReservaIntervalo.Filtro filtro = criarFiltro(inputData);
        ListaPaginada<ReservaIntervalo> intervalosEncontrados = buscarIntervalosAdicionados(filtro);

        return outpuDataConverter.to(intervalosEncontrados);
    }

    private void validarDadosEntrada(BuscarReservaIntervalosInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarReservaIntervalosInputData::getReservaId, Objects::nonNull, "Id da reserva é nulo")
            .validate(BuscarReservaIntervalosInputData::getPage, page -> Objects.nonNull(page) && page > 0, "Página não informada")
            .validate(BuscarReservaIntervalosInputData::getSize, size -> Objects.nonNull(size) && size > 0, "Tamanho não informado")
            .get();
    }

    private void verificarSeReservaExiste(BuscarReservaIntervalosInputData inputData) {
        if(!reservaDataProvider.existe(inputData.getReservaId())) {
            throw new ReservaNaoEncontradaException();
        }
    }

    private ReservaIntervalo.Filtro criarFiltro(BuscarReservaIntervalosInputData inputData) {
        return filtroConverter.to(inputData);
    }

    private ListaPaginada<ReservaIntervalo> buscarIntervalosAdicionados(ReservaIntervalo.Filtro filtro) {
        return reservaIntervaloDataProvider.buscarIntervalosPorReserva(filtro);
    }

}
