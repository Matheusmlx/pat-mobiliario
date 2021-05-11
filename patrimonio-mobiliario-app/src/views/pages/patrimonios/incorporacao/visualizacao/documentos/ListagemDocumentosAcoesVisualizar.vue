<template>
    <div class="az-actions-form">
        <v-btn @click="tratarEventoVoltar" color="grey" outlined text>
            <v-icon class="mr-1" size="15">fas fa-arrow-left</v-icon>
            VOLTAR
        </v-btn>
        <v-spacer/>
        <v-btn
            color="primary"
            depressed
            class="btn-disabled"
            disabled
            v-if="!itensNaoFinalizados">
            <v-icon class="mr-1" size="15">fas fa-check</v-icon>
            Finalizar
        </v-btn>
        <v-tooltip max-width="250" nudge-left="-10" top v-else>
            <template v-slot:activator="{ on }" :class="{'btn-disabled': itensNaoFinalizados}">
                <div v-on="on">
                    <v-btn
                        disabled
                        color="primary"
                        depressed>
                        <v-icon class="mr-1" size="15">fas fa-check</v-icon>
                        Finalizar
                    </v-btn>
                </div>
            </template>
            Todos os itens do passo 2 devem estar finalizados.
        </v-tooltip>
    </div>
</template>

<script>
    export default {
        name: 'ListagemDocumentosAcoesVisualizar',
        props: {
            itensNaoFinalizados: {
                type: Boolean,
                default: false
            }
        },
        methods: {
            tratarEventoVoltar() {
                this.$emit('voltar')
            }
        },
    }
</script>
