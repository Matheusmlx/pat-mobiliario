<template>
    <v-tooltip top nudge-top="30" max-width="200">
        <template v-slot:activator="{ on }">
            <div v-on="on" class="btn-disabled">
                <v-btn
                    disabled
                    color="primary"
                    depressed>
                    Salvar
                </v-btn>
            </div>
        </template>
        É necessário que todos os campos obrigatórios sejam preenchidos para salvar.
    </v-tooltip>
</template>

<script>
    export default {
        name: 'BotaoSalvar'
    }
</script>
