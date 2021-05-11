import _ from 'lodash'

export default {
    getRotulosPersonalizados: () => window.$i18n.getLocaleMessage(window.$i18n.locale),

    getNomeRotulosPersonalizados: () => (campoNome, telaNome) => {
        return (window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome].campos[campoNome] || {}).nome
    },

    getObrigatorioRotulosPersonalizados: () => (campoNome, telaNome) => {
        const obrigatorio = (window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome].campos[campoNome] || {}).obrigatorio
        return (obrigatorio === true || obrigatorio === null || obrigatorio === undefined || obrigatorio === 'true')
    },

    getTooltipRotulosPersonalizados: () => (campoNome, telaNome) => {
        return (window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome].campos[campoNome] || {}).tooltip
    },

    getObjetoValidado: () => (objetoEntidade, telaNome) => {
        if (!window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome]) return false

        let valido = true

        _.each(Object.keys(window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome].campos), campoNome => {

            const obrigatorio = window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome].campos[campoNome].obrigatorio

            if ((obrigatorio === true || obrigatorio === null || obrigatorio === undefined) && !objetoEntidade[campoNome]) {
                valido = false
            }
        })
        return valido
    },

    getItemIncorporacaoValidado: () => (objetoEntidade, tipoVeiculo, telaNome) => {
        let validacao = new ValidacaoItensIncorporacao()
        if (!window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome]) return false

        let valido = true

        _.each(Object.keys(window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome].campos), campoNome => {

            const obrigatorio = window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome].campos[campoNome].obrigatorio

            if (validacao.campoObrigatorioNaoPreenchidoSeItemTipoVeiculo(obrigatorio, objetoEntidade, campoNome, tipoVeiculo) ||
                validacao.campoObrigatorioNaoPreenchidoExcetoCamposVeiculoSeItemDiferenteDeTipoVeiculo(obrigatorio, objetoEntidade, campoNome, tipoVeiculo)) {
                valido = false
            }
        })
        return valido
    },

    getIncorporacaoValidado: () => (objetoEntidade, convenioCheckbox, fundoCheckbox, projetoCheckbox, comodatoCheckbox, notaObrigatorio, telaNome) => {
        let validacao = new ValidacaoIncorporacao()
        if (!window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome]) return false

        let valido = true

        _.each(Object.keys(window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome].campos), campoNome => {

            const obrigatorio = window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome].campos[campoNome].obrigatorio

            if (validacao.campoObrigatorioNaoEspecialNaoFoiPreenchido(obrigatorio, objetoEntidade, campoNome) ||
                validacao.campoConvenioObrigatorioESelecionadoNaoFoiPreenchido(obrigatorio, objetoEntidade, campoNome, convenioCheckbox) ||
                validacao.campoFundoObrigatorioESelecionadoNaoFoiPreenchido(obrigatorio, objetoEntidade, campoNome, fundoCheckbox) ||
                validacao.campoProjetoObrigatorioESelecionadoNaoFoiPreenchido(obrigatorio, objetoEntidade, campoNome, projetoCheckbox) ||
                validacao.campoComodanteObrigatorioESelecionadoNaoFoiPreenchido(obrigatorio, objetoEntidade, campoNome, comodatoCheckbox) ||
                validacao.notaObrigatorioECampoNotaNaoPreenchido(obrigatorio, objetoEntidade, campoNome, notaObrigatorio)) {
                valido = false
            }

        })
        return valido
    },

    getDocumentoTodosObrigatoriosValidado: () => (objetoEntidade, telaNome) => {
        let validacao = new ValidacaoDocumento()
        if (!window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome]) return false

        let valido = true

        _.each(Object.keys(window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome].campos), campoNome => {

            const obrigatorio = window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome].campos[campoNome].obrigatorio

            if (validacao.campoObrigatorioNaoFoiPreenchido(objetoEntidade, campoNome) ||
                validacao.campoUriAnexoNaoFoiPreenchido(obrigatorio, objetoEntidade, campoNome)) {
                valido = false
            }

        })
        return valido
    },

    getDocumentoNumeroTipoObrigatorioValidado: () => (objetoEntidade, telaNome) => {
        let validacao = new ValidacaoDocumento()
        if (!window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome]) return false

        let valido = true

        _.each(Object.keys(window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome].campos), campoNome => {

            const obrigatorio = window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome].campos[campoNome].obrigatorio

            if (validacao.camposObrigatoriosNaoPreenchidos(obrigatorio, objetoEntidade, campoNome)) {
                valido = false
            }

        })
        return valido
    },

    getDistribuicaoValidado: () => (objetoEntidade, nomeTelaMovimentacao, nomeTelaIncorporacao, nomeTelaDistribuicao) => {
        let validacao = new ValidacaoDistribuicao()
        if (!window.$i18n.getLocaleMessage(window.$i18n.locale)[nomeTelaMovimentacao]) return false
        if (!window.$i18n.getLocaleMessage(window.$i18n.locale)[nomeTelaDistribuicao]) return false
        if (!window.$i18n.getLocaleMessage(window.$i18n.locale)[nomeTelaIncorporacao]) return false

        let valido = true

        const camposMovimentacao = {
            ...window.$i18n.getLocaleMessage(window.$i18n.locale)[nomeTelaMovimentacao].campos,
            ...window.$i18n.getLocaleMessage(window.$i18n.locale)[nomeTelaDistribuicao].campos
        }

        _.each(Object.keys(camposMovimentacao), campoNome => {

            let obrigatorio

            if (window.$i18n.getLocaleMessage(window.$i18n.locale)[nomeTelaMovimentacao].campos[campoNome]) {
                obrigatorio = window.$i18n.getLocaleMessage(window.$i18n.locale)[nomeTelaMovimentacao].campos[campoNome].obrigatorio
            } else {
                obrigatorio = window.$i18n.getLocaleMessage(window.$i18n.locale)[nomeTelaDistribuicao].campos[campoNome].obrigatorio
            }

            if (validacao.camposObrigatoriosNaoPreenchidosDistribuicao(obrigatorio, objetoEntidade, campoNome)) {
                valido = false
            }
        })

        _.each(Object.keys(window.$i18n.getLocaleMessage(window.$i18n.locale)[nomeTelaIncorporacao].campos), campoNome => {

            const obrigatorio = window.$i18n.getLocaleMessage(window.$i18n.locale)[nomeTelaIncorporacao].campos[campoNome].obrigatorio

            if (validacao.camposObrigatoriosNaoPreenchidosIncorporacao(obrigatorio, objetoEntidade, campoNome)) {
                valido = false
            }
        })

        return valido
    },

    getMovimentacaoInternaValidado: () => (objetoEntidade) => {
        const camposObrigatorios = ['orgao', 'setorDestino', 'setorOrigem']

        for (let campo of camposObrigatorios) {
            if (!objetoEntidade[campo]) {
                return false
            }
        }

        return true
    },

    getTemporariaValidado: () => (objetoEntidade) => {
        const camposObrigatorios = ['orgao', 'setorDestino', 'setorOrigem', 'dataDevolucao']

        for (let campo of camposObrigatorios) {
            if (!objetoEntidade[campo]) {
                return false
            }
        }

        return true
    }
}

class ValidacaoItensIncorporacao {

    campoObrigatorioNaoPreenchidoSeItemTipoVeiculo(obrigatorio, objetoEntidade, campoNome, tipoVeiculo) {
        return this.campoObrigatorio(obrigatorio) && !objetoEntidade[campoNome] && tipoVeiculo
    }

    campoObrigatorioNaoPreenchidoExcetoCamposVeiculoSeItemDiferenteDeTipoVeiculo(obrigatorio, objetoEntidade, campoNome, tipoVeiculo) {
        return !tipoVeiculo && this.diferenteDeCamposVeiculo(campoNome) && this.campoObrigatorio(obrigatorio) && !objetoEntidade[campoNome]
    }

    diferenteDeCamposVeiculo(campoNome){
        return(campoNome !== 'anoFabricacaoModelo' && campoNome !== 'combustivel' && campoNome !== 'categoria')
    }

    campoObrigatorio(obrigatorio){
        return (obrigatorio === true || obrigatorio === null || obrigatorio === undefined || obrigatorio === 'true')
    }
}

class ValidacaoIncorporacao {

    campoObrigatorioNaoEspecialNaoFoiPreenchido(obrigatorio, objetoEntidade, campoNome) {
        return this.camposNaoEspeciais(campoNome) && this.campoObrigatorio(obrigatorio) && !objetoEntidade[campoNome]
    }

    campoConvenioObrigatorioESelecionadoNaoFoiPreenchido(obrigatorio, objetoEntidade, campoNome, convenioCheckbox) {
        return campoNome === 'convenio' && this.campoObrigatorio(obrigatorio) && convenioCheckbox && !objetoEntidade[campoNome]
    }

    campoFundoObrigatorioESelecionadoNaoFoiPreenchido(obrigatorio, objetoEntidade, campoNome, fundoCheckbox) {
        return campoNome === 'fundo' && this.campoObrigatorio(obrigatorio) && fundoCheckbox && !objetoEntidade[campoNome]
    }

    campoProjetoObrigatorioESelecionadoNaoFoiPreenchido(obrigatorio, objetoEntidade, campoNome, projetoCheckbox) {
        return campoNome === 'projeto' && this.campoObrigatorio(obrigatorio) && projetoCheckbox && !objetoEntidade[campoNome]
    }

    campoComodanteObrigatorioESelecionadoNaoFoiPreenchido(obrigatorio, objetoEntidade, campoNome, comodanteCheckbox) {
        return campoNome === 'comodante' && this.campoObrigatorio(obrigatorio) && comodanteCheckbox && !objetoEntidade[campoNome]
    }

    notaObrigatorioECampoNotaNaoPreenchido(obrigatorio, objetoEntidade, campoNome, notaObrigatorio) {
        return notaObrigatorio && (campoNome === 'nota' || campoNome === 'dataNota' || campoNome === 'valorNota') && !objetoEntidade[campoNome]
    }

    camposNaoEspeciais(campoNome){
        return (campoNome === 'reconhecimento' || campoNome === 'numProcesso' || campoNome === 'fornecedor' ||
            campoNome === 'orgao' || campoNome === 'setor' || campoNome === 'dataRecebimento' ||
            campoNome === 'numeroNotaLancamentoContabil' || campoNome === 'dataNotaLancamentoContabil')
    }

    campoObrigatorio(obrigatorio){
        return (obrigatorio === true || obrigatorio === null || obrigatorio === undefined || obrigatorio === 'true')
    }
}

class ValidacaoDocumento {

    campoObrigatorioNaoFoiPreenchido(objetoEntidade, campoNome) {
        return campoNome !== 'uriAnexo' && !objetoEntidade[campoNome]
    }

    campoUriAnexoNaoFoiPreenchido(obrigatorio,objetoEntidade, campoNome){
        return campoNome === 'uriAnexo' && this.campoObrigatorio(obrigatorio) && !objetoEntidade[campoNome]
    }

    camposObrigatoriosNaoPreenchidos(obrigatorio, objetoEntidade, campoNome){
       return this.campoObrigatorio(obrigatorio) && !objetoEntidade[campoNome]
    }

    campoObrigatorio(obrigatorio){
        return (obrigatorio === true || obrigatorio === null || obrigatorio === undefined || obrigatorio === 'true')
    }
}

class ValidacaoDistribuicao {
    camposObrigatoriosNaoPreenchidosDistribuicao(obrigatorio, objetoEntidade, campoNome){
        return this.campoObrigatorio(obrigatorio) && !this.camposIncorporacao(campoNome) && !objetoEntidade[campoNome]
    }

    camposObrigatoriosNaoPreenchidosIncorporacao(obrigatorio, objetoEntidade, campoNome){
        return this.campoObrigatorio(obrigatorio) && this.camposIncorporacao(campoNome) && !objetoEntidade[campoNome]
    }

    camposIncorporacao(campoNome) {
        return (campoNome === 'orgao' || campoNome === 'numeroNotaLancamentoContabil' || campoNome === 'dataNotaLancamentoContabil')
    }

    campoObrigatorio(obrigatorio){
        return (obrigatorio === true || obrigatorio === null || obrigatorio === undefined || obrigatorio === 'true')
    }
}
