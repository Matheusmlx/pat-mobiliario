package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception.ItemNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.converter.EditarPatrimonioOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.ChassiCadastradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.ChassiInvalidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.MotorCadastradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.NumeroSerieCadastradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.PatrimonioEstornadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.PatrimonioNaoExisteException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.PlacaCadastradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.PlacaInvalidaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.RenavamCadastradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.RenavamInvalidoException;
import br.com.azi.patrimoniomobiliario.utils.validate.VehicleValidate;
import lombok.AllArgsConstructor;
import org.springframework.util.Base64Utils;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class EditarPatrimonioUseCaseImpl implements EditarPatrimonioUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private EditarPatrimonioOutputDataConverter outputDataConverter;

    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Override
    public EditarPatrimonioOutputData executar(EditarPatrimonioInputData inputData) {
        validarDadosEntrada(inputData);
        validarDuplicacaoDePatrimonio(inputData);
        validarCampos(inputData);

        Patrimonio patrimonio = buscar(inputData);
        ItemIncorporacao item = buscarItem(patrimonio);
        removerArquivoTemporario(patrimonio.getUriImagem(), inputData.getUriImagem(), item.getUriImagem());
        promoverArquivoTemporario(patrimonio.getUriImagem(), inputData.getUriImagem(), item.getUriImagem());
        validarCamposJaCadastrados(inputData, patrimonio);
        setarDados(inputData, patrimonio);

        Patrimonio patrimonioAtualizado = atualizar(patrimonio);
        carregarImagem(patrimonioAtualizado);
        return outputDataConverter.to(patrimonioAtualizado);
    }

    private void validarDadosEntrada(EditarPatrimonioInputData inputData) {
        Validator.of(inputData)
            .validate(EditarPatrimonioInputData::getId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private void validarDuplicacaoDePatrimonio(EditarPatrimonioInputData inputData) {
        if(!patrimonioDataProvider.existe(inputData.getId())) {
            throw new PatrimonioNaoExisteException();
        }
    }

    private void validarCampos(EditarPatrimonioInputData inputData) {
        validarPlaca(inputData.getPlaca());
        validarRenavam(inputData.getRenavam());
        validarChassi(inputData.getChassi());
    }

    private void validarCamposJaCadastrados(EditarPatrimonioInputData inputData, Patrimonio patrimonio) {
        validarSeNumeroSerieJaCadastrado(inputData.getNumeroSerie(), patrimonio.getNumeroSerie(), inputData.getId());
        validarSeMotorJaCadastrado(inputData.getMotor(), patrimonio.getMotor(), inputData.getId());
        validarSePlacaJaCadastrada(inputData.getPlaca(), inputData.getId());
        validarSeRenavamJaCadastrado(inputData.getRenavam(), patrimonio.getRenavam(), inputData.getId());
        validarSeChassiJaCadastrado(inputData.getChassi(), patrimonio.getChassi(), inputData.getId());
    }

    private Patrimonio buscar(EditarPatrimonioInputData inputData) {
        Optional<Patrimonio> patrimonioOptional = patrimonioDataProvider.buscarPorId(inputData.getId());
        Patrimonio patrimonio = patrimonioOptional.orElseThrow(PatrimonioNaoEncontradoException::new);
        validaSePatrimonioEstornado(patrimonio);
        return patrimonio;
    }

    private ItemIncorporacao buscarItem(Patrimonio patrimonio) {
        Optional<ItemIncorporacao> itemIncorporacao = itemIncorporacaoDataProvider.buscarPorId(patrimonio.getItemIncorporacao().getId());
        return (itemIncorporacao.orElseThrow(ItemNaoEncontradoException::new));
    }

    private void setarDados(EditarPatrimonioInputData source, Patrimonio target) {

        target.setUriImagem(source.getUriImagem());
        target.setPlaca(retornaStringSemTracoEspaco(source.getPlaca()));
        target.setRenavam(retornaStringSemTracoEspaco(source.getRenavam()));
        target.setLicenciamento(retornaStringSemTracoEspaco(source.getLicenciamento()));
        target.setMotor(retornaStringSemTracoEspaco(source.getMotor()));
        target.setChassi(retornaStringSemTracoEspaco(source.getChassi()));
        target.setNumeroSerie(retornaStringSemTracoEspaco(source.getNumeroSerie()));
    }

    private void removerArquivoTemporario(String uriAnexo, String uriInput, String uriItem) {
        if (Objects.isNull(uriInput) && Objects.nonNull(uriAnexo) && Objects.nonNull(uriItem) && !uriItem.equals(uriAnexo)) {
            sistemaDeArquivosIntegration.removeDefinitiveFile(Arquivo.builder().uri(uriAnexo).build());
        }
    }

    private void removerArquivoAtual(String uriAnexo){
        sistemaDeArquivosIntegration.removeDefinitiveFile(Arquivo.builder().uri(uriAnexo).build());
    }

    private void promoverArquivoTemporario(String uriAnexo, String uriInput, String uriItem) {
        if(Objects.nonNull(uriAnexo) && Objects.nonNull(uriItem) &&  !uriItem.equals(uriAnexo)){
            removerArquivoAtual(uriAnexo);
        }
        if (Objects.nonNull(uriInput)) {
            sistemaDeArquivosIntegration.promote(Arquivo.builder().uri(uriInput).build());
        }
    }

    private void carregarImagem(Patrimonio patrimonio){
        if(Objects.nonNull(patrimonio.getUriImagem())){
            Arquivo arquivo = sistemaDeArquivosIntegration.download(patrimonio.getUriImagem());
            if(Objects.nonNull(arquivo.getContent())){
                patrimonio.setImagem(Base64Utils.encodeToString(arquivo.getContent()));
            }
        }
    }

    private void validaSePatrimonioEstornado(Patrimonio patrimonio){
        if (Patrimonio.Situacao.ESTORNADO.equals(patrimonio.getSituacao())){
            throw new PatrimonioEstornadoException();
        }
    }

    private Patrimonio atualizar(Patrimonio patrimonio) {
        return patrimonioDataProvider.atualizar(patrimonio);
    }

    private void validarSeNumeroSerieJaCadastrado(String numeroSerie, String target, Long id) {
        if(Objects.nonNull(numeroSerie) && !numeroSerie.isEmpty() && !numeroSerie.equals(target) && patrimonioDataProvider.existePorNumeroSerie(retornaStringSemTracoEspaco(numeroSerie), id)) {
            throw new NumeroSerieCadastradoException();
        }
    }

    private void validarSeMotorJaCadastrado(String motor, String target, Long id) {
        if(Objects.nonNull(motor) && !motor.isEmpty() && !motor.equals(target) && patrimonioDataProvider.existePorMotor(retornaStringSemTracoEspaco(motor), id)) {
            throw new MotorCadastradoException();
        }
    }

    private void validarSePlacaJaCadastrada(String placa, Long id) {
        if(Objects.nonNull(placa)){
            if(patrimonioDataProvider.existePorPlaca(retornaStringSemTracoEspaco(placa), id)) {
                throw new PlacaCadastradaException();
            }
        }
    }

    private void validarSeRenavamJaCadastrado(String renavam, String target, Long id) {
        if(Objects.nonNull(renavam) && !renavam.isEmpty() && !renavam.equals(target) && patrimonioDataProvider.existePorRenavam(retornaStringSemTracoEspaco(renavam), id)) {
            throw new RenavamCadastradoException();
        }
    }

    private void validarSeChassiJaCadastrado(String chassi, String target, Long id) {
        if(Objects.nonNull(chassi) && !chassi.isEmpty() && !chassi.equals(target) && patrimonioDataProvider.existePorChassi(retornaStringSemTracoEspaco(chassi), id)) {
            throw new ChassiCadastradoException();
        }
    }

    private void validarPlaca(String placa) {
        if(Objects.nonNull(placa) && !placa.isEmpty() && !VehicleValidate.validarPlaca(retornaStringSemTracoEspaco(placa))) {
            throw new PlacaInvalidaException();
        }
    }

    private void validarRenavam(String renavam) {
        if(Objects.nonNull(renavam) && !renavam.isEmpty() && !VehicleValidate.validarRenavam(retornaStringSemTracoEspaco(renavam))) {
            throw new RenavamInvalidoException();
        }
    }

    private void validarChassi(String chassi) {
        if(Objects.nonNull(chassi) && !chassi.isEmpty() && !VehicleValidate.validarChassi(retornaStringSemTracoEspaco(chassi))) {
            throw new ChassiInvalidoException();
        }
    }

    private String retornaStringSemTracoEspaco(String palavra){
        if(Objects.nonNull(palavra)){
            return palavra.replaceAll("[\\s\\-]", "");
        }
        return null;
    }
}
