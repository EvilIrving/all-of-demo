import { defineConfig } from 'vitepress'
import arr from './nav'
import sidebar from './sidebar'

// https://vitepress.dev/reference/site-config
export default defineConfig({
  title: "Cain's Blog",
  description: "Life has many moments worth remembering.",
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: arr,
    sidebar: sidebar,
    socialLinks: [
      { icon: 'github', link: 'https://github.com/vuejs/vitepress' }
    ]
  }
})
