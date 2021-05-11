<template>
    <div class="corpo-conteiner">
        <ficha-patrimonio-dados-gerais-cabecalho :patrimonio="patrimonio"/>

        <ficha-patrimonio-dados-gerais-campos :patrimonio="patrimonio" @atualizarPatrimonio="atualizar"/>

        <ficha-patrimonio-dados-gerais-depreciacao :patrimonio="patrimonio"/>

        <router-view :depreciacoesDoBem="depreciacoesDoBem"/>
    </div>

</template>

<script>
    import {mapActions} from 'vuex'
    import {actionTypes} from '@/core/constants'
    import _ from 'lodash'
    import FichaPatrimonioDadosGeraisCabecalho
        from '@/views/pages/patrimonios/patrimonio/ficha-patrimonios/dados-gerais/FichaPatrimonioDadosGeraisCabecalho'
    import FichaPatrimonioDadosGeraisCampos
        from '@/views/pages/patrimonios/patrimonio/ficha-patrimonios/dados-gerais/FichaPatrimonioDadosGeraisCampos'
    import FichaPatrimonioDadosGeraisDepreciacao
        from '@/views/pages/patrimonios/patrimonio/ficha-patrimonios/dados-gerais/FichaPatrimonioDadosGeraisDepreciacao'
    export default {
        name: 'FichaPatrimonioDadosGerais',
        components: {
            FichaPatrimonioDadosGeraisDepreciacao,
            FichaPatrimonioDadosGeraisCampos, FichaPatrimonioDadosGeraisCabecalho},
        props: {
            dadosFicha: {
                type: Object,
                required: true
            }
        },
        data(){
            return {
                patrimonio:{},
                depreciacoesDoBem: []
            }
        },
        watch: {
            'dadosFicha'() {
                this.preencherPatrimonio()
            }
        },
        async mounted() {
            await this.buscarDepreciacoesDoBem()
            this.buscarPatrimonio()
        },
        methods:{
            ...mapActions([
                actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO,
                actionTypes.PATRIMONIO.BUSCAR_DEPRECIACOES,
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.IMAGEM.UPLOAD,
            ]),
            buscarPatrimonio() {
                this.$emit('buscarPatrimonio')
            },
            preencherPatrimonio() {
                this.patrimonio = _.cloneDeep(this.dadosFicha)
            },
            async atualizar(patrimonio){
                await this.tratarEventoUploadImagem(patrimonio)
                const patrimonioNovo = this.criaPatrimonioParaAtualizacao(patrimonio)
                const resultado = await this.atualizarPatrimonio(patrimonioNovo)
                if(resultado){
                    this.setaAtualizacaoDeCamposEditados(resultado)
                }
            },
            async tratarEventoUploadImagem(itemAnexo) {
                this.desabilitarLoadingGlobal()
                if (itemAnexo.imagem && typeof itemAnexo.imagem == 'object') {
                    itemAnexo = { uriImagem: itemAnexo.imagem }
                    this.patrimonio.uriImagem = await this.uploadImagem(itemAnexo)
                }
                if(itemAnexo.imagem === null){
                    this.patrimonio.uriImagem = null
                }
                this.habilitarLoadingGlobal()
            },
            criaPatrimonioParaAtualizacao(patrimonio){
                let patrimonioNovo = {}
                patrimonioNovo.id = patrimonio.id
                patrimonioNovo.uriImagem = this.patrimonio.uriImagem
                patrimonioNovo.placa = patrimonio.placa
                patrimonioNovo.renavam = patrimonio.renavam
                patrimonioNovo.licenciamento = patrimonio.licenciamento
                patrimonioNovo.motor = patrimonio.motor
                patrimonioNovo.chassi = patrimonio.chassi
                patrimonioNovo.numeroSerie = patrimonio.numeroSerie
                return patrimonioNovo
            },
            setaAtualizacaoDeCamposEditados(patrimonio){
                this.patrimonio.imagem = patrimonio.imagem
                this.patrimonio.placa = patrimonio.placa
                this.patrimonio.renavam = patrimonio.renavam
                this.patrimonio.licenciamento = patrimonio.licenciamento
                this.patrimonio.motor = patrimonio.motor
                this.patrimonio.chassi = patrimonio.chassi
                this.patrimonio.numeroSerie = patrimonio.numeroSerie
            },
            async buscarDepreciacoesDoBem() {
                this.desabilitarLoadingGlobal()
                this.depreciacoesDoBem = await this.buscarDepreciacoes(this.$route.params.patrimonioId)
                this.habilitarLoadingGlobal()
            },
        }
    }
</script>

<style scoped lang="stylus">
.corpo-conteiner
    min-height 75vh
</style>
