<template>
  <v-form class="az-form-content pb-0" ref="form">
    <v-container class="white" fluid>
      <v-card flat height="180">

        <v-row>
          <v-col cols="12" md="4" sm="6" xs="12" class="colunaInput">
            <v-autocomplete
                v-model="reserva.orgaoId"
                :items="orgaos"
                item-text="descricao"
                item-value="id"
                no-data-text="Não há órgãos com este nome"
                name="orgao"
                ref="autoCompleteOrgao"
                placeholder="Selecione"
                :filter="filtroComboAutoComplete"
                v-validate="'required'"
                :error-messages="!reserva.orgaoId ? errors.collect('orgao') : ''"
                @change="tratarEdicao">
              <template v-slot:label>
                Órgão
                <span class="ml-1 red--text">*</span>
              </template>
              <template v-slot:item="data">
                <label
                    :style="obterLarguraAutoComplete('autoCompleteOrgao')"
                    class="uo__autocomplete cursor__pointer text-truncate">
                  {{ data.item.descricao }}
                </label>
              </template>
            </v-autocomplete>
          </v-col>

          <v-col cols="12" md="4" sm="6" xs="12" class="colunaInput">
            <v-select
                v-model="reserva.preenchimento"
                :disabled="!reserva.orgaoId"
                :items="tipoReserva"
                flat
                name="preenchimento"
                item-text="text"
                item-value="value"
                v-validate="'required'"
                :error-messages="errors.collect('preenchimento')"
                @change="tratarEdicao">
              <template v-slot:label>
                Preenchimento
                <span class="ml-1 red--text">*</span>
              </template>
            </v-select>
          </v-col>

          <v-col cols="12" md="4" sm="6" xs="12" v-if="preenchimentoAutomatico">
            <v-text-field
                v-model="reserva.quantidadeReservada"
                :disabled="!reserva.orgaoId"
                name="Quantidade"
                type="number"
                placeholder="Informe o número"
                v-validate="preenchimentoAutomatico ? `required|integer|min_value:1|max:${quantidadeDigitosNumeroPatrimonio}` : ''"
                :error-messages="errors.collect('Quantidade')"
                @blur="tratarEdicao">
              <template v-slot:label>
                Quantidade
                <span class="ml-1 red--text">*</span>
              </template>
            </v-text-field>
          </v-col>

          <v-col cols="12" md="4" sm="6" xs="12" v-if="preenchimentoAutomatico" class="textoInformativo">
            <span class="label-personalizado ">Intervalo</span>
            <p class="intervalo mt-1">{{ reserva.numeroInicio }} - {{ reserva.numeroFim }}</p>
          </v-col>

          <v-col cols="12" md="4" sm="6" xs="12" v-if="!preenchimentoAutomatico" class="colunaInput">
            <v-row>
              <v-col md="6">
                <v-text-field
                    v-model="reserva.numeroInicio"
                    name="Número inicial"
                    type="number"
                    placeholder="00"
                    class="pt-0"
                    persistent-hint
                    :hint="'Próxima Numeração: ' + proximoNumeroDisponivel"
                    v-validate="!preenchimentoAutomatico ? `required|integer|max:${quantidadeDigitosNumeroPatrimonio}` : ''"
                    :error-messages="errors.collect('Número inicial')"
                    @blur="calcularQuantidadeIntervaloManual">
                  <template v-slot:label>
                    Intervalo
                    <span class="ml-1 red--text">*</span>
                  </template>
                </v-text-field>
              </v-col>
              <span class="intervalo">a</span>
              <v-col md="5">
                <v-text-field
                    v-model="reserva.numeroFim"
                    name="Número final"
                    type="number"
                    placeholder="00"
                    class="pt-0"
                    v-validate="!preenchimentoAutomatico ? `required|integer|max:${quantidadeDigitosNumeroPatrimonio}|min_value:${reserva.numeroInicio}` : ''"
                    :error-messages="errors.collect('Número final')"
                    @blur="calcularQuantidadeIntervaloManual"/>
              </v-col>
            </v-row>
          </v-col>

          <v-col cols="12" md="4" sm="6" xs="12" v-if="!preenchimentoAutomatico" class="textoInformativo">
            <span class="label-personalizado mt-0 pt-0">Quantidade</span>
            <p class="intervalo mt-1">{{ reserva.quantidadeReservada ? reserva.quantidadeReservada : ' - ' }}</p>
          </v-col>

        </v-row>
      </v-card>
    </v-container>
  </v-form>
</template>

<script>
    import { mapState } from 'vuex'

    export default {
        name: 'ModalAdicionarReservaPorOrgaoForm',
        props: {
            reserva: {
                type: Object,
                required: true,
            },
            orgaos: {
                type: Array,
                required: true,
            },
            proximoNumeroDisponivel: {
                default: '',
            },
        },
        computed: {
            ...mapState(
                { quantidadeDigitosNumeroPatrimonio: state => state.parametros.parametros.quantidadeDigitosNumeroPatrimonio }),
            preenchimentoAutomatico () {
                return this.reserva.preenchimento === 'AUTOMATICO'
            },
        },
        methods: {
            async verificarSeTodosCamposPreenchidos () {
                if (await this.camposPreenchidos()) {
                    this.$emit('liberarBotaoFinalizar')
                } else {
                    this.$emit('bloquearBotaoFinalizar')
                }
            },
            async camposPreenchidos () {
                return await this.$validator.validateAll()
            },
            async calcularQuantidadeIntervaloManual () {
                if (!this.preenchimentoAutomatico && await this.camposPreenchidos()) {
                    const numeroInicio = this.reserva.numeroInicio
                    const numeroFim = this.reserva.numeroFim
                    if (numeroFim >= numeroInicio) {
                        this.reserva.quantidadeReservada = parseInt(`${(numeroFim - numeroInicio) + 1}`)
                    }
                }
                if (!await this.camposPreenchidos()) {
                    this.resetarQuantidade()
                }
                await this.verificarSeTodosCamposPreenchidos()
            },
            resetarQuantidade () {
                this.reserva.quantidadeReservada = null
            },
            async tratarEdicao () {
                if (!this.preenchimentoAutomatico) {
                    this.tratarBuscarProximoNumero()
                }else {
                    await this.tratarBuscarIntervalo()
                }
                await this.verificarSeTodosCamposPreenchidos()
            },
            tratarBuscarProximoNumero () {
                this.resetarReserva()
                if (this.reserva.orgaoId) {
                    this.buscarProximoNumero()
                }
            },
            async tratarBuscarIntervalo () {
                if (await this.camposPreenchidos()) {
                    this.buscarIntervalo()
                }else {
                    this.resetarReserva()
                }
            },
            buscarProximoNumero () {
                this.$emit('buscarProximoNumeroDisponivel', this.reserva.orgaoId)
            },
            buscarIntervalo () {
                const buscarIntervaloEntidade = this.ciarEntidadeParaBuscarIntervalo()
                this.$emit('buscarIntervalo', buscarIntervaloEntidade)
            },
            resetarReserva () {
                this.$emit('resetarReserva')
            },
            ciarEntidadeParaBuscarIntervalo () {
                return {
                    orgaoId: this.reserva.orgaoId,
                    quantidadeReservada: this.reserva.quantidadeReservada,
                }
            },
            finalizarReserva () {
                this.$emit('finalizar')
            },
            filtroComboAutoComplete (item, queryText) {
                const text = item.nome.toLowerCase()
                const searchText = queryText.toLowerCase()
                return text.indexOf(searchText) > -1
            },
            obterLarguraAutoComplete (ref) {
                if (this.$refs[ref] && this.$refs[ref].$el) {
                    return {
                        width: (this.$refs[ref].$el.clientWidth - 30) + 'px',
                    }
                }
                return {}
            }
        }
    }
</script>

<style lang="stylus">

.textoInformativo
  padding-top 0 !important

.colunaInput
  padding-bottom 0 !important

.uo__autocomplete
  font-size 14px

.label-personalizado
  color: #777
  font-weight: bold;
  font-size 15px
  top -5px

.intervalo
  color: #777
  font-size 15px

</style>