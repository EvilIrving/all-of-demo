
<template>
  <div ref="slide_wrap" class="slide_wrap">
    <div class="slide_title" @click="enrichPeople">提能增效</div>
    <div class="right_top">
      <p class="second_title2 last_title">
        <!-- <span style="width:60%;" @click="chooseMapType('纳蓄')">纳蓄</span> -->
        <span :class="[
            index === topActive ? 'active' : '',
            sgzgMapType == item.name ? 'mapTypeActive' : '',
          ]" v-for="(item, index) in topTitle" :key="index"
          @click="switchTitle1(item, index), chooseMapType(item.name)">{{ item.name }}</span>
      </p>
      <div v-if="topActive == 0" ref="first_box" class="right_box first_box">
        <ul class="xsl_ul cursor">
          <li :class="numberSelected == '蓄水量' ? 'numberActive' : ''" @click="chooseMapType('蓄水量', '蓄水量')">
            <p class="num_title">当前蓄水总量</p>
            <p class="num_text">
              <countTo class="ba_num" :startVal="0" :endVal="nxData.capable" :duration="1500" :autoplay="true"
                :decimals="2"></countTo>
              <span class="ba_unit">亿m³</span>
            </p>
          </li>
          <li :class="numberSelected == '蓄水率' ? 'numberActive' : ''" @click="chooseMapType('蓄水率', '蓄水率')">
            <p class="num_title">当前蓄水率</p>
            <p class="num_text">
              <!-- <span class="ba_num">{{nxData.storagePerc}}</span> -->
              <countTo class="ba_num" :startVal="0" :endVal="nxData.storagePerc" :duration="1500" :autoplay="true"
                :decimals="2"></countTo>
              <span class="ba_unit">%</span>
            </p>
          </li>
        </ul>
        <!-- <funnel-chart class="funnel_chart" v-loading="funnelLoading" :chartData="funnelData" /> -->
        <!-- <concentric-chart class="funnel_chart" v-loading="funnelLoading" :chartData="funnelData" /> -->
        <div id="histogram3d"></div>
        <ul class="lenged">
          <li>
            <span>可纳雨量</span>
            <span v-permission="'jlzs:deduction'" class="cursor deducing"
              @click="openDeduction(deducing)">{{ deducing }}</span>
          </li>
          <!-- 1.以下，  2.以上 -->
          <li class="cursor" @click="chooseNxType('纳蓄', '200,2', 0)">
            <span class="green"></span><span :class="nxSelectIndex === 0 ? 'active' : ''">200mm以上</span>
            <img v-if="nxSelectIndex === 0" class="selected" :src="$config.imgUrl + 'arr.png'" alt="" />
          </li>
          <li class="cursor" @click="chooseNxType('纳蓄', '100,200', 1)">
            <span class="blue"></span><span :class="nxSelectIndex === 1 ? 'active' : ''">100-200mm</span>
            <img v-if="nxSelectIndex === 1" class="selected" :src="$config.imgUrl + 'arr.png'" alt="" />
          </li>
          <li class="cursor" @click="chooseNxType('纳蓄', '50,100', 2)">
            <span class="orange"></span><span :class="nxSelectIndex === 2 ? 'active' : ''">50-100mm</span>
            <img v-if="nxSelectIndex === 2" class="selected" :src="$config.imgUrl + 'arr.png'" alt="" />
          </li>
          <li class="cursor" @click="chooseNxType('纳蓄', '50,1', 3)">
            <span class="red"></span><span :class="nxSelectIndex === 3 ? 'active' : ''">50mm以下</span>
            <img v-if="nxSelectIndex === 3" class="selected" :src="$config.imgUrl + 'arr.png'" alt="" />
          </li>
        </ul>
      </div>
      <div v-if="topActive == 1" style="height: 100%">
        <p class="third_title">
          <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
          <span style="cursor: unset">运行状态</span>
          <i></i>
          <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" />
        </p>
        <ul ref="circle_wrapper" class="circle_wrapper">
          <li class="">
            <section class="left circle_box">
              <img :src="$config.imgUrl + 'circle_x.png'" alt="" class="index-out" />
              <img :src="$config.imgUrl + 'circle_y.png'" alt="" class="index-in" />
            </section>
            <section class="right text_box cursor" :class="numberSelected == '泄洪' ? 'numberActive' : ''"
              @click="chooseMapType('防洪', '泄洪')">
              <p class="num_title">泄洪</p>
              <p class="num_text">
                <span class="ba_num">{{
                  floodControlStatistic.gateflowNum
                }}</span><span class="ba_unit">座</span>
              </p>
            </section>
          </li>
          <li class="" style="position: relative; left: -20px">
            <section class="left circle_box">
              <img :src="$config.imgUrl + 'circle_x.png'" alt="" class="index-out" />
              <img :src="$config.imgUrl + 'circle_y.png'" alt="" class="index-in" />
            </section>
            <section class="right text_box cursor" :class="numberSelected == '溢洪' ? 'numberActive' : ''"
              @click="chooseMapType('防洪', '溢洪')">
              <p class="num_title">溢洪</p>
              <p class="num_text">
                <span class="ba_num">{{
                  floodControlStatistic.overflowNum
                }}</span><span class="ba_unit">座</span>
              </p>
            </section>
          </li>
        </ul>
        <p class="third_title">
          <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
          <span style="cursor: unset">防洪效益</span>
          <i></i>
          <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" />
        </p>
        <!-- <ul ref="circle_wrapper" class="circle_wrapper" style="margin-top:20px;height:80px">
          <li class="">
            <section class="circle_box">
              <img src="../../assets/images/skzg/ChassisRing.png" alt="" class="bottom_ring">
              <img src="../../assets/images/skzg/glitterRing.png" alt="" class="middle_ring">
              <img src="../../assets/images/skzg/flashCylinder.png" alt="" class="top_ring">
            </section>
            <section class="lanxu_box">
              <p class="num_title">防洪座次</p>
              <p class="num_text"><span class="ba_num">{{ floodControlStatistic.floodRetentionNum }}</span><span class="ba_unit">座次</span></p>
            </section>
          </li>
          <li class="">
            <section class="circle_box">
              <img src="../../assets/images/skzg/ChassisRing.png" alt="" class="bottom_ring">
              <img src="../../assets/images/skzg/glitterRing.png" alt="" class="middle_ring">
              <img src="../../assets/images/skzg/flashCylinder.png" alt="" class="top_ring">
            </section>
            <section class="lanxu_box">
              <p class="num_title">拦洪水量</p>
              <p class="num_text"><span class="ba_num">{{floodControlStatistic.floodRetention}}</span><span class="ba_unit">万方</span></p>
            </section>
          </li>
          <li class="">
            <section class="circle_box">
              <img src="../../assets/images/skzg/ChassisRing.png" alt="" class="bottom_ring">
              <img src="../../assets/images/skzg/glitterRing.png" alt="" class="middle_ring">
              <img src="../../assets/images/skzg/flashCylinder.png" alt="" class="top_ring">
            </section>
            <section class="lanxu_box">
              <p class="num_title">泄洪总量</p>
              <p class="num_text"><span class="ba_num">{{floodControlStatistic.gateflow}}</span><span class="ba_unit">万方</span></p>
            </section>
          </li>
        </ul> -->
        <ul class="fhxy_wrapper">
          <div class="fhxy_item cursor" @click="chooseMapType('拦洪座次', '拦洪座次')">
            <img :src="$config.imgUrl + 'fhxy1.png'" alt="" />
            <div :class="numberSelected == '拦洪座次' ? 'numberActive' : ''">
              <h3>拦洪座次</h3>
              <p>
                <span>{{ floodControlStatistic.floodRetentionNum }}</span>座次
              </p>
            </div>
          </div>
          <div class="fhxy_item">
            <img :src="$config.imgUrl + 'fhxy2.png'" alt="" />
            <div>
              <h3>拦洪水量</h3>
              <p>
                <span v-show="floodControlStatistic.floodRetention">{{
                  (floodControlStatistic.floodRetention + "").length > 4
                    ? (floodControlStatistic.floodRetention / 10000).toFixed(0)
                    : floodControlStatistic.floodRetention.toFixed(0)
                }}</span>
                <span v-show="!floodControlStatistic.floodRetention">0</span>
                {{
                  (floodControlStatistic.floodRetention + "").length > 4
                    ? "亿m³"
                    : "万m³"
                }}
                <!-- <span>9999</span><span>万m³</span> -->
              </p>
            </div>
          </div>
        </ul>
      </div>
    </div>
    <div class="right_middle">
      <p class="second_title2 last_title">
        <span :class="[
            index === titleActive0 ? 'active' : '',
            sgzgMapType == item.name ? 'mapTypeActive' : '',
          ]" v-for="(item, index) in firstTitle" :key="index"
          @click="switchTitle0(item, index), chooseMapType(item.name)">{{ item.name }}</span>
      </p>
      <div class="right_box gs_box" v-show="titleActive0 == 0">
        <div class="gs_list">
          <div class="gs_item">
            <img :src="$config.imgUrl + 'gs1.png'" alt="" />
            <div class="cursor" :class="numberSelected == '供水' ? 'numberActive' : ''"
              @click="chooseMapType('供水', '供水')">
              <h3>供水水库</h3>
              <p>
                <span>{{ suppplyStatistic }}</span>座
              </p>
            </div>
          </div>
          <div class="gs_item pl">
            <img :src="$config.imgUrl + 'gs2.png'" alt="" />
            <!-- 省级取消供水水量交互 -->
            <div v-if="
                ((sgzgSelectAdcd.indexOf('0000000000') != -1 &&
                  sgzgSelectAdcd.indexOf(',') == -1) ||
                  sgzgSelectAdcd == '') &&
                userAdcd.indexOf('0000000000') != -1
              ">
              <h3>供水水量</h3>
              <p>
                <span>{{ gsTotal }}</span>亿m³
              </p>
            </div>
            <div v-else class="cursor" :class="numberSelected == '供水水量' ? 'numberActive' : ''"
              @click="chooseMapType('供水水量', '供水水量')">
              <h3>供水水量</h3>
              <p>
                <span>{{ gsTotal }}</span>亿m³
              </p>
            </div>
          </div>
          <div class="gs_item">
            <img :src="$config.imgUrl + 'gs3.png'" alt="" />
            <div class="cursor" @click="chooseMapType('可供水量', '可供水量')"
              :class="numberSelected == '可供水量' ? 'numberActive' : ''">
              <h3>可供水量</h3>
              <p>
                <span>{{ supply_total }}</span>亿m³
              </p>
            </div>
          </div>
          <div class="gs_item pl">
            <img :src="$config.imgUrl + 'gs4.png'" alt="" />
            <div>
              <h3>供水人口</h3>
              <p><span>4564</span>万人</p>
            </div>
          </div>
        </div>
        <!-- 供水水库：1006座；可供水量：万方；供水人口：4564万人” -->
        <!-- 临时替换三个统计项 先注释 -->
        <!-- <ul>
          <li style="flex:3;padding-left: 5px">
            <p class="num_title">供水水量</p>
            <p class="num_text"><span class="ba_num">4234</span><span class="ba_unit">万m³</span></p>
          </li>
          <li style="flex:2">
            <p class="num_title">水厂数量</p>
            <p class="num_text"><span class="ba_num">7</span><span class="ba_unit">座</span></p>
          </li>
          <li style="flex:3">
            <p class="num_title">供水人数</p>
            <p class="num_text"><span class="ba_num">2967</span><span class="ba_unit">万人</span></p>
          </li>
        </ul> -->

        <!-- <ul>
          <li style="flex:3;padding-left: 5px">
            <p class="num_title">供水水库</p>
            <p class="num_text"><span class="ba_num">1006</span><span class="ba_unit">座</span></p>
          </li>
          <li style="flex:4">
            <p class="num_title">可供水量</p>
            <p class="num_text"><span class="ba_num">780048.2</span><span class="ba_unit">万方</span></p>
          </li>
          <li style="flex:3">
            <p class="num_title">供水人口</p>
            <p class="num_text"><span class="ba_num">4564</span><span class="ba_unit">万人</span></p>
          </li>
        </ul> -->

        <!-- <BarChartNoClick v-loading="barLoading" class="securityChart" :chartData=" barData" /> -->
      </div>
      <div class="gg_box" ref="gg_box" v-if="titleActive0 == 1">
        <div class="gg_item cursor" :class="numberSelected == '灌溉' ? 'numberActive' : ''"
          @click="chooseMapType('灌溉', '灌溉')">
          <img :width="ggImgWidth" :height="ggImgHeight" :src="$config.imgUrl + 'irrigationR.png'" alt="" />
          <div>
            <span>灌溉水库</span>
            <p>{{ irrigationStatistic }}<span>座</span></p>
          </div>
        </div>
        <div class="gg_item cursor" :class="numberSelected == '灌溉水量' ? 'numberActive' : ''"
          @click="chooseMapType('灌溉水量', '灌溉水量')">
          <img :width="ggImgWidth" :height="ggImgHeight" :src="$config.imgUrl + 'dngsl.png'" alt="" />
          <div>
            <span>当年灌溉水量</span>
            <p>{{ ggTotal }}<span>亿m³</span></p>
          </div>
        </div>
        <div class="gg_item cursor" :class="numberSelected == '灌溉面积' ? 'numberActive' : ''"
          @click="chooseMapType('灌溉面积', '灌溉面积')">
          <img :width="ggImgWidth" :height="ggImgHeight" :src="$config.imgUrl + 'ggmj.png'" alt="" />
          <div>
            <span>灌溉面积</span>
            <p>
              {{ ggData.irrigatedArea ? ggData.irrigatedArea.toFixed(0) : 0
              }}<span>万亩</span>
            </p>
          </div>
        </div>
        <!-- <div class="gg_rate">
          <div class="rate_top">
            <p>粮食功能区水源水库占比</p>
            <div>{{ggData.rate}}%</div>
          </div>
          <div class="rate_body">
            <div class="rate_box">
              <div class="rate_item" v-for="(item, index) in rateList" :key="index">
                <div class="rate_item_color" :style="{width: +item.width + '%'}"></div>
              </div>
            </div>
          </div>
        </div> -->
      </div>
    </div>
    <div class="right_bottom">
      <p class="second_title2 last_title">
        <span :class="[
            index === titleActive ? 'active' : '',
            sgzgMapType == item.name ? 'mapTypeActive' : '',
          ]" v-for="(item, index) in lastTitle" :key="index"
          @click="switchTitle(item, index), chooseMapType(item.name)">{{ item.name }}</span>
      </p>
      <div class="third_box" v-show="titleActive == 0">
        <ul>
          <li>
            <div class="pic_box">
              <img :src="$config.imgUrl + 'ecology.png'" class="index-in" alt="" />
              <img :src="$config.imgUrl + 'twinkling-bg.png'" class="index-out" alt="" />
            </div>
            <div class="pic_bottom cursor" :class="numberSelected == '生态' ? 'numberActive' : ''"
              @click="chooseMapType('生态', '生态')">
              <p class="pic_title">水库座数</p>
              <p class="pic_text">
                <span class="th_num">{{ stData.skNum }}</span><span class="th_unit">座</span>
              </p>
            </div>
          </li>
          <!-- <li>
            <div class="pic_box">
              <img :src="$config.imgUrl + 'section.png'" class="index-in" alt="">
              <img :src="$config.imgUrl + 'twinkling-bg.png'" class="index-out" alt="">
            </div>
            <p class="pic_title">监测断面达标率</p>
            <p class="pic_text"><span class="th_num">24 / 24</span><span class="th_unit"></span></p>
          </li> -->
          <li>
            <div class="pic_box">
              <img :src="$config.imgUrl + 'scenicArea.png'" class="index-in" alt="" />
              <img :src="$config.imgUrl + 'twinkling-bg.png'" class="index-out" alt="" />
            </div>
            <div class="pic_bottom cursor" :class="numberSelected == '风景区' ? 'numberActive' : ''"
              @click="chooseMapType('风景区', '风景区')">
              <p class="pic_title">风景区</p>
              <p class="pic_text">
                <span class="th_num">{{ stData.scenicNum }}</span><span class="th_unit">个</span>
              </p>
            </div>
          </li>
        </ul>
      </div>
      <div class="third_box" v-if="titleActive == 1">
        <div class="fd_list">
          <div class="fd_item cursor" :class="numberSelected == '发电' ? 'numberActive' : ''"
            @click="chooseMapType('发电', '发电')">
            <h3>发电水库</h3>
            <p>
              <span>{{ powerGeStatistic }}</span>座
            </p>
          </div>
          <div class="fd_item cursor" :class="numberSelected == '装机容量' ? 'numberActive' : ''"
            @click="chooseMapType('装机容量', '装机容量')">
            <h3>装机容量</h3>
            <p>
              <span>{{ installedTotal }}</span>万千瓦
            </p>
          </div>
          <div class="fd_item cursor" :class="numberSelected == '当年发电量' ? 'numberActive' : ''"
            @click="chooseMapType('当年发电量', '当年发电量')">
            <h3>当年发电量</h3>
            <p>
              <span>{{ generatingTotal }}</span>亿千瓦时
            </p>
          </div>
          <div class="fd_item">
            <h3>碳减排量</h3>
            <!-- <p><span>{{carbon}}</span>万吨</p> -->
            <p>
              <span>{{ carbonCount.toFixed(2) }}</span>万吨
            </p>
          </div>
        </div>
      </div>
    </div>

    <!-- 推演弹窗 -->
    <primary-dialog :showFooter="showFooter" top="4vh" mainTitle="推演" @changeFullscreen="changeFullscreen"
      :fullscreen="fullscreen" :visible="dialogVisible" @handleClose="handleClose" width="1000px">
      <div class="dialog-content" :class="{ 'fullscreen-box': fullscreen }">
        <deduction v-if="dialogVisible" :fullscreen="fullscreen" :sgzgSelectBasin="sgzgSelectBasin"
          :sgzgSelectAdcd="sgzgSelectAdcd" @startDeducing="startDeducing" @handleClose="handleClose" />
      </div>
    </primary-dialog>
    <!-- 顶部标题点击弹窗 -->
    <primary-dialog mainTitle="" :visible="huiminDialogVisible" @handleClose="handleCloseHuimin"
      @changeFullscreen="changeFullscreen" :fullscreen="fullscreen" width="1200px" append-to-body top="10vh">
      <div class="dialog-img-content" :class="{ 'fullscreen-box': fullscreen }" v-viewer>
        <img class="huimin fullscreen-huimin" :src="$config.imgUrl + 'newhuimin.png'" alt="" />
      </div>
      <!-- <p style="text-align: center;color: #fff;padding-top:5px;font-size: 20px;">点击可放大图片</p> -->
    </primary-dialog>
  </div>
</template>

<script>
  import countTo from "vue-count-to";
  import Highcharts from "highcharts";
  import FunnelChart from "./components/FunnelChart.vue";
  import ConcentricChart from "./components/ConcentricChart.vue";
  import BarChartNoClick from "./components/BarChartNoClick";
  import PrimaryDialog from "./components/PrimaryDialog.vue";
  import Deduction from "./components/Deduction.vue";
  import {
    floodCountStorage,
    nxStatisticApi,
    reservoirBigScreen,
    wateringApi,
    electricityApi,
    ecologyApi,
    clickLog,
  } from "@/api/api_reservoir";
  export default {
    name: "SkzgScreenRight",
    props: {},
    components: {
      FunnelChart,
      BarChartNoClick,
      PrimaryDialog,
      Deduction,
      ConcentricChart,
      countTo,
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
      waterStorageQuantity() {
        // 纳蓄的图例变化更新到vuex
        return this.$store.state.waterStorageQuantity;
      },
      numberSelected() {
        return this.$store.state.numberSelected;
      },
      waterStorageRate() {
        // 纳蓄的图例变化更新到vuex
        return this.$store.state.waterStorageRate;
      },
    },
    watch: {
      selectData: {
        handler(val) {
          this.getFunnelData();
          this.topActive == 1 ? this.getfDData() : this.getNxData()
          this.titleActive0 == 1 ? this.getIrrigationData() : this.getSupplyData()
          this.titleActive == 0 ? this.getEcologyData() : this.getPowerGeData()
        },
        deep: true,
        immediate: true,
      },
      titleActive: {
        handler(newVal, oldVal) {
          //  == 1 发电 == 0 生态
          newVal == 1 ? this.getPowerGeData() : this.getEcologyData()
        },
        deep: true,
      },
      titleActive0: {
        handler(newVal, oldVal) {
          //  == 1 灌溉 == 0 供水
          newVal == 1 ? this.getIrrigationData() : this.getSupplyData()
        },
        deep: true,
      },
      waterStorageQuantity(val) {
        // 纳蓄 蓄水量的图例 更新
        this.getNxData();
      },
      waterStorageRate(val) {
        // 纳蓄 蓄水率的图例 更新
        this.getNxData();
      },
    },
    data() {
      return {
        huiminDialogVisible: false,
        playlistDialogVisible: false,
        funnelLoading: false,
        barLoading: true,
        dialogVisible: false,
        loading: false,
        showFooter: false,
        fullscreen: false,
        deducing: "推演",
        titleActive: 0,
        titleActive0: 0,
        topActive: 0,
        topTitle: [{ name: "纳蓄" }, { name: "防洪" }],
        firstTitle: [{ name: "供水" }, { name: "灌溉" }],
        lastTitle: [{ name: "生态" }, { name: "发电" }],
        funnelHeight: 200,
        funnelData: {
          id: "funnelData",
          data: [],
          unit: "座",
        },
        barHeight: 150,
        barData: {
          id: "functionData",
          xData: [],
          yData: [],
          yData2: [],
          adcdList: [],
          unit: "万m³",
          // noWords: true
        },
        stData: {
          skNum: 0,
          scenicNum: 0,
        },
        nxData: {
          capable: 0,
          storagePerc: 0,
        },
        adcdList: [],
        selectAdcd: "",
        inputName: "",
        tableHeight: 0,
        siteList: [],
        ggData: {},
        floodControlStatistic: {},
        funnel3dData: [],
        rateList: [
          {
            width: 0,
          },
          {
            width: 0,
          },
          {
            width: 0,
          },
          {
            width: 0,
          },
          {
            width: 0,
          },
          {
            width: 0,
          },
          {
            width: 0,
          },
          {
            width: 0,
          },
          {
            width: 0,
          },
          {
            width: 0,
          },
        ],
        nxSelectIndex: "",
        nxInterval: null,
        powerGeStatistic: 0,
        suppplyStatistic: 0,
        irrigationStatistic: 0,
        carbon: 0,
        ggImgWidth: 30,
        ggImgHeight: 30,
        supply_total: 0, // 可供水量
        installedTotal: 0, //装机容量
        capacityTotal: 0, //当年灌溉水量
        gsTotal: 0, //供水水量
        ggTotal: 0, //灌溉水量
        generatingTotal: 0, //发电量
        carbonCount: 0, // 碳排量
        userAdcd: "",
      };
    },
    mounted() {
      this.userAdcd = this.$localData("get", "userInfo").adcd;
      this.getBarData();
      this.getNxData();
      this.getFunnelData();
      // this.barHeight = this.$refs.slide_wrap.offsetHeight - (this.$refs.first_box.offsetHeight + this.$refs.third_box.offsetHeight + 400)
    },
    methods: {
      changeFullscreen() {
        this.fullscreen = !this.fullscreen;
        // this.$nextTick(() => {
        // 	this.$refs.generation.$refs.tb_wrappers.doLayout();
        // });
      },
      startDeducing(flag) {
        this.showFooter = false;
        // this.deducing = "取消";
        this.$nextTick(() => {
          this.showFooter = flag;
        });
      },
      enrichPeople() {
        this.huiminDialogVisible = true;
        // setTimeout(() => {
        //   this.$message.success('点击可以放大图片')
        // }, 500);
        clickLog({
          title: "智慧水库-提能增效"
        });
      },
      async getNxData() {
        let opt = {
          adcd: this.sgzgSelectAdcd
            ? this.sgzgSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          basinCode: this.sgzgSelectBasin,
        };
        if (this.numberSelected == "蓄水率") {
          // opt.projectScale = this.waterStorageQuantity
          opt.projectScale = this.waterStorageRate;
        } else {
          opt.projectScale = this.waterStorageQuantity;
        }
        let res = await nxStatisticApi(opt);
        if (res.success && res.data.length !== 0) {
          this.nxData.capable = 0;
          this.nxData.storagePerc = 0;
          setTimeout(() => {
            this.nxData.capable = +res.data[0].capable;
            this.nxData.storagePerc = res.data[0].storagePerc;
          });
          if (this.nxInterval) clearInterval(this.nxInterval);
          this.nxInterval = setInterval(() => {
            this.getNxData();
          }, 60000);
        } else {
          this.nxData.capable = 0;
          this.nxData.storagePerc = 0;
        }
      },
      // 获取防洪统计数据
      async getfDData() {
        let res = await floodCountStorage({
          adcd: this.sgzgSelectAdcd
            ? this.sgzgSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          basinCode: this.sgzgSelectBasin,
        });
        if (res.success) {
          this.floodControlStatistic = res.data;
        }
      },
      // 水库库容
      async getFunnelData() {
        this.funnelLoading = true;
        let opt = {
          adcd: this.sgzgSelectAdcd
            ? this.sgzgSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          basin: this.sgzgSelectBasin,
          gpType: 6,
        };
        let res = await reservoirBigScreen(opt);
        if (res.success) {
          if (res.data.length > 0) {
            let arr = [];
            let sumprice = res.data.reduce(function (
              totalValue,
              currentValue,
              currentIndex,
              arr
            ) {
              totalValue += currentValue.num;
              currentValue.accumulateNum = totalValue;
              return totalValue;
            },
              0);
            // res.data.sort((a, b) => b.num - a.num )
            res.data.reverse();

            res.data.map((i, index) => {
              i.name = i.num + "座";
              i.value = ((i.accumulateNum / sumprice) * 100 * 0.6).toFixed(2);
              arr.push(i);
            });
            this.funnelData.data = arr;
            this.funnel3dData = [];
            arr.forEach((i) => {
              this.funnel3dData.push([i.fz, +i.num]);
            });
          }
        } else {
          this.$message.error(res.msg ? res.msg : "请求失败");
        }
        this.initHistogram3D();
        this.funnelLoading = false;
      },
      // 3D柱状图
      initHistogram3D() {
        var chart = new Highcharts.chart("histogram3d", {
          chart: {
            type: "funnel3d",
            options3d: {
              enabled: true,
              alpha: 6,
              viewDistance: 50,
            },
            backgroundColor: "rgba(0,0,0,0)",
          },
          title: false,
          plotOptions: {
            series: {
              dataLabels: {
                enabled: true,
                format: "<b>{point.y:,.0f}",
                color: (Highcharts.theme && Highcharts.theme.colors) || "black",
                allowOverlap: false,
                y: 12,
                color: "#fff",
                style: {
                  fontSize: 14,
                },
                align: "center",
              },
              cursor: "pointer",
              neckWidth: "20%",
              neckHeight: "0%",
              width: "75%",
              height: "100%",
              minSize: "100",
              events: {
                click: (event) => {
                  console.log(event, "3D-event");
                  // event.point.name
                  // event.point.y
                  // event.point.color = 'red'
                  switch (event.point.name) {
                    case "50mm以下":
                      this.chooseNxType("纳蓄", "50,1", 3);
                      break;
                    case "50-100mm":
                      this.chooseNxType("纳蓄", "50,100", 2);
                      break;
                    case "100-200mm":
                      this.chooseNxType("纳蓄", "100,200", 1);
                      break;
                    default:
                      this.chooseNxType("纳蓄", "200,2", 0);
                      break;
                  }
                },
              },
            },
          },
          credits: {
            enabled: false,
          },
          series: [
            {
              name: "",
              data: this.funnel3dData,
              colorByPoint: true,
              allowPointSelect: true,
              colors: [
                "rgba(91, 190, 88, 0.7)",
                "rgba(56, 134, 255, 0.7)",
                "rgba(247, 181, 0, 0.7)",
                "rgba(250, 100, 0, 0.8)",
              ],
            },
          ],
        });
        // document.getElementById('histogram3d').addEventListener('click', () => {
        //   var selectedPoints = chart.getSelectedPoints();
        //   console.log(selectedPoints[0], 'selectedPoints')
        //   if (chart.lbl) {
        //     chart.lbl.destroy();
        //   }
        // });
      },

      chooseNxType(type, rainScope, index) {
        this.$parent.$refs.skzgMap.closeAllOverly();
        this.$store.commit("SET_SKZG_SWITCH_LEGEND", true);
        // 关闭蓄水量浮窗开关
        this.$store.commit("SET_SKZG_SWITCH_XSL", false);
        this.$store.commit("SET_SKZG_MAP_TYPE", type);
        this.$store.commit("SET_RAIN", rainScope);
        this.nxSelectIndex = index;
      },

      chooseMapType(type, numberSelected) {
        clickLog({
          title: "智慧水库-" + type
        });
        this.$store.commit("SET_NUMBER_SELECT", "");
        if (numberSelected) {
          this.$store.commit("SET_NUMBER_SELECT", numberSelected);
        }
        if (type === "蓄水量") {
          this.$store.commit("SET_WATER_STORAGE_QUANTITY", "1,2,3");
        }
        if (type === "蓄水率") {
          this.$store.commit("SET_WATER_STORAGE_RATE", "1,2,3");
        }
        // if (condition) {
        //   //
        // }
        // this.$parent.$refs.skzgMap.removeAllLayer()
        this.$parent.$refs.skzgMap.closeAllOverly();
        this.$store.commit("SET_SKZG_SWITCH_LEGEND", true);
        // 关闭蓄水量浮窗开关
        this.$store.commit("SET_SKZG_SWITCH_XSL", false);
        this.$store.commit("SET_SKZG_MAP_TYPE", type);
      },
      openDeduction(deducing) {
        if (deducing === "推演") {
          this.dialogVisible = true;
          clickLog({
            title: "智慧水库-推演"
          });
        } else {
          this.deducing = "推演";
          this.$store.commit("SET_IFUNDER", "");
          this.$store.commit("SET_RAIN", "");
          this.dialogVisible = false;
        }
      },
      tabsSwitch(item, index) {
        this.tabActive = index;
      },
      switchTitle0(item, index) {
        this.titleActive0 = index;
        if (index == 1) {
          this.$nextTick(() => {
            this.ggImgWidth = this.$refs.gg_box.offsetHeight / 3.6;
            this.ggImgHeight = this.$refs.gg_box.offsetHeight / 3.6;
          });
          this.getIrrigationData();
        }
      },
      switchTitle1(item, index) {
        this.topActive = index;
        if (index == 1) {
          this.getfDData();
        } else {
          this.nxSelectIndex = "";
          this.getFunnelData();
        }
      },
      switchTitle(item, index) {
        this.titleActive = index;
        if (index == 1) {
          this.getPowerGeData();
        } else {
          this.getEcologyData();
        }
      },
      // 水库库容
      async getBarData() {
        this.barLoading = true;
        // let opt = {
        //   statistic: "capacity",
        //   adcd: '330000000000',
        // };
        // let res = await reservoirLeftStatisticApi(opt);
        let res = {
          success: true,
          data: [
            {
              adcd: "330100000000",
              adnm: "杭州市",
              supply: 410.290315,
            },

            {
              adcd: "330200000000",
              adnm: "宁波市",
              supply: 998.891,
            },

            {
              adcd: "330300000000",
              adnm: "温州市",
              supply: 32.780979,
            },

            // {
            //   adcd: "330500000000",
            //   adnm: "湖州市",
            //   supply: 16173.81
            // },

            {
              adcd: "330600000000",
              adnm: "绍兴市",
              supply: 718.274634,
            },

            {
              adcd: "330700000000",
              adnm: "金华市",
              supply: 998.891,
            },

            // {
            //   adcd: "330800000000",
            //   adnm: "衢州市",
            //   supply: 77307.54
            // },

            {
              adcd: "330900000000",
              adnm: "舟山市",
              supply: 777.99961,
            },

            {
              adcd: "331000000000",
              adnm: "台州市",
              supply: 1094.170407,
            },
            {
              adcd: "331100000000",
              adnm: "丽水市",
              supply: 96.746755,
            },
          ],
        };
        if (res.success) {
          let xData = [];
          let yData = [];
          let yData2 = [];
          let adcdList = [];
          if (res.data.length) {
            for (let i = 0; i < res.data.length; i++) {
              if (res.data[i].adnm.length > 2) {
                xData.push(res.data[i].adnm.replace(/市/, ""));
              } else {
                xData.push(res.data[i].adnm);
              }
              yData.push(res.data[i].supply.toFixed(1));
              // yData.push(res.data[i].supply);
              // yData2.push(
              //   res.data[i].addtion ? (res.data[i].addtion / 10000).toFixed(1) : 0
              // );
              adcdList.push(res.data[i].adcd);
            }
          }
          this.barData.xData = xData;
          this.barData.yData = yData;
          this.barData.yData2 = yData2;
          this.barData.adcdList = adcdList;
        } else {
          this.$message.error(res.msg ? res.msg : "请求失败");
        }
        this.barLoading = false;
      },
      //获取 生態 統計数字
      async getEcologyData() {
        // 原先的老接口
        let res = await ecologyApi.rightNum({
          adcd: this.sgzgSelectAdcd
            ? this.sgzgSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          basinCode: this.sgzgSelectBasin,
        });
        // 新接口
        let res2 = await reservoirBigScreen({
          adcd: this.sgzgSelectAdcd
            ? this.sgzgSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          basin: this.sgzgSelectBasin,
          gpType: 8,
          extractPram: "生态",
        });
        if (res.success) {
          this.stData.scenicNum = res.data.scenicNum;
        }
        if (res2.success) {
          this.stData.skNum =
            res2.data && res2.data.length ? res2.data[0].count : 0;
        }
      },
      //获取 灌溉 統計数字
      async getIrrigationData() {
        let res = await reservoirBigScreen({
          adcd: this.sgzgSelectAdcd
            ? this.sgzgSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          basin: this.sgzgSelectBasin,
          gpType: 8,
          extractPram: "灌溉",
        });
        if (res.success) {
          this.irrigationStatistic =
            res.data && res.data.length ? res.data[0].count : 0;
          this.ggTotal =
            (res.data && res.data.length && res.data[0].ggTotal) || 0;
        } else {
          this.$message.error(res.msg ? res.msg : "请求失败");
        }

        this.rateList.map((v) => {
          v.width = 0;
        });
        let res2 = await wateringApi.rightNum({
          adcd: this.sgzgSelectAdcd
            ? this.sgzgSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          basinCode: this.sgzgSelectBasin,
        });
        if (res2.success) {
          this.ggData = res2.data;
          if (res2.data.rate > 10) {
            let itemInt = parseInt(res2.data.rate / 10); //整数部分
            let itemRemainder = (res2.data.rate % 10).toFixed(1) * 10; //余数部分
            this.rateList.map((v, i) => {
              if (i < itemInt) {
                v.width = 100;
              }
            });
            this.rateList[itemInt].width = itemRemainder;
          } else {
            this.rateList[0].width = res2.data.rate * 10;
          }
        }
      },
      //获取 發電 統計数字
      async getPowerGeData() {
        let res = await electricityApi.rightNum({
          adcd: this.sgzgSelectAdcd
            ? this.sgzgSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          basinCode: this.sgzgSelectBasin,
        });
        let res2 = await reservoirBigScreen({
          adcd: this.sgzgSelectAdcd
            ? this.sgzgSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          basin: this.sgzgSelectBasin,
          gpType: 8,
          extractPram: "发电",
        });
        if (res.success) {
          this.carbon = res.data.carbon ? res.data.carbon.toFixed(2) : 0;
        }
        if (res2.success) {
          this.powerGeStatistic =
            res2.data && res2.data.length ? res2.data[0].count : 0;
          this.installedTotal = res2.data[0].installedTotal
            ? res2.data[0].installedTotal.toFixed(0)
            : 0;
          this.generatingTotal = res2.data[0].generatingTotal
            ? res2.data[0].generatingTotal.toFixed(0)
            : 0;
          this.carbonCount = res2.data[0].generatingTotal
            ? res2.data[0].generatingTotal * 9.97
            : 0;
        }
      },
      //获取 供水 統計数字
      async getSupplyData() {
        let opt = {
          adcd: this.sgzgSelectAdcd
            ? this.sgzgSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          basin: this.sgzgSelectBasin,
          gpType: 8,
          extractPram: "供水",
        };
        let res = await reservoirBigScreen(opt);
        if (res.success) {
          this.suppplyStatistic =
            (res.data && res.data.length && res.data[0].count) || 0;
          this.supply_total =
            (res.data &&
              res.data.length &&
              res.data[0].supply_total.toFixed(0)) ||
            0;
          if (
            ((this.sgzgSelectAdcd.indexOf("0000000000") != -1 &&
              this.sgzgSelectAdcd.indexOf(",") == -1) ||
              this.sgzgSelectAdcd == "") &&
            this.userAdcd.indexOf("0000000000") != -1
          ) {
            this.gsTotal = 27;
          } else {
            this.gsTotal =
              res.data && res.data.length ? res.data[0].gsTotal.toFixed(0) : 0;
          }
        } else {
          this.$message.error(res.msg ? res.msg : "请求失败");
        }
      },
      handleClose() {
        this.dialogVisible = false;
        this.deducing = "推演";
      },
      handlePlaylistClose() {
        this.playlistDialogVisible = false;
      },
      handleCloseHuimin() {
        this.huiminDialogVisible = false;
      },
    },
  };
</script>

<style lang='scss' scoped>
.third_title:before {
  content: none;
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
    z-index: 2;
    box-sizing: border-box;
    span {
      cursor: pointer;
    }
    &.last_title {
      display: flex;
      flex-direction: row;
      align-items: center;
      span {
        transition: all 0.2s linear;
        margin-right: 25px;
        font-size: 24px;
        color: rgba(226, 237, 255, 0.5);
        cursor: pointer;
        &.active {
          font-size: 30px;
          color: rgb(238, 246, 255);
        }
      }
    }
  }
  .right_top {
    height: calc(33% - 15px);
    overflow: hidden;
    .first_box {
      height: 100%;
      .xsl_ul {
        @include flexbox();
        @include flexAI(center);
        @include flexJC(center);
        li {
          width: 40%;
          &:first-child {
            margin-right: 30px;
          }
        }
      }
      .funnel_chart {
        width: 100%;
        height: calc(100% - 120px);
      }
    }
    .circle_wrapper {
      width: 100%;
      // height: 100px;
      height: calc(50% - 50px);
      // margin: 2% 0px;
      @include flexbox();
      @include flexJC(center);
      @include flexAI(center);
      li {
        width: 50%;
        // height: 100%;
        display: flex;
        justify-content: center;
        // align-items: flex-start;
        @include flexAI(center);
        margin: 0px 5px;
        .circle_box {
          @include flexbox();
          @include flexJC(center);
          @include flexAI(center);
          width: 50%;
          // height: 100%;
          position: relative;
          img.index-out {
            @include flexbox();
            @include flexJC(center);
            @include flexAI(center);
            width: 60%;
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
            top: 20%;
            left: 35%;
            width: 30%;
          }
          img.top_ring {
            position: absolute;
            top: 25%;
            left: 30%;
            width: 100%;
            animation: opacityc 1.5s linear 2s infinite;
            @keyframes opacityc {
              0% {
                opacity: 0;
              }
              50% {
                opacity: 0.8;
              }
              100% {
                opacity: 0;
              }
            }
          }
          img.middle_ring {
            position: absolute;
            top: 35%;
            left: 40%;
            width: 80%;
            animation: rotateZdeg 5s linear 2s infinite;
            @keyframes rotateZdeg {
              0% {
                transform: translate3d(0px, 0px, 0px) scaleX(1) scaleY(1)
                  rotateX(65deg) rotateY(0deg) rotateZ(0deg) skewX(0deg)
                  skewY(0deg);
              }
              100% {
                transform: translate3d(0px, 0px, 0px) scaleX(1) scaleY(1)
                  rotateX(65deg) rotateY(0deg) rotateZ(360deg) skewX(0deg)
                  skewY(0deg);
              }
            }
          }
          img.bottom_ring {
            position: absolute;
            top: 58%;
            left: 40%;
            width: 80%;
            height: 50%;
          }
        }
        .text_box {
          @include flexbox();
          flex-direction: column;
          @include flexJC(center);
          width: calc(50% - 5px);
          height: 100%;
          position: relative;
          .num_title {
            font-size: 18px;
            color: rgba(230, 247, 255, 0.8);
            padding: 5px 0 5px 5px;
          }
          .num_text {
            text-align: left;
            padding-left: 5px;
          }
          .ba_num {
            font-size: 26px;
            font-family: "youshe";
            color: rgb(255, 255, 255);
            letter-spacing: 3px;
          }
          .ba_unit {
            font-size: 12px;
            font-weight: 500;
            color: rgb(255, 255, 255);
          }
        }
        .lanxu_box {
          @include flexbox();
          @include flexJC(center);
          flex-direction: column;
          width: 50%;
          height: 100%;
          position: relative;
          .num_title {
            position: absolute;
            width: 130px;
            top: -5px;
            left: -75px;
            font-size: 18px;
            color: rgba(230, 247, 255, 0.8);
            text-align: center;
          }
          .num_text {
            position: absolute;
            width: 130px;
            top: 26px;
            left: -75px;
            text-align: center;
          }
          .ba_num {
            font-size: 26px;
            font-family: "youshe";
            color: rgb(255, 255, 255);
            letter-spacing: 3px;
          }
          .ba_unit {
            font-size: 12px;
            font-weight: 500;
            color: rgb(255, 255, 255);
          }
        }
        span {
          margin: 0px 0px -4px;
          white-space: nowrap;
        }
        .total_text {
          line-height: 30px;
          font-size: 18px;
          color: rgba(216, 235, 255, 0.75);
        }
        .total_num {
          align-items: baseline;
          padding-top: 5px;
          font-size: 32px;
          // font-family: "youshe";
          font-weight: bold;
          margin: 0px;
          color: rgb(0, 230, 241);
        }
        .total_unit {
          font-size: 18px;
          color: rgb(255, 255, 255);
          transform: translate3d(10px, -2px, 0px);
        }
      }
    }
    .fhxy_wrapper {
      @include flexbox();
      @include flexAI(center);
      @include flexflow(row wrap);
      width: 100%;
      height: calc(50% - 50px);
      box-sizing: border-box;
      .fhxy_item {
        width: 50%;
        height: 100%;
        @include flexbox();
        @include flexAI(center);
        box-sizing: border-box;
        // padding-left: 15px;
        // margin-top: 50px;
        &:first-child {
          width: 45%;
        }
        &:last-child {
          width: 55%;
        }
        img {
          width: 75px;
          height: 75px;
          margin: 0 0 0 15px;
        }
        &:first-child {
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
              margin-right: 3px;
            }
          }
        }
      }
    }
  }
  .right_middle {
    height: calc(34% - 15px);
    overflow: hidden;
    .gs_box {
      width: 100%;
      height: 100%;
      // height: calc(100% - 690px);
      box-sizing: border-box;
      overflow: hidden;
      // padding-left: 10px;
      ul {
        display: flex;
        flex-direction: row;
        justify-content: space-around;
        align-items: center;
        li {
          width: 100%;
          .num_title {
            font-size: 20px;
            color: rgba(230, 247, 255, 0.8);
            text-align: left;
            padding: 8px 0 15px 0px;
          }
          .num_text {
            padding-left: 0px;
          }
        }
      }
      .securityChart {
        width: 100%;
        height: calc(100% - 110px);
      }
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
            width: 110px;
            height: 72px;
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
    }
    .gg_box {
      width: calc(100% - 20px);
      // height: 100%;
      height: calc(100% - 60px);
      box-sizing: border-box;
      overflow: auto;
      margin-left: 20px;
      .gg_item {
        @include flexbox();
        @include flexAI(center);
        margin-top: 10px;
        img {
          // width: 61px;
          // height: 61px;
          margin-right: 15px;
          // @media screen and (min-height: 1000px) {
          //   width: 75px;
          //   height: 75px;
          // }
          // @media screen and (min-height: 1600px) and (max-height: 1900px){
          //   width: 50px;
          //   height: 50px;
          // }
          // @media screen and (min-height: 1900px) and (max-height: 2000px){
          //   width: 61px;
          //   height: 61px;
          // }
        }

        div {
          @include flex(1);
          @include flexbox();
          @include flexAI(center);
          @include flexJC(space-between);
          & > span {
            font-size: 20px;
            color: rgba(230, 247, 255, 0.8);
            text-align: center;
          }
          & > p {
            margin-top: 15px;
            font-size: 26px;
            font-family: "manrope";
            color: rgb(255, 255, 255);
            margin-right: 5px;
            letter-spacing: 3px;
            & > span {
              font-size: 18px;
              font-weight: 600;
              color: #00cfff;
              padding-left: 5px;
            }
          }
        }
      }
      .gg_rate {
        // margin-top: 15px;
        .rate_top {
          @include flexbox();
          @include flexAI(center);
          @include flexJC(space-between);
          p {
            font-size: 18px;
          }
          div {
            font-size: 20px;
            width: 75px;
            height: 30px;
            line-height: 29px;
            text-align: center;
            box-sizing: border-box;
            border: 2px solid #6be2e5;
            font-weight: bold;
            margin-right: 10px;
          }
        }
        .rate_body {
          // 图片进度条
          // margin-top: 15px;
          // background: url($imageUrl + 'rate.png') no-repeat center;
          // background-size: 100% auto;
          // height: 20px;
          // box-sizing: border-box;

          // diy 进度条
          .rate_box {
            margin-top: 10px;
            width: 100%;
            @include flexbox();
            @include flexAI(center);
            @include flexJC(space-between);
            .rate_item {
              width: 8%;
              height: 20px;
              background: #1843a9;
              border-radius: 4px;
              overflow: hidden;
              .rate_item_color {
                width: 0;
                height: 20px;
                background: #4ba6f4;
              }
            }
          }
        }
      }
    }
  }
  .right_bottom {
    // height: 33%;
    height: calc(32% - 18px);
    overflow: hidden;
    .third_box {
      height: calc(100% - 50px);
      ul {
        height: 100%;
        @include flexbox();
        li {
          height: 100%;
          width: 100%;
          @include flexbox();
          @include flexAI(center);
          // @include flexJC(center);
          // @include flexJC(space-between);
          @include flexflow(column wrap);
          padding-bottom: 10px;
          box-sizing: border-box;
          .pic_box {
            position: relative;
            width: 70%;
            height: 60%;
            img.index-out {
              position: absolute;
              width: 100%;
            }
            img.index-in {
              position: absolute;
              top: 0%;
              left: 25%;
              width: 50%;
              animation: imageAnimation 3s linear infinite;
              @keyframes imageAnimation {
                0% {
                  opacity: 1;
                  transform: none;
                }
                50% {
                  opacity: 1;
                  transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg) scaleX(1)
                    scaleY(1) translate3d(0px, 10px, 0px);
                }
                100% {
                  opacity: 1;
                  transform: none;
                }
              }
            }
          }
          .pic_bottom {
            width: 60%;
            .pic_title {
              font-size: 20px;
              color: rgba(230, 247, 255, 0.8);
              text-align: center;
              padding: 5px 0 10px 0;
            }
            .pic_text {
              text-align: center;
            }
          }

          .th_num {
            margin-top: 10px;
            font-size: 28px;
            font-family: "youshe";
            color: rgb(255, 255, 255);
            margin-right: 5px;
          }
          .th_unit {
            color: #fff;
            font-size: 18px;
            font-weight: 600;
          }
        }
      }
      .fd_list {
        width: 100%;
        @include flexbox();
        @include flexAI(center);
        @include flexflow(row wrap);
        .fd_item {
          width: 50%;
          padding-top: 10px;
          h3 {
            background: url($imageUrl + "title_bg2.png") no-repeat left;
            height: 38px;
            line-height: 40px;
            color: #46fdff;
            padding-left: 35px;
            font-size: 16px;
            background-size: auto 100%;
          }
          p {
            color: #fff;
            font-size: 20px;
            padding-left: 35px;
            padding-top: 5px;
            span {
              font-size: 32px;
              font-family: youshe;
              vertical-align: text-bottom;
              margin-right: 5px;
            }
          }
        }
      }
    }
  }
  .right_box {
    ul {
      display: flex;
      flex-direction: row;
      justify-content: space-around;
      align-items: center;
      li {
        width: 100%;
        .num_title {
          font-size: 20px;
          color: rgba(230, 247, 255, 0.8);
          text-align: left;
          padding: 8px 0 15px 30px;
        }
        .num_text {
          font-size: 26px;
          padding-left: 30px;
          font-family: "manrope";
        }
        .ba_num {
          font-size: 26px;
          font-family: "manrope";
          color: rgb(255, 255, 255);
          margin-right: 5px;
        }
        .ba_unit {
          font-size: 18px;
          font-weight: 600;
          color: #00cfff;
        }
        .th_num {
          margin-top: 10px;
          font-size: 28px;
          font-family: "youshe";
          color: rgb(255, 255, 255);
          margin-right: 5px;
        }
        .th_unit {
          color: #fff;
          font-size: 18px;
          font-weight: 600;
        }
        .pic_box {
          position: relative;
          width: 100%;
          height: 120px;
          img.index-out {
            position: absolute;
            width: 100%;
          }
          img.index-in {
            position: absolute;
            top: 0%;
            left: 25%;
            width: 50%;
            animation: imageAnimation 3s linear infinite;
            @keyframes imageAnimation {
              0% {
                opacity: 1;
                transform: none;
              }
              50% {
                opacity: 1;
                transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg) scaleX(1)
                  scaleY(1) translate3d(0px, 10px, 0px);
              }
              100% {
                opacity: 1;
                transform: none;
              }
              100% {
                opacity: 1;
                transform: none;
              }
            }
          }
        }
        .pic_title {
          font-size: 20px;
          color: rgba(230, 247, 255, 0.8);
          text-align: center;
          padding: 5px 0 10px 0;
        }
        .pic_text {
          text-align: center;
        }
      }
    }
    .lenged {
      display: flex;
      flex-direction: column;
      width: 10%;
      position: absolute;
      top: 30%;
      right: 2%;
      .deducing {
        font-family: youshe;
        color: #69e3ee;
        line-height: 18px;
        width: 46px;
        font-size: 18px;
        margin-left: 5px;
        text-align: center;
        border-radius: 18px;
        border: 2px solid #69e3ee;
      }
      li {
        user-select: none;
        -webkit-user-select: none;
        width: 100%;
        font-size: 18px;
        line-height: 28px;
        @include flexbox();
        @include flexAC();
        @include flexJC(flex-start);
        span.active {
          font-weight: bold;
          color: #69e3ee;
        }
        span.green {
          width: 24px;
          height: 14px;
          margin-right: 5px;
          border-radius: 2px;
          background: rgba(91, 190, 88, 0.7);
        }
        span.blue {
          width: 24px;
          height: 14px;
          margin-right: 5px;
          border-radius: 2px;
          background: rgba(56, 134, 255, 0.7);
        }
        span.orange {
          width: 24px;
          height: 14px;
          margin-right: 5px;
          border-radius: 2px;
          background: rgba(247, 181, 0, 0.7);
        }
        span.red {
          width: 24px;
          height: 14px;
          margin-right: 5px;
          border-radius: 2px;
          background: rgba(250, 100, 0, 0.8);
        }
        .selected {
          width: 20px;
          margin-left: 5px;
        }
      }
    }
    #histogram3d {
      width: 65%;
      height: 60%;
    }
  }
  .dialog-playing {
    height: 600px;
    ::v-deep .primary-dialog {
      .el-dialog__body {
        padding: 0;
      }
    }
  }
  .dialog-content {
    height: 650px;
    // overflow: auto;
    img.huimin {
      width: 100%;
    }
  }
}
.dialog-img-content {
  height: 600px;
  width: 100%;
  overflow: auto;
  text-align: center;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE 10+ */
  &::-webkit-scrollbar {
    display: none;
  }
  img.huimin {
    width: 100%;
  }
  &.fullscreen-box .fullscreen-huimin {
    width: 80%;
  }
}
</style>
<style lang="scss">
.el-loading-mask {
  background-color: #4f5fd052;
}
.viewer-container {
  z-index: 9999 !important;
}
.highcharts-data-label-hidden {
  opacity: 1 !important;
}
</style>