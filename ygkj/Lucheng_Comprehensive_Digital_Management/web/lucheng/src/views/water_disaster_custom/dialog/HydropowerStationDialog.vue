<!--
 * @Author: dyn
 * @Date: 2021-07-12 09:47:17
 * @LastEditTime: 2022-04-02 11:40:36
 * @LastEditors: dtb
 * @Description:  防汛风险 落点弹窗
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\HydropowerStationDialog.vue
-->
<template>
  <div class="detail-table">
    <el-dialog width="60%" destroy-on-close :visible.sync="visible" :before-close="handleClose" append-to-body :modal-append-to-body="true" :close-on-click-modal="false" class="primary-dialog">
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">{{ name }}</p>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <div class="content-panel">
          <div class="tab-panel">
            <div :class="['tab-item', activeTab == '基本信息' ? 'active' : '']" @click="changeActiveTab('基本信息')">
              基本信息
            </div>
          </div>
          <!-- 基本信息 -->
          <div class="tab-content-panel baseinfo-panel" v-if="activeTab == '基本信息'" v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
            <p class="title">{{ name }}</p>
            <el-row :gutter="20">
              <el-col :span="8" v-for="(item, i) in inforColumn" :key="i">
                <span class="label">{{ item.name }}</span>
                <span class="value">{{ baseInfo[item.prop] || "-" }}</span>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { waterResourceApi } from "@/api/waterResourcesGuarantee";
export default {
  name: "HydropowerStationDialog",
  data() {
    return {
      name: "",
      loading: false,
      hystCode: "",
      activeTab: "基本信息",
      baseInfo: {
        code: "123",
      },
      inforColumn: [],
    };
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    pointData: {
      type: Object,
      default: ''
    },
  },
  components: {  },
  created() { },
  mounted() {
    this.init(this.pointData)
  },
  computed: {},
  methods: {
    init(val) {
      this.name = val.name;
      this.hystCode = val.code;
      this.$nextTick(() => {
        this.hystPointDetail();
      });
    },
    // 水电站详情
    async hystPointDetail() {
      this.inforColumn = [
        { name: "水电站编码", prop: "hystCode" },
        { name: "水电站类型", prop: "hystType" },
        { name: "水库编码", prop: "reservoirCode" },
        { name: "配套水库名称", prop: "sresName" },
        { name: "水库经度", prop: "hystLong" },
        { name: "水库纬度", prop: "hystLat" },
        { name: "水库所在位置", prop: "hystLoc" },
        { name: "行政区域", prop: "adcd" },
        { name: "行政区域代码", prop: "areaCode" },
        { name: "所属流域", prop: "bas" },
        { name: "本流域集雨面积(k㎡)", prop: "localWaterCollectionArea" },
        { name: "是否水库发电", prop: "isUseResvoirWater" },
        { name: "机组台数(台)", prop: "generatorsCount" },
        { name: "额定水头(m)", prop: "ratHead" },
        { name: "总装机容量(m³)", prop: "totInsCap" },
        { name: "装机流量((m³/s)", prop: "insCap" },
        { name: "保证出力", prop: "firmPow" },
        { name: "水轮机主要型式", prop: "hydraulicTurbineMainType" },
        { name: "机组主要布置方式", prop: "generatorsArrangementMainWay" },
        { name: "完工时间", prop: "completeTime" },
        { name: "工程等别", prop: "engGrad" },
        { name: "工程规模", prop: "engScal" },
        { name: "主要建筑物级别", prop: "mainBuildGrad" },
        { name: "工程建筑情况", prop: "engStat" },
        // { name: "开工时间", prop: "startDate" },
        // { name: "竣工验收时间", prop: "compDate" },
        // { name: "大坝类型", prop: "damTypeByMaterials" },
        // { name: "最高坝高", prop: "damMaxHeight" },
        // { name: "设计标准(年)", prop: "designStandard" },
        // { name: "政府负责人", prop: "govermentChargeId" },
        // { name: "水行政部门负责人", prop: "waterDepartmentChargeId" },
        // { name: "主管部门负责人", prop: "competentDepartmentChargeId" },
        // { name: "技术负责人", prop: "technologyChargeId" },
        // { name: "巡查负责人", prop: "patrolChargeId" },
        // { name: "归口管理部门", prop: "amdDep" },
        // { name: "记录生效时间", prop: "effDate" },
        // { name: "记录失效时间", prop: "exprDate" },
        // { name: "备注", prop: "note" },
        // {
        //   name: "水库安全技术认定日期",
        //   prop: "reservoirSafetyIdentificationDate",
        // },
        // {
        //   name: "水库安全技术认定结论",
        //   prop: "reservoirSafetyIdentificationConclusion",
        // },
      ];
      this.loading = true;
      await waterResourceApi
        .hystPointDetail({ hystCode: this.hystCode })
        .then((res) => {
          this.baseInfo = res.data;
          this.loading = false;
        });
    },
    // tab 切换
    changeActiveTab(val) {
      this.activeTab = val;
      switch (val) {
        case "基本信息":
          this.hystPointDetail();
          break;
      }
    },
    handleClose() {
      this.$emit("closeDialog");
    },
  },
};
</script>
<style lang="scss" scoped>
.primary-dialog {
  .content-panel {
    height: 500px;
    color: #fff;
  }
}

.tab-panel {
  display: flex;
  margin-bottom: 20px;
}
.tab-item {
  padding: 8px 10px;
  margin-right: 20px;
  color: #fff;
  border: 1px solid rgba(255, 255, 255, 0.6);
  cursor: pointer;
  &.active {
    color: #56fefe;
    background: rgba(72, 152, 251, 0.1);
    border: 1px solid #56fefe;
    box-shadow: inset 0 0 5px 0 #56fefe;
  }
}

.tab-content-panel {
  height: calc(100% - 52px);
}

.baseinfo-panel {
  .title {
    font-size: 20px;
    margin-bottom: 10px;
  }
  .el-row {
    height: calc(100% - 30px);
    overflow-y: scroll;
  }
  .el-col {
    line-height: 40px;
    font-size: 16px;
    .value {
      font-size: 18px;
      color: #33ffff;
      margin-left: 20px;
    }
  }
}

.line-panel {
  display: flex;
  .left-panel {
    width: 60%;
    margin-right: 20px;
    .top-panel {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .title {
        font-size: 18px;
        i {
          vertical-align: middle;
          margin-right: 10px;
        }
      }
      .time {
        ::v-deep.el-date-editor {
          width: 140px;
        }
        ::v-deep.el-input__inner {
          padding-left: 1.875rem;
          background: none;
          border: 1px solid rgba(255, 255, 255, 0.5);
          height: 32px;
        }
        ::v-deep.el-input__icon {
          line-height: 1;
        }
      }
    }
    .chart-panel {
      height: calc(100% - 30px);
    }
  }
  .right-panel {
    flex: 1;
  }
}

.video-panel {
  display: flex;
  justify-content: space-between;
  .left-panel {
    width: 30%;
    margin-right: 20px;
    overflow-y: scroll;
    .camera-item {
      color: #fff;
      cursor: pointer;
      margin: 8px 0;
      margin-right: 0.375rem;
      border: 1px #285c9e solid;
      padding: 6px;
      &:hover,
      &.active {
        border: 1px solid #168aa7;
        box-shadow: 0 0 20px #168aa746 inset;
      }
    }
  }
  .right-panel {
    flex: 1;
    margin-bottom: 20px;
  }
}

.abc ::v-deep .el-table__body tr:hover {
  background-color: rgba($color: #56fefe, $alpha: 0.4);
}
::v-deep .el-dialog {
  .el-dialog__header {
    padding: 2px;
    .primary-dialog__title {
      .primary-dialog__title--label {
        width: 80%;
        height: 32px;
        color: #fff;
        font-size: 16px;
        letter-spacing: 0;
        margin-left: 15px;
        font-weight: bold;
        line-height: 35px;
        padding-left: 56px;
      }
    }
  }
  .el-dialog__body {
    // height: 300px;
    padding: 2px;
    padding-top: 0;
    border-bottom: 0.0625rem solid #00c1ff;
    position: relative;
    .primary-dialog__content {
      height: 100%;
      padding: 20px;
    }
  }
}
</style>