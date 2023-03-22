import resolve from '@rollup/plugin-node-resolve';
import { terser } from "rollup-plugin-terser";
import commonjs from 'rollup-plugin-commonjs';
import cleanup from 'rollup-plugin-cleanup';
import babel from 'rollup-plugin-babel';
export default {
    input: 'app.js',
    output: {
        // dir: 'dist',
        file: 'bundles.js',
        // format: 'es', // es cjs umd amd iife
    },
    // 指出哪些模块需要被视为外部引入
    external: ['lodash-es'],
    plugins: [
        resolve({
            // 将自定义选项传递给解析插件
            customResolveOptions: {
                moduleDirectory: 'node_modules'
            }
        }),
        commonjs(),
        terser(),

        babel({
            babelHelpers: 'bundled',
            exclude: 'node_modules/**' // 只编译我们的源代码
        }),
        cleanup(),
    ],
}