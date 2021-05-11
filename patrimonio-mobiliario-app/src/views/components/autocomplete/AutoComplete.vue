<template>
    <v-autocomplete
        v-model="fieldValue"
        :item-text="itemText"
        :item-value="itemValue"
        :no-data-text="noDataText"
        :name="name"
        :ref="getRefName"
        :v-validate="required"
        :items="items"
        :disabled="disabled"
        :class="{desativado: disabled}"
        :placeholder="placeholder"
        :filter="filter"
        :error-messages="!fieldValue ? errors.collect(name) : ''"
        @change="tratarEventoAlteracao">
        <template v-if="label" v-slot:label>
            {{ label }}
            <span v-if="requiredIcon" class="ml-1 red--text">*</span>
        </template>
        <template v-else-if="campo && nomeTela" v-slot:label>
            <label-personalizado :campo="campo"
                                 :tela="nomeTela"
                                 :permissaoEdicao="permissaoEdicao"
                                 :obrigatorioPorPadrao="obrigatorioPorPadrao"/>
        </template>
        <template v-slot:item="data">
            <label
                :style="obterLarguraAutoComplete(getRefName)"
                class="cursor-pointer auto-complete-item-text text-truncate">
                {{data.item.descricao}}
            </label>
        </template>
    </v-autocomplete>
</template>

<script>
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'

    export default {
        name: 'AutoComplete',
        components: {
            LabelPersonalizado
        },
        props: {
            items: {
                type: Array,
                required: false
            },
            disabled: {
                type: Boolean,
                default: false
            },
            filter: {
                type: Function
            },
            value: {
                type: Number
            },
            name: {
                type: String,
                required: true
            },
            required: {
                type: Boolean,
                default: false
            },
            requiredIcon: {
                type: Boolean,
                default: false
            },
            placeholder: {
                type: String
            },
            label: {
                type: String
            },
            itemText: {
                type: String
            },
            itemValue: {
                type: String
            },
            noDataText: {
                type: String
            },
            campo: {
                type: String
            },
            nomeTela: {
                type: String
            },
            obrigatorioPorPadrao: {
                type: Boolean,
                default: false
            },
            permissaoEdicao: {
                type: Boolean,
                default: false
            }
        },
        computed: {
            fieldValue: {
                get() {
                    return this.value
                },
                set(value) {
                    this.$emit('changeValue', this.name, value)
                }
            },
            getRefName() {
                return 'autoComplete'+this.name
            }
        },
        methods: {
            tratarEventoAlteracao(value) {
                this.$emit('change', value)
            },
            obterLarguraAutoComplete(ref) {
                if (this.$refs[ref] && this.$refs[ref].$el) {
                    return {
                        width: (this.$refs[ref].$el.clientWidth-30)+'px'
                    }
                }
                return {}
            }
        }
    }
</script>

<style lang="stylus" scoped>
    .cursor-pointer
        cursor pointer
</style>
