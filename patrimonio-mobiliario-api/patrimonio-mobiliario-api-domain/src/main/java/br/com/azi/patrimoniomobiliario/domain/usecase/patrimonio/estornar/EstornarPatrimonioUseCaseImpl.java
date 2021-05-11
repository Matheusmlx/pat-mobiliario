package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.estornar;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.LancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.estornar.exception.IncorporacaoComPatrimoniosEmOutrasMovimentacoesException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.estornar.exception.IncorporacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.estornar.exception.IncorporacaoNaoPodeSerEstornadaException;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class EstornarPatrimonioUseCaseImpl implements EstornarPatrimonioUseCase {

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final LancamentoContabilDataProvider lancamentoContabilDataProvider;

    @Override
    public void executar(EstornarPatrimonioInputData inputData) {
        validarDadosEntrada(inputData);
        List<Patrimonio> patrimonios = buscarPatrimonios(inputData);
        List<ItemIncorporacao> itensIncorporacao = buscarItensIncorporacao(inputData);

        Incorporacao incorporacao = buscarIncorporacao(inputData);
        validarSituacaoIncorporacaoParaEstornarPatrimonio(incorporacao);
        validarPatrimoniosIncorporadosNaoAdicionadosEmOutrasMovimentacoes(incorporacao.getId());

        atualizarPatrimonios(patrimonios, inputData, incorporacao);
        atualizarItensIncorporacao(patrimonios, itensIncorporacao);
        atualizarSituacaoIncorporacao(inputData, incorporacao);
    }

    private void validarDadosEntrada(EstornarPatrimonioInputData inputData) {
        Validator.of(inputData)
            .validate(EstornarPatrimonioInputData::getPatrimoniosId, Objects::nonNull, "Lista de itens é nula")
            .validate(EstornarPatrimonioInputData::getIncorporacaoId, Objects::nonNull, "A incorporação é nula")
            .validate(EstornarPatrimonioInputData::getMotivo, Objects::nonNull, "O motivo é nulo")
            .validate(EstornarPatrimonioInputData::getUsuario, Objects::nonNull, "O usuário é nulo")
            .validate(EstornarPatrimonioInputData::getData, Objects::nonNull, "A data é nula")
            .get();
    }

    private List<Patrimonio> buscarPatrimonios(EstornarPatrimonioInputData inputData) {
        if (Boolean.TRUE.equals(inputData.getTodosSelecionados())) {
            return patrimonioDataProvider.buscarPatrimoniosAtivosPorIncorporacaoId(inputData.getIncorporacaoId(), inputData.getPatrimoniosExcecao());
        }
        return patrimonioDataProvider.buscarTodosPatrimonios(inputData.getPatrimoniosId());
    }

    private List<ItemIncorporacao> buscarItensIncorporacao(EstornarPatrimonioInputData inputData) {
        return itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(inputData.getIncorporacaoId());
    }

    private Incorporacao buscarIncorporacao(EstornarPatrimonioInputData inputData) {
        Optional<Incorporacao> incorporacao = incorporacaoDataProvider.buscarPorId(inputData.getIncorporacaoId());
        return incorporacao.orElseThrow(IncorporacaoNaoEncontradaException::new);
    }

    private void validarSituacaoIncorporacaoParaEstornarPatrimonio(Incorporacao incorporacao) {
        if (!Incorporacao.Situacao.FINALIZADO.equals(incorporacao.getSituacao()) &&
            !Incorporacao.Situacao.PARCIALMENTE_ESTORNADO.equals(incorporacao.getSituacao())) {
            throw new IncorporacaoNaoPodeSerEstornadaException();
        }
    }

    private void validarPatrimoniosIncorporadosNaoAdicionadosEmOutrasMovimentacoes(Long incorporacaoId) {
        final Long quantidadePatrimoniosEmOutrasMovimentacoes = patrimonioDataProvider
            .buscarQuantidadePatrimoniosEmOutrasMovimentacoesPorIncorporacaoId(incorporacaoId);

        if (quantidadePatrimoniosEmOutrasMovimentacoes > 0L) {
            throw new IncorporacaoComPatrimoniosEmOutrasMovimentacoesException();
        }
    }

    private void atualizarPatrimonios(List<Patrimonio> patrimonios, EstornarPatrimonioInputData inputData, Incorporacao incorporacao) {
        for (Patrimonio patrimonio : patrimonios) {
            patrimonio.setSituacao(Patrimonio.Situacao.ESTORNADO);
            patrimonio.setMotivoEstorno(inputData.getMotivo());
            patrimonio.setUsuarioEstorno(inputData.getUsuario());
            patrimonio.setDataEstorno(DateUtils.asLocalDateTime(inputData.getData()));
            gerarLancamentoContabil(patrimonio, incorporacao);
        }

        patrimonioDataProvider.salvar(patrimonios);
    }

    private void atualizarItensIncorporacao(List<Patrimonio> patrimonios, List<ItemIncorporacao> itensIncorporacao) {
        for (ItemIncorporacao itemIncorporacao : itensIncorporacao) {
            for (Patrimonio patrimonio : patrimonios) {
                if (patrimonio.getItemIncorporacao().getId().equals(itemIncorporacao.getId())) {
                    itemIncorporacao.setQuantidade(itemIncorporacao.getQuantidade() - 1);
                    itemIncorporacao.setValorTotal(itemIncorporacao.getValorTotal().subtract(patrimonio.getValorAquisicao()));
                }
            }
        }
        itemIncorporacaoDataProvider.salvarTodos(itensIncorporacao);
    }

    private void atualizarSituacaoIncorporacao(EstornarPatrimonioInputData inputData, Incorporacao incorporacao) {
        Long quantidadeTotalPatrimonios = retornaQuantidadeTotalPatrimoniosAtivos(inputData);
        if (quantidadeTotalPatrimonios.equals(0L)) {
            incorporacao.setSituacao(Incorporacao.Situacao.ESTORNADO);
        } else {
            incorporacao.setSituacao(Incorporacao.Situacao.PARCIALMENTE_ESTORNADO);
        }
        incorporacaoDataProvider.salvar(incorporacao);
    }

    private Long retornaQuantidadeTotalPatrimoniosAtivos(EstornarPatrimonioInputData inputData) {
        return patrimonioDataProvider.buscarQuantidadePatrimoniosAtivosPorIncorporacaoId(inputData.getIncorporacaoId());
    }

    private void gerarLancamentoContabil(Patrimonio patrimonio, Incorporacao incorporacao) {
        LancamentoContabil lancamentoContabil = LancamentoContabil
            .builder()
            .tipoLancamento(LancamentoContabil.TipoLancamento.DEBITO)
            .valor(patrimonio.getValorAquisicao())
            .dataLancamento(LocalDateTime.now())
            .tipoMovimentacao(LancamentoContabil.TipoMovimentacao.INCORPORACAO)
            .orgao(patrimonio.getOrgao())
            .setor(patrimonio.getSetor())
            .contaContabil(patrimonio.getContaContabilAtual())
            .patrimonio(patrimonio)
            .incorporacao(incorporacao)
            .build();

        lancamentoContabilDataProvider.salvar(lancamentoContabil);
    }
}
