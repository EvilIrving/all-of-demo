export default [
  {
    name: "防洪减灾调度系统",
    value: "防洪减灾调度系统",
    children: [
      {
        label: "防汛物资管理",
        icon: "icon-menu-disaster",
        iconb: "icon-menu-disaster-b",
        children: [
          {
            name: "防汛仓库管理",
            url: "/warehouseManage",
            iconClass: "icon-menu-law",
            children: []
          },{
            name: "抢险队伍管理",
            url: "/teamManage",
            iconClass: "icon-menu-regulation",
            children: []
          }
        ]
      },
    ]
  }
];
