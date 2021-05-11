<template>
    <div class="movimentacao-edicao-itens">
        <movimentacao-interna-listagem-itens
            v-if="podeContinuar"
            :itens="itens"
            :paginas="paginas"
            :total-itens="totalItens"
            @buscarTodosItens="buscarTodosItens"
            @adicionarItem="tratarEventoAdicionarItem"/>

        <movimentacao-interna-listagem-itens-vazia v-else @adicionarItem="tratarEventoAdicionarItem"/>

        <router-view @buscarItensAdicionados="buscarTodosItens"/>

        <acoes-botoes-continuar-voltar :controleContinuar="podeContinuar" @voltar="voltar" @continuar="continuar"/>
    </div>

</template>

<script>
    import {mapActions} from 'vuex'
    import {actionTypes} from '@/core/constants'
    import AcoesBotoesContinuarVoltar from '@/views/components/acoes/AcoesBotoesContinuarVoltar'
    import MovimentacaoInternaListagemItensVazia from './MovimentacaoInternaListagemItensVazia'
    import MovimentacaoInternaListagemItens from './MovimentacaoInternaListagemItens'

    export default {
        name: 'MovimentacaoInternaEdicaoItens',
        components: {
            MovimentacaoInternaListagemItens,
            MovimentacaoInternaListagemItensVazia, AcoesBotoesContinuarVoltar
        },
        async mounted() {
            await this.buscarTodosItens()
        },
        data() {
            return {
                itens: [],
                paginas: 0,
                totalItens: 0,
            }
        },
        computed: {
            podeContinuar() {
                return this.itens.length > 0
            }
        },
        methods: {
            ...mapActions([actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]),
            voltar() {
                this.$router.replace({name: 'MovimentacaoInternaEdicaoDadosGerais', params: {movimentacaoInternaId: this.$route.params.movimentacaoInternaId}})
            },
            continuar() {
                this.$router.replace({
                    name: 'MovimentacaoInternaEdicaoDocumentos',
                    params: {movimentacaoInternaId: this.$route.params.movimentacaoInternaId}
                })
            },
            async buscarTodosItens() {
                const resultado = await this.buscarTodosItensAdicionadosMovimentacaoInterna(this.$route.params.movimentacaoInternaId)
                if (resultado) {
                    this.itens = resultado.items
                    this.paginas = resultado.totalPages
                    this.totalItens = resultado.totalElements
                }
                this.controlaPasso4()
            },
            controlaPasso4() {
                if (this.podeContinuar) {
                    this.habilitaPasso4()
                } else {
                    this.desabilitaPasso4()
                }
            },
            tratarEventoAdicionarItem(){
                this.$router.replace({name:'ModalMovimentacaoInternaListagemItens', params:{ 'movimentacaoInternaId': this.$route.params.movimentacaoInternaId}})
            },
            habilitaPasso4(){
                this.$emit('habilitaPasso4')
            },
            desabilitaPasso4(){
                this.$emit('desabilitaPasso4')
            }
        }
    }
</script>

<style scoped lang="stylus">
.movimentacao-edicao-itens
    min-height 62vh
</style>
