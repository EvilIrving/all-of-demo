import { defineConfig } from 'vite';
import legacy from '@vitejs/plugin-legacy';
import vueJsx from '@vitejs/plugin-vue-jsx';
import vue from '@vitejs/plugin-vue';
import PxToViewport from 'postcss-px2vp';

export default defineConfig({
  base: process.env.NODE_ENV === 'production' ? '/approval/' : '/',
  resolve: {
    alias: {
      '@': '/src'
    }
  },
  plugins: [
    vue(),
    vueJsx(),
    legacy({
      targets: ['Chrome 63'],
      additionalLegacyPolyfills: ['regenerator-runtime/runtime'],
      modernPolyfills: true
    })
  ],
  css: {
    postcss: {
      plugins: [
        PxToViewport({
          unitToConvert: 'px',
          viewportWidth: rule => {
            const file = rule.source?.input.file;
            return file.includes('vant') ? 375 : 750;
          },
          unitPrecision: 3,
          propList: ['*'],
          viewportUnit: 'vw',
          fontViewportUnit: 'vw',
          selectorBlackList: ['.ignore'],
          minPixelValue: 1,
          mediaQuery: false,
          replace: true,
          exclude: [],
          landscape: false,
          landscapeUnit: 'vw',
          landscapeWidth: 1628
        })
      ]
    }
  },
  server: {
    port: 2333,
    proxy: {
      '/api': {
        target: 'https://sgpt.wzsl.com:8081/api/',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api/, '')
      },
      '/gdt-api': {
        target: 'https://openplatform.dg-work.cn/',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/gdt-api/, '')
      },
      '/waterSearch/RV_RVAA/': {
        target: 'http://117.149.227.112:6080/arcgis/rest/services/waterSearch/RV_RVAA/',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/waterSearch\/RV_RVAA/, '')
      }
    }
  },
  build: {
    target: 'es2015'
  }
});
