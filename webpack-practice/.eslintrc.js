module.exports = {
    env: {
        browser: true,
        es2021: true,
    },
    extends: [
        // "standard",
        // "plugins:vue/strongly-recommended"
    ],

    plugins: [],
    parserOptions: {
        ecmaVersion: 6,
        sourceType: "module",
        ecmaFeature: {
            jsx: true
        }
    },
    rules: {
        "no-console": 1
    }

}