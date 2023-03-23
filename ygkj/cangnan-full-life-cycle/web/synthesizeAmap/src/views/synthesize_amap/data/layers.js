//水利工程图层
export const hydraulic = 
[
  {
    id: 20,
    name: "水利工程",
    icon: "icon-tree-project",
    disabled: true,
    children: [
      {
        name: "水库",
        url: "/projectSide?type=水库",
      },{
        name: "山塘",
        url: "/projectSide?type=山塘",
      },{
        name: "堤防",
        url: "/projectSide?type=堤防",
      },{
        name: "海塘",
        url: "/projectSide?type=海塘",
      },{
        name: "泵站",
        url: "/projectSide?type=泵站",
      },{
        name: "水闸",
        url: "/projectSide?type=水闸",
      },
    ]
  }
]