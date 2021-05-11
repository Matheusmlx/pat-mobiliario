<template>
    <tr>
        <td>
            <v-checkbox v-model="reservaIntervalo.selecionado" @change="tratarSelecaoReservaIntervalo"/>
        </td>
        <td class="max-20 pt-3">
            <v-tooltip top nudge-top max-width="600" open-delay="500">
                <template v-slot:activator="{ on }">
                    <div v-on="on">
                        <span class="grey--text text--darken-2 d-inline-block text-truncate max-20">
                            {{ reservaIntervalo.descricao | textoSemValor }}
                        </span>
                    </div>
                </template>
                {{ reservaIntervalo.descricao }}
            </v-tooltip>
        </td>
        <td class="max-5">
            <div class="orgao-reserva az-form-content pa-0">
                <v-select
                    flat
                    :disabled="!reservaIntervalo.selecionado"
                    v-model="reservaIntervalo.preenchimento"
                    item-text="text"
                    item-value="value"
                    :items="tipoReserva"
                    @change="buscarProximoNumeroDisponivelDoIntervalo">
                </v-select>
            </div>
        </td>
        <td class="max-5">
            <div class="orgao-reserva az-form-content pa-0">
                <v-text-field
                    :disabled="!reservaIntervalo.selecionado || preenchimentoManual"
                    v-model="reservaIntervalo.quantidadeReservada"
                    name="Quantidade"
                    type="number"
                    @blur="tratarEdicaoQuantidade"
                    v-validate="reservaIntervalo.selecionado ? `required|integer|min_value:1|max:${quantidadeDigitosNumeroPatrimonio}` : ''"
                    :error-messages="errors.collect('Quantidade')"
                    :placeholder="placeholderQuantidade">
                </v-text-field>
            </div>
        </td>
        <td class="max-20">
            <div class="orgao-reserva az-form-content pa-0" v-if="!preenchimentoManual">
                <v-text-field
                    disabled
                    :placeholder="reservaIntervalo.selecionado ? reservaIntervalo.numeroInicio + ' - ' + reservaIntervalo.numeroFim : ' --- '">
                </v-text-field>
            </div>
            <div class="orgao-reserva az-form-content pa-0" v-else>
                <v-row>
                    <v-col md="6">
                        <v-text-field
                            v-model="reservaIntervalo.numeroInicio"
                            name="Número inicial"
                            v-validate="`required|integer|max:${quantidadeDigitosNumeroPatrimonio}`"
                            type="number"
                            :error-messages="errors.collect('Número inicial')"
                            placeholder="00"
                            :hint="'Próxima Numeração: ' + proximoNumeroDisponivel"
                            persistent-hint
                            class="mt-o pt-0"
                            @blur="calcularQuantidadeIntervaloManual">
                        </v-text-field>
                    </v-col>
                    <span class="intervalo">a</span>
                    <v-col md="5">
                        <v-text-field
                            v-model="reservaIntervalo.numeroFim"
                            name="Número final"
                            type="number"
                            v-validate="`required|integer|max:${quantidadeDigitosNumeroPatrimonio}|min_value:${reservaIntervalo.numeroInicio}`"
                            :error-messages="errors.collect('Número final')"
                            placeholder="00"
                            class="mt-o pt-0"
                            @blur="tratarIntervalo"
                        />
                    </v-col>
                </v-row>

            </div>
        </td>
    </tr>
</template>

<script>
    import {mapActions} from 'vuex'
    import {actionTypes} from '@/core/constants'

    export default {
        name: 'ModalAdicionarIntervalosForm',
        props: ['reservaIntervalo', 'maiorNumeroFimIntervalo'],
        data() {
            return {
                proximoNumeroDisponivel: ''
            }
        },
        computed: {
            preenchimentoManual() {
                return this.reservaIntervalo.preenchimento === 'MANUAL'
            },
            quantidadeDigitosNumeroPatrimonio() {
                return this.$store.state.parametros.parametros.quantidadeDigitosNumeroPatrimonio
            },
            placeholderQuantidade() {
                if (this.preenchimentoManual) {
                    if (this.reservaIntervalo.quantidadeReservada) {
                        return this.reservaIntervalo.quantidadeReservada.toString()
                    }
                    return ' -'
                }
                return 'Ex: 00'
            }
        },
        methods: {
            ...mapActions([
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_PROXIMO_NUMERO,
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.VALIDAR_INTERVALO
            ]),
            tratarSelecaoReservaIntervalo() {
                if (!this.reservaIntervalo.selecionado) {
                    this.atualizarMaiorNumeroFimIntervalo(null)
                    this.deselecionaReservaIntervalo()
                } else {
                    this.selecionaReservaIntervalo()
                }
            },
            async buscarProximoNumeroDisponivelDoIntervalo() {
                await this.$validator.reset()
                const resultado = await this.buscarProximoNumero(
                    {reservaId: this.reservaIntervalo.reservaId, maiorNumeroFimIntervalo: this.maiorNumeroFimIntervalo})
                this.proximoNumeroDisponivel = resultado.proximoNumero
                if (!this.reservaIntervalo.numeroInicio) {
                    this.reservaIntervalo.numeroInicio = resultado.proximoNumero
                }
            },
            async tratarEdicaoQuantidade() {
                if (await this.$validator.validateAll()) {

                    this.selecionaReservaIntervalo()
                    this.buscarProximoIntervalo()
                }
            },
            async buscarProximoIntervalo() {
                this.$emit('buscarProximoIntervalo', this.reservaIntervalo)
            },
            selecionaReservaIntervalo() {
                this.$emit('selecionaReservaIntervalo', this.reservaIntervalo)
            },
            deselecionaReservaIntervalo() {
                this.$emit('deselecionaReservaIntervalo', this.reservaIntervalo)
            },
            async tratarIntervalo() {
                this.atualizarMaiorNumeroFimIntervalo(this.reservaIntervalo.numeroFim)
                this.calcularQuantidadeIntervaloManual()
                await this.validarIntervaloReservaIntervalo()
            },
            atualizarMaiorNumeroFimIntervalo(numeroFim) {
                this.$emit('atualizarMaiorNumeroFimIntervalo', numeroFim)
            },
            async validarIntervaloReservaIntervalo() {
                if (this.reservaIntervalo && this.reservaIntervalo.numeroInicio && this.reservaIntervalo.numeroFim) {
                    const validarIntervaloEntidade = {
                        numeroInicio: this.reservaIntervalo.numeroInicio,
                        numeroFim: this.reservaIntervalo.numeroFim
                    }
                    await this.validarIntervalo({
                        reservaId: this.reservaIntervalo.reservaId,
                        numeroInicio: validarIntervaloEntidade.numeroInicio,
                        numeroFim: validarIntervaloEntidade.numeroFim
                    })
                    this.maiorNumeroFimIntervalo = validarIntervaloEntidade.numeroFim
                }
            },
            calcularQuantidadeIntervaloManual() {
                this.selecionaReservaIntervalo()
                if (this.preenchimentoManual && this.reservaIntervalo && this.reservaIntervalo.numeroInicio && this.reservaIntervalo.numeroFim) {
                    const numeroInicio = this.reservaIntervalo.numeroInicio
                    const numeroFim = this.reservaIntervalo.numeroFim
                    if (numeroFim >= numeroInicio) {
                        this.reservaIntervalo.quantidadeReservada = parseInt(`${(numeroFim - numeroInicio) + 1}`)
                    }
                }
            }
        }
    }
</script>

<style lang="stylus">

</style>
