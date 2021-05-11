<template>
    <div class="az-text">
        <label v-if="labelPadrao" class="label">{{labelPadrao}} {{complementoLabel}}</label>
        <label v-else class="label">{{getNomeRotulosPersonalizados(campo,tela)}} {{complementoLabel}}</label>
        <v-tooltip v-if="text && text.length > 30" top nudge-top max-width="800">
            <template v-slot:activator="{ on }">
                <div v-on="on">
                    <p class="mb-0 text-truncate texto max-13">{{ text }}</p>
                </div>
            </template>
            <p class="mb-0" >{{ text }}</p>
        </v-tooltip>
        <p v-else class="texto">{{ text }}</p>
    </div>
</template>

<script>
    import {createNamespacedHelpers} from 'vuex'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')
    export default {
        name: 'CampoApresentacao',
        props: {
            text: {
                required: true
            },
            campo: {
                type: String
            },
            tela: {
                type: String
            },
            labelPadrao: {
                type: String
            },
            complementoLabel: {
                type: String,
                default: ''
            },
        },
        computed: {
            ...mapGetters([
                'getNomeRotulosPersonalizados'
            ]),
        }
    }
</script>
<style scoped lang="stylus">
.label
    font-size 13px
    font-weight bold
    color #777 !important

.texto
    font-size 13px
    color #777 !important


</style>
