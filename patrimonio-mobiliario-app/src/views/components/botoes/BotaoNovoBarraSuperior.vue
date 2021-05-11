<template>
    <div>
        <botao-novo v-if="!desabilitar" v-on="$listeners"/>
        <botao-novo-desabilitado v-else :mensagem-tooltip="mensagemTooltip"/>
    </div>
</template>

<script>
    import BotaoNovo from '@/views/components/botoes/BotaoNovo'
    import BotaoNovoDesabilitado from '@/views/components/botoes/BotaoNovoDesabilitado'
    export default {
        name: 'BotaoNovoBarraSuperior',
        components: {BotaoNovoDesabilitado, BotaoNovo},
        props: {
            desabilitar:{
                type: Boolean,
                required: true
            },
            mensagemTooltip:{
                type: String,
                required: true
            }
        }
    }
</script>

<style lang="stylus" scoped>
    .botao-cancelar
        color #999 !important
</style>
