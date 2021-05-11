package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao;


import br.com.azi.patrimoniomobiliario.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.converter.EditarContaContabilOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.ConfigContaContabilNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.PercentualResidualExcedidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.PercentualResidualNaoNuloException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.PercentualResidualNuloException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.PercentualResidualZeroException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.TipoAssociadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.VidaUtilNaoNulaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.VidaUtilNulaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.VidaUtilZeroException;
import lombok.AllArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class EditarContaContabilUseCaseImpl implements EditarContaContabilUseCase {

    private ConfigContaContabilDataProvider configContaContabilDataProvider;

    private EditarContaContabilOutputDataConverter outputDataConverter;

    @Override
    public EditarContaContabilOutputData executar(EditarContaContabilInputData inputData) {
        verificarDadosEntrada(inputData);

        verificarTipoBemAssociado(inputData);

        if(verficarContaDepreciavel(inputData)){
            verificarVidaUtil(inputData);
            verificarPercentualResidual(inputData);
        }else {
            verificarVidaUtilNaoNula(inputData);
            verificarPercentualResidualNaoNula(inputData);
        }

        ConfigContaContabil configContaContabil;
        if(verificarSeConfigContaContabilExiste(inputData)){
            ConfigContaContabil configConta = buscaConfigContaContabil(inputData);
            configContaContabil = editaConfigAmortizacao(configConta, inputData);
        }else{
            configContaContabil = criaConfigAmortizacao(inputData);
        }

        ConfigContaContabil configContaContabilAtualizada = atualizar(configContaContabil);

        return outputDataConverter.to(configContaContabilAtualizada);
    }

    private void verificarDadosEntrada(EditarContaContabilInputData input) {
        Validator.of(input)
            .validate(EditarContaContabilInputData::getTipo, Objects::nonNull, "Tipo da Conta não pode ser nulo")
            .validate(EditarContaContabilInputData::getTipoBem, Objects::nonNull, "Tipo de bem associado não pode ser nulo")
            .validate(EditarContaContabilInputData::getContaContabil, Objects::nonNull, "A conta contábil não pode ser nula")
            .get();
    }

    private boolean verficarContaDepreciavel(EditarContaContabilInputData inputData) {
        return inputData.getTipo().equals(ConfigContaContabil.Tipo.DEPRECIAVEL.name());
    }

    private void verificarVidaUtil(EditarContaContabilInputData inputData) {
        verificarVidaUtilNula(inputData);
        verificarVidaUtilZero(inputData);
    }

    private void verificarVidaUtilNula(EditarContaContabilInputData inputData) {
        if(Objects.isNull(inputData.getVidaUtil())){
            throw new VidaUtilNulaException();
        }
    }

    private void verificarVidaUtilZero(EditarContaContabilInputData inputData) {
        if(inputData.getVidaUtil().compareTo(0) == 0){
            throw new VidaUtilZeroException();
        }
    }

    private void verificarPercentualResidual(EditarContaContabilInputData inputData) {
        verificarPercentualResidualNula(inputData);
        verificarPercentualResidualExcedido(inputData);
        verificarPercentualResidualZero(inputData);
    }

    private void verificarPercentualResidualNula(EditarContaContabilInputData inputData) {
        if(Objects.isNull(inputData.getPercentualResidual())){
            throw new PercentualResidualNuloException();
        }
    }

    private void verificarPercentualResidualExcedido(EditarContaContabilInputData inputData) {
        if (inputData.getPercentualResidual().compareTo(BigDecimal.valueOf(99.99)) > 0) {
            throw new PercentualResidualExcedidoException();
        }
    }

    private void verificarPercentualResidualZero(EditarContaContabilInputData inputData) {
        if(inputData.getPercentualResidual().compareTo(BigDecimal.valueOf(0)) == 0 ||
            inputData.getPercentualResidual().compareTo(BigDecimal.valueOf(0)) < 0) {
            throw new PercentualResidualZeroException();
        }
    }

    private void verificarVidaUtilNaoNula(EditarContaContabilInputData inputData) {
        if(!Objects.isNull(inputData.getVidaUtil())){
            throw new VidaUtilNaoNulaException();
        }
    }

    private void verificarPercentualResidualNaoNula(EditarContaContabilInputData inputData) {
        if(!Objects.isNull(inputData.getPercentualResidual())){
            throw new PercentualResidualNaoNuloException();
        }
    }

    private void verificarTipoBemAssociado(EditarContaContabilInputData inputData) {
        if (!ObjectUtils.containsConstant(ConfigContaContabil.Tipo.values(), inputData.getTipo(), true)) {
            throw new TipoAssociadoException();
        }
    }

    private Boolean verificarSeConfigContaContabilExiste(EditarContaContabilInputData inputData){
        return configContaContabilDataProvider.existePorContaContabil(inputData.getContaContabil());
    }

    private ConfigContaContabil buscaConfigContaContabil(EditarContaContabilInputData inputData){
        Optional<ConfigContaContabil> configContaContabil = configContaContabilDataProvider.buscarAtualPorContaContabil(inputData.getContaContabil());
        return configContaContabil.orElseThrow(ConfigContaContabilNaoEncontradaException::new);
    }

    private ConfigContaContabil editaConfigAmortizacao(ConfigContaContabil configContaContabil, EditarContaContabilInputData inputData) {
        configContaContabil.setTipo(ConfigContaContabil.Tipo.valueOf(inputData.getTipo()));
        configContaContabil.setTipoBem(ConfigContaContabil.TipoBem.valueOf(inputData.getTipoBem()));
        configContaContabil.setMetodo(retornaMetodoContaContabilPorTipo(ConfigContaContabil.Tipo.valueOf(inputData.getTipo())));
        configContaContabil.setPercentualResidual(inputData.getPercentualResidual());
        configContaContabil.setVidaUtil(inputData.getVidaUtil());
        return configContaContabil;
    }

    private ConfigContaContabil criaConfigAmortizacao(EditarContaContabilInputData inputData) {
        return ConfigContaContabil
            .builder()
            .tipo(ConfigContaContabil.Tipo.valueOf(inputData.getTipo()))
            .tipoBem(ConfigContaContabil.TipoBem.valueOf(inputData.getTipoBem()))
            .situacao(ConfigContaContabil.Situacao.ATIVO)
            .metodo(retornaMetodoContaContabilPorTipo(ConfigContaContabil.Tipo.valueOf(inputData.getTipo())))
            .contaContabil(ContaContabil.builder().id(inputData.getContaContabil()).build())
            .percentualResidual(inputData.getPercentualResidual())
            .vidaUtil(inputData.getVidaUtil())
            .build();
    }

    private ConfigContaContabil.Metodo retornaMetodoContaContabilPorTipo(ConfigContaContabil.Tipo tipo) {
        if (ConfigContaContabil.Tipo.DEPRECIAVEL.equals(tipo)) {
            return ConfigContaContabil.Metodo.QUOTAS_CONSTANTES;
        }
        return null;
    }

    private ConfigContaContabil atualizar(ConfigContaContabil configContaContabil) {
        return configContaContabilDataProvider.atualizar(configContaContabil);
    }
}
