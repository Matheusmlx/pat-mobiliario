import axios from 'axios'

class NotificacaoApiClient {

  async buscarNotificacoes(page) {
    return  axios.get(`api/notificacoes?page=${page}`)
  }

  async buscarQuantidadeNotificacoesNaoVisualizadas() {
    return axios.get('api/notificacoes/quantidade-nao-visualizadas')
  }

}

export default new NotificacaoApiClient()