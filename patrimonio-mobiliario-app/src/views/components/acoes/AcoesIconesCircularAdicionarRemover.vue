<template>
    <div class="botoes_acoes">
        <botao-icone-circular-remover :desabilitado="desabilitarRemover"  @remover="remover"/>
        <botao-icone-circular-adicionar :desabilitado="desabilitarAdicionar" :ocultado="ocultarAdicionar" @adicionar="adicionar"/>
    </div>
</template>

<script>
    import BotaoIconeCircularRemover from '../botoes/BotaoIconeCircularRemover'
    import BotaoIconeCircularAdicionar from '../botoes/BotaoIconeCircularAdicionar'

    export default {
        name: 'AcoesIconesCircularAdicionarRemover',
        components: {BotaoIconeCircularAdicionar, BotaoIconeCircularRemover},
        props:{
            desabilitarAdicionar:{
                type: Boolean,
                required: true
            },
            desabilitarRemover:{
                type: Boolean,
                required: true
            },
            ocultarAdicionar:{
                type: Boolean,
                required: true
            }
        },
        methods: {
            remover(){
                this.$emit('remover')
            },
            adicionar(){
                this.$emit('adicionar')
            }
        }
    }
</script>

<style scoped lang="stylus">
    .botoes_acoes
        display inline-flex
</style>
