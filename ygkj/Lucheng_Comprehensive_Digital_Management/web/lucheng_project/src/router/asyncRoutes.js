import homeView from "@/layouts/HomeView.vue";
import Home from "@/layouts/Home.vue";

export function getAsyncRoutes(routes) {
  let route = dealRoutes(routes);
  let projectDetail = []
  let damMenu = []
  // let propertyEffectMenu = []
  for (let i = 0; i < route.length; i++) {
    if (route[i].path === '/seawallPrevent') {
      let index = route[i].children.findIndex(item => item.path == '/seawallPrevent/projectList')
      projectDetail = route[i].children[index].children
      route[i].children[index].children = []
    }
    if (route[i].path === '/propertyManage') {
      let index = route[i].children.findIndex(item => item.path == '/propertyManage/propertyUnitMatter')
      damMenu = route[i].children[index].children
      route[i].children[index].children = []
    }
    // if (route[i].title === '大坝研判') {
    //   damMenu = route[i].children
    //   route[i].children = []
    // }
  }
  let outRoute = [
    {
      path: '/',
      name: 'home',
      redirect: route.length ? route[0].path : '',
      component: Home,
      children: route.reverse()
    },
    ...projectDetail,
    ...damMenu,
    // ...propertyEffectMenu
  ];

  return outRoute;
}

function dealRoutes(routes) {
  const res = []
  routes.forEach(item => {
    const newItem = {};
    if (item.resType != '3') { // 3是功能权限，这里只遍历目录菜单
      if (item.component) {
        newItem['title'] = item.resName;
        newItem['name'] = item.name;
        newItem['path'] = item.resUrl ? item.resUrl : item.redirect;
        newItem['hideFourth'] = item.hideSecond;
        newItem['children'] = item.sysJobResTrees;
        let obj = {
          id: item.id,
          title: item.resName,
        };
        newItem['meta'] = obj;
        // newItem['meta']['title'] = item.resName;
        ('redirect' in item) ? newItem['redirect'] = item.redirect : null;
        // if (item.resName == '洪水预报') {
        //   newItem['hideThird'] = true
        // }
        if (item.component === 'homeView') {
          newItem.component = homeView;
          if (item.sysJobResTrees && item.sysJobResTrees.length == 1) {
            newItem['redirect'] = item.sysJobResTrees[0].resUrl
          }
        } else {
          newItem.component = () => import(`@/views${item.component}`);
        }
      }
      if (newItem.children && newItem.children.length) {
        newItem.children = dealRoutes(item.sysJobResTrees);
      }
      res.push(newItem);
    }
  })
  return res;
}