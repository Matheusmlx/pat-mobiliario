<template>
    <v-container fluid white class="sem-orgao-adicionado">
        <v-row>
            <v-col cols="12">
                <div class="text-center mb-12">
                    <div class="pb-3">
                        <v-icon x-large color="rgb(220, 220, 220)">
                            fa-inbox
                        </v-icon>
                    </div>
                    <p class="text--darken-1 grey--text text-body-2">Nenhum <strong>órgão</strong> adicionado,
                        clique para adicionar</p>
                    <az-call-to-action
                        @click="adicionarIntervalos"
                        active
                        slot="actions">
                        <v-icon>add_circle</v-icon>
                        Adicionar órgãos
                    </az-call-to-action>
                </div>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
    export default {
        name: 'ReservaIntervaloDadosGeraisNovo',
        props: {
            reservaId: {
                type: String,
                required: true,
            },
        },
        methods: {
            async adicionarIntervalos() {
                await this.$router.replace({name: 'ModalAdicionarIntervalos', params: {id: this.reservaId}})
            }
        }
    }
</script>
