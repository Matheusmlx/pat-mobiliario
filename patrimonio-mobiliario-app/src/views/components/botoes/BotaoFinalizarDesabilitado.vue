<template>
    <v-tooltip
        top
        nudge-top="30"
        max-width="200">
        <template v-slot:activator="{ on }">
            <div v-on="on" class="btn-disabled">
                <v-btn
                    disabled
                    color="primary"
                    depressed>
                    <span>Finalizar</span>
                </v-btn>
            </div>
        </template>
        {{ texto }}
    </v-tooltip>
</template>

<script>
    export default {
        name: 'BotaoFinalizarDesabilitado',
        props: {
            texto: {
                type: String,
                required: true
            }
        }
    }
</script>
