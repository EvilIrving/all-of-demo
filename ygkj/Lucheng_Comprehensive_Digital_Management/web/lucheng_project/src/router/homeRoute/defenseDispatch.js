/*
 * @Author: hanyu
 * @LastEditTime: 2022-05-17 17:13:38
 * @Description: 灾害防御调度 路由
 * @FilePath: \lucheng_project\src\router\homeRoute\defenseDispatch.js
 */
import homeView from "@/layouts/HomeView.vue";
export default [
  {
    path: '/defenseDispatch',
    name: 'defenseDispatch',
    title: '灾害防御调度',
    redirect: '/defenseDispatch/defense',
    component: homeView,
    children: [
      {
        path: '/defenseDispatch/defense',
        name: 'defense',
        title: '山洪灾害平台',
        component: (resolve) => require(['@/views/defenseDispatch/DefenseDispatch.vue'], resolve),
      },
      // {
      //   path: '/defenseDispatch/personLiable',
      //   name: 'personLiable',
      //   title: '山洪灾害责任人表',
      //   component: (resolve) => require(['@/views/defenseDispatch/personLiable/PersonLiable.vue'], resolve),
      // },
      {
        path: '/defenseDispatch/comprehensive',
        name: 'comprehensive',
        title: '山洪灾害数据表',
        redirect: '/defenseDispatch/dataSheet',
        component: homeView,
        children: [
          {
            path: '/defenseDispatch/dataSheet',
            name: 'dataSheet',
            title: '山洪灾害数据表',
            hideFourth: true,
            redirect: '/defenseDispatch/personLiable',
            component: (resolve) => require(['@/views/defenseDispatch/MountainTorrentDisasterIndex.vue'], resolve),
            children: [
              {
                path: '/defenseDispatch/personLiable',
                name: 'personLiable',
                title: '山洪灾害责任人表',
                component: (resolve) => require(['@/views/defenseDispatch/personLiable/PersonLiable.vue'], resolve)
              },
              {
                path: "/defenseDispatch/fishingBoatNameList",
                name: "fishingBoatNameList",
                title: '渔船名单',
                component: (resolve) => require(['@/views/defenseDispatch/fishingBoatNameList/FishingBoatNameList.vue'], resolve)
              },
              {
                path: '/defenseDispatch/largeGrainGrowersNameList',
                name: 'largeGrainGrowersNameList',
                title: '种粮大户名单',
                component: (resolve) => require(['@/views/defenseDispatch/largeGrainGrowersNameList/LargeGrainGrowersNameList.vue'], resolve)
              },
              {
                path: '/defenseDispatch/defenseObject',
                name: 'defenseObject',
                title: '山洪灾害防御对象',
                component: (resolve) => require(['@/views/defenseDispatch/defenseObject/DefenseObject.vue'], resolve)
              },
            ]
          }
        ]
      },
      {
        path: '/defenseDispatch/speWarning',
        name: 'speWarning',
        title: '声光电预警平台',
        component: (resolve) => require(['@/views/defenseDispatch/SpeWarningPlatform.vue'], resolve),
      },
    ]
  }
]