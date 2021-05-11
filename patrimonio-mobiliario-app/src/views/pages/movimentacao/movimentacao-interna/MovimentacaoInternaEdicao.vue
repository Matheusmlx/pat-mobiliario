<template>
    <div v-if="habilitarEdicaoMovimentacao">
        <az-back-button :route="{ name: rotaOrigem }" text="Voltar para listagem"/>

        <az-container class="movimentacao-interna-container">
            <v-stepper v-model="passoAtual.numero">
                <v-stepper-header class="elevation-0 barra-passo">
                    <v-spacer/>

                    <v-stepper-step
                        :complete="passos[0].numero < passoAtual.numero"
                        :disabled="passos[0].desabilitado"
                        :editable="!passos[0].desabilitado"
                        :key="`${passos[0].numero}-header`"
                        :step="passos[0].numero"
                        @click="tratarEventoClick(passos[0])"
                        edit-icon="fas fa-check">
                        {{ passos[0].titulo }}
                    </v-stepper-step>

                    <v-divider/>

                    <v-tooltip
                        nudge-left="10"
                        nudge-top="-22"
                        max-width="200"
                        top>
                        <template v-slot:activator="{ on }">
                            <div v-on="on" v-show="passos[1].desabilitado">
                                <v-stepper-step
                                    :complete="passos[1].numero < passoAtual.numero"
                                    :disabled="passos[1].desabilitado"
                                    :editable="!passos[1].desabilitado"
                                    :key="`${passos[1].numero}-header`"
                                    :step="passos[1].numero"
                                    @click="tratarEventoClick(passos[1])"
                                    edit-icon="fas fa-check"
                                    :class="{'passo-ativo' : !passos[1].desabilitado}"
                                >{{ passos[1].titulo }}
                                </v-stepper-step>
                            </div>
                            <v-stepper-step
                                :complete="passos[1].numero < passoAtual.numero"
                                :disabled="passos[1].desabilitado"
                                :editable="!passos[1].desabilitado"
                                :key="`${passos[1].numero}-header`"
                                :step="passos[1].numero"
                                @click="tratarEventoClick(passos[1])"
                                edit-icon="fas fa-check"
                                v-show="!passos[1].desabilitado"
                                :class="{'passo-ativo' : !passos[1].desabilitado}"
                            >{{ passos[1].titulo }}
                            </v-stepper-step>
                        </template>
                        É necessário que todos os campos obrigatórios sejam preenchidos para continuar.
                    </v-tooltip>

                    <v-divider/>

                    <v-tooltip
                        nudge-left="10"
                        nudge-top="-20"
                        max-width="200"
                        top>
                        <template v-slot:activator="{ on }">
                            <div v-on="on" v-show="passos[2].desabilitado && !passos[1].desabilitado">
                                <v-stepper-step
                                    :complete="passos[2].numero < passoAtual.numero"
                                    :disabled="passos[2].desabilitado"
                                    :editable="!passos[2].desabilitado"
                                    :key="`${passos[2].numero}-header`"
                                    :step="passos[2].numero"
                                    @click="tratarEventoClick(passos[2])"
                                    edit-icon="fas fa-check"
                                    :class="{'passo-ativo' : !passos[2].desabilitado}"
                                >{{ passos[2].titulo }}
                                </v-stepper-step>
                            </div>
                            <v-stepper-step
                                :complete="passos[2].numero < passoAtual.numero"
                                :disabled="passos[2].desabilitado"
                                :editable="!passos[2].desabilitado"
                                :key="`${passos[2].numero}-header`"
                                :step="passos[2].numero"
                                @click="tratarEventoClick(passos[2])"
                                edit-icon="fas fa-check"
                                v-show="!passos[2].desabilitado || passos[1].desabilitado"
                                :class="{'passo-ativo' : !passos[2].desabilitado}"
                            >{{ passos[2].titulo }}
                            </v-stepper-step>
                        </template>
                        É necessário que todos os campos obrigatórios sejam preenchidos para continuar.
                    </v-tooltip>

                    <v-divider/>

                    <v-tooltip
                        nudge-left="10"
                        nudge-top="-20"
                        max-width="200"
                        top>
                        <template v-slot:activator="{ on }">
                            <div v-on="on" v-show="passos[3].desabilitado && !passos[2].desabilitado">
                                <v-stepper-step
                                    :complete="passos[3].numero < passoAtual.numero"
                                    :disabled="passos[3].desabilitado"
                                    :editable="!passos[3].desabilitado"
                                    :key="`${passos[3].numero}-header`"
                                    :step="passos[3].numero"
                                    @click="tratarEventoClick(passos[3])"
                                    edit-icon="fas fa-check"
                                    :class="{'passo-ativo' : !passos[3].desabilitado}"
                                >{{ passos[3].titulo }}
                                </v-stepper-step>
                            </div>
                            <v-stepper-step
                                :complete="passos[3].numero < passoAtual.numero"
                                :disabled="passos[3].desabilitado"
                                :editable="!passos[3].desabilitado"
                                :key="`${passos[3].numero}-header`"
                                :step="passos[3].numero"
                                @click="tratarEventoClick(passos[3])"
                                edit-icon="fas fa-check"
                                v-show="!passos[3].desabilitado || passos[2].desabilitado"
                                :class="{'passo-ativo' : !passos[3].desabilitado}"
                            >{{ passos[3].titulo }}
                            </v-stepper-step>
                        </template>
                        É necessário que todos os campos obrigatórios sejam preenchidos para continuar.
                    </v-tooltip>

                    <v-spacer/>
                    <div v-if="passoAtual.numero !== 1">
                        <documento-button :movimentacaoInternaId="movimentacaoInternaId" :visualizacao="false"/>
                    </div>
                    <div>
                        <botao-remover @remover="tratarEventoDeletarMovimentacaoInterna"
                                       v-if="movimentacaoInternaId && verificaPermissaoEdicao"/>
                    </div>
                </v-stepper-header>

                <v-divider/>

                <v-stepper-items>
                    <v-stepper-content
                        :key="`${passo.numero}-content`"
                        :step="passo.numero"
                        v-for="passo in passos"></v-stepper-content>
                    <router-view @desabilitaPasso2="desabilitaPasso2"
                                 @habilitaPasso2="habilitaPasso2"
                                 @desabilitaPasso3="desabilitaPasso3"
                                 @habilitaPasso3="habilitaPasso3"
                                 @desabilitaPasso4="desabilitaPasso4"
                                 @habilitaPasso4="habilitaPasso4"/>
                </v-stepper-items>
            </v-stepper>
        </az-container>
    </div>
</template>

<script>
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import {mutationTypes} from '@/core/constants'
    import BotaoRemover from '@/views/components/botoes/BotaoRemover'
    import { mapActions, mapMutations } from 'vuex'
    import actionTypes from '@/core/constants/actionTypes'
    import DocumentoButton from '@/views/components/botoes/DocumentoButton.vue'

    export default {
        name: 'MovimentacaoInternaEdicao',
        components: {BotaoRemover,DocumentoButton},
        data() {
            return {
                rotaOrigem: this.$store.state.movimentacaointerna.rota.origem,
                passos: [
                    {
                        titulo: 'Tipo',
                        rotaNovo: 'MovimentacaoInternaNovoTipo',
                        rotaEdicao: 'MovimentacaoInternaEdicaoTipo',
                        rotaEdicaoVisualizacao: 'VisualizarMovimentacaoInternaNovoTipo',
                        desabilitado: false,
                        numero: 1,
                    },
                    {
                        titulo: 'Dados Gerais',
                        rotaEdicao: 'MovimentacaoInternaEdicaoDadosGerais',
                        rotaEdicaoVisualizacao: 'VisualizarMovimentacaoInternaEdicaoDadosGerais',
                        desabilitado: true,
                        numero: 2,
                    },
                    {
                        titulo: 'Patrimônios',
                        rotaEdicao: 'MovimentacaoInternaEdicaoItens',
                        rotaEdicaoVisualizacao: 'VisualizarMovimentacaoInternaEdicaoItens',
                        desabilitado: true,
                        numero: 3,
                    },
                    {
                        titulo: 'Documentos',
                        rotaEdicao: 'MovimentacaoInternaEdicaoDocumentos',
                        rotaEdicaoVisualizacao: 'VisualizarMovimentacaoInternaEdicaoDocumentos',
                        desabilitado: true,
                        numero: 4,
                    }
                ],
                passoAtual: {numero:3},
                movimentacaoInternaId: null,
                tipoAtual:''
            }
        },
        computed: {
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Mobiliario.Normal'])
            },
            habilitarEdicaoMovimentacao() {
                const situacaoMovimentacao = this.$store.state.movimentacaointerna.situacaoMovimentacaoInterna
                const inserindo = situacaoMovimentacao === null && !this.movimentacaoInternaId
                return situacaoMovimentacao === 'EM_ELABORACAO' || situacaoMovimentacao === 'ERRO_PROCESSAMENTO' || inserindo
            }
        },
        watch: {
            $route() {
                this.setMovimentacaoInternaId()
                this.setPassoAtual(this.$route)
            },
            async movimentacaoInternaId(){
                if(this.movimentacaoInternaId){
                    this.tipoAtual = await this.buscarTipoMovimentacaoInterna(this.movimentacaoInternaId)
                    const situacaoMovimentacao = await this.buscarSituacaoMovimentacao()
                    this.redirecionarSeMovimentacaoInternaNaoPodeSerEditada(situacaoMovimentacao)
                }
            }
        },
        async created() {
            this.retrairMenu()
            this.setMovimentacaoInternaId()
            this.setPassoAtual(this.$route)
        },
        async destroyed() {
            this.expandirMenu()
            this.setSituacaoMovimentacaoInterna(null)
            await this.removerMovimentacaoVazia()
        },
        methods: {
            ...mapActions([
                actionTypes.MOVIMENTACAO_INTERNA.REMOVER_MOVIMENTACAO_INTERNA_COMPLETA,
                actionTypes.MOVIMENTACAO_INTERNA.REMOVER_MOVIMENTACAO_INTERNA_VAZIA,
                actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA,
                actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA,
            ]),
            ...mapMutations([
                mutationTypes.MOVIMENTACAO_INTERNA.SET_SITUACAO_MOVIMENTACAO_INTERNA
            ]),
            async buscarSituacaoMovimentacao(){
                if(this.movimentacaoInternaId){
                    const situacaoMovimentacao = await this.buscarSituacaoMovimentacaoInterna(this.movimentacaoInternaId)
                    this.setSituacaoMovimentacaoInterna(situacaoMovimentacao)
                    return situacaoMovimentacao
                }
                return null
            },
            redirecionarSeMovimentacaoInternaNaoPodeSerEditada(situacaoMovimentacao){
                if(!['EM_ELABORACAO', 'ERRO_PROCESSAMENTO', null].includes(situacaoMovimentacao)){
                    this.mostrarNotificacaoErro('Não é possível editar esta movimentação')
                    this.$router.replace({name: 'MovimentacaoInternaListagem'})
                }
            },
            habilitaPasso2(movimentacaoInternaCadastradaId) {
                this.passos[1].desabilitado = false
                if(movimentacaoInternaCadastradaId){
                    this.movimentacaoInternaId = movimentacaoInternaCadastradaId
                }
            },
            desabilitaPasso2() {
                this.passos[1].desabilitado = true
            },
            habilitaPasso3() {
                this.passos[2].desabilitado = false
            },
            desabilitaPasso3() {
                this.passos[2].desabilitado = true
            },
            habilitaPasso4() {
                this.passos[3].desabilitado = false
            },
            desabilitaPasso4() {
                this.passos[3].desabilitado = true
            },
            expandirMenu() {
                this.$store.commit(mutationTypes.COMUM.SET_EXPANDIR_MENU)
            },
            retrairMenu() {
                this.$store.commit(mutationTypes.COMUM.SET_RETRAIR_MENU)
            },
            setMovimentacaoInternaId() {
                if(this.$route.params){
                    this.movimentacaoInternaId = this.$route.params.movimentacaoInternaId
                }
            },
            setPassoAtual(route) {
                const encontrado = this.passos.filter(
                    passo =>
                        passo.rotaNovo === route.name ||
                        passo.rotaEdicao === route.name ||
                        passo.rotaEdicaoVisualizacao === route.name
                )
                if(encontrado[0]){
                    this.passoAtual = encontrado[0]
                }
                this.contralaAcessoAosPassosEmCadaRota()
            },
            contralaAcessoAosPassosEmCadaRota() {
                if (this.$route.name === 'MovimentacaoInternaNovoTipo' || this.$route.name === 'VisualizarMovimentacaoInternaNovoTipo' ||
                    this.$route.name === 'MovimentacaoInternaEdicaoTipo' || this.$route.name === 'VisualizarMovimentacaoInternaEdicaoTipo') {
                    this.passos[1].desabilitado = true
                }
                if (this.$route.name === 'MovimentacaoInternaEdicaoDadosGerais' || this.$route.name === 'VisualizarMovimentacaoInternaEdicaoDadosGerais') {
                    this.passos[1].desabilitado = false
                    this.passos[2].desabilitado = true
                    this.passos[3].desabilitado = true
                }
                if (this.$route.name === 'MovimentacaoInternaEdicaoItens' || this.$route.name === 'VisualizarMovimentacaoInternaEdicaoItens') {
                    this.passos[1].desabilitado = false
                    this.passos[2].desabilitado = false
                    this.passos[3].desabilitado = true
                }
                if (this.$route.name === 'MovimentacaoInternaEdicaoDocumentos' || this.$route.name === 'VisualizarMovimentacaoInternaEdicaoDocumentos') {
                    this.passos[0].desabilitado = false
                    this.passos[1].desabilitado = false
                    this.passos[2].desabilitado = false
                    this.passos[3].desabilitado = false
                }
            },
            async tratarEventoDeletarMovimentacaoInterna() {
                await this.removerMovimentacaoInternaCompleta(this.movimentacaoInternaId)
                this.mostrarNotificacaoSucessoDefault()
                await this.$router.push({name: this.rotaOrigem})
            },
            tratarEventoClick(item) {
                if (!item.desabilitado) {
                    if (!this.verificaPermissaoEdicao) {
                        this.$router.replace({name: item.rotaEdicaoVisualizacao, params: {movimentacaoInternaId: this.movimentacaoInternaId}})
                    } else {
                        this.$router.replace({name: item.rotaEdicao, params: {movimentacaoInternaId: this.movimentacaoInternaId}})
                    }
                }
            },
            async removerMovimentacaoVazia() {
                const movimentacao = this.$store.state.movimentacaointerna.movimentacaoInterna
                const movimentacaoExiste = movimentacao && movimentacao.id
                const movimentacaoEmElaboracao = movimentacao.situacao === 'EM_ELABORACAO'
                const movimentacaoComAlgumCampoPreenchido = movimentacaoExiste && this.verificarMovimentacaoComAlgumCampoPreenchido(movimentacao)
                if (movimentacaoExiste && movimentacaoEmElaboracao && !movimentacaoComAlgumCampoPreenchido) {
                    await this.removerMovimentacaoInternaVazia(movimentacao.id)
                }
            },
            verificarMovimentacaoComAlgumCampoPreenchido(movimentacao) {
                const keys = Object.keys(movimentacao)
                const camposPreenchidosAutomaticamente = ['id', 'situacao', 'codigo', 'usuarioCriacao']
                let possuiCampoPreenchido = false
                for (let key of keys) {
                    if (camposPreenchidosAutomaticamente.find(campo => campo === key)) {
                        continue
                    }
                    if (movimentacao[key]) {
                        possuiCampoPreenchido = true
                        break
                    }
                }
                return possuiCampoPreenchido
            }
        }
    }
</script>

<style scoped lang="stylus">
>>>
    .movimentacao-interna-container
        .v-stepper
            box-shadow none

            &__content
                padding 0 15% 0 60%

            &__step__step .v-icon.v-icon
                font-size .9rem

        .container
            background-color #fff

        .az-actions-form
            border-top solid thin rgba(0, 0, 0, .12)
            padding-top 10px
            margin-top 0

        &-conteudo
            min-height 350px

</style>
