export const layersData = [{
    id: 20,
    label: "水利工程",
    icon: "icon-tree-project",
    disabled: true,
    children: [{
        id: 21,
        label: "水库",
        url: "/projectSide?type=水库",
        type: '水灾害防御',
        iconClass: 'icon-catalog-reservoir'
    },
    {
        id: 22,
        label: "山塘",
        url: "/projectSide?type=山塘",
        type: '水灾害防御',
        iconClass: 'icon-catalog-pool'
    },
    {
        id: 23,
        label: "堤防",
        url: "/projectSide?type=堤防",
        type: '水灾害防御',
        iconClass: 'icon-catalog-dike'
    },
    {
        id: 24,
        label: "海塘",
        url: "/projectSide?type=海塘",
        type: '水灾害防御',
        iconClass: 'icon-catalog-seawall'
    },
    {
        id: 25,
        label: "泵站",
        url: "/projectSide?type=泵站",
        type: '水灾害防御',
        iconClass: 'icon-catalog-pump'
    },
    {
        id: 26,
        label: "水闸",
        url: "/projectSide?type=水闸",
        type: '水灾害防御',
        iconClass: 'icon-catalog-sluice'
    },
    {
        id: 27,
        label: "闸站",
        url: "/projectSide?type=闸站",
        type: '水灾害防御',
        iconClass: 'icon-catalog-gatestation'
    },
    {
        id: 28,
        label: "灌区",
        url: "/projectSide?type=灌区",
        type: '水灾害防御',
        iconClass: 'icon-catalog-irrigated'
    },
    {
        id: 29,
        label: "农饮水",
        url: "/projectSide?type=农饮水",
        type: '水灾害防御',
        iconClass: 'icon-catalog-farming'
    },
    {
        id: 30,
        label: "水电站",
        url: "/projectSide?type=水电站",
        type: '水灾害防御',
        iconClass: 'icon-catalog-hydropower'
    }],
},
{
    id: 1,
    label: "感知监测",
    icon: "icon-tree-monitor",
    disabled: true,
    children: [{
        id: 4,
        label: "水情",
        disabled: true,
        children: [{
            id: 9,
            label: "水库水位",
            url: "/stationSide?type=水库水位",
            type: '水灾害防御',
            iconClass: 'icon-catalog-hydropower'
        },
        {
            id: 10,
            label: "河道水位",
            url: "/stationSide?type=河道水位",
            type: '水灾害防御',
            iconClass: 'icon-catalog-river'
        },
        {
            id: 11,
            label: "堰闸水位",
            url: "/stationSide?type=堰闸水位",
            type: '水灾害防御',
            iconClass: 'icon-catalog-checkgate'
        },
        {
            id: 12,
            label: "潮位",
            url: "/stationSide?type=潮位",
            type: '水灾害防御',
            iconClass: 'icon-catalog-sealevel'
        },
        {
            id: 20,
            label: "墒情",
            url: "/stationSide?type=墒情",
            type: '水灾害防御',
            // iconClass: 'icon-catalog-video'
        },
        ],
    },
    {
        id: 2,
        label: "雨情",
        children: [],
        url: "/stationSide?type=雨情",
        type: '水灾害防御'
    },
    {
        id: 3,
        label: "工情",
        disabled: true,
        children: [{
            id: 7,
            // label: "水闸开度",
            label: "水闸工情",
            url: "/stationSide?type=水闸工情",
            type: '水灾害防御',
            iconClass: 'icon-catalog-sluicestation'
        },],
    },
    {
        id: 13,
        label: "流量",
        disabled: false,
        url: "/stationSide?type=流量",
        type: '水灾害防御',
        iconClass: 'icon-catalog-flow'
    },
    {
        id: 14,
        label: "水质",
        disabled: true,
        children: [{
            id: 15,
            label: "国控断面(开发中)",
            disabled: true,
            url: "/stationSide",
            type: '水灾害防御',
            iconClass: 'icon-catalog-gkdm'
        }, {
            id: 16,
            label: "省控断面(开发中)",
            disabled: true,
            url: "/stationSide",
            type: '水灾害防御',
            iconClass: 'icon-catalog-skdm'
        }, {
            id: 17,
            label: "水文站水质",
            url: "/stationSide?type=水文站水质",
            type: '水灾害防御',
            iconClass: 'icon-catalog-hydrologic'
        },],
    },
    {
        id: 18,
        label: "积水",
        url: "/stationSide?type=积水",
        type: '水灾害防御',
        iconClass: 'icon-catalog-hydrops'
    },
    {
        id: 19,
        label: "视频",
        url: "/stationSide?type=视频",
        type: '水灾害防御',
        iconClass: 'icon-catalog-video'
    },
    ],
},
{
    id: 31,
    label: "应急救援",
    disabled: true,
    icon: "icon-tree-rescue",
    children: [{
        id: 32,
        label: "防汛仓库",
        type: '水灾害防御',
        url: "/rescueSide?type=防汛仓库",
        iconClass: 'icon-catalog-warehouse'
    },
    {
        id: 33,
        label: "救援队伍",
        type: '水灾害防御',
        url: "/rescueSide?type=救援队伍",
        iconClass: 'icon-catalog-team'
    },
    ],
},
{
    id: 34,
    label: "规划类型",
    disabled: true,
    icon: "icon-tree-rescue",
    children: [{
        id: 35,
        label: "发展规划",
        url: "/ProgrammeSide?type=发展规划",
        type: '水发展规划'
    }, {
        id: 36,
        label: "综合规划",
        url: "/ProgrammeSide?type=综合规划",
        type: '水发展规划'
    }, {
        id: 37,
        label: "专业规划",
        url: "/ProgrammeSide?type=专业规划",
        type: '水发展规划'
    }, {
        id: 38,
        label: "专项规划",
        url: "/ProgrammeSide?type=专项规划",
        type: '水发展规划'
    }],
}, {
    id: 39,
    label: "水域监管",
    disabled: true,
    icon: "icon-tree-rescue",
    children: [{
        id: 40,
        label: "清四乱",
        url: "/ClearFour",
        type: '河湖库保护'
    },{
        id: "RDTB",
        label: "扰动图斑",
        url: "",
        type: '河湖库保护'
    },{
        id: "YGYX",
        label: "遥感影像",
        url: "",
        type: '河湖库保护'
    }],
}, {
    id: 'SZYBZ',
    label: "水资源保障",
    disabled: true,
    icon: "icon-tree-rescue",
    children: [{
        id: 'sy',
        label: "水源",
        url: "/WaterResourSide?type=水源",
        type: '水资源保障'
    }, {
        id: 'sc',
        label: "水厂",
        url: "/WaterResourSide?type=水厂",
        type: '水资源保障'
    }, {
        id: 'scjsxm',
        label: "生产建设项目",
        url: "/ProductionProjSide?type=生产建设项目",
        type: '水资源保障'
    }],
},
{
    id: "QYS",
    label: "取用水管理",
    disabled: true,
    icon: "icon-tree-rescue",
    children: [{
        id: "QSH",
        label: "取水户",
        url: "/InformationOfLakes?type=取水户",
        type: '水资源保障'
    }, {
        id: "DX_JJ",
        label: "地下机井",
        url: "/InformationOfLakes?type=地下机井",
        type: '水资源保障'
    }, {
        id: "DX_SWZ",
        label: "地下水位站",
        url: "/InformationOfLakes?type=地下水位站",
        type: '水资源保障'
    }]
},
{
    id: "HH",
    label: "河湖信息",
    disabled: true,
    icon: "icon-tree-rescue",
    children: [{
        id: "HH_HL",
        label: "河道",
        url: "/InformationOfLakes?type=河道",
        type: '水资源保障'
    }, {
        id: "HH_HJ",
        label: "河景",
        url: "/InformationOfLakes?type=河景",
        type: '水资源保障'
    }, {
        id: "HH_QSK",
        label: "取水口",
        url: "/InformationOfLakes?type=取水口",
        type: '水资源保障'
    }, {
        id: "HH_YSK",
        label: "雨水口",
        url: "/InformationOfLakes?type=雨水口",
        type: '水资源保障'
    }, {
        id: "HH_PWK",
        label: "入河排污口",
        url: "/InformationOfLakes?type=入河排污口",
        type: '水资源保障'
    }]
},
{
    id: "SY",
    label: "水域数据",
    icon: "icon-tree-project",
    disabled: true,
    children: [{
        id: "AC",
        label: "人工水道图层",
        disabled: true,
        children: [{
            id: "AC_lsxd",
            label: "人工水道临水线点图层",
            index: "0",
            type: '水灾害防御',
        },
        {
            id: "AC_lsx",
            label: "人工水道临水线图层",
            index: "1",
            type: '水灾害防御',
        },
        {
            id: "AC_zxx",
            label: "人工水道中心线图层",
            index: "2",
            type: '水灾害防御',
        },
        {
            id: "AC_smx",
            label: "人工水道水面线图层",
            index: "3",
            type: '水灾害防御',
        },
        {
            id: "AC_glfwx",
            label: "人工水道管理范围线图层",
            index: "4",
            type: '水灾害防御',
        },
        {
            id: "AC_sym",
            label: "人工水道水域面图层",
            index: "5",
            type: '水灾害防御',
        },
        ],
    },
    {
        id: "HP",
        label: "山塘图层",
        disabled: true,
        children: [{
            id: "HP_sxd",
            label: "山塘临水线点图层",
            index: "0",
            type: '水灾害防御',
        },
        {
            id: "HP_lsx",
            label: "山塘临水线图层",
            index: "1",
            type: '水灾害防御',
        },
        {
            id: "HP_glfwx",
            label: "山塘管理范围线图层",
            index: "2",
            type: '水灾害防御',
        },
        {
            id: "HP_smx",
            label: "山塘水面线图层",
            index: "3",
            type: '水灾害防御',
        },
        {
            id: "HP_sym",
            label: "山塘水域面图层",
            index: "4",
            type: '水灾害防御',
        },
        ],
    },
    {
        id: "P",
        label: "水域工程图层",
        disabled: true,
        children: [{
            id: "P_lsb",
            label: "拦水坝（堰）图层",
            index: "0",
            type: '水灾害防御',
        },
        {
            id: "P_cz",
            label: "船闸图层",
            index: "1",
            type: '水灾害防御',
        },
        {
            id: "P_qtgc",
            label: "其他工程图层",
            index: "2",
            type: '水灾害防御',
        },
        {
            id: "P_bz",
            label: "泵站图层",
            index: "3",
            type: '水灾害防御',
        },
        {
            id: "P_sz",
            label: "水闸图层",
            index: "04",
            type: '水灾害防御',
        },
        {
            id: "P_df",
            label: "码头图层",
            index: "5",
            type: '水灾害防御',
        },
        {
            id: "P_ql",
            label: "桥梁图层",
            index: "6",
            type: '水灾害防御',
        },
        {
            id: "P_df",
            label: "堤防图层",
            index: "7",
            type: '水灾害防御',
        },
        ],
    },
    {
        id: "OW",
        label: "其他水域图层",
        disabled: true,
        children: [{
            id: "OW_lsxd",
            label: "其他水域临水线点图层",
            index: "0",
            type: '水灾害防御',
        },
        {
            id: "OW_lsx",
            label: "其他水域临水线图层",
            index: "1",
            type: '水灾害防御',
        },
        {
            id: "OW_glfwx",
            label: "其他水域管理范围线图层",
            index: "2",
            type: '水灾害防御',
        },
        {
            id: "OW_smx",
            label: "其他水域水面线图层",
            index: "3",
            type: '水灾害防御',
        },
        {
            id: "OW_syx",
            label: "其他水域水域面图层",
            index: "4",
            type: '水灾害防御',
        },
        ],
    },
    {
        id: "RS",
        label: "水库图层",
        disabled: true,
        children: [{
            id: "RS_lsxd",
            label: "水库临水线点图层",
            index: "0",
            type: '水灾害防御',
        },
        {
            id: "RS_lsx",
            label: "水库临水线图层",
            index: "1",
            type: '水灾害防御',
        },
        {
            id: "RS_glfwx",
            label: "水库管理范围线图层",
            index: "2",
            type: '水灾害防御',
        },
        {
            id: "RS_smx",
            label: "水库水面线图层",
            index: "3",
            type: '水灾害防御',
        },
        {
            id: "RS_sym",
            label: "水库水域面图层",
            index: "4",
            type: '水灾害防御',
        },
        ],
    },
    {
        id: "RV",
        label: "河道图层",
        disabled: true,
        children: [{
            id: "RV_lsxd",
            label: "河道临水线点图层",
            index: "0",
            type: '水灾害防御',
        },
        {
            id: "RV_lsx",
            label: "河道临水线图层",
            index: "1",
            type: '水灾害防御',
        },
        {
            id: "RV_smx",
            label: "河道水面线图层",
            index: "2",
            type: '水灾害防御',
        },
        {
            id: "RV_glfwx",
            label: "河道管理范围线图层",
            index: "3",
            type: '水灾害防御',
        },
        {
            id: "RV_zxx",
            label: "河道中心线图层",
            index: "4",
            type: '水灾害防御',
        },
        {
            id: "RV_ah",
            label: "暗河图层",
            index: "5",
            type: '水灾害防御',
        },
        {
            id: "RV_qtgq",
            label: "其他沟渠图层",
            index: "6",
            type: '水灾害防御',
        },
        {
            id: "RV_sym",
            label: "河道水域面图层",
            index: "7",
            type: '水灾害防御',
        },
        ],
    },
    {
        id: "SEC",
        label: "水域断面图层",
        disabled: true,
        children: [{
            id: "SEC_dmd",
            label: "水域断面点图层",
            index: "0",
            type: '水灾害防御',
        },
        {
            id: "SEC_dmx",
            label: "水域断面线图层",
            index: "1",
            type: '水灾害防御',
        },
        ],
    },
    ]
},
{
    id: 41,
    label: "台风分布",
    icon: "icon-tree-typhoon",
    url: "/typhoonSearch",
    type: '水灾害防御'
},
{
    id: 42,
    label: "气象预报(开发中)",
    icon: "icon-tree-weather",
    disabled: true,
    type: '水灾害防御'
},
{
    id: 43,
    label: "降雨分布(开发中)",
    icon: "icon-tree-realtime",
    disabled: true,
    type: '水灾害防御'
},
{
    id: 44,
    label: "降雨预报(开发中)",
    icon: "icon-tree-forecast",
    disabled: true,
    type: '水灾害防御'
},
{
    id: 45,
    label: "水域调查(开发中)",
    icon: "icon-tree-survey",
    disabled: true,
    type: '水灾害防御'
}, {
    id: 46,
    label: "风场预报",
    icon: "icon-tree-survey",
    type: '水灾害防御'
}, {
    id: 47,
    label: "浪场预报",
    icon: "icon-tree-survey",
    type: '水灾害防御'
}, {
    id: 48,
    label: "卫星云图",
    icon: "icon-tree-survey",
    type: '水灾害防御'
}, {
    id: 49,
    label: "雷达降雨",
    icon: "icon-tree-survey",
    type: '水灾害防御'
},
]