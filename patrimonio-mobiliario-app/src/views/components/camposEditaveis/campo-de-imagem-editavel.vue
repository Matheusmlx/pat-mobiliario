<template>
    <div class="az-text az-text-edit az-text-view-edit-text-area alinhaMentoBox">
        <div v-if="!editing">
            <label>{{ label }}</label>
                <v-hover v-slot="{ hover }" class="image-container">
                    <v-card max-width="50" tile elevation="0">
                        <v-img
                            v-if="value"
                            :src="'data:image/png;base64,' + value"
                            max-width="50"
                            max-height="50"
                            aspect-ratio="1"
                            class="img-campo"/>
                        <v-img
                            v-else
                            max-width="50"
                            max-height="50"
                            aspect-ratio="1"
                            class="img-campo"/>
                        <v-overlay :value="hover" absolute v-if="validate !== 'required' && value">
                            <v-tooltip top v-if="!disabled">
                                <template v-slot:activator="{ on }">
                                    <v-btn x-small v-on="on" class="start-edit-btn" icon @click="removerImagem">
                                        <v-icon small>fas fa-times</v-icon>
                                    </v-btn>
                                </template>
                                Remover
                            </v-tooltip>
                        </v-overlay>
                    </v-card>
                </v-hover>
                <v-tooltip top v-if="!disabled">
                    <template v-slot:activator="{ on }">
                        <v-btn x-small v-on="on" class="start-edit-btn" icon @click="startEdit">
                            <v-icon class="mt-2" x-small>fas fa-edit</v-icon>
                        </v-btn>
                    </template>
                    {{labelBtnEdit}}
                </v-tooltip>
                <v-tooltip top v-if="value">
                    <template v-slot:activator="{ on }">
                        <v-btn x-small v-on="on" icon slot="activator" class="ml-3" :href="criarLinkDownload(uri)" target="_blank">
                            <v-icon x-small color="primary" class="start-edit-btn btn-download">fas fa-download</v-icon>
                        </v-btn>
                    </template>
                    Download
                </v-tooltip>
        </div>

        <div>
            <v-file-input
                v-if="editing"
                v-model="model"
                class="file-input-imagem-component"
                :name="name"
                file
                dense
                outlined
                append-icon="fa-file-upload"
                accept="image/png, image/jpeg, image/jpg"
                auto-grow
                prepend-icon
                :class="stylus"
                :error-messages="errorTamanhoExcedido ==1 ? 'Tamanho permitido: 15MB' : errors.collect(name)"
                :v-validate="validate"
                :placeholder="placeholder"
                :clearable="false"
                @keyupEsc="cancelEdit"
                @click:append="cancelEdit">
                <template v-slot:append>
                    <v-tooltip top>
                        <template v-slot:activator="{ on }">
                            <v-btn v-on="on" icon @click="submitEdit">
                                <v-icon class="primary--text" small>fas fa-check</v-icon>
                            </v-btn>
                        </template>
                        {{labelBtnSave}}
                    </v-tooltip>
                    <v-tooltip top>
                        <template v-slot:activator="{ on }">
                            <v-btn v-on="on" icon @click="cancelEdit">
                                <v-icon small>fas fa-times</v-icon>
                            </v-btn>
                        </template>
                        {{labelBtnCancel}}
                    </v-tooltip>
                </template>
            </v-file-input>
            <p v-if="editing && !this.model && errorTamanhoExcedido!==1" class="tamanhoPermitidoImagemClass">Tamanho
                permitido: 15MB</p>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'campo-de-imagem-editavel',
        props: {
            value: {
                required: true
            },
            name:{
                type: String,
                required: true
            },
            stylus: {
                type: String
            },
            label: {
                type: String,
                default: ''
            },
            labelBtnSave: {
                type: String,
                default: 'Salvar'
            },
            labelBtnCancel: {
                type: String,
                default: 'Cancelar'
            },
            labelBtnEdit: {
                type: String,
                default: 'Editar'
            },
            validate: {
                type: String,
                default: 'required'
            },
            placeholder: {
                type: String
            },
            disabled: {
                type: Boolean,
                default: false
            },
            uri:{
                required:true
            }
        },
        $_veeValidate: {
            validator: 'new'
        },
        data: () => ({
            model: undefined,
            editing: false,
            errorObrigatoriedadeAnexo: 0,
            errorTamanhoExcedido: 0,
        }),
        watch: {
            value(val) {
                this.model = val
            }
        },
        methods: {
            startEdit() {
                this.model = null
                this.editing = true
                this.$emit('setaEditando', this.name)
            },
            async submitEdit() {
                if (this.model) {
                    const anexo = this.model
                    if (this.extensaoArquivoValida(anexo) === -1) {
                        this.mostrarNotificacaoErro('Extensão do arquivo inválida. Envie arquivos nos seguintes formatos .jpeg .jpg .png')
                        this.model = null
                    } else {
                        if (this.validarTamanhoMaximo(this.model.size)) {
                            this.errorTamanhoExcedido = 1
                            this.model = await null
                            setTimeout(() => {
                                this.errorTamanhoExcedido = 0
                                this.errorObrigatoriedadeAnexo = 1
                                this.$validator._base.validateAll()
                            }, 3000)
                        } else {
                            this.editing = false
                            this.$emit('retiraEditando', this.name)
                            this.$emit('input', anexo)
                        }

                    }
                } else {
                    this.mostrarNotificacaoErro('Sem imagem em anexo para salvar')
                }
            },
            removerImagem(){
                if(this.validate !== 'required') {
                    this.$emit('input', null)
                }
            },
            validarTamanhoMaximo(tamanhoAnexo) {
                if (tamanhoAnexo >= 15116247) {
                    return true
                }
                return false
            },
            extensaoArquivoValida(nomeArquivo) {
                const extensaoAceitas = ['.jpg', '.JPG', '.png', '.PNG', '.jpeg', '.JPEG']
                const extensao = nomeArquivo.name.substr(nomeArquivo.name.lastIndexOf('.'), nomeArquivo.name.length)
                return extensaoAceitas.indexOf(extensao)
            },
            cancelEdit() {
                this.editing = false
                this.$emit('retiraEditando', this.name)
            },
        },
        computed: {
            nomeAnexo() {
                return typeof this.value === 'string' ? this.value.split('/')[1] : ''
            }
        }
    }
</script>

<style lang="stylus" scoped>

    .btn-download
        opacity 1

    alinhaMentoBox
        margin-bottom: 20px

    .formatoTexto
        color: #6b6b6b

    .file-input-imagem-component
        font-size 13px
        height 65px
        width 85%

    .truncate
        width: 130px
        white-space: nowrap
        overflow: hidden
        text-overflow: ellipsis

    .img-campo
        border 1px solid #777

    .imagem-content
        display flex
        flex-direction row

    .imagem-buttons
        display flex
        flex-direction column
        margin-left 5px

    .tamanhoPermitidoImagemClass
        color #a5a5a5
        margin-top -10px
        margin-bottom 5px
        font-size 12px

    @media (max-width: 1100px)
        .truncate
            width 125px

    @media (max-width: 1211px)
        .tamanhoPermitidoImagemClass
            margin-top 0px

        .image-container
            margin-bottom 10px
</style>
