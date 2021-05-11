<template>
    <div class="az-actions-form">
        <botao-cancelar @cancelar="cancelar"/>
        <botao-continuar v-if="controleContinuar" @continuar="continuar" :quantidade="quantidade" :esconder-icone="esconderIcone" />
        <botao-continuar-desabilitado v-else :esconder-icone="esconderIcone"/>
    </div>
</template>

<script>
    import BotaoContinuar from '../botoes/BotaoContinuar'
    import BotaoCancelar from '../botoes/BotaoCancelar'
    import BotaoContinuarDesabilitado from '../botoes/BotaoContinuarDesabilitado'
    export default {
        name: 'AcoesBotoesContinuarCancelar',
        components: {BotaoCancelar, BotaoContinuar, BotaoContinuarDesabilitado},
        props: {
            esconderIcone:{
                type: Boolean,
                default: false
            },
            controleContinuar:{
                type: Boolean,
                default: false,
                required: true
            },
            quantidade:{
                type: Number
            }
        },
        methods: {
            cancelar(){
                this.$emit('cancelar')
            },
            continuar(){
                this.$emit('continuar')
            }
        }
    }
</script>

<style lang="stylus">
</style>
