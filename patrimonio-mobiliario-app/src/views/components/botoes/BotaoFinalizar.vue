<template>
    <v-btn
        color="primary"
        depressed
        :loading="loading"
        @click="finalizar">
        {{ textoButton }}
    </v-btn>
</template>

<script>
    export default {
        name: 'BotaoFinalizar',
        props: {
            textoButton: {
                type: String,
                default: 'Finalizar'
            }
        },
        data() {
            return {
                loading: false
            }
        },
        methods: {
            async finalizar() {
                try {
                    this.loading = true
                    await this.$listeners.finalizar()
                    this.loading = false
                } catch (e) {
                    this.loading = false
                }
            }
        }
    }
</script>

<style lang="stylus" scoped>
    .botao-cancelar
        color #999 !important
</style>
