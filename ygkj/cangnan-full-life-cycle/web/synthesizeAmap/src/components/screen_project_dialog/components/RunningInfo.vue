<script>
import { PrimaryMapTab, PrimaryMapTable, PrimarySubhead } from "@/components";
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
import "v-charts/lib/style.css";

let chartOptions = {
  extend: {
    textStyle: { color: "#fff" },
    legend: {
      icon: "rect",
      itemWidth: 8,
      itemHeight: 4,
      right: 0,
      bottom: 0,
      textStyle: {
        //图例文字的样式
        color: "#fff",
        fontSize: 12,
      },
    },
    series(v) {
      v && v.forEach((i) => (i.barGap = "0%"));
      return v;
    },
  },
  colors: ["#32BCB4"],
  settings: {
    showLine: ["水位曲线"],
  },
  grid: {
    top: 20,
    bottom: 30,
    left: 0,
    right: 0,
  },
  markLine: {
    data: [
      {
        yAxis: 30,
      },
    ],
    lineStyle: {
      normal: {
        color: "red", // 这儿设置安全基线颜色
      },
    },
    label: {
      normal: {
        position: "middle",
        formatter: "超汛限水位", // 这儿设置安全基线
      },
    },
  },
};
export default {
  name: "RunningInfo",
  props: { type: { type: String, default: "" }, stationCode: { type: String } },
  data() {
    return {
      listTabArr: [
        {
          value: 0,
          label: "数据列表",
        },
      ],
      time: 7,
      tableData: [],
      chartData: {
        columns: [],
        rows: [],
      },
    };
  },
  mounted() {
    this.getWaterStationInfo();
  },
  methods: {
    renderCurve() {
      const timeOptions = [
        { value: 1, label: "前一天" },
        { value: 2, label: "前二天" },
        { value: 3, label: "前三天" },
        { value: 4, label: "前四天" },
        { value: 7, label: "前一周" },
        { value: 14, label: "前二周" },
      ];
      this.chartData.columns = ["时间", "水位"];
      if (this.type == "水闸") {
        this.chartData.columns = ["时间", "闸上水位", "闸下水位"];
      }
      return (
        <div class="running-info-curve">
          <PrimarySubhead text="水位曲线">
            <el-select
              value={this.time}
              class="primary-select"
              onChange={(val) => {
                this.time = val;
                this.getWaterStationInfo();
              }}
              placeholder="请选择时间"
              style="width: 126px"
            >
              {timeOptions.map((item) => (
                <el-option label={item.label} value={item.value} />
              ))}
            </el-select>
          </PrimarySubhead>
          <ve-line
            {...{
              props: {
                ...chartOptions,
                data: this.chartData,
                dataEmpty: this.chartData.rows.length == 0,
              },
            }}
          />
        </div>
      );
    },
    renderTable() {
      let columnList = [
        { width: 160, prop: "时间", label: "时间" },
        { prop: "水位", label: "水位(m)" },
      ];
      if (this.type == "水闸")
        columnList = [
          { width: 160, prop: "时间", label: "时间" },
          { prop: "闸上水位", label: "闸上水位(m)" },
          { prop: "闸下水位", label: "闸下水位(m)" },
          { prop: "过闸流量", label: "过闸流量(m³/s)" },
        ];
      return (
        <div class="running-info-table">
          <PrimaryMapTab
            ref="listTab"
            tabArr={this.listTabArr}
            tabName=""
            onTabChange={() => {}}
          />
          <div class="table-box">
            <PrimaryMapTable columnList={columnList} dataList={this.tableData} />
          </div>
        </div>
      );
    },
    getWaterStationInfo() {
      const opt = {
        stationCode: this.stationCode,
        stationType: this.type == "水库" ? "RR" : "DD",
        days: this.time,
      };
      if (opt.stationCode == "") return;
      hydraulicApi.waterStationInfo(opt).then((res) => {
        if (res.code == 0) {
          this.dataList = res.data.data.map((item) => {
            item.rz = item.rz || item.z || item.tdz;
            return item;
          });
          let info = JSON.parse(JSON.stringify(res.data.data)).reverse();
          this.tableData = info;
          if (this.type == "水库") {
            // 水库
            chartOptions.markLine.label.normal.formatter = "汛限水位";
            chartOptions.markLine.data[0].yAxis = res.data.limitLevel;
          } else if (this.type == "水闸") {
            // 闸坝
            chartOptions.markLine.label.normal.formatter = "警戒水位";
            chartOptions.markLine.data[0].yAxis = res.data.warningLevel; // 警戒水位
          }
          info.map((item) => {
            item["时间"] = item.tm;
            item["水位"] = item.rz || item.z || item.tdz;
            item["闸上水位"] = item.upz;
            item["闸下水位"] = item.dwz;
            item["过闸流量"] = item.ll;
            return item;
          });
          this.chartData.rows = info;
        }
      });
    },
  },
  render() {
    const renderTable = this.renderTable();
    const renderCurve = this.renderCurve();
    return (
      <div class="running-info">
        {renderCurve}
        {renderTable}
      </div>
    );
  },
};
</script>
<style lang="scss">
.running-info {
  display: flex;
  height: 450px;
  .running-info-curve {
    width: 67%;
    margin-right: 8px;
  }
  .running-info-table {
    width: 33%;
    display: flex;
    flex-direction: column;
  }
  .primary-map-tab {
    margin: 0;
    margin-bottom: 8px;
  }
  .subhead-wrap {
    margin: 0;
  }
  .table-box {
    width: 100%;
    flex: 1;
    display: flex;
    flex-flow: column wrap;
    position: relative;
    overflow-y: auto;
  }
}
</style>
