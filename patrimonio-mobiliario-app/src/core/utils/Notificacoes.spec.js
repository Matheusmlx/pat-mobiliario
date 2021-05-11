import Notificacoes from './Notificacoes'

describe('Notificacoes', () => {

    it('Deve retornar array de notificações completo formatado', () => {
        const data = {
            items: [
                {
                    id: 1,
                    origem: 'DISTRIBUICAO',
                    origemId: 2,
                    assunto: 'Distribuição 1234567891253968',
                    mensagem: 'Em Processamento',
                    dataCriacao: '2020-02-10T00:00:00.000-0400',
                    visualizada: false,
                },
                {
                    id: 2,
                    origem: 'INCORPORACAO',
                    origemId: 3,
                    assunto: 'Incorporação 12345678917589',
                    mensagem: 'Em Processamento',
                    dataCriacao: '2020-02-11T00:00:00.000-0400',
                    visualizada: false,
                },
                {
                    id: 3,
                    origem: 'INCORPORACAO',
                    origemId: 4,
                    assunto: 'Incorporação 78912539687589',
                    mensagem: 'Finalizado',
                    dataCriacao: '2020-02-12T00:00:00.000-0400',
                    visualizada: true,
                },
                {
                    id: 4,
                    origem: 'TIPO_DESCONHECIDO',
                    origemId: 5,
                    assunto: 'Tipo desconhecido 1',
                    mensagem: 'Finalizado',
                    dataCriacao: '2020-02-12T00:00:00.000-0400',
                    visualizada: true,
                },
            ],
            totalElements: 6,
            quantidadeNaoVisualizadas: 6,
        }

        const retornoCorreto = {
            messages: [
                {
                    origem: 'DISTRIBUICAO',
                    origemId: 2,
                    text: '<div style="display: flex;flex-wrap: nowrap;height: 35px;width: 350px; float: left">' +
                        '<div><i class="fas fa-paper-plane" style="font-size: 20px; padding-top: 10px; margin-right: 20px"></i></div>' +
                        '<div><span class="grey--text text--darken-1 font-weight-bold body-2">Distribuição 1234567891253968</span>' +
                        '<p class="blue--text text--darken-2">Em Processamento</p></div>' +
                        '</div>',
                    when: '2020-02-10T00:00:00.000-0400',
                    read: false,
                },
                {
                    origem: 'INCORPORACAO',
                    origemId: 3,
                    text: '<div style="display: flex;flex-wrap: nowrap;height: 35px;width: 350px; float: left">' +
                        '<div><i class="fas fa-clipboard-check" style="font-size: 20px; padding-top: 10px; margin-right: 20px"></i></div>' +
                        '<div><span class="grey--text text--darken-1 font-weight-bold body-2">Incorporação 12345678917589</span>' +
                        '<p class="blue--text text--darken-2">Em Processamento</p></div>' +
                        '</div>',
                    when: '2020-02-11T00:00:00.000-0400',
                    read: false,
                },
                {
                    origem: 'INCORPORACAO',
                    origemId: 4,
                    text: '<div style="display: flex;flex-wrap: nowrap;height: 35px;width: 350px; float: left">' +
                        '<div><i class="fas fa-clipboard-check" style="font-size: 20px; padding-top: 10px; margin-right: 20px"></i></div>' +
                        '<div><span class="grey--text text--darken-1 font-weight-bold body-2">Incorporação 78912539687589</span>' +
                        '<p class="blue--text text--darken-2">Finalizado</p></div>' +
                        '</div>',
                    when: '2020-02-12T00:00:00.000-0400',
                    read: true,
                },
                {
                    origem: 'TIPO_DESCONHECIDO',
                    origemId: 5,
                    text: '<div style="display: flex;flex-wrap: nowrap;height: 35px;width: 350px; float: left">' +
                        '<div><i class="" style="font-size: 20px; padding-top: 10px; margin-right: 20px"></i></div>' +
                        '<div><span class="grey--text text--darken-1 font-weight-bold body-2">Tipo desconhecido 1</span>' +
                        '<p class="blue--text text--darken-2">Finalizado</p></div>' +
                        '</div>',
                    when: '2020-02-12T00:00:00.000-0400',
                    read: true,
                },
            ],
            unread: 6,
            viewMore: true,
            endNotification: false
        }

        const retorno = Notificacoes.retornaArrayDeNotificacoes(data)

        expect(retorno).toEqual(retornoCorreto)

    })

    it('Deve retornar array de notificações vazio formatado', () => {
        const data = {
            quantidadeNaoVisualizadas: 6,
        }

        const retornoCorreto = {messages: [], unread: 6, viewMore: false, endNotification: false}

        const retorno = Notificacoes.retornaArrayDeNotificacoes(data)

        expect(retorno).toEqual(retornoCorreto)
    })

})
