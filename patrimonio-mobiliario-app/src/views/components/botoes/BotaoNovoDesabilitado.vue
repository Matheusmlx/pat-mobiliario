<template>
    <v-tooltip
        top
        max-width="200">
        <template v-slot:activator="{ on }">
            <div v-on="on">
                <az-call-to-action
                    class="botao-novo"
                    hideBorder
                    disabled>
                    <v-icon>add_circle</v-icon>
                    <span class="text">Novo</span>
                </az-call-to-action>
            </div>
        </template>
        <span>{{ mensagemTooltip }}</span>
    </v-tooltip>
</template>

<script>
    export default {
        name: 'BotaoNovoDesabilitado',
        props:{
            mensagemTooltip:{
                type: String,
                required: true
            }
        }
    }
</script>
