<!--
 * @Date: 2022-06-01 17:44:11
 * @Author: 
 * @Description: 水雨情
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-07-25 17:18:00
 * @FilePath: \lucheng-app\src\views\appView\reservoir\indexPage\WaterAndRain.vue
-->
<template>
  <div class="wrap-page">
    <div class="page-detail-box">
      <wrapTitle title="水情信息" />
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
        <div v-if="!hasWater && !hasRain" class="nodata">
          <img src="../../../../assets/nodata.png" alt="">
          <p>暂无数据</p>
        </div>
        <div class="echarts-bar-box" v-if="hasRain">
          <EchartsBar :extraOption="rainExtraOption" :inverse="hasWater" :widthIndex="defaultIndex" />
        </div>
        <div class="echarts-bar-box" v-if="hasWater">
          <EchartsLine :extraOption="extraOption" />
        </div>
      </div>
      <div>
        <wrapTitle title="水位列表" />
        <div>
          <tableList v-if="hasWater || hasRain" :theadList="theadList" :list="list" />
          <div v-else class="nodata">
            <img src="../../../../assets/nodata.png" alt="">
            <p>暂无数据</p>
          </div>
        </div>
      </div>
    </div>
    <!--选择器-->
    <vantPicker @onConfirm="onConfirm" @onCancel="onCancel" :defaultIndex="defaultIndex" :showPicker="showPicker" :columns="columns" />
  </div>
</template>

<script>
import echarts from "echarts";
import wrapTitle from "@/components/wrapTitle.vue";
import vantPicker from "@/components/vantPicker.vue";
import EchartsBar from "./EchartsBar.vue";
import EchartsLine from "./EchartsLine.vue";
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
      columns: ["前一天", "前二天", "前三天", "前四天", "前一周", "前二周"],
      defaultIndex: 0,
      showPicker: false,
      theadList: [
        {
          label: "时间",
          prop: "date",
          slot: false,
        },
        {
          label: "水位（m）",
          prop: "waterLevel",
          slot: false,
        },
      ],
      list: [],
      extraOption: {},
      rainExtraOption: {},
      stType: "RR",
      stCode: '',
      propList: [],
      hasRain: false,
      hasWater: false,
      seriesData: {},
      days: 1,
      statuslist: {
        水情: [
          { text: '当前水位', class: 'green_color', data: '', prop: 'waterLevel', unit: 'm' },
          { text: '最低水位', class: 'orange_color', data: '', prop: 'minLevel', unit: 'm' },
          { text: '最高水位', class: 'blue_color', data: '', prop: 'maxLevel', unit: 'm' },
          { text: '出现时间', class: 'blue_color', data: '', prop: 'maxLevelTime', unit: '' },

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
    hasRain: {
      handler: function (newVal, oldVal) {
        if (newVal) {
          this.theadList = [
            {
              label: "时间",
              prop: "date",
              slot: false,
            },
            {
              label: "水位（m）",
              prop: "waterLevel",
              slot: false,
            },
            {
              label: "雨量（mm）",
              prop: "rainValue",
              slot: false,
            }
          ],
            this.statuslist['水情'].splice(1, 0, { text: '累计雨量', class: 'green_color', data: 0, prop: 'rainAll', unit: 'm' })
        }
      },
      immediate: true
    },
    type: {
      handler: function (val) {
        let temp = ''
        let code = ''
        switch (val) {
          case '水闸':
            temp = 'DD'
            code = this.$localData('get', 'engineeringInfo').stCode;
            break;
          case '堤防':
            temp = 'TT'
          case '山塘':
          case '海塘':
            temp = 'TT'
            code = this.$localData('get', 'engineeringInfo').seawallCode;
            break;
          default:
            temp = 'RR'
            code = this.$localData('get', 'engineeringInfo').mainStcd;
            break;
        }
        this.stType = temp
        this.stCode = code
      },
      immediate: true
    },
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
      this.hasWater = false
      this.hasRain = false
      let timeList = [];
      this.list = []
      let rainData = [];
      let waterData = [];
      let marklineArr = []
      // RR水库水位站，PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站，ZZ河道，SS墒情,PT泵站
      let opt = {
        days: this.days,
        prcd: this.prcd,
      };
      switch (this.type) {
        case '水闸':
          opt.stationCode = this.stCode
          opt.stationType = this.stType
          break;
        case '堤防':
        case '山塘':
        case '海塘':
          opt.stationType = this.stType
          break;
        default:
          opt.stationCode = this.stCode
          opt.stationType = this.stType
          break;
      }
      let res = await realTimeMonitoring.waterRainHours(opt);
      if (res.code === 0) {
        // 水库→汛限水位；河道、闸坝→警戒水位、保证水位；潮位→警戒水位；
        this.hasRain = res.data.hasRain || false
        this.hasWater = res.data.hasWater || false
        switch (this.type) {
          case '水闸':
            marklineArr = [
              { name: '警戒水位', value: res.data.warningLevel || 0, color: '#FF0000' },
              { name: '保证水位', value: res.data.guaranteeLevel || 0, color: '#40CD96' }
            ]
            break;
          default:
            marklineArr = [
              { name: '汛限水位', value: res.data.limitLevel || 0, color: '#FF0000' },
            ]
            break;
        }
        res.data.maxLevelTime = res.data.maxLevelTime != null ? res.data.maxLevelTime.substring(5, 16) : ''
        res.data.waterLevel = res.data.list[res.data.list.length - 1].waterLevel
        let resProp = res.data, list = [];
        for (let item of this.statuslist['水情']) {
          list.push({
            name: item.text,
            class: item.class,
            unit: item.unit || '',
            prop: item.prop,
            data: (resProp && resProp[item.prop]) || '-'
          });
        }
        this.propList = list;
        res.data.list.forEach(item => {
          timeList.push(item.date);
          rainData.push(item.rainValue);
          waterData.push(item.waterLevel);
          this.list.push({
            date: item.date,
            waterLevel: parseFloat(item.waterLevel),
            rainValue: parseFloat(item.rainValue),
          })
        });
    
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

        let minValue = this.arrayMin(waterData)
        let maxValue = this.arrayMax([res.data.limitLevel, res.data.warningLevel, res.data.guaranteeLevel, ...waterData])

        // 数据
        this.extraOption.series = [{ name: '水位', data: waterData, markLine: { data: mark } }]
        // x轴
        this.extraOption.xAxis = {
          data: timeList
        }
        // y轴
        this.extraOption.yAxis = {
          // min: minValue * 0.9,
          max: maxValue * 1.1
        }

        // 数据
        this.rainExtraOption.series = [{ name: '雨量', data: rainData }]
        // x轴
        this.rainExtraOption.xAxis = {
          data: timeList
        }
      }
    },
    onConfirm(data, index) {
      this.defaultIndex = index;
      this.showPicker = false;
      switch (index) {
        case 1:
          this.days = 2;
          break;
        case 2:
          this.days = 3;
          break;
        case 3:
          this.days = 4;
          break;
        case 4:
          this.days = 7;
          break;
        case 5:
          this.days = 14;
          break;
        default:
          this.days = 1;
          break;
      }
      this.getDateList();
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
    onCancel() {
      this.showPicker = false;
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
            font-size: 14px;
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