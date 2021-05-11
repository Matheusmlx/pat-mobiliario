import actions from './actions'

jest.mock('axios', () => ({
    get: () => new Promise(resolve => resolve({
        data: {urlChatSuporte: 'http://urlChatSuporte.com.br'}
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
        await actions.buscarTodosParametros({commit})
        expect(commit).toHaveBeenCalledWith('SET_PARAMETROS', {urlChatSuporte: 'http://urlChatSuporte.com.br'})
    })
})
