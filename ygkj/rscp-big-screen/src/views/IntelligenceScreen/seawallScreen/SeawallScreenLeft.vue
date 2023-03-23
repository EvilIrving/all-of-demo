<template>
  <div ref="slide_wrap" class="slide_wrap">
    <div class="slide_title">海塘安澜</div>
    <!-- 第一部分风险研判 -->
    <section class="left_top">
      <p class="second_title2">
        风险研判
        <!-- <img class="threeColorBtn" @click="coderuleDialogVisible = true" :src="$config.imgUrl + 'threeColorBtn.png'" title="三色码规则" /> -->
      </p>
      <ul ref="first_box" class="left_box first_box">
        <li @click="chooseMapType('风险研判')" :class="['cursor',sgzgMapType == '风险研判' && colorCode == 'all'? 'numberActive': '']">
          <span class="total_text">总数</span>
          <span class="total_num">{{ colorsNum.TotalCount }}<span class="total_unit">条</span></span>
        </li>
        <li @click="showOneColor('red')" :class="['cursor',sgzgMapType == '风险研判' && colorCode == 'red'? 'numberActive': '']">
          <span class="total_text">红码</span>
          <span class="total_num red">{{ colorsNum.RedCount }}<span class="total_unit">条</span></span>
        </li>
        <li @click="showOneColor('yellow')" :class="['cursor',sgzgMapType == '风险研判' && colorCode == 'yellow'? 'numberActive': '']">
          <span class="total_text">黄码</span>
          <span class="total_num yellow">{{ colorsNum.yellowCount }}<span class="total_unit">条</span></span>
        </li>
        <li @click="showOneColor('green')" :class="['cursor',sgzgMapType == '风险研判' && colorCode == 'green'? 'numberActive': '']">
          <span class="total_text">绿码</span>
          <span class="total_num green">{{ colorsNum.GreenCount }}<span class="total_unit">条</span></span>
        </li>
      </ul>
      <bar-chart-colors-code :class="['securityChart', showZjsNubmer ? 'hasNum' : 'noNum']" :chartData="barData" ref="seawallColorchart" />
    </section>

    <!-- 第二个防潮体系 -->
    <section class="left_second">
      <p class="second_title2">防潮体系</p>
      <!-- 一线海塘  和 二道防线 -->
      <p class="third_title cursor">
        <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
        <span :class="[
            tabIndex1 == index ? 'active ' : '',
            sgzgMapType == item ? 'mapTypeActive' : '',
          ]" v-for="(item, index) in tabList1" :key="item" @click="(tabIndex1 = index), chooseMapType(item)">{{ item }}</span>
        <i></i>
        <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" />
      </p>
      <div class="pie_box">
        <div class="chart_title">
          <div @click="chooseMapType('防潮体系', xzyxData.name)">
            <p>
              {{
                this.htjbtjData.totalNum !== undefined
                  ? this.htjbtjData.totalNum
                  : "-"
              }}<span>条</span>
            </p>
            <span>海塘总数</span>
          </div>
        </div>
        <pie-chart class="htfbChart" :chartData="htjbtjData"></pie-chart>
        <ul class="xzyx_legend">
          <li :class="['cursor',legendIndex == index + 1?'active':'']" v-for="(item, index) in htjbtjData.series" :key="index" @click="chartClick(item.project_scale,tabList1[tabIndex1]),legendIndex = index + 1">
            <span class="little_block" :style="{ 'background-color': item.color }"></span>
            <span class="name"> {{ item.name }}</span>
            <span class="unit"> {{ item.value }}</span>
          </li>
        </ul>
      </div>
    </section>

    <!-- 第三个 风险管控 -->
    <section class="left_third">
      <p class="second_title2">风险管控</p>
      <p class="third_title">
        <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
        <span :class="[
        'cursor',
            tabIndex0 == index ? 'active' : '',
            sgzgMapType == item ? 'mapTypeActive' : '',
          ]" v-for="(item, index) in tabList0" :key="item" @click="(tabIndex0 = index), chooseMapType(item)">{{ item }}</span>
        <i></i>
        <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" />
      </p>
      <div v-if="tabIndex0 == 0" class="aqjd_box">
        <div @click="chooseMapType('安全鉴定', '一类塘')" class="aqjd_item cursor" :class="numberSelected == '一类塘' ? 'numberActive' : ''" style="width: auto">
          <h3>一类塘</h3>
          <p>{{ safeData.yltNum }}<span>条</span></p>
        </div>
        <div @click="chooseMapType('安全鉴定', '二类塘')" class="aqjd_item cursor" :class="numberSelected == '二类塘' ? 'numberActive' : ''" style="width: auto">
          <h3>二类塘</h3>
          <p>{{ safeData.eltNum }}<span>条</span></p>
        </div>
        <div @click="chooseMapType('安全鉴定', '三类塘')" class="aqjd_item cursor" :class="numberSelected == '三类塘' ? 'numberActive' : ''" style="width: auto">
          <h3>三类塘</h3>
          <p>{{ safeData.sltNum }}<span>条</span></p>
        </div>
        <div @click="chooseMapType('安全鉴定', '一年内到期')" class="aqjd_item cursor" :class="numberSelected == '一年内到期' ? 'numberActive' : ''" style="width: auto">
          <h3>一年内到期</h3>
          <p>{{ safeData.dueNum }}<span>条</span></p>
        </div>
      </div>
      <div v-if="tabIndex0 == 1" class="third_box">
        <ul class="tab_box">
          <li>
            <p>未开工</p>
            <div class="cursor tab_box_item" :class="numberSelected == '未开工,二类塘' ? 'numberActive' : ''" @click="chooseMapType('除险加固', '未开工,二类塘')">
              <span class="ba_text">二类塘</span>
              <span class="ba_num">
                {{ cxjgStaticData.elwkg }}
                <span class="ba_unit">条</span>
              </span>
            </div>
            <div class="cursor tab_box_item" :class="numberSelected == '未开工,三类塘' ? 'numberActive' : ''" @click="chooseMapType('除险加固', '未开工,三类塘')">
              <span class="ba_text">三类塘</span>
              <span class="ba_num">
                {{ cxjgStaticData.slwkg }}
                <span class="ba_unit">条</span>
              </span>
            </div>
          </li>
          <li>
            <p>已开工</p>
            <div class="cursor tab_box_item" :class="numberSelected == '已开工,二类塘' ? 'numberActive' : ''" @click="chooseMapType('除险加固', '已开工,二类塘')">
              <span class="ba_text">二类塘</span>
              <span class="ba_num">
                {{ cxjgStaticData.elkgnum }}
                <span class="ba_unit">条</span>
              </span>
            </div>
            <div class="cursor tab_box_item" :class="numberSelected == '已开工,三类塘' ? 'numberActive' : ''" @click="chooseMapType('除险加固', '已开工,三类塘')">
              <span class="ba_text">三类塘</span>
              <span class="ba_num">
                {{ cxjgStaticData.slkgnum }}
                <span class="ba_unit" style="width: 32px;">条</span>
              </span>
            </div>
          </li>
          <li>
            <p>海塘安澜</p>
            <div class="tab_box_item" :class="numberSelected == '海塘安澜,二类塘' ? 'numberActive' : ''">
              <span class="ba_text">开工</span>
              <span class="ba_num">
                {{ cxjgStaticData.elkglength }}
                <span class="ba_unit">条</span>
              </span>
            </div>
            <div class="tab_box_item" :class="numberSelected == '海塘安澜,三类塘' ? 'numberActive' : ''">
              <span class="ba_text">长度</span>
              <span class="ba_num">
                {{ cxjgStaticData.slkglength }}
                <span class="ba_unit">公里</span>
              </span>
            </div>
          </li>
        </ul>
      </div>
    </section>

    <!-- 第四个问题处置 -->
    <section class="left_four">
      <p class="second_title2 cursor">
        <span :class="sgzgMapType == '问题处置' ? 'mapTypeActive' : ''" @click="chooseMapType('问题处置')">
          问题处置
        </span>
      </p>
      <div class="problem_box">
        <ul class="gs_list">
          <li class="gs_item" v-for="(item, index) in problemList" :key="index">
            <img :src="item.src" alt="" />
            <div :class="[[2,3].includes(index)?'cursor':'',numberSelected == item.status ? 'numberActive' : '']" @click="chooseMapType('问题处置', item.status)">
              <h3>{{ item.status }}</h3>
              <p>
                <span>{{ item.num }}</span>{{ item.unit }}
              </p>
            </div>
          </li>
        </ul>
      </div>
    </section>

    <primary-dialog mainTitle="" :visible="siweiDialogVisible" @handleClose="handleCloseSiwei" @changeFullscreen="changeFullscreen" :fullscreen="fullscreen" width="1200px" append-to-body top="6vh">
      <div class="dialog-img-content" :class="{ 'fullscreen-box': fullscreen }" v-viewer>
        <img class="huimin siwei-huimin" :src="$config.imgUrl + 'whiteSiwei.png'" alt="" />
      </div>
      <!-- <p style="text-align: center;color: #fff;padding-top:5px;font-size: 20px;">点击可放大图片</p> -->
    </primary-dialog>

    <!-- 三色码 规则展示 -->
    <primary-dialog mainTitle="三色码规则" :visible="coderuleDialogVisible" @handleClose="handleCloseCode" @changeFullscreen="changeFullscreen" :fullscreen="fullscreen" width="850px" top="5vh" append-to-body>
      <div class="dialog-img-content threeColor" :class="{ 'fullscreen-box': fullscreen }" v-viewer>
        <img class="huimin threeColor-huimin" :src="$config.imgUrl + 'threeColorImg.png'" alt="三色码规则" />
      </div>
    </primary-dialog>

    <!-- 水库全周期管理流程图 -->
    <primary-dialog mainTitle="水库全周期管理流程图" :visible="reservoirFullCycle" @changeFullscreen="changeFullscreen" :fullscreen="fullscreen" @handleClose="handleCloseCycle" width="80%" top="10vh" append-to-body>
      <div class="dialog-img-content threeColor" :class="{ 'fullscreen-box': fullscreen }" v-viewer>
        <img class="huimin" :src="$config.imgUrl + 'reservoirfcmf.png'" alt="水库全周期管理流程图" />
      </div>
    </primary-dialog>
  </div>
</template>

<script>
import PieChart from "./components/PieChart.vue";
import SecurityBarChart from "./components/SecurityBarChart.vue";
import BarChartColorsCode from "./components/BarChartColorsCode";
import { zhSeawall } from "@/api/api_seawall";
import { clickLog } from "@/api/api_reservoir";
import PrimaryDialog from "./components/PrimaryDialog.vue";
export default {
  name: "SgzgScreenLeft",
  props: {},
  components: {
    PieChart,
    SecurityBarChart,
    BarChartColorsCode,
    PrimaryDialog,
  },
  mounted() {
    this.getXzyyData();
    this.getCXJGStatic();
    this.getClqlData('一线海塘');
    this.getColorsNum();
    this.getColorsBarData();
    this.getSafeData();

    let userAdcd = this.$localData("get", "userInfo").adcd;
    if (userAdcd.indexOf("0000000000") != -1) {
      this.showZjsNubmer = true;
    } else {
      this.showZjsNubmer = false;
    }

    let nowTime = new Date(new Date().format("MM-dd"));
    let startTime = new Date("04-15");
    let endTime = new Date("10-14");
    if (nowTime >= startTime && nowTime <= endTime) {
      this.inXq = true;
    }
  },
  computed: {
    sgzgMapType() {
      return this.$store.state.sgzgMapType;
    },
    sgzgSelectAdcd() {
      return this.$store.state.sgzgSelectAdcd || this.$localData("get", "userInfo").adcd;
    },
    sgzgSelectBasin() {
      return this.$store.state.sgzgSelectBasin;
    },
    selectData() {
      let { sgzgSelectAdcd, sgzgSelectBasin } = this.$store.state;
      return { sgzgSelectAdcd, sgzgSelectBasin };
    },
    colorCode() {
      return this.$store.state.colorCode;
    },
    numberSelected() {
      return this.$store.state.numberSelected;
    },
  },

  watch: {
    selectData: {
      handler(val) {
        this.getXzyyData();
        this.getCXJGStatic();
        this.getClqlData(this.tabIndex1 === 0 ? '一线海塘' : '非一线海塘');
        this.getColorsNum();
        this.getColorsBarData();
        this.getSafeData();
        if (val.sgzgSelectAdcd == "" && val.sgzgSelectBasin == "") {
          this.showZjsNubmer = true;
        } else if (val.sgzgSelectAdcd.indexOf("0000000000") != -1) {
          this.showZjsNubmer = true;
        } else {
          this.showZjsNubmer = false;
        }
      },
      deep: true,
      immediate: false,
    },
    tabIndex1(val) {
      this.getClqlData(val === 0 ? '一线海塘' : '非一线海塘')
    },
  },
  data() {
    return {
      siweiDialogVisible: false,
      coderuleDialogVisible: false,
      reservoirFullCycle: false,
      kaigongList: [
        { name: "未开工" },
        { name: "已开工" },
        { name: "当年完工" },
      ],
      htjbtjLoading: true,
      htjbtjData: {
        title: "",
        id: "seawallChart",
        unit: "条",
        color: ["#E02021", "#EB7E63", " #C447D6", "#3154EB", "#00FD6D"],
        series: [],
        totalNum: 0,
      },
      barLoading: true,
      barHeight: 150,
      barData: {
        id: "seawallCodeChart",
        xData: [],
        rData: [],
        yData: [],
        gData: [],
        adcdList: [],
        unit: "座",
        yInterval: 0,
        noWords: false,
      },
      problemList: [
        {
          status: "工程检查",
          num: 0,
          unit: "条次",
          src: this.$config.zhImgUrl + "total_Inspection.png",
        },
        {
          status: "发现问题",
          num: 0,
          unit: "项",
          src: this.$config.zhImgUrl + "problem_found.png",
        },
        {
          status: "处置中",
          num: 0,
          unit: "项",
          src: this.$config.zhImgUrl + "disposing.png",
        },
        {
          status: "已处置",
          num: 0,
          unit: "项",
          src: this.$config.zhImgUrl + "disposed.png",
        },
      ],

      timer: null,
      fullscreen: false,
      damTimer: null,
      timerIndex: 0,
      damTimerIndex: 0,
      cxjgStaticData: {},
      colorsNum: {},
      inXq: false,
      showColor: "all",
      tabList0: ["安全鉴定", "除险加固"],
      tabList1: ["一线海塘", "非一线海塘"],
      tabIndex0: 0,
      tabIndex1: 0,
      legendIndex: 0,
      safeData: {},
      chart: {},
      slbchart: {}, //三类坝
      xzyxArr: [],
      xzyxData: {
        name: "超限制水位",
        num: 0,
      },
      damThreeArr: [],
      damThreeData: {
        name: "空库运行",
        num: 0,
      },
      bxskNum: {},
      clqlIndex: 0,
      showZjsNubmer: true,
    };
  },

  methods: {
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
    },
    isDuringDate: function (beginDateStr, endDateStr) {
      var curDate = new Date(),
        beginDate = new Date(beginDateStr),
        endDate = new Date(endDateStr);
      if (curDate >= beginDate && curDate <= endDate) {
        return true;
      }
      return false;
    },
    // 海塘 三色码 统计
    async getColorsNum() {
      let res = await zhSeawall.seawallThreeCodeSta({
        basinCode: this.sgzgSelectBasin,
        adcd: this.sgzgSelectAdcd,

      });
      if (res.success) {
        this.colorsNum = res.data;
      }
    },
    //  三色码 柱状图
    async getColorsBarData() {
      this.barLoading = true;
      let res = await zhSeawall.seawallthreeYardBarChart({
        basinCode: this.sgzgSelectBasin,
        adcd: this.sgzgSelectAdcd,
      });
      if (res.success) {
        let xData = [];
        let rData = [];
        let yData = [];
        let gData = [];
        let adcdList = [];
        if (res.rows.length > 0) {
          res.rows.forEach((element) => {
            if (element.red + element.yellow + element.green != 0) {
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
    // 一线海塘  he 二线海塘统计数据
    async getClqlData(name) {
      this.htjbtjData = {
        title: "",
        id: "seawallChart",
        unit: "条",
        color: ["#E02021", "#EB7E63", " #C447D6", "#3154EB", "#00FD6D"],
        series: [],
        totalNum: 0,
      }
      let res = await zhSeawall.seawallAline({
        basinCode: this.sgzgSelectBasin,
        adcd: this.sgzgSelectAdcd,
        extractPram: name, // 一线海塘  非一线海塘
      })
      if (res.success) {
        if (res.rows.length) {
          let totalNum = 0;

          let testArr = ['1', '2', '3', '4', '5']
          let arr = []

          for (let i = 0; i < res.rows.length; i++) {
            let sacle = ''
            arr.push(res.rows[i].project_scale)
            switch (res.rows[i].project_scale) {
              case '1':
                sacle = '一级'
                break;
              case '2':
                sacle = '二级'
                break;
              case '3':
                sacle = '三级'
                break;
              case '4':
                sacle = '四级'
                break;
              default:
                sacle = '非标'
                break;
            }
            let htName = sacle + "海塘";
            totalNum += res.rows[i].count;
            if (res.rows[i].project_scale != 6) {
              this.htjbtjData.series.push({
                name: htName,
                value: res.rows[i].count,
                color: this.getColor(res.rows[i].project_scale),
                project_scale: res.rows[i].project_scale,
              });
            }
          }
          testArr = testArr.filter(item => !arr.includes(item))
          testArr.forEach((item) => {
            this.htjbtjData.color.splice(item - 1, 1)
          })


          this.htjbtjData.totalNum = totalNum;
          this.htjbtjLoading = false;
        } else {

        }
      } else {
        this.htjbtjLoading = false;
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
    },

    getColor(project_scale) {
      let color = "";
      switch (project_scale) {
        case "1":
          color = "#E02021";
          break;
        case "2":
          color = "#EB7E63";
          break;
        case "3":
          color = "#C447D6";
          break;
        case "4":
          color = "#3154EB";
          break;
        default:
          color = "#00FD6D";
          break;
      }
      return color;
    },
    // 安全鉴定
    async getSafeData() {
      let res = await zhSeawall.safeStaNum({
        interType: 1,
        basinCode: this.sgzgSelectBasin,
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      });
      if (res.success) {
        this.safeData = res.data;
      }
    },
    // 除险加固
    async getXzyyData() {
      let opt = {
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd,
        basinCode: this.sgzgSelectBasin,
      };
      let res = await zhSeawall.reinforcementSta(opt);
      if (res.success) {
        this.cxjgStaticData = res.data;
      }
    },
    // 问题处置
    async getCXJGStatic(type) {
      let res = await zhSeawall.problemDealSta({
        gpType: type,
        basinCode: this.sgzgSelectBasin,
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      });
      if (res.success) {
        this.problemList[1].num = res.data.problemNum;
        this.problemList[2].num = res.data.dealingNum;
        this.problemList[3].num = res.data.dealedNum;
        this.problemList[0].num =
          res.data.problemNum + res.data.dealingNum + res.data.dealedNum;
      }
    },
    handleOpneSiwei() {
      this.siweiDialogVisible = true;
      clickLog({
        title: "智慧水库-除险保安",
      });
    },
    handleCloseSiwei() {
      this.fullscreen = false;
      this.siweiDialogVisible = false;
    },
    handleCloseCode() {
      this.fullscreen = false;
      this.coderuleDialogVisible = false;
    },
    chartClick(scale, type) {
      console.log(type, 'type');
      let temp = ''
      switch (+scale) {
        case 1:
          temp = '一级海塘'
          break;
        case 2:
          temp = '二级海塘'
          break;
        case 3:
          temp = '三级海塘'
          break;
        case 4:
          temp = '四级海塘'
          break;
        case 5:
          temp = '非标海塘'
          break;
      }
      this.$store.commit("SET_LEFT_CHART_CHOOSE", temp);
      // this.chooseMapType(type)
    },
    handleCloseCycle() {
      this.reservoirFullCycle = false;
    },
    chooseMapType(type, numberSelect) {
      if (type == '工程检查' || type == '发现问题') return;
      let numberSelected = numberSelect;
      clickLog({
        title: "智慧水库-" + type,
      });
      const noSelectValue = ["检查总量", "发现问题"];
      if (type === "安全鉴定" && !numberSelected) {
        numberSelected = "一类塘";
      }
      if (type === "一线海塘" || type === "非一线海塘") {
        console.log('1111111111111111111')
        this.legendIndex = 0
      }
      if (numberSelected) {
        if (!noSelectValue.includes(numberSelected)) {
          this.$store.commit("SET_NUMBER_SELECT", numberSelected);
        } else {
          return;
        }
      } else {
        this.$store.commit("SET_NUMBER_SELECT", "");
      }
      // 清除图表图例选中
      this.$store.commit("SET_LEFT_CHART_CHOOSE", '');
      //清除红黄码选中状态
      this.$store.commit("SET_COLORCODE", "all");
      this.$refs.seawallColorchart.initChart("all");

      // 关闭蓄水量浮窗开关
      this.$store.commit("SET_SKZG_SWITCH_XSL", false);

      this.$store.commit("SET_PLAN_ID", '');
      // 通用
      this.$parent.$refs.seawallMap.closeAllOverly(); // 关闭弹窗
      this.$store.commit("SET_SKZG_SWITCH_LEGEND", true); // 打开图例
      this.$store.commit("SET_SKZG_MAP_TYPE", type); //切换图例
    },
    showOneColor(color) {
      this.chooseMapType("风险研判");
      this.$refs.seawallColorchart.initChart(color);
      this.$store.commit("SET_COLORCODE", color);
    },
  },
  beforeDestroy() {
    this.timer = null;
  },
};
</script>

<style lang='scss' scoped>
.third_title:before {
  content: none;
}
.dialog-img-content {
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
    &.threeColor-huimin {
      width: 750px;
    }
  }
  &.degradedWork {
    height: 650px;
  }
  &.fullscreen-box .siwei-huimin {
    width: 70%;
  }
}
.slide_wrap {
  width: 100%;
  height: 100%;
  .slide_title {
    width: 100%;
    height: 48px;
    font-size: 36px;
    margin-bottom: 4px;
    font-family: "pangmen";
    @include flexbox();
    @include flexJC(center);
    @include flexAI(center);
    background: url($imageUrl + "title_bg.png") no-repeat center;
    background-size: cover;
    // cursor: pointer;
  }
  .left_top {
    width: 100%;
    height: calc(30% - 24px);
    overflow: hidden;
    .threeColorBtn {
      margin-left: 10px;
      width: 20px;
    }
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
    .first_box {
      width: 100%;
      @include flexbox();
      @include flexJC(center);
      @include flexAI(center);
      margin: 3px 0px;
      &.left_box {
        li {
          flex: 1;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: flex-start;
          padding: 0 5px;
          // cursor: pointer;
          span {
            margin: 0px 0px -4px;
            white-space: nowrap;
          }
          .total_text {
            line-height: 30px;
            font-size: 18px;
            padding-left: 5px;
            color: rgba(216, 235, 255, 0.75);
            @include flexbox();
            @include flexJC(center);
            @include flexAI(center);
          }
          .total_num {
            align-items: baseline;
            padding: 5px 5px 0 5px;
            font-size: 32px;
            // font-family: "youshe";
            font-weight: bold;
            margin: 0px;
            color: #46fdff;
            &.red {
              color: #e4443d;
            }
            &.yellow {
              color: #ded052;
            }
            &.green {
              color: #6eec61;
            }
          }
          .total_unit {
            font-size: 18px;
            color: rgb(255, 255, 255);
            transform: translate3d(10px, -2px, 0px);
          }
        }
        .circle_box {
          flex: 0.9;
          position: relative;
          img.index-out {
            position: absolute;
            width: 100%;
            animation: rotate 3s linear 2s infinite;
            @keyframes rotate {
              0% {
                transform: rotate(0deg);
              }
              100% {
                transform: rotate(360deg);
              }
            }
          }
          img.index-in {
            position: absolute;
            top: 25%;
            left: 25%;
            width: 50%;
          }
        }
      }
    }
    .securityChart {
      width: 100%;
      &.hasNum {
        height: calc(100% - 105px);
      }
      &.noNum {
        height: calc(100% - 105px);
      }
    }
  }
  .left_second {
    height: calc(27% - 24px);
    overflow: hidden;
    .pie_box {
      width: 80%;
      margin: 0 auto;
      height: 150px;
      // height: calc(50% - 150px);
      @include flexbox();
      position: relative;
      .htfbChart {
        width: 100%;
        height: 100%;
      }
      .chart_title {
        box-sizing: border-box;
        // width: 61%;
        // height: 100%;
        color: #fff;
        position: absolute;
        left: 41px;
        top: 29px;
        text-align: center;
        @include flexbox();
        @include flexAI(center);
        @include flexJC(center);
        p {
          font-size: 18px;
          font-weight: bold;
          padding-bottom: 5px;
          cursor: pointer;
          span {
            font-size: 14px;
          }
        }
        span {
          font-size: 14px;
          cursor: pointer;
        }
      }
      ul.xzyx_legend {
        height: 85%;
        width: 100%;
        color: #fff;
        @include flexbox();
        @include flexflow(column nowrap);
        @include flexAI(start);
        @include flexJC(center);
        box-sizing: border-box;
        > li {
          margin: 5px 0;
          @include flexbox();
          @include flexAI(center);
          @include flexJC(center);
          &.active {
            font-weight: bold;
            color: #69e3ee;
          }
          span {
            &.little_block {
              display: inline-block;
              width: 5px;
              height: 5px;
              margin-right: 10px;
            }
            font-size: 16px;
            &.name {
              // font-family: "youshe";
            }
            &.unit {
              margin-left: 5px;
              font-family: "youshe";
            }
          }
        }
      }
    }
  }
  .left_third {
    height: calc(23% - 16px);
    overflow: hidden;
    .aqjd_box {
      height: 60%;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(space-between);
      padding: 0 50px 0 10px;
      .aqjd_item {
        width: 100%;
        @include flexbox();
        @include flexAI(center);
        @include flexJC(flex-start);
        flex-direction: column;
        padding: 10px 0 5px 0;
        &.active {
          h4 {
            background: url($imageUrl + "cl-active.png") no-repeat left;
            background-size: 20px 100%;
            box-sizing: border-box;
            color: #69e3ee;
            font-weight: 500;
          }
          p {
            span {
              color: #69e3ee;
            }
          }
        }
        h3 {
          font-size: 16px;
          padding: 0 10px;
        }
        p {
          font-size: 22px;
          font-family: "youshe";
          padding-top: 10px;
          span {
            font-size: 18px;
            font-family: none;
            margin-left: 5px;
            color: #fff;
          }
        }
        li {
          user-select: none;
          margin: 0px 0px;
          width: 100%;
          @include flexbox();
          @include flexAI(center);
          @include flexJC(flex-start);
          .aqjd_title {
            font: normal bold 20px/1.5 "youshe";
            color: #36c0e4;
            background: linear-gradient(45deg, #36c0e4 50%, #0639ff 100%);
            background-clip: text;
            -webkit-background-clip: text;
            text-shadow: none;
            color: transparent;
          }
          p {
            display: inline-block;
            font-size: 22px;
            font-family: "youshe";
            padding-left: 20px;
            span {
              font-size: 18px;
              font-family: "youshe";
              text-shadow: 0px 0px 16px #00a9ff;
            }
          }
          span {
            font-size: 16px;
            font-family: "youshe";
            color: #defcff;
            text-shadow: 0px 0px 16px #00a9ff;
          }
        }
        i.small_triangle {
          width: 20px;
          height: 20px;
          background: center/100% url($imageUrl + "small_triangle.png")
            no-repeat;
        }
        i.lines {
          display: inline-block;
          height: 1px;
          @include flex(1.5);
          border-bottom: 1px solid #4c70a3;
          margin: 0 10px;
        }
      }
    }
    .third_box {
      margin: 8px 0;
      .tab_box {
        width: calc(100% - 20px);
        display: grid;
        grid-template-columns: 1fr 1fr 1fr;
        gap: 6px 4px;
        padding: 10px 0;
        font-size: 22px;
        text-align: left;
        li {
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          align-items: start;
          p {
            perspective: 138px;
            font-family: "youshe";
            width: 80%;
            text-align: center;
            color: rgb(20, 245, 241, 0.9);
            margin: 0 0 0 5px;
          }
          .tab_box_item {
            @include flexbox;
            @include flexAI(center);
          }
          div {
            margin: 5px 0px;
            span {
              font-size: 16px;
              color: rgba(230, 247, 255, 0.8);
              margin: 0 3px;
            }
            .ba_num {
              font-family: "manrope";
              display: flex;
              font-size: 20px;
              flex: 0 1.5 0%;
              align-items: center;
              color: #fff;
              .ba_unit {
                color: rgba(230, 247, 255, 0.8);
                font-size: 16px;
                margin: 0 6px;
              }
            }
          }
        }
      }
    }
  }
  .left_four {
    height: calc(25% - 28px);
    overflow: hidden;
    .problem_box {
      height: calc(100% - 5px);
      .gs_list {
        @include flexbox();
        @include flexAI(center);
        @include flexflow(row wrap);
        width: 90%;
        height: calc(100% - 50px);
        box-sizing: border-box;
        padding: 10px 0;
        margin: 0 auto;
        .gs_item {
          width: 45%;
          height: 45%;
          @include flexbox();
          @include flexAI(center);
          box-sizing: border-box;
          // padding-left: 15px;
          // margin-top: 50px;
          &.pl {
            width: 52%;
            // padding-left: 20px;
          }
          img {
            width: 28px;
            margin-right: 10px;
          }
          &:nth-child(2n + 1) {
            div {
              padding: 5px;
            }
          }
          div {
            h3 {
              font-size: 17px;
              padding-top: 4px;
              padding-bottom: 8px;
              color: rgba(230, 247, 255, 0.8);
            }
            p {
              // color: rgb(15, 146, 255);
              color: rgba(230, 247, 255, 0.8);
              font-size: 16px;
              span {
                vertical-align: text-bottom;
                font-family: youshe;
                color: #fff;
                font-size: 24px;
                margin-right: 4px;
              }
            }
          }
        }
      }
    }
  }
  .second_title2 {
    padding: 0 0 0 37px;
    margin-bottom: 10px;
    transition: all 0.3s linear;
    font-family: "youshe";
    width: 100%;
    height: 40px;
    font-size: 30px;
    @include flexbox();
    @include flexJC(flex-start);
    @include flexAI(center);
    background: url($imageUrl + "second_title.png") no-repeat bottom;
    background-size: 100% auto;
    // cursor: pointer;
    box-sizing: border-box;
    z-index: 2;
    &.last_title {
      display: flex;
      flex-direction: row;
      align-items: center;
      span {
        transition: all 0.2s linear;
        margin-right: 10px;
        font-size: 24px;
        color: rgba(226, 237, 255, 0.5);
        &.active {
          font-size: 30px;
          color: rgb(238, 246, 255);
        }
      }
    }
    .codeRules {
      display: inline-block;
      margin-left: 10px;
      width: 24px;
      height: 24px;
      background: center/100% url($imageUrl + "coderules.png");
    }
  }

  .third_title {
    @include flexbox();
    @include flexAI(center);
    // @include flexJC();
    font-weight: bold;
    color: #fff;
    margin-bottom: 4px;
    ::before {
      content: none;
    }
    .point {
      width: 20px;
      margin-right: 5px;
    }
    span {
      font-family: "youshe";
      color: rgba(226, 237, 255, 0.5);
      font-size: 20px;
      user-select: none;
      margin-right: 10px;
      // transition: all 0.2s linear 0s;
      &.active {
        font-size: 24px;
        color: #fff;
      }
    }
    i {
      display: block;
      height: 1px;
      @include flex(1);
      border-bottom: 1px solid #4c70a3;
      margin: 0 10px;
    }
    .arr {
      width: 30px;
    }
  }
}
</style>
<style  lang='scss'>
.color_detail_popper {
  left: 40px !important;
}
.four_box {
  .circle_ring {
    width: 24px;
    height: 24px;
    font-family: manrope-semibold;
    font-size: 16px;
    color: rgb(255, 255, 255);
    font-weight: normal;
    line-height: 24px;
    background: url(https://easyv.assets.dtstack.com/components/static-image/tableList/tableList.png)
      50% 50% / 100% 100% no-repeat;
    font-style: normal;
  }
  .el-table,
  .el-table__expanded-cell {
    color: white;
    background-color: transparent;
  }
  .el-table--enable-row-hover .el-table__body tr:hover > td {
    background-color: transparent;
  }
  .el-table__body-wrapper::-webkit-scrollbar {
    width: 0;
  }
  /* 表格内背景颜色 */
  .el-table th,
  .el-table tr,
  .el-table td {
    border: 0;
    color: white;
    background-color: #052176;
  }
  /* 双数行背景颜色 */
  .el-table--striped .el-table__body tr.el-table__row--striped td {
    background-color: #052176 !important;
  }
  /* 使表格背景透明 */
  .el-table th,
  .el-table tr {
    background-color: #052176;
  }
  .el-table td,
  .el-table th {
    padding: 3px 0;
  }
  /* 删除表格下横线 */
  .el-table::before {
    left: 0;
    bottom: 0;
    width: 100%;
    height: 0px;
  }
  /* 表格表头字体颜色 */
  .el-table thead {
    color: white;
    font-weight: 500;
  }
}
.viewer-container {
  z-index: 9999 !important;
}
</style>