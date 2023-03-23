<!--
 * @Author: hanyu
 * @LastEditTime: 2022-08-05 18:36:25
 * @Description: 地图右侧-潮位预警
 * @FilePath: \rscp-big-screen\src\views\rscpDriveScreen\seawall\seawallRight\SeawallRightDefault.vue
-->
<template>
  <div class='wrap'>
    <section class="seawallList_box">
      <div class="chart_title">
        海塘分布
        <div class="chart_clean" v-if="canExport" @click="exportList()">导出</div>
      </div>
      <div class="table_list" ref="tableContainer">
        <el-table :height="tableHeight" v-loading="tableLoading" element-loading-background="rgba(0, 0, 0, 0.2)" :data="tableData" stripe style="width: 100%" @row-click="clickSeawall">
          <template slot="empty">
            <div class="table_nodata">
              <img :src="$config.swImgUrl + 'empty_data.png'" alt="" />
            </div>
          </template>
          <el-table-column prop="NAME" label="海塘名称"></el-table-column>
          <el-table-column prop="DESIGN_TIDE_LEVEL" label="设计潮位" align="center">
            <template slot-scope="scope">
              <div>{{scope.row.DESIGN_TIDE_LEVEL ? scope.row.DESIGN_TIDE_LEVEL + 'm' : '-'}}</div>
            </template>
          </el-table-column>
          <el-table-column label="设计标准" align="center">
            <template slot-scope="scope">
              <div>{{scope.row.DESIGN_TIDE_STANDARD ? scope.row.DESIGN_TIDE_STANDARD + '年' : '-'}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="tide" label="实时潮位" align="center">
            <template slot-scope="scope">
              <div>{{scope.row.tide ? scope.row.tide + 'm' : '-'}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="tm" label="时间" align="center">
            <template slot-scope="scope">
              <div>{{scope.row.tm ? scope.row.tm.slice(0, 16) : '-'}}</div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </section>
  </div>
</template>

<script>
import { seawallLeftStatisticApi } from "@/api/api_seawall";

export default {
  name: "",
  props: {},
  components: {},
  data() {
    return {
      cwFlag: "1",
      seawallList: [],
      seawallAd: "",
      adOptions: [],
      htfbData: {
        title: "预警涉及海塘分布",
        id: "htfbChart",
        xData: [],
        yData: [],
        unit: "条"
      },
      htfbLoading: true,
      tableData: [],
      tableHeight: 0,
      tableLoading: true,
      squareList: [],
      squareIndex: "",
      squareValue: "total",
      loading_square: true,
      seawallFangan: "",
      fanganOptions: [
        {
          label: "暂无方案",
          value: ""
        }
      ],
      listFlag: true,
      opt: {
        statistic: "mapList",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd
      },
      canExport: false
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
    this.opt.adcd = this.leftSelectAdcd
      ? this.leftSelectAdcd
      : this.$localData("get", "userInfo").adcd;
    if (this.leftSelectAdcd == "33") {
      this.opt.superviseLevel = 1;
    }
    if (this.leftSelectProjectScale) {
      this.setOptions("scale", this.leftSelectProjectScale);
    }
    this.getSeawallList();
    this.resizeTable();
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
    clickSeawall(row) {
      this.$parent.setView(row.LGTD, row.LTTD);
      row.lat = row.LTTD;
      row.lng = row.LGTD;
      row.rightType = "4";
      this.$parent.showOneSelectPoint([row]);
      setTimeout(() => {
        row.rightType = "4";
        this.$parent.setPopup(row);
      }, 500);
    },
    async getSeawallList() {
      if (this.listFlag) {
        this.$parent.$refs.seawallMap.removeAllLayer();
        this.$parent.$refs.seawallMap.closePopup();
        this.$parent.$refs.seawallMap.initView();
        this.$store.commit("SET_SEAWALL_RIGTH_SELECT_FLAG", false);
        this.listFlag = false;
        this.tableData = [];
        this.tableLoading = true;
        this.canExport = false;
        let res = await seawallLeftStatisticApi(this.opt);
        if (res.success) {
          let pointArr = [];
          let lineArr = [];
          if (res.data.length) {
            this.canExport = true;
            this.tableData = res.data;
            res.data.map(v => {
              pointArr.push({
                lat: v.LTTD,
                lng: v.LGTD,
                NAME: v.NAME,
                PRCD: v.PRCD,
                rightType: "4",
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
          }
          this.$parent.showMapPointJh(pointArr, "default");
          this.$parent.showMapLines(lineArr);
        }
        this.listFlag = true;
        this.tableLoading = false;
        this.$store.commit("SET_SEAWALL_RIGTH_SELECT_FLAG", true);
      }
    },
    resizeTable() {
      this.$nextTick(() => {
        const height = this.$refs.tableContainer.clientHeight;
        this.tableHeight = height;
        window.onresize = () => {
          const height = this.$refs.tableContainer.clientHeight;
          this.tableHeight = height;
        };
      });
    },
    exportList() {
      // let adcd = this.leftSelectAdcd
      //   ? this.leftSelectAdcd
      //   : this.$localData("get", "userInfo").adcd;
      let token = this.$localData("get", "token");
      console.log(this.opt, "-----");
      window.open(
        this.$config.host +
          `/mgt/bm/seawallei/toExcel` +
          `?adcd=${this.opt.adcd}` +
          `&statistic=mapList` +
          `&superviseLevel=${this.opt.superviseLevel}` +
          `&itemCategory=${this.opt.itemCategory}` +
          `&itemValue=${this.opt.itemValue}` +
          `&token=${token}`
      );
    }
  }
};
</script>

<style lang='scss' scoped>
@import "../../style/common.scss";
.wrap {
  height: 100%;
  .select_title {
    padding: 16px 16px 10px 16px;
    @include flexbox();
    @include flexAC();
    @include flexJC(space-between);
    & > div {
      @include flexbox();
      @include flexAC();
      color: #000;
      font-weight: 500;
      font-size: 16px;
      img {
        margin-right: 5px;
      }
    }
  }
  .seawallList_box {
    height: 100%;
    .chart_title {
      @include chartTitle();
      .chart_clean {
        @include chartExport();
      }
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
.htfbChart {
  width: 100%;
  height: 210px;
}
</style>