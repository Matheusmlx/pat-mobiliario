<template>
    <div>
        <az-back-button :route="{name: rotaOrigem}" text="Voltar para listagem"/>
        <az-container class="corpo-conteiner">
            <visualizar-registro-incorporacao-cabecalho
                :incorporacao="incorporacao"
                @reabrirIncorporacao="tratarEventoReabrirIncorporacao"
            />

            <visualizar-registro-incorporacao-dados-gerais :incorporacao="incorporacao"/>

            <visualizar-registro-incorporacao-empenho
                :incorporacaoId="incorporacaoId"
                v-if="incorporacaoId"
            />

            <visualizar-registro-incorporacao-item
                :incorporacaoId="incorporacaoId"
                v-if="incorporacaoId"
            />

            <visualizar-registro-incorporacao-itens-estornados
                :incorporacaoId="incorporacaoId"
                @mostrarMotivoEstorno="mostrarMotivoEstorno"
                v-if="incorporacaoId"
            />

            <incorporacao-detalhe-documentos
                :incorporacaoId="incorporacaoId"
                v-if="incorporacaoId"
            />

            <router-view/>
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
    import VisualizarRegistroIncorporacaoItensEstornados
        from '@/views/pages/patrimonios/incorporacao/visualizacaoRegistro/VisualizarRegistroIncorporacaoItensEstornados'
    import IncorporacaoDetalheDocumentos from './VisualizarRegistroIncorporacaoDocumentos'
    import {mapActions, mapMutations} from 'vuex'
    import {actionTypes, mutationTypes} from '@/core/constants'

    export default {
        name: 'VisualizarRegistroIncorporacao',
        components: {
            VisualizarRegistroIncorporacaoItensEstornados,
            VisualizarRegistroIncorporacaoDadosGerais,
            VisualizarRegistroIncorporacaoCabecalho,
            VisualizarRegistroIncorporacaoEmpenho,
            VisualizarRegistroIncorporacaoItem,
            IncorporacaoDetalheDocumentos
        },
        data() {
            return {
                rotaOrigem: this.$store.state.incorporacao.rota.origem,
                incorporacao: {},
                incorporacaoId: null,
                estorno: {}
            }
        },
        async mounted() {
            this.setIncorporacaoId(Number.parseInt(this.$route.params.incorporacaoId))
            await this.buscarIncorporacao()
        },
        async beforeRouteUpdate(to, from, next) {
            this.setIncorporacaoId(to.params.incorporacaoId)
            await this.buscarIncorporacao()
            next()
        },
        watch:{
            async '$store.state.incorporacao.buscarRegistro'(){
                await this.buscarIncorporacao()
            }
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_REGISTRO_INCORPORACAO_POR_ID,
                actionTypes.PATRIMONIO.INCORPORACAO.REABRIR_INCORPORACAO
            ]),
            ...mapMutations([
                mutationTypes.PATRIMONIO.SET_ESTORNO
            ]),

            async buscarIncorporacao() {
                this.incorporacao = await this.buscarRegistroIncorporacaoPorId(this.incorporacaoId)
            },
            async tratarEventoReabrirIncorporacao(incorporacaoId) {
                await this.reabrirIncorporacao(incorporacaoId)
                await this.$router.push({name: 'ItensIncorporacaoListagem'})
            },
            setIncorporacaoId(incorporacaoId) {
                this.incorporacaoId = incorporacaoId
            },
            mostrarMotivoEstorno(estorno) {
                this.setEstorno(estorno)
                this.$router.push({
                    name: 'ModalEstornarPatrimoniosVisualizarEstorno'
                })
            }
        }
    }
</script>

<style scoped lang="stylus">
    .corpo-conteiner
        min-height 80vh
</style>
