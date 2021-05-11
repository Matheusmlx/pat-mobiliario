<template>
    <v-dialog persistent v-model="dialog" width="800">
        <v-card>
            <v-card-title class="body-2 primary white--text" primary-title>
                <span class="font-weight-bold mr-1">Visualizar Motivo Estorno</span>
                <v-spacer/>
                <v-btn min-width="10px" class="pa-1 white--text" text @click="fecharModal">
                    <v-icon>fas fa-times</v-icon>
                </v-btn>
            </v-card-title>

            <div class="white">
                <v-row class="row box-informacoes mx-4 my-4">
                    <v-col md="6" xs="12" class="pl-md-4">
                        <p class="body-1 mb-0 pb-0 font-weight-bold opacidade-texto">Usuário</p>
                        <p class="body-1 mb-0 pb-0 black--text opacidade-texto"> {{ this.estorno.usuario }} </p>
                    </v-col>
                    <v-col md="6" xs="12" class="pl-md-4">
                        <p class="body-1 mb-0 pb-0 font-weight-bold opacidade-texto">
                            {{ getNomeRotulosPersonalizados('dataHora', telaNome) }}
                        </p>
                        <p class="body-1 mb-0 pb-0 black--text opacidade-texto"> {{ dataEstorno }} </p>
                    </v-col>
                    <v-col md="12" xs="12" class="pl-md-4">
                        <p class="body-1 mb-0 pb-0 font-weight-bold opacidade-texto">
                            {{ getNomeRotulosPersonalizados('motivo', telaNome) }}
                        </p>
                        <p class="body-1 black--text opacidade-texto"> {{ estorno.motivo }} </p>
                    </v-col>
                </v-row>
            </div>
            <v-divider></v-divider>
        </v-card>
    </v-dialog>
</template>

<script>
    import {createNamespacedHelpers, mapMutations} from 'vuex'
    import {mutationTypes} from '@/core/constants'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')

    export default {
        name: 'ModalEstornarPatrimoniosVisualizarEstorno',
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
        computed: {
            dataEstorno() {
                return this.moment(this.estorno.data).tz(this.$store.state.loki.timezone).format('DD/MM/YYYY - HH\\h\\\\mm')
            },
            ...mapGetters(['getNomeRotulosPersonalizados'])
        },
        methods: {
            ...mapMutations([
                mutationTypes.PATRIMONIO.SET_ESTORNO
            ]),
            fecharModal() {
                this.setEstorno({motivo: null})
                this.$router.replace({
                    name: 'VisualizarRegistroIncorporacao',
                    params: {incorporacaoId: this.$route.params.incorporacaoId}
                })
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
