package com.ygkj.lcsyn.enums;

import lombok.Getter;

@Getter
public enum ProvinceDataEnum {

    TOKEN("https://sgpt.zjwater.com/service/v1/token/getAccessToken", "获取token"),

    ST_BASE("https://sgpt.zjwater.com/service/v2/api/provider/query/bfefb6d0", "测站基础信息表"),

    SEAWALL_LIST("https://sgpt.zjwater.com/service/v2/api/provider/query/117afd2f", "海塘名录表"),

    PUMP_BASE("https://sgpt.zjwater.com/service/v2/api/provider/query/5e468412", "泵站基础信息"),

    PUMP_LIST("https://sgpt.zjwater.com/service/v2/api/provider/query/fddad060", "泵站名录表"),

    SLUICE_PROJECT_PHOTO("https://sgpt.zjwater.com/service/v2/api/provider/query/2267cea9", "水闸-工程照片"),

    SEAWALL_BASE("https://sgpt.zjwater.com/service/v2/api/provider/query/6a585dca", "海塘基础信息"),

    SEAWALL_PROJECT_PHOTO("https://sgpt.zjwater.com/service/v2/api/provider/query/2267cf83", "海塘-工程照片"),

    PUMP_PROJECT_PHOTO("https://sgpt.zjwater.com/service/v2/api/provider/query/2267cb92", "泵站-工程照片"),

    SLUICE_BASE("https://sgpt.zjwater.com/service/v2/api/provider/query/7c885435", "水闸基础信息"),

    SLUICE_LIST("https://sgpt.zjwater.com/service/v2/api/provider/query/30ca56bf", "水闸名录表"),

    TYPHOON_HISTORY("https://sgpt.zjwater.com/service/v2/api/provider/query/395fb4f2", "历史台风信息"),

//    STATION_MAX_HOUR_RAIN ( "https://sgpt.zjwater.com/service/v2/api/133/BiggestHourRain","单站最大小时雨量"),

//    DAY_MIX_WATER ( "https://sgpt.zjwater.com/service/v2/api/133/singleDayWater","单天融合水位(整点数据)"),

//    TODAY_FLOOD_WATER ( "https://sgpt.zjwater.com/service/v2/api/133/newestWarnedWater","当天最新水情超限数据"),

//    DAYS_MIX_WATER ( "https://sgpt.zjwater.com/service/v2/api/133/MultiDayRain","多天融合雨量"),

//    WATER_FORECAST_NOTE ( "https://sgpt.zjwater.com/service/v2/api/133/forecastc","水情预报成果注释"),

//    STATION_RAIN_COUNT ( "https://sgpt.zjwater.com/service/v2/api/133/TotalRainList","单站累计雨量数量统计"),

//    DAYS_MIX_WATER_HOUR ( "https://sgpt.zjwater.com/service/v2/api/133/multiDayWater","多天融合水位(整点数据)"),

//    ST_BASE2 ( "https://sgpt.zjwater.com/service/v2/api/133/baseData","测站基本信息"),

//    WATER_FORECAST_RESULT ( "https://sgpt.zjwater.com/service/v2/api/133/forecast","水情预报成果"),

//    DAY_MIX_RAIN ( "https://sgpt.zjwater.com/service/v2/api/133/OneDayRain","单天融合雨量"),

    RW_STATION_DETAIL("https://sgpt.zjwater.com/service/v2/api/102/station/getStation", "查询水雨情测站详情信息"),

    BATCH_REALTIME_WATER("https://sgpt.zjwater.com/service/v2/api/102/water/listBatchStationWater", "批量查询测站实时水位信息"),

    //查询行政区划异常
//    FORECAST_RAIN_GRID ( "https://sgpt.zjwater.com/service/v2/api/12850/rain/listForecastRainGridData","预报降雨网格数据"),

    REALTIME_WATER("https://sgpt.zjwater.com/service/v2/api/102/water/listStationWater", "查询实时水位信息"),

    BATCH_REALTIME_RAIN("https://sgpt.zjwater.com/service/v2/api/102/rain/listBatchStationRain", "批量查询测站实时雨量信息"),

    ST_PAGE("https://sgpt.zjwater.com/service/v2/api/102/station/listStations", "分页查询测站列表信息"),

    PROJECT_BY_CODE("https://sgpt.zjwater.com/service/v2/api/102/project/unitequery", "根据工程编码获取工程信息"),

    REALTIME_RW("https://sgpt.zjwater.com/service/v2/api/102/rain/listStationRain", "查询实时雨情信息"),

//    WARNING ( "https://sgpt.zjwater.com/service/v2/api/133/WarnPubLog","预警信息"),

//    WARNING_STATION_REL ( "https://sgpt.zjwater.com/service/v2/api/133/UnitToStcd","预警单位站点关联"),

//    BASIN_AREA_RAIN_HIS ( "https://sgpt.zjwater.com/service/v2/api/133/lyRealTimeData","流域定制面面雨量历史数据"),

//    BASIN_AREA_BASE ( "https://sgpt.zjwater.com/service/v2/api/133/lyBaseData","流域定制面基础信息"),

    //No instances available for water-manage-flood
//    WARNING_RISK ( "https://sgpt.zjwater.com/service/v2/api/102/warnpublish/list","查询预警风险发布"),

    WARNING_RESPOND_DEPT("https://sgpt.zjwater.com/service/v2/api/provider/query/5e66670b", "预警响应部门信息表"),

    FOCUS_DEFENSE("https://sgpt.zjwater.com/service/v2/api/provider/query/d5a94fd7", "重点防御对象信息表"),

    WARNING_PEOPLE("https://sgpt.zjwater.com/service/v2/api/provider/query/5e6668bf", "预警人员表"),

    //No instances available for water-manage-flood
//    WARNING_STATION ( "https://sgpt.zjwater.com/service/v2/api/102/warnstation/list","查询测站预警消息"),
    //No instances available for water-manage-flood
//    WARNING_AREA ( "https://sgpt.zjwater.com/service/v2/api/102/warnad/list","查询区划预警消息"),

    //No instances available for water-manage-flood
//    WARNING_PEOPLE2 ( "https://sgpt.zjwater.com/service/v2/api/102/warnperson/list","查询预警人员信息"),

    //nginx 404
//    WARNING_VILLAGE("https://sgpt.zjwater.com/service/v2/api/135/floodWarningPic/listWarning", "获取预警村落地图点位列表"),

//    WARNING_UNIT ( "https://sgpt.zjwater.com/service/v2/api/133/UnitInfo","预警单位"),

//    WARNING_INDICATORS ( "https://sgpt.zjwater.com/service/v2/api/133/WarnStdIndex","预警指标"),

    //No instances available for water-manage-flood
//    WARNING_AREA_STATISTICS("https://sgpt.zjwater.com/service/v2/api/102/warnad/count", "区划预警消息统计"),

    //nginx 404
//    WARNING_VILLAGE_DETAIL("https://sgpt.zjwater.com/service/v2/api/135/floodWarningPic/getPrevDetail", "获取预警村落地图点位详情"),

    DEFENSE_LIST("https://sgpt.zjwater.com/service/v2/api/provider/query/d5a95295", "防御对象名录表"),

    WARNING_STATION_RECORD("https://sgpt.zjwater.com/service/v2/api/provider/query/5fd988a3", "测站预警记录表"),

    FLOOD_WARNING_INFO("https://sgpt.zjwater.com/service/v2/api/provider/query/2dd5e30b", "山洪预警责任人信息表"),
    FLOOD_WARNING_REF("https://sgpt.zjwater.com/service/v2/api/provider/query/2dd5e5d4", "山洪预警责任人名录表"),
    FLOOD_WARNING("https://sgpt.zjwater.com/service/v2/api/provider/query/0dc19255", "山洪预警"),

    AFFECT_POPULATION("https://sgpt.zjwater.com/service/v2/api/provider/query/e671431b", "山洪易发区影响人口信息表"),
    PREV_OBJ_INFO("https://sgpt.zjwater.com/service/v2/api/provider/query/47f4c820", "防治对象信息表"),
    PREV_OBJ_BASE("https://sgpt.zjwater.com/service/v2/api/provider/query/47f4cd69", "防治对象名录表"),

    ORG_SCOPE("https://sgpt.zjwater.com/service/v2/api/102/auth/getOrgScope","获取应用通讯录权限范围"),
    DEPT_INFO("https://sgpt.zjwater.com/service/v2/api/102/dept/getDeptInfo","获取部门详情"),
    DEPT_LIST("https://sgpt.zjwater.com/service/v2/api/102/dept/listSubDeptIds","获取直属下级部门ID"),
    DEPT_USER("https://sgpt.zjwater.com/service/v2/api/102/user/listDeptUser","根据部门获取用户"),
    USER_INFO("https://sgpt.zjwater.com/service/v2/api/102/user/getUserInfo","获取用户详情"),
    CHANGE_USER("https://sgpt.zjwater.com/service/v2/api/102/user/listUserInfoByTime","用户失效"),
    CHANGE_DEPT("https://sgpt.zjwater.com/service/v2/api/102/dept/listDeptInfoByTime","部门失效"),

    MOUNTAIN_POND_WARN_RESPONSE("https://sgpt.zjwater.com/service/v2/api/provider/query/9e99f6a3","山塘预警责任人信息表"),

    SUB_ADCD("https://sgpt.zjwater.com/service/v2/api/102/adcd/listSubAdcd","获取行政区划下的镇街道"),

    WARNING_AREA_RECORD("https://sgpt.zjwater.com/service/v2/api/provider/query/5e666485", "区划预警记录表");


    private String url;
    private String describe;

    ProvinceDataEnum(String url, String describe) {
        this.url = url;
        this.describe = describe;
    }
}
