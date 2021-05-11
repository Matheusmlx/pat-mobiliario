import actions from './actions'
import {actionTypes} from '@/core/constants'

let url, mockResponseData, returnedResponse

jest.mock('axios', () => ({
    get(_url) {
        return new Promise((resolve) => {
            url = _url
            resolve({data: mockResponseData})
        })
    }
}))

describe('Actions', () => {
    const context = jest.fn()

    beforeEach(() => {
        url = ''
        mockResponseData = true
    })

    it('Deve buscar comodantes dinamicamente', async () => {
        const conteudo = 'Gilberto'
        returnedResponse = await actions[actionTypes.COMODATO.BUSCAR_COMODANTES_DINAMICAMENTE](context, conteudo)
        expect(url).toBe(`api/comodantes?conteudo=${conteudo}&page=1&size=100`)
    })
})
