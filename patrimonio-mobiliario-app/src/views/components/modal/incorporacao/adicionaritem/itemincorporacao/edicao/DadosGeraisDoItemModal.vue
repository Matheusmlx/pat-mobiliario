<template>
    <div>
        <subTitle :value="dadosDeEntrada"/>
        <v-divider></v-divider>
        <DadosGeraisDoItemModalFormulario
            :dadosDeEntrada="dadosDeEntrada"
            :naturezasDespesa="naturezasDespesa"
            :estadosConservacao="estadosConservacao"
            :anoFabricacaoModelo="anoFabricacaoModelo"
            :naturezaDespesaInativo="naturezaDespesaInativo"
            :apresentaTooltipImagem="apresentaTooltipImagem"
            :errorObrigatoriedadeAnexo="errorObrigatoriedadeAnexo"
            :errorTamanhoExcedido="errorTamanhoExcedido"
            @editarItem="salvarDadosGerais"
            @salvarAnexo="tratarEventoSalvarAnexo"
            @bloquearPasso3="liberarContinuar"
        />
    </div>
</template>


<script>
    import DadosGeraisDoItemModalFormulario from './DadosGeraisDoItemModalFormulario'
    import DadosGeraisDoitemModalSubTitle from './DadosGeraisDoItemModalSubTitle'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import {mapActions, mapMutations} from 'vuex'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    export default {
        name: 'DadosGeraisDoItemModal',
        components: {
            DadosGeraisDoItemModalFormulario,
            subTitle: DadosGeraisDoitemModalSubTitle,
        },
        data() {
            return {
                camposObrigatoriosNaoPreenchidos: true,
                dialog: false,
                dadosDeEntrada: {
                    default: {}
                },
                itemIncorporacaoId: null,
                incorporacaoId: null,
                naturezasDespesa: [],
                estadosConservacao: [],
                anoFabricacaoModelo: [],
                naturezaDespesaInativo: false,
                apresentaTooltipImagem: false,
                errorObrigatoriedadeAnexo: 0,
                errorTamanhoExcedido: 0,
            }
        },
        async mounted() {
            this.geraAnoModeloFabricacao()
            this.setItemIncorporacaoId()
            this.setIncorporacaoId()
            if (!this.verificaPermissaoEdicao()) {
                await this. etapaDoisVisualizacao()
            }
            await this.buscarDadosGeraisItemIncorporacao()
            await this.buscarNaturezasDespesa()
            await this.buscarTodosEstadosConservacao()
            this.trataSeNaturezaDespesaInativo()
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.IMAGEM.UPLOAD,
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO,
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO,
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.ESTADO_CONSERVACAO.BUSCAR_ESTADOS_CONSERVACAO
            ]),
            ...mapMutations([
                mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DESATIVAR_SALVAMENTO_AUTOMATICO,
                mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_INCORPORACAO_ITEM
            ]),
            async salvarDadosGerais(novoItem) {
                this.desabilitarLoadingGlobal()
                if(novoItem.naturezaDespesa && typeof novoItem.naturezaDespesa.id !== 'undefined'){
                    novoItem.naturezaDespesa = novoItem.naturezaDespesa.id
                }
                this.dadosDeEntrada = novoItem
                this.dadosDeEntrada.idIncorporacao = this.incorporacaoId
                this.setIncorporacaoItem(this.dadosDeEntrada)

                await this.tratarEventoUploadImagem(novoItem)
                const {contaContabil, tipoBem, naturezaDespesa} = await this.editarItemIncorporacao(this.dadosDeEntrada)
                this.dadosDeEntrada.contaContabil = contaContabil
                this.dadosDeEntrada.tipoBem = tipoBem
                this.dadosDeEntrada.naturezaDespesa = naturezaDespesa
                this.trataSeNaturezaDespesaInativo()
            },
            async tratarEventoSalvarAnexo(novoItem) {
                if(novoItem && novoItem.uriImagem) {
                    if (!this.extensaoArquivoValida(novoItem.uriImagem)) {
                        this.mostrarNotificacaoErro(
                            'Extensão do arquivo inválida. Envie arquivos nos seguintes formatos .jpeg .jpg .png'
                        )
                        await this.buscarDadosGeraisItemIncorporacao()
                    } else {
                        if (this.validarTamanhoMaximo(novoItem.uriImagem.size)) {
                            this.errorTamanhoExcedido = 1
                            this.dadosDeEntrada.uriImagem = await null
                            setTimeout(() => {
                                this.errorTamanhoExcedido = 0
                                this.errorObrigatoriedadeAnexo = 1
                                this.$validator._base.validateAll()
                            }, 3000)
                        } else {
                            await this.salvarDadosGerais(novoItem)
                        }
                    }
                }
                this.apresentaTooltipImagem = false
            },
            liberarContinuar(condicao) {
                this.camposObrigatoriosNaoPreenchidos = condicao
                this.verificarSituacaoItemIncorporacao(condicao)
                this.setIncorporacaoItem(this.dadosDeEntrada)
                this.$emit('verificarContinuar', condicao)
            },
            async buscarNaturezasDespesa() {
                this.desabilitarLoadingGlobal()
                const resultado = await this.$store.dispatch(
                    actionTypes.COMUM.BUSCAR_NATUREZAS_DESPESA,
                    this.dadosDeEntrada.codigo
                )
                this.habilitarLoadingGlobal()
                if (resultado) {
                    if (resultado.items.length === 0) {
                        this.mostrarNotificacaoAviso('Item sem conta contábil, natureza de despesa ou tipo ativos. Escolha outro item')
                    }
                    this.naturezasDespesa = resultado.items
                    this.selecionarNaturezaDespesaCasoTenhaUmaApenas(resultado.items)
                }
            },
            async buscarTodosEstadosConservacao() {
                const {estadosConservacao} = await this.buscarEstadosConservacao()
                this.estadosConservacao = estadosConservacao
            },
            selecionarNaturezaDespesaCasoTenhaUmaApenas(naturezasDespesa) {
                if(!this.dadosDeEntrada.naturezaDespesa && naturezasDespesa.length === 1) {
                    this.dadosDeEntrada.naturezaDespesa = naturezasDespesa[0].id
                    this.salvarDadosGerais(this.dadosDeEntrada)
                }
            },
            geraAnoModeloFabricacao() {
                const anoMinimo = 1980
                const anoAtual = new Date().getFullYear()
                for (let ano = anoAtual; ano >= anoMinimo; ano--) {
                    let anoFabricacao = parseInt(ano)
                    let anoModeloMaior = parseInt(ano + 1)
                    this.anoFabricacaoModelo.push(anoFabricacao + '/' + anoModeloMaior)
                    this.anoFabricacaoModelo.push(anoFabricacao + '/' + anoFabricacao)
                }
            },
            trataSeNaturezaDespesaInativo(){
                if(this.dadosDeEntrada && this.dadosDeEntrada.naturezaDespesa && this.dadosDeEntrada.naturezaDespesa.situacao === 'INATIVO'){
                    this.dadosDeEntrada.naturezaDespesa.disabled = true
                    this.naturezaDespesaInativo = true
                    this.naturezasDespesa.unshift(this.dadosDeEntrada.naturezaDespesa)
                    this.dadosDeEntrada.naturezaDespesa = this.dadosDeEntrada.naturezaDespesa.id
                }else{
                    this.naturezaDespesaInativo = false
                }
            },
            extensaoArquivoValida(arquivo) {
                const formatosAceitos = [
                    'image/png',
                    'image/jpg',
                    'image/jpeg'
                ]

                return formatosAceitos.includes(arquivo.type)
            },
            validarTamanhoMaximo(tamanhoAnexo) {
                return tamanhoAnexo >= 15116247
            },
            verificarSituacaoItemIncorporacao(estaInvalido){
                if(estaInvalido) {
                    this.dadosDeEntrada.situacao = 'EM_ELABORACAO'
                }
            },
            async tratarEventoUploadImagem(itemAnexo) {
                this.desabilitarLoadingGlobal()
                if (itemAnexo.uriImagem && typeof itemAnexo.uriImagem == 'object') {
                    this.dadosDeEntrada.uriImagem = await this.uploadImagem(itemAnexo)
                }
                this.habilitarLoadingGlobal()
            },
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
            async buscarDadosGeraisItemIncorporacao() {
                this.desabilitarLoadingGlobal()
                const resultado = await this.buscarItemIncorporacao({idItem: this.itemIncorporacaoId, idIncorporacao: this.incorporacaoId})
                this.habilitarLoadingGlobal()
                this.dadosDeEntrada = resultado
                this.dadosDeEntrada.valorTotalAnterior = resultado.valorTotal
                this.dadosDeEntrada.numeracaoPatrimonial = 'AUTOMATICA'
                this.setIncorporacaoItem(this.dadosDeEntrada)
            },
            etapaDoisVisualizacao(){
                this.$router.push({
                    name: 'VisualizacaoDadosGerais',
                    itemIncorporacaoId: this.itemIncorporacaoId,
                    incorporacaoId: this.incorporacaoId
                })
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, [
                    'Mobiliario.Normal',
                ])
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
