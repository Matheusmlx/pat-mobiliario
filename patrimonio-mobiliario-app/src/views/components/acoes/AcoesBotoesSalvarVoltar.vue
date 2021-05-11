<template>
    <div class="az-actions-form">
        <botao-voltar @voltar="voltar"/>
        <botao-salvar v-if="controleSalvar" :loading="loading" @salvar="salvar" />
        <botao-salvar-desabilitado v-else />
    </div>
</template>

<script>
    import BotaoSalvar from '../botoes/BotaoSalvar'
    import BotaoVoltar from '../botoes/BotaoVoltar'
    import BotaoSalvarDesabilitado from '@/views/components/botoes/BotaoSalvarDesabilitado'
    export default {
        name: 'AcoesBotoesContinuarVoltar',
        components: {BotaoVoltar, BotaoSalvar, BotaoSalvarDesabilitado},
        props:['controleSalvar'],
        data(){
            return{
                loading: false
            }
        },
        methods: {
            voltar(){
                this.$emit('voltar')
            },
            salvar(){
                this.loading = true
                this.$emit('salvar')
            }
        }
    }
</script>

<style lang="stylus">
</style>
