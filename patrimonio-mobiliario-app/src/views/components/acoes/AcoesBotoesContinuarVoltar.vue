<template>
    <div class="az-actions-form">
        <botao-voltar @voltar="voltar"/>
        <botao-continuar v-if="controleContinuar" @continuar="continuar" />
        <botao-continuar-desabilitado v-else />
    </div>
</template>

<script>
    import BotaoContinuar from '../botoes/BotaoContinuar'
    import BotaoVoltar from '../botoes/BotaoVoltar'
    import BotaoContinuarDesabilitado from '../botoes/BotaoContinuarDesabilitado'
    export default {
        name: 'AcoesBotoesContinuarVoltar',
        components: {BotaoVoltar, BotaoContinuar, BotaoContinuarDesabilitado},
        props:['controleContinuar'],
        methods: {
            voltar(){
                this.$emit('voltar')
            },
            continuar(){
                this.$emit('continuar')
            }
        }
    }
</script>

<style lang="stylus">
</style>
