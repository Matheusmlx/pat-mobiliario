<template>
    <v-dialog persistent v-model="dialog" width="800">
        <v-card>
            <v-card-title
                class="body-2  primary white--text"
                primary-title>
                <span class="font-weight-bold mr-1">Estornar Patrimônios 1/3</span>- Informe o motivo
                <v-spacer/>
                <v-btn min-width="10px" class="pa-1 white--text" text @click="fecharModal">
                    <v-icon>fas fa-times</v-icon>
                </v-btn>
            </v-card-title>

            <div class="white">
                <p class="body-1 grey--text text--darken-1 mt-4 mb-4 ml-5 pl-1">
                    Para continuar, informe o motivo para o estorno dos patrimônios:
                </p>
                <v-divider/>
                <v-row class="row wrap ml-3">
                    <v-col md="4" sm="4" xs="12">
                        <span class="body-2 mt-2 mb-0 pb-0 font-weight-bold opacidade-texto">Usuário:</span>
                        <span class="body-2 black--text opacidade-texto"> {{ this.usuario }} </span>
                    </v-col>
                    <v-col md="4" sm="6" xs="12">
                        <span class="body-2 mt-2 mb-0 pb-0 font-weight-bold opacidade-texto">
                            {{ getNomeRotulosPersonalizados('dataHora', telaNome) }}:
                        </span>
                        <span class="body-2 black--text opacidade-texto"> {{ dataAtual }} </span>
                    </v-col>
                </v-row>
                <v-col md="12" sm="12" xs="11" class="pt-0 ml-3 pr-10">
                    <span class="body-2 mt-2 mb-0 pb-0 font-weight-bold opacidade-texto">
                        {{ getNomeRotulosPersonalizados('motivo', telaNome) }}<span class=" red--text">*</span>
                    </span>
                    <v-textarea
                        v-model="estorno.motivo"
                        :counter="500"
                        :error-messages="errors.collect('motivo')"
                        outlined
                        auto-grow
                        rows="4"
                        maxlength="500"
                        name="motivo"
                        v-validate="'required'"
                        class="mt-3">
                    </v-textarea>
                </v-col>
            </div>
            <v-divider></v-divider>
            <v-card-actions>
                <v-row cols="12" md="12" class="mx-1">
                    <v-col cols="6" class="d-flex justify-start">
                        <botao-cancelar @cancelar="fecharModal"/>
                    </v-col>
                    <v-col cols="6" class="d-flex justify-end">
                        <botao-continuar v-if="estorno.motivo" @continuar="continuar"/>
                        <botao-continuar-desabilitado v-else/>
                    </v-col>
                </v-row>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import {createNamespacedHelpers, mapMutations} from 'vuex'
    import {mutationTypes} from '@/core/constants'
    import BotaoContinuar from '@/views/components/botoes/BotaoContinuar'
    import BotaoCancelar from '@/views/components/botoes/BotaoCancelar'
    import BotaoContinuarDesabilitado from '@/views/components/botoes/BotaoContinuarDesabilitado'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')

    export default {
        name: 'ModalEstornarPatrimoniosMotivo',
        components: {BotaoContinuar, BotaoCancelar, BotaoContinuarDesabilitado},
        props: {
            estorno: {
                type: Object,
            }
        },
        data() {
            return {
                dialog: true,
                data: new Date(),
                usuario: this.$store.state.loki.user.name,
                telaNome: 'INCORPORACAO_ESTORNO'
            }
        },
        computed: {
            dataAtual() {
                return this.moment(this.data).tz(this.$store.state.loki.timezone).format('DD/MM/YYYY - HH\\h\\\\mm')
            },
            ...mapGetters(['getNomeRotulosPersonalizados'])
        },
        methods: {
            ...mapMutations([mutationTypes.PATRIMONIO.SET_ESTORNO]),
            fecharModal() {
                this.setEstorno({motivo: null, patrimoniosId: []})
                this.$router.replace({
                    name: 'VisualizarRegistroIncorporacao',
                    params: {incorporacaoId: this.$route.params.incorporacaoId}
                })
            },
            continuar() {
                this.estorno.data = this.data
                this.estorno.usuario = this.usuario
                this.setEstorno(this.estorno)
                this.$router.replace({name: 'ModalEstornarPatrimoniosListagem'})
            }
        }
    }
</script>

<style scoped lang="stylus">
.opacidade-texto
    opacity 0.7
</style>
