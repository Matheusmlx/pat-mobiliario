package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar;

import br.com.azi.patrimoniomobiliario.domain.constant.permissoes.PermissaoMobiliarioConstants;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception.CampoNaoInformadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception.InterseccaoEntreIntervalosException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception.IntervaloComNumeroFimPosteriorAReservaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception.IntervaloComNumeroInicioAnteriorAReservaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception.IntervaloComQuantidadeIncorretaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception.IntervaloEmUsoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception.IntervaloInvalidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception.IntervaloNaoInformadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception.QuantidadeTotalDeNumerosDosIntervalosMaiorQueAQuantidadeRestanteException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception.ReservaNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
public class CadastrarReservaIntervaloUseCaseImpl implements CadastrarReservaIntervaloUseCase {

    private final ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    private final ReservaDataProvider reservaDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Override
    public void executar(CadastrarReservaIntervaloInputData inputData) {
        validarDadosEntrada(inputData);

        validarCamposPreenchidosCorretamente(inputData.getIntervalos());

        validarIntervalosComQuantidadeCorreta(inputData.getIntervalos());

        reservaIntervaloDataProvider.bloquearEntidade();

        Reserva.Filtro filtro = criarFiltro();

        Reserva reserva = buscarReserva(inputData, filtro);

        validarIntervalosDentroDeIntervaloDaReserva(inputData.getIntervalos(), reserva);

        validarQuantidadeTotalDeNumerosDosIntervalosMenorIgualQuantidadeDeNumeroRestante(inputData.getIntervalos(), reserva);

        validarInterseccaoEntreIntervalos(inputData.getIntervalos());

        validarIntervalosEmUsoEmReservaOuPatrimonio(inputData.getIntervalos(), inputData.getReservaId());

        List<ReservaIntervalo> reservaIntervalos = criarIntervalosDaReserva(inputData.getIntervalos(), reserva);

        List<ReservaIntervalo> intervalosSalvos = salvarReservaIntervalo(reservaIntervalos);

        atualizarQuantidadeRestanteReserva(reserva, intervalosSalvos);

        salvarReserva(reserva);
    }

    private void validarDadosEntrada(CadastrarReservaIntervaloInputData inputData) {
        Validator.of(inputData)
                .validate(CadastrarReservaIntervaloInputData::getReservaId, Objects::nonNull, "Id da reserva é nulo")
                .validate(CadastrarReservaIntervaloInputData::getIntervalos, Objects::nonNull, "Lista de intervalos é nula")
                .get();
    }

    private void validarCamposPreenchidosCorretamente(List<CadastrarReservaIntervaloInputData.Intervalo> intervalos) {
        intervalos.forEach(this::validarCamposPreenchidos);
    }

    private void validarIntervalosComQuantidadeCorreta(List<CadastrarReservaIntervaloInputData.Intervalo> intervalos) {
        Boolean intervaloQuantidadeIncorreta = intervalos.stream().anyMatch(
                intervalo -> !intervalo.getQuantidadeReservada().equals(intervalo.getNumeroFim() - intervalo.getNumeroInicio() + 1));
        if (Boolean.TRUE.equals(intervaloQuantidadeIncorreta)) {
            throw new IntervaloComQuantidadeIncorretaException();
        }
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

    private Reserva buscarReserva(CadastrarReservaIntervaloInputData inputData, Reserva.Filtro filtro) {
        return reservaDataProvider.buscarPorId(inputData.getReservaId(), filtro).orElseThrow(ReservaNaoEncontradaException::new);
    }

    private void validarIntervalosDentroDeIntervaloDaReserva(List<CadastrarReservaIntervaloInputData.Intervalo> intervalos, Reserva reserva) {
        Long menorNumeroInicio = retornaMenorNumeroInicio(intervalos);
        Long maiorNumeroFim = retornaMaiorNumeroFim(intervalos);

        if (reserva.getNumeroInicio().compareTo(menorNumeroInicio) > 0) {
            throw new IntervaloComNumeroInicioAnteriorAReservaException();
        }

        if (reserva.getNumeroFim().compareTo(maiorNumeroFim) < 0) {
            throw new IntervaloComNumeroFimPosteriorAReservaException();
        }
    }

    private void validarQuantidadeTotalDeNumerosDosIntervalosMenorIgualQuantidadeDeNumeroRestante(List<CadastrarReservaIntervaloInputData.Intervalo> intervalos, Reserva reserva) {
        Long quantidadeTotalIntervalos = intervalos.stream().map(CadastrarReservaIntervaloInputData.Intervalo::getQuantidadeReservada).reduce(0L, Long::sum);

        if (quantidadeTotalIntervalos.compareTo(reserva.getQuantidadeRestante()) > 0) {
            throw new QuantidadeTotalDeNumerosDosIntervalosMaiorQueAQuantidadeRestanteException();
        }
    }

    private void validarInterseccaoEntreIntervalos(List<CadastrarReservaIntervaloInputData.Intervalo> intervalos) {
        List<CadastrarReservaIntervaloInputData.Intervalo> intervalosReduzido = new ArrayList<>(intervalos);

        intervalos.forEach(intervalo -> {
            intervalosReduzido.remove(0);
            intervalosReduzido.forEach(intervaloProximo -> validarInterseccao(intervalo, intervaloProximo));
        });
    }

    private void validarIntervalosEmUsoEmReservaOuPatrimonio(List<CadastrarReservaIntervaloInputData.Intervalo> intervalos, Long reservaId) {
        intervalos.forEach(intervalo -> validarIntervaloEmUso(intervalo.getNumeroInicio(), intervalo.getNumeroFim(), reservaId));
    }

    private List<ReservaIntervalo> criarIntervalosDaReserva(List<CadastrarReservaIntervaloInputData.Intervalo> intervalos, Reserva reserva) {
        List<ReservaIntervalo> reservaIntervalos = new ArrayList<>();
        AtomicReference<String> codigo = new AtomicReference<>(gerarCodigoIntervaloReserva());
        intervalos.forEach(intervalo -> {

            ReservaIntervalo reservaIntervalo = ReservaIntervalo
                    .builder()
                    .codigo(codigo.get())
                    .reserva(reserva)
                    .orgao(UnidadeOrganizacional.builder().id(intervalo.getOrgaoId()).build())
                    .situacao(ReservaIntervalo.Situacao.EM_ELABORACAO)
                    .preenchimento(ReservaIntervalo.Preenchimento.valueOf(intervalo.getPreenchimento()))
                    .quantidadeReservada(intervalo.getQuantidadeReservada())
                    .numeroInicio(intervalo.getNumeroInicio())
                    .numeroFim(intervalo.getNumeroFim())
                    .build();


            reservaIntervalos.add(reservaIntervalo);
            codigo.set(incrementarCodigoIntervaloReserva(codigo.get()));
        });

        return reservaIntervalos;
    }


    private String gerarCodigoIntervaloReserva() {
        Optional<ReservaIntervalo> reservaIntervalo = reservaIntervaloDataProvider.buscarReservaIntervaloComMaiorCodigo();

        if (reservaIntervalo.isPresent()) {
            String codigo = reservaIntervalo.get().getCodigo();
            if (Objects.nonNull(codigo)) {
                return incrementarCodigoIntervaloReserva(codigo);
            }
        }
        return String.format("%5d", 1).replace(" ", "0");
    }

    private String incrementarCodigoIntervaloReserva(String codigoAntigo) {
        Long proximoNumero = Long.parseLong(codigoAntigo) + 1;
        return String.format("%5d", proximoNumero).replace(" ", "0");
    }

    private List<ReservaIntervalo> salvarReservaIntervalo(List<ReservaIntervalo> reservaIntervalos) {
        return reservaIntervaloDataProvider.salvar(reservaIntervalos);
    }

    private void validarCamposPreenchidos(CadastrarReservaIntervaloInputData.Intervalo intervalo) {
        if (Objects.isNull(intervalo.getPreenchimento())) {
            throw new CampoNaoInformadoException("Preenchimento não informado.");
        }

        if (Objects.isNull(intervalo.getOrgaoId())) {
            throw new CampoNaoInformadoException("Órgão não informado.");
        }

        if (Objects.isNull(intervalo.getQuantidadeReservada())) {
            throw new CampoNaoInformadoException("A quantidade a ser reservada não foi informada");
        }

        if (intervalo.getQuantidadeReservada() <= 0) {
            throw new CampoNaoInformadoException("A quantidade a ser reservada deve ser maior que 0");
        }

        if (Objects.isNull(intervalo.getNumeroInicio()) || Objects.isNull(intervalo.getNumeroFim())) {
            throw new IntervaloNaoInformadoException();
        }

        if (intervalo.getNumeroInicio() <= 0 || (intervalo.getNumeroFim() < intervalo.getNumeroInicio())) {
            throw new IntervaloInvalidoException();
        }
    }

    private Long retornaMenorNumeroInicio(List<CadastrarReservaIntervaloInputData.Intervalo> intervalos) {
        CadastrarReservaIntervaloInputData.Intervalo intervaloMenorNumeroInicio = intervalos.stream().min(
                Comparator.comparing(CadastrarReservaIntervaloInputData.Intervalo::getNumeroInicio)).orElseThrow(NoSuchElementException::new);
        return intervaloMenorNumeroInicio.getNumeroInicio();
    }

    private Long retornaMaiorNumeroFim(List<CadastrarReservaIntervaloInputData.Intervalo> intervalos) {
        CadastrarReservaIntervaloInputData.Intervalo intervaloMaiorNumeroFim = intervalos.stream().min(
                Comparator.comparing(CadastrarReservaIntervaloInputData.Intervalo::getNumeroFim)).orElseThrow(NoSuchElementException::new);
        return intervaloMaiorNumeroFim.getNumeroFim();
    }

    private void validarInterseccao(CadastrarReservaIntervaloInputData.Intervalo intervalo, CadastrarReservaIntervaloInputData.Intervalo intervaloProximo) {
        if (validarValorEntreMinimoEMaximo(intervalo.getNumeroInicio(), intervaloProximo.getNumeroInicio(), intervaloProximo.getNumeroFim()) ||
                validarValorEntreMinimoEMaximo(intervalo.getNumeroFim(), intervaloProximo.getNumeroInicio(), intervaloProximo.getNumeroFim())) {
            throw new InterseccaoEntreIntervalosException();
        }
    }

    private void validarIntervaloEmUso(Long numeroInicial, Long numeroFinal, Long reservaId) {
        Patrimonio.Filtro filtro = Patrimonio
            .Filtro
                .builder()
                .numeroInicio(numeroInicial)
                .numeroFim(numeroFinal)
                .build();

        if (reservaDataProvider.existeEmIntervaloDeOutraReserva(numeroInicial, numeroFinal, reservaId) ||
                patrimonioDataProvider.existeEmIntervalo(filtro) ||
                reservaIntervaloDataProvider.existeIntervaloNaReserva(numeroInicial, numeroFinal, reservaId)) {
            throw new IntervaloEmUsoException();
        }
    }

    public boolean validarValorEntreMinimoEMaximo(Long valor, Long menorValor, Long maiorValor) {
        return valor >= menorValor && valor <= maiorValor;
    }

    private void atualizarQuantidadeRestanteReserva(Reserva reserva, List<ReservaIntervalo> intervalosSalvos) {
        final Long quantidadeUtilizada = intervalosSalvos.stream()
                .mapToLong(ReservaIntervalo::getQuantidadeReservada)
                .sum();

        reserva.setQuantidadeRestante(reserva.getQuantidadeRestante() - quantidadeUtilizada);
    }

    private void salvarReserva(Reserva reserva) {
        reservaDataProvider.salvar(reserva);
    }
}
