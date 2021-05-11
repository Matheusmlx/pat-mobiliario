<template>
<div>
    <component v-bind:is="tipoMovimentacaoInterna" @habilitaPasso3="habilitaPasso3" @desabilitaPasso3="desabilitaPasso3"/>
</div>
</template>

<script>
    import {mapActions} from 'vuex'
    import {actionTypes} from '@/core/constants'
    import DISTRIBUICAO from './distribuicao/EdicaoDadosGeraisDistribuicao'
    import ENTRE_SETORES from './entre-setores/EdicaoDadosGeraisEntreSetores'
    import ENTRE_ESTOQUES from './entre-estoques/EdicaoDadosGeraisEntreEstoques'
    import DEVOLUCAO_ALMOXARIFADO from './devolucao-almoxarifado/EdicaoDadosGeraisDevolucaoAlmoxarifado'
    import TEMPORARIA from './temporaria/EdicaoDadosGeraisTemporaria'

    export default {
        name: 'MovimentacaoInternaEdicaoDadosGerais',
        components:{DISTRIBUICAO, ENTRE_SETORES, ENTRE_ESTOQUES, DEVOLUCAO_ALMOXARIFADO,TEMPORARIA},
        data(){
            return{
                tipoMovimentacaoInterna: ''
            }
        },
        async mounted() {
            await this.buscarTipo()
        },
        methods: {
            ...mapActions([
                actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA,
            ]),
            habilitaPasso3(){
                this.$emit('habilitaPasso3')
            },
            desabilitaPasso3(){
                this.$emit('desabilitaPasso3')
            },
            async buscarTipo(){
                this.tipoMovimentacaoInterna = await this.buscarTipoMovimentacaoInterna(this.$route.params.movimentacaoInternaId)
            }
        }
    }
</script>
