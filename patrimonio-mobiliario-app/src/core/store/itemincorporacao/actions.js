import api from '@/core/apiclient'
import {actionTypes, mutationTypes} from '@/core/constants'

export default {

    async [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO](context, dados){
        const {data} = await api.itemIncorporacao.buscarItemIncorporacaoPorId(dados)
        return data
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO_REGISTRO](context, dados){
        const {data} = await api.itemIncorporacao.buscarItemIncorporacaoVisualizacao(dados)
        return data
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO]({state}, id_incorporacao) {
        const {filtros, paginacao} = state.resultadoBuscaTodosItensIncorporacao
        const {data} = await api.itemIncorporacao.buscarTodos(filtros, paginacao, id_incorporacao)
        return data
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_REGISTRO_ITENS_INCORPORACAO]({state}, id_incorporacao) {
        const {filtros, paginacao} = state.buscaRegistroItensIncorporacao
        const {data} = await api.itemIncorporacao.buscarTodos(filtros, paginacao, id_incorporacao)
        return data
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DELETAR_ITEM_INCORPORACAO](context, dados){
        await api.itemIncorporacao.deletar(dados)
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO]({commit}, item){
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        commit(mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.ATIVAR_SALVAMENTO_AUTOMATICO)
        const {data} = await api.itemIncorporacao.editarItemIncorporacao(item)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.IMAGEM.UPLOAD](context, imagem){
        let formData = new FormData()
        formData.append('file', imagem.uriImagem)
        const {data} = await api.itemIncorporacao.uploadImagem(formData)
        return data.uri
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.IMAGEM.DOWNLOAD](context, imagem){
        const {data} = await api.itemIncorporacao.downloadAnexo(imagem)
        let blob = new Blob([data], {type: ['img/pdf', 'img/jpeg', 'img/png']})
        const downloadLink = document.createElement('a')
        downloadLink.href = window.URL.createObjectURL(blob)
        downloadLink.download = 'Imagem.png'
        downloadLink.click()
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.CADASTRAR_ITEM_INCORPORACAO](context, dados) {
        const {data} = await api.itemIncorporacao.cadastrarItem(dados)
        return data
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.ESTADO_CONSERVACAO.BUSCAR_ESTADOS_CONSERVACAO]() {
        const {data} = await api.estadoConservacao.buscarTodos()
        return data
    }
}
