export default class Notificacoes {
    static icones = {
        'INCORPORACAO': 'fas fa-clipboard-check',
        'DISTRIBUICAO': 'fas fa-paper-plane'
    }

    static retornaArrayDeNotificacoes(data) {
        const arrayNotificacoes = data.items ? data.items : []
        const quantidadeNaoVisualizadasAtual = data.quantidadeNaoVisualizadas
        const mostraVerMais = data.totalElements > arrayNotificacoes.length
        const fimNotificacoes = data.totalElements === arrayNotificacoes.length
        const notificacoes = {
            messages: [],
            unread: quantidadeNaoVisualizadasAtual,
            viewMore: mostraVerMais,
            endNotification: fimNotificacoes
        }

        if (arrayNotificacoes.length > 0) {
            for (let i = 0; i < arrayNotificacoes.length; i++) {
                const icone = Notificacoes.icones[arrayNotificacoes[i].origem] || ''
                const message = {
                    origem: arrayNotificacoes[i].origem,
                    origemId: arrayNotificacoes[i].origemId,
                    text: '<div style="display: flex;flex-wrap: nowrap;height: 35px;width: 350px; float: left">' +
                        `<div><i class="${icone}" style="font-size: 20px; padding-top: 10px; margin-right: 20px"></i></div>` +
                        '<div><span class="grey--text text--darken-1 font-weight-bold body-2">' + arrayNotificacoes[i].assunto + '</span>' +
                        '<p class="blue--text text--darken-2">' + arrayNotificacoes[i].mensagem + '</p></div>' +
                        '</div>',
                    when: arrayNotificacoes[i].dataCriacao,
                    read: arrayNotificacoes[i].visualizada
                }
                notificacoes.messages.push(message)
            }
        }
        return notificacoes
    }

}
