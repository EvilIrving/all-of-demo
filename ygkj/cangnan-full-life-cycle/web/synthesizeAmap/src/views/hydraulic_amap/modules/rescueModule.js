import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
//应急救援相关操作module
export const rescueModule = {
    //获取工程点位
    // async getProjectPoint(type) {
    //     let opt = {
    //         type,
    //     };
    //     let res = await hydraulicApi.getProjects(opt);
    //     let list = res.data;
    //     this.showProjectPoint(list, type);
    // },
    //展示工程点位
    // showProjectPoint(list, type) {
    //     this.MapComponent.showProjectPoint(list, type);
    // },
    //仓库/队伍弹框
    showRescueDialog(data, feature) {
        this.dialogTitle = data.name;
        this.rescueDialogInfo = data;
        this.rescueDialogInfo.name = data.name;
        this.rescueDialogInfo.code = data.resCode || data.mnpdCode || data.seawallCode || data.dikeCode || data.wagaCode || data.hystCode || data.pustCode || data.irrCode || data.cwsCode || data.gateCode;
        this.rescueDialogInfo.type = feature.type;
        //周边分析统一经纬度字段
        this.rescueDialogInfo.latitude = feature.lat;
        this.rescueDialogInfo.longitude = feature.lng;
        this.rescueDialogShow = true;
    },
    //关闭工程弹框
    handleRescueClose() {
        this.rescueDialogShow = false;
        this.rescueDialogInfo = {};
    },
}
