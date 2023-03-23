<template>
  <div class="water-research-card">
    <panel-item title="水研究专题" has-switch tc @tclick="tclick">
      <template #title-switch>
        <el-dropdown placement="bottom-start" trigger="click">
          <span
            class="el-dropdown-link"
            :class="yearDropdownVisible ? 'dropdown-active' : ''"
          >
            <span style="font-size: 1rem">{{ year }}</span>
            <i class="el-icon-caret-bottom"></i>
          </span>
          <el-dropdown-menu slot="dropdown" class="year-dropdown">
            <el-dropdown-item v-for="(item, index) in yearOption" :key="index">
              <el-radio
                v-model="year"
                :label="item"
                @change="yearChange"
                >{{ item }}</el-radio
              >
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <!-- <i
          :class="[pointActive ? 'icon-yinzhang-active' : 'icon-yinzhang-1']"
        /> -->
        <i class="icon-liebiao" @click.stop="openDetailsDialog" />
      </template>
      <!-- 研究类别 -->
      <table-title title="研究类别" />
      <p class="trend-analysis-sty" @click="trendAnalysis">
        <i class="icon-qushi-fenxi" style="vertical-align:middle;diaplay:inline-block;margin-right:5px;"></i> 
        趋势分析
      </p>
        <!-- 研究类别图表 -->
        <div class="project-type">
          <ve-ring
            height="130px"
            width="160px"
            v-bind="prjectOption"
            :data="resultData"
            :tooltip-visible="false"
            :legend-visible="false"
            :after-config="afterProjectConfig"
          />
          <div class="project-type-legend">
            <ul class="list">
              <li v-for="(item, index) in resultData.rows" :key="index" class="list-item">
                <i class="dot" :style="{'backgroundColor': item.color}" />
                {{ item.name }}
                <span style="float:right;"> 
                  <span style="color:#1CFFF5;">{{ item.data }}</span> 个
                </span>
              </li>
            </ul>
          </div>
        </div>
        <!-- 研究类别统计 -->
        <div>
          <el-row>
            <el-col :span="7" v-for="(item, index) in amountData.rows" :key="index" style="margin: 10px 6px;">
              <div class="type-item suface-proj" 
                :style="{'border-left-style': 'solid','border-left-width': '3px', 'border-left-color': item.color}" 
                :class="index == 0 ? 'background-color-sty0' : index == 1 ? 'background-color-sty1' :index == 2 ? 'background-color-sty2' :index == 3 ? 'background-color-sty3' :index == 4 ? 'background-color-sty4' :'background-color-sty5'"
              >
                <div class="title" style="padding: 5px 10px;"> {{ item.name }} </div>
                <div class="label" style="padding: 5px 10px;"> <span :style="{'font-weight': 800,color:item.color}">{{ item.data }}</span> 万元</div>
              </div>
            </el-col>
          </el-row>
        </div>
      <table-title title="承担单位" />
      <div class="project-type">
        <ve-ring
          height="130px"
          width="160px"
          v-bind="prjectOption"
          :data="undertakUnitList"
          :tooltip-visible="false"
          :legend-visible="false"
          :after-config="afterProjectConfig"
        />
        <div class="project-type-legend">
          <ul class="list">
            <li v-for="(item, index) in undertakUnitList.rows" :key="index" class="list-item">
              <i class="dot" :style="{'backgroundColor': item.color}" />
              {{ item.name }}
            </li>
          </ul>
        </div>
      </div>
      <table-title title="成果转化" />
      <div style="height:calc(100% - 580px)">
        <ve-histogram
          v-bind="finishRateOption"
          ref="chart"
          height="100%"
          width="100%"
          :data="tranformData"
          :tooltip-visible="false"
          :legend-visible="false"
          :after-config="afterFinishRateConfig"
        />
      </div>
    </panel-item>
  </div>
</template>
<script>
import PanelItem from "../components/PanelItem.vue";
import { programmeApi } from "@/api/programme_api/index.js";
import { mapActions } from "vuex";
import {
  genBodyLinearGradients,
  generateSeries,
  generateDataZoom,
  genNormalLinearGradients,
} from "./utils/chart.js";
import { TableTitle } from "./CardPages/components.js";
export default {
  name: "WaterResearchCard",
  components: { TableTitle, PanelItem },
  data() {
    return {
      yearDropdownVisible: false,
      yearOption: [],
      year: '',
      title: "水研究专题",
      pointActive: false,
      finishRateOption: {
        grid: {
          top: 40,
          bottom: "2%",
          containLabel: true,
        },
        settings: {
          yAxisName: ["%"],
          itemStyle: { color: "#fff" },
          label: {
            show: true,
            // fontSize: 16,
            fontWeight: "bold",
            position: "top",
            color: "#fff",
            formatter: ({ value }) => Number(value).toFixed(1),
          },
        },
        extend: {
          "textStyle.color": "#fff",
          barWidth: 15,
          xAxis: {
            axisLabel: {
              interval: 0,
              // fontSize: "16",
              formatter: function (item) {
                return item.replace("水系", "");
              },
            },
          },
          yAxis: {
            nameTextStyle: {
              color: "#ffffff99",
              fontSize: this.$commonSetting.chartFontSize,
            },
            axisLine: { show: false },
            axisLabel: { color: "#fff", fontSize: this.$commonSetting.chartFontSize },
            splitLine: {
              lineStyle: {
                color: "#fff",
                opacity: 0.5,
              },
            },
            max: 100,
          },
        },
      },
      // 承担单位
      undertakUnitList: {
        columns: ["name", "data"],
        rows: []
      },
      // 研究类别
      resultData: {
        columns: ["name", "data"],
        rows: []
      },
      // 经费统计
      amountData: {
        columns: ["name", "data"],
        rows: []
      },
      // 成果转化
      tranformData: {
        columns: ["name", "data"],
        rows: []
      },
      prjectOption: {
        settings: { radius: [35, 65] },
        extend: {
          "textStyle.color": "#fff",
          series: {
            center: [80, "50%"],
            hoverAnimation: false,
            label: { show: false },
          },
        },
      },
    };
  },
  mounted() {
    this.$nextTick(()=>{
      this.statsOfWaterResearch();
    })
  },
  created() {
    this.getYearOption();
  },
  methods: {
    ...mapActions([
      "SetTrendAnalysis",
      "SetWaterResearchShow",
    ]),
    // 获取年份选择下拉内容
    getYearOption() {
      let date = new Date();
      let y = date.getFullYear();
      for (let i = y; i >= 1990; i--) {
        this.yearOption.push(i + " 年");
      }
      this.year = y + " 年";
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    // 年份选择
    yearChange() {
      this.$store.commit('yearChange',this.year);
      this.statsOfWaterResearch();
    },
    async statsOfWaterResearch() {
      let str = this.year == null ? '' : this.year.slice(0,4);
      await programmeApi.statsOfWaterResearch({year: str}).then((res) => {
        if (res.code == 0) {
          let colorArr = ['#19AF47','#008FE8','#E89100','#FF7144','#8A8AE1','#A80BEE'];
          // 承担单位
          let undertakUnit = [];
          res.data.undertakUnitList.forEach((val,i) => {
            undertakUnit.push({
              name: val.undertakUnit,
              data: Number(val.num),
              color: colorArr[i],
            })
          })
          this.undertakUnitList.rows = undertakUnit;
          // 研究类别
          let resultList = [];
          res.data.resultList.forEach((val,i) => {
            resultList.push({
              name: val.researchTypeName,
              data: val.data,
              color: colorArr[i],
            })
          })
          this.resultData.rows = resultList;
          // 经费统计
          let amountList = [];
          res.data.amountList.forEach((val,i) => {
            amountList.push({
              name: val.researchTypeName,
              data: val.data,
              color: colorArr[i],
            })
          })
          this.amountData.rows = amountList;
          // 成果转化
          let tranformList = [];
          res.data.tranformList.forEach((val,i) => {
            tranformList.push({
              name: val.researchTypeName,
              data: val.rate * 100,
              color: colorArr[i],
            })
          })
          this.tranformData.rows = tranformList;
        }
      })
    },
    onTitleChange(title) {},
    // 趋势分析弹窗
    trendAnalysis() {
      this.SetTrendAnalysis(true);
    },
    openDetailsDialog() {
      this.SetWaterResearchShow(true);
    },
    afterFinishRateConfig(options) {
      let data = options.series[0].data;
      let color = genBodyLinearGradients("#1EF8F2", "#50bfda");
      options.series = generateSeries({ data, color: color });
      options.dataZoom = generateDataZoom(5, data.length);
      return options;
    },
    afterProjectConfig(options) {
      let data = options.series[0].data;
      let count = data.map((i) => i.value).reduce((total, cur) => total + cur);

      options.title = {
        show: true,
        text: `{count|${count}}个\n总数`,
        textStyle: { color: "#fff", rich: { count: { fontSize: 22 } } },
        textAlign: "center",
        left: 75,
        top: "30%",
      };
      let colorArr = ['#19AF47','#008FE8','#E89100','#FF7144','#8A8AE1','#A80BEE'];
      options.color = colorArr.map((item) => {
        return genNormalLinearGradients(item, "#000");
      });
      return options;
    },
  },
};
</script>
<style lang="scss">
.water-research-card {
  position: relative;
  .background-color-sty0 {
    background-image: linear-gradient(90deg, rgba(25,175,71,0.4), rgba(255,255,255, 0.1));
  }
  .background-color-sty1 {
    background-image: linear-gradient(90deg, rgba(0,143,232,0.4), rgba(255,255,255, 0.1));
  }
  .background-color-sty2 {
    background-image: linear-gradient(90deg, rgba(232,145,0,0.4), rgba(255,255,255, 0.1));
  }
  .background-color-sty3 {
    background-image: linear-gradient(90deg, rgba(255,113,68,0.4), rgba(255,255,255, 0.1));
  }
  .background-color-sty4 {
    background-image: linear-gradient(90deg,  rgba(138,138,225,0.4), rgba(255,255,255, 0.1));
  }
  .background-color-sty5 {
    background-image: linear-gradient(90deg,  rgba(168,11,238,0.4), rgba(255,255,255, 0.1));
  }
  .trend-analysis-sty {
    position: absolute;
    color: #1CFFF5;
    top:54px;
    right: 0;
  }
  .page-title-select {
    background: #0000;
    font-size: 24px;
    font-weight: 600;
    padding: 0;
    line-height: 44px;
    width: 120px;
  }
  .el-dropdown {
    cursor: pointer;
    top: -2px;
    right: 10px;
    .el-dropdown-link {
      display: flex;
      align-items: center;
      &.dropdown-active {
        span,
        i {
          color: #1cfff5;
        }
      }
    }
  }
  .project-type {
    display: flex;
    height: 140px;
    flex-direction: row;
    .project-type-legend {
      padding-top: 5px;
      width: 100%;
      height: 140px;
      overflow-y: auto;
      &::-webkit-scrollbar-thumb:vertical {
        background-color: #1ef8f2;
      }

      .list {
        .list-item {
          padding: 5px 6px;
          font-size: 16px;
          color: #fff;
          font-weight: 500;
          position: relative;
          padding-left: 20px;
          .dot {
            content: "";
            position: absolute;
            left: 0;
            top: 50%;
            width: 12px;
            height: 12px;
            border-radius: 50%;
            transform: translateY(-50%);
          }
        }
      }
    }
  }
}
</style>
