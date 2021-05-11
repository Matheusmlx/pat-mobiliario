import register from './engine/Register'
import triggerEvents from './engine/TriggerEvent'
import rootPageCreator from './engine/RootPageCreator'

export default class Application {

    static run() {
        register.registerAll()

        triggerEvents.triggerOnStartEvents()
            .then(() => {
                rootPageCreator.createInstance()
            })
            .catch(() => {
                rootPageCreator.createBootstrapError()
            })
    }

}
