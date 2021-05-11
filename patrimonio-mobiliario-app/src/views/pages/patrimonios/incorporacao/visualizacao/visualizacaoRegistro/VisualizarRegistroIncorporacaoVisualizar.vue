<template>
    <div>
        <az-back-button :route="{name: rotaOrigem}" text="Voltar para listagem"/>
        <modal-itens-registro-incorporacao v-if="modalItem" v-model="modalItem" @fecharModal="fecharModalItem"/>
        <az-container class="corpo-conteiner">
            <visualizar-registro-incorporacao-cabecalho :incorporacao="incorporacao"/>

            <visualizar-registro-incorporacao-dados-gerais :incorporacao="incorporacao"/>

            <visualizar-registro-incorporacao-empenho :incorporacaoId="incorporacao.id"/>

            <visualizar-registro-incorporacao-item @acessar="tratarEventoAbrirModalItem"/>

            <visualizar-registro-incorporacao-documentos-visualizar/>
        </az-container>
    </div>
</template>

<script>
    import VisualizarRegistroIncorporacaoCabecalho
        from '@/views/pages/patrimonios/incorporacao/visualizacaoRegistro/VisualizarRegistroIncorporacaoCabecalho'
    import VisualizarRegistroIncorporacaoDadosGerais
        from '@/views/pages/patrimonios/incorporacao/visualizacaoRegistro/VisualizarRegistroIncorporacaoDadosGerais'
    import VisualizarRegistroIncorporacaoEmpenho
        from '@/views/pages/patrimonios/incorporacao/visualizacaoRegistro/VisualizarRegistroIncorporacaoEmpenho'
    import VisualizarRegistroIncorporacaoItem
        from '@/views/pages/patrimonios/incorporacao/visualizacaoRegistro/VisualizarRegistroIncorporacaoItem'
    import {mapActions} from 'vuex'
    import {actionTypes} from '@/core/constants'
    import ModalItensRegistroIncorporacao
        from '@/views/components/modal/incorporacao/registroIncorporacao/ModalItensRegistroIncorporacao'
    import VisualizarRegistroIncorporacaoDocumentosVisualizar
        from './VisualizarRegistroIncorporacaoDocumentosVisualizar'

    export default {
        name: 'VisualizarRegistroIncorporacao',
        components: {
            VisualizarRegistroIncorporacaoDocumentosVisualizar,
            ModalItensRegistroIncorporacao,
            VisualizarRegistroIncorporacaoDadosGerais,
            VisualizarRegistroIncorporacaoCabecalho,
            VisualizarRegistroIncorporacaoEmpenho,
            VisualizarRegistroIncorporacaoItem,
        },
        data() {
            return {
                rotaOrigem: this.$store.state.incorporacao.rota.origem,
                incorporacao: {},
                incorporacaoId: null,
                modalItem: false,
            }
        },
        async mounted() {
            this.setIncorporacaoId()
            this.verificarRotaModal()
            await this.buscarIncorporacao()
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_REGISTRO_INCORPORACAO_POR_ID
            ]),
            async buscarIncorporacao() {
                this.incorporacao = await this.buscarRegistroIncorporacaoPorId(this.incorporacaoId)
            },
            tratarEventoAbrirModalItem(item) {
                this.$router.push({
                    name: 'ModalItemVisualizarRegistroVisualizacao',
                    params: {incorporacaoId: this.incorporacaoId, itemIncorporacaoId: item.id}
                })
                this.modalItem = true
            },
            fecharModalItem() {
                this.$router.push({
                    name: 'VisualizarRegistroIncorporacaoVisualizacao',
                    params: {incorporacaoId: this.incorporacaoId}
                })
                this.modalItem = false
            },
            setIncorporacaoId() {
                this.incorporacaoId = this.$route.params.incorporacaoId
            },
            verificarRotaModal() {
                if (this.$route.name === 'ModalItemVisualizarRegistroVisualizacao') {
                    this.tratarEventoAbrirModalItem({id: this.$route.params.itemIncorporacaoId})
                }
            }
        }
    }
</script>

<style scoped lang="stylus">
    .corpo-conteiner
        min-height 80vh
</style>
