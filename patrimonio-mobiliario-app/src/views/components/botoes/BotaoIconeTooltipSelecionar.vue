<template>
    <v-tooltip top>
        <template v-slot:activator="{ on }">
            <v-btn v-on="on" @click="selecionar" :disabled="desabilitado"  icon>
                <v-icon size="18" >fa-check-circle</v-icon>
            </v-btn>
        </template>
        Selecionar
    </v-tooltip>
</template>

<script>
    export default {
        name: 'BotaoIconeTooltipSelecionar',
        props:{
            desabilitado:{
                type: Boolean,
                required: true
            }
        },
        methods: {
            selecionar() {
                this.$emit('selecionar')
            }
        }
    }
</script>

<style scoped lang="stylus">
</style>
