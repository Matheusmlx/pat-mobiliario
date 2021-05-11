package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao;

import br.com.azi.patrimoniomobiliario.domain.constant.permissoes.PermissaoMobiliarioConstants;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.converter.EditarReservaPatrimonialOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception.IntervaloEmUsoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception.IntervaloInvalidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception.IntervaloNaoInformadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception.PreenchimentoInvalidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception.QuantidadeReservadaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception.ReservaNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception.ReservaNaoPodeSerEditadaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception.SituacaoReservaException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class EditarReservaPatrimonialUseCaseImpl implements EditarReservaPatrimonialUseCase {

    private final ReservaDataProvider reservaDataProvider;

    private final ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private final EditarReservaPatrimonialOutputDataConverter outputDataConverter;

    @Override
    public EditarReservaPatrimonialOutputData executar(EditarReservaPatrimonialInputData inputData) {
        validarDadosEntrada(inputData);

        patrimonioDataProvider.bloquearEntidade();

        reservaDataProvider.bloquearEntidade();

        Reserva.Filtro filtro = criarFiltro();
        Reserva reserva = buscarReserva(inputData, filtro);
        verificarSeReservaPodeSerAlterada(reserva);
        verificarSeReservaEstaEmElaboracao(reserva);

        Reserva reservaAtualizada = atualizarReserva(inputData, reserva);

        return outputDataConverter.to(reservaAtualizada);
    }

    private void validarDadosEntrada(EditarReservaPatrimonialInputData inputData) {
        Validator.of(inputData)
            .validate(EditarReservaPatrimonialInputData::getId, Objects::nonNull, "Id da reserva é nulo")
            .validate(EditarReservaPatrimonialInputData::getPreenchimento, Objects::nonNull, "Tipo do preenchimento é nulo")
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

    private Reserva buscarReserva(EditarReservaPatrimonialInputData inputData, Reserva.Filtro filtro) {
        return reservaDataProvider.buscarPorId(inputData.getId(), filtro).orElseThrow(ReservaNaoEncontradaException::new);
    }

    private void verificarSeReservaPodeSerAlterada(Reserva reserva) {
        if(reservaIntervaloDataProvider.existeIntervalosPorReserva(reserva)) {
            throw new ReservaNaoPodeSerEditadaException();
        }
    }

    private void verificarSeReservaEstaEmElaboracao(Reserva reserva) {
        if(!Reserva.Situacao.EM_ELABORACAO.equals(reserva.getSituacao())) {
            throw new SituacaoReservaException();
        }
    }

    private Reserva atualizarReserva(EditarReservaPatrimonialInputData inputData, Reserva reserva) {
        if (Reserva.Preenchimento.AUTOMATICO.name().equals(inputData.getPreenchimento())) {
            return atualizarReservaAutomatica(inputData, reserva);
        } else if (Reserva.Preenchimento.MANUAL.name().equals(inputData.getPreenchimento())) {
            return atualizarReservaManual(inputData, reserva);
        } else {
            throw new PreenchimentoInvalidoException();
        }
    }

    private Reserva atualizarReservaAutomatica(EditarReservaPatrimonialInputData inputData, Reserva reserva) {
        verificarCamposPreenchimentoAutomatico(inputData);

        if(!reservaAutomaticaAlterada(inputData, reserva)) {
            return reserva;
        }

        Long quantidade = inputData.getQuantidadeReservada();
        Long numeroInicial = obterNumeroInicioIntervalo(reserva);
        Long numeroFinal = (numeroInicial + quantidade - 1);

        reserva.setPreenchimento(Reserva.Preenchimento.valueOf(inputData.getPreenchimento()));
        reserva.setQuantidadeReservada(quantidade);
        reserva.setQuantidadeRestante(quantidade);
        reserva.setNumeroInicio(numeroInicial);
        reserva.setNumeroFim(numeroFinal);

        return salvarReserva(reserva);
    }

    private Reserva atualizarReservaManual(EditarReservaPatrimonialInputData inputData, Reserva reserva) {
        verificarCamposPreenchimentoManual(inputData);

        if(!reservaManualAlterada(inputData, reserva)) {
            return reserva;
        }

        Long numeroInicial = inputData.getNumeroInicio();
        Long numeroFinal = inputData.getNumeroFim();
        Long quantidade = (numeroFinal - numeroInicial + 1);

        Reserva.Filtro filtro = Reserva.Filtro
            .builder()
            .numeroInicio(numeroInicial)
            .numeroFim(numeroFinal)
            .reservaId(reserva.getId())
            .build();

        Patrimonio.Filtro patrimonioFiltro = Patrimonio.Filtro
            .builder()
            .numeroInicio(numeroInicial)
            .numeroFim(numeroFinal)
            .build();

        verificarIntervaloEmUso(filtro, patrimonioFiltro);

        reserva.setPreenchimento(Reserva.Preenchimento.valueOf(inputData.getPreenchimento()));
        reserva.setQuantidadeReservada(quantidade);
        reserva.setQuantidadeRestante(quantidade);
        reserva.setNumeroInicio(numeroInicial);
        reserva.setNumeroFim(numeroFinal);

        return salvarReserva(reserva);
    }

    private void verificarCamposPreenchimentoAutomatico(EditarReservaPatrimonialInputData inputData) {
        if (!Objects.nonNull(inputData.getQuantidadeReservada())) {
            throw new QuantidadeReservadaException("A quantidade a ser reservada não foi informada");
        }
        if (inputData.getQuantidadeReservada() <= 0) {
            throw new QuantidadeReservadaException("A quantidade a ser reservada deve ser maior que 0");
        }
    }

    private void verificarCamposPreenchimentoManual(EditarReservaPatrimonialInputData inputData) {
        if (!Objects.nonNull(inputData.getNumeroInicio()) || !Objects.nonNull(inputData.getNumeroFim())) {
            throw new IntervaloNaoInformadoException();
        }
        if (inputData.getNumeroInicio() <= 0 || (inputData.getNumeroFim() < inputData.getNumeroInicio())) {
            throw new IntervaloInvalidoException();
        }
    }

    private boolean reservaAutomaticaAlterada(EditarReservaPatrimonialInputData inputData, Reserva reserva) {
        return !inputData.getPreenchimento().equals(reserva.getPreenchimento().name()) ||
            !inputData.getQuantidadeReservada().equals(reserva.getQuantidadeReservada());
    }

    private boolean reservaManualAlterada(EditarReservaPatrimonialInputData inputData, Reserva reserva) {
        return !inputData.getPreenchimento().equals(reserva.getPreenchimento().name()) ||
            !inputData.getNumeroInicio().equals(reserva.getNumeroInicio()) ||
            !inputData.getNumeroFim().equals(reserva.getNumeroFim());
    }

    private Long obterNumeroInicioIntervalo(Reserva reserva) {
        Optional<Reserva> ultimaReservaCadastrada = reservaDataProvider.buscarReservaComMaiorNumeroDiferenteDe(reserva);
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
            .map(value -> Long.parseLong(value.getNumero()))
            .orElse(0L);
    }

    private void verificarIntervaloEmUso(Reserva.Filtro filtro, Patrimonio.Filtro patrimonioFiltro) {
        if (reservaDataProvider.existeEmIntervalo(filtro) ||
            patrimonioDataProvider.existeEmIntervalo(patrimonioFiltro)) {
            throw new IntervaloEmUsoException();
        }
    }

    private Reserva salvarReserva(Reserva reserva) {
        return reservaDataProvider.salvar(reserva);
    }

}
