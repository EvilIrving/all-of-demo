/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const nestedRouter = [
  {
    path: "/dataManage",
    component: Layout,
    redirect: 'noRedirect',
    name: '数据管理',
    meta: {
      icon: "menu",
      noCache: false,
      title: "数据管理",
    },
    children: [{
      alwaysShow: null,
      children: [],
      component: "dataManage/dataSheetManage/dataSheetManage",
      hidden: false,
      meta: {
        icon: "component",
        noCache: true,
        title: "数据表管理"
      },
      name: "dataSheetManage",
      path: "dataSheetManage",
      redirect: null,
    }
    ]
  }
]

export default nestedRouter
