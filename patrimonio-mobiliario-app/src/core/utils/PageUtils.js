export default class PageUtils {
    constructor(router) {
        this.router = router
    }

    goToHome() {
        this.router.push({ path: '/' })
    }

    reload() {
        setTimeout(() => window.location.reload(), 3000)
    }

}
