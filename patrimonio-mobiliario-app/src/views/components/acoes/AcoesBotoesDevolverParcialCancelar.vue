<template>
    <div class="az-actions-form">
        <botao-cancelar @cancelar="cancelar"/>
        <botao-devolver-parcial v-if="controleContinuar" @devolverParcial="devolverParcial" :quantidade="quantidade" />
        <botao-devolver-parcial-desabilitado v-else/>
    </div>
</template>

<script>
    import BotaoCancelar from '../botoes/BotaoCancelar'
    import BotaoDevolverParcial from '@/views/components/botoes/BotaoDevolverParcial'
    import BotaoDevolverParcialDesabilitado from '@/views/components/botoes/BotaoDevolverParcialDesabilitado'

    export default {
        name: 'AcoesBotoesDevolverParcialCancelar',
        components: {BotaoDevolverParcialDesabilitado, BotaoDevolverParcial, BotaoCancelar},
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
            devolverParcial(){
                this.$emit('devolverParcial')
            }
        }
    }
</script>
<style lang="stylus">
</style>
