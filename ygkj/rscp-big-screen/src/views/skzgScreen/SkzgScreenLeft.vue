<template>
  <div ref="slide_wrap" class="slide_wrap">
    <div class="slide_title" @click="handleOpneSiwei">除险保安</div>
    <section class="left_top">
      <p class="second_title2">
        风险研判<img class="threeColorBtn" @click="coderuleDialogVisible = true" :src="$config.imgUrl + 'threeColorBtn.png'" title="三色码规则" />
      </p>
      <div class="all_num" v-if="showZjsNubmer">
        <p>
          总数：<span>4296 (</span> 注册:<span class="cursor" @click="registerVisible = true">9，</span>注销:<span class="cursor" @click="DegradedWorkVisible = true">34</span>
          )座
        </p>
        <div></div>
      </div>
      <ul ref="first_box" class="left_box first_box">
        <li @click="chooseMapType('风险研判')" :class="
            sgzgMapType == '风险研判' && colorCode == 'all'
              ? 'numberActive'
              : ''
          ">
          <!-- <el-popover v-if="showZjsNubmer" trigger="hover" popper-class="color_detail_popper" placement="bottom" width="160">
            <div class="color_detail_box">
              <p>水库总数：<span style="color:#00E6F1">4296</span>座</p>
              <p>降等报废：<span style="color:#DED052">19</span>座</p>
            </div>
            <div slot="reference" class="title_img_box">
              <span class="total_text">总数<img style="margin-bottom:8px;width:12px;margin-left:5px" class="threeColorBtn" :src="$config.imgUrl + 'detailinfo.png'"></span>
            </div>
          </el-popover> -->
          <span class="total_text">在册</span>
          <span class="total_num">{{ colorsNum.TotalCount }}<span class="total_unit">座</span></span>
        </li>
        <li @click="showOneColor('red')" :class="
            sgzgMapType == '风险研判' && colorCode == 'red'
              ? 'numberActive'
              : ''
          ">
          <span class="total_text">红码</span>
          <span class="total_num red">{{ colorsNum.RedCount }}<span class="total_unit">座</span></span>
        </li>
        <li @click="showOneColor('yellow')" :class="
            sgzgMapType == '风险研判' && colorCode == 'yellow'
              ? 'numberActive'
              : ''
          ">
          <span class="total_text">黄码</span>
          <span class="total_num yellow">{{ colorsNum.yellowCount }}<span class="total_unit">座</span></span>
        </li>
        <li @click="showOneColor('green')" :class="
            sgzgMapType == '风险研判' && colorCode == 'green'
              ? 'numberActive'
              : ''
          ">
          <span class="total_text">绿码</span>
          <span class="total_num green">{{ colorsNum.GreenCount }}<span class="total_unit">座</span></span>
        </li>
      </ul>
      <bar-chart-colors-code :class="['securityChart', showZjsNubmer ? 'hasNum' : 'noNum']" :chartData="barData" ref="threeColorchart" />
    </section>

    <section class="left_bottom">
      <p class="second_title2">风险管控</p>
      <p class="third_title">
        <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
        <span :class="[
            tabIndex0 == index ? 'active' : '',
            sgzgMapType == item ? 'mapTypeActive' : '',
          ]" v-for="(item, index) in tabList0" :key="item" @click="(tabIndex0 = index), chooseMapType(item)">{{ item }}</span>
        <i></i>
        <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" />
      </p>
      <div v-if="tabIndex0 == 0" class="aqjd_box">
        <div @click="chooseMapType('安全鉴定', '三类坝')" class="aqjd_item cursor" :class="numberSelected == '三类坝' ? 'numberActive' : ''" style="width: auto">
          <h3>三类坝</h3>
          <p>{{ safeData.three }}<span>座</span></p>
        </div>
        <div @click="chooseMapType('安全鉴定', '二类坝')" class="aqjd_item cursor" :class="numberSelected == '二类坝' ? 'numberActive' : ''" style="width: auto">
          <h3>二类坝</h3>
          <p>{{ safeData.two }}<span>座</span></p>
        </div>
        <div @click="chooseMapType('安全鉴定', '近一年到期')" class="aqjd_item cursor" :class="numberSelected == '近一年到期' ? 'numberActive' : ''" style="width: auto">
          <h3>近一年到期</h3>
          <p>{{ safeData.due_num }}<span>座</span></p>
        </div>

        <!-- 除险加固统计项 新设计图 -->
        <!-- <ul class="aqjd_item">
          <li>
            <i class="small_triangle"></i>
            <span class="aqjd_title">当年已鉴定</span>
            <p>{{safeData.dnjdCount}} <span>座</span> </p>
            <i class="lines"></i>
          </li>
          <li style="width:110%;margin-left: 10%;">
            <i class="small_triangle"></i>
            <span class="aqjd_title">鉴定结论</span>
            <span style="padding-left: 36px;font-size:14px">一类坝</span> <span style="color:#6EEC61;font-size:22px;margin:0 4px"> {{safeData.ylbCount}}</span> <span style="font-size:14px">座</span>
            <span style="padding-left: 10px;font-size:14px">二类坝</span> <span style="color:#DED052;font-size:22px;margin:0 4px;"> {{safeData.elbCount}}</span> <span style="font-size:14px">座</span>
            <span style="padding-left: 10px;font-size:14px">三类坝</span> <span style="color:#E4443D;font-size:22px;margin:0 4px"> {{safeData.slbCount}}</span> <span style="font-size:14px">座</span>
          </li>
          <li>
            <i class="small_triangle"></i>
            <span class="aqjd_title">近一年到期</span>
            <p>{{safeData.due_num}} <span>座</span> </p>
            <i class="lines"></i>
          </li>
        </ul> -->
      </div>
      <div v-if="tabIndex0 == 1" class="third_box">
        <ul class="tab_box">
          <li>
            <p>总数</p>
            <div class="cursor tab_box_item" :class="numberSelected == '总数,二类坝' ? 'numberActive' : ''" @click="chooseMapType('除险加固', '总数,二类坝')">
              <span class="ba_text">二类坝</span>
              <span class="ba_num">
                {{ safeData.two }}
                <span class="ba_unit">座</span>
              </span>
            </div>
            <div class="cursor tab_box_item" :class="numberSelected == '总数,三类坝' ? 'numberActive' : ''" @click="chooseMapType('除险加固', '总数,三类坝')">
              <span class="ba_text">三类坝</span>
              <span class="ba_num">
                {{ safeData.three }}
                <span class="ba_unit">座</span>
              </span>
            </div>
          </li>
          <li>
            <p>未开工</p>
            <div class="cursor tab_box_item" :class="numberSelected == '未开工,二类坝' ? 'numberActive' : ''" @click="chooseMapType('除险加固', '未开工,二类坝')">
              <span class="ba_text">二类坝</span>
              <span class="ba_num">
                {{ cxjgStaticData.twowkg }}
                <span class="ba_unit">座</span>
              </span>
            </div>
            <div class="cursor tab_box_item" :class="numberSelected == '未开工,三类坝' ? 'numberActive' : ''" @click="chooseMapType('除险加固', '未开工,三类坝')">
              <span class="ba_text">三类坝</span>
              <span class="ba_num">
                {{ cxjgStaticData.threewkg }}
                <span class="ba_unit">座</span>
              </span>
            </div>
          </li>
          <li>
            <p>已开工</p>
            <div class="cursor tab_box_item" :class="numberSelected == '已开工,二类坝' ? 'numberActive' : ''" @click="chooseMapType('除险加固', '已开工,二类坝')">
              <span class="ba_text">二类坝</span>
              <span class="ba_num">
                {{ cxjgStaticData.twoykg }}
                <span class="ba_unit">座</span>
              </span>
            </div>
            <div class="cursor tab_box_item" :class="numberSelected == '已开工,三类坝' ? 'numberActive' : ''" @click="chooseMapType('除险加固', '已开工,三类坝')">
              <span class="ba_text">三类坝</span>
              <span class="ba_num">
                {{ cxjgStaticData.threeykg }}
                <span class="ba_unit">座</span>
              </span>
            </div>
          </li>
          <!-- <li>
            <p>当年完工</p>
            <div><span class="ba_text">二类坝</span>
              <span class="ba_num cursor" @click="chooseMapType('除险加固', '当年完工,二类坝')"> {{cxjgStaticData.twowg}}
                <span class="ba_unit">座</span>
              </span>
            </div>
            <div><span class="ba_text">三类坝</span>
              <span class="ba_num cursor" @click="chooseMapType('除险加固', '当年完工,三类坝')"> {{cxjgStaticData.threewg}}
                <span class="ba_unit">座</span>
              </span>
            </div>
          </li> -->
        </ul>
      </div>
      <!-- <div v-if="tabIndex0 == 2" class="aqjd_box2">
        <div class="aqjd_top">
          <p>总数<span class="cursor" @click="chooseMapType('存量清零', '已竣工,处置中')">{{bxskNum.totalNum || 0}}</span>座
          <span></span>
          已竣工<span class="cursor" @click="chooseMapType('存量清零', '已竣工')">{{bxskNum.totalNum > bxskNum.stockNum ? bxskNum.totalNum - bxskNum.stockNum : 0}}</span>座
          </p>
        </div>
        <div class="aqjd_bottom">
          <div class="aqjd_item" style="width: auto; padding-right:20px; border-right: 1px solid #5E7FBA;">
            <h3>处置中</h3>
            <p style="color:#E4443D;" @click="chooseMapType('存量清零', '处置中')">{{bxskNum.stockNum || 0}}<span>座</span></p>
          </div>

          <div class="aqjd_item" style="width: auto;">
            <h3>未开工</h3>
            <p style="color:#FFB32A;" @click="chooseMapType('存量清零', '处置中')">50<span>座</span></p>
          </div>
          <div class="aqjd_item" style="width: auto;">
            <h3>加固中</h3>
            <p style="color:#5EA2FF;" @click="chooseMapType('存量清零', '处置中')">64<span>座</span></p>
          </div>
          <div class="aqjd_item" style="width: auto;">
            <h3>已完工</h3>
            <p style="color:#6EEC61;" @click="chooseMapType('存量清零', '处置中')">89<span>座</span></p>
          </div>
          
        </div>
      </div> -->
      <div v-if="tabIndex0 == 2" class="aqjd_box2">
        <div class="aqjd_top cursor" :class="
            numberSelected == '已竣工,加固中,已完工,项目前期'
              ? 'numberActive'
              : ''
          " @click.self="
            chooseMapType('存量清零', '已竣工,加固中,已完工,项目前期')
          ">
          <!-- 第一行：总数   二行：前期准备、加固中、已完工、已竣工  -->
          <p>
            总数<span class="cursor" @click.self="
                chooseMapType('存量清零', '已竣工,加固中,已完工,项目前期')
              ">{{ bxskNum.totalNum || 0 }}</span>座
            <span></span>
          </p>
        </div>
        <div class="aqjd_bottom">
          <div class="aqjd_item cursor" style="width: auto" :class="numberSelected == '项目前期' ? 'numberActive' : ''" @click="chooseMapType('存量清零', '项目前期')">
            <h3>项目前期</h3>
            <p style="color: #e4443d; cursor: pointer" @click="chooseMapType('存量清零', '项目前期')">
              {{ bxskNum.xmqq || 0 }}<span>座</span>
            </p>
          </div>
          <div class="aqjd_item cursor" style="width: auto" :class="numberSelected == '加固中' ? 'numberActive' : ''" @click="chooseMapType('存量清零', '加固中')">
            <h3>加固中</h3>
            <p style="color: #ffb32a; cursor: pointer" @click="chooseMapType('存量清零', '加固中')">
              {{ bxskNum.jgz || 0 }}<span>座</span>
            </p>
          </div>
          <div class="aqjd_item cursor" style="width: auto" :class="numberSelected == '已完工' ? 'numberActive' : ''" @click="chooseMapType('存量清零', '已完工')">
            <h3>已完工</h3>
            <p style="color: #5ea2ff; cursor: pointer" @click="chooseMapType('存量清零', '已完工')">
              {{ bxskNum.ywg || 0 }}<span>座</span>
            </p>
          </div>
          <div class="aqjd_item cursor" style="width: auto" :class="numberSelected == '已竣工' ? 'numberActive' : ''" @click="chooseMapType('存量清零', '已竣工')">
            <h3>已竣工</h3>
            <p style="color: #6eec61; cursor: pointer" @click="chooseMapType('存量清零', '已竣工')">
              {{ bxskNum.yjg || 0 }}<span>座</span>
            </p>
          </div>
        </div>
      </div>

      <p class="third_title">
        <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
        <span :class="[
            tabIndex1 == index ? 'active' : '',
            sgzgMapType == item ? 'mapTypeActive' : '',
          ]" v-for="(item, index) in tabList1" :key="item" @click="(tabIndex1 = index), chooseMapType(item)">{{ item }}</span>
        <i></i>
        <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" />
      </p>
      <div class="pie3d_box" v-if="tabIndex1 === 0">
        <div id="xzyx_chart"></div>
        <div class="chart_title">
          <div @click="chooseMapType('限制运用', xzyxData.name)">
            <p>
              {{ xzyxData.num !== undefined ? xzyxData.num : "-"
              }}<span>座</span>
            </p>
            <span>{{ xzyxData.name }}</span>
          </div>
        </div>
        <div class="xzyx_legend">
          <!-- <p v-if="inXq"><i class="cxx"></i><span>超汛限水位</span></p> -->
          <p class="cursor" :class="numberSelected == '超设计洪水位' ? 'numberActive' : ''" @click="chooseMapType('限制运用', '超设计洪水位')">
            <i class="czc"></i><span>超设计洪水位<strong>{{
                this.xzyxArr.length ? this.xzyxArr[0].num : 0
              }}</strong>座</span>
          </p>
          <p class="cursor" :class="numberSelected == '超限制水位' ? 'numberActive' : ''" @click="chooseMapType('限制运用', '超限制水位')">
            <i class="cfhg"></i><span>超限制水位<strong>{{
                this.xzyxArr.length ? this.xzyxArr[1].num : 0
              }}</strong>座</span>
          </p>
        </div>
      </div>
      <div class="pie3d_box" v-else>
        <div id="slb_chart"></div>
        <div class="chart_title">
          <div @click="chooseMapType('三类坝', damThreeData.name)">
            <p>
              {{ damThreeData.num !== undefined ? damThreeData.num : "-"
              }}<span>座</span>
            </p>
            <span>{{ damThreeData.name }}</span>
          </div>
        </div>
        <div class="xzyx_legend">
          <p class="cursor" :class="numberSelected == '超限运行' ? 'numberActive' : ''" @click="chooseMapType('三类坝', '超限运行')">
            <i class="czc"></i><span>超限运行<strong>{{
                this.damThreeArr.length ? this.damThreeArr[1].num : 0
              }}</strong>座</span>
          </p>

          <p class="cursor" :class="numberSelected == '空库运行' ? 'numberActive' : ''" @click="chooseMapType('三类坝', '空库运行')">
            <i class="cfhg"></i><span>空库运行<strong>{{
                safeData.three - damThreeArr[1].num
                  ? safeData.three - damThreeArr[1].num
                  : 0
              }}</strong>座</span>
          </p>
          <!-- <p class="cursor" :class="numberSelected == '空库运行' ? 'numberActive' : ''" @click="chooseMapType('三类坝', '空库运行')"><i class="cfhg"></i><span>空库运行<strong>{{this.damThreeArr.length ? this.damThreeArr[0].num : 0}}</strong>座</span></p> -->
        </div>
      </div>

      <p class="third_title" @click="chooseMapType('问题处置')">
        <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
        <span class="active" :class="sgzgMapType == '问题处置' ? 'mapTypeActive' : ''">问题处置</span>
        <i></i>
        <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" />
      </p>
      <div class="problem_box">
        <ul class="gs_list">
          <li class="gs_item" v-for="(item, index) in problemList" :key="index">
            <img :src="item.src" alt="" />
            <div class="cursor" :class="numberSelected == item.status ? 'numberActive' : ''" @click="chooseMapType('问题处置', item.status)">
              <h3>{{ item.status }}</h3>
              <p>
                <span>{{ item.num }}</span>{{ item.unit }}
              </p>
            </div>
          </li>
          <!-- <li v-for="(item,index) in problemList" :key="index">
            <div class="pic_box">
              <img :src="item.src" class="index-in" alt="">
              <img :src="$config.imgUrl + 'twinkling-bg.png'" class="index-out" alt="">
            </div>
            <div class="pic_bottom cursor" :class="numberSelected == item.status ? 'numberActive' : ''" @click="chooseMapType('问题处置', item.status)">
              <p class="pic_bottom_title">{{item.status}}</p>
              <p><span class="th_num">{{item.num}}</span><span class="th_unit">{{item.unit}}</span></p>
            </div>
          </li> -->
        </ul>
      </div>
    </section>

    <primary-dialog mainTitle="" :visible="siweiDialogVisible" @handleClose="handleCloseSiwei" @changeFullscreen="changeFullscreen" :fullscreen="fullscreen" width="1200px" append-to-body top="6vh">
      <div class="dialog-img-content" :class="{'fullscreen-box': fullscreen}" v-viewer>
        <img class="huimin siwei-huimin" :src="$config.imgUrl + 'whiteSiwei.png'" alt="" />
      </div>
      <!-- <p style="text-align: center;color: #fff;padding-top:5px;font-size: 20px;">点击可放大图片</p> -->
    </primary-dialog>

    <!-- 三色码 规则展示 -->
    <primary-dialog mainTitle="三色码规则" :visible="coderuleDialogVisible" @handleClose="handleCloseCode" @changeFullscreen="changeFullscreen" :fullscreen="fullscreen" width="850px" top="5vh" append-to-body>
      <div class="dialog-img-content threeColor" :class="{'fullscreen-box': fullscreen}" v-viewer>
        <img class="huimin threeColor-huimin" :src="$config.imgUrl + 'threeColorImg.png'" alt="三色码规则" />
      </div>
    </primary-dialog>
    <!-- 新注册工程列表 -->
    <primary-dialog mainTitle="新注册工程列表" :visible="registerVisible" @changeFullscreen="changeFullscreen" :fullscreen="fullscreen" @handleClose="handleCloseRegister" width="70%" top="5vh" append-to-body>
      <div class="dialog-img-content degradedWork" :class="{'fullscreen-box': fullscreen}" v-viewer>
        <img class="huimin" :src="$config.imgUrl + 'NewDegradedWorksList4.png'" alt="新注册工程列表" />
      </div>
    </primary-dialog>
    <!-- 降等工程列表 -->
    <primary-dialog mainTitle="降等工程列表" :visible="DegradedWorkVisible" @changeFullscreen="changeFullscreen" :fullscreen="fullscreen" @handleClose="handleCloseDegraded" width="70%" top="5vh" append-to-body>
      <div class="dialog-img-content degradedWork" :class="{'fullscreen-box': fullscreen}" v-viewer>
        <img class="huimin" :src="$config.imgUrl + 'NewDegradedWorksList5.png'" alt="降等工程列表" />
      </div>
    </primary-dialog>

    <!-- 水库全周期管理流程图 -->
    <primary-dialog mainTitle="水库全周期管理流程图" :visible="reservoirFullCycle" @changeFullscreen="changeFullscreen" :fullscreen="fullscreen" @handleClose="handleCloseCycle" width="80%" top="10vh" append-to-body>
      <div class="dialog-img-content threeColor" :class="{'fullscreen-box': fullscreen}" v-viewer>
        <img class="huimin" :src="$config.imgUrl + 'reservoirfcmf.png'" alt="水库全周期管理流程图" />
      </div>
    </primary-dialog>
  </div>
</template>

<script>
import Highcharts from "highcharts";
import SecurityBarChart from "./components/SecurityBarChart.vue";
import BarChartColorsCode from "./components/BarChartColorsCode";
import {
  reservoirBigScreen,
  xzyyStatisticApi,
  threeMalCount,
  threeYardBarChart,
  safeAppraisalApi,
  cyjgNumApi,
  generateProject,
  clickLog,
} from "@/api/api_reservoir";
import PrimaryDialog from "./components/PrimaryDialog.vue";
export default {
  name: "SgzgScreenLeft",
  props: {},
  components: {
    SecurityBarChart,
    BarChartColorsCode,
    PrimaryDialog,
  },
  computed: {
    sgzgMapType() {
      return this.$store.state.sgzgMapType;
    },
    sgzgSelectAdcd() {
      return this.$store.state.sgzgSelectAdcd;
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
        // this.getDamThreeData()
        this.getCXJGStatic(3);
        this.getCXJGStatic(5);
        this.getClqlData();
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
      if (val === 0) {
        this.getXzyyData();
      } else {
        this.getDamThreeData();
      }
    },
  },
  data() {
    return {
      siweiDialogVisible: false,
      coderuleDialogVisible: false,
      DegradedWorkVisible: false,
      registerVisible: false,
      reservoirFullCycle: false,
      kaigongList: [
        { name: "未开工" },
        { name: "已开工" },
        { name: "当年完工" },
      ],
      barLoading: true,
      barHeight: 150,
      barData: {
        id: "colorsCodeChart",
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
          status: "检查总量",
          num: 0,
          unit: "座次",
          src: this.$config.imgUrl + "totalInspection.png",
        },
        {
          status: "发现问题",
          num: 0,
          unit: "项",
          src: this.$config.imgUrl + "problemFound.png",
        },
        {
          status: "处置中",
          num: 0,
          unit: "项",
          src: this.$config.imgUrl + "underDisposal.png",
        },
        {
          status: "已处置",
          num: 0,
          unit: "项",
          src: this.$config.imgUrl + "hadDispose.png",
        },
      ],

      timer: null,
      fullscreen: false,
      damTimer: null,
      timerIndex: 0,
      damTimerIndex: 0,
      cxjgStaticData: [],
      colorsNum: {},
      inXq: false,
      showColor: "all",
      tabList0: ["安全鉴定", "除险加固", "存量清零"],
      tabList1: ["限制运用", "三类坝"],
      tabIndex0: 0,
      tabIndex1: 0,
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
  mounted() {
    // this.barHeight = this.$refs.slide_wrap.offsetHeight - (this.$refs.four_box.offsetHeight + this.$refs.third_box.offsetHeight + this.$refs.third_box.offsetHeight + 100 + 320 + 150)
    this.getXzyyData();
    // this.getDamThreeData()
    this.getCXJGStatic(3);
    this.getCXJGStatic(5);
    this.getClqlData();
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
  methods: {
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
      // this.$nextTick(() => {
      // 	this.$refs.generation.$refs.tb_wrappers.doLayout();
      // });
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
    async getColorsNum() {
      let res = await threeMalCount({
        basinCode: this.sgzgSelectBasin,
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      });
      if (res.success) {
        this.colorsNum = res.data;
      }
    },
    async getColorsBarData() {
      this.barLoading = true;
      let res = await threeYardBarChart({
        basinCode: this.sgzgSelectBasin,
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      });
      if (res.success) {
        let xData = [];
        let rData = [];
        let yData = [];
        let gData = [];
        let adcdList = [];
        if (res.data.length > 0) {
          res.data.forEach((element) => {
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

    async getCXJGStatic(type) {
      let res = await xzyyStatisticApi({
        gpType: type,
        basinCode: this.sgzgSelectBasin,
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      });
      if (res.success) {
        if (type === 3) {
          this.cxjgStaticData = res.data[0];
        } else if (type === 5) {
          if (res.success) {
            this.problemList[0].num = res.data[0].checkNum;
            this.problemList[1].num = res.data[0].problemNum;
            this.problemList[2].num = res.data[0].czzNum;
            this.problemList[3].num = res.data[0].yczNum;
          }
        }
      }
    },
    // 存量清零
    async getClqlData() {
      let res = await cyjgNumApi({
        interType: 1, //统计
        basinCode: this.sgzgSelectBasin,
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      });
      if (res.success) {
        this.bxskNum = res.data;
      }
    },

    async getSafeData() {
      let res = await safeAppraisalApi({
        interType: 1,
        basinCode: this.sgzgSelectBasin,
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      });
      if (res.success) {
        this.safeData = res.data;
      }
      this.getDamThreeData();
    },
    handleOpneSiwei() {
      this.siweiDialogVisible = true;
      clickLog({
        title: "智慧水库-除险保安"
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
    handleCloseRegister() {
      this.registerVisible = false;
    },
    handleCloseDegraded() {
      this.DegradedWorkVisible = false;
    },
    handleCloseCycle() {
      this.reservoirFullCycle = false;
    },
    // 超限 3d 数据
    async getXzyyData() {
      this.xzyxData = {
        name: "超限制水位",
        num: 0,
      };
      // let res = await xzyyStatisticApi({
      //   gpType: 4,
      //   basinCode: this.sgzgSelectBasin,
      //   adcd: this.sgzgSelectAdcd
      //     ? this.sgzgSelectAdcd
      //     : this.$localData("get", "userInfo").adcd,
      // });
      let opt = {
        adcd: this.sgzgSelectAdcd ? this.sgzgSelectAdcd : this.$localData("get", "userInfo").adcd,
        basinCode: this.sgzgSelectBasin,
        gpType: 4,
        screen: '11,14',
        name: ''
      }
      let res = await generateProject(opt);
      if (res.success) {
        // this.timer = null
        let pointArr = {
          '超限制水位': [],
          '超设计洪水位': []
        }
        res.data.map((v, index) => {
          switch (v.Ltstatus) {
            case 11:
              pointArr['超限制水位'].push(v)
              break;
            case 14:
              pointArr['超设计洪水位'].push(v)
              break;
          }
        });

        let depth = 40;
        let LTLimitCnt = pointArr['超限制水位'].length ? pointArr['超限制水位'].length : 0;
        let LtDsflzCnt = pointArr['超设计洪水位'].length ? pointArr['超设计洪水位'].length : 0;

        this.xzyxArr = [
          {
            name: "超设计洪水位",
            num: LtDsflzCnt,
          },
          {
            name: "超限制水位",
            num: LTLimitCnt,
          },
        ];
        this.xzyxData = {
          name: "超设计洪水位",
          num: LTLimitCnt,
        };

        let series = [
          {
            name: "超设计洪水位",
            color: "rgba(120,252,253,1)",
            y: LtDsflzCnt,
          },
          {
            name: "超限制水位",
            color: "rgba(67,148,230,1)",
            y: LTLimitCnt,
          },
        ];
        await this.initPie3D(depth, series);
        if (this.timer) clearInterval(this.timer);
        this.timer = setInterval(() => {
          this.xzyxData = this.xzyxArr[this.timerIndex];
          this.timerIndex++;
          if (this.timerIndex > 1) {
            this.timerIndex = 0;
          }
          series.map((v) => {
            v.sliced = false;
          });
          if (this.timerIndex === 0) {
            series[0].color = "rgba(120,252,253,.3)";
            series[1].color = "rgba(67,148,230,1)";
          } else {
            series[0].color = "rgba(120,252,253,1)";
            series[1].color = "rgba(67,148,230,.3)";
          }
          this.chart.series[0].setData(series);
          this.chart.redraw();
        }, 3000);
      }
    },
    // 三类坝 3d 数据
    async getDamThreeData() {
      this.damThreeData = {
        name: "空库运行",
        num: 0,
      };
      // let res = await generateProject({
      //   gpType: 4,
      //   dam: 3,
      //   interType: 1,
      //   extractPram: 1,
      //   basinCode: this.sgzgSelectBasin,
      //   adcd: this.sgzgSelectAdcd
      //     ? this.sgzgSelectAdcd
      //     : this.$localData("get", "userInfo").adcd,
      // });
      let opt = {
        adcd: this.sgzgSelectAdcd ? this.sgzgSelectAdcd : this.$localData("get", "userInfo").adcd,
        basinCode: this.sgzgSelectBasin,
        gpType: 4,
        screen: '11,122',
        dam: 3,
        name: '',
        extractPram: '1'
      }
      let res = await generateProject(opt);
      if (res.success) {
        let pointArr = {
          '空库运行': [],
          '超限运行': [],
        }
        res.data.map((v, index) => {
          switch (v.Ltstatus) {
            case 11:
              pointArr['超限运行'].push(v)
              break;
            case 122:
              pointArr['空库运行'].push(v)
              break;
          }
        });
        let depth = 40;
        let kknum = pointArr['空库运行'].length ? pointArr['空库运行'].length
          : 0;
        let xznum = pointArr['超限运行'].length ? pointArr['超限运行'].length : 0;
        this.damThreeArr = [
          {
            name: "空库运行",
            num: kknum,
          },
          {
            name: "超限运行",
            num: xznum,
          },
        ];
        this.damThreeData = {
          name: "空库运行",
          num: kknum,
        };
        let series = [
          {
            name: "空库运行",
            color: "rgba(67,148,230,1)",
            y: kknum,
          },
          {
            name: "超限运行",
            color: "rgba(120,252,253,1)",
            y: xznum,
          },
        ];
        this.initDamPie3D(depth, series);
        if (this.damTimer) clearInterval(this.damTimer);
        this.damTimer = setInterval(() => {
          this.damThreeData = this.damThreeArr[this.damTimerIndex];
          ++this.damTimerIndex;
          if (this.damTimerIndex > 1) {
            this.damTimerIndex = 0;
          }
          series.map((v) => {
            v.sliced = false;
          });
          // 有报错
          if (this.damTimerIndex === 0) {
            series[0].color = "rgba(67,148,230,.3)";
            series[1].color = "rgba(120,252,253,1)";
          } else {
            series[1].color = "rgba(120,252,253,.3)";
            series[0].color = "rgba(67,148,230,1)";
          }
          this.slbchart.series[0].setData(series);
          this.slbchart.redraw();
        }, 3000);
      }
    },
    async initPie3D(depth, series) {
      let chart = new Highcharts.Chart("xzyx_chart", {
        chart: {
          type: "pie",
          spacing: [-60, 0, -30, 0],
          options3d: {
            enabled: true,
            alpha: 70,
          },
          backgroundColor: "rgba(0,0,0,0)",
        },
        title: false,
        plotOptions: {
          pie: {
            innerSize: "70%",
            depth: depth,
            dataLabels: {
              enabled: false,
            },
            showInLegend: false,
          },
        },
        credits: {
          enabled: false,
        },
        series: [
          {
            name: "水库个数",
            // colors: ['#4169f6', '#4394e6', '#78fcfd'],
            // colors: ['#78fcfd', '#4394e6'],
            // slicedOffset: 30,
            data: series,
          },
        ],
      });
      this.chart = chart;
    },
    async initDamPie3D(depth, series) {
      let chart = new Highcharts.Chart("slb_chart", {
        chart: {
          type: "pie",
          spacing: [-60, 0, -30, 0],
          options3d: {
            enabled: true,
            alpha: 70,
          },
          backgroundColor: "rgba(0,0,0,0)",
        },
        title: false,
        plotOptions: {
          pie: {
            innerSize: "70%",
            depth: depth,
            dataLabels: {
              enabled: false,
            },
            showInLegend: false,
          },
        },
        credits: {
          enabled: false,
        },
        series: [
          {
            name: "水库个数",
            data: series,
          },
        ],
      });
      this.slbchart = chart;
    },
    chooseMapType(type, numberSelected) {
      clickLog({
        title: "智慧水库-" + type
      });
      const noSelectValue = ["检查总量", "发现问题"];
      if (numberSelected) {
        if (!noSelectValue.includes(numberSelected)) {
          this.$store.commit("SET_NUMBER_SELECT", numberSelected);
        } else {
          return;
        }
      } else {
        if (type == "存量清零") {
          this.$store.commit(
            "SET_NUMBER_SELECT",
            "已竣工,加固中,已完工,项目前期"
          );
        } else {
          this.$store.commit("SET_NUMBER_SELECT", "");
        }
      }
      //清除红黄码选中状态
      this.$store.commit("SET_COLORCODE", "all");
      this.$refs.threeColorchart.initChart("all");

      // 关闭蓄水量浮窗开关
      this.$store.commit("SET_SKZG_SWITCH_XSL", false);

      // 通用
      this.$parent.$refs.skzgMap.closeAllOverly(); // 关闭弹窗
      this.$store.commit("SET_SKZG_SWITCH_LEGEND", true); // 打开图例
      this.$store.commit("SET_SKZG_MAP_TYPE", type); //切换图例
    },

    async getTotalNumberOfReservoirs() {
      let opt = {
        adcd: "330000000000",
        gpType: 1,
        projectScale: "1",
      };
      let res = await reservoirBigScreen(opt);
      if (res.success) {
        // this.siteList = res.data
      }
    },
    showOneColor(color) {
      this.chooseMapType("风险研判");
      this.$refs.threeColorchart.initChart(color);
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
    cursor: pointer;
  }
  .left_top {
    width: 100%;
    height: calc(35% - 24px);
    overflow: hidden;
    .threeColorBtn {
      margin-left: 10px;
      width: 20px;
    }
    .all_num {
      width: 100%;
      @include flexbox();
      @include flexAI(center);
      margin-bottom: 5px;
      padding-left: 5px;
      p {
        color: #46fdff;
        font-size: 22px;
        font-family: youshe;
        margin-right: 15px;
        span {
          font-size: 24px;
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
      margin: 4px 0px;
      &.left_box {
        // height: 100px;
        // margin-top: 3px;
        li {
          flex: 1;
          // height: 100%;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: flex-start;
          padding: 0 5px;
          cursor: pointer;
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
        height: calc(100% - 130px);
      }
      &.noNum {
        height: calc(100% - 105px);
      }
    }
  }
  .left_bottom {
    height: calc(65% - 24px);
    overflow: hidden;
    .threeColorBtn {
      margin-left: 10px;
      width: 20px;
    }
    .aqjd_box2 {
      height: 112px;
      box-sizing: border-box;
      padding: 10px 50px 0 10px;
      .aqjd_top {
        width: 100%;
        height: 40px;
        background: linear-gradient(
          90deg,
          rgba(25, 74, 130, 0.7) 0%,
          rgba(114, 163, 233, 0) 100%
        );
        box-sizing: border-box;
        padding-left: 10px;
        p {
          font-size: 18px;
          font-weight: 400;
          color: #ffffff;
          line-height: 40px;
          span {
            font-size: 28px;
            color: #69e3ee;
            line-height: 40px;
            font-weight: bold;
            padding: 0 5px 0 10px;
          }
        }
      }
      .aqjd_bottom {
        padding-top: 5px;
        @include flexbox();
        @include flexAI(center);
        @include flexJC(space-between);
        .aqjd_item {
          width: 100%;
          // @include flexbox();
          // @include flexAI(center);
          // @include flexJC(center);
          // // flex-direction: column;
          padding: 5px;
          h3 {
            font-size: 18px;
            // padding: 0 20px;
            // text-align: left;
          }
          p {
            font-size: 22px;
            font-family: "youshe";
            padding-top: 5px;
            span {
              font-size: 18px;
              font-family: none;
              margin-left: 5px;
              color: #fff;
            }
          }
        }
      }
    }
    .aqjd_box {
      height: 112px;
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
          h3 {
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
          font-size: 18px;
          padding: 0 20px;
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
        gap: 10px 4px;
        margin: 0 auto;
        font-size: 26px;
        text-align: left;
        li {
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          align-items: center;
          p {
            perspective: 138px;
            font-family: "youshe";
            width: 80%;
            text-align: left;
            color: rgb(20, 245, 241, 0.9);
            margin: 5px 0;
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
              margin: 0 4px;
            }
            .ba_num {
              font-family: "manrope";
              display: flex;
              font-size: 20px;
              flex: 0 1 0%;
              align-items: center;
              // margin: 0 6px;
              color: #fff;
              .ba_unit {
                color: rgba(230, 247, 255, 0.8);
                font-size: 18px;
                margin: 0 6px;
              }
            }
          }
        }
      }
    }
    .pie3d_box {
      width: 100%;
      // height: 150px;
      height: calc(50% - 150px);
      @include flexbox();
      position: relative;
      #xzyx_chart {
        // position: relative;
        // left: -30px;
        width: 70%;
        height: 100%;
      }
      #slb_chart {
        width: 70%;
        height: 100%;
      }
      .chart_title {
        box-sizing: border-box;
        width: 61%;
        height: 100%;
        color: #fff;
        position: absolute;
        left: 0;
        top: 0;
        text-align: center;
        @include flexbox();
        @include flexAI(center);
        @include flexJC(center);
        p {
          font-size: 32px;
          font-weight: bold;
          padding-bottom: 8px;
          cursor: pointer;
          span {
            font-size: 14px;
          }
        }
        span {
          font-size: 20px;
          cursor: pointer;
        }
      }
      .xzyx_legend {
        height: 100%;
        width: 45%;
        color: #fff;
        @include flexbox();
        @include flexflow(row wrap);
        box-sizing: border-box;
        padding: 15px 0;
        box-sizing: border-box;

        p {
          width: 100%;
          @include flexbox();
          @include flexAI(center);
          // height: 33.33%;
          padding-left: 10px;
          i {
            width: 8px;
            height: 8px;
            margin-right: 10px;
            &.cxx {
              // ['#78fcfd','#4169f6','#4394e6'],
              // background: #78fcfd;
              background: #4169f6;
            }
            &.czc {
              // background: #78fcfd;
              background: #78fcfd;
            }
            &.cfhg {
              background: #4394e6;
            }
          }
          span {
            font-size: 14px;
            strong {
              font-size: 20px;
              font-weight: bold;
              cursor: pointer;
              padding: 0 5px;
            }
          }
        }
      }
    }
    .problem_box {
      height: calc(50% - 30px);
      .gs_list {
        @include flexbox();
        @include flexAI(center);
        @include flexflow(row wrap);
        width: 100%;
        height: calc(100% - 50px);
        box-sizing: border-box;
        padding: 10px 0;
        .gs_item {
          width: 48%;
          height: 50%;
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
            width: 90px;
            height: 60px;
            // margin-right: 10px;
          }
          &:nth-child(2n + 1) {
            div {
              padding: 5px;
            }
          }
          div {
            h3 {
              font-size: 20px;
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
                font-size: 28px;
                margin-right: 4px;
              }
            }
          }
        }
      }
      // height: 195px;
      // ul {
      //   height: 100%;
      //   @include flexbox();
      //   li {
      //     height: 100%;
      //     width: 100%;
      //     @include flexbox();
      //     @include flexAI(center);
      //     // @include flexJC(space-between);
      //     // @include flexJC(center);
      //     @include flexflow(column wrap);
      //     // padding: 10px 0;
      //     box-sizing: border-box;
      //     padding: 10px 0;
      //     p {
      //       width: 100%;
      //       text-align: center;
      //       font-size: 20px;
      //       color: rgba(230, 247, 255, 0.8);
      //       line-height: 20px;
      //       &:last-child {
      //         margin-top: 10px;
      //       }
      //     }
      //     .th_num {
      //       margin-top: 10px;
      //       font-size: 28px;
      //       font-family: "youshe";
      //       color: rgb(255, 255, 255);
      //       margin-right: 5px;
      //       cursor: pointer;
      //     }
      //     .th_unit {
      //       color: #fff;
      //       font-size: 18px;
      //       font-weight: 600;
      //       cursor: pointer;
      //     }
      //     .pic_box {
      //       position: relative;
      //       width: 90%;
      //       height: 60%;
      //       img.index-out {
      //         position: absolute;
      //         width: 100%;
      //       }
      //       img.index-in {
      //         position: absolute;
      //         top: 0%;
      //         left: 25%;
      //         width: 50%;
      //         animation: imageAnimation 3s linear infinite;
      //         @keyframes imageAnimation {
      //           0% {
      //             opacity: 1;
      //             transform: none;
      //           }
      //           50% {
      //             opacity: 1;
      //             transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg) scaleX(1)
      //               scaleY(1) translate3d(0px, 10px, 0px);
      //           }
      //           100% {
      //             opacity: 1;
      //             transform: none;
      //           }
      //           100% {
      //             opacity: 1;
      //             transform: none;
      //           }
      //         }
      //       }
      //     }
      //     .pic_bottom {
      //       width: 100%;
      //       .pic_bottom_title {
      //         padding-top: 5px;
      //       }
      //     }
      //   }
      // }
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
    cursor: pointer;
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
      cursor: pointer;
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

  // .second_box {
  //   .tab_box {
  //     width: calc(100% - 80px);
  //     height: 32px;
  //     display: grid;
  //     grid-template-columns: 1fr 1fr 1fr;
  //     gap: 10px 4px;
  //     margin: 0 auto;
  //     li {
  //       display: flex;
  //       flex-direction: column;
  //       align-items: center;
  //       justify-content: center;
  //       transition: all 0.3s linear 0s;
  //       font-size: 16px;
  //       color: rgba(226, 237, 255, 0.5);
  //       border: 2px solid rgba(160, 169, 184, 0.3);
  //       background: rgba(15, 22, 34, 0.6);
  //       cursor: pointer;
  //       &.active {
  //         color: #fff;
  //         background: rgba(24, 144, 255, 0.15);
  //         border-color: rgb(24, 144, 255);
  //       }
  //     }
  //   }
  // }
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
    background-color: transparent;
  }
  /* 双数行背景颜色 */
  .el-table--striped .el-table__body tr.el-table__row--striped td {
    background-color: transparent;
  }
  /* 使表格背景透明 */
  .el-table th,
  .el-table tr {
    background-color: transparent;
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