<template>
    <div>
        <v-container fluid white>
            <v-row class="ma-0 ml-2">
                <v-col>
                    <v-card
                        flat tile
                        class="pa-2 text--darken-1 grey--text text-body-2 font-weight-bold">
                        {{ 'Reserva: ' + reservaEdicao.codigo }}
                    </v-card>
                </v-col>
                <v-col md="3" sm="2" class="d-flex justify-end   pr-6">
                    <botao-remover @remover="removerReserva" v-if="!possuiNumerosUtilizados"/>
                </v-col>
            </v-row>
            <v-divider/>
            <v-row>
                <v-col :cols="ehTipoManual ? 10 : 7" class="">
                    <v-form ref="form" class="az-form-content nova-reserva ml-2 d-flex">
                        <v-row>
                            <v-col cols="12" :md="ehTipoManual ? 3 : 4">
                                <v-select
                                    flat
                                    name="preenchimento"
                                    v-validate="'required'"
                                    :error-messages="errors.collect('preenchimento')"
                                    v-model="reservaEdicao.preenchimento"
                                    item-text="text"
                                    item-value="value"
                                    :items="tipoReserva"
                                    @change="resetarReserva">
                                    <template v-slot:label>
                                        Preenchimento
                                        <span class="ml-1 red--text">*</span>
                                    </template>
                                </v-select>
                            </v-col>
                            <v-col cols="12" md="4" v-if="!ehTipoManual">
                                <v-text-field
                                    v-model="reservaEdicao.quantidadeReservada"
                                    name="quantidade"
                                    key="quantidade"
                                    v-validate="`required|integer|max:${quantidadeDigitosNumeroPatrimonio}`"
                                    type="number"
                                    :error-messages="errors.collect('quantidade')"
                                    placeholder="Informe a quantidade">
                                    <template v-slot:label>
                                        Quantidade
                                        <span class="ml-1 red--text">*</span>
                                    </template>
                                </v-text-field>
                            </v-col>
                            <v-row v-else class="ma-0" style="display: contents">
                                <v-col cols="12" md="2">
                                    <v-text-field
                                        v-model="reservaEdicao.numeroInicio"
                                        name="número inicial"
                                        key="numero-inicial"
                                        v-validate="`required|integer|max:${quantidadeDigitosNumeroPatrimonio}`"
                                        type="number"
                                        :error-messages="errors.collect('número inicial')"
                                        placeholder="00"
                                        :hint="'Próxima Numeração: ' + proximoNumeroDisponivel"
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
                                        v-model="reservaEdicao.numeroFim"
                                        name="número final"
                                        key="numero-final"
                                        type="number"
                                        v-validate="`required|integer|max:${quantidadeDigitosNumeroPatrimonio}`"
                                        :error-messages="errors.collect('número final')"
                                        placeholder="00"
                                        @input="calcularQuantidadeIntervaloManual"/>
                                </v-col>
                                <v-col cols="12" md="2">
                                    <v-text-field
                                        label="Quantidade"
                                        placeholder=" "
                                        key="quantidade-label"
                                        :value="reservaEdicao.quantidadeReservada"
                                        disabled
                                    ></v-text-field>
                                </v-col>
                                <v-col md="1" cols="12" class="d-flex align-center mb-2">
                                    <v-btn
                                        outlined
                                        @click="editar"
                                        elevation="0"
                                        color="primary"
                                        min-width="50">
                                        <v-icon>check</v-icon>
                                    </v-btn>
                                </v-col>
                            </v-row>
                            <v-col v-if="!ehTipoManual" md="2" cols="12" class="d-flex align-center mb-2">
                                <v-btn
                                    outlined
                                    @click="editar"
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
            <reserva-intervalo-dados-gerais-novo :reserva-id="reservaId"/>
        </v-container>
    </div>
</template>

<script>
    import {actionTypes} from '@/core/constants'
    import BotaoRemover from '@/views/components/botoes/BotaoRemover'
    import {mapActions} from 'vuex'
    import ReservaIntervaloDadosGeraisNovo
        from '@/views/pages/configuracao/reserva/global/edicao/intervalo/novo/ReservaIntervaloDadosGeraisNovo'

    export default {
        name: 'ReservaDadosGeraisEdicao',
        components: {ReservaIntervaloDadosGeraisNovo,BotaoRemover},
        props: {
            reservaId: {
                type: String,
                required: true
            },
            reserva: {
                type: Object,
                default: () => {
                    return {
                        quantidadeReservada: '',
                        preenchimento: 'AUTOMATICO',
                        possuiNumerosUtilizados:false
                    }
                }
            }
        },
        watch: {
            reserva(reserva) {
                this.reservaEdicao = {...reserva}
            }
        },
        data() {
            return {
                proximoNumeroDisponivel: '',
                reservaEdicao: {},
                possuiNumerosUtilizados:false
            }
        },
        updated(){
            this.verificarNumeroReservaUtilizando()
        },
        computed: {
            ehTipoManual() {
                return this.reservaEdicao.preenchimento === 'MANUAL'
            },
            quantidadeDigitosNumeroPatrimonio() {
                return this.$store.state.parametros.parametros.quantidadeDigitosNumeroPatrimonio
            }
        },
        async mounted() {
            this.reservaEdicao = {...this.reserva}
            await this.buscarProximoNumeroDisponivelDaReserva()
        },
        methods: {
            ...mapActions([
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.EDITAR_RESERVA,
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_PROXIMO_NUMERO_DISPONIVEL,
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.VERIFICAR_RESERVA_POSSUI_NUMERO_UTILIZADO,
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.DELETAR_RESERVA
            ]),
            resetarReserva() {
                this.reservaEdicao.quantidadeReservada = this.reserva.quantidadeReservada
                this.reservaEdicao.numeroInicio = this.reserva.numeroInicio
                this.reservaEdicao.numeroFim = this.reserva.numeroFim
            },
            async verificarNumeroReservaUtilizando() {
                let {possuiNumerosUtilizados} = await this.verificarReservaPossuiNumeroUtilizado(this.reservaId)
                this.possuiNumerosUtilizados = possuiNumerosUtilizados
            },
            async buscarProximoNumeroDisponivelDaReserva() {
                const resultado = await this.buscarProximoNumeroDisponivel()
                this.proximoNumeroDisponivel = resultado.proximoNumero
                if (!this.reservaEdicao.numeroInicio) {
                    this.reservaEdicao.numeroInicio = resultado.proximoNumero
                }
            },
            async editar() {
                if (await this.validarDadosFormulario() && this.validarQuantidade()) {
                    const reservaAtualizada = await this.editarReserva(this.reservaEdicao)

                    if (reservaAtualizada) {
                        this.$emit('recarregarDadosReserva')
                        this.mostrarNotificacaoSucessoDefault()
                        await this.buscarProximoNumeroDisponivelDaReserva()
                    }
                }
            },
            validarQuantidade() {
                if(this.reservaEdicao && this.reservaEdicao.quantidadeReservada  > 999999999) {
                    this.mostrarNotificacaoErro('Quantidade de números por intervalo ultrapassada')
                    return false
                }
                return true
            },
            async removerReserva() {
                await this.deletarReserva(this.reserva.id)
                await this.$router.push({name:'ReservaListagem'})
                this.mostrarNotificacaoSucessoDefault()
            },
            calcularQuantidadeIntervaloManual() {
                if (this.ehTipoManual && this.reservaEdicao) {
                    const numeroInicio = this.reservaEdicao.numeroInicio
                    const numeroFim = this.reservaEdicao.numeroFim
                    if (numeroInicio && numeroFim && numeroFim >= numeroInicio) {
                        this.reservaEdicao.quantidadeReservada = (numeroFim - numeroInicio) + 1
                    } else {
                        this.reservaEdicao.quantidadeReservada = ''
                    }
                }
            },
            async validarDadosFormulario() {
                return await this.$validator.validateAll()
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
        display flex
        align-items center

    .intervalo
        margin-top 10px
        display flex
        font-size 13px
        align-items center
        color #777
</style>
