// 天生支持 ts css 以及各种资源

//  vite.config.ts
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import * as path from 'path'
export default defineConfig({
    // root: '/',
    base: "www.xx.com",
    publicPath: './static',
    esbuild: {
        jsxFactory: 'h',
        jsxFragment: 'Fragment',
        jsxInject: 'import {h} from "vue";'
    },
    reslove: {
        extension: ['.js', '.ts', '.css'],
        alias: {
            '@': path.resolve(__dirname, 'src'),
            '@views': path.resolve(__dirname, 'src/views'),
            '@layout': path.resolve(__dirname, 'src/layout'),
            '@components': path.resolve(__dirname, 'src/components')
        }

    },
    build: {
        rollupOptions: {
            // 将vue切分出来 
            // manualChunks: {
            //     vendor: ['vue']
            // }
            input: './index.html',
            output: {
                entryFileNames: 'bundle.js',
                chunkFileNames: '[name].chunks.js'
            },

            manualChunks: (id) => {
                if (id.includes('node_modules')) {
                    return 'vendor'
                }
            }
        },
        assetsInlineLimit: 20000
    },
    server: {
        port: 2000,
        proxy: {
            "/api": {
                targer: 'www.xxx.com',
                rewrite: (path) => {
                    return path.replace(/^\/api/, 'xxx')
                },
            }
        },
        headers: {

        }
    },
    plugins: [
        vue()
    ]
})