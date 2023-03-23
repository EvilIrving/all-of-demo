<!--
 * @Date: 2022-10-18 16:52:17
 * @Author: 
 * @Description: 潮位预报 台风
 * @LastEditors: dtb
 * @LastEditTime: 2022-10-29 15:58:25
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\blueSeawall\seawallRight\SeawallRightTyphoon.vue
-->

<template>
  <div class="wrap">
    <section class="typhoon-box">
      <div class="second_title2">台风</div>
      <div class="typhoon-detail">
        <!--留着备用 start-->
        <!-- <div class="typhoon-tap-detail">
          <div class="fangan_select_type">
            <div
              v-for="(item, index) in typhoonTypeList"
              :key="index"
              :class="{ active_type_btn: index === typhoonTypeIndex }"
              @click="checkTyphoonClick(index)"
              class="select_type_btn"
            >
              {{ item }}
            </div>
          </div>
          <div class="date-picker-box" v-if="typhoonTypeIndex === 1">
            <el-date-picker
              @change="handleChangeYear"
              :clearable="false"
              :picker-options="pickerOptions"
              class="date-picker"
              size="small"
              value-format="yyyy"
              v-model="typhoonYear"
              type="year"
            >
            </el-date-picker>
          </div>
        </div> -->
        <!-- <div class="typhoon-list" v-loading="loading_typhoon">
          <div class="current-typhoon" v-show="typhoonTypeIndex == 0">
            <div class="typhoon" v-if="currentTyphoon">
              <img
                @click="showTyphoon(currentTyphoon.tfid, true)"
                src="../../../../assets/images/typhoon-icon.png"
                alt=""
              />
              <div>
                <h3>
                  {{ currentTyphoon.tfid + currentTyphoon.name }}({{
                    currentTyphoon.enname
                  }})
                </h3>
                <p>
                  当前位置<span
                    >{{ currentTyphoon.lng }}°/{{ currentTyphoon.lat }}°</span
                  >
                </p>
                <p>
                  风速风力<span>{{ currentTyphoon.speed }}米/秒</span
                  ><span class="typhoon-red"
                    >({{ currentTyphoon.power }}级)</span
                  >
                </p>
                <p>
                  移速移向<span
                    >{{ currentTyphoon.movespeed }}公里/小时，{{
                      currentTyphoon.movedirection
                    }}</span
                  >
                </p>
              </div>
            </div>
            <div class="no-typhoon" v-if="currentTyphoon == 1">
              <img src="../../../../assets/images/no-typhoon.png" alt="" />
            </div>
          </div>
          <div class="history-Typhoon" v-if="typhoonTypeIndex == 1">
            <div v-if="carouselShow">
              <el-carousel
                class="carousel-Typhoon"
                trigger="click"
                height="100%"
                :autoplay="false"
                indicator-position="none"
              >
                <el-carousel-item
                  v-for="itemIndex in carouselTyphoonPage"
                  :key="itemIndex"
                  class="carousel-Typhoon-item"
                >
                  <div
                    @click="checkTyphoonItem(item)"
                    :class="{ 'active-Typhoon-item': activeTfid === item.tfid }"
                    v-for="item in typhoonPageList(itemIndex)"
                    :key="item.tfid"
                  >
                    <p>{{ item.tfid }}</p>
                    <h3>{{ item.name }}</h3>
                    <p>{{ item.enname }}</p>
                  </div>
                </el-carousel-item>
              </el-carousel>
            </div>
          </div>
        </div> -->
        <!--留着备用 end-->
        <div class="typhoon-list">
          <div class="current-typhoon">
            <div class="typhoon">
              <img src="../../../../assets/images/typhoon-icon-a.png" alt="" />
              <div>
                <h3>202212梅花(Muifa)</h3>
                <p>当前位置<span>122.00°/30.30°</span></p>
                <p>
                  风速风力<span>42米/秒(14级)</span><span class="typhoon-red"></span>
                </p>
                <p>移速移向<span>30公里/小时，北西</span></p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="fangan_box">
      <div class="second_title2">
        预报潮位
        <span class="title_time">2022-9-13 19时</span>
      </div>
      <div class="scroll_item_title">
        <div>站点名称</div>
        <div>最高潮位(m)</div>
        <div>出现时间</div>
      </div>
      <div class="fangan_list">
        <!-- ["综合", "海洋", "水文", "研究院"], -->
        <div class="fangan_item">
          <p class="third_title">
            <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
            <span @click="switchTide(0)" :class="{ active: fanganIndex === 0 }">综合预报</span>
            <i></i>
          </p>
          <div class="fangan_item_table" ref="tableContainer">
            <div class="scroll_item_box" @click="viewRecords($event)">
              <seamless-scroll :list="tideTableOneData" className="scroll_item_ervice_one" v-if="tideTableOneData.length > 0">
                <div class="scroll_item">
                  <div v-for="(item, idx) in tideTableOneData" :key="idx + 'n'" class="scroll_item_ervice scroll_item_ervice_one" :data-index="idx" data-type="One" data-parent='0'>
                    <div>{{ item.stnm }}</div>
                    <div>{{ item.forecast.toFixed(2) || "-" }}</div>
                    <div>{{ item.forecastTime.slice(5, 16) || "-" }}</div>
                  </div>
                </div>
              </seamless-scroll>
            </div>
          </div>
        </div>
        <div class="fangan_item">
          <p class="third_title">
            <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
            <span @click="switchTide(1)" :class="{ active: fanganIndex === 1 }">海洋预报</span>
            <img class="tip-icon" @click="downloadPdf(1)" src="../../../../assets/images/tip-icon-a.png" alt="" />
            <i></i>
          </p>
          <div class="fangan_item_table" ref="tableContainer">
            <div class="scroll_item_box" @click="viewRecords($event)">
              <seamless-scroll :list="tideTableTwoData" className="scroll_item_ervice_two" v-if="tideTableTwoData.length > 0">
                <div class="scroll_item">
                  <div v-for="(item, idx) in tideTableTwoData" :key="idx + 'n'" class="scroll_item_ervice scroll_item_ervice_two" :data-index="idx" data-type="Two" data-parent='1'>
                    <div>{{ item.stnm }}</div>
                    <div>{{ item.forecast.toFixed(2) || "-" }}</div>
                    <div>{{ item.forecastTime.slice(5, 16) || "-" }}</div>
                  </div>
                </div>
              </seamless-scroll>
            </div>
          </div>
        </div>
        <div class="fangan_item">
          <p class="third_title">
            <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
            <span @click="switchTide(2)" :class="{ active: fanganIndex === 2 }">水文预报</span>
            <img class="tip-icon" @click="downloadPdf(2)" src="../../../../assets/images/tip-icon-a.png" alt="" />
            <i></i>
            <img class="tip-icon" @click="downloadPdf(6)" src="../../../../assets/images/xiaodianshi.png" alt="" />
          </p>
          <div class="fangan_item_table" ref="tableContainer">
            <div class="scroll_item_box" @click="viewRecords($event)">
              <seamless-scroll :list="tideTableThreeData" className="scroll_item_ervice_three" v-if="tideTableThreeData.length > 0">
                <div class="scroll_item">
                  <div v-for="(item, idx) in tideTableThreeData" :key="idx + 'n'" class="scroll_item_ervice scroll_item_ervice_three" :data-index="idx" data-type="Three" data-parent='2'>
                    <div>{{ item.stnm }}</div>
                    <div>{{ item.forecast.toFixed(2) || "-" }}</div>
                    <div>{{ item.forecastTime.slice(5, 16) || "-" }}</div>
                  </div>
                </div>
              </seamless-scroll>
            </div>
          </div>
        </div>
        <div class="fangan_item">
          <p class="third_title">
            <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
            <span @click="switchTide(3)" :class="{ active: fanganIndex === 3 }">研究院预报</span>
            <img class="tip-icon" @click="downloadPdf(3)" src="../../../../assets/images/tip-icon-a.png" alt="" />
            <i></i>
            <img class="tip-icon" @click="downloadPdf(7)" src="../../../../assets/images/xiaodianshi.png" alt="" />
          </p>
          <div class="fangan_item_table" ref="tableContainer">
            <div class="scroll_item_box" @click="viewRecords($event)">
              <seamless-scroll :list="tideTableFourData" className="scroll_item_ervice_four" v-if="tideTableFourData.length > 0">
                <div class="scroll_item">
                  <div v-for="(item, idx) in tideTableFourData" :key="idx + 'n'" class="scroll_item_ervice scroll_item_ervice_four" :data-index="idx" data-type="Four" data-parent='3'>
                    <div>{{ item.stnm }}</div>
                    <div>{{ item.forecast.toFixed(2) || "-" }}</div>
                    <div>{{ item.forecastTime.slice(5, 16) || "-" }}</div>
                  </div>
                </div>
              </seamless-scroll>
            </div>
          </div>
        </div>
      </div>
    </section>

    <PrimaryDialog mainTitle="" class="" :visible="showFanganDialog && fileType" width="85%" :showFooter="false" top="10vh" :fullscreen="fullscreen" @changeFullscreen="changeFullscreen" @handleClose="showFanganDialog = false">
      <div class="img-control" :class="fullscreen ? 'fullscreen' : ''">
        <iframe v-show="fileType" class="iframe-box" :src="iframeSrc" frameborder="0"></iframe>
      </div>
    </PrimaryDialog>

    <div class="mask" v-show="showFanganDialog && !fileType">
      <div :class="['report-dialog',{'full-screen':fullscreen}]">
        <div class="dialog-icon-group">
          <i v-if="!fullscreen" class="dialog-icon dialog-zoom-in" @click="fullscreen = true"></i>
          <i v-else class="dialog-icon dialog-zoom-out" @click="fullscreen = false"></i>
          <i class="dialog-icon dialog-close" @click="showFanganDialog = false"></i>
        </div>
        <div :class="['report-dialog-inner',{'full-screen':fullscreen}]">
          <!-- arrow="always" -->
          <el-carousel ref="carousel" class="carousel_box ppt-carousel" indicator-position="inside" :autoplay="false">
            <el-carousel-item class="carousel_item" v-for="(item, index) in photosList" :key="index">
              <!-- <div class="swiper_img" :style="{backgroundImage:'url('+item.url+')'}" :title="item.name"></div> -->
              <!-- <el-image style="width:100%;height: 85.5vh;" :src="item.url" :fit="fit"></el-image> -->
              <img :src="item.url" alt="" :class="[{'full-box':fullscreen}]">
            </el-carousel-item>

          </el-carousel>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  seawallLeftStatisticApi,
  cwzListApi,
  getPlanApi,
} from "@/api/api_seawall";
import BarChart from "@/components/BarChart";
import PrimaryDialog from "../components/PrimaryDialog.vue";
import jquery from "@/plugins/jquery-1.11.3";
import SeamlessScroll from "@/components/SeamlessScroll";
let currentTimes = new Date();
export default {
  name: "",
  components: {
    BarChart,
    PrimaryDialog,
    SeamlessScroll,
  },
  data() {
    return {
      iframeSrc: "",
      fileType: true,
      showFanganDialog: false,
      fullscreen: false,
      cwFlag: "1",
      seawallList: [],
      seawallAd: "",
      adOptions: [],
      photosList: [{ url: this.$config.zhImgUrl + "jilituTwo.png", name: "jilituTwo" }],
      yjhtfbData: {
        title: "预警分布",
        // yInterval: 2,
        id: "yjhtfbChart",
        xData: [],
        yData: [],
        adcdList: [],
        unit: "条",
        noTitle: true,
      },
      htfbLoading: true,
      tableData: [],
      tableLoading: true,
      squareList: [],
      squareIndex: 0,
      squareValue: "overCnt",
      loading_square: true,
      seawallFangan: "",
      fanganOptions: [
        {
          label: "暂无方案",
          value: "",
        },
      ],
      listFlag: true,
      tableHeight: 0,
      cwztableHeight: 0,
      showChart: true,
      tideTableOneData: [],
      tideTableTwoData: [],
      tideTableThreeData: [],
      tideTableFourData: [],
      tideTableOneInterval: null,
      tideTableTwoInterval: null,
      tideTableThreeInterval: null,
      tideTableFourInterval: null,
      tideTableOnetimeout: null,
      tideTableTwotimeout: null,
      tideTableThreetimeout: null,
      tideTableFourtimeout: null,
      spanArr: [],
      yjNum: 0,
      fanganList: [],
      planName: "",
      planTypeIndex: 0,
      planTypeList: ["综合", "海洋", "水文", "研究院"],
      fanganIndex: 0,
      planId: "",
      showTable: true,
      canExport: false,
      currentTyphoon: null,
      typhoonTypeList: ["当前台风", "历史台风"],
      typhoonTypeIndex: 0,
      typhoonList: [],
      loading_typhoon: true,
      carouselTyphoonPage: 1,
      carouselShow: false,
      activeTfid: "",
      pickerOptions: {
        disabledDate(time) {
          let currentTime = new Date();
          return time.getFullYear() > currentTime.getFullYear();
        },
      },
      typhoonYear: String(currentTimes.getFullYear()),
    };
  },

  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd || this.$localData("get", "userInfo").adcd;
    },
    leftSelectProjectScale() {
      return this.$store.state.leftSelectProjectScale;
    },
  },
  watch: {
    leftSelectAdcd(val) {
      // this.getYbcwData();
      console.log(val,'leftSelectAdcd');
      if (val == '330000000000') {
        switch (this.fanganIndex) {
          case 3:
            this.getTidePoint("DE0DAE5036C54569A7E7B33C5809D031", "Four"); // 20220914.21【梅花】研究院
            break;
          case 1:
            this.getTidePoint("BEB2DC0BDC6C4A06A7D44403C5803B2F", "Two"); //  20220914.21【梅花】水文
            break;
          case 2:
            this.getTidePoint("8ECE24BB90444EDB832AA306FF3681BE", "Three"); // 20220914.21【梅花】海洋
            break;
          default:
            this.getTidePoint("F2DB010A1E87451685EAC761D76FEBFD", "One"); // 综合
            break;
        }
      } else {
        let latlng = []
        switch (val) {
          case '330100000000':
            latlng = ['119.6', '29.8']
            break;
          case '330200000000':
            latlng = ['122', '29.44']
            break;
          case '330300000000':
            latlng = ['120.92', '27.9']
            break;
          case '330600000000':
            latlng = ['120.7', '29.65']
            break;
          case '330900000000':
            latlng = ['122.5', '30.2']
            break;
          case '331000000000':
            latlng = ['121.5', '28.8']
            break;
          case '330400000000':
            latlng = ['121.2', '30.7']
            break;
        }
        this.$parent.$refs.seawallMap._setHtView(latlng, 11);
      }
    },

    cwFlag(val) {
      this.squareIndex = 0;
      this.squareValue = "overCnt";
      this.resizeTable();
      this.getYbcwData();
    },
  },

  mounted() {
    this.getCwzList("F2DB010A1E87451685EAC761D76FEBFD", "One"); // 综合
    this.getCwzList("D0B6254B73B94AAFB3F4FB8C95DBF34B", "Two"); //  20220914.21【梅花】水文
    this.getCwzList("964A168DCCDF4722876804B4376DAD4F", "Three"); // 20220914.21【梅花】海洋
    this.getCwzList("DE0DAE5036C54569A7E7B33C5809D031", "Four"); // 20220914.21【梅花】研究院 
    this.switchTide(0);
    // this.resizeTable();
    this.getPlanList();
    // this.getCurrentTyphoon();
    this.getTyphoonList();
    let that = this;
    window.addEventListener("resize", function () {
      that.resizeTable();
    });
  },
  beforeDestroy() {
    this.$parent.removeTyphoonLayer();
    // window.removeEventListener("resize");
    this.clearTimes("One");
    this.clearTimes("Two");
    this.clearTimes("Three");
    this.clearTimes("Four");
  },
  methods: {
    clearTimes(FaIndex) {
      clearInterval(this["tideTable" + FaIndex + "Interval"]);
      clearTimeout(this["tideTable" + FaIndex + "timeout"]);
    },
    openTimes(idName, FaIndex) {
      this.animate(idName, FaIndex);
    },
    animate(id, FaIndex) {
      // 通过判断，保证页面上只有一个定时器在执行动画
      // if (this["tideTable" + FaIndex + "Interval"]) {
      //   clearInterval(this["tideTable" + FaIndex + "Interval"]);
      //   this["tideTable" + FaIndex + "Interval"] = null;
      // }
      // let element = document.getElementById(id);
      // let target = 0;
      // if (element.children.length > 0) {
      //   target = element.children[0].offsetHeight;
      // }
      // this["tideTable" + FaIndex + "Interval"] = setInterval(() => {
      //   let step = -1;
      //   let current = element.offsetTop;
      //   current += step;
      //   if (current >= -target) {
      //     element.style.top = current + "px";
      //   } else {
      //     element.style.top = "0px";
      //     clearInterval(this["tideTable" + FaIndex + "Interval"]);
      //     let data = this["tideTable" + FaIndex + "Data"][0];
      //     this["tideTable" + FaIndex + "Data"].push(data);
      //     this["tideTable" + FaIndex + "Data"].splice(0, 1);
      //     this["tideTable" + FaIndex + "timeout"] = setTimeout(() => {
      //       clearTimeout(this["tideTable" + FaIndex + "timeout"]);
      //       this.animate(id, FaIndex);
      //     }, 500);
      //   }
      // }, 35);
    },
    switchTide(index) {
      this.fanganIndex = index;
      switch (index) {
        case 3:
          this.getTidePoint("DE0DAE5036C54569A7E7B33C5809D031", "Four"); // 20220914.21【梅花】研究院
          break;
        case 1:
          this.getTidePoint("BEB2DC0BDC6C4A06A7D44403C5803B2F", "Two"); //  20220914.21【梅花】水文
          break;
        case 2:
          this.getTidePoint("8ECE24BB90444EDB832AA306FF3681BE", "Three"); // 20220914.21【梅花】海洋
          break;
        default:
          this.getTidePoint("F2DB010A1E87451685EAC761D76FEBFD", "One"); // 综合
          break;
      }
    },
    downloadPdf(index) {
      this.fileType = true;
      switch (index) {
        case 3:
          this.iframeSrc = `${this.$config.zhImgUrl}河口院-潮位预报样本.pdf`;
          break;
        case 1:
          this.iframeSrc = `${this.$config.zhImgUrl}海洋）20220913.16）浙江省海洋监测预报中心2022年09月13日16时发布风暴潮橙色警报、海浪红色警报02.pdf`;
          break;
        case 2:
          this.iframeSrc = `${this.$config.zhImgUrl}沿海河口水位预报单02（2022年第12号台风“梅花”第8期）.pdf`;
          break;
        case 7:
          this.photosList = [{ url: this.$config.zhImgUrl + "jilituTwo.png", name: "jilituTwo" }]
          this.fileType = false;
          break;
        default:
          this.photosList = [{ url: this.$config.zhImgUrl + "jilitu.png", name: "jilitu" }]
          this.fileType = false;

          break;
      }
      this.showFanganDialog = true;
    },
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
    },
    checkTyphoonClick(index) {
      if (this.typhoonTypeIndex === index) {
        return false;
      }
      this.typhoonTypeIndex = index;
      if (this.typhoonTypeIndex == 1) {
        this.typhoonYear = String(currentTimes.getFullYear());
        this.getTyphoonList(true);
      }
    },
    getCurrentTyphoon() {
      let that = this;
      jquery.ajax({
        url: "https://sk.zjwater.com/typhoonServer/Api/TyhoonActivity",
        type: "get",
        dataType: "jsonp",
        data: {},
        success: function (res) {
          if (res && res.length > 0 && res[0]) {
            that.activeTfid = res[0].tfid;
            that.showTyphoon(res[0].tfid);
            that.currentTyphoon = res[0];
          } else {
            that.currentTyphoon = 1;
          }
          that.$nextTick(() => {
            that.loading_typhoon = false;
          });
        },
      });
    },
    handleChangeYear() {
      this.loading_typhoon = true;
      this.carouselShow = false;
      this.getTyphoonList();
    },
    getTyphoonList(type) {
      let that = this;
      that.typhoonYear = "2022";
      jquery.ajax({
        url:
          "https://sk.zjwater.com/typhoonServer/Api/TyphoonList/" +
          that.typhoonYear,
        type: "get",
        dataType: "jsonp",
        data: {},
        success: function (res) {
          that.typhoonList = res.reverse();
          if (that.typhoonList.length > 0) {
            let data = that.typhoonList.find((item) => item.name == "梅花");
            that.showTyphoon(data.tfid);
            return false;
            that.carouselTyphoonPage = Math.ceil(that.typhoonList.length / 3);
            that.carouselShow = true;
            if (type) {
              that.activeTfid = that.typhoonList[0].tfid;
              that.showTyphoon(res[0].tfid, true);
            }
          }
          that.$nextTick(() => {
            that.loading_typhoon = false;
          });
        },
      });
    },
    checkTyphoonItem(item) {
      if (this.activeTfid === item.tfid) {
        return false;
      }
      this.activeTfid = item.tfid;
      this.showTyphoon(item.tfid, true);
    },
    typhoonPageList(index) {
      let startNum = (index - 1) * 3;
      let endNum = startNum + 3;
      return this.typhoonList.slice(startNum, endNum);
    },
    async showTyphoon(tfid, type) {
      await this.$parent.showTyphoon(tfid, type);
      // jquery.ajax({
      //   url: "http://typhoon.zjwater.gov.cn/Api/TyphoonInfo/" + res[0].tfid,
      //   type: "get",
      //   dataType: "jsonp",
      //   data: {},
      //   success: function (res) {
      //     if (res.length) {
      //       let mydata = {
      //         name: res[0].name,
      //         code: res[0].tfid,
      //         points: [],
      //       };
      //       // let nowDate = new Date('2021-07-25 13:00:00')
      //       res[0].points.map((item, i) => {
      //         item.lat = Number(item.lat);
      //         item.lng = Number(item.lng);
      //         item.index = i;
      //         item.type = "live";
      //         mydata.points.push(item);
      //         // let typhoonTime = new Date(item.time)
      //         // if(typhoonTime < nowDate){
      //         //   mydata.points.push(item);
      //         // }
      //       });
      //       that.tfLng = res[0].points[res[0].points.length - 1].lng;
      //       that.tfLat = res[0].points[res[0].points.length - 1].lat;
      //       const typhoon = new newTyphoon(
      //         that.map,
      //         mydata,
      //         that.typhoonsMap,
      //         that
      //       );
      //       typhoon.init();
      //     }
      //   },
      // });
    },
    getYbcwData() {
      this.getTideAlertData("forecast");
      this.getLengthData();
    },
    // 预警海塘分布
    async getLengthData() {
      this.htfbLoading = true;
      this.yjhtfbData = {
        title: "预警分布",
        // yInterval: 2,
        id: "yjhtfbChart",
        xData: [],
        yData: [],
        adcdList: [],
        unit: "条",
        noTitle: true,
      };
      let opt = {};
      if (this.planId != "") {
        opt = {
          statistic: "adcd",
          adcd: this.leftSelectAdcd,
          itemCategory: "forecast,planId",
          itemValue: this.squareValue + "," + this.planId,
        };
      } else {
        opt = {
          statistic: "adcd",
          adcd: this.leftSelectAdcd,
          itemCategory: "forecast",
          itemValue: this.squareValue,
        };
      }
      let res = await seawallLeftStatisticApi(opt);
      if (res.success) {
        if (res.data.length) {
          for (let i = 0; i < res.data.length; i++) {
            if (res.data[i].adnm.length > 2) {
              this.yjhtfbData.xData.push(
                res.data[i].adnm.substring(0, res.data[i].adnm.length - 1)
              );
            } else {
              this.yjhtfbData.xData.push(res.data[i].adnm);
            }
            this.yjhtfbData.yData.push(res.data[i].num);
            this.yjhtfbData.adcdList.push(res.data[i].adcd);
          }
          // this.showChart = false
          // this.$nextTick(()=>{
          //   this.showChart = true
          // })
        } else {
          this.showChart = false;
          this.$nextTick(() => {
            this.showChart = true;
          });
        }
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }

      this.htfbLoading = false;
    },

    viewRecords(e) {
      let parentNode = e.target.parentNode;
      let index = parentNode.getAttribute("data-index");
      let type = parentNode.getAttribute("data-type");
      let typeIndex = parentNode.getAttribute("data-parent");
      this.rowClick(this["tideTable" + type + "Data"][index], typeIndex);
    },
    rowClick(row, index) {
      if (this.fanganIndex != index) {
        return false
      }
      this.$parent.setView(row.stLgtd, row.stLttd);
      row.lat = row.stLttd;
      row.lng = row.stLgtd;
      row.LGTD = row.stLgtd;
      row.LTTD = row.stLttd;
      row.rightType = "6";
      // this.$parent.showOneSelectPoint([row],true,'tide');
      setTimeout(() => {
        this.$parent.setPopup(row);
      }, 500);
    },

    // async getAdOptions(){
    //   this.adOptions = []
    //   let res = await adListApi()
    //   if(res.success){
    //     if(res.rows.length){
    //       for (let i = 0; i < res.rows.length; i++) {
    //         this.adOptions.push({
    //           label: res.rows[i].coastalName,
    //           value: res.rows[i].id
    //         })
    //       }
    //     }
    //     this.adOptions.unshift({
    //       label: '全部岸段',
    //       value: ''
    //     })
    //   }
    // },

    async getTideAlertData(type) {
      this.loading_square = true;
      this.squareList = [];
      let res = await seawallLeftStatisticApi({
        statistic: type,
        adcd: this.leftSelectAdcd,
        itemCategory: "planId",
        itemValue: this.planId,
      });
      if (res.success) {
        this.yjNum = res.data[0]["total"];
        this.squareList.push(
          // {
          //   type: 'blue',
          //   name: '蓝色',
          //   value: res.data[0]['blue']
          // },
          // {
          //   type: 'yellow',
          //   name: '黄色',
          //   value: res.data[0]['yellow']
          // },
          // {
          //   type: 'orange',
          //   name: '橙色',
          //   value: res.data[0]['orange']
          // },
          // {
          //   type: 'red',
          //   name: '红色',
          //   value: res.data[0]['red']
          // },
          {
            type: "overCnt",
            name: "超设计潮位",
            value: res.data[0]["overCnt"],
            length: res.data[0]["overLength"]
              ? (res.data[0]["overLength"] / 1000).toFixed(1)
              : 0,
          },
          {
            type: "sickCnt",
            name: "超病险塘能力",
            value: res.data[0]["sickCnt"],
            length: res.data[0]["sickLength"]
              ? (res.data[0]["sickLength"] / 1000).toFixed(1)
              : 0,
          },
          {
            type: "overTop",
            name: "超塘顶高程",
            value: res.data[0]["overTop"],
            length: res.data[0]["overTopLength"]
              ? (res.data[0]["overTopLength"] / 1000).toFixed(1)
              : 0,
          }
          // {
          //   type: 'nearCnt',
          //   name: '临设',
          //   value: res.data[0]['nearCnt']
          // },
          // {
          //   type: 'closeCnt',
          //   name: '近设',
          //   value: res.data[0]['closeCnt']
          // },
          // {
          //   type: 'overDesign',
          //   name: '超标准',
          //   value: res.data[0]['overDesign']
          // },
        );
      }
      this.loading_square = false;
    },
    resizeTable() {
      this.$nextTick(function () {
        // const height = this.$refs.tableContainer[0].clientHeight;
        // this.tableHeight = height;
        window.onresize = function () {
          const height = this.$refs.tableContainer[0].clientHeight;
          this.tableHeight = height - 80;
        };
      });
    },
    // 列表
    async getCwzList(planId, FaIndex) {
      this.$parent.$refs.seawallMap.removeAllLayer();
      this.$parent.$refs.seawallMap.closePopup();
      this["tideTable" + FaIndex + "Data"] = [];
      let res = await cwzListApi({
        planId: planId,
        adcd: this.leftSelectAdcd,
      });
      let pointArr = [];
      if (res.success) {
        res.rows.map((v, i) => {
          if (v.forecast) {
            let colorType = "";
            if (v.forecast > v.tideAlertBlue) {
              if (
                v.forecast < v.tideAlertYellow ||
                v.forecast == v.tideAlertYellow
              ) {
                colorType = "blue";
              } else if (
                v.forecast < v.tideAlertOrange ||
                v.forecast == v.tideAlertOrange
              ) {
                colorType = "yellow";
              } else if (
                v.forecast < v.tideAlertRed ||
                v.forecast == v.tideAlertRed
              ) {
                colorType = "orange";
              } else {
                colorType = "red";
              }
            } else {
              colorType = "default";
            }
            v.colorType = colorType;
            this["tideTable" + FaIndex + "Data"].push(v);
            pointArr.push({
              lat: v.stLttd,
              lng: v.stLgtd,
              coastalName: v.coastalName,
              forecastTime: v.forecastTime,
              forecast: v.forecast,
              colorType: colorType,
              rightType: "6",
              stnm: v.stnm,
              src: require("@/assets/images/map/seawall.png"),
            });
          }
        });
        this.spanArr = [];
        let contactDot = 0;
        this["tideTable" + FaIndex + "Data"].forEach((item, index) => {
          item.index = index;
          if (index === 0) {
            this.spanArr.push(1);
          } else {
            if (
              item.stnm === this["tideTable" + FaIndex + "Data"][index - 1].stnm
            ) {
              this.spanArr[contactDot] += 1;
              this.spanArr.push(0);
            } else {
              this.spanArr.push(1);
              contactDot = index;
            }
          }
        });
        this.$nextTick(() => {
          let idName = "";
          switch (FaIndex) {
            case "One":
              idName = "service_0";
              break;
            case "Two":
              idName = "service_1";
              break;
            case "Three":
              idName = "service_2";
              break;
            default:
              idName = "service_3";
              break;
          }
          this.animate(idName, FaIndex);
        });
      }
    },
    // 落点
    async getTidePoint(planId, FaIndex) {
      this.$parent.$refs.seawallMap.removeAllLayer();
      this.$parent.$refs.seawallMap.closePopup();
      this.$parent.$refs.seawallMap.initView();
      let res = await cwzListApi({
        planId: planId,
      });
      let pointArr = [];
      if (res.success) {
        let showLayerName = ["温州", "乍浦", "镇海", "海门"];
        res.rows.map((v, i) => {
          let obj = {};
          if (v.forecast) {
            let colorType = "";
            if (v.forecast > v.tideAlertBlue) {
              if (
                v.forecast < v.tideAlertYellow ||
                v.forecast == v.tideAlertYellow
              ) {
                colorType = "blue";
              } else if (
                v.forecast < v.tideAlertOrange ||
                v.forecast == v.tideAlertOrange
              ) {
                colorType = "yellow";
              } else if (
                v.forecast < v.tideAlertRed ||
                v.forecast == v.tideAlertRed
              ) {
                colorType = "orange";
              } else {
                colorType = "red";
              }
            } else {
              colorType = "default";
            }
            obj = {
              lat: v.stLttd,
              lng: v.stLgtd,
              coastalName: v.coastalName,
              forecastTime: v.forecastTime,
              forecast: v.forecast,
              colorType: colorType,
              rightType: "6",
              stnm: v.stnm,
              src: require("@/assets/images/map/yndq.png"),
            };
            if (showLayerName.includes(v.stnm)) {
              obj.showPopup = true;
            } else {
              obj.showPopup = false;
            }
            pointArr.push(obj);
          }
        });
        this.$parent.showMapPoints(pointArr, "tide");
      }
    },
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 0) {
        const _row = this.spanArr[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col,
        };
      }
    },
    checkPlanClick(index) {
      if (this.planTypeIndex === index) {
        return false;
      }
      this.planTypeIndex = index;
      this.getPlanList();
    },
    async getPlanList() {
      let res = await getPlanApi({
        isMatch: 1,
        fetchAll: true,
        planType: this.planTypeList[this.planTypeIndex],
      });
      if (res.success) {
        this.fanganList = res.rows;
        if (this.fanganList.length > 0) {
          this.planId = this.fanganList[0].id;
        } else {
          this.planId = "";
        }
        this.getYbcwData();
      }
    },

    fanganManage() {
      let url =
        "https://sk.zjwater.com/mgt/coastalTideAlert/forecastList" +
        // let url = 'http://192.168.2.97:8082/mgt/coastalTideAlert/forecastList' +
        "?from=bigscreen&token=" +
        this.$localData("get", "token");
      this.$parent.openFanganManage(url);
    },
    exportList() {
      let adcd = this.leftSelectAdcd;
      let token = this.$localData("get", "token");
      window.open(
        this.$config.host +
        `/mgt/bm/seawallei/toExcel` +
        `?adcd=${adcd}` +
        `&itemCategory=forecast,planId` +
        `&itemValue=${this.squareValue},${this.planId}` +
        `&token=${token}`
      );
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  height: 100%;
  .third_title:before {
    content: none;
  }
  .third_title {
    padding-bottom: 8px;
    span {
      color: #ffffff;
      font-size: 24px;
      font-family: pangmen3;
      &.active {
        color: #46fdff;
      }
    }
    .tip-icon {
      width: 19px;
      height: 20px;
      margin-left: 8px;
      cursor: pointer;
    }
  }
  .second_title2 {
    padding: 0 0 0 37px;
    margin-bottom: 10px;
    transition: all 0.3s linear;
    font-family: "hanyi";
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
    color: #ffffff;
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
    span.title_time {
      font-family: pangmen;
      transition: all 0.2s linear;
      padding-left: 30px;
      font-size: 22px;
      color: #ffffff;
    }
  }
  .typhoon-box {
    height: 22%;
    .typhoon-detail {
      height: calc(100% - 50px);
      .typhoon-tap-detail {
        display: flex;
        justify-content: space-between;
        padding-right: 15px;
        .fangan_select_type {
          padding-top: 0;
          padding-bottom: 8px;
          justify-content: flex-start;
          > div {
            margin-right: 8px;
          }
        }
        .date-picker-box {
          width: 150px;
          .date-picker {
            width: 100%;
            position: relative;
            top: -2px;
          }
        }
      }
      .typhoon-list {
        // height: calc(100% - 38px);
        height: 100%;
        .current-typhoon {
          height: 100%;
          display: flex;
          flex-direction: column;
          justify-content: center;
          .typhoon {
            display: flex;
            padding: 0 0 0 25px;
            width: 100%;
            box-sizing: border-box;
            img {
              // width: 79px;
              // height: 81px;
              width: 98px;
              height: 101px;
              margin-top: 10px;
              cursor: pointer;
            }
            div {
              padding-left: 23px;
              h3 {
                font-family: "pangmen3";
                font-size: 26px;
                color: #46fdff;
                padding-bottom: 15px;
                font-weight: 700;
              }
              p {
                font-size: 18px;
                font-weight: 600;
                color: #ffffff;
                padding-bottom: 15px;
                span {
                  font-size: 18px;
                  padding-left: 40px;
                  color: #46fdff;
                  font-weight: 600;
                  &.typhoon-red {
                    padding-left: 0px;
                    color: rgba(255, 0, 0, 1);
                  }
                }
              }
            }
          }
          .no-typhoon {
            padding: 0 16px;
            height: 100%;
            img {
              width: 100%;
              height: 100%;
            }
          }
        }
        .history-Typhoon {
          height: 100%;
          > div {
            height: 100%;
            .carousel-Typhoon {
              height: 100%;
              .carousel-Typhoon-item {
                display: flex;
                padding: 0 15px;
                > div {
                  width: 117px;
                  height: calc(100% - 2px);
                  background: #f8f8f8;
                  border-radius: 2px;
                  border: 1px solid #d6d6d6;
                  margin-right: 6px;
                  display: flex;
                  flex-direction: column;
                  justify-content: space-between;
                  align-items: center;
                  padding: 12px 0;
                  box-sizing: border-box;
                  cursor: pointer;
                  p {
                    font-size: 14px;
                    color: rgba(0, 0, 0, 0.88);
                  }
                  h3 {
                    font-size: 20px;
                    font-weight: bold;
                    color: #000000;
                  }
                  &:last-child {
                    margin-right: 0;
                  }
                  &.active-Typhoon-item {
                    background: #193cc5;
                    border-color: #193cc5;
                    h3 {
                      color: #fff;
                    }
                    p {
                      color: #ffffff;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  .scroll_item_title {
    width: calc(100% -30px- 32px);
    height: 48px;
    line-height: 48px;
    height: 48px;
    background: rgba(0, 186, 255, 0.2);
    // opacity: 0.2;
    padding: 0 16px;
    align-items: center;
    display: flex;
    margin: 0 15px 16px 15px;
    > div {
      color: #46fdff;
      font-size: 18px;
      font-weight: 600;
      text-align: center;
      opacity: 1 !important;
      font-family: PingFangSC-Semibold, PingFang SC;
      &:first-child {
        text-align: left;
        width: 33%;
      }
      &:nth-child(2) {
        width: 25%;
      }
      &:last-child {
        // text-align: right;
        width: 42%;
      }
    }
  }
  .fangan_box {
    height: 78.8%;
    .fangan_list {
      display: flex;
      justify-content: space-between;
      flex-direction: column;
      height: calc(100% - 55px - 48px - 16px - 26px);
      padding: 0 15px;
      .fangan_item {
        // height: 25%;
        height: 128px;
        box-sizing: border-box;
        // &:first-child {
        //   height: calc(128px + 48px);
        //   .fangan_item_table {
        //     // height: calc(100% - 48px);
        //     height: calc(96px + 48px);
        //     .scroll_item_box {
        //       // height: calc(100% - 40px);
        //       height: calc(96px + 48px);
        //     }
        //   }
        // }
        .fangan_item_title {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 8px 0 7px;
          height: 32px;
          span {
            color: #000000;
            font-size: 16px;
          }
          img {
            width: 19px;
            height: 20px;
            cursor: pointer;
          }
        }
        .fangan_item_table {
          // height: calc(100% - 48px);
          height: 96px;
          .scroll_item_box {
            // height: calc(100% - 40px);
            height: 96px;
            width: 100%;
            overflow: hidden;
            position: relative;
            .scroll_item {
              width: 100%;
              .scroll_item_ervice {
                height: 48px;
                padding: 0 16px;
                align-items: center;
                display: flex;
                // border-bottom: 1px solid rgba(51, 144, 225, 0.4);
                cursor: pointer;
                > div {
                  color: #ffffff;
                  font-size: 18px;
                  text-align: center;
                  &:first-child {
                    text-align: left;
                    width: 33%;
                  }
                  &:nth-child(2) {
                    width: 27%;
                  }
                  &:last-child {
                    // text-align: right;
                    width: 40%;
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  .fbcyj_box {
    height: 30%;
    .select_square {
      height: calc(100% - 80px);
      box-sizing: border-box;
      @include flexbox();
      @include flexJC(space-between);
      @include flexflow(row wrap);
      align-items: center;
      padding: 8px 16px 16px;
      .square_item {
        box-sizing: border-box;
        width: 23.5%;
        height: 116px;
        text-align: center;
        cursor: pointer;
        border-radius: 2px;
        &.active {
          background: #193cc5 !important;
          border: 1px solid #193cc5 !important;
          & > p {
            color: #fff !important;
          }
          & > span {
            color: #fff !important;
          }
          & > h3 {
            color: #fff !important;
          }
        }
        & > p {
          line-height: 24px;
          font-size: 14px;
          color: #000;
          padding: 12px 0 6px 0;
        }
        i {
          font-size: 14px;
          padding-left: 4px;
        }

        & > span,
        & > h3 {
          line-height: 24px;
          font-size: 24px;
          color: #000;
          font-weight: 500;
        }
        & > h3 {
          padding-top: 8px;
        }
        &.overCnt,
        &.nearCnt,
        &.overTop,
        &.closeCnt,
        &.overDesign,
        &.sickCnt {
          width: 32%;
          background: #f8f8f8;
          border: 1px solid #d6d6d6;
        }
        &.blue {
          background: #eef5fe;
          border: 1px solid #2478f0;
        }
        &.yellow {
          background: #fffbf0;
          border: 1px solid #f7c739;
        }
        &.orange {
          background: #fef5f3;
          border: 1px solid #eb7e65;
        }
        &.red {
          background: #fdeeee;
          border: 1px solid #e02020;
        }
      }
    }
    .fangan_select_type {
      padding-bottom: 4px;
      padding-top: 4px;
      .select_type_btn {
        padding: 8px 0;
        width: 31.5%;
        text-align: center;
        cursor: not-allowed;
        &:first-child {
          cursor: default;
        }
      }
    }
  }
  .htfb_box {
    height: 43%;
    .htfbChart {
      margin-top: 3px;
      width: 100%;
      height: calc(100% - 55px);
    }
    .control-measures-box {
      padding: 2px 16px 0;
      .control-measures {
        width: 100%;
        height: 48px;
        background: #f8f8f8;
        border-radius: 4px;
        border: 1px solid #d6d6d6;
        font-size: 16px;
        font-weight: bold;
        color: #2e3d53;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        img {
          width: 24px;
          margin-left: 15px;
        }
      }
    }
  }
  .seawallList_wrap {
    &.hei2 {
      height: calc(100% - 160px);
    }
  }
  .seawallList_box {
    height: calc(100% - 356px);
  }
  .cwzfblist_wrap {
    height: calc(100% - 105px);
  }
  .table_list {
    height: calc(100% - 42px);
    box-sizing: border-box;
    padding-bottom: 16px;
    overflow: auto;
    .el-table td div {
      & > .bold {
        font-weight: bold;
      }
      & > .blue {
        color: #2478f0;
      }
      & > .yellow {
        color: #f7c739;
      }
      & > .orange {
        color: #eb7e65;
      }
      & > .red {
        color: #e02020;
      }
    }
  }
}
.fangan_select_type {
  padding: 13px 16px 12px 16px;
  @include flexbox();
  @include flexJC(space-between);
  @include flexflow(row wrap);
  // overflow-x: auto;
  // overflow-y: hidden;
  .select_type_btn {
    padding: 8px 15px;
    cursor: pointer;
    background-color: #f8f8f8;
    font-size: 14px;
    color: rgba(0, 0, 0, 0.48);
    &.active_type_btn {
      background-color: #193cc5;
      color: #ffffff;
    }
  }
}
.mask {
  position: fixed;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  z-index: 3000;

  .report-dialog {
    position: absolute;
    left: 50%;
    top: 50%;
    padding: 40px 20px 20px 20px;
    transform: translate(-50%, -50%);
    background-color: #fff;
    &.full-screen {
      width: 98%;
      height: 94%;
    }
  }
  .report-dialog-close {
    position: absolute;
    top: -16px;
    right: 19px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 20px;
    color: #fff;
    z-index: 3001;
    cursor: pointer;
  }

  .dialog-icon-group {
    display: flex;
    position: absolute;
    right: 0;
    top: 0;
    padding: 10px 20px;
  }
  .dialog-icon {
    width: 20px;
    height: 20px;
    background-size: 100% 100%;
    cursor: pointer;
  }
  .dialog-zoom-in {
    background: url($imageUrl + "maxscreen.png") no-repeat center;
    background-size: 100%;
  }
  .dialog-zoom-out {
    background: url($imageUrl + "minscreen.png") no-repeat center;
    background-size: 100%;
  }
  .dialog-close {
    background: url($imageUrl + "closeImg.png") no-repeat center;
    background-size: 100%;
  }
  .dialog-icon + .dialog-icon {
    margin-left: 20px;
  }
  .report-dialog-close:before {
    box-sizing: border-box;
    position: absolute;
    left: -21px;
    top: -44px;
    content: "";
    width: 60px;
    height: 60px;
    border-top: 0;
    border-bottom: 20px solid rgba(0, 103, 223, 0.42);
    border-left: 2px solid transparent;
    border-right: 3px solid transparent;
  }
  .report-dialog-inner {
    width: 152vh;
    height: 85.5vh;
    &.full-screen {
      width: 100%;
      height: 100%;
    }
    .ppt-carousel {
      height: 100%;
      img {
        width: 100%;
        height: 100%;
        &.full-box {
          width: 100%;
          height: 100%;
        }
      }
    }
  }
}

.img-control {
  text-align: center;
  height: 100%;
  overflow: auto;
  padding-bottom: 25px;
  box-sizing: border-box;
  &::-webkit-scrollbar {
    display: none;
  }
  width: 100%;
  img.img-small {
    width: 100%;
  }
  &.fullscreen {
    .img-big {
      display: block;
      margin: 0 auto;
      width: 75%;
    }
  }
  .iframe-box {
    width: 100%;
    height: calc(100% - 15px);
  }
  &.fullscreen .iframe-box {
    height: calc(100% - 15px);
  }
}
</style>
<style lang="scss">
.select_title {
  .el-radio {
    color: #000;
    font-size: 16px;
    margin-right: 8px;
  }
  .el-radio__input.is-checked + .el-radio__label {
    color: #000;
  }
  .el-radio__label {
    padding-left: 4px;
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

#fangan_select {
  padding: 0 16px 12px 16px;
  // overflow-x: auto;
  // overflow-y: hidden;
  .el-select {
    width: 100%;
    padding: 8px 0;
    background-color: #f8f8f8;
    // height: 32px;
    .el-input__inner {
      border: none;
      background-color: #f8f8f8;
      &::-webkit-input-placeholder {
        /* Chrome/Opera/Safari */
        color: #606266;
      }
      &::-moz-placeholder {
        /* Firefox 19+ */
        color: #606266;
      }
      &:-ms-input-placeholder {
        /* IE 10+ */
        color: #606266;
      }
      &:-moz-placeholder {
        /* Firefox 18- */
        color: #606266;
      }
    }
    .el-input__suffix {
      position: absolute;
      right: 5px;
      &::after {
        content: "";
        position: absolute;
        left: -5px;
        top: 50%;
        margin-top: -8px;
        width: 1px;
        height: 16px;
        background-color: #e5e5e5;
      }
      .el-input__suffix-inner {
        .el-select__caret {
          color: rgba(0, 0, 0, 0.48);
        }
        .el-icon-arrow-up:before {
          content: "\e78f";
        }
      }
    }
  }
}

// #fangan_select::-webkit-scrollbar-track {
//   /*滚动条里面轨道*/
//   // -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
//   background: #fff;
//   // background-color: #fff;
//   // display: none

// }
.noFangan {
  text-align: center;
  padding-top: 10px;
}

.fangan_item_table {
  height: calc(100% - 47px);
  .fangan_table_box {
    &.el-table th {
      background-color: #f9f9f9;
    }
  }
}
</style>