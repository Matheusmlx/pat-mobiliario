import Vue from 'vue'
import VeeValidate, {Validator} from 'vee-validate'
import ptBR from 'vee-validate/dist/locale/pt_BR'
import {isCep, isCnpj, isCpf} from 'validator-brazil'

Vue.use(VeeValidate, {
    events: 'input|blur',
})

Validator.extend('unique', {
    validate(value, {currentValues}) {
        const found = currentValues.filter(current => current === value)
        return found.length === 0
    }
}, {
    paramNames: ['currentValues']
})

Validator.extend('cpf', {
    validate(value) {
        return isCpf(value)
    }
})

Validator.extend('cnpj', {
    validate(value) {
        return isCnpj(value)
    }
})

Validator.extend('cep', {
    validate(value) {
        return isCep(value)
    }
})

Validator.localize('pt_BR', ptBR)

Validator.localize({
    'pt_BR': {
        messages: {
            required: () => 'O campo é obrigatório',
            unique: field => `${field} duplicado`,
            cpf: field => `${field} inválido`,
            cnpj: field => `${field} inválido`,
            date_format: () => 'O campo não tem uma data válida',
            before: () => 'O campo tem uma data maior que a permitida',
            after: () => 'O campo tem uma data menor que a permitida'
        }
    }
})
