package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ConfiguracaoDepreciacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfiguracaoDepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid.exception.IncorporacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid.exception.ItemIncorporacaoNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.converter.BuscarPatrimonioPorIdOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.exception.ConfiguracaoDepreciacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.exception.PatrimonioNaoEncontradoException;
import lombok.AllArgsConstructor;
import org.springframework.util.Base64Utils;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BuscarPatrimonioPorIdUseCaseImpl implements BuscarPatrimonioPorIdUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private IncorporacaoDataProvider incorporacaoDataProvider;

    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    private ConfigContaContabilDataProvider configContaContabilDataProvider;

    private ConfiguracaoDepreciacaoDataProvider configuracaoDepreciacaoDataProvider;

    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    private BuscarPatrimonioPorIdOutputDataConverter outputDataConverter;


    @Override
    public BuscarPatrimonioPorIdOutputData executar(BuscarPatrimonioPorIdInputData inputData) {
        validarDadosEntrada(inputData);
        Patrimonio patrimonio = buscarPatrimonio(inputData);
        ItemIncorporacao itemIncorporacao = buscarItemIncorporacao(patrimonio);
        Incorporacao incorporacao = buscarIncorporacao(itemIncorporacao);
        ConfigContaContabil configConta = buscaConfigContaContabil(patrimonio);
        ConfiguracaoDepreciacao configuracaoDepreciacao = buscarConfiguracaoDepreciacao(itemIncorporacao.getConfiguracaoDepreciacao().getId());

        setarContaContabil(patrimonio, configConta);
        carregarImagem(patrimonio);

        return outputDataConverter.to(patrimonio, itemIncorporacao, incorporacao,configuracaoDepreciacao);
    }

    private void validarDadosEntrada(BuscarPatrimonioPorIdInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarPatrimonioPorIdInputData::getId, Objects::nonNull, "O id é nulo")
            .get();
    }

    private Patrimonio buscarPatrimonio(BuscarPatrimonioPorIdInputData inputData) {
        Optional<Patrimonio> patrimonio = patrimonioDataProvider.buscarPorId(inputData.getId());
        return patrimonio.orElseThrow(PatrimonioNaoEncontradoException::new);
    }

    private ItemIncorporacao buscarItemIncorporacao(Patrimonio patrimonio) {
        Optional<ItemIncorporacao> itemIncorporacao = itemIncorporacaoDataProvider.buscarPorId(patrimonio.getItemIncorporacao().getId());
        return itemIncorporacao.orElseThrow(ItemIncorporacaoNaoEncontradoException::new);
    }

    private Incorporacao buscarIncorporacao(ItemIncorporacao itemIncorporacao) {
        Optional<Incorporacao> incorporacao = incorporacaoDataProvider.buscarPorId(itemIncorporacao.getIdIncorporacao());
        return incorporacao.orElseThrow(IncorporacaoNaoEncontradaException::new);
    }

    private ConfigContaContabil buscaConfigContaContabil(Patrimonio patrimonio) {
        Optional<ConfigContaContabil> configContaContabil = configContaContabilDataProvider.buscarAtualPorContaContabil(patrimonio.getContaContabilClassificacao().getId());

        return configContaContabil.orElse(null);
    }

    private ConfiguracaoDepreciacao buscarConfiguracaoDepreciacao(Long idConfiguracaoDepreciacao) {
        Optional<ConfiguracaoDepreciacao> configuracaoDepreciacaoOptional = configuracaoDepreciacaoDataProvider.buscarPorId(idConfiguracaoDepreciacao);
        return configuracaoDepreciacaoOptional.orElseThrow(ConfiguracaoDepreciacaoNaoEncontradaException::new);
    }

    private void setarContaContabil(Patrimonio patrimonio, ConfigContaContabil configConta) {
        patrimonio.setContaContabilClassificacao(ContaContabil.builder()
            .id(patrimonio.getContaContabilClassificacao().getId())
            .situacao(patrimonio.getContaContabilClassificacao().getSituacao())
            .codigo(patrimonio.getContaContabilClassificacao().getCodigo())
            .descricao(patrimonio.getContaContabilClassificacao().getDescricao())
            .configContaContabil(configConta)
            .build()
        );
    }

    private void carregarImagem(Patrimonio entidade) {
        if (Objects.nonNull(entidade.getUriImagem())) {
            Arquivo arquivo = sistemaDeArquivosIntegration.download(entidade.getUriImagem());
            if (Objects.nonNull(arquivo.getContent())) {
                entidade.setImagem(Base64Utils.encodeToString(arquivo.getContent()));
            }
        }
    }
}
