<template>
    <v-dialog persistent v-model="dialog" width="800">
        <v-card>
            <v-card-title class="body-2 primary white--text" primary-title>
                <span class="font-weight-bold mr-1">Estornar Patrimônios 3/3</span>- Confirmação Final
                <v-spacer/>
                <v-btn min-width="10px" class="pa-1 white--text" text @click="fecharModal">
                    <v-icon>fas fa-times</v-icon>
                </v-btn>
            </v-card-title>

            <v-responsive class="overflow-y-auto" max-height="70vh">
            <div class="white">
                <v-row class="mx-1">
                    <v-col md="12">
                        <p class="body-1 text-center grey--text text--darken-1 mt-4 mb-1">
                            Tem certeza que deseja
                            <span class="red--text" v-if="!estorno.todosSelecionados">estornar {{ estorno.patrimoniosId.length }}
                                 patrimônio<span v-if="estorno.patrimoniosId.length >1">s</span>
                            </span>
                            <span class="red--text" v-else>estornar {{ quantidadeTotalPatrimonios }}
                                 patrimônio<span v-if="estorno.patrimoniosId.length >1">s</span>
                            </span>
                                do registro de incorporação com os seguintes dados?</p>
                        </v-col>
                    </v-row>

                    <v-row class="row wrap box-informacoes mx-6">
                        <v-col md="6" sm="6" xs="12" class="pl-md-4">
                            <p class="body-1 mb-0 pb-0 font-weight-bold opacidade-texto">Usuário</p>
                            <p class="body-1 mb-0 pb-0 black--text opacidade-texto"> {{ this.estorno.usuario }} </p>
                        </v-col>
                        <v-col md="6" sm="6" xs="12" class="pl-md-4">
                            <p class="body-1 mb-0 pb-0 font-weight-bold opacidade-texto">
                                {{ getNomeRotulosPersonalizados('dataHora', telaNome) }}
                            </p>
                            <p class="body-1 mb-0 pb-0 black--text opacidade-texto"> {{ dataAtual }} </p>
                        </v-col>
                        <v-col md="12" sm="12" xs="12" class="pl-md-4">
                            <p class="body-1 mb-0 pb-0 font-weight-bold opacidade-texto">
                                {{ getNomeRotulosPersonalizados('motivo', telaNome) }}
                            </p>
                            <p class="body-1 black--text opacidade-texto"> {{ estorno.motivo }} </p>
                        </v-col>
                    </v-row>

                    <v-col md="12" sm="12" xs="12" class="ml-4 mb-3">
                        <span class="body-2 mb-0 pb-0 font-weight-bold opacidade-texto">Obs: </span>
                        <span class="body-2 black--text opacidade-texto">
                            O valor do registro ficará divergente do valor da nota fiscal.
                        </span>
                    </v-col>
                </div>
                <v-divider></v-divider>
                <v-card-actions>
                    <v-row cols="12" md="12" class="mx-1">
                        <v-col cols="6" class="d-flex justify-start">
                            <botao-voltar @voltar="voltar"/>
                        </v-col>
                        <v-col cols="6" class="d-flex justify-end">
                            <botao-finalizar-estornar @finalizar="finalizar"/>
                        </v-col>
                    </v-row>
                </v-card-actions>
            </v-responsive>
        </v-card>
    </v-dialog>
</template>

<script>
    import {createNamespacedHelpers, mapActions, mapMutations} from 'vuex'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import BotaoVoltar from '@/views/components/botoes/BotaoVoltar'
    import BotaoFinalizarEstornar from '@/views/components/botoes/BotaoFinalizarEstornar'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')

    export default {
        name: 'ModalEstornarPatrimoniosFinalizar',
        components: {BotaoVoltar, BotaoFinalizarEstornar},
        props: {
            estorno: {
                type: Object,
            }
        },
        data() {
            return {
                dialog: true,
                telaNome: 'INCORPORACAO_ESTORNO'
            }
        },
        mounted() {
            this.setaIncorporacaoId()
        },
        computed: {
            dataAtual() {
                return this.moment(this.estorno.data).tz(this.$store.state.loki.timezone).format('DD/MM/YYYY - HH\\h\\\\mm')
            },
            quantidadePatrimonios() {
                return this.estorno.patrimoniosId ? this.estorno.patrimoniosId.length : 0
            },
            quantidadeTotalPatrimonios() {
                if (this.estorno.patrimoniosExcecao) {
                    return this.estorno.quantidadeTotalItens - this.estorno.patrimoniosExcecao.length
                }
                return this.estorno.quantidadeTotalItens
            },
            ...mapGetters(['getNomeRotulosPersonalizados'])
        },
        methods: {
            ...mapActions([actionTypes.PATRIMONIO.ESTORNAR_PATRIMONIOS]),
            ...mapMutations([
                mutationTypes.PATRIMONIO.SET_ESTORNO,
                mutationTypes.PATRIMONIO.INCORPORACAO.BUSCAR_REGISTRO_INCORPORACAO
            ]),
            setaIncorporacaoId() {
                this.estorno.incorporacaoId = this.$route.params.incorporacaoId
            },
            fecharModal() {
                this.setEstorno({motivo: null, patrimoniosId: []})
                this.$router.replace({
                    name: 'VisualizarRegistroIncorporacao',
                    params: {incorporacaoId: this.$route.params.incorporacaoId}
                })
            },
            async finalizar() {
                this.filtraIdPatrimoniosSelecionados()
                this.estorno.data = this.retornaDataHoraComTimezoneDoSistema(this.estorno.data)

                if(this.estorno.patrimoniosExcecao <= 0) {
                    this.estorno.patrimoniosExcecao = [0]
                }

                await this.estornarPatrimonios(this.estorno)
                this.setEstorno({motivo: null, patrimoniosId: []})
                this.mostrarNotificacaoSucessoDefault()
                this.buscarRegistroIncorporacao()
                await this.$router.replace({
                    name: 'VisualizarRegistroIncorporacao',
                    params: {incorporacaoId: this.$route.params.incorporacaoId}
                })
            },
            voltar() {
                this.$router.replace({name: 'ModalEstornarPatrimoniosListagem'})
            },
            filtraIdPatrimoniosSelecionados() {
                let patrimonios = []
                this.estorno.patrimoniosId.forEach(item => {
                    patrimonios.push(item.id)
                })
                this.estorno.patrimoniosId = patrimonios
            },
            retornaDataHoraComTimezoneDoSistema(dateTime) {
                const offset = this.retornaOffset(dateTime)
                return this.moment(dateTime).utcOffset(offset).format('YYYY-MM-DDTHH:mm:ss.SSSZZ')
            },
            retornaOffset(dateTime) {
                return this.moment(dateTime).tz(this.$store.state.loki.timezone).format('Z')
            }
        }
    }
</script>

<style scoped lang="stylus">

.opacidade-texto
    opacity 0.7

.box-informacoes
    border-width 1px
    border-color lightgrey
    border-style solid

</style>
