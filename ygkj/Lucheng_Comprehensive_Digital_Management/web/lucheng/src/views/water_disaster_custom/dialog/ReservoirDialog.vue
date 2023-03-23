<!--
 * @Author: dyn
 * @Date: 2021-07-12 09:47:17
 * @LastEditTime: 2022-05-31 14:31:22
 * @LastEditors: Please set LastEditors
 * @Description: 水库蓄水量落点弹窗
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\ReservoirDialog.vue
-->
<template>
  <div class="detail-table emergency-scheme">
    <el-dialog
      top="3vh"
      destroy-on-close
      :visible.sync="visible"
      :before-close="handleClose"
      append-to-body
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      class="primary-dialog emergency-scheme-dialog"
    >
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">{{ pointData.name }}</p>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <div v-if="pointData.type != '附属工程'">
          <div class="danger-tap">
            <div
              @click="checkTab(index)"
              :class="{ active: index == tapOptions }"
              v-for="(item, index) in tapList"
              :key="item"
            >
              {{ item }}
            </div>
          </div>
          <div v-if="resCode" class="dialog__content">
            <reservoirDetail :code="resCode" v-if="tapOptions == 0" />
            <reservoirPerson :code="resCode" v-if="tapOptions == 1" />
            <reservoirFacility :code="resCode" v-if="tapOptions == 2" />
          </div>
        </div>
        <subsidiaryFacilities v-else :detail="pointData" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { waterResourceApi } from '@/api/waterResourcesGuarantee';
import { generateDataZoom } from '../cards/utils/chart.js';
import reservoirDetail from './components/ReservoirDialog/reservoirDetail.vue';
import reservoirPerson from './components/ReservoirDialog/reservoirPerson.vue';
import reservoirFacility from './components/ReservoirDialog/reservoirFacility.vue';
import subsidiaryFacilities from './components/subsidiaryFacilities.vue';
export default {
  name: 'ReservoirDialog',
  data() {
    return {
      tapList: ['详情', '人员', '设备'],
      tapOptions: 0,
      name: '',
      resCode: '',
      barData1: {
        columns: ['year', 'endYearStore'],
        rows: []
      },
      barOption: {
        grid: {
          top: 40,
          bottom: '5%',
          containLabel: true
        },
        setting: {
          yAxisName: ['亿m³'],
          itemStyle: { color: '#fff' },
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold',
            position: 'top',
            color: '#fff'
          }
        },
        extend: {
          'textStyle.color': '#fff',
          barWidth: 15,
          xAxis: {
            axisLabel: {
              interval: 0,
              fontSize: '16'
            }
          },
          yAxis: {
            nameTextStyle: {
              color: '#ffffff99',
              fontSize: '16'
            },
            axisLine: {
              show: false
            },
            axisLabel: {
              color: '#fff',
              fontSize: '16'
            },
            splitLine: {
              lineStyle: {
                color: '#fff',
                opacity: 0.5
              }
            },
            max: (value) => {
              return value.max > 1 ? null : 1;
            }
          }
        }
      }
    };
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    pointData: {
      type: Object,
      default: {}
    }
  },
  components: { reservoirDetail, reservoirPerson, reservoirFacility, subsidiaryFacilities },
  mounted() {
    console.log(this.pointData, 'this.pointData');
    this.resCode = this.pointData.code || '';
  },
  computed: {},
  methods: {
    checkTab(index) {
      this.tapOptions = index;
    },
    handleClose() {
      this.$emit('closeDialog');
    }
  }
};
</script>
<style lang="scss" scoped>
.emergency-scheme-dialog.primary-dialog {
  .top {
    margin-bottom: 25px;
  }
  p.comTit {
    font-family: PingFangSC-Semibold;
    font-size: 1.125rem;
    color: #ffffff;
    letter-spacing: 0;
    line-height: 2rem;
    font-weight: 600;
    i {
      vertical-align: middle;
      margin-top: -2px;
    }
  }
  p.little {
    font-family: PingFangSC-Semibold;
    font-size: 0.875rem;
    color: #ffffff;
    letter-spacing: 0;
    line-height: 1.5rem;
    font-weight: 400;
    i {
      vertical-align: middle;
      margin-top: -2px;
    }
  }
  .module-title {
    color: #1ef8f2;
    padding-left: 4px;
    line-height: 40px;
  }
  .title {
    font-family: PingFangSC-Semibold;
    font-size: 18px;
    color: #ffffff;
    letter-spacing: 0;
    line-height: 32px;
    font-weight: 600;
  }
  .t-title {
    font-family: PingFangSC-Semibold;
    font-size: 18px;
    color: #33ffff;
    letter-spacing: 0;
    line-height: 24px;
    font-weight: 600;
    margin: 10px 0;
  }
  ::v-deep .el-radio__input.is-checked .el-radio__inner {
    border-color: #1ef8f2;
    background-color: #1ef8f2;
  }
  ::v-deep .el-dialog {
    background: url(~assets/images/WaterDisastersImage/dialog-bg.png) no-repeat;
    background-size: 100% 100%;
    width: 1296px;
    height: 860px;
    .el-dialog__headerbtn {
      top: 39px;
      right: 30px;
      font-size: 18px;
      .el-dialog__close {
        color: #fff;
      }
    }
    .el-dialog__header {
      padding-top: 28px;
      padding-bottom: 16px;
      .primary-dialog__title {
        background: url(~assets/images/WaterDisastersImage/dialog-title-bg.png) no-repeat;
        background-size: 100% 100%;
        height: 42px;
        padding: 0;
        .primary-dialog__title--label {
          width: 80%;
          height: 32px;
          color: #fff;
          font-size: 16px;
          letter-spacing: 0;
          line-height: 35px;
          padding-left: 36px;
        }
      }
    }
    .el-dialog__body {
      height: 500px;
      padding: 2px;
      padding-top: 0;
      position: relative;

      .primary-dialog__content {
        padding: 0 16px;
        height: 100%;
        > div {
          height: 100%;
          .dialog__content {
            height: calc(100% - 50px);
          }
        }
      }
    }
  }
}
.danger-tap {
  display: flex;
  > div {
    width: 114px;
    height: 32px;
    border: 1px solid rgba(255, 255, 255, 0.6);
    line-height: 32px;
    text-align: center;
    margin-right: 16px;
    color: rgba(255, 255, 255, 0.8);
    cursor: pointer;
    &.active {
      color: #00baff;
      background: rgba(72, 152, 251, 0.1);
      box-shadow: 0px 0px 5px 0px #00baff inset;
      border-color: rgba(0, 186, 255, 0.6);
    }
  }
}
</style>