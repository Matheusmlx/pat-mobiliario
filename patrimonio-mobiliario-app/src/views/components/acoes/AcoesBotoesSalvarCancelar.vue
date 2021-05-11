<template>
    <div class="az-actions-form">
        <botao-cancelar
            @cancelar="cancelar"/>
        <botao-salvar v-if="camposAceitos" @salvar="salvar" />
        <botao-salvar-desabilitado v-else />
    </div>
</template>

<script>
    import BotaoSalvar from '../botoes/BotaoSalvar'
    import BotaoCancelar from '../botoes/BotaoCancelar'
    import BotaoSalvarDesabilitado from '../botoes/BotaoSalvarDesabilitado'
    export default {
        name: 'AcoesBotoesSalvarCancelar',
        components: {BotaoCancelar, BotaoSalvar, BotaoSalvarDesabilitado},
        props:['camposAceitos'],
        methods: {
            cancelar(){
                this.$emit('cancelar')
            },
            salvar(){
                this.$emit('salvar')
            }
        }
    }
</script>

<style lang="stylus">
</style>
