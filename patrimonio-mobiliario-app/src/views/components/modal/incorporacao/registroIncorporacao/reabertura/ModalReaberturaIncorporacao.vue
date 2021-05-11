<template>
    <v-dialog
        v-model="dialog"
        width="500">
        <template v-slot:activator="{ on }">
            <v-tooltip nudge-top="0" top transition="scale-transition" origin="center center">
                <template v-slot:activator="{ on }">
                    <v-btn @click="dialog = true" dark text v-on="on" class="mr-12">
                        <span class="text-capitalize">Reabrir incorporação</span>
                    </v-btn>
                </template>
                Reabertura permitida dentro do mês corrente.
            </v-tooltip>
        </template>

        <v-card>
            <v-card-title
                class="flex align-content-center body-2 font-weight-bold primary white--text modal__header"
                primary-title>
                Reabrir Incorporação
                <v-spacer/>
                <v-btn min-width="10px" class="pa-1 white--text" text @click="fecharModal">
                    <v-icon>fas fa-times</v-icon>
                </v-btn>
            </v-card-title>

            <v-card-text class="title font-weight-bold d-flex justify-center align-center pa-11 my-2">
                Tem certeza que deseja reabrir?
            </v-card-text>

            <v-divider></v-divider>

            <v-card-actions>
                <v-spacer/>
                <v-col cols="12" md="12" class="row justify-center align-center">
                    <v-btn
                        depressed
                        @click="fecharModal"
                        class="lighten-2"
                        min-width="110px"
                        color="primary">
                        Não
                    </v-btn>
                    <v-col md="1"> <v-spacer/></v-col>
                    <v-btn
                        outlined
                        depressed
                        min-width="110px"
                        @click="tratarEventoReabrirIncorporacao"
                        color="grey">
                        Sim
                    </v-btn>
                </v-col>
                <v-spacer/>
            </v-card-actions>
        </v-card>
    </v-dialog>

</template>

<script>
    export default {
        name: 'ModalReaberturaIncorporacao',
        props: ['value'],
        data: () => ({
            dialog: false
        }),
        methods: {
            fecharModal() {
                this.dialog = false
            },
            async tratarEventoReabrirIncorporacao() {
                const {incorporacaoId} = this.$route.params
                await this.$emit('reabrirIncorporacao', incorporacaoId)
                this.fecharModal()
            }
        }
    }
</script>

<style scoped lang="stylus">
    .modal__header
        height 55px
</style>
