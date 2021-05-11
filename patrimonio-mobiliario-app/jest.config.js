const Vue = require('vue')
Vue.config.productionTip = false

module.exports = {
    moduleFileExtensions: [
        'js',
        'jsx',
        'json',
        'vue'
    ],
    transform: {
        '^.+\\.vue$': 'vue-jest',
        '.+\\.(css|styl|less|sass|scss|svg|png|jpg|ttf|woff|woff2)$': 'jest-transform-stub',
        '^.+\\.jsx?$': 'babel-jest'
    },
    transformIgnorePatterns: [
        'node_modules/(?!(@azinformatica|vue-radial-progress|swiper)/)'
    ],
    moduleNameMapper: {
        '^@/(.*)$': '<rootDir>/src/$1'
    },
    snapshotSerializers: [
        'jest-serializer-vue'
    ],
    testMatch: [
        '**/src/**/*.spec.(js|jsx|ts|tsx)|**/__src__/*.(js|jsx|ts|tsx)'
    ],
    clearMocks: true,
    testURL: 'http://localhost/',
    collectCoverage: true,
    collectCoverageFrom: [
        '**/*.{js,vue}',
        '!**/node_modules/**'
    ],
    coverageReporters: [
        'html',
        'text-summary',
        'lcov'
    ],
    coverageDirectory: '.tmp'
}
