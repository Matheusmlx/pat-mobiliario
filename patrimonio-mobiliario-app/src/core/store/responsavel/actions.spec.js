import actions from './actions'
import {actionTypes} from '@/core/constants'

let url, mockResponseData

jest.mock('axios', () => ({
    get(_url) {
        return new Promise((resolve) => {
            url = _url
            resolve({data: mockResponseData})
        })
    }
}))

describe('Actions', () => {

    beforeEach(() => {
        mockResponseData = true
    })

    it('BUSCAR_TODOS_RESPONSAVEIS', async () => {
        const response = await actions[actionTypes.RESPONSAVEL.BUSCAR_TODOS_RESPONSAVEIS]()

        expect(response).toBeTruthy()
        expect(url).toBe('api/responsavel')
    })

})
