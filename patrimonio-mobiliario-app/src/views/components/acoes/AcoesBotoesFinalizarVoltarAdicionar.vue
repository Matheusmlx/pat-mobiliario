<template>
    <div class="az-actions-form">
        <botao-voltar @voltar="voltar"/>
        <v-col cols="2" md="5" class="pt-0 pb-0">
            <v-row class="align-end justify-end">
                <botao-adicionar-outro :desabilitado="desabilitado" @adicionarOutro="adicionarOutro" class="mr-10"/>
                <botao-finalizar  @finalizar="finalizar" />
            </v-row>
        </v-col>
    </div>
</template>

<script>
    import BotaoVoltar from '../botoes/BotaoVoltar'
    import BotaoFinalizar from '../botoes/BotaoFinalizar'
    import BotaoAdicionarOutro from '../botoes/BotaoAdicionarOutro'
    export default {
        name: 'AcoesBotoesContinuarCancelar',
        components: {BotaoAdicionarOutro, BotaoFinalizar, BotaoVoltar},
        props:{
            desabilitado: {
                type: Boolean
            },
        },
        methods: {
            voltar(){
                this.$emit('voltar')
            },
            finalizar(){
                this.$emit('finalizar')
            },
            adicionarOutro(){
                this.$emit('adicionarOutro')
            }
        }
    }
</script>

<style lang="stylus">
</style>
