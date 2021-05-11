import Alert from './Alert'
import {createLocalVue} from '@vue/test-utils'
import Vuex from 'vuex'

const localVue = createLocalVue()
localVue.use(Vuex)

const mutations = {
    showAlert: jest.fn()
}

const store = new Vuex.Store({ mutations })

const alert = new Alert(store)

describe('Alert', () => {

    it('Deve commitar uma mutation de mensagem de erro', () => {
        const key = 'Não foi possível efetuar o cadastro!'
        alert.showError(key)
        expect(mutations.showAlert).toHaveBeenCalledWith({}, { message: key, type: 'error' })
    })

    it('Deve commitar uma mutation de mensagem de sucesso', () => {
        const key = 'Fornecedor cadastrado com sucesso!'
        alert.showInfo(key)
        expect(mutations.showAlert).toHaveBeenCalledWith({}, { message: key, type: 'success' })
    })

    it('Deve commitar uma mutation de mensagem de erro genérica para mensagens não definidas', () => {
        alert.showError(null)
        expect(mutations.showAlert).toHaveBeenCalledWith({}, {
            message: 'Não foi possível realizar a operação!',
            type: 'error'
        })
    })

    it('Deve commitar uma mutation de mensagem de sucesso genérica para mensagens não definidas', () => {
        alert.showInfo(null)
        expect(mutations.showAlert).toHaveBeenCalledWith({}, {
            message: 'Operação realizada com sucesso!',
            type: 'success'
        })
    })

})
