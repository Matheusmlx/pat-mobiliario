<template>
    <div class="botoes_acoes">
        <botao-icone-cancelar @cancelar="cancelar"/>
        <botao-icone-salvar  @salvar="salvar" />
    </div>
</template>

<script>
    import BotaoIconeSalvar from '../botoes/BotaoIconeSalvar'
    import BotaoIconeCancelar from '../botoes/BotaoIconeCancelar'

    export default {
        name: 'AcoesIconesSalvarCancelar',
        components: {BotaoIconeCancelar, BotaoIconeSalvar},
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

<style scoped lang="stylus">
    .botoes_acoes
        display inline-flex
</style>
