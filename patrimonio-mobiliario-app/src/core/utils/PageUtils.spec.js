import {PageUtils} from './index'
import router from '@/views/routers'

jest.useFakeTimers()
jest.mock('@/views/routers', () => ({
    beforeEach: jest.fn(),
    push: jest.fn(),
    history: {
        current: {
            path: '/'
        }
    }
}))

describe('PageUtils', () => {
    let pageUtils

    beforeEach(() => {
        pageUtils = new PageUtils(router)
    })

    it('constructor', () => {
        expect(pageUtils.router).toBeDefined()
    })

    it('goToHome', () => {
        pageUtils.goToHome()

        expect(pageUtils.router.history.current.path).toEqual('/')
    })

    it('reload', () => {
        window.location.reload = jest.fn()
        pageUtils.reload()
        jest.runAllTimers()

        expect(window.location.reload).toHaveBeenCalled()
    })
})
