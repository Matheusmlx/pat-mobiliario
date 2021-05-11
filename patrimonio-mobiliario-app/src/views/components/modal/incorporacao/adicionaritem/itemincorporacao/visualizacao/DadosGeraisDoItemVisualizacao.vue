<template>
    <div>
        <subTitle :value="dadosDeEntrada"/>
        <v-divider></v-divider>
        <DadosGeraisDoItemVisualizacaoFormulario
            :dadosDeEntrada="dadosDeEntrada"
            @bloquearPasso3="liberarContinuar"
        />
    </div>
</template>


<script>
    import DadosGeraisDoItemVisualizacaoFormulario from './DadosGeraisDoItemVisualizacaoFormulario'
    import DadosGeraisDoItemVisualizacaoSubTitle from './DadosGeraisDoItemVisualizacaoSubTitle'
    import {actionTypes} from '@/core/constants'
    import {mapActions} from 'vuex'

    export default {
        name: 'DadosGeraisDoItemModal',
        components: {
            DadosGeraisDoItemVisualizacaoFormulario,
            subTitle: DadosGeraisDoItemVisualizacaoSubTitle,
        },
        data() {
            return {
                dialog: false,
                dadosDeEntrada: {
                    default: {}
                },
                itemIncorporacaoId: null,
                incorporacaoId: null
            }
        },
        async created() {
            this.setItemIncorporacaoId()
            this.setIncorporacaoId()
            await this.buscarDadosGeraisItemIncorporacao()
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO,
            ]),
            setItemIncorporacaoId(){
                if (this.$route.params.itemIncorporacaoId) {
                    this.itemIncorporacaoId = this.$route.params.itemIncorporacaoId
                }
            },
            setIncorporacaoId(){
                if(this.$route.params.incorporacaoId){
                    this.incorporacaoId = this.$route.params.incorporacaoId
                }
            },
            liberarContinuar(condicao) {
                this.camposObrigatoriosNaoPreenchidos = condicao
                this.$emit('verificarContinuar', condicao)
            },
            async buscarDadosGeraisItemIncorporacao() {
                this.desabilitarLoadingGlobal()
                const resultado = await this.buscarItemIncorporacao({idItem: this.itemIncorporacaoId, idIncorporacao: this.incorporacaoId})
                this.habilitarLoadingGlobal()
                this.dadosDeEntrada = resultado
                this.dadosDeEntrada.numeracaoPatrimonial = 'AUTOMATICA'
            },
        },
    }
</script>

<style scoped lang="stylus">
    .close__button::before
        background: transparent

    .acoes
        height 100%
        padding 0px 20px 15px

    .styleHeader
        font-size 17px

    .styleHeader span
        font-weight bold

    .v-dialog:not(.v-dialog--fullscreen)
        max-height 100% !important
</style>
