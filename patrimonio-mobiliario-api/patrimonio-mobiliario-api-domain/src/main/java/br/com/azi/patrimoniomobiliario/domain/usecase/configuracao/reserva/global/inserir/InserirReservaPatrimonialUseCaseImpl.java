package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir;

import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.converter.InserirReservaPatrimonialOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.exception.IntervaloEmUsoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.exception.IntervaloInvalidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.exception.IntervaloNaoInformadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.exception.PreenchimentoInvalidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.exception.QuantidadeReservadaException;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class InserirReservaPatrimonialUseCaseImpl implements InserirReservaPatrimonialUseCase {

    private final Clock clock;

    private final ReservaDataProvider reservaDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final InserirReservaPatrimonialOutputDataConverter outputDataConverter;

    @Override
    public InserirReservaPatrimonialOutputData executar(InserirReservaPatrimonialInputData inputData) {
        validarDadosEntrada(inputData);

        patrimonioDataProvider.bloquearEntidade();

        reservaDataProvider.bloquearEntidade();

        Reserva reserva = cadastrarReserva(inputData);

        return outputDataConverter.to(reserva);
    }

    private void validarDadosEntrada(InserirReservaPatrimonialInputData inputData) {
        Validator.of(inputData)
            .validate(InserirReservaPatrimonialInputData::getPreenchimento, Objects::nonNull, "Tipo do preenchimento é nulo")
            .get();
    }

    private Reserva cadastrarReserva(InserirReservaPatrimonialInputData inputData) {
        if (Reserva.Preenchimento.AUTOMATICO.name().equals(inputData.getPreenchimento())) {
            return realizarReservaAutomatica(inputData);
        } else if (Reserva.Preenchimento.MANUAL.name().equals(inputData.getPreenchimento())) {
            return realizarReservaManual(inputData);
        } else {
            throw new PreenchimentoInvalidoException();
        }
    }

    private Reserva realizarReservaAutomatica(InserirReservaPatrimonialInputData inputData) {
        verificarCamposPreenchimentoAutomatico(inputData);

        Long quantidade = inputData.getQuantidadeReservada();
        Long numeroInicial = obterNumeroInicioIntervalo();
        Long numeroFinal = (numeroInicial + quantidade - 1);
        String proximoCodigo = gerarCodigoReservaFormatado();

        Reserva reserva = Reserva
            .builder()
            .codigo(proximoCodigo)
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.valueOf(inputData.getPreenchimento()))
            .dataCriacao(LocalDateTime.now(clock))
            .quantidadeReservada(quantidade)
            .quantidadeRestante(quantidade)
            .numeroInicio(numeroInicial)
            .numeroFim(numeroFinal)
            .build();

        return salvarReserva(reserva);
    }

    private Reserva realizarReservaManual(InserirReservaPatrimonialInputData inputData) {
        verificarCamposPreenchimentoManual(inputData);

        Long numeroInicial = inputData.getNumeroInicio();
        Long numeroFinal = inputData.getNumeroFim();
        Long quantidade = (numeroFinal - numeroInicial + 1);

        Reserva.Filtro filtro = Reserva.Filtro
            .builder()
            .numeroInicio(numeroInicial)
            .numeroFim(numeroFinal)
            .build();

        Patrimonio.Filtro patrimonioFiltro = Patrimonio.Filtro
            .builder()
            .numeroInicio(numeroInicial)
            .numeroFim(numeroFinal)
            .build();

        verificarIntervaloEmUso(filtro, patrimonioFiltro);
        String proximoCodigo = gerarCodigoReservaFormatado();

        Reserva reserva = Reserva
            .builder()
            .codigo(proximoCodigo)
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.valueOf(inputData.getPreenchimento()))
            .dataCriacao(LocalDateTime.now(clock))
            .quantidadeReservada(quantidade)
            .quantidadeRestante(quantidade)
            .numeroInicio(numeroInicial)
            .numeroFim(numeroFinal)
            .build();

        return salvarReserva(reserva);
    }

    private void verificarIntervaloEmUso(Reserva.Filtro filtro, Patrimonio.Filtro patrimonioFiltro) {
        if (reservaDataProvider.existeEmIntervalo(filtro) ||
            patrimonioDataProvider.existeEmIntervalo(patrimonioFiltro)) {
            throw new IntervaloEmUsoException();
        }
    }

    private void verificarCamposPreenchimentoAutomatico(InserirReservaPatrimonialInputData inputData) {
        if (!Objects.nonNull(inputData.getQuantidadeReservada())) {
            throw new QuantidadeReservadaException("A quantidade a ser reservada não foi informada");
        }
        if (inputData.getQuantidadeReservada() <= 0) {
            throw new QuantidadeReservadaException("A quantidade a ser reservada deve ser maior que 0");
        }
    }

    private void verificarCamposPreenchimentoManual(InserirReservaPatrimonialInputData inputData) {
        if (!Objects.nonNull(inputData.getNumeroInicio()) || !Objects.nonNull(inputData.getNumeroFim())) {
            throw new IntervaloNaoInformadoException();
        }
        if (inputData.getNumeroInicio() <= 0 || (inputData.getNumeroFim() < inputData.getNumeroInicio())) {
            throw new IntervaloInvalidoException();
        }
    }

    private Long obterNumeroInicioIntervalo() {
        Optional<Reserva> ultimaReservaCadastrada = reservaDataProvider.buscarReservaComMaiorNumeroFim();
        Long ultimoNumeroPatrimonio = obterUltimoNumeroPatrimonioCadastrado();

        if (ultimaReservaCadastrada.isPresent()) {
            Long proximoNumeroReserva = ultimaReservaCadastrada.get().getNumeroFim() + 1;
            if (proximoNumeroReserva.compareTo(ultimoNumeroPatrimonio) > 0) {
                return proximoNumeroReserva;
            }
        }

        return ultimoNumeroPatrimonio + 1;
    }

    private Long obterUltimoNumeroPatrimonioCadastrado() {
        Optional<Patrimonio> patrimonio = patrimonioDataProvider.buscarPatrimonioComMaiorNumero();

        return patrimonio
            .map(value -> Long.valueOf(value.getNumero()))
            .orElse(0L);
    }

    private String gerarCodigoReservaFormatado() {
        Optional<Reserva> reserva = reservaDataProvider.buscarReservaComMaiorCodigo();
        if (reserva.isPresent()) {
            String codigo = reserva.get().getCodigo();
            if (Objects.nonNull(codigo)) {
                Long proximoNumero = Long.parseLong(codigo) + 1;
                return String.format("%5d", proximoNumero).replace(" ", "0");
            }
        }
        return String.format("%5d", 1).replace(" ", "0");
    }

    private Reserva salvarReserva(Reserva reserva) {
        return reservaDataProvider.salvar(reserva);
    }
}
