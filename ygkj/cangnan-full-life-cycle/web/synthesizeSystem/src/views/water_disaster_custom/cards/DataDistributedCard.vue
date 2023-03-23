<template>
  <div class="data-distributed-card">
    <page-pane title="数据分布" @onTitle="() => {}">
      <template #append>
        <i class="icon-jump-to" @click="() => {}" />
      </template>
      <div class="custom-legend-chart">
        <ve-ring
          width="160px"
          height="180px"
          v-bind="distributedOption"
          :data="distributedData"
          :tooltip-visible="false"
          :legend-visible="false"
          :after-config="afterDistributedOption"
          :loading="distributedData.rows.length == 0"
        />
        <div class="custom-legend" style="height: 180px">
          <ul class="list">
            <li
              v-for="(item, index) in distributedData.rows"
              :key="index"
              class="list-item"
            >
              <i
                class="dot"
                :style="{ backgroundColor: afterDistributedColor[index] }"
              />

              <div class="legend-unit">
                <div>{{ item.name }}</div>
                <div>
                  <span>{{ item.data }}</span
                  >万条
                </div>
              </div>
            </li>
          </ul>
        </div>
      </div>
      <table-title title="水利基础数据">
        <template #append>
          <el-select
            v-model="basicTab"
            popper-class="styled-select"
            class="styled-select"
            @change="onCategoryChange"
          >
            <el-option
              v-for="(item, index) in basicTabs"
              :key="index"
              :label="item"
              :value="item"
            />
          </el-select>
        </template>
      </table-title>
      <div class="basic-data-wrap">
        <ve-bar
          v-bind="basicDataOption"
          :data="basicData"
          :tooltip-visible="false"
          :legend-visible="false"
          :after-config="basicConfig"
          height="100%"
        ></ve-bar>
      </div>
      <table-title title="水利实时数据" />
      <vue-seamless-scroll
        :data="RealtimeDataList"
        :classOption="{ singleHeight: 50, waitTime: 2000 }"
        class="realtime-scroll-list"
      >
        <div
          v-for="(item, index) in RealtimeDataList"
          :key="index"
          class="realtime-scroll-item"
        >
          <div class="category-name">
            <i class="icon-diamond" />{{ item.category }}
          </div>
          <span class="data">{{ item.dataNumber }}</span>
        </div>
      </vue-seamless-scroll>
    </page-pane>
  </div>
</template>
<script>
import "v-charts/lib/style.css";
import { TableTitle } from "./CardPages/components.js";
import { PagePane, PagePaneItem } from "../components/PagePane";
import { genNormalLinearGradients, generateDataZoom, wrapFormatter } from "./utils/chart.js";
import { getTotalStatistic, getRealtimeData, getBasicData } from "@/api/data-management/";
import vueSeamlessScroll from "vue-seamless-scroll";

export default {
  name: "DataDistributedCard",
  components: { TableTitle, PagePane, PagePaneItem, vueSeamlessScroll },
  data() {
    return {
      RealtimeDataList: [],
      distributedOption: {
        grid: {
          top: 40,
          bottom: "2%",
          containLabel: true
        },
        settings: { radius: [45, 75] },
        extend: {
          "textStyle.color": "#fff",
          series: {
            center: [80, "50%"],
            hoverAnimation: false,
            label: { show: false }
          }
        }
      },
      distributedData: {
        columns: ["name", "data"],
        rows: []
      },
      afterDistributedColor: [],
      basicTabs: [],
      basicTab: "",
      basicDataOption: {
        grid: {
          top: 0,
          bottom: 0,
          left: 10,
          right: 80,
          containLabel: true
        },
        settings: {
          itemStyle: {
            color: "#1ef8f2",
            barBorderRadius: 20
          },
          label: {
            show: true,
            fontSize: this.$commonSetting.chartFontSize,
            fontWeight: "bold",
            position: ["120%", "0%"],
            color: "#1ef8f2"
          }
        },
        extend: {
          xAxis: { 
						show: false, 
						axisLabel: { 
							fontSize: this.$commonSetting.chartFontSize
						} 
					},
          yAxis: {
            inverse: true,
            axisLine: { show: false },
            axisLabel: {
              width: 20,
              interval: 0,
              inside: false,
              color: "#ffffff",
              fontWeight: 500,
              fontSize: this.$commonSetting.chartFontSize,
              align: "right",
              formatter: p => wrapFormatter(p, 4)
            }
          },
          series: {
            showBackground: true,
            backgroundStyle: {
              barBorderRadius: 20
            },
            barWidth: 12
          }
        }
      },
      basicData: {
        columns: ["name", "data"],
        rows: []
      }
    };
  },
  mounted() {
    this.basicData.rows = [
      { name: "水库", data: 256 },
      { name: "山塘", data: 171 },
      { name: "堤防", data: 89 },
      { name: "海塘", data: 31 },
      { name: "水电站", data: 17 },
      { name: "泵站", data: 17 }
    ];
    this.getTotalStatistic();
    this.getRealtimeData();
    this.getBasicData().then(ret => {
      this.basicTabs = ret.data.map(item => item.category);
      this.basicTab = this.basicTabs[0];
      this.onCategoryChange(this.basicTab);
    });
  },
  methods: {
    basicConfig(options) {
      let data = options.series[0].data;
      options.dataZoom = generateDataZoom(6, data.length, { axis: "y", index: 0 });
      return options;
    },
    afterDistributedOption(options) {
      this.afterDistributedColor = options.color;
      let data = options.series[0].data;
      let count = 0;
      if (data.length > 0) count = data.map(i => i.value).reduce((total, cur) => total + cur);
      options.title = {
        show: true,
        text: `{count|${count}}个\n总数`,
        textStyle: { color: "#fff", rich: { count: { fontSize: 22 } } },
        textAlign: "center",
        left: 75,
        top: "35%"
      };
      options.color = options.color.map(item => {
        return genNormalLinearGradients(item, "#000");
      });
      return options;
    },
    getTotalStatistic() {
      getTotalStatistic().then(res => {
        if (res.code == 0) {
          this.distributedData.rows = res.data.map(item => ({
            name: item.category,
            data: Math.floor(item.dataNumber / 1000)
          }));
        }
      });
    },
    getRealtimeData() {
      getRealtimeData().then(res => {
        if (res.code == 0) {
          this.RealtimeDataList = res.data;
        }
      });
    },
    async getBasicData(name = "基础数据") {
      const res = await getBasicData({ name });
      if (res.code == 0) {
        return res;
      }
    },
    async onCategoryChange(name) {
      let res = await this.getBasicData(name);
      if (res.code == 0) {
        this.basicData.rows = res.data.map(item => ({ name: item.category, data: item.dataNumber }));
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.data-distributed-card {
  .basic-data-wrap {
    height: calc(100% - 570px);
  }
  .custom-legend-chart {
    display: flex;
    flex-direction: row;
    .custom-legend {
      width: 210px;
      height: 100%;
      overflow-y: scroll;
      display: flex;
      align-items: center;
      &::-webkit-scrollbar-thumb:vertical {
        background-color: #1ef8f2;
      }
      .list {
        width: 100%;
        .list-item {
          cursor: pointer;
          line-height: 30px;
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
          .legend-unit {
            display: flex;
            justify-content: space-between;
            > div:nth-child(2) {
              > span {
                font-size: 20px;
                color: #1cfff5;
              }
            }
          }
        }
      }
    }
  }
  .realtime-scroll-list {
    padding: 0 8px;
    height: 280px;
    overflow: hidden;
    .realtime-scroll-item {
      height: 40px;
      background: #00c1ff33;
      display: flex;

      justify-content: space-between;
      align-items: center;
      padding-left: 8px;
      padding-right: 8px;
      margin-bottom: 10px;
      .category-name {
        display: inline-flex;
        align-items: center;
        > i {
          margin-right: 8px;
        }
      }
      .data {
        color: #1ef8f2;

        &::after {
          content: "条";
          color: #fff;
          font-size: 14px;
          margin-left: 6px;
        }
      }
    }
  }
}
</style>
<style lang="scss">
.el-select.styled-select {
  width: 200px;
  .el-input {
    .el-input__inner {
      background: transparent;
      text-align: right;
      height: unset;
      line-height: 1;
    }
    .el-input__suffix {
      .el-input__icon {
        line-height: 16px;
      }
    }
  }
}
.el-select-dropdown.el-popper.styled-select {
  margin: 0;
  border-color: #1cfff5;
  background: #0741778e;
  .popper__arrow {
    display: none;
  }
  .el-scrollbar {
    .el-select-dropdown__wrap {
      max-height: 180px;
      .el-select-dropdown__list {
        .el-select-dropdown__item {
          color: #fff;
          &:hover,
          &.hover {
            background-color: #26767142;
          }
        }
      }
    }
  }
}
</style>
