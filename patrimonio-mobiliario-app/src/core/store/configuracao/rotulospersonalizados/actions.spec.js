import actions from './actions'

jest.mock('axios', () => ({
    get: () => new Promise(resolve => resolve({
        data: {rotulosPersonalizados: {i18n: {telas: [{tipo: 'DOCUMENTOS'}]}}}
    }))
}))

describe('actions', () => {
    let commit, spyConsoleError

    beforeEach(() => {
        spyConsoleError = jest.spyOn(global.console, 'error').mockImplementation(() => {
        })
        commit = jest.fn()
    })

    afterEach(() => {
        spyConsoleError.mockRestore()
    })

    it('deve chamar a api e chamar o commit com o resultado da api', async () => {
        await actions.getAllRotulosPersonlizados({commit})
        expect(commit).toHaveBeenCalledWith('SET_ROTULOS_PERSONALIZADOS', {telas: [{tipo: 'DOCUMENTOS'}]})
    })
})
