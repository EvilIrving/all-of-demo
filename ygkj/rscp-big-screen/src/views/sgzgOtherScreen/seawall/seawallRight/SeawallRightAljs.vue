<!--
 * @Author: hanyu
 * @LastEditTime: 2021-07-21 15:23:02
 * @Description: 地图右侧-安澜建设
 * @FilePath: /rscp-big-screen/src/views/bigScreen/seawall/seawallRight/SeawallRightAljs.vue
-->
<template>
  <div class='wrap'>
    <pie-chart v-loading="sghtfbLoading" class="htfbChart" :chartData="sghtfbData" :legendShow="false" element-loading-background="rgba(0, 0, 0, 0.2)"></pie-chart>
    <section class="seawallList_box">
      <div class="chart_title">
        海塘列表
      </div>
      <div class="table_list" ref="tableContainer">
        <el-table :height="tableHeight" v-loading="tableLoading"  element-loading-background="rgba(0, 0, 0, 0.2)" :data="tableData" stripe style="width: 100%" @row-click="clickSeawall">
          <template slot="empty">
            <div class="table_nodata">
              <img :src="$config.swImgUrl + 'empty_data.png'" alt="" />
            </div>
          </template>
          <el-table-column prop="NAME" label="海塘名称"></el-table-column>
          <el-table-column prop="SEAWALL_LENGTH" label="海塘长度（m）" align="center"></el-table-column>
          <el-table-column label="海塘等级" align="center">
            <template slot-scope="scope">
              <div v-if="scope.row.project_scale == 1">一级</div>
              <div v-else-if="scope.row.project_scale == 2">二级</div>
              <div v-else-if="scope.row.project_scale == 3">三级</div>
              <div v-else-if="scope.row.project_scale == 4">四级</div>
              <div v-else-if="scope.row.project_scale == 5">五级</div>
              <div v-else-if="scope.row.project_scale == 6">六级</div>
              <div v-else>-</div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </section>
  </div>
</template>

<script>
import PieChart from "../components/PieChart";
import { seawallLeftStatisticApi } from "@/api/api_seawall";
export default {
  name: "",
  props: {},
  components: {
    PieChart
  },
  data() {
    return {
      sghtfbData: {
        title: "施工海塘分布",
        id: "sghtfbChart",
        unit: "",
        legend: {
          orient: "vertical",
          left: "60%",
          height: "70%"
        },
        color: [
          "#00c87b",
          "#539ff5",
          "#6e86a3",
          "#ffc425",
          "#ff7663",
          "#4fd3f0",
          "#aa84d4",
          "#ffa662",
          "#269A99"
        ],
        series: []
      },
      sghtfbLoading: true,
      tableData: [],
      tableLoading: true,
      tableHeight: 0,
      opt: {
        statistic: "mapList",
        itemCategory: "construction",
        itemValue: 1,
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd
      }
    };
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    leftSelectPreventLevel() {
      return this.$store.state.leftSelectPreventLevel;
    },
    leftSelectProjectScale() {
      return this.$store.state.leftSelectProjectScale;
    }
  },
  watch: {
    leftSelectAdcd(val) {
      this.opt.adcd = val ? val : this.$localData("get", "userInfo").adcd;
      if (val == "33") {
        this.opt.superviseLevel = 1;
      }
      this.getSeawallList();
    },
    leftSelectPreventLevel(val) {
      this.setOptions("preventLevel", val);
      this.getSeawallList();
    },
    leftSelectProjectScale(val) {
      this.setOptions("scale", val);
      this.getSeawallList();
    }
  },
  mounted() {
    this.$nextTick(() => {
      const height = this.$refs.tableContainer.clientHeight;
      this.tableHeight = height;
      window.onresize = () => {
        const height = this.$refs.tableContainer.clientHeight;
        this.tableHeight = height;
      };
    });
    if (this.leftSelectAdcd == "33") {
      this.opt.superviseLevel = 1;
    }
    if (this.leftSelectProjectScale) {
      this.setOptions("scale", this.leftSelectProjectScale);
    }
    this.getSghtfbData();
    this.getSeawallList();
  },
  methods: {
    setOptions(type, val) {
      if (val) {
        // val不为空
        if (this.opt.itemCategory) {
          if (this.opt.itemCategory.indexOf(type) == -1) {
            // 不存在该参数
            this.opt.itemCategory = this.opt.itemCategory + "," + type;
            this.opt.itemValue = this.opt.itemValue + "," + val;
          } else {
            // 已存在该参数 更新参数
            let itemArr = this.opt.itemCategory.split(",");
            let index = itemArr.indexValue(type);
            let valueArr = this.opt.itemValue.toString().split(",");
            valueArr[index] = val;
            this.opt.itemValue = valueArr.join(",");
          }
        } else {
          // 没有参数
          this.opt.itemCategory = type;
          this.opt.itemValue = val;
        }
      } else {
        // 为空
        if (this.opt.itemCategory) {
          if (this.opt.itemCategory.indexOf(type) != -1) {
            // 存在该参数 清空参数
            let itemArr = this.opt.itemCategory.split(",");
            let valueArr = this.opt.itemValue.toString().split(",");
            let index = itemArr.indexValue(type);
            itemArr.splice(index, 1);
            valueArr.splice(index, 1);
            this.opt.itemCategory = itemArr.join(",");
            this.opt.itemValue = valueArr.join(",");
          }
        }
      }
    },
    // 施工海棠分布
    async getSghtfbData() {
      this.sghtfbLoading = true;
      let res = await seawallLeftStatisticApi({
        statistic: "adcd",
        itemCategory: "construction",
        itemValue: 1,
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd
      });
      if (res.success) {
        if (res.data.length) {
          for (let i = 0; i < res.data.length; i++) {
            this.sghtfbData.series.push({
              name: res.data[i].adnm,
              value: res.data[i].num
            });
          }
        }
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.sghtfbLoading = false;
    },

    // 施工海棠分布列表
    async getSeawallList() {
      this.$parent.$refs.seawallMap.removeAllLayer();
      this.$parent.$refs.seawallMap.closePopup();
      this.$parent.$refs.seawallMap.initView();
      this.tableLoading = true;
      this.$store.commit("SET_SEAWALL_RIGTH_SELECT_FLAG", false);
      this.tableData = [];
      // let res = await seawallLeftStatisticApi(opt)
      let res = await seawallLeftStatisticApi(this.opt);
      if (res.success) {
        if (res.data.length) {
          this.tableData = res.data;
          let pointArr = [];
          let lineArr = [];
          res.data.map(v => {
            pointArr.push({
              lat: v.LTTD,
              lng: v.LGTD,
              NAME: v.NAME,
              PRCD: v.PRCD,
              rightType: "2",
              tm: v.tm,
              tide: v.tide,
              DESIGN_TIDE_LEVEL: v.DESIGN_TIDE_LEVEL,
              src: require("@/assets/images/map/seawall.png")
            });
            lineArr.push({
              line: v.COORPOT
                ? JSON.parse(
                    v.COORPOT.replace(new RegExp("&quot;", "gm"), '"')
                  )[0]
                : "",
              level: v.project_scale
            });
            v.lineArr = [];
            v.lineArr.push({
              line: v.COORPOT
                ? JSON.parse(
                    v.COORPOT.replace(new RegExp("&quot;", "gm"), '"')
                  )[0]
                : "",
              level: v.project_scale
            });
          });
          this.$parent.showMapPointJh(pointArr, "aljs");
          this.$parent.showMapLines(lineArr);
        }
      }
      this.tableLoading = false;
      this.$store.commit("SET_SEAWALL_RIGTH_SELECT_FLAG", true);
    },

    clickSeawall(row) {
      this.$parent.setView(row.LGTD, row.LTTD);
      row.lat = row.LTTD;
      row.lng = row.LGTD;
      row.rightType = "2";
      this.$parent.showOneSelectPoint([row]);
      setTimeout(() => {
        this.$parent.setPopup(row);
      }, 500);
    }
  }
};
</script>

<style lang='scss' scoped>
@import "../../style/common.scss";
.wrap {
  height: 100%;
  .htfbChart {
    width: 100%;
    height: 256px;
  }
  .seawallList_box {
    height: calc(100% - 256px);
    .chart_title {
      @include chartTitle();
    }
    .table_list {
      height: calc(100% - 50px);
      box-sizing: border-box;
      padding: 0 0 0 16px;
      margin-bottom: 8px;
      overflow: auto;
      @include changeElTable();
    }
  }
}
</style>