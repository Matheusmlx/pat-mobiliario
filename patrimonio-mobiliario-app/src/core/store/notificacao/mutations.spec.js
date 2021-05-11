import mutations from './mutations'
import { mutationTypes } from '@/core/constants'

describe('Mutations', () => {
  const state = {
    notificacao: {
      page: 1,
      items: ['123']
    },
    configuracaoNotificacao: {
      noNotificationText: 'Nenhuma notificação encontrada...'
    },
    noNotificationTextAuxiliar: ''
  }

  it('Deve incrementar page da notificação', () => {
    mutations[mutationTypes.NOTIFICACAO.ACRESCENTA_PAGE_NOTIFICACAO](state)
    expect(state.notificacao.page).toEqual(2)
  })

  it('Deve resetar a paginação da notificação', () => {
    state.notificacao.page = 3
    mutations[mutationTypes.NOTIFICACAO.RESETA_PAGE_NOTIFICACAO](state)
    expect(state.notificacao.page).toEqual(1)
  })

  it('Deve guardar itens da notificação', () => {
    state.notificacao.items = []
    const itens = ['123']
    mutations[mutationTypes.NOTIFICACAO.SET_NOTIFICACAO_ITENS](state, itens)
    expect(state.notificacao.items).toEqual(itens)
  })

  it('Deve limpar itens da notificação', () => {
    mutations[mutationTypes.NOTIFICACAO.RESETA_NOTIFICACAO_ITENS](state)
    expect(state.notificacao.items).toEqual([])
  })

  it('Deve guardar valor de noNotificationText e apagar seu valor', () => {
    mutations[mutationTypes.NOTIFICACAO.OCULTA_NO_NOTIFICATION_TEXT](state)
    expect(state.configuracaoNotificacao.noNotificationText).toEqual('')
    expect(state.noNotificationTextAuxiliar).toEqual('Nenhuma notificação encontrada...')
  })

  it('Deve voltar valor padrão de noNotificationText e apagar valor de auxiliar', () => {
    state.noNotificationTextAuxiliar = 'Nenhuma notificação encontrada...'
    state.configuracaoNotificacao.noNotificationText = ''
    mutations[mutationTypes.NOTIFICACAO.VOLTA_NO_NOTIFICATION_TEXT_PADRAO](state)
    expect(state.noNotificationTextAuxiliar).toEqual('')
    expect(state.configuracaoNotificacao.noNotificationText).toEqual('Nenhuma notificação encontrada...')
  })
})
