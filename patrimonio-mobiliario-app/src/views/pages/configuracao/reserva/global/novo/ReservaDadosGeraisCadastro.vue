<template>
    <div>
        <v-container fluid white>
            <v-row class="ma-0 ml-2">
                <v-col>
                    <v-card
                        flat
                        tile
                        class="pa-2 text--darken-1 grey--text text-body-2 font-weight-bold">
                        Nova Reserva
                    </v-card>
                </v-col>
            </v-row>
            <v-divider/>
            <v-row>
                <v-col :cols="ehTipoManual ? 10 : 7" class="pb-0">
                    <v-form ref="form" class="az-form-content nova-reserva ml-2 d-flex">
                        <v-row>
                            <v-col cols="12" :md="ehTipoManual ? 3 : 4">
                                <v-select
                                    flat
                                    name="preenchimento"
                                    v-validate="'required'"
                                    :error-messages="errors.collect('preenchimento')"
                                    v-model="reserva.preenchimento"
                                    item-text="text"
                                    item-value="value"
                                    :items="tipoReserva"
                                    @change="resetarQuantidade"
                                >
                                    <template v-slot:label>
                                        Preenchimento
                                        <span class="ml-1 red--text">*</span>
                                    </template>
                                </v-select>
                            </v-col>
                            <v-col cols="12" md="4" v-if="!ehTipoManual">
                                <v-text-field
                                    v-model="reserva.quantidadeReservada"
                                    name="quantidade"
                                    v-validate="`required|integer|max:${quantidadeDigitosNumeroPatrimonio}`"
                                    type="number"
                                    :error-messages="errors.collect('quantidade')"
                                    placeholder="Informe o número">
                                    <template v-slot:label>
                                        Quantidade
                                        <span class="ml-1 red--text">*</span>
                                    </template>
                                </v-text-field>
                            </v-col>
                            <v-row v-else class="ma-0" style="display: contents">
                                <v-col cols="12" md="2">
                                    <v-text-field
                                        v-model="reserva.numeroInicio"
                                        name="número inicial"
                                        v-validate="`required|integer|max:${quantidadeDigitosNumeroPatrimonio}`"
                                        type="number"
                                        :error-messages="errors.collect('número inicial')"
                                        placeholder="00"
                                        :hint="'Próxima Numeração: ' + proximoNumeroDisponivel"
                                        persistent-hint
                                        @input="calcularQuantidadeIntervaloManual">
                                        <template v-slot:label>
                                            Intervalo
                                            <span class="ml-1 red--text">*</span>
                                        </template>
                                    </v-text-field>
                                </v-col>
                                <span class="intervalo">a</span>
                                <v-col cols="12" md="2">
                                    <v-text-field
                                        v-model="reserva.numeroFim"
                                        name="número final"
                                        type="number"
                                        v-validate="`required|integer|max:${quantidadeDigitosNumeroPatrimonio}`"
                                        :error-messages="errors.collect('número final')"
                                        placeholder="00"
                                        @input="calcularQuantidadeIntervaloManual"
                                    />
                                </v-col>
                                <v-col cols="12" md="2">
                                    <v-text-field
                                        placeholder=" "
                                        label="Quantidade"
                                        v-model="reserva.quantidadeReservada"
                                        disabled
                                    ></v-text-field>
                                </v-col>
                                <v-col md="1" cols="12" class="d-flex align-center mb-2">
                                    <v-btn
                                        @click="salvar"
                                        elevation="0"
                                        color="primary"
                                        min-width="50">
                                        <v-icon>check</v-icon>
                                    </v-btn>
                                </v-col>
                            </v-row>
                            <v-col v-if="!ehTipoManual" md="2" cols="12" class="d-flex align-center mb-2">
                                <v-btn
                                    @click="salvar"
                                    elevation="0"
                                    color="primary"
                                    min-width="50">
                                    <v-icon>check</v-icon>
                                </v-btn>
                            </v-col>
                        </v-row>
                    </v-form>
                </v-col>
            </v-row>
            <v-divider/>
            <v-container fluid white class="sem-orgao-adicionado"/>
            <v-divider/>
            <div class="az-actions-form">
                <botao-cancelar @cancelar="cancelar"/>
                <botao-finalizar-desabilitado texto="É necessário realizar o cadastro da reserva e adicionar órgãos para finalizar."/>
            </div>
        </v-container>
    </div>
</template>

<script>
    import BotaoCancelar from '@/views/components/botoes/BotaoCancelar'
    import BotaoFinalizarDesabilitado from '@/views/components/botoes/BotaoFinalizarDesabilitado'
    import {actionTypes} from '@/core/constants'
    import {mapActions} from 'vuex'

    export default {
        name: 'ReservaDadosGeraisCadastro',
        components: {BotaoCancelar, BotaoFinalizarDesabilitado},
        data() {
            return {
                proximoNumeroDisponivel: '',
                reserva: {
                    preenchimento: 'AUTOMATICO',
                    quantidadeReservada: '',
                    numeroInicio: null,
                    numeroFim: null
                }
            }
        },
        computed: {
            ehTipoManual() {
                return this.reserva.preenchimento === 'MANUAL'
            },
            quantidadeDigitosNumeroPatrimonio() {
                return this.$store.state.parametros.parametros.quantidadeDigitosNumeroPatrimonio
            }
        },
        async mounted() {
            await this.buscarProximoNumeroDisponivelDaReserva()
        },
        methods: {
            ...mapActions([
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.SALVAR_RESERVA,
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_PROXIMO_NUMERO_DISPONIVEL]),
            async buscarProximoNumeroDisponivelDaReserva() {
                const resultado = await this.buscarProximoNumeroDisponivel()
                this.proximoNumeroDisponivel = resultado.proximoNumero
                this.reserva.numeroInicio = resultado.proximoNumero
            },
            resetarQuantidade() {
                this.reserva.quantidadeReservada = ''
            },
            validarQuantidade() {
                if(this.reserva && this.reserva.quantidadeReservada  > 999999999) {
                    this.mostrarNotificacaoErro('Quantidade de números por intervalo ultrapassada')
                    return false
                }
                return true
            },
            calcularQuantidadeIntervaloManual() {
                if (this.ehTipoManual && this.reserva) {
                    const numeroInicio = this.reserva.numeroInicio
                    const numeroFim = this.reserva.numeroFim
                    if (numeroInicio && numeroFim && numeroFim >= numeroInicio) {
                        this.reserva.quantidadeReservada = (numeroFim - numeroInicio) + 1
                    } else {
                        this.reserva.quantidadeReservada = ''
                    }
                }
            },
            async validarDadosFormulario() {
                return await this.$validator.validateAll()
            },
            async salvar() {
                if (await this.validarDadosFormulario() && this.validarQuantidade()) {
                    const resultado = await this.salvarReserva(this.reserva)
                    if (resultado.id !== null && typeof resultado.id !== 'undefined') {
                        await this.$router.push({name: 'ReservaEdicao', params: {id: resultado.id}})
                    }
                }
            },
            cancelar() {
                this.$router.push({name: 'ReservaListagem'})
            }
        }
    }
</script>

<style lang="stylus">
    .nova-reserva
        padding 3px 0 3px 20px

        .v-messages__message
            font-size 10px

        .v-select__selection
            font-size 13px
            color #777

    .sem-orgao-adicionado
        min-height 55vh

    .intervalo
        margin-top 10px
        display flex
        font-size 13px
        align-items center
        color #777
</style>
