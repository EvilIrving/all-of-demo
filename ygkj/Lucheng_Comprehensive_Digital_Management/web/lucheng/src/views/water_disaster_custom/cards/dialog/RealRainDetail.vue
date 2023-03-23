<template>
  <div class="primary-dialog sluice-detail-table">
    <el-dialog
      top="10vh"
      width="1200px"
      destroy-on-close
      :visible.sync="visible"
      :before-close="handleClose"
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      class="sluice-detail-table list-dialog"
      append-to-body
      :fullscreen="fullscreen"
    >
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">实时降雨</p>
        <!-- <i @click="changeFullscreen" :class="['el-icon-full-screen', 'full-screen-icon']"></i> -->
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <!-- <div class="query-sty">
          <el-row>
            <el-col :span="5">
              <el-select v-model="typeValue" size="small" @input="changeType">
                <el-option label="实时降雨" value="实时降雨" />
                <el-option label="距平分析" value="距平分析" />
              </el-select>
            </el-col>
            <el-col :span="5" class="aa" v-show="typeValue == '实时降雨'">
              <div class="demo-input-suffix">
                <p style="width: 50px">类型:</p>
                <el-select v-model="realTimeType" size="small" @blur="changeVal" placeholder="全部" @change="selectVal">
                  <el-option v-for="item in realTimeStration" :key="item.name" :label="item.name" :value="item.name" />
                </el-select>
              </div>
            </el-col>
            <el-col v-show="typeValue == '实时降雨' && realTimeType == '点雨量'" :span="5" class="aa">
              <div class="demo-input-suffix">
                <p style="width: 90px">统计类型:</p>
                <el-select v-model="surfaceType" size="small" @blur="changeVal" @change="selectVal" placeholder="全部">
                  <el-option
                    v-for="item in surfaceTypeStration"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col
              :style="{ marginTop: realTimeType == '点雨量' ? '' : '' }"
              v-show="typeValue == '实时降雨' && realTimeType == '点雨量' && surfaceType == '流域'"
              :span="5"
              class="aa"
            >
              <div class="demo-input-suffix">
                <p style="width: 50px">流域：</p>
                <el-select
                  v-model="drainageType"
                  size="small"
                  clearable
                  @blur="changeVal"
                  placeholder="全部"
                  @change="selectVal"
                >
                  <el-option v-for="item in administrative" :key="item.name" :label="item.name" :value="item.name" />
                </el-select>
              </div>
            </el-col>
            <el-col
              :style="{ marginTop: realTimeType == '点雨量' ? '' : '' }"
              v-show="(typeValue == '实时降雨' && realTimeType == '面雨量') || surfaceType == '行政区划'"
              :span="5"
              class="aa"
            >
              <div class="demo-input-suffix">
                <p style="width: 100px">行政区划：</p>
                <el-select
                  v-model="administrativeType"
                  size="small"
                  clearable
                  @blur="changeVal"
                  placeholder="全部"
                  @change="selectVal"
                >
                  <el-option v-for="item in administration" :key="item.adcd" :label="item.adnm" :value="item.adcd" />
                </el-select>
              </div>
            </el-col>
            <el-col
              v-show="typeValue == '实时降雨' && realTimeType == '点雨量'"
              :span="5"
              class="aa"
              style="margin-right: 10px"
            >
              <div class="demo-input-suffix" style="height: 35px">
                <el-radio
                  v-model="radio"
                  class="el-radio"
                  :label="true"
                  style="color: #fff"
                  @change="changeVal"
                  @input="selectVal"
                  >参与计算雨量站</el-radio
                >
                <el-radio
                  v-model="radio"
                  class="el-radio"
                  :label="false"
                  style="color: #fff"
                  @change="changeVal"
                  @input="selectVal"
                  >全部雨量站</el-radio
                >
              </div>
            </el-col>
            <el-col :span="18" v-show="typeValue == '实时降雨'">
              <div class="demo-input-suffix">
                <p style="width: 90px; line-height: 32px">降雨历时:</p>
                <el-checkbox-group v-model="durationRainfall" @change="selectVal">
                  <el-checkbox v-for="item in durationRainfallList" :key="item.name" :label="item.value">{{
                    item.name
                  }}</el-checkbox>
                </el-checkbox-group>
              </div>
            </el-col>
            <el-col :span="6">
              <DataDetailButton @click="exportExcel()">导 出</DataDetailButton>
            </el-col>
          </el-row>
        </div> -->
        <div class="tab-list-sty">
          <el-table
            ref="multipleTable"
            :data="realTimeData"
            height="530px"
            stripe
            class="abc"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            :default-sort="{ prop: 'date', order: 'descending' }"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <template v-if="typeValue == '实时降雨'">
              <el-table-column :show-overflow-tooltip="true" sortable prop="name" label="名称" />
              <el-table-column
                :show-overflow-tooltip="true"
                :formatter="Formatter"
                sortable
                prop="newest1hour"
                label="近1小时(mm)"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                :formatter="Formatter"
                sortable
                prop="1hour"
                label="前1小时(mm)"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                :formatter="Formatter"
                sortable
                prop="3hour"
                label="前3小时(mm)"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                :formatter="Formatter"
                sortable
                prop="6hour"
                label="前6小时(mm)"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                :formatter="Formatter"
                sortable
                prop="24hour"
                label="前24小时(mm)"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                :formatter="Formatter"
                sortable
                prop="72hour"
                label="前72小时(mm)"
              />
            </template>
            <template v-if="typeValue == '距平分析'">
              <el-table-column :show-overflow-tooltip="true" sortable prop="name" label="行政区划" />
              <el-table-column :show-overflow-tooltip="true" sortable prop="name" label="月份" />
              <el-table-column :show-overflow-tooltip="true" sortable prop="name" label="降雨量" />
              <el-table-column :show-overflow-tooltip="true" sortable prop="name" label="多年平均降雨量" />
              <el-table-column :show-overflow-tooltip="true" sortable prop="name" label="距平百分率" />
            </template>
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { disasterPreventionApi } from '@/api/RightSildePanel';
import { DataDetailButton } from '../../dialog/DetailsCommonDialog/components';

import { hydraulicApi } from '@/api/hydraulic_amap';
export default {
  name: 'RealRainDetail',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      //行政区划
      administration: [],
      realTimeData: [],
      realTimeStration: [
        {
          name: '面雨量'
        },
        {
          name: '点雨量'
        }
      ],
      realTimeType: '点雨量',
      surfaceType: '',
      surfaceTypeStration: [
        {
          name: '行政区划'
        },
        {
          name: '流域'
        }
      ],
      drainageType: '',
      //水库流域
      administrative: [
        {
          name: '飞云江'
        },
        {
          name: '瓯江'
        },
        {
          name: '鳌江'
        }
      ],
      administrativeType: '',
      radio: true,
      durationRainfall: [1, 3, 6, 24, 72],
      durationRainfallList: [
        {
          value: 1,
          name: '显示前1小时'
        },
        {
          value: 3,
          name: '显示前3小时'
        },
        {
          value: 6,
          name: '显示前6小时'
        },
        {
          value: 24,
          name: '显示前24小时'
        },
        {
          value: 72,
          name: '显示前72小时'
        }
      ],
      typeValue: '实时降雨',
      fullscreen: false
    };
  },
  computed: {},
  async mounted() {
    await this.grandTotalRainfallGrid(-1);
    await this.grandTotalRainfallGrid(0);
    await this.grandTotalRainfallGrid(1);
    await this.grandTotalRainfallGrid(2);
    await this.grandTotalRainfallGrid(3);
    await this.grandTotalRainfallGrid(4);
    this.getArea().then(() => {});
  },
  components: {
    DataDetailButton
  },
  methods: {
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
      this.$nextTick(() => {
        this.$refs.multipleTable.doLayout();
      });
    },
    Formatter(row, column, value, index) {
      // if (!value || value == "") return "-";
      if (value != null) {
        let valueFixed = String(value);
        if (valueFixed.split(',').length > 1) {
          valueFixed = valueFixed.split(',')[0];
        }
        valueFixed = Number(valueFixed).toFixed(1);
        return valueFixed;
      } else {
        return '-';
      }
    },
    /**
     * 行政区划
     */
    async getArea() {
      let obj = {
        areaCode: 3303,
        level: 3
      };
      await disasterPreventionApi.getArea(obj).then((res) => {
        if (res.code == 0) {
          this.administration = res.data;
        }
      });
    },
    /**
     * 实时降雨
     */
    async grandTotalRainfallGrid(index) {
      this.loading = true;
      let obj = {
        hour: this.durationRainfall[index],
        type: this.realTimeType, //只能是面雨量或者点雨量
        surfaceType: this.surfaceType, //在type选择面雨量之后，只能传流域或者行政区划
        areaCode: this.administrativeType, //行政区划编码，只有在type选择点雨量的时候才传
        bas: this.drainageType, //流域名称（瓯江/飞云江/鳌江），只有在type选择点雨量的时候才传
        isSurfaceRainStation: this.radio // 重点时传true 非重点传false
      };
      await disasterPreventionApi
        .grandTotalRainfallGrid(obj)
        .then((res) => {
          if (res.code == 0) {
            if (!this.durationRainfall[index]) {
              this.realTimeData = res.data;
            } else {
              if (this.durationRainfall[index] == 1) {
                let arr = [];
                this.realTimeData.forEach((item, index) => {
                  item['1hour'] = res.data[index]['1hour'];
                  arr.push(item);
                });
                this.realTimeData = arr;
              } else if (this.durationRainfall[index] == 3) {
                let arr = [];
                this.realTimeData.forEach((item, index) => {
                  item['3hour'] = res.data[index]['3hour'];
                  arr.push(item);
                });
                this.realTimeData = arr;
              } else if (this.durationRainfall[index] == 6) {
                let arr = [];
                this.realTimeData.forEach((item, index) => {
                  item['6hour'] = res.data[index]['6hour'];
                  arr.push(item);
                });
                this.realTimeData = arr;
              } else if (this.durationRainfall[index] == 24) {
                let arr = [];
                this.realTimeData.forEach((item, index) => {
                  item['24hour'] = res.data[index]['24hour'];
                  arr.push(item);
                });
                this.realTimeData = arr;
              } else if (this.durationRainfall[index] == 72) {
                let arr = [];
                this.realTimeData.forEach((item, index) => {
                  item['72hour'] = res.data[index]['72hour'];
                  arr.push(item);
                });
                this.realTimeData = arr;
              }
            }
          }
          if (index == 4) {
            this.loading = false;
          }
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    handleClose() {
      this.$emit('close');
    },
    selectVal() {
      this.grandTotalRainfallGrid();
      if (this.realTimeType == '面雨量') {
        this.drainageType = '';
        this.surfaceType = '';
      } else {
        if (this.surfaceType == '流域') {
          this.administrativeType = '';
        } else {
          this.drainageType = '';
        }
      }
    },
    changeVal() {
      this.durationRainfall = [];
      this.realTimeData = [];
    },
    changeType() {
      if (this.typeValue == '实时降雨') {
        this.grandTotalRainfallGrid();
      } else {
        this.realTimeData = [];
        // this.wiuWarnByAdcd();
      }
    },
    exportExcel() {}
  }
};
</script>

<style lang="scss" scoped>
.mg-l16 {
  margin-left: 16px;
}
.fr {
  float: right;
}
.sluice-detail-table {
  ::v-deep.el-dialog {
    background: transparent;
    @include flexbox;
    @include flexflow(column, nowrap);
    height: 680px;
    background: url(~assets/images/WaterDisastersImage/dialog-bg.png) no-repeat;
    background-size: 100% 100%;
    &.is-fullscreen {
      height: 100%;
      overflow: hidden;
    }
    .el-dialog__headerbtn {
      top: 39px;
      right: 30px;
      font-size: 18px;
      .el-dialog__close {
        color: #fff;
      }
    }
    .el-dialog__header {
      // border: 1px solid #00c1ff;
      // box-shadow: inset 0 0.25rem 1.875rem 0 #2351ef40;
      // background-image: linear-gradient(180deg, #1f375f 0%, #1b3047 100%);
      padding-top: 28px;
      padding-bottom: 16px;
      background: transparent;
      box-shadow: none;
      .primary-dialog__title {
        position: relative;
        background: url(~assets/images/WaterDisastersImage/dialog-title-bg.png) no-repeat;
        background-size: 100% 100%;
        height: 42px;
        padding: 0;
        .primary-dialog__title--label {
          width: 80%;
          height: 42px;
          color: #fff;
          font-size: 16px;
          letter-spacing: 0;
          font-weight: bold;
          line-height: 42px;
          padding-left: 56px;
        }
        .full-screen-icon {
          position: absolute;
          right: 40px;
          color: #00c1ff;
          cursor: pointer;
          top: 15px;
        }
      }
      .el-dialog__headerbtn {
        color: #00c1ff;
      }
    }

    .el-dialog__body {
      padding: 2px;
      padding-top: 0;
      border-top: 0;
      margin: 0 0.125rem;
      .primary-dialog__content {
        height: 100%;
        // height: 680px;
        border-top: 0;
        padding: 0 16px;
        // box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        // background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);

        // 统计样式
        .statistics-sty {
          padding: 10px 0;
          width: 100%;
          display: flex;
          justify-content: left;
          // border-bottom: 1px solid rgba($color: #409eff, $alpha: 0.4);
          > div {
            // margin-right: 50px;
            padding: 10px 0;
            margin-right: 20px;
            display: flex;
            justify-content: space-between;
            > p {
              padding-top: 5px;
              margin-right: 15px;
            }
            > div {
              > p:nth-child(1) {
                font-family: PingFangSC-Regular;
                font-size: 15px;
                color: #ffffff;
                letter-spacing: 0;
                line-height: 24px;
                font-weight: 400;
                margin-bottom: 5px;
              }
              > p:nth-child(2) {
                font-family: PingFangSC-Medium;
                font-size: 14px;
                color: #fff;
                letter-spacing: 0;
                line-height: 24px;
                font-weight: 500;
                > span {
                  font-size: 24px;
                  color: #56fefe;
                }
              }
            }
          }
        }
        // 查询样式
        .query-sty {
          // padding: 15px 0;
          margin: 10px 0;
          color: #fff;
          .derive-sty {
            padding: 0 10px;
            text-align: center;
            border-radius: 4px;
            line-height: 30px;
            float: right;
            color: #fff;
            cursor: pointer;
            border-color: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%) !important;
            background: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
          }
          .aa {
            margin-right: 20px;
            margin-bottom: 10px;
          }
          .demo-input-suffix {
            display: flex;
            justify-content: left;
            align-items: center;
          }
        }

        .tab-list-sty {
          @include flex(1);
          overflow: auto;
          padding-bottom: 20px;
        }
      }
    }
  }
  ::v-deep.el-input-number {
    width: 5.25rem;
  }
  ::v-deep.el-input__inner:hover,
  .el-select:hover .el-input__inner {
    border: 1px solid #fff;
  }
  ::v-deep.el-input__inner {
    height: 2rem;
    background: rgba(24, 47, 90, 0.1);
    color: #fff;
    border: 1px solid #fff;
  }
  ::v-deep.el-select .el-input .el-select__caret {
    color: #fff;
  }
  ::v-deepinput::-webkit-input-placeholder {
    color: #fff;
  }
  .dialog-footer {
    .el-button--primary.is-plain {
      color: #fff;
      border-color: #0bbbff;
      background-color: rgba($color: #0bbbff, $alpha: 0.1);
    }
  }
  ::v-deep.el-table .warm-row .cell {
    color: #eec80b !important;
  }
  .abc ::v-deep.el-table__body tr:hover {
    background-color: rgba($color: #56fefe, $alpha: 0.4);
  }
  // 弹窗底部按钮样式
  // .dialog-footer {
  //   position: absolute;
  //   right: 20px;
  //   bottom: 15px;
  //   .el-button--primary.is-plain {
  //     color: #fff;
  //     border-color: #0bbbff;
  //     background-color: rgba($color: #0bbbff, $alpha: 0.1);
  //   }
  // }
  ::v-deep.el-dialog .el-table {
    th {
      color: #00baff;
      padding: 3px 0;
      font-size: 14px;
      background-color: rgba(41, 147, 180, 0.1);
      font-weight: bold;
      .cell {
        height: 37px;
        line-height: 37px;
      }
    }
  }

  ::v-deep.el-table .ascending .sort-caret.ascending {
    border-bottom-color: #1cfff5;
  }
  ::v-deep.el-table .descending .sort-caret.descending {
    border-top-color: #1cfff5;
  }
  .myClass .el-input__suffix {
    margin-top: -3px;
    padding: 0;
  }
  .el-date-editor .el-range-input {
    background: none;
    color: #fff;
  }
  .el-date-editor .el-range-separator {
    color: #fff;
    line-height: 1.5rem;
  }
  .el-date-editor .el-range__icon {
    color: #fff;
    line-height: 1.4rem;
  }
  .el-date-editor .el-range__close-icon {
    color: #fff;
    line-height: 1.4rem;
  }
  .el-date-editor .el-icon-date {
    line-height: 1;
  }
  .detail-table {
    .el-table__body-wrapper::-webkit-scrollbar {
      height: 6px; // 纵向滚动条 必写
      width: 6px;
    }
  }
  .tb-top-panel {
    margin-top: 10px;
    color: #fff;
    display: flex;
    align-items: center;
    i {
      float: left;
    }
    p {
      flex: 1;
      font-size: 15px;
      margin-left: 10px;
      background-color: rgba(255, 255, 255, 0.1);
      padding: 8px 10px;
      border-radius: 4px;
    }
    span {
      color: #7ccfff;
      font-size: 20px;
    }
    .laba {
      width: 24px;
      height: 24px;
      background: url(~assets/images/laba.png) no-repeat;
      background-size: 100%;
    }
  }

  .is-checked .el-checkbox__label {
    color: #70adf8;
  }
  .el-radio {
    margin-right: 15px;
  }
  .el-radio__label {
    color: #fff;
  }
  .is-checked .el-checkbox__inner {
    border-color: #70adf8;
    color: #70adf8;
    &::after {
      border-color: #70adf8;
    }
  }
  .water-supply-select-panel,
  .beauty-tab-panel {
    display: flex;
    margin-bottom: 20px;
  }
  .beauty-tab-panel {
    margin-bottom: 0;
  }
  .water-supply-select-btn,
  .beauty-tab-item {
    padding: 10px 20px;
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

  .pagination {
    margin-top: 15px;
    text-align: right;
  }
  .el-pagination {
    .el-pagination__total {
      color: #fff;
    }
    .el-pager {
      li {
        color: #fff;
        background: rgba(0, 0, 0, 0);
        &.active {
          background-color: #409eff !important;
        }
      }
    }
    .btn-prev,
    .btn-next {
      background: rgba(0, 0, 0, 0);
    }
  }
}
</style>

<style lang="scss">
.list-dialog .tab-list-sty .el-table tr {
  color: #fff;
}
.list-dialog .tab-list-sty .el-table--striped .el-table__body tr.el-table__row--striped td,
.list-dialog .tab-list-sty .el-table--striped .el-table__body tr.el-table__row--striped td:hover,
.list-dialog .tab-list-sty .el-table--enable-row-hover .el-table__body tr:hover > td {
  background: rgba(35, 157, 239, 0.23) !important;
}
</style>