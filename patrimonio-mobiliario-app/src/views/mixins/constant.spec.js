import constant from './constant'
import {notificacoesDefault} from '../../core/constants'

describe('constant', () => {
    it('Deve formatar lista', () => {
        const valor = constant.methods.formatarLista(notificacoesDefault)
        expect(valor).toEqual(['ERRO_DEFAULT', 'SUCESSO_DEFAULT'])
    })

    it('Deve retornar vazio', () => {
        const Observer = constant.data()
        const observer = {__ob__:Observer}
        const valor = constant.methods.formatarLista(observer)
        expect(valor).toEqual([])
    })
})
