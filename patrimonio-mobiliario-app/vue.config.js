const path = require('path')

module.exports = {
    publicPath: '/patrimonio-mobiliario/',
    outputDir: path.resolve(__dirname, 'app'),
    pwa: {
        name: 'Patrimônio mobiliário',
        themeColor: '#4DBA87',
        msTileColor: '#000000',
        appleMobileWebAppCapable: 'yes',
        appleMobileWebAppStatusBarStyle: 'black',
        workboxPluginMode: 'InjectManifest',
        workboxOptions: {
            swSrc: 'public/service-worker.js',
        }
    }
}
