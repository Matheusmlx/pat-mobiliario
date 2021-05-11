importScripts('https://storage.googleapis.com/workbox-cdn/releases/5.1.2/workbox-sw.js')

self.addEventListener('message', (e) => {
    if (e.data.action === 'skipWaiting') self.skipWaiting()
})

self.__precacheManifest = [].concat(
    [
        {
            'revision': '3be7016ef4bc439e76af',
            'url': 'css/app.1c96ec5a.css'
        },
        {
            'revision': '14e6beb0ee995e59857c',
            'url': 'css/chunk-vendors.49759f45.css'
        },
        {
            'revision': 'd441705e1b8d7c26f3f4fe5836ae19bd',
            'url': 'index.html'
        },
        {
            'revision': '3be7016ef4bc439e76af',
            'url': 'js/app.bee97167.js'
        },
        {
            'revision': '14e6beb0ee995e59857c',
            'url': 'js/chunk-vendors.f93db6b8.js'
        },
        {
            'revision': '24597ec85069f8cf1716e1cecbde348a',
            'url': 'manifest.json'
        },
        {
            'revision': 'b6216d61c03e6ce0c9aea6ca7808f7ca',
            'url': 'robots.txt'
        },
        {
            'revision': 'c3ca045608a3d72a76f71b9d7667ee50',
            'url': 'service-worker.js'
        }
    ]
)
workbox.precaching.precacheAndRoute(self.__precacheManifest, {})
