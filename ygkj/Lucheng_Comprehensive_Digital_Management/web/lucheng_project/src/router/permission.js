import router from './index'
import store from '../store/index'
import { sessionData } from '../plugins/storage'
const whiteList = ['/login']
router.beforeEach(async (to, form, next) => {
  let userInfo = sessionData('get', 'userInfo')
  if (userInfo && userInfo.accessToken) {
    if (to.path != '/login') {
      let hasRouters = store.getters.routesList
      if (hasRouters.length > 0) {
        next()
      } else {
        try {
          let accessRoutes = await store.dispatch("jobRoutes", userInfo.jobId)
          if (accessRoutes.length > 0) {
            router.addRoutes(accessRoutes)
            next({ ...to, replace: true })
          } else {
            cleanUser()
            next(`/login`)
          }
        } catch {
          cleanUser()
          next(`/login`)
        }
      }
    } else {
      next()
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      cleanUser()
      next(`/login`)
    }
  }
})

function cleanUser() {
  store.commit("ADD_ROUTES", []);
  sessionData("clean", "userInfo");
}