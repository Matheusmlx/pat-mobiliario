<template>
    <div>
        <az-back-button :route="{ name: rotaOrigem }" text="Voltar para listagem"/>

        <az-container class="incorporacao-container">
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

                    <v-spacer/>

                    <div>
                        <botao-remover @remover="tratarEventoDeletarIncorporacao"
                                       v-if="incorporacaoId && verificaPermissaoEdicao"/>
                    </div>
                </v-stepper-header>

                <v-divider/>

                <v-stepper-items>
                    <v-stepper-content
                        :key="`${passo.numero}-content`"
                        :step="passo.numero"
                        v-for="passo in passos">
                    </v-stepper-content>
                    <router-view @desabilitaPasso2="desabilitaPasso2"
                                 @habilitaPasso2="habilitaPasso2"
                                 @desabilitaPasso3="desabilitaPasso3"
                                 @habilitaPasso3="habilitaPasso3"
                    />
                </v-stepper-items>
            </v-stepper>
        </az-container>
    </div>
</template>

<script>
    import {actionTypes, mutationTypes} from '@/core/constants'
    import {mapActions, mapMutations} from 'vuex'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import BotaoRemover from '@/views/components/botoes/BotaoRemover'

    export default {
        name: 'IncorporacaoEdicao',
        components: {BotaoRemover},
        data() {
            return {
                passos: [
                    {
                        titulo: 'Dados Gerais',
                        rotaPadrao: 'EditarIncorporacao',
                        rotaVisualizacao: 'VisualizarIncorporacao',
                        rotaEdicao: 'EditarIncorporacao',
                        desabilitado: false,
                        numero: 1,
                    },
                    {
                        titulo: 'Itens',
                        rotaPadrao: 'ItensIncorporacaoListagem',
                        rotaVisualizacao: 'VisualizarItensIncorporacao',
                        rotaEdicao: 'ItensIncorporacaoListagem',
                        desabilitado: true,
                        numero: 2,
                    },
                    {
                        titulo: 'Documentos',
                        rotaPadrao: 'IncorporacaoDocumentosEdicao',
                        rotaVisualizacao: 'VisualizarIncorporacaoDocumentos',
                        rotaEdicao: 'IncorporacaoDocumentosEdicao',
                        desabilitado: true,
                        numero: 3,
                    }
                ],
                passoAtual: {},
                incorporacaoId: null,
                rotaOrigem: this.$store.state.incorporacao.rota.origem,
                valueToolTip: false,
                situacaoIncorporacao:''
            }
        },
        computed: {
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Mobiliario.Normal'])
            }
        },
        watch: {
            $route() {
                this.setIncorporacaoId()
                this.setPassoAtual(this.$route)
            },
        },
        async created() {
            await this.buscarSituacaoDaIncorporacao()
            await this.validarIncorporacaoEmModoElaboracao(this.situacaoIncorporacao)
        },
        async mounted() {
            this.retrairMenu()
            this.setIncorporacaoId()
            this.setPassoAtual(this.$route)
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.INCORPORACAO.DELETAR_INCORPORACAO,
                actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO
            ]),
            ...mapMutations([
                mutationTypes.PATRIMONIO.INCORPORACAO.SET_SITUACAO_INCORPORACAO
            ]),
            contralaAcessoAosPassosEmCadaRota() {
                if (this.$route.name === 'ItensIncorporacaoListagem' || this.$route.name === 'VisualizarItensIncorporacao') {
                    this.passos[1].desabilitado = false
                    this.passos[2].desabilitado = true
                }
                if (this.$route.name === 'EditarIncorporacao' || this.$route.name === 'VisualizarIncorporacao') {
                    this.passos[1].desabilitado = true
                }
                if (this.$route.name === 'IncorporacaoDocumentosEdicao' || this.$route.name === 'VisualizarIncorporacaoDocumentos') {
                    this.passos[0].desabilitado = false
                    this.passos[1].desabilitado = false
                    this.passos[2].desabilitado = false
                }
            },
            async buscarSituacaoDaIncorporacao() {
                this.situacaoIncorporacao = await this.buscarSituacaoIncorporacao(this.$route.params.incorporacaoId)
                this.setSituacaoIncorporacao(this.situacaoIncorporacao)
            },
            async validarIncorporacaoEmModoElaboracao(situacaoIncorporacao) {
                if (situacaoIncorporacao !== 'EM_ELABORACAO' && situacaoIncorporacao !== 'ERRO_PROCESSAMENTO') {
                    this.mostrarNotificacaoErro('Não é possivel editar esta incorporação!')
                    await this.$router.replace({name: 'IncorporacaoListagem'})
                }
            },

            desabilitaPasso3() {
                this.passos[2].desabilitado = true
            },
            expandirMenu() {
                this.$store.commit(mutationTypes.COMUM.SET_EXPANDIR_MENU)
            },
            habilitaPasso3() {
                this.passos[2].desabilitado = false
            },
            desabilitaPasso2() {
                this.passos[1].desabilitado = true
            },
            habilitaPasso2() {
                this.passos[1].desabilitado = false
            },
            retrairMenu() {
                this.$store.commit(mutationTypes.COMUM.SET_RETRAIR_MENU)
            },
            setIncorporacaoId() {
                this.incorporacaoId = this.$route.params.incorporacaoId
            },
            setPassoAtual(route) {
                const encontrado = this.passos.filter(
                    passo =>
                        passo.rotaPadrao === route.name ||
                        passo.rotaEdicao === route.name ||
                        passo.rotaVisualizacao === route.name
                )
                this.passoAtual = encontrado[0]
                this.contralaAcessoAosPassosEmCadaRota()
            },
            async tratarEventoDeletarIncorporacao() {
                await this.deletarIncorporacao(this.incorporacaoId)
                this.mostrarNotificacaoSucessoDefault()
                await this.$router.replace({name: this.rotaOrigem})
            },
            tratarEventoClick(item) {
                if (item.desabilitado === false) {
                    if (this.incorporacaoId) {
                        if (!this.verificaPermissaoEdicao) {
                            this.$router.replace({name: item.rotaVisualizacao})
                        } else {
                            this.$router.replace({
                                name: item.rotaEdicao,
                            })
                        }
                    } else {
                        this.$router.replace({
                            name: this.rotaOrigem,
                        })
                    }
                }
            }
        },
        destroyed() {
            this.expandirMenu()
        }
    }
</script>

<style lang="stylus">

.incorporacao-container
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

.barra-passo
    display flex
    align-items center
    margin-right 1%

.passo-ativo
    .v-stepper__step__step
        background-color #487b9c !important
        border-color #487b9c !important

</style>
