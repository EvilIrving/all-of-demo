<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-10-11 11:29:35
 * @Description: 
 * @FilePath: \big_screen\src\views\floodControl\FloodControl.vue
-->
<template>
  <div class="wrap flex-box">
    <section class="left">
      <div class="box_item warp-box">
        <h2 class="warp-title">旱情指数</h2>
        <div class="curve">
          <div class="curve_content">
            <EchartLine
              ref="droughtCurveLine"
              :extraOption="lineOption"
            ></EchartLine>
          </div>
          <div class="curve_content">
            <EchartLine
              ref="droughtCurveLine"
              :extraOption="lineOption"
            ></EchartLine>
          </div>
        </div>
      </div>
    </section>
    <section class="right">
      <div class="box_item warp-box">
        <h2 class="warp-title">控制运行</h2>
        <progress-list
          v-loading="progressLoading"
          title="工程视频分布情况"
          class="progress"
          :progressData="progressData"
        ></progress-list>
        <div class="pieces">
          <div
            class="piece"
            v-for="(item, index) in waterRealesList"
            :key="index"
          >
            <p class="piece_title">{{ item.name }}</p>
            <img
              v-if="item.id == 1"
              src="@/assets/image/water-release-circle.png"
              alt=""
            />
          </div>
        </div>
      </div>
      <div class="box_item warp-box">
        <h2 class="warp-title">下游预警</h2>
        <div class="strips">
          <div class="strip" v-for="(item, index) in warningList" :key="index">
            <span>{{ item.name }}</span
            ><span>{{ item.status }}</span>
            <div></div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import EchartLine from "../../components/echart_line/EchartLine.vue";
export default {
  name: "irrigation",
  props: {},
  data() {
    return {
      waterRealesList: [
        { name: "老虎湾", id: 1 },
        { name: "老虎湾", id: 1 },
        { name: "老虎湾", id: 1 },
        { name: "老虎湾", id: 1 },
        { name: "老虎湾", id: 1 },
        { name: "老虎湾", id: 1 },
      ],
      warningList: [
        { name: "老虎湾", status: "已完成" },
        { name: "老虎湾", status: "已完成" },
        { name: "老虎湾", status: "已完成" },
        { name: "老虎湾", status: "已完成" },
        { name: "老虎湾", status: "已完成" },
      ],
      //折线图
      lineOption: {
        tooltip: {
          trigger: "axis",
        },
        legend: {
          show: false,
        },
        xAxis: {
          type: "category",
          boundaryGap: false,
          data: [1, 2, 3, 3, 5, 6, 7, 8, 9],
          nameTextStyle: {
            color: "#113d5e",
            fontSize: 10,
          },
          axisLine: {
            show: true,
            lineStyle: {
              type: "dashed",
              color: "#fff",
              width: 2,
              opacity: 0.15,
            },
          },
        },
        yAxis: {
          type: "value",
          name: "",
          minInterval: 1,
          axisLabel: {
            show: false,
            color: "#fff",
            fontSize: "12",
          },
          splitLine: {
            show: true,
            lineStyle: {
              type: "dashed",
              color: "#fff",
              width: 2,
              opacity: 0.15,
              // dashOffset: 5,
            },
          },
        },
        series: [
          {
            name: "",
            type: "line",
            smooth: true,
            symbol: "none",
            color: "#15E2DB",
            areaStyle: {
              color: {
                type: "linear",
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: "#15E2DB", // 0% 处的颜色
                  },
                  {
                    offset: 1,
                    color: "rgba(0,0,0,0.5)", // 100% 处的颜色
                  },
                ],
                global: false, // 缺省为 false
              },
            },
            data: [12, 15, 36, 3, 12, 54, 87, 68, 25],
          },
        ],
      },
    };
  },
  computed: {},
  created() {},
  mounted() {
    this.getDroughtIndexCurve();
  },
  watch: {},
  methods: {
    //获取旱情指数曲线数据
    async getDroughtIndexCurve() {
      console.log("111111111111111111");
      // let res = await engineerAlarmApi.engineerAlarmCurve({
      //   type: this.tabCorrType[this.activeTab].type,
      // });
      // let res = {
      //   code: 0,
      //   data: [
      //     { month: 1, num: 12 },
      //     { month: 2, num: 435 },
      //     { month: 3, num: 12 },
      //     { month: 4, num: 32 },
      //     { month: 5, num: 33 },
      //     { month: 6, num: 66 },
      //     { month: 7, num: 4 },
      //     { month: 8, num: 87 },
      //   ],
      // };
      // if (res.code == 0) {
      //   let xData = [];
      //   let yData = [];
      //   res.data.map((item) => {
      //     xData.push(item.month);
      //     yData.push(item.num);
      //   });
      //   this.lineOption.xAxis.data = xData;
      //   this.lineOption.series[0].data = yData;
      // } else {
      //   this.lineOption.xAxis.data = [];
      //   this.lineOption.series[0].data = [];
      // }
    },
  },
  components: { EchartLine },
};
</script>

<style lang="scss" scoped>
.left,
.right {
  padding-top: 62px;
  width: 300px;
}
.flex-box {
  justify-content: space-between;
  box-sizing: border-box;
}
.left {
}
.right {
}
.box_item {
  margin-bottom: 20px;
  width: 100%;
  > h2 {
    // padding-top: 20px;
  }
  .curve {
    width: 100%;
    height: 90%;
    .curve_content {
      width: 86.88%;
      height: 200px;
      margin: 10px auto;
    }
  }
  .pieces,
  .strips {
    width: 86.88%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-flow: row wrap;
    margin: 20px auto;
  }
  .pieces .piece {
    width: 45%;
    height: 130px;
    padding: 10px 0;
    box-sizing: border-box;
    margin-bottom: 20px;
    // background: linear-gradient(179deg, #334269 0%, #43f7e2 100%);
    // box-shadow: 0px 0px 100px 0px #47e6f4;
    border: 1px solid #00fbff;
    display: flex;
    opacity: 0.55;
    justify-content: space-around;
    align-items: center;
    flex-flow: column wrap;
    .piece_title {
      text-align: center;
      height: 30px;
      font-size: 16px;
      font-weight: 500;
      color: #ffffff;
      line-height: 30px;
    }
    img {
      text-align: center;
      width: 60px;
      height: 60px;
    }
  }
  .strip {
    display: flex;
    justify-content: space-around;
    align-items: center;
    width: 100%;
    height: 25px;
    margin-bottom: 10px;
    // background: linear-gradient(90deg, #00fbff 0%, #224d63 100%);
    opacity: 0.55;
    border: 1px solid;
    border-image: linear-gradient(
        64deg,
        rgba(0, 251, 255, 1),
        rgba(33, 76, 98, 1)
      )
      1 1;
    span {
      font-size: 14px;
      font-weight: 500;
    }
    & > span:nth-child(1) {
      color: #00fbff;
    }
    & > span:nth-child(2) {
      color: #c0c4cc;
    }
    & > div {
      width: 11px;
      height: 11px;
      background: #39ef0f;
      border-radius: 50%;
    }
  }
}
</style>
