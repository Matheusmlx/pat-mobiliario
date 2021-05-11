<template>
    <v-tooltip top>
        <template v-slot:activator={on}>
            <v-btn
                v-on="on"
                icon
                color="primary"
                @click="salvar">
                <v-icon small>fas fa-check</v-icon>
            </v-btn>
        </template>
        Salvar
    </v-tooltip>
</template>

<script>
    export default {
        name: 'BotaoIconeSalvar',
        methods: {
            salvar() {
                this.$emit('salvar')
            }
        }
    }
</script>

<style scoped lang="stylus">
</style>
