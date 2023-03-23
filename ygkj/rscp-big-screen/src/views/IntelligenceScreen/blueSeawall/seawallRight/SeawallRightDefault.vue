
<template>
  <div class="wrap">
    <section class="seawallList_box">
      <div class="chart_title chart_title-ht" style="height: 25px">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        三色码管理
        <img class="threeColorBtn cursor" @click="coderuleDialogVisible = true" :src="$config.zhImgUrl + 'threeColorBtn.png'" title="三色码规则" />
      </div>
      <section class="left_top">
        <div class="select_square">
          <div :class="['square_item', index === colorCodeIndex ? 'active' : '']" v-for="(item, index) in colorCodeList" :key="index" @click="clickColorCode(index)">
            <p>{{ item.name }}</p>
            <p :style="{ color: item.color }" :class="[index === colorCodeIndex ? 'active' : '']">
              {{ item.value }}
              <span style="color: #000">条</span>
            </p>
          </div>
        </div>
        <bar-chart-colors-code :class="['securityChart']" :chartData="barData" ref="seawalChart" />
      </section>

      <div class="chart_title chart_title-ht" style="height: 25px">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        薄弱环节
      </div>
      <section class="right_middle">
        <div class="select_square">
          <div :class="[
              'square_item',
              item.type,
              index !== 3 && index === squareIndex
                ? 'active'
                : '',
            ]" v-for="(item, index) in squareList" :key="index" @click="clickSquare(index)">
            <p>{{ item.name }}</p>
          </div>
        </div>
        <div v-show="squareIndex === 0" class="aqjd_box">
          <div :class="['aqjd_item cursor', { active: tangActive == 1 }]" @click="tangClick(1)">
            <h3>一类塘</h3>
            <p>{{ safeData.yltNum }}<span>条</span></p>
          </div>
          <div :class="['aqjd_item cursor', { active: tangActive == 2 }]" @click="tangClick(2)">
            <h3>二类塘</h3>
            <p>{{ safeData.eltNum }}<span>条</span></p>
          </div>
          <div :class="['aqjd_item cursor', { active: tangActive == 3 }]" @click="tangClick(3)">
            <h3>三类塘</h3>
            <p>{{ safeData.sltNum }}<span>条</span></p>
          </div>
          <div :class="['aqjd_item cursor', { active: tangActive == 4 }]" @click="tangClick(4)">
            <h3>一年内到期</h3>
            <p>{{ safeData.dueNum }}<span>条</span></p>
          </div>
          <div :class="['aqjd_item cursor', { active: tangActive == 5 }]" @click="tangClick(5)">
            <h3>超期未鉴定</h3>
            <p>{{ safeData.duedNum }}<span>条</span></p>
          </div>
        </div>
        <div v-show="squareIndex === 1" class="third_box">
          <ul class="tab_box">
            <li>
              <p>未开工</p>
              <div class="tab_box_item">
                <span class="ba_text">二类塘</span>
                <span class="ba_num">
                  {{ cxjgStaticData.elwkg }}
                  <span class="ba_unit">条</span>
                </span>
              </div>
              <div class="tab_box_item">
                <span class="ba_text">三类塘</span>
                <span class="ba_num">
                  {{ cxjgStaticData.slwkg }}
                  <span class="ba_unit">条</span>
                </span>
              </div>
            </li>
            <li>
              <p>已开工</p>
              <div class="tab_box_item">
                <span class="ba_text">二类塘</span>
                <span class="ba_num">
                  {{ cxjgStaticData.elykg }}
                  <span class="ba_unit">条</span>
                </span>
              </div>
              <div class="tab_box_item">
                <span class="ba_text">三类塘</span>
                <span class="ba_num">
                  {{ cxjgStaticData.slywg }}
                  <span class="ba_unit">条</span>
                </span>
              </div>
            </li>
            <li>
              <p>当年完工</p>
              <div class="tab_box_item">
                <span class="ba_text">二类塘</span>
                <span class="ba_num">
                  {{ cxjgStaticData.eldnwg }}
                  <span class="ba_unit">条</span>
                </span>
              </div>
              <div class="tab_box_item">
                <span class="ba_text">三类塘</span>
                <span class="ba_num">
                  {{ cxjgStaticData.sldnwg }}
                  <span class="ba_unit">条</span>
                </span>
              </div>
            </li>
          </ul>
        </div>
        <div v-show="squareIndex === 2" class="control-one-list">
          <div class="control-one-item" :class="{}" v-for="(item, index) in oneList" :key="index">
            <img :src="item.imgUrl" alt="" />
            <div>
              <p>{{ item.name }}</p>
              <p>
                <span>{{ item.num }}</span>座
              </p>
            </div>
          </div>
        </div>
      </section>

      <div class="chart_title chart_title-ht" style="height: 25px">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        工程清单
      </div>
      <div class="table_list" ref="tableContainer">
        <el-table :height="tableHeight" v-loading="tableLoading" :data="tableData" stripe style="width: 100%" @row-click="clickSeawall">
          <template slot="empty">
            <div class="table_nodata">
              <img src="../../../../assets/images/noData.png" alt="" />
            </div>
          </template>
          <el-table-column prop="name" width="80px" label="海塘名称"></el-table-column>
          <el-table-column prop="status" label="安全状态" align="center"></el-table-column>
          <el-table-column prop="designTideLevel" label="设计潮位(m)" align="center">
            <template slot-scope="scope">
              <div>
                {{
                  scope.row.designTideLevel ? scope.row.designTideLevel : "-"
                }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="seawall_length" label="长度(m)" width="65px" align="center"></el-table-column>
        </el-table>
      </div>
    </section>

    <!-- 三色码 规则展示 -->
    <primary-dialog mainTitle="三色码规则" :visible="coderuleDialogVisible" @handleClose="handleCloseCode" @changeFullscreen="changeFullscreen" :fullscreen="fullscreen" width="850px" top="5vh" append-to-body>
      <div class="dialog-img-content threeColor" :class="{'fullscreen-box': fullscreen}" v-viewer>
        <img class="huimin threeColor-huimin" :src="$config.zhImgUrl + 'sansemadisizhang.png'" alt="三色码规则" />
      </div>
    </primary-dialog>
  </div>
</template>

<script>
import { zhSeawall, seawallProIndo } from "@/api/api_seawall";
import BarChartColorsCode from "../components/BarChartColorsCode";
import PrimaryDialog from "../components/PrimaryDialog.vue";
export default {
  name: "SeawallRightOne",
  props: {},
  components: {
    BarChartColorsCode,
    PrimaryDialog,
  },
  data() {
    return {
      fullscreen: false,
      seawallList: [],
      showZjsNubmer: true,
      coderuleDialogVisible: false,
      barData: {
        id: "zhSeawallCodeChart",
        xData: [],
        rData: [],
        yData: [],
        gData: [],
        adcdList: [],
        unit: "座",
        yInterval: 100,
        noWords: false,
      },
      oneList: [
        {
          imgUrl: require("@/assets/images/control-a.png"),
          name: "工程检查",
          num: 68,
        },
        {
          imgUrl: require("@/assets/images/control-b.png"),
          name: "发现问题",
          num: 540,
        },
        {
          imgUrl: require("@/assets/images/control-c.png"),
          name: "处置中",
          num: 685,
        },
        {
          imgUrl: require("@/assets/images/control-d.png"),
          name: "已处置",
          num: 80,
        },
      ],
      squareList: [
        {
          type: "authenticate",
          name: "安全鉴定",
        },
        {
          type: "construction",
          name: "工程施工",
        },
        {
          type: "inspection",
          name: "工程检查",
        },
        {
          type: "supervise",
          name: "安全监测",
        },
      ],
      colorCodeList: [
        {
          type: "all",
          name: "总数",
          lable: "",
          value: 0,
          color: "#000",
        },
        {
          type: "red",
          name: "红码",
          lable: "红色",
          value: 0,
          color: "#FA7C7CFF",
        },
        {
          type: "yellow",
          name: "黄码",
          lable: "黄色",
          value: 0,
          color: "#F2A466FF",
        },
        {
          type: "green",
          name: "绿码",
          lable: "绿色",
          value: 0,
          color: "#63BF70FF",
        },
      ],

      squareIndex: 0,
      tangActive: 0,
      colorCodeIndex: "",
      squareValue: "authenticate",
      colorCodeValue: "",
      safeData: {},
      cxjgStaticData: {},
      tableData: [],
      tableHeight: 0,
      tableLoading: true,
      loading_square: true,
      listFlag: true,
      opt: {
        statistic: "mapList",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      },
      pointArr: {
        green: [],
        red: [],
        yellow: [],
      },
      //定一个状态 只允许请求一次 0：请求之前 1：请求结束
      apiOnce: true,
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
      this.getColorsNum();
      this.getColorsBarData();
      this.getSeawallList();
    },
    leftSelectPreventLevel(val) {
      this.setOptions("preventLevel", val);
      this.getSeawallList();
    },
    leftSelectProjectScale(val) {
      this.setOptions("scale", val);
      this.getSeawallList();
    },
  },
  mounted() {
    this.opt.adcd = this.leftSelectAdcd
      ? this.leftSelectAdcd
      : this.$localData("get", "userInfo").adcd;
    if (this.leftSelectAdcd == "33") {
      this.opt.superviseLevel = 1;
    }
    if (this.opt.adcd.indexOf("0000000000") != -1) {
      this.showZjsNubmer = true;
    } else {
      this.showZjsNubmer = false;
    }
    if (this.leftSelectProjectScale) {
      this.setOptions("scale", this.leftSelectProjectScale);
    }
    this.getColorSeawallList();
    this.resizeTable();
    this.getColorsBarData();
    this.getColorsNum();
    this.getSafeData();
    // this.clickSquare(0);
    this.clickColorCode(0);
  },
  methods: {
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
    },
    handleCloseCode() {
      this.fullscreen = false;
      this.coderuleDialogVisible = false;
    },
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
    // 海塘 三色码 统计
    async getColorsNum() {
      let res = await zhSeawall.seawallThreeCodeSta({
        adcd: this.leftSelectAdcd,
      });
      if (res.success) {
        this.colorCodeList[0].value = res.data.TotalCount;
        this.colorCodeList[1].value = res.data.RedCount;
        this.colorCodeList[2].value = res.data.yellowCount;
        this.colorCodeList[3].value = res.data.GreenCount;
      }
    },
    //  三色码 柱状图
    async getColorsBarData() {
      this.barLoading = true;
      let res = await zhSeawall.seawallthreeYardBarChart({
        adcd: this.leftSelectAdcd,
        userAdcd: this.leftSelectAdcd,
        projectScale: this.$store.state.leftSelectProjectScale,
      });
      if (res.success) {
        let xData = [];
        let rData = [];
        let yData = [];
        let gData = [];
        let adcdList = [];
        let totalCount = 0;
        if (res.rows.length > 0) {
          res.rows.forEach((element) => {
            if (element.red + element.yellow + element.green != 0) {
              if (element.adnm === "杭州市") {
                totalCount = element.total;
              }
              if (element.adnm.length > 2) {
                xData.push(element.adnm.replace(/市/, ""));
              } else {
                xData.push(element.adnm);
              }
              rData.push(element.red);
              yData.push(element.yellow);
              gData.push(element.green);
              adcdList.push(element.adcd);
            }
          });
        }

        this.$store.commit("SET_TEMP_HANGZSHOU", totalCount);
        this.barData.xData = xData;
        this.barData.rData = rData;
        this.barData.yData = yData;
        this.barData.gData = gData;
        this.barData.adcdList = adcdList;
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.barLoading = false;
    },
    tangClick(index) {
      this.tangActive = index
      this.$parent.checkSgzgMapType({ name: '安全鉴定', type: index })
      this.getDamSeawallList(index)
    },
    clickSquare(index) {
      if (index === 3) {
        return;
      }
      this.squareIndex = index;
      this.squareValue = this.squareList[index].type;
      switch (index) {
        case 0:
          this.getDamSeawallList(1)
          this.tangActive = 1
          this.colorCodeIndex = ''
          this.getSafeData();
          this.$parent.checkSgzgMapType({ name: '安全鉴定', type: 1 })
          break;
        case 1:
          this.colorCodeIndex = "";
          this.getXzyyData();
          break;
        case 2:
          // this.getXzyyData();
          break;
        default:
          break;
      }
    },
    clickColorCode(index) {
      this.$parent.checkSgzgMapType({ name: '监测分析',})
      this.tangActive = '';
      this.squareIndex = 0
      this.colorCodeIndex = index
      this.colorCodeValue = this.colorCodeList[index].type;
      this.$refs.seawalChart.initChart(this.colorCodeValue);
      this.$store.commit("SET_HTCOLORCODE", this.colorCodeValue);
      this.getColorSeawallList(
        this.colorCodeValue,
        this.colorCodeList[index].lable
      );
    },

    // 安全鉴定
    async getSafeData() {
      let res = await zhSeawall.safeStaNum({
        interType: 1,
        adcd: this.leftSelectAdcd,
      });
      if (res.success) {
        this.safeData = res.data;
      }
    },
    // 除险加固
    async getXzyyData() {
      let opt = {
        adcd: this.leftSelectAdcd,
      };
      let res = await zhSeawall.reinforcementSta(opt);
      if (res.success) {
        this.cxjgStaticData = res.data;
      }
    },
    clickSeawall(row) {
      let lttd = Number(row.lttd) + 0.008;
      this.$parent.setView(row.lgtd, lttd);
      row.lat = row.lttd;
      row.lng = row.lgtd;
      row.rightType = "4";
      this.$parent.showOneSelectPoint([row]);
      setTimeout(() => {
        row.rightType = "4";
        this.$parent.setPopup(row);
      }, 500);
    },
    /**
     * @function:
     * @description: 三色码请求接口
     * @param {*} type 颜色类型 red yellow green 切换图层用
     * @param {*} name 颜色名字 红黄绿 请求接口用
     * @return {*}
     */
    async getColorSeawallList(type = "", name = "") {
      console.log(type, "type", name, "name");
      this.pointArr = {
        green: [],
        red: [],
        yellow: [],
      };
      let opt = {
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
        extractPram: name,
        projectScale: this.$store.state.leftSelectProjectScale,
      };
      this.tableLoading = true;
      this.$parent.$refs.seawallMap.removeAllLayer();
      this.$parent.$refs.seawallMap.closePopup();
      this.$parent.$refs.seawallMap.initView();
      let res = await seawallProIndo(opt);
      if (res.success) {
        let lineArr = [];
        this.tableData = [];
        if (res.data.length) {
          this.tableData = res.data;
          res.data.map((v) => {
            v.lat = v.lttd;
            v.lng = v.lgtd;
            v.rightType = 4;
            v.styleObj = {
              scale: 1,
            };
            switch (v.threeYards) {
              case "绿色":
                v.color = "green";
                this.pointArr["green"].push(v);
                break;
              case "红色":
                v.color = "red";
                this.pointArr["red"].push(v);
                break;
              case "黄色":
                v.color = "yellow";
                this.pointArr["yellow"].push(v);
                break;
            }
            v.mapType = "seawallPopup";
            lineArr.push({
              line: v.coorpot
                ? JSON.parse(
                  v.coorpot.replace(new RegExp("&quot;", "gm"), '"')
                )[0]
                : "",
              level: v.project_scale,

              type: "seawallPopup",
            });
            v.lineArr = [];
            v.lineArr.push({
              line: v.coorpot
                ? JSON.parse(
                  v.coorpot.replace(new RegExp("&quot;", "gm"), '"')
                )[0]
                : "",
              level: v.seawallPopup,
              type: "seawallPopup",
            });
          });
        }
        this.tableLoading = false;
        this.$parent.showMapPointJh(
          this.pointArr["yellow"],
          "yellowPointLayer"
        );
        this.$parent.showMapPointJh(this.pointArr["green"], "greenPointLayer");
        this.$parent.showMapPointJh(this.pointArr["red"], "redPointLayer");
        this.$parent.showMapLines(lineArr);
      }
    },
    async getDamSeawallList(dam = "") {
      this.pointArr = {
        ylTang: [],
        elTang: [],
        slTang: [],
        yndq: [],
        cqwjd: [],
      };
      let opt = {
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
        dam: dam,
        projectScale: this.$store.state.leftSelectProjectScale,
      };

      this.tableLoading = true;
      this.$parent.$refs.seawallMap.removeAllLayer();
      this.$parent.$refs.seawallMap.closePopup();
      this.$parent.$refs.seawallMap.initView();
      let res = await seawallProIndo(opt);
      if (res.success) {
        let lineArr = [];
        this.tableData = [];
        if (res.data.length) {
          this.tableData = res.data;
          res.data.map((v) => {
            v.lat = v.lttd;
            v.lng = v.lgtd;
            v.rightType = 4;
            v.styleObj = {
              scale: 1
            }
            switch (dam) {
              case 1:
                v.color = 'ylTang'
                this.pointArr['ylTang'].push(v)
                break;
              case 2:
                v.color = 'elTang'
                this.pointArr['elTang'].push(v)
                break;
              case 3:
                v.color = 'slTang'
                this.pointArr['slTang'].push(v)
                break;
              case 4:
                v.color = 'yndq'
                this.pointArr['yndq'].push(v)
                break;
              case 5:
                v.color = 'cqwjd'
                this.pointArr['cqwjd'].push(v)
                break;
            }
            v.mapType = "seawallPopup";
            lineArr.push({
              line: v.coorpot
                ? JSON.parse(
                  v.coorpot.replace(new RegExp("&quot;", "gm"), '"')
                )[0]
                : "",
              level: v.project_scale,
              type: "seawallPopup",
            });
            v.lineArr = [];
            v.lineArr.push({
              line: v.coorpot
                ? JSON.parse(
                  v.coorpot.replace(new RegExp("&quot;", "gm"), '"')
                )[0]
                : "",
              level: v.seawallPopup,
              type: "seawallPopup",
            });
          });
        }
        this.tableLoading = false
        switch (dam) {
          case 1:
            this.$parent.showMapPointJh(this.pointArr['ylTang'], 'ylTangPointLayer',)
            break;
          case 2:
            this.$parent.showMapPointJh(this.pointArr['elTang'], 'elTangPointLayer',)
            break;
          case 3:
            this.$parent.showMapPointJh(this.pointArr['slTang'], 'slTangPointLayer',)
            break;
          case 4:
            this.$parent.showMapPointJh(this.pointArr['yndq'], 'yndqPointLayer',)
            break;
          case 5:
            this.$parent.showMapPointJh(this.pointArr['cqwjd'], 'cqwjdPointLayer',)
            break;
        }
        this.$parent.showMapLines(lineArr)
      }
    },
    resizeTable() {
      this.$nextTick(function () {
        const height = this.$refs.tableContainer.clientHeight;
        this.tableHeight = height - 25;
      });
    },
    exportList() {
      let adcd = this.leftSelectAdcd
        ? this.leftSelectAdcd
        : this.$localData("get", "userInfo").adcd;
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
    },
  },
};
</script>

<style lang='scss' scoped>
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
      &:first-child {
        font-weight: bold;
      }
    }
  }
  .seawallList_box {
    height: 100%;
    .table_list {
      height: 340px;
      box-sizing: border-box;
      padding-bottom: 16px;
    }
    .table_list .el-table .cell {
      font-size: 16px;
      color: #666;
    }
    .table_list .el-table th > .cell {
      font-size: 16px;
      color: #666;
    }
  }
}
.threeColorBtn {
  margin-left: 6px;
  width: 16px;
}
.left_top {
  width: 100%;
  height: calc(100% - 650px);
  overflow: hidden;
  .all_num {
    width: 100%;
    @include flexbox();
    @include flexAI(center);
    margin-bottom: 3px;
    padding-left: 5px;
    p {
      color: #46fdff;
      font-size: 21px;
      font-family: youshe;
      margin-right: 15px;
      span {
        font-size: 22px;
      }
    }
    div {
      @include flex(1);
      border-bottom: 1px solid #526aaf;
    }
  }
  .select_square {
    box-sizing: border-box;
    @include flexbox();
    @include flexJC(space-between);
    @include flexflow(row wrap);
    padding: 3px 6px;
    .square_item {
      box-sizing: border-box;
      width: 23.5%;
      cursor: pointer;
      border-radius: 2px;
      padding: 10px 0px 8px 12px;
      background: #f8f8f8;
      border: 1px solid #d6d6d6;
      &.active {
        background: #193cc5 !important;
        border: 1px solid #193cc5 !important;
        & > p {
          color: #fff !important;
        }
        & span {
          color: #fff !important;
        }
      }
      & > p {
        line-height: 20px;
        font-size: 16px;
        color: #000;
        &:nth-child(2) {
          text-align: left;
          font-size: 20px;
          padding-top: 4px;
          span {
            font-size: 16px;
          }
        }
      }
    }
  }
  .first_box {
    width: 100%;
    @include flexbox();
    @include flexJC(center);
    @include flexAI(center);
    margin: 3px 0px;
  }
  .securityChart {
    margin-top: 5px;
    width: 100%;
    height: calc(100% - 65px);
  }
}
.right_middle {
  width: 100%;
  height: 160px;
  overflow: hidden;
  .select_square {
    width: 100%;
    height: 24px;
    box-sizing: border-box;
    margin-bottom: 16px;
    @include flexbox();
    @include flexJC(space-around);
    @include flexflow(row nowrap);
    .square_item {
      height: 100%;
      width: 20%;
      cursor: pointer;
      border-radius: 2px;
      padding: 4px;
      background: #f8f8f8;
      p {
        line-height: 24px;
        font-size: 15px;
        text-align: center;
        color: #000;
      }
      &:nth-child(4) {
        cursor: not-allowed;
      }
      &.active {
        background: #193cc5 !important;
        border: 1px solid #193cc5 !important;
        & > p {
          color: #fff !important;
        }
      }
    }
  }
  .aqjd_box {
    height: 50%;
    @include flexbox();
    @include flexAI(center);
    @include flexJC(flex-start);
    @include flexflow(row wrap);
    padding: 0 0px 0 10px;
    .aqjd_item {
      user-select: none;
      width: 30%;
      box-sizing: border-box;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(center);
      @include flexflow(column nowrap);
      padding: 3px 0;
      &.active {
        background: rgba(25, 59, 196, 0.08);
        box-shadow: 0px 4px 8px 2px rgba(0, 0, 0, 0.02),
          0px 0px 2px 0px rgba(0, 0, 0, 0.06),
          0px 2px 2px 0px rgba(0, 0, 0, 0.06);
        border: 1px solid #193bc4;
      }
      h3 {
        font-size: 16px;
        padding: 6px 0 0 0;
        color: #000;
      }
      p {
        font-size: 22px;
        padding-top: 6px;
        font-weight: 600;
        span {
          font-size: 14px;
          font-family: none;
          margin-left: 5px;
          color: #000;
        }
      }
    }
  }
  .third_box {
    margin: 16px 0;
    .tab_box {
      width: 100%;
      display: grid;
      grid-template-columns: 1fr 1fr 1fr;
      gap: 10px 4px;
      padding: 10px 0;
      font-size: 20px;
      text-align: left;
      margin: 5px auto;
      li {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        align-items: start;
        p {
          width: 80%;
          font-weight: 600;
          text-align: center;
          color: "#193cc5";
          margin: 0 0 0 5px;
        }
        .tab_box_item {
          @include flexbox;
          @include flexAI(center);
        }
        div {
          margin: 16px 0px 0px 10px;
          span {
            font-size: 16px;
            color: "#000";
            margin: 0 2px;
          }
          .ba_text {
            font-size: 16px;
          }
          .ba_num {
            display: flex;
            font-size: 18px;
            flex: 0 1 0%;
            align-items: center;
            color: "#000";
            font-weight: 600;
            .ba_unit {
              color: "#000";
              font-size: 16px;
              margin: 0 2px;
              font-weight: 500;
            }
          }
        }
      }
    }
  }
  .control-one-list {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
    padding-top: 6px;
    .control-one-item {
      width: calc(50% - 20px);
      margin-left: 20px;
      height: 60px;
      padding: 6px 0 6px 16px;
      box-sizing: border-box;
      @include flexbox();
      @include flexAC();
      // cursor: pointer;
      img {
        width: 18px;
      }
      div {
        padding-left: 10px;
        p {
          line-height: 20px;
          font-size: 14px;
          color: #000000;
          span {
            font-size: 20px;
            padding-right: 5px;
            font-weight: 600;
            line-height: 32px;
          }
        }
      }
      &.avtive-one-item {
        width: 180px;
        height: 76px;
        background: rgba(25, 59, 196, 0.08);
        box-shadow: 0px 2px 4px 1px rgba(0, 0, 0, 0.02),
          0px 0px 1px 0px rgba(0, 0, 0, 0.06),
          0px 1px 1px 0px rgba(0, 0, 0, 0.06);
        border: 1px solid #193bc4;
        padding: 12px 0 12px 23px;
        div {
          p {
            color: #193cc5;
          }
        }
      }
    }
  }
}
.htfbChart {
  width: 100%;
  height: 100%;
}
.dialog-img-content {
  z-index: 999;
  overflow: auto;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE 10+ */
  &::-webkit-scrollbar {
    display: none;
  }
  width: 100%;
  height: 620px;
  text-align: center;
  img.huimin {
    width: 100%;
  }
  &.fullscreen-box {
    .threeColor-huimin {
      width: 65%;
    }
  }
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