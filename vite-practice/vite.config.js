// 天生支持 ts css 以及各种资源

//  vite.config.ts
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import * as path from 'path'
import postcssPxToViewport from 'postcss-px-to-viewport'
export default defineConfig({
    // root: '/',
    base: "www.xx.com",
    publicPath: './static',
    esbuild: {
        jsxFactory: 'h',
        jsxFragment: 'Fragment',
        jsxInject: 'import {h} from "vue";'
    },
    css: {
        preprocessorOptions: {
            scss: {
                // additionalData: `$injectedColor: orange;` // 全局变量
                additionalData: `@import '/src/assets/styles/variables.scss';` // 引入全局变量文件
            }
        },
        postcss: {
            plugins: [
                // viewport 布局适配
                postcssPxToViewport({
                    viewportWidth: 375
                })
            ]
        }
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
    optimizeDeps: {
        force: true // 强制进行依赖预构建 或通过删除 .vite 文件夹或运行 npx vite --force 来强制进行依赖预构建
    },
    build: {
        // outDir: 'build' // 打包文件的输出目录
        // assetsDir: 'static' // 静态资源的存放目录
        // assetsInlineLimit: 4096 // 图片转 base64 编码的阈值
        // 压缩
        minify: process.env.VITE_NODE_ENV === 'production' ? 'esbuild' : false,
        // 服务端渲染
        ssr: false,
        emptyOutDir: true,
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
                // if (id.includes('node_modules')) {
                //     return id.toString().split('node_modules/')[1].split('/')[0].toString()
                // }

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