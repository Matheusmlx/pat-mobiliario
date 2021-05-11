<template>
    <div class="az-actions-form">
        <botao-voltar @voltar="voltar"/>
        <botao-finalizar-desabilitado v-if="desabilitado && !emProcessamento" :texto="textoBloqueio"/>
        <botao-finalizar
            v-else-if="!desabilitado && !emProcessamento"
            @finalizar="finalizar"
            :textoButton="textoButton"
        />
        <botao-finalizar-em-processamento v-else-if="emProcessamento" :mensagem="textoEmProcessamento"/>
    </div>
</template>

<script>
    import BotaoVoltar from '../botoes/BotaoVoltar'
    import BotaoFinalizar from '../botoes/BotaoFinalizar'
    import BotaoFinalizarDesabilitado from '../botoes/BotaoFinalizarDesabilitado'
    import BotaoFinalizarEmProcessamento from '../botoes/BotaoFinalizarEmProcessamento'

    export default {
        name: 'AcoesBotoesFinalizarVoltar',
        components: {BotaoFinalizarEmProcessamento, BotaoFinalizarDesabilitado, BotaoFinalizar, BotaoVoltar},
        props: {
            desabilitado: {
                type: Boolean
            },
            emProcessamento: {
                type: Boolean,
                default: false
            },
            textoEmProcessamento: {
                type: String,
                required: true
            },
            textoBloqueio: {
                type: String,
                required: true
            },
            textoButton: {
                type: String
            }
        },
        methods: {
            voltar() {
                this.$emit('voltar')
            },
            async finalizar() {
                await this.$listeners.finalizar()
            },
            adicionarOutro() {
                this.$emit('adicionarOutro')
            }
        }
    }
</script>

<style lang="stylus">
</style>
