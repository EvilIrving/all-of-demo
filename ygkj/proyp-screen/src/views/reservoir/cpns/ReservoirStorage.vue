<!--
 * @Author: wqn
 * @Date: 2023-02-14 11:42:01
 * @LastEditors: dtb
 * @LastEditTime: 2023-03-07 17:41:38
 * @Description: 水库纳蓄
-->
<template>
  <ModuleLayout title="纳蓄" disabled>
    <div slot="extra" class="extra_btn">
      <el-button type="primary" size="mini" @click="showTheDZX=true">大中型水库</el-button>
    </div>
    <div class="reservoir-storage">
      <div class="storage-tap">
        <div>
          <p>当前蓄水总量</p>
          <p>
            <span>{{ nxData.capable }}</span>亿m³
          </p>
        </div>
        <div>
          <p>当前蓄水率</p>
          <p>
            <span>{{ nxData.storagePerc }}</span>%
          </p>
        </div>
      </div>
      <div class="highcharts-box">
        <div id="histogram3d"></div>
        <div class="highcharts-lenged">
          <div>可纳雨量</div>
          <div class="pointer" :class="{ active: activeRain === '200' }" @click="getReservoirList('200', 2)">
            <span class="green-color"></span>
            <span>200mm以上</span>
          </div>
          <div class="pointer" :class="{ active: activeRain === '100,200' }" @click="getReservoirList('', '', '100,200')">
            <span class="blur-color"></span>
            <span>100-200mm</span>
          </div>
          <div class="pointer" :class="{ active: activeRain === '50,100' }" @click="getReservoirList('', '', '50,100')">
            <span class="yellow-color"></span>
            <span>50-100mm</span>
          </div>
          <div class="pointer" :class="{ active: activeRain === '50' }" @click="getReservoirList('50', 1)">
            <span class="red-color"></span>
            <span>50mm以下</span>
          </div>
        </div>
      </div>
    </div>
    <MapLegend v-if="reservoirScale === '纳蓄'">
      <el-checkbox-group class="map-legend-checkbox" v-model="checkList" @change="changeCheckList">
        <el-checkbox label="1"><img src="@/assets/images/map_points/reservoir-lv1-green.png" /><span>大（I）型水库</span></el-checkbox>
        <el-checkbox label="2"><img src="@/assets/images/map_points/reservoir-lv2-green.png" /><span>大（II）型水库</span></el-checkbox>
        <el-checkbox label="3"><img src="@/assets/images/map_points/reservoir-lv3-green.png" /><span>中型水库</span></el-checkbox>
        <el-checkbox label="4"><img src="@/assets/images/map_points/reservoir-lv4-green.png" /><span>小（I）型水库</span></el-checkbox>
        <el-checkbox label="5"><img src="@/assets/images/map_points/reservoir-lv5-green.png" /><span>小（II）型水库</span></el-checkbox>
      </el-checkbox-group>
    </MapLegend>
  </ModuleLayout>
</template>

<script>
import Highcharts from "highcharts";
import highcharts3d from "highcharts/highcharts-3d";
import cylinder from "highcharts/modules/cylinder";
import funnel3d from "highcharts/modules/funnel3d";
import {
  reservoirBigScreen,
  nxStatisticApi,
  generateProject,
} from "@/api/api_reservoir";
export default {
  name: "ReservoirStorage",
  props: {
    map: {
      type: Object,
      default: null,
    },
  },
  components: {},
  data() {
    return {
      funnelData: {
        id: "funnelData",
        data: [],
        unit: "座",
      },
      nxData: {
        capable: 0,
        storagePerc: 0,
      },
      activeRain: "",
      rainOpt: null,
      checkList: ['1', '2', '3', '4', '5'],
      projectScale: '1, 2, 3, 4, 5',
      showTheDZX: false, // 只展示大中型水库
      pointsList: [],
    };
  },
  computed: {
    selectAdcd() {
      return this.$store.state.selectAdcd;
    },
    reservoirScale() {
      return this.$store.state.reservoir.reservoirScale;
    },
  },
  watch: {
    showTheDZX(newVal) {
      if (newVal) {
        this.projectScale = '1,2,3'
        this.changeCheckList('1,2,3'.split(','))
        this.getReservoirList('200', 2)
      }

      this.getFunnelData();
    },
    selectAdcd() {
      this.getNxData();
      this.getFunnelData();
      if (this.rainOpt) {
        this.getReservoirList(
          this.rainOpt.rain,
          this.rainOpt.ifUnder,
          this.rainOpt.rainScope,
          true
        );
      }
    },
    reservoirScale(value) {
      if (value !== "纳蓄") {
        this.activeRain = "";
        this.rainOpt = null;
        this.removeDangerPoint();
      }
    },
  },
  mounted() {
    highcharts3d(Highcharts);
    funnel3d(Highcharts);
    cylinder(Highcharts);
    this.getNxData();
    this.getFunnelData();
  },
  methods: {
    arraysEqual(array1, array2) {
      if (array1.length !== array2.length) return false
      for (let i = 0; i < array1.length; i++) {
        if (array1[i] !== array2[i]) {
          return false;
        }
      }
      return true;
    },
    //点位图例
    changeCheckList(val) {
      this.checkList = val
      if (!this.arraysEqual(val, ['1', '2', '3'])) {
        //图例选中项不等于大中型 则重置大中型按钮
        this.showTheDZX = false
        this.projectScale = val.join(',')
      }

      this.getNxData()

      this.$olMapUtil.drawPoint(
        this.map,
        this.pointsList.filter((e) =>
          this.checkList.includes(e.data.project_scale)
        ),
        "纳蓄雨量落点"
      );
    },
    //蓄水统计
    async getNxData() {
      let opt = {
        adcd: this.selectAdcd,
        projectScale: this.projectScale,
      };
      let res = await nxStatisticApi(opt);
      if (res.success && res.data.length !== 0) {
        this.nxData.capable = 0;
        this.nxData.storagePerc = 0;
        res.data.forEach((item) => {
          this.nxData.capable += item.capable;
          this.nxData.storagePerc += item.storagePerc;
        });
      } else {
        this.nxData.capable = 0;
        this.nxData.storagePerc = 0;
      }
    },
    // 水库库容
    async getFunnelData() {
      let opt = {
        adcd: this.selectAdcd,
        gpType: 6,
        projectScale: this.projectScale,
      };
      let res = await reservoirBigScreen(opt);
      if (res.success) {
        if (res.data.length > 0) {
          let arr = [];
          let sumprice = res.data.reduce(function (totalValue, currentValue) {
            totalValue += currentValue.num;
            currentValue.accumulateNum = totalValue;
            return totalValue;
          }, 0);
          res.data.reverse();
          res.data.map((i) => {
            i.name = i.num + "座";
            i.value = ((i.accumulateNum / sumprice) * 100 * 0.6).toFixed(2);
            arr.push(i);
          });
          this.funnelData.data = arr;
          this.funnel3dData = [];
          arr.forEach((i) => {
            this.funnel3dData.push([i.fz, +i.num]);
          });
        }
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.initHistogram3D();
      this.funnelLoading = false;
    },
    // 3D柱状图
    initHistogram3D() {
      new Highcharts.chart("histogram3d", {
        chart: {
          type: "funnel3d",
          options3d: {
            enabled: true,
            alpha: 6,
            viewDistance: 50,
          },
          backgroundColor: "rgba(0,0,0,0)",
        },
        title: false,
        plotOptions: {
          series: {
            dataLabels: {
              enabled: true,
              format: "<b>{point.y:,.0f}",
              color: (Highcharts.theme && Highcharts.theme.colors) || "black",
              allowOverlap: false,
              y: 12,
              // color: "#fff",
              style: {
                fontSize: 14,
              },
              align: "center",
            },
            cursor: "pointer",
            // neckWidth: "20%",
            neckHeight: "0%",
            width: "100%",
            height: "100%",
            events: {
              click: (event) => {
                switch (event.point.name) {
                  case "50mm以下":
                    // this.chooseNxType("纳蓄", "50,1", 3);
                    break;
                  case "50-100mm":
                    // this.chooseNxType("纳蓄", "50,100", 2);
                    break;
                  case "100-200mm":
                    // this.chooseNxType("纳蓄", "100,200", 1);
                    break;
                  default:
                    // this.chooseNxType("纳蓄", "200,2", 0);
                    break;
                }
              },
            },
          },
        },
        credits: {
          enabled: false,
        },
        series: [
          {
            name: "",
            data: this.funnel3dData,
            colorByPoint: true,
            allowPointSelect: true,
            colors: [
              "rgba(91, 190, 88, 0.7)",
              "rgba(56, 134, 255, 0.7)",
              "rgba(247, 181, 0, 0.7)",
              "rgba(250, 100, 0, 0.8)",
            ],
          },
        ],
      });
    },
    //移除图层
    removeDangerPoint() {
      this.$olMapUtil.removeLayer(this.map, "纳蓄雨量落点");
    },
    //纳蓄图例水库点位
    async getReservoirList(rain, ifUnder, rainScope, flag) {
      const activeRain = rain || rainScope;
      if (!flag) {
        if (this.activeRain === activeRain) {
          this.activeRain = "";
          this.rainOpt = null;
          this.removeDangerPoint();
          if (this.reservoirScale === "纳蓄") {
            this.$store.commit("setReservoirScale", "");
          }
          return;
        }
      }
      this.$store.commit("setReservoirScale", "纳蓄");
      this.activeRain = activeRain;
      this.rainOpt = {
        rain,
        ifUnder,
        rainScope,
      };
      let opt = {
        adcd: this.selectAdcd,
        basinCode: "",
        gpType: 6,
        rain: rain,
        ifUnder: ifUnder,
        rainScope: rainScope,
      };
      let res = await generateProject(opt);
      if (res.success) {
        this.pointsList = res.data.map((item) => {
          return {
            longitude: item.LGTD,
            latitude: item.LTTD,
            styleObj: {
              src: require(`@/assets/images/map_points/reservoir-lv${item.project_scale}-green.png`),
              scale: 0.7,
            },
            data: item,
          };
        });

        this.showPoints()
        //  this.checkList = this.showTheDZX ? this.projectScale.split(',') : ['1', '2', '3', '4', '5'];
        // this.$olMapUtil.drawPoint(this.map, this.pointsList, "纳蓄雨量落点");
      }
    },

    showPoints() {
      console.log('---------')
      this.checkList = this.showTheDZX ? this.projectScale.split(',') : ['1', '2', '3', '4', '5'];

      this.$olMapUtil.drawPoint(
        this.map,
        this.pointsList.filter((e) =>
          this.checkList.includes(e.data.project_scale)
        ),
        "纳蓄雨量落点"
      );

    }
  },
};
</script>
<style lang='scss' scoped>
.extra_btn {
  margin-right: 70px;
}
.reservoir-storage {
  height: 100%;
  .pointer {
    cursor: pointer;
  }

  .storage-tap {
    padding: 10px 0;
    > div {
      display: inline-block;
      width: 194px;
      height: 84px;
      position: relative;
      background: url("../../../assets/images/reservoir-storage.png") no-repeat;
      margin-right: 15px;
      &:last-child {
        margin-right: 0;
      }
      P {
        padding-left: 15px;
        &:first-child {
          font-size: 18px;
          font-family: "fzzyjw";
          padding-top: 15px;
        }
        &:last-child {
          padding-top: 5px;
          color: #b1dcff;
          font-size: 16px;
          span {
            font-family: "digitalnarrow";
            font-size: 36px;
            padding-right: 10px;
            letter-spacing: 5px;
            color: #74dbff;
          }
        }
      }
    }
  }
  .highcharts-box {
    height: calc(100% - 104px);
    display: flex;
    #histogram3d {
      height: 100%;
      width: 240px;
    }
    .highcharts-lenged {
      padding: 15px 0 0 10px;
      height: 100%;
      box-sizing: border-box;
      display: flex;
      flex-direction: column;
      justify-content: center;
      > div {
        margin-bottom: 8%;
        &.active {
          span {
            &:last-child {
              color: #69e3ee;
            }
          }
        }
        > span {
          display: inline-block;
          &:first-child {
            width: 30px;
            height: 16px;
            border-radius: 2px;
            &.green-color {
              background-color: #3f9e40;
            }
            &.blur-color {
              background-color: #1e6be4;
            }
            &.yellow-color {
              background-color: #da9500;
            }
            &.red-color {
              background-color: #ff7234;
            }
          }
          &:last-child {
            padding-left: 15px;
          }
        }
      }
    }
  }
}
</style>