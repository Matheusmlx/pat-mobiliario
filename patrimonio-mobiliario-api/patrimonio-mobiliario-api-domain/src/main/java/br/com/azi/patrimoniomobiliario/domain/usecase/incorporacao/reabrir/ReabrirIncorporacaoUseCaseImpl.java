package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.reabrir;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfiguracaoDepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.reabrir.exception.IncorporacaoComPatrimoniosEmOutrasMovimentacoesException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.reabrir.exception.IncorporacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.reabrir.exception.ReabrirDuranteMesCorrenteException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.reabrir.exception.ReabrirIncorporacaoNaoFinalizadaException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class ReabrirIncorporacaoUseCaseImpl implements ReabrirIncorporacaoUseCase {

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final LancamentoContabilDataProvider lancamentoContabilDataProvider;

    private final ConfiguracaoDepreciacaoDataProvider configuracaoDepreciacaoDataProvider;

    @Override
    public void executar(ReabrirIncorporacaoInputData inputData) {
        validarDadosEntrada(inputData);

        Incorporacao incorporacao = buscar(inputData);

        validarMesCorrente(incorporacao);
        validarIncorporacaoFinalizada(incorporacao);
        validarPatrimoniosIncorporadosNaoAdicionadosEmOutrasMovimentacoes(incorporacao);

        reabrirIncorporacao(incorporacao);
        ajustarPatrimonios(incorporacao);
    }

    private void validarDadosEntrada(ReabrirIncorporacaoInputData inputData) {
        Validator.of(inputData)
            .validate(ReabrirIncorporacaoInputData::getIncorporacaoId, Objects::nonNull, "Id da incorporação é nulo")
            .get();
    }

    private Incorporacao buscar(ReabrirIncorporacaoInputData inputData) {
        return incorporacaoDataProvider.buscarPorId(inputData.getIncorporacaoId())
            .orElseThrow(IncorporacaoNaoEncontradaException::new);
    }

    private void validarMesCorrente(Incorporacao incorporacao) {
        LocalDateTime dataAtual = LocalDateTime.now();

        if (dataAtual.getMonth().compareTo(incorporacao.getDataFinalizacao().getMonth()) != 0 ||
            dataAtual.getYear() != incorporacao.getDataFinalizacao().getYear()) {
            throw new ReabrirDuranteMesCorrenteException();
        }
    }

    private void validarIncorporacaoFinalizada(Incorporacao incorporacao) {
        if (!Incorporacao.Situacao.FINALIZADO.equals(incorporacao.getSituacao())) {
            throw new ReabrirIncorporacaoNaoFinalizadaException();
        }
    }

    private void validarPatrimoniosIncorporadosNaoAdicionadosEmOutrasMovimentacoes(Incorporacao incorporacao) {
        final Long quantidadePatrimoniosEmOutrasMovimentacoes = patrimonioDataProvider
            .buscarQuantidadePatrimoniosEmOutrasMovimentacoesPorIncorporacaoId(incorporacao.getId());

        if (quantidadePatrimoniosEmOutrasMovimentacoes > 0L) {
            throw new IncorporacaoComPatrimoniosEmOutrasMovimentacoesException();
        }
    }

    private void reabrirIncorporacao(Incorporacao incorporacao) {
        incorporacao.setSituacao(Incorporacao.Situacao.EM_ELABORACAO);
        incorporacaoDataProvider.salvar(incorporacao);
    }

    private void ajustarPatrimonios(Incorporacao incorporacao) {
        final List<ItemIncorporacao> itens = buscarItens(incorporacao.getId());

        for (ItemIncorporacao itemIncorporacao : itens) {
            excluirConfiguracaoDepreciacao(itemIncorporacao.getConfiguracaoDepreciacao().getId());
            final List<Patrimonio> patrimonios = buscarPatrimonios(itemIncorporacao);

            for (Patrimonio patrimonio : patrimonios) {
                removerLancamentoContabil(patrimonio);
                setarPatrimonioEmElaboracao(patrimonio);
            }
        }
    }

    private List<ItemIncorporacao> buscarItens(Long incorporacaoId) {
        return itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(incorporacaoId);
    }

    private void excluirConfiguracaoDepreciacao(Long idConfiguracaoDepreciacao){
        configuracaoDepreciacaoDataProvider.excluir(idConfiguracaoDepreciacao);
    }

    private List<Patrimonio> buscarPatrimonios(ItemIncorporacao itemIncorporacao) {
        return patrimonioDataProvider.buscarPatrimoniosPorItemIncorporacaoId(itemIncorporacao.getId());
    }

    private void removerLancamentoContabil(Patrimonio patrimonio) {
        lancamentoContabilDataProvider.removerPorPatrimonio(patrimonio);
    }

    private void setarPatrimonioEmElaboracao(Patrimonio patrimonio) {
        patrimonio.setContaContabilAtual(null);
        patrimonio.setSituacao(null);
        patrimonioDataProvider.atualizar(patrimonio);
    }
}
