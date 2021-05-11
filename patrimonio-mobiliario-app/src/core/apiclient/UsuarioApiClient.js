import axios from 'axios'

class UsuarioApiClient {

    async atualiazarSessaoDominio(produtoId, dominioTipoCliente) {
        return await axios.put(`/hal/usuario/sessao/atualizarSessaoMultiplosDominios?produtoId=${produtoId}&dominioTipoCliente=${dominioTipoCliente}`)
    }

    async buscarLogado(produtoId) {
        return await axios.get(`/hal/usuario/sessao?produtoId=${produtoId}`)
    }

    async editar(payload) {
        return await axios.post('/hal/editarUsuario', payload)
    }
}

export default new UsuarioApiClient()
