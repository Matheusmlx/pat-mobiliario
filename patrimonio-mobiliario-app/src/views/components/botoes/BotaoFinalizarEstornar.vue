<template>
    <v-btn
        color="primary"
        depressed
        :loading="loading"
        @click="finalizar">
        <span :class="{'text-capitalize': textCapitalize}">Estornar</span>
    </v-btn>
</template>

<script>
    export default {
        name: 'BotaoFinalizar',
        props: {
            textCapitalize: {type: Boolean, required: false, default: false}
        },
        data(){
            return {
                loading:false
            }
        },
        methods: {
            finalizar() {
                this.$emit('finalizar')
                this.loading = true
            }
        }
    }
</script>
