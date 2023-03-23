<script>
import CardTabs from "../../../components/CardTabs.vue";
import PanelItem from "../../../components/PanelItem.vue";
import PrecipitionMaxList from "../../../components/PrecipitionMaxList.vue";
import CapsuleTabs from "../../../components/CapsuleTabs.vue";
import { forecastChartOption, forecastConfig } from "./DefenseModuleForecastChart.js";
import { mapActions, mapGetters } from "vuex";
export default {
  name: "DefenseModuleForecast",
  props: { cardSelection: { type: Boolean, default: false } },
  data() {
    return {
      chartTypeSelect: 0,
      cardSelect: 0,
    };
  },
  computed: {
    ...mapGetters(["forecastChartData", "forecastMaxList", "forecastByHour"]),
    chartData() {
      let hour = [1, 3, 6][this.cardSelect];
      let selectMap = ["area", "basin"];
      let type = selectMap[this.chartTypeSelect];
      let data = this.forecastChartData(type, hour);
      return {
        columns: ["name", "min", "max"],
        rows: data,
      };
    },
    precipitionMaxData() {
      let hour = [1, 3, 6][this.cardSelect];
      return this.forecastMaxList(hour);
    },
  },
  mounted() {
    this.getForecastChart();
    this.getForecastEachHour(1).then(() => {
      this.getForecastEachHour(3).then(() => {
        this.getForecastEachHour(6);
      });
    });
    this.getForecastData();
  },
  methods: {
    ...mapActions(["getForecastData", "getForecastChart", "getForecastEachHour"]),
    renderCarousel() {
      let maxChart = this.renderMaxChart();
      return (
        <el-carousel height="230px" trigger="click" arrow="never" autoplay={false}>
          <el-carousel-item>
            <PrecipitionMaxList maxList={this.precipitionMaxData} />
          </el-carousel-item>
          <el-carousel-item>{maxChart}</el-carousel-item>
        </el-carousel>
      );
    },
    renderMaxChart() {
      return [
        <div class="panel-item-subtitle">
          <span> 一小时面雨量</span>
          <CapsuleTabs
            value={this.chartTypeSelect}
            tabs={["行政区划", "流域"]}
            onChange={(value) => {
              this.chartTypeSelect = value;
            }}
          />
        </div>,
        <ve-histogram
          data={this.chartData}
          height="180px"
          settings={forecastChartOption.settings}
          extend={forecastChartOption.extend}
          grid={forecastChartOption.grid}
          afterConfig={forecastConfig}
          judgeWidth
        />,
      ];
    },
  },
  render() {
    let carousel = this.renderCarousel();
    return (
      <PanelItem title="短临预报" hasSwitch class="panel-foreacast">
        <template slot="title-switch">
          <i class="icon-liebiao icon-sty" onClick="openDetailsDialog('短临预报')" />
        </template>
        <CardTabs
          tabsActive={this.cardSelection}
          list={this.forecastByHour}
          value={this.cardSelect}
          onChange={(value) => {
            // console.log(value);
            this.$emit("change", value);
          }}
          onInput={(value) => {
            this.cardSelect = value;
            this.$emit("input", value);
          }}
        />
        {carousel}
      </PanelItem>
    );
  },
};
</script>
<style lang="scss">
.panel-foreacast {
  .el-carousel {
    margin-top: 10px;
  }
}
</style>
