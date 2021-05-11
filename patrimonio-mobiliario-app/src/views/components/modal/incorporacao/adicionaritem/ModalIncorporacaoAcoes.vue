<template>
    <div>
        <acoes-botoes-continuar-cancelar
            v-if="this.$route.name === 'ItensCatalogoModalNovo' || this.$route.name === 'ItensCatalogoModalEdicao' ||
            this.$route.name === 'ItensCatalogoModalVisualizacao' ||
            this.$route.name === 'ItensCatalogoModalVisualizacaoCadastrado'"
            @cancelar="fecharModal"
            @continuar="continuar"
            :controleContinuar="podeContinuar"/>

        <acoes-botoes-continuar-voltar
            v-if="this.$route.name === 'DadosGeraisDoItemModal' || this.$route.name === 'VisualizacaoDadosGerais'"
            @voltar="voltar"
            @continuar="continuar"
            :controleContinuar="!podeContinuar"/>

        <acoes-botoes-finalizar-voltar-adicionar
            v-if="this.$route.name === 'IncorporacaoItemListagemPatrimonios' || this.$route.name === 'IncorporacaoItemListagemPatrimoniosVisualizacao'"
            :desabilitado="this.$route.name === 'IncorporacaoItemListagemPatrimoniosVisualizacao'"
            @adicionarOutro="adicionar"
            @finalizar="fecharModal"
            @voltar="voltar"/>
    </div>
</template>

<script>
    import AcoesBotoesContinuarVoltar from '@/views/components/acoes/AcoesBotoesContinuarVoltar'
    import AcoesBotoesContinuarCancelar from '@/views/components/acoes/AcoesBotoesContinuarCancelar'
    import AcoesBotoesFinalizarVoltarAdicionar from '@/views/components/acoes/AcoesBotoesFinalizarVoltarAdicionar'
    export default {
        name: 'ModalIncorporacaoAcoes',
        components: {AcoesBotoesContinuarCancelar, AcoesBotoesContinuarVoltar, AcoesBotoesFinalizarVoltarAdicionar},
        props: {
            etapa: Number,
            podeContinuar: {
                type: Boolean,
                default: false
            },
        },
        data() {
            return {
                podeVoltar: false
            }
        },
        methods: {
            adicionar(){
                this.$emit('voltarPassoNovo')
            },
            continuar() {
                if (this.$route.name === 'ItensCatalogoModalNovo') {
                    this.$emit('salvarNovoItem')
                } else if (this.$route.name === 'ItensCatalogoModalEdicao') {
                    this.$emit('editarItemPassoUm')
                } else if (this.$route.name === 'ItensCatalogoModalVisualizacaoCadastrado') {
                    this.$emit('avancarEtapaDoisVisualizar')
                } else if (this.$route.name === 'DadosGeraisDoItemModal'){
                    this.$emit('avancarEtapaTres')
                } else if (this.$route.name === 'VisualizacaoDadosGerais'){
                    this.$emit('avancarEtapaTresVisualizar')
                }
            },
            fecharModal() {
                this.$emit('fechar')
            },
            voltar() {
                if (this.$route.name === 'DadosGeraisDoItemModal') {
                    this.$emit('voltarPassoUmEdicao')
                } else if (this.$route.name === 'VisualizacaoDadosGerais') {
                    this.$emit('voltarPassoUmVisualizar')
                } else if (this.$route.name === 'IncorporacaoItemListagemPatrimonios') {
                    this.$emit('voltarPassoDoisEdicao')
                } else if (this.$route.name === 'IncorporacaoItemListagemPatrimoniosVisualizacao'){
                    this.$emit('voltarPassoDoisVisualizar')
                }
            }
        }
    }
</script>

<style scoped lang="stylus">
    .acoes
        height 100%

    .btn-adicionar-modal3
        margin-right 10px
        pointer-events visible !important
        color #999 !important
</style>
