import {mensagens, mutationTypes, notificacoesDefault} from '@/core/constants'

export default {
    methods: {
        mostrarNotificacaoErro(message) {
            if (message === 'GENERAL') {
                message = mensagens.DISCONNECTED
            }
            this.$store.commit(mutationTypes.LOKI.SHOW_ALERT, {
                message,
                type: 'error'
            })
        },
        mostrarNotificacaoErroDefault() {
            this.$store.commit(mutationTypes.LOKI.SHOW_ALERT, {
                message: notificacoesDefault.ERRO_DEFAULT,
                type: 'error'
            })
        },
        mostrarNotificacaoSucesso(message) {
            this.$store.commit(mutationTypes.LOKI.SHOW_ALERT, {
                message,
                type: 'success'
            })
        },
        mostrarNotificacaoSucessoDefault() {
            this.$store.commit(mutationTypes.LOKI.SHOW_ALERT, {
                message: notificacoesDefault.SUCESSO_DEFAULT,
                type: 'success'
            })
        },
        mostrarNotificacaoAviso(message) {
            this.$store.commit(mutationTypes.LOKI.SHOW_ALERT, {
                message,
                type: 'warning'
            })
        },
        mostrarNotificacaoSucessoComConfirmacao({titulo= 'Tudo certo',subTitulo= '',textoBotao= 'Ok'},callBack) {
            this.$swal({
                icon: 'success',
                title: titulo,
                text: subTitulo,
                confirmButtonText: textoBotao,
                allowOutsideClick: false,
                allowEscapeKey: false,
                confirmButtonColor: '#487b9c'
            }).then(callBack.bind(this))
        },
        mostrarConfirmacao({
            titulo= 'Tudo certo',
            subTitulo= '',
            textoBotaoConfirm= 'Ok',
            textoBotaoCancel= 'Cancelar',
            icon = 'success'
        },callBack) {
            this.$swal({
                icon: icon,
                title: titulo,
                text: subTitulo,
                confirmButtonText: textoBotaoConfirm,
                cancelButtonText: textoBotaoCancel,
                allowOutsideClick: false,
                allowEscapeKey: false,
                confirmButtonColor: '#487b9c',
                showCancelButton: true,
                reverseButtons: true
            }).then(callBack.bind(this))
        },
    }
}
