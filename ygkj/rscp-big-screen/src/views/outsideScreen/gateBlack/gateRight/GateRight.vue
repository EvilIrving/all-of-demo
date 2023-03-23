<!--
 * @Author: hanyu
 * @LastEditTime: 2022-03-28 16:21:18
 * @Description: 流域大屏右侧-闸站
 * @FilePath: /rscp-big-screen/src/views/outsideScreen/gateBlack/gateRight/GateRight.vue
-->
<template>
  <div class="wrap" ref="blackRightWrap">
    <section class="cxjg_box" ref="cxjgBox">
      <div class="chart_title_black">
        除险加固
      </div>
      <div class="status_list" v-loading="loading_status">
        <div
          :class="['status_item', index === statusIndex ? 'active' : '']"
          v-for="(item, index) in statusList"
          :key="index"
          @click="clickStatus(index)"
        >
          <img :src="item.src" alt="" />
          <div class="status_right">
            <p>{{ item.name }}</p>
            <span>{{ item.value ? item.value : 0 }}<i>座</i></span>
          </div>
        </div>
      </div>
    </section>
    <section class="reservoirList_box" :style="{height: tableHeight}">
      <div class="chart_title_black">
        闸站列表
      </div>
      <div class="table_list_diy">
        <table>
          <thead>
            <tr>
              <td style="width:50%">闸站名称</td>
              <td style="width:25%">规模</td>
              <td style="width:25%">行政区划</td>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in tableData" :key="index" @click="clickReservoir(item)">
              <td style="width:50%">{{item.NAME}}</td>
              <td style="width:25%">{{item.project_scale ? item.project_scale.toString().searchSacle() : '-'}}</td>
              <td style="width:25%">{{item.cityADNM ? item.cityADNM : "-"}}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  </div>
</template>

<script>
import { basinStatisticApi } from "@/api/api_basin";
export default {
  name: "",
  props: {
    safeStatus: String
  },
  components: {
  },
  data() {
    return {
      tableData: [],
      tableLoading: true,
      tableHeight: 0,
      opt: {
        statistic: "mapList",
        itemCategory: "",
        itemValue: "",
        basin: "",
        projectCategory: '闸站',
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      },
      listFlag: true,
      adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData("get", "userInfo").adcd,
      superviseLevel: "",

      scale: "all",

      loading_status: true,
      statusList: [],
      statusIndex: "",
      statusValue: "",
    };
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    leftSelectBasin() {
      return this.$store.state.leftSelectBasin;
    },
    projectLeftScale() {
      return this.$store.state.projectLeftScale;
    },
  },
  watch: {
    safeStatus(val){
      this.setOptions('safeStatus', this.safeStatus)
      this.getStatusData();
      this.getReservoirList();
    },
    leftSelectAdcd(val) {
      this.opt.adcd = val ? val : this.$localData("get", "userInfo").adcd;
      if (val == "33") {
        this.opt.superviseLevel = 1;
        this.superviseLevel = 1;
      } else {
        this.opt.superviseLevel = "";
        this.superviseLevel = "";
      }
      this.adcd = val;
      this.getStatusData();
      this.getReservoirList();
    },
    leftSelectBasin(val) {
      this.getStatusData();
      this.getReservoirList();
    },
    projectLeftScale(val){
      this.scale = val
      this.setOptions('scale', this.scale)
      this.getStatusData();
      this.getReservoirList();
    }
  },
  mounted() {
    if (this.leftSelectAdcd == "33") {
      this.opt.superviseLevel = 1;
      this.superviseLevel = 1;
    }
    if (this.leftSelectAdcd) {
      this.opt.adcd = this.leftSelectAdcd;
      this.adcd = this.leftSelectAdcd;
    }
    this.setOptions('safeStatus', this.safeStatus)
    this.getStatusData();
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
          if (this.opt.itemCategory.indexOf(type) !== -1) {
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

    // 除险加固情况
    async getStatusData() {
      this.loading_status = true;
      let res = await basinStatisticApi({
        statistic: "safeStatus",
        adcd: this.adcd,
        basin: this.leftSelectBasin,
        projectCategory: '闸站',
        itemCategory: "safeStatus" + (this.scale ? ',scale' : ''),
        itemValue: this.safeStatus + ',' + this.scale,
        superviseLevel: this.superviseLevel
      });
      if (res.success) {
        switch(this.safeStatus){
          case 'fourthCnt':
            this.statusList = [
              {
                name: "未加固",
                value: res.data[0].status1 ? res.data[0].status1 : 0,
                src: require("@/assets/images/black_wjg.png"),
              },
              {
                name: "拟降等报废",
                value: res.data[0].status4 ? res.data[0].fourthdegraded : 0,
                src: require("@/assets/images/black_njdbf.png"),
              },
            ];
          break;
          case 'thirdCnt':
            this.statusList = [
              {
                name: "未加固",
                value: res.data[0].status1 ? res.data[0].status1 : 0,
                src: require("@/assets/images/black_wjg.png"),
              },
              {
                name: "拟降等报废",
                value: res.data[0].status4 ? res.data[0].thirddegraded : 0,
                src: require("@/assets/images/black_njdbf.png"),
              },
            ];
          break;
          case 'secondCnt':
            this.statusList = [
              {
                name: "未加固",
                value: res.data[0].status1 ? res.data[0].status1 : 0,
                src: require("@/assets/images/black_wjg.png"),
              },
              {
                name: "拟降等报废",
                value: res.data[0].status4 ? res.data[0].seconddegraded : 0,
                src: require("@/assets/images/black_njdbf.png"),
              },
            ];
          break;
          case 'processCnt':
            this.statusList = [
              {
                name: "四类闸站加固中",
                value: res.data[0].thirdprocess ? res.data[0].fourthprocess : 0,
                src: require("@/assets/images/black_jgz.png"),
              },
              {
                name: "三类闸站加固中",
                value: res.data[0].thirdprocess ? res.data[0].thirdprocess : 0,
                src: require("@/assets/images/black_jgz.png"),
              },
              {
                name: "二类闸站加固中",
                value: res.data[0].secondprocess ? res.data[0].secondprocess : 0,
                src: require("@/assets/images/black_jgz.png"),
              },
            ];
          break;
          case 'firstCnt':
            this.statusList = [
              {
                name: "一类闸站",
                value: res.data[0].firstClass ? res.data[0].firstClass : 0,
                src: require("@/assets/images/black_wjg.png"),
              },
              {
                name: "二类闸站已完工",
                value: res.data[0].secondfinish ? res.data[0].secondfinish : 0,
                src: require("@/assets/images/black_ywg.png"),
              },
              {
                name: "三类闸站已完工",
                value: res.data[0].thirdfinish ? res.data[0].thirdfinish : 0,
                src: require("@/assets/images/black_ywg.png"),
              },
              {
                name: "四类闸站已完工",
                value: res.data[0].thirdfinish ? res.data[0].fourthfinish : 0,
                src: require("@/assets/images/black_ywg.png"),
              },
            ];
          break;
          case 'totalCnt':
            this.statusList = [
              {
                name: "未加固",
                value: res.data[0].status1 ? res.data[0].status1 : 0,
                src: require("@/assets/images/black_wjg.png"),
              },
              {
                name: "加固中",
                value: res.data[0].status2 ? res.data[0].status2 : 0,
                src: require("@/assets/images/black_jgz.png"),
              },
              {
                name: "已完工",
                value: res.data[0].status3 ? res.data[0].status3 : 0,
                src: require("@/assets/images/black_ywg.png"),
              },
              {
                name: "拟降等报废",
                value: res.data[0].status4 ? res.data[0].status4 : 0,
                src: require("@/assets/images/black_njdbf.png"),
              },
            ];
          break;
        };
        
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.loading_status = false;
      this.$nextTick(()=>{
        this.tableHeight = (this.$refs.blackRightWrap.clientHeight - this.$refs.cxjgBox.clientHeight) + 'px'
      })
    },

    // 列表
    async getReservoirList(colorNum) {
      if (this.listFlag) {
        this.opt.basin = this.leftSelectBasin
        this.$parent.$refs.mainMap.removeAllLayer();
        this.$parent.$refs.mainMap.closePopup();
        this.$parent.$refs.mainMap.initView();
        this.tableLoading = true;
        this.$store.commit("SET_BASIN_RIGTH_SELECT_FLAG", false);
        this.tableData = [];
        this.listFlag = false;
        let res = await basinStatisticApi(this.opt);
        if (res.success) {
          let pointArr = [];
          let pointObj = {
            green: [],
            blue: [],
            yellow: [],
            orange: [],
          };
          let lineArr = []
          if (res.data.length) {
            this.tableData = res.data;
            res.data.map((v) => {
              v.lat = v.LTTD;
              v.lng = v.LGTD;
              v.rightType = "2";
              v.src = require("@/assets/images/map/reservoir.png");
              pointArr.push(v);
            });
          }
          this.$parent.showMapPoints(pointArr, 'sluice');
        }
        this.listFlag = true;
        this.tableLoading = false;
        this.$store.commit("SET_BASIN_RIGTH_SELECT_FLAG", true);
      }
    },

    clickReservoir(row) {
      this.$parent.setView(Number(row.LGTD) + 0.004, Number(row.LTTD) + 0.013);
      row.lat = row.LTTD;
      row.lng = row.LGTD;
      row.rightType = "2";
      // this.$parent.showOneSelectPoint([row]);
      setTimeout(() => {
        this.$parent.setPopup(row);
      }, 500);
    },


    scaleChartClick(index){
      this.scale = this.scaleData.scaleList[index]
      this.setOptions('scale', this.scale)
      this.getReservoirList()
    },
    cleanScaleClick(){
      this.scale = 'all'
      this.setOptions('scale', this.scale)
      this.$refs.scaleChart.initChart()
    }
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  height: 100%;
  .cxjg_box {
    .status_list {
      @include flexbox();
      @include flexJC(space-between);
      @include flexflow(row wrap);
      padding: 16px 16px 8px 16px;
      box-sizing: border-box;
      .status_item {
        width: calc(50% - 4px);
        @include flexbox();
        @include flexAC();
        cursor: pointer;
        background: #17272E;
        box-sizing: border-box;
        padding: 8px 0;
        border: 1px solid transparent;
        margin-bottom: 8px;
        &.active {
          background: #284E53;
          border: 1px solid rgba(84,249,252,0.80);
          box-shadow: inset 0 0 16px 0 rgba(84,249,252,0.60);
          // border: 1px solid #193bc4;
        }
        img {
          margin-right: 10px;
          margin-left: 20px;
        }
        .status_right {
          & > p {
            line-height: 24px;
            font-size: 14px;
            color: #fff;
            font-weight: bold;
            padding: 8px 0 4px 0;
          }
          i {
            font-size: 12px;
            padding-left: 4px;
          }
          & > span {
            line-height: 24px;
            font-size: 16px;
            color: #fff;
            font-weight: bold;
          }
        }
      }
    }
  }
  .reservoirList_box {
    height: 100%;
    .table_list {
      // height: calc(100% - 302px);
      box-sizing: border-box;
      padding-bottom: 16px;
      // height: 500px;
      overflow: auto;
    }
  }
}

</style>
