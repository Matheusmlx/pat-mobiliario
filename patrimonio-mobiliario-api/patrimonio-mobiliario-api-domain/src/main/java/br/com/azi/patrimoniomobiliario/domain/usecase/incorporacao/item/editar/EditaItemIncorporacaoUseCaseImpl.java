package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.EstadoConservacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NaturezaDespesa;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NaturezaDespesaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.converter.EditaItemIncorporacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception.ConfigContaContabilException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception.ContaContabilException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception.IncorporacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception.IncorporacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception.ItemComMesmaNaturezaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception.ItemNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception.ItemNaoExisteException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class EditaItemIncorporacaoUseCaseImpl implements EditaItemIncorporacaoUseCase {

    private final ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final ContaContabilDataProvider contaContabilDataProvider;

    private final ConfigContaContabilDataProvider configContaContabilDataProvider;

    private final NaturezaDespesaDataProvider naturezaDespesaDataProvider;

    private final SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    private final EditaItemIncorporacaoOutputDataConverter outputDataConverter;

    @Override
    public EditaItemIncorporacaoOutputData executar(EditaItemIncorporacaoInputData inputData) {
        validarDadosEntrada(inputData);
        verificarSeItemExiste(inputData);
        verificarSeExisteItemComMesmaNatureza(inputData);

        ItemIncorporacao item = buscar(inputData);
        Incorporacao incorporacao = buscarIncorporacao(item.getIncorporacao().getId());
        validarIncorporacaoEmModoElaboracao(incorporacao);

        removerArquivoTemporario(item, inputData);
        promoverArquivoTemporario(item.getUriImagem(), inputData.getUriImagem());
        setarDados(item, inputData);

        ItemIncorporacao itemIncorporacao = atualizar(item);

        return outputDataConverter.to(itemIncorporacao);
    }

    private void validarDadosEntrada(EditaItemIncorporacaoInputData inputData) {
        Validator.of(inputData)
            .validate(EditaItemIncorporacaoInputData::getId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private void verificarSeItemExiste(EditaItemIncorporacaoInputData inputData) {
        if (!itemIncorporacaoDataProvider.existe(inputData.getId())) {
            throw new ItemNaoExisteException();
        }
    }

    private void verificarSeExisteItemComMesmaNatureza(EditaItemIncorporacaoInputData inputData) {
        if (Objects.nonNull(inputData.getNaturezaDespesa())) {
            Boolean existeItem = itemIncorporacaoDataProvider.existeItemComMesmaNatureza(inputData);
            if (existeItem) {
                throw new ItemComMesmaNaturezaException();
            }
        }
    }

    private ItemIncorporacao buscar(EditaItemIncorporacaoInputData inputData) {
        Optional<ItemIncorporacao> itemIncorporacao = itemIncorporacaoDataProvider.buscarPorId(inputData.getId());
        return (itemIncorporacao.orElseThrow(ItemNaoEncontradoException::new));
    }

    private Incorporacao buscarIncorporacao(Long incorporacaoId) {
        return incorporacaoDataProvider.buscarPorId(incorporacaoId)
            .orElseThrow(IncorporacaoNaoEncontradaException::new);
    }

    private void validarIncorporacaoEmModoElaboracao(Incorporacao incorporacao) {
        if (!Incorporacao.Situacao.EM_ELABORACAO.equals(incorporacao.getSituacao()) &&
            !Incorporacao.Situacao.ERRO_PROCESSAMENTO.equals(incorporacao.getSituacao())) {
            throw new IncorporacaoNaoEstaEmElaboracaoException();
        }
    }

    private void removerArquivoTemporario(ItemIncorporacao item, EditaItemIncorporacaoInputData inputData) {
        if (Objects.isNull(inputData.getUriImagem()) && Objects.nonNull(item.getUriImagem())) {
            sistemaDeArquivosIntegration.removeDefinitiveFile(Arquivo.builder().uri(item.getUriImagem()).build());
        }
    }

    private void promoverArquivoTemporario(String uriAnexo, String uriInput) {
        if (Objects.nonNull(uriInput) && !uriInput.equals(uriAnexo)) {
            sistemaDeArquivosIntegration.promote(Arquivo.builder().uri(uriInput).build());
        }
    }

    private void setarDados(ItemIncorporacao item, EditaItemIncorporacaoInputData inputData) {

        if (Objects.nonNull(inputData.getCodigo())) {
            item.setCodigo(inputData.getCodigo());
        }

        if (Objects.nonNull(inputData.getDescricao())) {
            item.setDescricao(inputData.getDescricao());
        }

        if (Objects.nonNull(inputData.getId())) {
            item.setId(inputData.getId());
        }

        if (Objects.nonNull(inputData.getIdIncorporacao())) {
            item.setIdIncorporacao(inputData.getIdIncorporacao());
        }

        if (Objects.nonNull(inputData.getMarca())) {
            item.setMarca(inputData.getMarca());
        } else {
            item.setMarca(null);
        }
        if (Objects.nonNull(inputData.getModelo())) {
            item.setModelo(inputData.getModelo());
        } else {
            item.setModelo(null);
        }

        if (Objects.nonNull(inputData.getFabricante())) {
            item.setFabricante(inputData.getFabricante());
        } else {
            item.setFabricante(null);
        }

        if (Objects.nonNull(inputData.getQuantidade())) {
            item.setQuantidade(inputData.getQuantidade());
        }

        if (Objects.nonNull(inputData.getValorTotal())) {
            item.setValorTotal(inputData.getValorTotal());
        }

        if (Objects.nonNull(inputData.getSituacao())) {
            item.setSituacao(ItemIncorporacao.Situacao.valueOf(inputData.getSituacao()));
        }

        if (Objects.nonNull(inputData.getNumeracaoPatrimonial())) {
            item.setNumeracaoPatrimonial(ItemIncorporacao.NumeracaoPatrimoninal.valueOf(inputData.getNumeracaoPatrimonial()));
        }

        if (Objects.nonNull(inputData.getEstadoConservacao())) {
            item.setEstadoConservacao(EstadoConservacao.builder().id(inputData.getEstadoConservacao()).build());
        }

        if (Objects.nonNull(inputData.getUriImagem())) {
            item.setUriImagem(inputData.getUriImagem());
        } else {
            item.setUriImagem(null);
        }

        if (Objects.nonNull(inputData.getAnoFabricacaoModelo())) {
            item.setAnoFabricacaoModelo(inputData.getAnoFabricacaoModelo());
        } else {
            item.setAnoFabricacaoModelo(null);
        }

        if (Objects.nonNull(inputData.getCombustivel())) {
            item.setCombustivel(ItemIncorporacao.Combustivel.valueOf(inputData.getCombustivel()));
        }

        if (Objects.nonNull(inputData.getCategoria())) {
            item.setCategoria(ItemIncorporacao.Categoria.valueOf(inputData.getCategoria()));
        }

        if (Objects.nonNull(inputData.getNaturezaDespesa())) {
            item.setNaturezaDespesa(NaturezaDespesa
                .builder()
                .id(inputData.getNaturezaDespesa())
                .build());

            atualizaContaContabil(item);
        }

    }

    private NaturezaDespesa buscarNaturezaDespesa(Long id) {
        Optional<NaturezaDespesa> naturezaDespesa = naturezaDespesaDataProvider.buscarPorId(id);
        return naturezaDespesa.orElseThrow(ContaContabilException::new);
    }

    private void atualizaContaContabil(ItemIncorporacao item) {
        NaturezaDespesa naturezaDespesa = buscarNaturezaDespesa(item.getNaturezaDespesa().getId());
        ContaContabil contaContabil = buscarContaContabilPorNaturezaDespesa(naturezaDespesa);
        ConfigContaContabil configContaContabil = buscarConfiguracaoContaContabil(contaContabil);
        setarContaContabil(item, contaContabil);
        setarTipo(item, configContaContabil);
    }

    private ContaContabil buscarContaContabilPorNaturezaDespesa(NaturezaDespesa naturezaDespesa) {
        Optional<ContaContabil> contaContabil = contaContabilDataProvider.buscarPorId(naturezaDespesa.getContaContabil().getId());
        return contaContabil.orElseThrow(ContaContabilException::new);
    }

    private void setarContaContabil(ItemIncorporacao item, ContaContabil contaContabil) {
        item.setContaContabil(
            ContaContabil.builder()
                .id(contaContabil.getId())
                .codigo(contaContabil.getCodigo())
                .descricao(contaContabil.getDescricao())
                .build());
    }

    private ConfigContaContabil buscarConfiguracaoContaContabil(ContaContabil contaContabil) {
        Optional<ConfigContaContabil> configContaContabil = configContaContabilDataProvider.buscarAtualPorContaContabil(contaContabil.getId());
        return configContaContabil.orElseThrow(ConfigContaContabilException::new);
    }

    private void setarTipo(ItemIncorporacao item, ConfigContaContabil configContaContabil) {
        item.setTipoBem(String.valueOf(configContaContabil.getTipoBem()));
    }

    private ItemIncorporacao atualizar(ItemIncorporacao item) {
        return itemIncorporacaoDataProvider.salvar(item);
    }


}
