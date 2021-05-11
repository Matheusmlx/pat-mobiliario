package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.cadastro;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.MovimentacaoConstants;
import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.cadastro.converter.CadastrarMovimentacaoInternaOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class CadastrarMovimentacaoInternaUseCaseImpl implements CadastrarMovimentacaoInternaUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private final CadastrarMovimentacaoInternaOutputDataConverter outputDataConverter;

    @Override
    public CadastrarMovimentacaoInternaOutputData executar(CadastrarMovimentacaoInternaInputData inputData) {
        validarDadosEntrada(inputData);

        String codigo = gerarCodigoMovimentacao();
        Movimentacao movimentacao = criarMovimentacao(inputData, codigo);
        Movimentacao movimentacaoSalva = registrarMovimentacao(movimentacao);

        return outputDataConverter.to(movimentacaoSalva);
    }

    private void validarDadosEntrada(CadastrarMovimentacaoInternaInputData inputData) {
        Validator.of(inputData)
            .validate(CadastrarMovimentacaoInternaInputData::getTipo, Objects::nonNull, "Tipo da movimentação é nulo")
            .get();
    }

    private String gerarCodigoMovimentacao() {
        final String ultimoCodigoCadastrado = movimentacaoDataProvider.buscarUltimoCodigoCadastrado();
        final int proximoCodigoSequencial = Integer.parseInt(ultimoCodigoCadastrado) + 1;
        final int quantidadeDigitos = MovimentacaoConstants.QUANTIDADE_DIGITOS_CODIGO_SEQUENCIAL;
        return String.format(("%" + quantidadeDigitos + "s"), proximoCodigoSequencial).replace(' ', '0');
    }

    private Movimentacao criarMovimentacao(CadastrarMovimentacaoInternaInputData inputData, String codigo) {
        String usuario = obterUsuario();

        return Movimentacao
            .builder()
            .codigo(codigo)
            .usuarioCriacao(usuario)
            .tipo(TipoMovimentacaoEnum.valueOf(inputData.getTipo()))
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .build();
    }

    private String obterUsuario() {
        return sessaoUsuarioDataProvider.getLogin();
    }

    private Movimentacao registrarMovimentacao(Movimentacao movimentacao) {
        return movimentacaoDataProvider.salvar(movimentacao);
    }
}
