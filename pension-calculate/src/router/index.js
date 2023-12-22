import Vue from 'vue'
import VueRouter from 'vue-router'
import PensionCalc from '../views/PensionCalc.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: PensionCalc,
  },
  {
    path: '/comps',
    name: 'card',
    component: (resolve) => { require(['../views/Comps.vue'], resolve) },
  },
  
]

const router = new VueRouter({
  mode: 'hash',
  routes
})

export default router
