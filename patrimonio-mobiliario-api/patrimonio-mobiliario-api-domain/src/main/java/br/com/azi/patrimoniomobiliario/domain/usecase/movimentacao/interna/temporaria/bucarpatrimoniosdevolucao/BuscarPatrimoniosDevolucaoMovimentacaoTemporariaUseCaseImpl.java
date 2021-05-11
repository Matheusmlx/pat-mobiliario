package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.exception.FiltroIncompletoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.converter.BuscarPatrimoniosDevolucaoMovimentacaoTemporariaFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.converter.BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.exception.MovimentacaoNaoEstaAguardandoDevolucaoException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BuscarPatrimoniosDevolucaoMovimentacaoTemporariaUseCaseImpl implements BuscarPatrimoniosDevolucaoMovimentacaoTemporariaUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final BuscarPatrimoniosDevolucaoMovimentacaoTemporariaFiltroConverter filtroConverter;

    private final BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputDataConverter converter;

    @Override
    public BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData executar(BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData inputData) {
        validarDadosEntrada(inputData);

        Movimentacao movimentacao = buscarMovimentacao(inputData.getMovimentacaoId());
        validarSeMovimentacaoAguardandoDevolucao(movimentacao);

        Patrimonio.Filtro filtro = criarFiltro(inputData);
        ListaPaginada<Patrimonio> itens = buscarPatrimoniosAguardandoDevolucao(filtro, movimentacao);

        return converter.to(itens);
    }

    private void validarDadosEntrada(BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData::getMovimentacaoId, Objects::nonNull, "Id da movimentação é nulo.")
            .validate(BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData::getPage, page -> Objects.nonNull(page) && page >= 0, new FiltroIncompletoException("Ausência do número da página."))
            .validate(BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData::getSize, size -> Objects.nonNull(size) && size > 0, new FiltroIncompletoException("Ausência do tamanho da página."))
            .get();
    }

    private Movimentacao buscarMovimentacao(Long movimentacaoId) {
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(movimentacaoId);
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void validarSeMovimentacaoAguardandoDevolucao(Movimentacao movimentacao) {
        if (Movimentacao.Situacao.DEVOLVIDO.equals(movimentacao.getSituacao())) {
            throw new MovimentacaoNaoEstaAguardandoDevolucaoException();
        }
    }

    private Patrimonio.Filtro criarFiltro(BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData inputData) {
        return filtroConverter.to(inputData);
    }

    private ListaPaginada<Patrimonio> buscarPatrimoniosAguardandoDevolucao(Patrimonio.Filtro filtro, Movimentacao movimentacao) {
        return patrimonioDataProvider.buscarPatrimoniosAguardandoDevolucaoPorMovimentacao(filtro, movimentacao.getId());
    }

}
