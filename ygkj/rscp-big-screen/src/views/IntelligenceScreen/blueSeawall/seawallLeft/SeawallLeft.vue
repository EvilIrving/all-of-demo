
<template>
  <div class="warp">
    <p class="second_title2">海塘总数</p>
    <section class="top_box">
      <ul class="aqjd_box">
        <li class="aqjd_item" v-for="(item, index) in htStatistics" :key="index">
          <img :src="item.src" alt="" />
          <div>
            <!-- <p>{{ item.title }}</p> -->
            <p>
              <span class="num">{{ item.num }}</span>
              <span class="unit">{{ item.unit }}</span>
            </p>
          </div>
        </li>
      </ul>
      <p class="third_title">
        <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
        <span :class="{active: htTabsActive == index ,chooesedActive: choosedItem == item.name}" v-for="(item, index) in htTabs" :key="index" @click="(htTabsActive = index), clickSquare(index,1)">{{ item.name }}</span>
        <i></i>
        <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" />
      </p>
      <!-- 分布 -->
      <bar-chart v-if="htTabsActive === 0 && !htfbLoading" v-loading="htfbLoading" class="htfbChart" :chartData="htfbData"></bar-chart>
      <!-- 长度 -->
      <bar-chart v-if="htTabsActive === 1 && !htglsLoading" v-loading="htglsLoading" class="htfbChart" :chartData="htglsData"></bar-chart>
      <!-- 级别 -->
      <pie-chart v-if="htTabsActive === 2&& !htjbtjLoading" v-loading="htjbtjLoading" class="htfbChart" :chartData="htjbtjData"></pie-chart>
      <ul v-if="htTabsActive === 2 && htjbtjData.series.length !== 0" class="lenged">
        <!-- 1.以下，  2.以上 -->
        <li class="" v-if="this.htjbtjData.series[0]">
          <span class="one"></span><span class="selected">一级海塘 ({{this.htjbtjData.series[0].value}}条/段)</span>
          <span></span>
        </li>
        <li class="" v-if="this.htjbtjData.series[1]">
          <span class="two"></span><span class="selected">二级海塘 ({{this.htjbtjData.series[1].value}}条/段)</span>
          <span></span>
        </li>
        <li class="" v-if="this.htjbtjData.series[2]">
          <span class="three"></span><span class="selected">三级海塘 ({{this.htjbtjData.series[2].value}}条/段)</span>
          <span></span>
        </li>
        <li class="" v-if="this.htjbtjData.series[3]">
          <span class="four"></span><span class="selected">四级海塘 ({{this.htjbtjData.series[3].value}}条/段)</span>
          <span></span>
        </li>
        <li class="" v-if="this.htjbtjData.series[4]">
          <span class="five"></span><span class="selected">非标海塘 ({{this.htjbtjData.series[4].value}}条/段)</span>
          <span></span>
        </li>
      </ul>
    </section>
    <div class="second_title2">沿塘设施</div>
    <section class="middle_box">
      <p class="third_title">
        <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
        <span :class="{active: htFacilityOneActive == index,chooesedActive: clickType == item.name}" :style="{cursor : index == htFacilityOneTabs.length - 1 ? 'not-allowed' : 'pointer'}" v-for="(item, index) in htFacilityOneTabs" :key="index" @click="clickSquare(index,3)">{{ item.name }}</span>
        <i></i>
        <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" />
      </p>
      <div class="middle_box_list">
        <div class="control-one-list" v-show="htFacilityOneActive === 0">
          <div class="control-one-item" :class="{}" v-for="(item, index) in oneList" :key="index">
            <div class="item-info">
              <p>
                <img :src="item.imgUrl" alt="" />
                {{ item.name }}
              </p>
              <div>
                <p :class="['cursor',{xunchaActive:choosedItem===item.name }]" @click='showThePopup(index,item.num,0)'>
                  <span>{{ item.num }}</span>座
                </p>
              </div>
            </div>
          </div>
        </div>
        <div class="control-one-list" v-show="htFacilityOneActive === 1">
          <div class="control-one-item" :class="{}" v-for="(item, index) in twoList" :key="index">
            <div class="item-info">
              <p>
                <img :src="item.imgUrl" alt="" />
                {{ item.name }}
              </p>
              <div>
                <p :class="['cursor',{xunchaActive:choosedItem===item.name }]" @click='showThePopup(index,item.num,1)'>
                  <span>{{ item.num }}</span> <span>座</span>
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <div class="second_title2">工程施工</div>
    <section class="third_box">
      <p class="third_title">
        <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
        <span :class="{active: htFacilityTwoActive == index,chooesedActive: choosedItem == item.name}" v-for="(item, index) in htFacilityTwoTabs" :key="index" @click="clickSquare(index,2)">{{ item.name }}</span>
        <i></i>
        <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" />
      </p>
      <bar-chart v-loading="htchartDataLoading" class="htfbChart" :chartData="chartData"></bar-chart>
    </section>
    <div class="second_title2">安全鉴定</div>
    <div class="fourth_box">
      <ul class="tab_box">
        <li>
          <p class="little_title">数量</p>
          <div @click="chooesdCheck('数量二类塘')" :class='["tab_box_item cursor",{chooesedBox: choosedItemBox === "数量二类塘"}]'>
            <span class="ba_text">二类塘</span>
            <span class="ba_num">
              {{ cxjgStaticData.elwkg + cxjgStaticData.elykg }}
            </span>
            <p class="ba_unit">条(段)</p>
          </div>
          <div @click="chooesdCheck('数量三类塘')" :class='["tab_box_item cursor",{chooesedBox: choosedItemBox === "数量三类塘"}]'>
            <span class="ba_text">三类塘</span>
            <span class="ba_num">
              {{ cxjgStaticData.slwkg + cxjgStaticData.slywg }}
            </span>
            <p class="ba_unit">条(段)</p>
          </div>
        </li>
        <li>
          <p class="little_title">未加固</p>
          <div @click="chooesdCheck('未加固二类塘')" :class='["tab_box_item cursor",{chooesedBox: choosedItemBox === "未加固二类塘"}]'>
            <span class="ba_text">二类塘</span>
            <span class="ba_num">
              {{ cxjgStaticData.elwkg }}
            </span>
            <p class="ba_unit">条(段)</p>
          </div>
          <div @click="chooesdCheck('未加固三类塘')" :class='["tab_box_item cursor",{chooesedBox: choosedItemBox === "未加固三类塘"}]'>
            <span class="ba_text">三类塘</span>
            <span class="ba_num">
              {{ cxjgStaticData.slwkg }}
            </span>
            <p class="ba_unit">条(段)</p>
          </div>
        </li>
        <li>
          <p class="little_title">加固中</p>
          <div @click="chooesdCheck('加固中二类塘')" :class='["tab_box_item cursor",{chooesedBox: choosedItemBox === "加固中二类塘"}]'>
            <span class="ba_text">二类塘</span>
            <span class="ba_num">
              {{ cxjgStaticData.elykg }}
            </span>
            <p class="ba_unit">条(段)</p>
          </div>
          <div @click="chooesdCheck('加固中三类塘')" :class='["tab_box_item cursor",{chooesedBox: choosedItemBox === "加固中三类塘"}]'>
            <span class="ba_text">三类塘</span>
            <span class="ba_num">
              {{ cxjgStaticData.slywg }}
            </span>
            <p class="ba_unit">条(段)</p>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import BarChart from "../components/BarChart";
import PieChart from "../components/PieChart.vue";
import {
  seawallLeftStatisticApi,
  zhSeawall,
  seawallProIndo,
  ytFacility,
  safeReinforNum,
} from "@/api/api_seawall";
export default {
  name: "",
  props: {},
  components: {
    BarChart,
    PieChart,
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd || this.$localData("get", "userInfo").adcd;
    },
    hideCity() {
      return this.$store.state.hideCity;
    },

    leftSelectProjectScale() {
      return this.$store.state.leftSelectProjectScale;
    },
    choosedItem() {
      return this.$store.state.choosedItem;
    },
    choosedItemBox() {
      return this.$store.state.choosedItemBox;
    },
    damType() {
      let type = ''
      if (this.choosedItem !== '') {
        switch (this.choosedItem) {
          case '安澜工程':
            type = 12
            break;
          case '水闸':
            type = 13
            break;
          case '泵站':
            type = 14
            break;
          case '闸站':
            type = 15
            break;
          default:
            type = ''
            break;
        }
      } else {
        switch (this.choosedItemBox) {
          case '数量二类塘':
            type = 2
            break;
          case '数量三类塘':
            type = 3
            break;
          case '未加固二类塘':
            type = 6
            break;
          case '未加固三类塘':
            type = 7
            break;
          case '加固中二类塘':
            type = 9
            break;
          case '加固中三类塘':
            type = 10
            break;
          default:
            type = ''
            break;
        }
      }
      return type
    },
  },
  watch: {
    leftSelectAdcd(val) {
      this.htTotal()
      this.getHtfbData()
      this.getHtjbtjData()
      this.getColorSeawallList();
      this.getKouMenData();
      this.getGateData();
      this.getXzyyData();
      this._safeReinforNum();
    },
    leftSelectProjectScale(val) {
      // this.$parent.removeCityLayer();
      // this.$parent.setRemoveCityType(false)
      this.getColorSeawallList();
    },
    damType: {
      handler(val) {
        this.getColorSeawallList()
      },
      deep: true
    },
  },
  data() {
    return {
      htStatistics: [
        {
          name: "",
          num: 815,
          unit: "条（段）",
          title: "条数",
          src: require("../../../../assets/images/sl_number.png"),
        },
        {
          name: "",
          num: 2014,
          unit: "公里",
          title: "长度",
          src: require("../../../../assets/images/sl_length.png"),
        },
      ], //海塘概况 统计
      htTabsActive: 0,
      htTabs: [
        { name: "分布", sort: 0 },
        { name: "长度", sort: 1 },
        { name: "等级", sort: 2 },
      ], //海塘概况 tab栏
      htFacilityOneActive: 0,
      htFacilityOneTabs: [
        { name: "沿塘闸站", sort: 0 },
        { name: "其他口门", sort: 1 },
        { name: "交叉管线", sort: 1 },
      ], // 沿塘设施 第一个
      clickType: "",
      htFacilityTwoActive: 0,
      htFacilityTwoTabs: [
        { name: "安澜工程", sort: 0 },
        { name: "涉塘工程", sort: 1 },
      ], // 沿塘设施 第二个
      oneActiveItem: 0,
      oneList: [
        // {
        //   imgUrl: require("@/assets/images/control-one-a.png"),
        //   name: "大中型闸站",
        //   num: 68,
        // },
        {
          imgUrl: require("@/assets/images/control-one-b.png"),
          name: "水闸",
          num: 1370,
        },
        {
          imgUrl: require("@/assets/images/control-one-c.png"),
          name: "泵站",
          num: 78,
        },
        {
          imgUrl: require("@/assets/images/control-one-d.png"),
          name: "闸站",
          num: 80,
        },
      ],
      twoList: [
        {
          name: "旱闸",
          num: 451,
          imgUrl: require("@/assets/images/control-two-a.png"),
        },
        {
          name: "缺口",
          num: 251,
          imgUrl: require("@/assets/images/control-two-b.png"),
        },
        {
          name: "道口",
          num: 462,
          imgUrl: require("@/assets/images/control-two-c.png"),
        },
        {
          name: "船坞口",
          num: 0,
          imgUrl: require("@/assets/images/control-two-d.png"),
        },
      ],
      cxjgStaticData: {},

      htfbData: {
        noTitle: true,
        title: "海塘数量(条)",
        id: "htfbChart",
        xData: [],
        yData: [],
        adcdList: [],
        unit: "条(段)",
      },
      htfbLoading: true,

      htjbtjData: {
        noTitle: true,
        title: "海塘等级",
        id: "htjbtjChart",
        unit: "条(段)",
        legend: {
          orient: "vertical",
          left: "65%",
          height: "80%",
        },
        color: [
          "#0C9800",
          "#17DA07",
          "#5AE34F",
          "#A8EBA2",
          "#A8EBA2",
          "#DCEDC8",
        ],
        series: [],
      },
      htjbtjLoading: true,

      htglsData: {
        noTitle: true,
        title: "海塘长度(km)",
        id: "htglsChart",
        xData: [],
        yData: [],
        adcdList: [],
        unit: "公里",
        noWords: false,
      },
      htglsLoading: true,
      cxjgStaticData: {},

      chartData: {
        noTitle: true,
        title: "安澜工程",
        id: "htAnLanChart",
        xData: [],
        yData: [],
        adcdList: [],
        unit: "条(段)",
      },
      htchartDataLoading: false,
      htAnLanData: {
        noTitle: true,
        title: "安澜工程",
        id: "htAnLanChart",
        xData: [],
        yData: [],
        adcdList: [],
        yInterval: 5,
        unit: "条(段)",
      },
      htSheTData: {
        noTitle: true,
        title: "涉塘工程",
        id: "htSheTChart",
        xData: ["杭州", "宁波", "嘉兴", "绍兴"],
        yData: [3, 1, 1, 1],
        adcdList: [],
        yInterval: 1,
        unit: "条(段)",
      },

      barHeight: "",
      SecondbarHeight: "",
      pieHeight: "",
      htjbtjData: {
        noTitle: true,
        title: "海塘等级",
        id: "htjbtjChart",
        unit: "条(段)",
        legend: {
          orient: "vertical",
          left: "65%",
          height: "80%",
        },
        color: [
          "#0C9800",
          "#0DB200",
          "#12E800",
          "#93F28B",
          "#E6FBE4",
          "#E6FBE4",
        ],
        series: []
      },
      pointArr: {},
    };
  },
  async mounted() {
    // this.barHeight = window.innerHeight / 2 * 15 / (15 + 19) + 'px'
    // this.pieHeight = window.innerHeight / 2 * 19 / (15 + 19) + 'px'
    // this.barHeight = window.innerHeight * 7 / 24 + 'px'
    // this.pieHeight = window.innerHeight * 5 / 12 + 'px'
    this.htTotal()
    this.getHtfbData();
    this.getHtglsData();
    this.getHtjbtjData();
    this.getColorSeawallList();
    this.getXzyyData();
    this.getGateData();
    this.getKouMenData()
    await this._safeReinforNum();
    this.chartData = this.htAnLanData
    // this.$parent.setCityfillColor();
    // this.$parent.setRemoveCityType(true)
  },
  beforeDestroy() {
    // window.removeEventListener("resize");
    this.$parent.removeCityLayer();
    this.$parent.setRemoveCityType(false)
  },
  methods: {
    // 海塘总数
    async htTotal() {
      let res = await zhSeawall.seawallTotal({
        adcd: this.leftSelectAdcd,
      });
      if (res.success) {
        this.htStatistics[0].num = res.data.num
        this.htStatistics[1].num = res.data.length
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }

    },
    // 安澜工程
    async _safeReinforNum() {
      let res = await safeReinforNum({
        adcd: this.leftSelectAdcd,
      });
      let xData = [], yData = [], adcdData = [];
      if (res.success) {
        if (res.data.length) {
          res.data.map((item) => {
            if (item.adnm.length > 2) {
              xData.push(item.adnm.replace(/市/, ""));
            } else {
              xData.push(item.adnm);
            }
            yData.push(item.num)
            adcdData.push(item.adcd);
          })
        }
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.htAnLanData.xData = xData;
      this.htAnLanData.yData = yData;
      this.htAnLanData.adcdData = adcdData;
    },
    resizeHeight() {
      if (window.innerHeight > 1000) {
        this.barHeight = window.innerHeight / 3.5 + "px";
        this.SecondbarHeight = window.innerHeight / 3.5 + "px";
      } else {
        this.barHeight = window.innerHeight / 4 + "px";
        this.SecondbarHeight = window.innerHeight / 4 + "px";
      }
    },

    /** 
     * @function: 
     * @description: 
    * @param {*} index 点击项目  
     * @param {*} order 点击类型 1 海塘总数 2 工程施工 3 沿塘设施 
     * @return {*}
     */
    clickSquare(index, order) {
      if (order === 1) {
        this.htTabsActive = index;
        this.clickType = ''
        this.$store.commit('SET_CHOOSED_ITEM', this.htTabs[index].name)
        switch (index) {
          case 1:
            this.getHtglsData();
            break;
          case 2:
            this.getHtjbtjData();
            break;
          default:
            this.getHtfbData();
            break;
        }
      } else if (order === 2) {
        this.htchartDataLoading = true;
        this.htFacilityTwoActive = index;
        switch (index) {
          case 1:
            this.chartData = this.htSheTData;
            this.$store.commit('SET_CHOOSED_ITEM', '')
            break;
          default:
            this.$store.commit('SET_CHOOSED_ITEM', this.htFacilityTwoTabs[index].name)
            this.chartData = this.htAnLanData;
            break;
        }
        this.htchartDataLoading = false;
      } else {
        if (index === 2) {
          return;
        }
        this.clickType = this.htFacilityOneTabs[index].name
        this.htFacilityOneActive = index;
        index === 0 ? this.$store.commit('SET_CHOOSED_ITEM', this.oneList[0].name) : this.$store.commit('SET_CHOOSED_ITEM', this.twoList[0].name)

      }
      this.$store.commit('SET_CHOOSED_ITEM_BORDER', '')
    },
    showThePopup(index, flag, order) {
      // 数字为0 不显示悬浮窗
      if (!flag) return;
      if (!order) {
        this.$store.commit('SET_CHOOSED_ITEM', this.oneList[index].name)
        this.$parent.$refs.seawallMap._drawBlockOverlays(this.oneList[index].name)
      } else {
        this.$store.commit('SET_CHOOSED_ITEM', this.twoList[index].name)
        this.$parent.$refs.seawallMap._drawBlockOverlays(this.twoList[index].name)
      }

      this.$store.commit('SET_CHOOSED_ITEM_BORDER', '')
    },
    chooesdCheck(item) {
      this.clickType = ''
      this.$store.commit('SET_CHOOSED_ITEM', '')
      this.$store.commit('SET_CHOOSED_ITEM_BORDER', item)
    },

    //  三色码 柱状图
    async getHtfbData() {
      this.htfbLoading = true;
      let res = await zhSeawall.seawallthreeYardBarChart({
        adcd: this.leftSelectAdcd,
        userAdcd: this.leftSelectAdcd,
        projectScale: this.leftSelectProjectScale,
      });
      if (res.success) {
        let xData = [];
        let yData = [];
        let adcdList = [];
        if (res.rows.length > 0) {
          res.rows.forEach((element) => {
            // 拿到 市级中心点位
            element.name = element.adnm
            switch (element.adnm) {
              case '杭州市':
                element.lng = '119.6'
                element.lat = '29.8'
                break;
              case '宁波市':
                element.lng = '122'
                element.lat = '29.44'
                break;
              case '温州市':
                element.lng = '120.92'
                element.lat = '27.9'
                break;
              case '湖州市':
                element.lng = '120.2'
                element.lat = '30.7'
                break;
              case '绍兴市':
                element.lng = '120.7'
                element.lat = '29.65'
                break;
              case '金华市':
                element.lng = '120.2'
                element.lat = '28.7'
                break;
              case '丽水市':
                element.lng = '119.5'
                element.lat = '27.8'
                break;
              case '衢州市':
                element.lng = '119'
                element.lat = '28.7'
                break;
              case '舟山市':
                element.lng = '122.2'
                element.lat = '30.2'
                break;
              case '台州市':
                element.lng = '121.5'
                element.lat = '28.8'
                break;
              case '丽水市':
                element.lng = '119.3'
                element.lat = '27.6'
                break;
              case '嘉兴市':
                element.lng = '121.2'
                element.lat = '30.7'
                break;
            }
            if (element.red + element.yellow + element.green !== 0) {
              if (element.adnm.length > 2) {
                xData.push(element.adnm.replace(/市/, ""));
              } else {
                xData.push(element.adnm);
              }
              yData.push(element.total);
              adcdList.push(element.adcd);
            }
          });
        }
        this.htfbData.xData = xData;
        this.htfbData.yData = yData;
        this.htfbData.adcdList = adcdList;
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.htfbLoading = false;
    },

    // 获取沿塘闸站 统计数据
    async getGateData() {
      let opt = {
        adcd: this.leftSelectAdcd,
      };
      let res = await zhSeawall.seawallAssPrcd(opt);
      if (res.success) {
        this.oneList[0].num = res.data[0].sluiceNum;
        this.oneList[1].num = res.data[0].pumpNum;
        this.oneList[2].num = res.data[0].gateNum;
      }
    },
    // 获取 其他口门统计数据
    async getKouMenData() {
      let opt = {
        adcd: this.leftSelectAdcd,
      };
      let res = await zhSeawall.seawallDoor(opt);
      if (res.success) {
        this.twoList[0].num = res.data[0].dryGate;
        this.twoList[1].num = res.data[0].gapNum;
        this.twoList[2].num = res.data[0].crossNum;
        this.twoList[3].num = res.data[0].dockNum || 0;
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

    // 海塘公里数统计
    async getHtglsData() {
      this.htglsData = {
        noTitle: true,
        title: "海塘长度(km)",
        id: "htglsChart",
        xData: [],
        yData: [],
        adcdList: [],
        unit: "公里",
        noWords: false,
      };
      let res = await zhSeawall.adcdLocation({
        adcd: this.leftSelectAdcd,
        userAdcd: this.leftSelectAdcd,
      });
      if (res.success) {
        let num = 0;
        if (res.data.length) {
          for (let i = 0; i < res.data.length; i++) {
            if (res.data[i].adnm.length > 2) {
              this.htglsData.xData.push(
                res.data[i].adnm.substring(0, res.data[i].adnm.length - 1)
              );
            } else {
              this.htglsData.xData.push(res.data[i].adnm);
            }
            this.htglsData.yData.push((res.data[i].length / 1000).toFixed(0));
            this.htglsData.adcdList.push(res.data[i].adcd);
            num += res.data[i].length;
          }
          // this.htglsData.title = `海塘长度（${(num / 1000).toFixed(1)}公里）`
          this.htglsData.title = `海塘长度(km)`;
        }
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.htglsLoading = false;
    },

    // 海塘级别统计
    async getHtjbtjData() {
      this.htjbtjData = {
        noTitle: true,
        title: "海塘等级",
        id: "htjbtjChart",
        unit: "条",
        legend: {
          orient: "vertical",
          left: "65%",
          height: "80%",
        },
        color: [
          "#0C9800",
          "#0DB200",
          "#12E800",
          "#93F28B",
          "#E6FBE4",
          "#E6FBE4",
        ],
        series: [],
      };
      let res = await seawallLeftStatisticApi({
        statistic: "scale",
        adcd: this.leftSelectAdcd,
        userAdcd: this.leftSelectAdcd,
      });
      if (res.success) {
        if (res.data.length) {
          for (let i = 0; i < res.data.length; i++) {
            let htName;
            switch (res.data[i].project_scale) {
              case 1:
              case "1":
                htName = "一级海塘";
                break;
              case 2:
              case "2":
                htName = "二级海塘";
                break;
              case 3:
              case "3":
                htName = "三级海塘";
                break;
              case 4:
              case "4":
                htName = "四级海塘";
                break;
              default:
                htName = "非标海塘";
                break;
            }
            if (res.data[i].project_scale != 6) {
              this.htjbtjData.series.push({
                name: htName,
                value: res.data[i].num,
                project_scale: res.data[i].project_scale,
              });
            }
          }
        }
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.htjbtjLoading = false;
    },

    // 海塘分布柱状图点击事件
    barClickAdcd1(index) {
      let cities = ['杭州', '宁波', '温州', '嘉兴', '绍兴', '舟山', '台州']
      if (cities.includes(this.htfbData.xData[index])) {
        if (this.htfbData.xData[index] == "省级") {
          this.$store.commit("SET_LEFT_SELECT_ADCD", "33");
        } else {
          this.$store.commit(
            "SET_LEFT_SELECT_ADCD",
            this.htfbData.adcdList[index]
          );
        }
        this.getHtglsData()
      }
    },

    // 海滩公里数统计柱状图点击事件
    barClickAdcd2(index) {
      if (this.htglsData.xData[index] == "省级") {
        this.$store.commit("SET_LEFT_SELECT_ADCD", "33");
      } else {
        this.$store.commit(
          "SET_LEFT_SELECT_ADCD",
          this.htglsData.adcdList[index]
        );
      }
      // this.$parent.removeCityLayer();
      // this.$parent.setRemoveCityType(false)
    },

    /**
     * @function:
     * @description: 首页海塘落点请求接口
     * @param {*} type 落点类型13 14 15 12 2 6 7 3 9 10 按顺序从上到下点击切换点位数据
     * @param {*} name   请求接口用
     * @return {*}
     */
    async getColorSeawallList(name = "") {
      // 13 14 15  12 2 6 7 3 9 10
      let pointType = ''
      this.pointArr = {
        '_all': [],
        '_damGate': [],
        '_pump': [],
        '_sluice': [],
        'anLan': [],
        'erTangNum': [],
        'erTangWjg': [],
        'erTangJgz': [],
        'slTangNum': [],
        'slTangWjg': [],
        'slTangJgz': [],
      };

      switch (this.damType) {
        case '':
          pointType = '_all'
          break;
        case 13:
          pointType = '_damGate'
          break;
        case 14:
          pointType = '_pump'
          break;
        case 15:
          pointType = '_sluice'
          break;


        case 12:
          pointType = 'anLan'
          break;
        case 2:
          pointType = 'erTangNum'
          break;
        case 6:
          pointType = 'erTangWjg'
          break;
        case 7:
          pointType = 'erTangJgz'
          break;
        case 3:
          pointType = 'slTangNum'
          break;
        case 9:
          pointType = 'slTangWjg'
          break;
        case 10:
          pointType = 'slTangJgz'
          break;
      }
      let opt = {
        adcd: this.leftSelectAdcd,
        dam: this.damType,
        projectScale: this.leftSelectProjectScale,
      };
      this.$parent.$refs.seawallMap.removeAllLayer();
      this.$parent.$refs.seawallMap.closePopup();
      let latlng = [];
      if (this.leftSelectAdcd === '330000000000') {
        this.$parent.$refs.seawallMap.initView();
      } else {
        switch (this.leftSelectAdcd) {
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
      }
      let res = null
      if (this.damType === 13 || this.damType === 14 || this.damType === 15) {
        res = await ytFacility(opt);
      } else {
        res = await seawallProIndo(opt);
      }

      if (res.success) {
        let lineArr = [];
        if (res.data.length) {
          res.data.map((v) => {
            v.lat = v.lttd || +v.LTTD;
            v.lng = v.lgtd || +v.LGTD;
            v.rightType = 4;
            v.styleObj = {
              scale: 1,
            };

            v.mapType = "seawallPopup";
            if (this.damType === 13 || this.damType === 14 || this.damType === 15) {

            } else {
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
            }

            v.color = pointType
            this.pointArr[pointType].push(v)
          });
        }

        if (this.damType === 13 || this.damType === 14 || this.damType === 15) {
          this.$parent.showMapPointJh(this.pointArr[pointType], "ptSeawallPointLayer");
          if (this.leftSelectAdcd !== '330000000000') this.$parent.$refs.seawallMap._setHtView(latlng, 9.5);
        } else {
          this.$parent.showMapPointJh(this.pointArr[pointType], "ptSeawallPointLayer");
          this.$parent.showMapLines(lineArr);
          if (this.leftSelectAdcd !== '330000000000') this.$parent.$refs.seawallMap._setHtView(latlng, 9.5);
        }

      }
    },

    // 海塘设计防洪标准统计点击事件
    pieClick1(data) {
      this.$store.commit("SET_LEFT_SELECT_PREVENT_LEVEL", data.preventLevel);
    },

    // 海塘级别统计点击事件
    pieClick2(data) {
      this.$store.commit("SET_LEFT_SELECT_PROJECT_SCALE", data.project_scale);
      // this.$parent.removeCityLayer();
      // this.$parent.setRemoveCityType(false)
    },
  },
};
</script>

<style lang='scss' scoped>
.warp {
  .third_title {
    &::before {
      content: none;
    }
    span {
      padding-right: 16px;
      font-size: 20px;
      color: rgba(255, 255, 255, 0.5);
      transition: all 0.2s linear 0s;
      font-family: pangmen3;
      &.active {
        font-size: 24px;
        color: #ffffff;
      }
    }
  }
  height: 100%;
  .second_title2 {
    padding: 0 0 0 37px;
    margin-bottom: 10px;
    transition: all 0.3s linear;
    font-family: "hanyi";
    width: 100%;
    height: 40px;
    font-size: 30px;
    color: #fff;
    @include flexbox();
    @include flexJC(flex-start);
    @include flexAI(center);
    background: url($imageUrl + "second_title.png") no-repeat bottom;
    background-size: 100% auto;
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
  }
}

.top_box {
  width: 100%;
  height: 27%;
  .aqjd_box {
    width: 82%;
    height: 36px;
    @include flexbox();
    @include flexAI(center);
    @include flexJC(space-around);
    margin: 0px auto 6px;
    li.aqjd_item {
      user-select: none;
      margin: 0px 0px;
      width: 100%;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(flex-start);
      flex-direction: row;
      color: #000;
      padding: 5px 0 10px 0;
      &:nth-child(2) {
        margin-left: 32px;
      }
      img {
        width: 15%;
      }
      div {
        @include flexbox();
        @include flexAI(start);
        @include flexJC(flex-start);
        flex-direction: column;
        margin-left: 10px;
        p {
          color: #fff;
          font-size: 18px;
          font-weight: 600;
          padding-top: 5px;
          span {
            &.num {
              font-family: pangmen3;
              font-size: 32px;
            }
            &.unit {
              padding-left: 5px;
              font-size: 14px;
            }
          }
        }
      }
    }
  }
  .lenged {
    display: flex;
    flex-direction: column;
    width: 43%;
    position: absolute;
    top: 15%;
    right: 2%;
    color: #fff;
    li {
      user-select: none;
      -webkit-user-select: none;
      width: 100%;
      font-size: 16px;
      margin: 6px auto;
      @include flexbox();
      @include flexAC();
      @include flexJC(flex-start);
      span {
        font-size: 16px;
        width: 24px;
        height: 14px;
        margin-right: 5px;
        border-radius: 2px;
        &.one {
          background: #0c9800;
        }
        &.two {
          background: #0db200;
        }
        &.three {
          background: #12e800;
        }
        &.four {
          background: #93f28b;
        }
        &.five {
          background: #e6fbe4;
        }
        &.selected {
          width: 180px;
          margin-left: 5px;
        }
      }
    }
  }
  .htfbChart {
    width: 100%;
    height: calc(100% - 64px);
  }
}
.middle_box {
  width: 100%;
  height: 15%;
  // height: calc(20% - 24px);
  .control-one-list {
     width: 100%;
    display: flex;
    justify-content: space-around;
    flex-direction: row;
    .control-one-item {
      width: 120px;
      height: 96px;
      padding-left: 10px;
      box-sizing: border-box;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(center);
      // flex-direction: column;
      // cursor: not-allowed;
      img {
        width: 24px;
        height: 24px;
        margin-right: 8px;
      }
      > p {
        margin: 10px 0px;
        font-size: 22px;
        color: #fff;
      }
      .item-info {
        @include flexbox();
        @include flexAI(center);
        @include flexJC(center);
        flex-direction: column;
        & > div {
          margin-top: 4px;
        }
      }
      div {
        @include flexbox();
        @include flexAI(center);
        @include flexJC(center);
        p {
          font-size: 16px;
          color: #fff;
          span {
            &:nth-child(1) {
              font-family: pangmen3;
              font-size: 32px;
            }
            font-size: 20px;
            padding-right: 5px;

            line-height: 36px;
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
  .middle_box_list {
    padding-top: 0px;
    box-sizing: border-box;
    height: calc(100% - 40px);
    display: flex;
    align-items: center;
    margin-top: 10px;
    margin-left: 20px;
  }
}
.third_box {
  width: 100%;
  height: 23%;
  .htfbChart {
    width: 100%;
    height: calc(100% - 24px);
    margin-top: 0;
    // height: var (--barChartHeight);
  }
}
.fourth_box {
  margin: 16px 0;
  .tab_box {
    width: 96%;
    display: grid;
    grid-template-columns: 1fr 1fr 1fr;
    gap: 10px 4px;
    margin: 0px 0px 0px 30px;
    font-size: 26px;
    text-align: left;
    li {
      display: flex;
      flex-direction: column;
      @include flexAI(flex-start);
      @include flexJC(space-between);
      p.little_title {
        perspective: 138px;
        font-family: "pangmen3";
        width: 80%;
        text-align: center;
        color: rgb(20, 245, 241, 0.9);
        margin: 5px 0;
      }
      .tab_box_item {
        @include flexbox;
        width: 100%;
        @include flexAI(center);
      }
      div {
        margin: 5px 0px;
        span {
          font-size: 16px;
          color: rgba(230, 247, 255, 0.8);
          margin: 0 4px;
        }
        .ba_text {
          font-size: 16px;
        }
        .ba_num {
          font-family: "pangmen3";
          display: flex;
          font-size: 18px;
          flex: 0 1 0%;
          align-items: center;
          // margin: 0 6px;
          color: #fff;
        }
        .ba_unit {
          color: rgba(230, 247, 255, 0.8);
          font-size: 18px;
          display: inline-block;
          margin: 0 6px;
        }
      }
    }
  }
}
</style>