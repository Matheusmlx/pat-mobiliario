<template>
    <div class="az-actions-form">
        <botao-cancelar @cancelar="cancelar"/>
        <botao-adicionar v-if="controleContinuar" @adicionar="adicionar" :quantidade="quantidade" />
        <botao-adicionar-desabilitado v-else/>
    </div>
</template>

<script>
    import BotaoCancelar from '../botoes/BotaoCancelar'
    import BotaoAdicionar from '@/views/components/botoes/BotaoAdicionar'
    import BotaoAdicionarDesabilitado from '@/views/components/botoes/BotaoAdicionarDesabilitado'

    export default {
        name: 'AcoesBotoesContinuarCancelar',
        components: {BotaoAdicionarDesabilitado, BotaoAdicionar, BotaoCancelar},
        props: {
            controleContinuar:{
                type: Boolean,
                default:false,
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
            adicionar(){
                this.$emit('adicionar')
            }
        }
    }
</script>
<style lang="stylus">
</style>
