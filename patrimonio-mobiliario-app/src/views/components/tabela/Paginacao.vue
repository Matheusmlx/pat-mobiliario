<template>
    <div class="az-pagination paginacao">
        <v-pagination
            v-if="paginas > 1"
            v-model="paginacaoInterna.page"
            total-visible="7"
            :length="paginas"
            class="pt-3"/>
        <v-spacer v-if="paginas <=1"/>
        <div class="az-select-pagination">
            <span>Linhas por página:</span>
            <v-select :items="linhasPorPagina" @change="resetaPage" v-model="paginacaoInterna.rowsPerPage"/>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'Paginacao',
        props: {
            paginacaoInterna: {
                type: Object,
                required: true
            },
            paginas: {
                type: Number,
                required: true
            },
            linhasPorPagina: {
                type: Array,
                default: () => {
                    return [10, 25, 50, 100]
                }
            }
        },
        methods: {
            resetaPage() {
                this.$emit('resetaPage')
            }
        }
    }
</script>

<style scoped lang="stylus">
    .paginacao
        height 50px !important

    .az-pagination >>> .v-pagination__item
        width 41px !important
</style>
