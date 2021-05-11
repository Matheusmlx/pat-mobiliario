<template>
    <div class="text-start" v-if="visualizacao">
        <v-menu offset-y open-on-hover>
            <template v-slot:activator="{on,attrs}">
                <v-btn
                    icon
                    color="white"
                    dark
                    v-bind="attrs"
                    v-on="on"
                    :disabled="carregando"
                    :loading="carregando">
                    <v-icon :size="sizeButton">description</v-icon>
                    <v-icon class="ml-5 mt-1" :size="15">keyboard_arrow_down</v-icon>
                </v-btn>
            </template>
            <v-list>
                <v-list-item v-for="(item, index) in items" :key="index" link>
                    <v-list-item-title @click="tratarEventoGerarDocumento(item.value)" :value="item">{{ item.title }}</v-list-item-title>
                </v-list-item>
            </v-list>
        </v-menu>
    </div>
    <div v-else>
        <v-tooltip
                   nudge-top="0"
                   top
                   transition="scale-transition"
                   origin="center center"
                   max-width="300">
            <template v-slot:activator="{ on }">
                <v-btn
                    color="grey"
                    text
                    v-on="on"
                    outlined
                    small
                    width="100"
                    class="buttonMemorando mr-3 text-body-2"
                    @click="tratarEventoGerarDocumento('MEMORANDO')"
                    :disabled="carregando"
                    :loading="carregando">
                    <span class="text-capitalize">Memorando</span>
                </v-btn>
            </template>
            Esse memorando ainda não tem valor contábil final, visto que a movimentação ainda não foi finalizada
        </v-tooltip>
    </div>
</template>

<script>
    import {actionTypes} from '@/core/constants'
    import {mapActions} from 'vuex'

    export default {
        data() {
            return {
                items: [
                    { title: 'Termo Responsabilidade',value:'TERMORESPONSABILIDADE' },
                    { title:'Memorando',value:'MEMORANDO'}
                ],
                carregando:false
            }
        },
        props:{
            sizeButton:{
                type:Number,
                default:25
            },
            visualizacao: {
                type: Boolean,
                default: false,
                required: true
            },
            movimentacaoInternaId:{
                required:true
            },
            movimentacaoTipo:{
                type:String
            }
        },
        methods:{
            ...mapActions([
                actionTypes.MOVIMENTACAO_INTERNA.RELATORIO.GERAR_TERMO_DE_RESPONSABILDADE,
                actionTypes.MOVIMENTACAO_INTERNA.RELATORIO.GERAR_MEMORANDO_MOVIMENTACAO_EM_ELABORACAO,
                actionTypes.MOVIMENTACAO_INTERNA.RELATORIO.GERAR_MEMORANDO_MOVIMENTACAO_FINALIZADA,
                actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.BUSCAR_TEMPORARIA_POR_ID
            ]),
            async tratarEventoGerarDocumento(value){
                this.desabilitarLoadingGlobal()
                this.setaCarregando(true)
                try {
                    if (value === 'TERMORESPONSABILIDADE') {
                        await this.gerarTermoDeResponsabilida(this.movimentacaoInternaId)
                    } else {
                        await this.gerarMemorando()
                    }
                    this.setaCarregando(false)
                } catch (err) {
                    this.setaCarregando(false)
                    this.mostraErro(err)
                }
                this.habilitarLoadingGlobal()
            },
            mostraErro(err){
                let dataView = new DataView(err.response.data)
                let decoder = new TextDecoder('utf8')
                let response = JSON.parse(decoder.decode(dataView))
                let message = response['message']
                this.mostrarNotificacaoErro(message)
            },
            setaCarregando(value){
                this.carregando = value
            },

            async gerarMemorando() {
                let movimentacao = await this.buscarTemporariaPorId(this.movimentacaoInternaId)
                if (movimentacao.tipo === 'TEMPORARIA') {
                    await this.gerarMemorandoParaMovimentacaoTemporaria(movimentacao)
                }else {
                    await this.gerarMemorandoParaMovimentacaoNaoTemporaria(movimentacao)
                }
            },
            async gerarMemorandoParaMovimentacaoTemporaria(movimentacao) {
                if (movimentacao.situacao === 'EM_ELABORACAO' || movimentacao.situacao === 'AGUARDANDO_DEVOLUCAO' || movimentacao.situacao === 'DEVOLVIDO_PARCIAL') {
                    await this.gerarMemorandoMovimentacaoEmElaboracao(this.movimentacaoInternaId)
                }else {
                    await this.gerarMemorandoMovimentacaoFinalizada(this.movimentacaoInternaId)
                }
            },
            async gerarMemorandoParaMovimentacaoNaoTemporaria(movimentacao) {
                if (movimentacao.situacao === 'EM_ELABORACAO') {
                    await this.gerarMemorandoMovimentacaoEmElaboracao(this.movimentacaoInternaId)
                }

                else {
                    await this.gerarMemorandoMovimentacaoFinalizada(this.movimentacaoInternaId)
                }
            }
        }
    }
</script>
