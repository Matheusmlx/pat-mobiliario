<template>
    <div>
        <az-back-button :route="{name: rotaOrigem}" text="Voltar para a Listagem"/>

        <az-container class="corpo-container">
            <visualizar-registro-movimentacao-interna-cabecalho
                :movimentacao="movimentacao"
                :eh-destinatario="ehDestinatario"
                @devolverTodosPatrimonios="tratarDevolucaoPatrimonios"
                v-if="movimentacao"
            />

            <visualizar-registro-movimentacao-interna-dados-gerais
                :movimentacao="movimentacao"
                :tooltip="tooltip"
                @editar="tratarEventoEdicao"
                v-if="movimentacao"
            />

            <visualizar-registro-movimentacao-interna-item
                :movimentacao="movimentacao"
                v-if="movimentacao"
            />

            <visualizar-registro-movimentacao-interna-documentos
                :movimentacaoInternaId="movimentacaoInternaId"
                v-if="movimentacaoInternaId"
            />

            <router-view @buscarInformacoesAtualizadasMovimentacao="buscarInformacoesAtualizadasMovimentacao"/>
        </az-container>
    </div>
</template>

<script>
    import VisualizarRegistroMovimentacaoInternaCabecalho from './VisualizarRegistroMovimentacaoInternaCabecalho'
    import VisualizarRegistroMovimentacaoInternaDadosGerais from './VisualizarRegistroMovimentacaoInternaDadosGerais'
    import VisualizarRegistroMovimentacaoInternaItem from './VisualizarRegistroMovimentacaoInternaItem'
    import VisualizarRegistroMovimentacaoInternaDocumentos from './VisualizarRegistroMovimentacaoInternaDocumentos'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import {mapActions} from 'vuex'

    export default {
        name: 'VisualizarRegistroMovimentacaoInterna',
        components: {
            VisualizarRegistroMovimentacaoInternaDadosGerais,
            VisualizarRegistroMovimentacaoInternaCabecalho,
            VisualizarRegistroMovimentacaoInternaItem,
            VisualizarRegistroMovimentacaoInternaDocumentos
        },
        data() {
            return {
                rotaOrigem: 'MovimentacaoInternaListagem',
                movimentacao: null,
                movimentacaoInternaId: null,
                tooltip: '',
                ehDestinatario: false
            }
        },
        async mounted() {
            this.setarIdMovimentacaoInterna(Number.parseInt(this.$route.params.movimentacaoInternaId))
            await this.buscarMovimentacaoInterna()
            await this.buscarTooltipMovimentacaoTemporaria()
            await this.validarUsuarioEDestinatario()
        },
        async beforeRouteUpdate(to, from, next) {
            this.setarIdMovimentacaoInterna(to.params.movimentacaoInternaId)
            await this.buscarMovimentacaoInterna()
            await this.buscarTooltipMovimentacaoTemporaria()
            await this.validarUsuarioEDestinatario()
            next()
        },
        methods: {
            ...mapActions([
                actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.ATUALIZAR_MOVIMENTACAO_INTERNA_VISUALIZACAO_REGISTRO,
                actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.BUSCAR_MOVIMENTACAO_INTERNA_VISUALIZACAO_REGISTRO,
                actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.DEVOLVER_TODOS_PATRIMONIOS,
                actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.BUSCAR_MOVIMENTACAO_INTERNA_VISUALIZACAO_REGISTRO,
                actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.BUSCAR_INFORMACAO_TOOLTIP,
                actionTypes.COMUM.BUSCAR_SETORES_POSSUI_ACESSO
            ]),
            setarIdMovimentacaoInterna(movimentacaoInternaId) {
                this.movimentacaoInternaId = movimentacaoInternaId
            },
            async tratarDevolucaoPatrimonios() {
                try {
                    await this.devolverTodosPatrimonios(this.movimentacaoInternaId)
                    this.mostrarNotificacaoSucesso('Operação realizada com sucesso')
                }catch (err){
                    this.mostrarNotificacaoErro(err.response.data.message)
                    this.$store.commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
                }finally {
                    await this.buscarMovimentacaoInterna()
                }
            },
            async tratarEventoEdicao(dados) {
                try {
                    await this.atualizarMovimentacaoInternaVisualizacaoRegistro(dados)
                } catch (err) {
                    this.mostrarNotificacaoErro(err.response.data.message)
                    this.$store.commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
                } finally {
                    await this.buscarMovimentacaoInterna()
                }
            },
            async buscarMovimentacaoInterna() {
                this.movimentacao = await this.buscarMovimentacaoInternaVisualizacaoRegistro(this.movimentacaoInternaId)
            },
            async buscarTooltipMovimentacaoTemporaria() {
                if (this.movimentacao.tipo === 'TEMPORARIA' && this.movimentacao.situacao === 'DEVOLVIDO_PARCIAL') {
                    const resultado = await this.buscarInformacaoTooltip(this.movimentacao.id)
                    this.tooltip = resultado.razaoPatrimoniosDevolvidos
                }
            },
            async buscarInformacoesAtualizadasMovimentacao() {
                await this.buscarMovimentacaoInterna()
                await this.buscarTooltipMovimentacaoTemporaria()
            },
            async validarUsuarioEDestinatario() {
                const resultado = await this.buscarSetoresPossuiAcesso()
                resultado.items.forEach(setor => {
                    if (setor.descricao === this.movimentacao.setorDestino) {
                        this.ehDestinatario = true
                    }
                })
            }
        }
    }
</script>

<style scoped lang="stylus">
    .corpo-container
        min-height 80vh
</style>
