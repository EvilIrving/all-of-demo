<!--
 * @Author: hanyu
 * @LastEditTime: 2021-10-28 17:42:39
 * @Description: 地图右侧-水库分布
 * @FilePath: /rscp-big-screen/src/views/bigScreen/reservoir/reservoirRight/ReservoirRightDefault.vue
-->
<template>
  <div class="wrap">
    <section class="reservoirList_box">
      <div class="chart_title" style="height:25px">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        水库分布
        <el-button
          v-if="canExport"
          style="float:right"
          plain
          size="small"
          @click="exportList()"
          >导出</el-button
        >
      </div>
      <div class="table_list" ref="tableContainer">
        <el-table
          :height="tableHeight"
          v-loading="tableLoading"
          :data="tableData"
          stripe
          style="width: 100%"
          @row-click="clickReservoir"
        >
          <template slot="empty">
            <div class="table_nodata">
              <img src="../../../../assets/images/noData.png" alt="" />
            </div>
          </template>
          <el-table-column
            prop="NAME"
            width="60px"
            label="水库名称"
          ></el-table-column>
          <el-table-column label="安全等级" align="center">
            <template slot-scope="scope">
              <div>{{ scope.row.status ? scope.row.status : "-" }}</div>
            </template>
          </el-table-column>
          <el-table-column label="水位" align="center">
            <template slot-scope="scope">
              <div>
                {{ scope.row.waterLevel ? scope.row.waterLevel + "m" : "-" }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="时间" align="center">
            <template slot-scope="scope">
              <div>
                {{
                  scope.row.waterleveltm
                    ? scope.row.waterleveltm.slice(0, 16)
                    : "-"
                }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="规模" align="center">
            <template slot-scope="scope">
              <div v-if="scope.row.project_scale == '1'">大(1)型</div>
              <div v-if="scope.row.project_scale == '2'">大(2)型</div>
              <div v-if="scope.row.project_scale == '3'">中型</div>
              <div v-if="scope.row.project_scale == '4'">小(1)型</div>
              <div v-if="scope.row.project_scale == '5'">小(2)型</div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </section>
  </div>
</template>

<script>
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
import { drawPoint } from "@/utils/mapUtil";

export default {
  name: "",
  props: {},
  components: {
  },
  data() {
    return {
      cwFlag: "1",
      reservoirList: [],
      reservoirAd: "",
      adOptions: [],
      htfbData: {
        title: "预警涉及水库分布",
        id: "htfbChart",
        xData: [],
        yData: [],
        unit: "条",
      },
      htfbLoading: true,
      tableData: [],
      tableHeight: 0,
      tableLoading: true,
      squareList: [],
      squareIndex: "",
      squareValue: "total",
      loading_square: true,
      reservoirFangan: "",
      fanganOptions: [
        {
          label: "暂无方案",
          value: "",
        },
      ],
      listFlag: true,
      opt: {
        statistic: "mapList",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      },
      canExport: false,
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
    },
  },
  watch: {
    leftSelectAdcd(val) {
      this.opt.adcd = val ? val : this.$localData("get", "userInfo").adcd;
      if (val == "33") {
        this.opt.superviseLevel = 1;
      }
      this.getReservoirList();
    },
    leftSelectPreventLevel(val) {
      this.setOptions("preventLevel", val);
      this.getReservoirList();
    },
    leftSelectProjectScale(val) {
      this.setOptions("scale", val);
      this.getReservoirList();
    },
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
    this.getReservoirList();
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

    async getReservoirList() {
      if (this.listFlag) {
        this.$parent.$refs.reservoirMap.removeAllLayer();
        this.$parent.$refs.reservoirMap.closePopup();
        this.$parent.$refs.reservoirMap.initView();
        this.$store.commit("SET_RESERVOIR_RIGTH_SELECT_FLAG", false);
        this.listFlag = false;
        this.tableData = [];
        this.tableLoading = true;
        this.canExport = false;
        let res = await reservoirLeftStatisticApi(this.opt);
        if (res.success) {
          let pointArr = [];
          if (res.data.length) {
            this.canExport = true;
            this.tableData = res.data;
            res.data.map((v) => {
              v.lat = v.LTTD;
              v.lng = v.LGTD;
              v.rightType = "1";
              v.src = require("@/assets/images/map/reservoir.png");
              pointArr.push(v);
              // if(v.NAME == '横锦水库'){
              //   pointArr.unshift(v)
              // }
            });
          }
          // this.$parent.showMapPointJh(pointArr, "default");
          this.$parent.showMapPoints(pointArr);
        }
        this.listFlag = true;
        this.tableLoading = false;
        this.$store.commit("SET_RESERVOIR_RIGTH_SELECT_FLAG", true);
      }
    },

    clickReservoir(row) {
      this.$parent.setView(Number(row.LGTD) + 0.004, Number(row.LTTD) + 0.013);
      row.lat = row.LTTD;
      row.lng = row.LGTD;
      row.rightType = "1";
      this.$parent.showOneSelectPoint([row]);
      setTimeout(() => {
        row.rightType = "1";
        this.$parent.setPopup(row);
      }, 500);
    },

    resizeTable() {
      this.$nextTick(function() {
        const height = this.$refs.tableContainer.clientHeight;
        this.tableHeight = height - 20;
        let _this = this;
        window.onresize = function() {
          const height = this.$refs.tableContainer.clientHeight;
          this.tableHeight = height - 20;
        };
      });
    },
    exportList() {
      let adcd = this.leftSelectAdcd
        ? this.leftSelectAdcd
        : this.$localData("get", "userInfo").adcd;
      let token = this.$localData("get", "token");
      window.open(
        this.$config.host +
          `/mgt/bm/reservoirei/toExcel` +
          `?adcd=${this.opt.adcd}` +
          `&statistic=mapList` +
          `&superviseLevel=${this.opt.superviseLevel}` +
          `&itemCategory=${this.opt.itemCategory}` +
          `&itemValue=${this.opt.itemValue}` +
          `&token=${token}`
      );
    },
  },
};
</script>

<style lang="scss" scoped>
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
  .reservoirList_box {
    height: 100%;
    .table_list {
      height: calc(100% - 42px);
      box-sizing: border-box;
      padding-bottom: 16px;
      // height: 500px;
      overflow: auto;
    }
  }
}
.htfbChart {
  width: 100%;
  height: 210px;
}
</style>
<style lang="scss">
.select_title {
  .el-radio {
    color: #000;
    font-size: 16px;
  }
  .el-radio__input.is-checked + .el-radio__label {
    color: #000;
  }
  .el-radio__input.is-checked .el-radio__inner {
    border-color: #1341c5;
    background: #1341c5;
  }
  .el-select .el-input__inner {
    height: 32px;
  }
  .el-input__icon {
    line-height: unset;
  }
}
.fangan_select {
  padding: 0 16px 16px 16px;
  .el-select {
    width: 100%;
    // height: 32px;
  }
}
</style>
