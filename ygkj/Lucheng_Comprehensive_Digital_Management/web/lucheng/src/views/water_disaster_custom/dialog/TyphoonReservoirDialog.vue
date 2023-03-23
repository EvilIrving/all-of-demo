<!--
 * @Date: 2022-01-07 10:55:50
 * @Author: dtb
 * @Description: 台风水库 落点弹窗
 * @LastEditors: dtb
 * @LastEditTime: 2022-01-07 11:28:45
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\TyphoonReservoirDialog.vue
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
import { getSeawallProjects } from '@/api/seawall.js';
export default {
  name: "MountainPondPointDialog",
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
    this.Init(this.pointData)
  },
  computed: {},
  methods: {
    Init(val) {
      this.name = val.name;
      this.hystCode = val.code;
      this.$nextTick(() => {
        this.getDetail();
      });
    },
    async getDetail() {
      this.inforColumn = [
        { name: '水库名称', prop: 'resName' },
        { name: '左下角经度', prop: 'lowLeftLong' },
        { name: '左下角维度', prop: 'lowLeftLat' },
        { name: '右上角经度', prop: 'upLeftLon' },
        { name: '右上角维度', prop: 'upRightLat' },
        { name: '水库所在位置', prop: 'resLoc' },
        { name: '水库类型', prop: 'resType' },
        { name: '工程等别', prop: 'engGrad' },
        { name: '工程规模', prop: 'engScal' },
        { name: '坝址控制流域面积', prop: 'watShedArea' },
        { name: '坝址多年平均径流量', prop: 'daadMulAverRuof' },
        { name: '防洪高水位', prop: 'uppLevFlco' },
        { name: '正常蓄水位', prop: 'normWatLev' },
        { name: '正常蓄水位相应水面面积', prop: 'normPoolStagArea' },
        { name: '正常蓄水位相应库容', prop: 'normPoolStagCap' },
        { name: '主汛期防洪限制水位', prop: 'flLowLimLev' },
        { name: '防汛限制水位库容', prop: 'flLowLimLevCap' },
        { name: '死水位', prop: 'deadLev' },
        { name: '总库容', prop: 'totCap' },
        { name: '兴利库容', prop: 'benResCap' },
        { name: '河道安全过流能力', prop: 'risaCap' },
        { name: '工程建设情况', prop: 'engStat' },
        { name: '开工时间', prop: 'startDate' },
        { name: '竣工验收时间', prop: 'scompDate' },
        { name: '归口管理部门', prop: 'admDep' },
        { name: '主要挡水建筑物类型', prop: 'mainWrType' },
        { name: '主要挡水坝类型', prop: 'damType' },
        { name: '主要泄洪建筑物形式', prop: 'mainFlType' },
        { name: '最大泄洪流量', prop: 'maxDisFlow' },
        { name: '主要功能', prop: 'function' },
        { name: '完工时间（非加固）', prop: 'finishTime' }      
      ];
      this.loading = true;
      let res = await getSeawallProjects({
        code: this.code,
        withWaterLevel: false,
        type: '水库'
      });
      if (res.code == 0) {
        this.baseInfo = res.data[0];
        this.loading = false;
      }
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