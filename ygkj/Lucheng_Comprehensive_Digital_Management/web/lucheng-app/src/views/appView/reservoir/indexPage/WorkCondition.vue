<!--
 * @Date: 2022-06-08 10:59:09
 * @Author: 
 * @Description: 水闸工情
 * @LastEditors: dtb
 * @LastEditTime: 2022-06-28 14:04:30
 * @FilePath: \lucheng-app\src\views\appView\reservoir\indexPage\WorkCondition.vue
-->
<template>
  <div class="wrap-page">
    <div class="page-detail-box">
      <wrapTitle title="工情信息" />
      <div class="picker-box">
        <div>
          <span @click="showPicker = true">{{columns[defaultIndex]}}</span>
          <span class="el-icon-caret-bottom"></span>
        </div>
      </div>
      <div class="van-grid-box">
        <div class="van-grid-item" v-for="(item,index) in propList" :key="index">
          <div>{{item.name}}</div>
          <div>
            <span :class="[item.class ? item.class :'blue_color']">{{item['data'] || "-"}} </span>
            <span>{{item.unit}}</span>
          </div>
        </div>
      </div>
      <div class="echarts-box">
        <div v-if="!hasRain" class="nodata">
          <img src="../../../../assets/nodata.png" alt="">
          <p>暂无数据</p>
        </div>

        <div class="echarts-bar-box" v-if="hasRain">
          <EchartsLine v-if="showEcahrt" ref="lineChart" :extraOption="extraOption" />
        </div>
      </div>
      <div>
        <wrapTitle title="水位列表" />
        <div>
          <div v-if="!hasRain" class="nodata">
            <img src="../../../../assets/nodata.png" alt="">
            <p>暂无数据</p>
          </div>
          <tableList v-else :theadList="theadList" :list="list" />

        </div>
      </div>
    </div>
    <!--选择器-->
    <vantPicker @onConfirm="onConfirm" @onCancel="onCancel" :defaultIndex="defaultIndex" :showPicker="showPicker" :columns="columns" />
  </div>
</template>

<script>
import wrapTitle from "@/components/wrapTitle.vue";
import vantPicker from "@/components/vantPicker.vue";
import EchartsBar from "@/components/EchartsBar.vue";
import EchartsLine from "@/components/EchartsLine.vue";
import tableList from "@/components/tableList.vue";
import { realTimeMonitoring } from "@/api/projectDetail.js";
export default {
  name: 'WaterAndRain',
  components: {
    wrapTitle,
    vantPicker,
    EchartsBar,
    EchartsLine,
    tableList,
  },
  data() {
    return {
      columns: ['前一天', '前二天', '前三天', '前四天', '前一周', '前二周'],
      defaultIndex: 5,
      showPicker: false,
      theadList: [
        {
          label: "时间",
          prop: "date",
          slot: false,
        },
        {
          label: "闸上水位",
          prop: "dwz",
          slot: false,
        },
        {
          label: "闸下水位",
          prop: "upz",
          slot: false,
        },
        {
          label: "流量",
          prop: "ll",
          slot: false,
        },
      ],
      list: [],
      extraOption: {},
      stType: "DD",
      stCode: this.$localData('get', 'engineeringInfo').stCode,
      propList: [],
      showEcahrt: false,
      hasRain: false,
      seriesData: {},
      days: 0,
      statuslist: {
        工情: [
          { text: '闸下水位', class: 'green_color', data: 12, prop: 'upz', unit: 'm' },
          { text: '闸上水位', class: 'blue_color', data: 11, prop: 'dwz', unit: 'm' },
        ],
      }
    };
  },
  computed: {
    prcd() {
      return this.$localData('get', 'engineeringInfo').code;
    },
    type() {
      return this.$localData('get', 'engineeringInfo').type;
    },
  },
  watch: {
    defaultIndex: {
      handler: function (newVal, oldVal) {
        switch (newVal) {
          case 0:
            this.days = 1;
            break;
          case 1:
            this.days = 2;
            break;
          case 2:
            this.days = 3;
            break;
          case 3:
            this.days = 4;
            break;
          case 7:
            this.days = 7;
            break;
          default:
            this.days = 14;
            break;
        }
      },
      immediate: true
    }
  },
  mounted() {
    this.addZwlog(this)
    this.getDateList()
  },
  methods: {
    //获取点位间接数据列表
    async getDateList() {
      this.hasRain = false
      this.list = []
      let timeList = [];
      let dataList = [];
      let marklineArr = [
        { name: '警戒水位', value: 0, color: '#FF0000' },
      ]
      // RR水库水位站，PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站，ZZ河道，SS墒情,PT泵站
      let opt = {
        stationCode: this.stCode,
        stationType: this.stType,
        days: this.days,
        prcd: this.prcd,
      };

      let res = await realTimeMonitoring.waterRainHours(opt);
      if (res.code === 0) {
        this.hasRain = res.data.hasRain || false
        marklineArr[0].value = res.data.warningLevel || 0
        res.data.dwz = res.data.list[res.data.list.length - 1].dwz || 0
        res.data.upz = res.data.list[res.data.list.length - 1].upz || 0
        res.data.ll = res.data.list[res.data.list.length - 1].ll || 0
        let resProp = res.data, list = [];
        for (let item of this.statuslist['工情']) {
          list.push({
            name: item.text,
            class: item.class,
            unit: item.unit || '',
            prop: item.prop,
            data: (resProp && resProp[item.prop]) || '-'
          });
        }
        this.propList = list;
        let tableData = res.data.list;
        for (let item of tableData) {
          timeList.push(item.date);
          dataList.push(item.ll);
          this.list.push({
            date: item.date,
            dwz: parseFloat(item.dwz),
            upz: parseFloat(item.upz),
            ll: parseFloat(item.ll),
          })
        }
        // markline
        let mark = []
        marklineArr.forEach((element, index) => {
          let line = {
            yAxis: element.value,
            lineStyle: {
              color: element.color
            },
            label: {
              color: element.color,
              formatter: element.name,
              fontSize: 10
            }
          }
          mark.push(line)
        });

        let minValue = this.arrayMin(dataList)
        let maxValue = this.arrayMax([res.data.warningLevel, ...dataList])

        // 数据
        this.extraOption.series = [{ name: '流量', data: dataList, markLine: { data: mark } }]
        // x轴
        this.extraOption.xAxis = {
          data: timeList
        }
        // y轴
        this.extraOption.yAxis = {
          // min: minValue * 0.9,
          max: maxValue * 1.1
        }
        this.showEcahrt = true
      }
    },
    onConfirm(data, index) {
      this.defaultIndex = index
      this.showPicker = false;
      this.getDateList()
    },
    onCancel() {
      this.showPicker = false;
    },
    // 查找数组中最小值
    arrayMin(arrs) {
      var min = arrs[0];
      for (var i = 1, ilen = arrs.length; i < ilen; i += 1) {
        if (arrs[i] < min) {
          min = arrs[i];
        }
      }
      return min;
    },

    // 查找数组中最大值
    arrayMax(arrs) {
      var max = arrs[0];
      for (var i = 1, ilen = arrs.length; i < ilen; i++) {
        if (arrs[i] > max) {
          max = arrs[i];
        }
      }
      return max;
    },
  },
};
</script>

<style lang="scss" scoped>
.page-detail-box {
  margin-top: 6px;
  background-color: #fff;
  .picker-box {
    padding: 0 8px;
    > div {
      border-top: 1px solid #e9f0fd;
      padding: 17px 8px;
      span {
        color: #666666;
        font-size: 12px;
        &:first-child {
          padding-right: 10px;
        }
        &:last-child {
          font-size: 14px;
          color: #aaaaaa;
        }
      }
    }
  }
  .van-grid-box {
    display: flex;
    border-top: 1px solid #e6e6e6;
    border-bottom: 1px solid #e6e6e6;
    .van-grid-item {
      flex: 1;
      padding: 11px 0;
      text-align: center;
      border-right: 1px solid #e6e6e6;
      &:last-child {
        border-right: none;
      }
      > div:first-child {
        font-size: 12px;
        color: #666666;
        font-weight: 600;
      }
      > div:last-child {
        padding-top: 5px;
        > span {
          &:first-child {
            font-size: 16px;
            font-weight: 500;
            line-height: 25px;
            &.blue_text {
              color: #276af0;
            }
            &.green_color {
              color: #01a59c;
            }
            &.red_color {
              color: #e02020;
            }
            &.orange_color {
              color: #fa6400;
            }
            &.blue_color {
              color: #276af0;
            }
            &.pink_color {
              color: #fa00b4;
            }
          }
          &:last-child {
            font-size: 10px;
          }
        }
      }
    }
  }
  .echarts-box {
    padding-top: 13px;
    .echarts-bar-box {
      height: 180px;
      width: 100%;
      padding: 0 12px;
      box-sizing: border-box;
    }
  }
}
</style>