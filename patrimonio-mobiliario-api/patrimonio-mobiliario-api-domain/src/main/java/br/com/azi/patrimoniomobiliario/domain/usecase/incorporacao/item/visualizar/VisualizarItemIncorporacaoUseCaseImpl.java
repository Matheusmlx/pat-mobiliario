package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.visualizar;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.visualizar.converter.VisualizarItemIncorporacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.visualizar.exception.ItemIncorporacaoNaoEncontradaException;
import lombok.AllArgsConstructor;
import org.springframework.util.Base64Utils;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class VisualizarItemIncorporacaoUseCaseImpl implements VisualizarItemIncorporacaoUseCase {

    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    private VisualizarItemIncorporacaoOutputDataConverter outputDataConverter;

    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Override
    public VisualizarItemIncorporacaoOutputData executar(VisualizarItemIncorporacaoInputData inputData) {
        validarDadosEntrada(inputData);

        ItemIncorporacao itemIncorporacaoEncontrado = buscar(inputData);
        carregarImagem(itemIncorporacaoEncontrado);

        return outputDataConverter.to(itemIncorporacaoEncontrado);
    }

    private void validarDadosEntrada(VisualizarItemIncorporacaoInputData inputData) {
        Validator.of(inputData)
            .validate(VisualizarItemIncorporacaoInputData::getId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private ItemIncorporacao buscar(VisualizarItemIncorporacaoInputData inputData) {
        Optional<ItemIncorporacao> entidade = itemIncorporacaoDataProvider.buscarPorId(inputData.getId());
        return entidade.orElseThrow(ItemIncorporacaoNaoEncontradaException::new);
    }

    private void carregarImagem(ItemIncorporacao entidade){
        if(Objects.nonNull(entidade.getUriImagem())){
            Arquivo arquivo = sistemaDeArquivosIntegration.download(entidade.getUriImagem());
            if(Objects.nonNull(arquivo.getContent())){
                entidade.setImagem(Base64Utils.encodeToString(arquivo.getContent()));
            }
        }
    }
}
