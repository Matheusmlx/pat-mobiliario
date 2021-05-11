<template>
    <div class="corpo-conteiner">
        <ficha-patrimonio-dados-gerais-cabecalho :patrimonio="patrimonio"/>

        <ficha-patrimonio-dados-gerais-campos-visualizar :patrimonio="patrimonio"/>

        <ficha-patrimonio-dados-gerais-depreciacao :patrimonio="patrimonio"/>
    </div>

</template>

<script>
    import {mapActions} from 'vuex'
    import {actionTypes} from '@/core/constants'
    import FichaPatrimonioDadosGeraisCabecalho
        from '@/views/pages/patrimonios/patrimonio/ficha-patrimonios/dados-gerais/FichaPatrimonioDadosGeraisCabecalho'
    import FichaPatrimonioDadosGeraisDepreciacao
        from '@/views/pages/patrimonios/patrimonio/ficha-patrimonios/dados-gerais/FichaPatrimonioDadosGeraisDepreciacao'
    import FichaPatrimonioDadosGeraisCamposVisualizar from './FichaPatrimonioDadosGeraisCamposVisualizar'

    export default {
        name: 'FichaPatrimonioDadosGeraisVisualizar',
        components: {
            FichaPatrimonioDadosGeraisCamposVisualizar,
            FichaPatrimonioDadosGeraisDepreciacao,
            FichaPatrimonioDadosGeraisCabecalho},
        data(){
            return {
                patrimonio:{}
            }
        },
        async mounted() {
            await this.buscarPatrimonio()
        },
        methods:{
            ...mapActions([
                actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID_FICHA
            ]),
            async buscarPatrimonio(){
                const resultado = await this.buscarPatrimonioPorIdFicha(this.$route.params.patrimonioId)
                if(resultado){
                    this.patrimonio = resultado
                }
            }
        }
    }
</script>

<style scoped lang="stylus">
    .corpo-conteiner
        min-height 75vh
</style>
