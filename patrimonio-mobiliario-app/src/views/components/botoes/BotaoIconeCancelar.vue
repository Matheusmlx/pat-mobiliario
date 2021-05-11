<template>
    <v-tooltip top>
        <template v-slot:activator={on}>
            <v-btn @click="cancelar" v-on="on" icon>
                <v-icon small>fas fa-times</v-icon>
            </v-btn>
        </template>
        Cancelar
    </v-tooltip>
</template>

<script>
    export default {
        name: 'BotaoIconeCancelar',
        methods: {
            cancelar() {
                this.$emit('cancelar')
            }
        }
    }
</script>

<style scoped lang="stylus">
</style>
