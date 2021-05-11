import listeners from '../listeners'

class TriggerEvent {

    async triggerOnStartEvents() {
        const starterPromise = Promise.resolve(null)
        const tasks = Object.keys(listeners)
        await tasks.reduce((p, task) => p.then(() => listeners[task].execute().then()), starterPromise)
    }
}

export default new TriggerEvent()