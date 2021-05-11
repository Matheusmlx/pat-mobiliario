<template>
    <div class="botoes_acoes">
        <botao-icone-tooltip-selecionar :desabilitado="desabilitado"  @selecionar="selecionar"/>
        <botao-icone-tooltip-editar @ativarEdicao="ativarEdicao"/>
    </div>
</template>

<script>
    import BotaoIconeTooltipSelecionar from '../botoes/BotaoIconeTooltipSelecionar'
    import BotaoIconeTooltipEditar from '../botoes/BotaoIconeTooltipEditar'

    export default {
        name: 'AcoesIconesTooltipEditarSelecionar',
        components: {BotaoIconeTooltipEditar, BotaoIconeTooltipSelecionar},
        props:{
            desabilitado:{
                type: Boolean,
                required: true
            }
        },
        methods: {
            selecionar(){
                this.$emit('selecionar')
            },
            ativarEdicao(){
                this.$emit('ativarEdicao')
            }
        }
    }
</script>

<style scoped lang="stylus">
    .botoes_acoes
        display inline-flex
</style>
