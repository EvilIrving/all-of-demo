<!--
 * @Date: 2022-01-07 10:55:50
 * @Author: dtb
 * @Description:台风水闸 落点弹窗
 * @LastEditors: dtb
 * @LastEditTime: 2022-01-07 11:22:13
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\TyphoonSluiceDialog.vue
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
  components: {},
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
        { name: '水闸名称', prop: 'wagaName' },
        { name: '水闸所在位置', prop: 'wagaLoc' },
        { name: '水闸类型', prop: 'wagaType' },
        { name: '水闸用途', prop: 'wagaUse' },
        { name: '工程规模', prop: 'engScal' },
        { name: '主要建筑物级别', prop: 'mainBuildGrad' },
        { name: '闸口数量', prop: 'gaorNum' },
        { name: '设计最大过闸流量(m³/s)', prop: 'desLockDisc' },
        { name: '闸口总净宽', prop: 'gaorTotNetWid' },
        { name: '工程建设情况', prop: 'engStat' },
        { name: '开工时间', prop: 'startDate' },
        { name: '竣工验收时间', prop: 'compDate' },
        { name: '归口管理部门', prop: 'amdDep' },
        { name: '建成时间', prop: 'builtdate' },
        { name: '闸门宽度(m)', prop: 'gatewidth' },
        { name: '闸底高度(m)', prop: 'gatebottomelevation' },
        { name: '是否竣工验收', prop: 'isComplete' },
        { name: '是否为闸站工程', prop: 'isGateStationProject' },
        { name: '是否为套闸工程', prop: 'isSetBrakeEngineering' },
        { name: '闸上是否能通车', prop: 'isTraffic' },
        { name: '水闸参照水位站名称', prop: 'referenceWlStationName' },
        { name: '水闸参照水位站站号', prop: 'referenceWlStationCode' },
        { name: '水闸参照水位站警戒水位', prop: 'referenceWlStationWarningLevel' },
        { name: '水闸参照水位站危急水位', prop: 'referenceWlStationCriticalLevel' },
        { name: '启闭机类型', prop: 'hoistType' },
        { name: '效能方式', prop: 'energyDissipationType' },
        { name: '节制闸过闸流量(m³/s)', prop: 'controlGateFlow' },
        { name: '设计闸上水位(m)', prop: 'designSluiceUpstreamLevel' },
        { name: '是否五水共治任务', prop: 'isWsgz' },
        { name: '所在河道等级', prop: 'riverLevel' },
        { name: '所在河流岸别', prop: 'bankSide' },
        { name: '是否有备用电源', prop: 'hasEmergencyPower' },
        { name: '是否船闸工程', prop: 'isShipLock' },
        { name: '是否五水共治任务', prop: 'isWsgz' },
        { name: '是否加固', prop: 'reinforce' },
      ];
      this.loading = true;
      let res = await getSeawallProjects({
        code: this.code,
        withWaterLevel: false,
        type: '水闸'
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