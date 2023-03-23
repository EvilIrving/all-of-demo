<!--
 * @Author: hanyu
 * @LastEditTime: 2021-08-09 16:53:36
 * @Description: 地图右侧-台风路径
 * @FilePath: /rscp-big-screen/src/views/bigScreen/reservoir/reservoirRight/ReservoirRightNxnl.vue
-->
<template>
  <div class="wrap">
    <!-- <div class="top_two_tabs">
      <div :class="[nxnlIndex == index ? 'active' : '', index === 0 ? 'left_tab' : '', index === 3 ? 'right_tab' : '']" v-for="(item, index) in nxnlList" :key="index" @click="tabClick(index)">
        {{item.name}}
      </div>
    </div> -->
    <div class="select_square">
      <div
        :class="[
          'square_item',
          nxnlIndex == index ? 'active' : '',
          index === 0 ? 'left_tab' : '',
          index === 3 ? 'right_tab' : '',
        ]"
        v-for="(item, index) in nxnlList"
        :key="index"
        @click="tabClick(index)"
      >
        <p>{{ item.name }}</p>
      </div>
    </div>
    <!-- <progress-list v-loading="progressLoading" title="水库纳蓄能力分布" unit="百万m³" class="progress" :progressData="progressData"></progress-list> -->
    <progress-list-custom
      v-loading="progressLoading"
      title="水库纳蓄能力分布"
      unit="百万m³"
      class="progress"
      :progressData="progressData"
    ></progress-list-custom>
    <section class="photo_list">
      <div class="chart_title">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        水库纳蓄能力列表
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
            label="水库名称"
            align="left"
          ></el-table-column>

          <el-table-column label="水位" width="120" align="center">
            <template slot-scope="scope">
              <div>
                {{
                  scope.row.waterLevel
                    ? scope.row.waterLevel.toFixed(2) + "m"
                    : "-"
                }}
                /
                <span v-if="nxnlIndex == 0">{{
                  scope.row.NWL ? scope.row.NWL.toFixed(2) + "m" : "-"
                }}</span>
                <span v-if="nxnlIndex == 1">{{
                  scope.row.FHLV ? scope.row.FHLV.toFixed(2) + "m" : "-"
                }}</span>
                <span v-if="nxnlIndex == 2">{{
                  scope.row.DSFLZ ? scope.row.DSFLZ.toFixed(2) + "m" : "-"
                }}</span>
                <span v-if="nxnlIndex == 3">{{
                  scope.row.CKFLV ? scope.row.CKFLV.toFixed(2) + "m" : "-"
                }}</span>
              </div>
            </template>
          </el-table-column>

          <!-- <el-table-column v-if="nxnlIndex == 0" label="正常蓄水位" align="center">
            <template slot-scope="scope">
              <div>{{scope.row.NWL ? (scope.row.NWL).toFixed(2) + 'm' : '-'}}</div>
            </template>
          </el-table-column>
          <el-table-column v-if="nxnlIndex == 1" label="防洪高水位" align="center">
            <template slot-scope="scope">
              <div>{{scope.row.FHLV ? (scope.row.FHLV).toFixed(2) + 'm' : '-'}}</div>
            </template>
          </el-table-column>
          <el-table-column v-if="nxnlIndex == 2" label="设计洪水位" align="center">
            <template slot-scope="scope">
              <div>{{scope.row.DSFLZ ? (scope.row.DSFLZ).toFixed(2) + 'm' : '-'}}</div>
            </template>
          </el-table-column>
          <el-table-column v-if="nxnlIndex == 3" label="校核洪水位" align="center">
            <template slot-scope="scope">
              <div>{{scope.row.CKFLV ? (scope.row.CKFLV).toFixed(2) + 'm' : '-'}}</div>
            </template>
          </el-table-column> -->
          <el-table-column label="可纳雨量" align="center">
            <template slot-scope="scope">
              <div>
                {{
                  scope.row.addtionRain
                    ? scope.row.addtionRain.toFixed(2) + "mm"
                    : "-"
                }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="可纳水量" align="center">
            <template slot-scope="scope">
              <div>
                {{
                  scope.row.addtion
                    ? scope.row.addtion.toFixed(2) + "万m³"
                    : "-"
                }}
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </section>
  </div>
</template>

<script>
import ProgressList from "@/components/ProgressList";
import ProgressListCustom from "@/components/ProgressListCustom";
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
export default {
  name: "",
  props: {},
  components: {
    ProgressList,
    ProgressListCustom,
  },
  data() {
    return {
      nxnlList: [
        {
          type: "CSCNWL",
          name: "正常蓄水位",
        },
        {
          type: "FCLWL",
          name: "防洪高水位",
        },
        {
          type: "DFCV",
          name: "设计洪水位",
        },
        {
          type: "EFCV",
          name: "校核洪水位",
        },
      ],
      nxnlIndex: 0,
      nxnlType: "CSCNWL",
      progressLoading: true,
      progressData: [],
      tableData: [],
      tableLoading: true,
      tableHeight: 0,
      listFlag: true,
      opt: {
        statistic: "mapList",
        itemCategory: "keyStr",
        itemValue: "FCLWL",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      },
    };
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    leftSelectProjectScale() {
      return this.$store.state.leftSelectProjectScale;
    },
  },
  watch: {
    leftSelectAdcd(val) {
      this.opt.adcd = val;
      this.getReservoirList();
    },
    leftSelectProjectScale(val) {
      this.setOptions("scale", val);
      this.getReservoirList();
    },
  },
  mounted() {
    this.$nextTick(function() {
      const height = this.$refs.tableContainer.clientHeight;
      this.tableHeight = height - 20;
      let _this = this;
      window.onresize = function() {
        const height = this.$refs.tableContainer.clientHeight;
        this.tableHeight = height - 20;
      };
    });
    this.getStatisticData();
    this.getReservoirList();
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

    tabClick(index) {
      if (this.listFlag) {
        this.nxnlIndex = index;
        this.nxnlType = this.nxnlList[index].type;
        this.opt.itemValue = this.nxnlType;
        this.getStatisticData();
        this.getReservoirList();
      }
    },
    clickReservoir(row) {
      this.$parent.setView(row.LGTD, row.LTTD);
      row.lat = row.LTTD;
      row.lng = row.LGTD;
      row.rightType = "3";
      this.$parent.showOneSelectPoint([row]);
      setTimeout(() => {
        row.rightType = "3";
        this.$parent.setPopup(row);
      }, 500);
    },
    async getStatisticData() {
      this.progressLoading = true;
      this.progressData = [];
      let res = await reservoirLeftStatisticApi({
        statistic: "waterQT",
        itemCategory: "keyStr",
        itemValue: this.nxnlType,
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      });
      if (res.success) {
        let maxarr = [];
        // for (let i = 0; i < res.data.length; i++) {
        //   maxarr.push(res.data[i].capable)
        // }
        // let max = Math.max(...maxarr)
        for (let i = 0; i < res.data.length; i++) {
          let fz = res.data[i].capable - res.data[i].currenty;
          if (fz < 0) {
            fz = 0;
          }
          let percentage = ((fz / res.data[i].capable) * 100).toFixed(0);

          this.progressData.push({
            title: res.data[i].adnm,
            nowNum: (res.data[i].capable / 100).toFixed(0),
            allNum: (fz / 100).toFixed(0),
            percentage: Number(percentage),
          });
        }
      }
      this.progressLoading = false;
    },
    async getReservoirList() {
      if (this.listFlag) {
        this.$parent.$refs.reservoirMap.removeAllLayer();
        this.$parent.$refs.reservoirMap.closePopup();
        this.$parent.$refs.reservoirMap.initView();
        this.tableLoading = true;
        this.$store.commit("SET_RESERVOIR_RIGTH_SELECT_FLAG", false);
        this.tableData = [];
        this.listFlag = false;
        // let res = await reservoirLeftStatisticApi(opt)
        let res = await reservoirLeftStatisticApi(this.opt);
        if (res.success) {
          let pointArr = [];
          if (res.data.length) {
            this.tableData = res.data;
            res.data.map((v) => {
              v.lat = v.LTTD;
              v.lng = v.LGTD;
              v.rightType = "3";
              v.src = require("@/assets/images/map/reservoir.png");
              pointArr.push(v);
            });
          }
          this.$parent.showMapPointJh(pointArr, "bxsk");
          // this.$parent.showMapPoints(pointArr)
        }
        this.listFlag = true;
        this.tableLoading = false;
        this.$store.commit("SET_RESERVOIR_RIGTH_SELECT_FLAG", true);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  height: 100%;
  .top_two_tabs {
    padding: 16px;
    width: 100%;
    box-sizing: border-box;
    @include flexbox();
    @include flexAC();
    @include flexJC(center);
    & > div {
      cursor: pointer;
      height: 66px;
      width: 25%;
      text-align: center;
      color: #000;
      font-size: 14px;
      font-weight: 500;
      line-height: 66px;
      background: #f8f8f8;
      border: 1px solid #f8f8f8;
    }
    .active {
      background: #dbe2f8;
      border: 1px solid #193bc4;
      color: #193bc4;
    }
    .left_tab {
      border-top-left-radius: 33px;
      border-bottom-left-radius: 33px;
    }
    .right_tab {
      border-top-right-radius: 33px;
      border-bottom-right-radius: 33px;
    }
  }
  .progress {
    height: 380px;
  }
  .photo_list {
    height: calc(100% - 450px);
    .table_list {
      height: calc(100% - 42px);
      box-sizing: border-box;
      padding-bottom: 16px;
      // height: 500px;
      overflow: auto;
    }
  }
  .select_square {
    height: 70px;
    box-sizing: border-box;
    @include flexbox();
    @include flexJC(space-between);
    @include flexflow(row wrap);
    padding: 16px;
    .square_item {
      box-sizing: border-box;
      width: 23%;
      height: 50px;
      text-align: center;
      background: #f8f8f8;
      border: 1px solid #d6d6d6;
      cursor: pointer;
      border-radius: 2px;
      &.square2 {
        width: 48%;
      }
      &.active {
        background: #193cc5 !important;
        border: 1px solid #193cc5 !important;
        & > p {
          color: #fff !important;
        }
      }
      & > p {
        line-height: 50px;
        font-size: 14px;
        color: #000;
      }
    }
  }
}
</style>
