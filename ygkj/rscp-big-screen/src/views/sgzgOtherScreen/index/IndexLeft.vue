<template>
  <div ref="slide_wrap" class="slide_wrap">
    <section class="left_top">
      <p class="secondary_title" @click="typeToGo('zhMenu')"> 智慧水库 </p>
      <div class="item_wrap">
        <ul class="gs_list">
          <li class="gs_item" v-for="(item, index) in skItemList" :key="index">
            <img :src="item.src" alt="" />
            <div @click="getReservoirList">
              <h3>{{ item.name }}({{ item.unit }})</h3>
              <p>
                <span>{{ item.num }}</span>
              </p>
            </div>
          </li>
        </ul>
      </div>
      <p class="lines_title">
        <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
        <span class="active">规模</span>
        <i></i>
        <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" />
      </p>
      <div class="sk_bottom" :style="{height:height}">
        <div class="sk_item" v-for="item in skNumList" :key="item.name">
          <h3>{{item.name}}</h3>
          <p>
            {{ item.value || 0 }}<span>座</span>
          </p>
        </div>
      </div>
    </section>
    <section class="left_top">
      <p class="secondary_title" @click="typeToGo('zhSeawallScreen')"> 智慧海塘 </p>
      <div class="item_wrap">
        <ul class="gs_list">
          <li class="gs_item" v-for="(item, index) in htItemList" :key="index">
            <img :src="item.src" alt="" />
            <div @click="getSeawallList">
              <h3>{{ item.name }}({{ item.unit }})</h3>
              <p>
                <span>{{ item.num }}</span>
              </p>
            </div>
          </li>
        </ul>
      </div>
      <p class="lines_title">
        <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
        <span class="active">规模</span>
        <i></i>
        <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" />
      </p>
      <div class="sk_bottom" :style="{height:height}">
        <div class="sk_item" v-for="item in htNumList" :key="item.name">
          <h3>{{item.name}}</h3>
          <p>
            {{ item.value || 0 }}<span>条</span>
          </p>
        </div>
      </div>
    </section>
    <section class="left_top">
      <p class="secondary_title" @click="typeToGo('skzgDikeScreen')"> 智慧堤防 </p>
      <div class="item_wrap">
        <ul class="gs_list">
          <li class="gs_item" v-for="(item, index) in dfItemList" :key="index">
            <img :src="item.src" alt="" />
            <div @click="getDikeList">
              <h3>{{ item.name }}({{ item.unit }})</h3>
              <p>
                <span>{{ item.num }}</span>
              </p>
            </div>
          </li>
        </ul>
      </div>
      <p class="lines_title">
        <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
        <span class="active">规模</span>
        <i></i>
        <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" />
      </p>
      <div class="sk_bottom" :style="{height:height}">
        <div class="sk_item" v-for="item in dfNumList" :key="item.name">
          <h3>{{item.name}}</h3>
          <p>
            {{ item.value || 0 }}<span>条</span>
          </p>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { generateProject, reservoirLeftStatisticApi } from "@/api/api_reservoir";
import { seawallLeftStatisticApi } from '@/api/api_seawall'
import { basinStatisticApi, resProstatistical } from "@/api/api_basin";

import { dikeStatisticApi } from "@/api/api_dike";
export default {
  name: 'IndexLeft',
  props: {

  },
  components: {

  },
  data() {
    return {
      skItemList: [
        {
          name: "数量",
          num: 4277,
          unit: "座",
          src: this.$config.swImgUrl + "skzsl.png",
        },
        {
          name: "总库容",
          num: 2081,
          unit: "亿方",
          src: this.$config.swImgUrl + "skzkr.png",
        },
      ],
      htItemList: [
        {
          name: "数量",
          num: 0,
          unit: "条",
          src: this.$config.swImgUrl + "htsl.png",
        },
        {
          name: "总长度",
          num: 0,
          unit: "km",
          src: this.$config.swImgUrl + "htcd.png",
        },
      ],
      dfItemList: [
        {
          name: "数量",
          num: 0,
          unit: "条",
          src: this.$config.swImgUrl + "dfsl.png",
        },
        {
          name: "总长度",
          num: 0,
          unit: "km",
          src: this.$config.swImgUrl + "dfcd.png",
        },
      ],

      skNumList: [
        {
          name: "大(1)型",
          value: 5,
        },
        {
          name: "大(2)型",
          value: 29,
        },
        {
          name: "中型水库",
          value: 159,
        },
        {
          name: "小(1)型",
          value: 728,
        },
        {
          name: "小(2)型",
          value: 3356,
        },
      ],
      htNumList: [
        {
          name: "1级",
          value: "1",
        },
        {
          name: "2级",
          value: "1",
        },
        {
          name: "3级",
          value: "1",
        },
        {
          name: "4级",
          value: "1",
        },
        {
          name: "5级",
          value: "1",
        },
      ],
      dfNumList: [
        {
          name: "1级",
          value: "1",
        },
        {
          name: "2级",
          value: "1",
        },
        {
          name: "3级",
          value: "1",
        },
        {
          name: "4级",
          value: "1",
        },
        {
          name: "5级",
          value: "1",
        },
      ],
      phone: this.$localData("get", "userInfo").phone,
      tabDataLeft: [
        {
          name: '水库',
          path: '/skzgScreen',
        },
        {
          name: '海塘',
          path: '/skzgSeawallScreen',
        },
        {
          name: '堤防工程',
          path: '/skzgDikeScreen',
        },
      ],
      height: '120px',
      superviseLevel: '',
      dikeLength: 10,
    };
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    sgzgSelectAdcd() {
      return this.$store.state.sgzgSelectAdcd;
    },
  },
  watch: {
    leftSelectAdcd(val) {
      // this.opt.adcd = val ? val : this.$localData("get", "userInfo").adcd;
      if (val == "33") {
        // this.opt.superviseLevel = 1;
        this.superviseLevel = 1;
      } else {
        // this.opt.superviseLevel = "";
        this.superviseLevel = "";
      }
      // this.adcd = val;
    },
  },
  created() {

  },
  mounted() {
    if (window.performance.navigation.type === 1) this.height = this.$refs.slide_wrap.offsetHeight < 900 ? '115px' : '165px';
    window.addEventListener('resize', this.handleWindowResize);
    this.getSkStatistic()
    this.getReservoirList();
    this.getHtStatistic()
    this.getDfStatistic()
    this.getDikeLength()
    this.getProStatistical()

  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleWindowResize);
  },
  methods: {
    handleWindowResize() {
      this.height = this.$refs.slide_wrap.offsetHeight < 900 ? '115px' : '165px'
    },
    typeToGo(path) {
      window.open('/screen/' + path)
      // this.$router.push('/' + path)
    },
    // 水库级别统计
    async getSkStatistic() {
      this.squareList = [];
      let res = await reservoirLeftStatisticApi({
        statistic: "scale",
        adcd: this.sgzgSelectAdcd ? this.sgzgSelectAdcd : this.$localData("get", "userInfo").adcd,
        superviseLevel: this.superviseLevel,
      });
      if (res.success) {
        let arr = [];
        for (let i = 0; i < res.data.length; i++) {
          let obj = {};
          obj.value = res.data[i].num
          switch (res.data[i].project_scale) {
            case "1":
              obj.name = "大(1)型";
              break;
            case "2":
              obj.name = "大(2)型";
              break;
            case "3":
              obj.name = "中型";
              break;
            case "4":
              obj.name = "小(1)型";
              break;
            case "5":
              obj.name = "小(2)型";
              break;
          }
          arr.push(obj);
        }
        this.skNumList = arr;

      }
      this.loading_square = false;
    },
    // 水库落点
    async getReservoirList() {
      let opt = {
        adcd: this.sgzgSelectAdcd ? this.sgzgSelectAdcd : this.$localData("get", "userInfo").adcd,
        gpType: 1,
        projectScale: '1,2,3,4',
        screen: '17,18,19',
      }
      this.$parent.$refs.skzgMap.removeAllLayer()
      let res = await generateProject(opt);
      if (res.success) {
        if (res.data.length) {
          res.data.map((v) => {
            v.lat = v.LTTD;
            v.lng = v.LGTD;
            v.styleObj = {
              scale: .5,
              src: this.$config.swImgUrl + "sk_jh.png"
            }
          });
        }
        this.$parent.showMapPointsJh(res.data, 'PointLayer', 'sk');
      }
    },

    // 海塘级别统计
    async getHtStatistic() {
      let res = await seawallLeftStatisticApi({
        statistic: 'scale',
        adcd: this.$localData('get', 'userInfo').adcd
      })
      if (res.success && res.data.length) {
        let totalNum = 0
        for (let i = 0; i < res.data.length; i++) {
          totalNum += res.data[i].num
          if (res.data[i].project_scale != 6) {
            switch (res.data[i].project_scale) {
              case '1':
                this.htNumList[0].value = res.data[i].num
                break;
              case '2':
                this.htNumList[1].value = res.data[i].num
                break;
              case '3':
                this.htNumList[2].value = res.data[i].num
                break;
              case '4':
                this.htNumList[3].value = res.data[i].num
                break;
              default:
                this.htNumList[4].value = res.data[i].num
                break;
            }
          }
        }
        this.htItemList[0].num = totalNum
      } else {
        this.$message.error(res.msg ? res.msg : '请求失败')
      }
      this.htjbtjLoading = false
    },
    // 施工海棠分布列表
    async getSeawallList() {
      this.$parent.$refs.skzgMap.removeAllLayer();
      let opt = {
        statistic: 'mapList',
        superviseLevel: this.superviseLevel,
        adcd: this.$localData('get', 'userInfo').adcd
      }
      let res = await seawallLeftStatisticApi(opt)
      if (res.success) {
        if (res.data.length) {
          let pointArr = []
          res.data.map(v => {
            pointArr.push({
              lat: v.LTTD,
              lng: v.LGTD,
              NAME: v.NAME,
              PRCD: v.PRCD,
              rightType: '2',
              tm: v.tm,
              tide: v.tide,
              DESIGN_TIDE_LEVEL: v.DESIGN_TIDE_LEVEL,
              src: require("@/assets/images/map/seawall.png")
            })
          });
          this.$parent.showMapPointsJh(pointArr, 'PointLayer', 'ht')
        }
      }
    },

    // 工程规模
    async getDfStatistic() {
      let res = await basinStatisticApi({
        statistic: "typeScale",
        adcd: this.$localData('get', 'userInfo').adcd,
        projectCategory: '堤防',
        itemCategory: "safeStatus",
        itemValue: 'totalCnt',
        superviseLevel: this.superviseLevel
      })
      if (res.success && res.data.length) {
        let totalNum = 0
        for (let i = 0; i < res.data.length; i++) {
          totalNum += res.data[i].num
          switch (Number(res.data[i].projectScale)) {
            case 1:
              this.dfNumList[0].value = res.data[i].num
              break;
            case 2:
              this.dfNumList[1].value = res.data[i].num
              break;
            case 3:
              this.dfNumList[2].value = res.data[i].num
              break;
            case 4:
              this.dfNumList[3].value = res.data[i].num
              break;
            case 5:
              this.dfNumList[4].value = res.data[i].num
              break;
          }
        }
        this.dfItemList[0].num = totalNum
      }
    },

    // 列表
    async getDikeList() {
      this.$parent.$refs.skzgMap.removeAllLayer();
      let opt = {
        statistic: "mapList",
        itemCategory: "",
        itemValue: "",
        basin: "",
        projectCategory: '堤防',
        adcd: this.$localData("get", "userInfo").adcd,
        superviseLevel: this.superviseLevel,
      }
      let res = await basinStatisticApi(opt);
      if (res.success && res.data.length) {
        let pointArr = res.data.map((v) => {
          return {
            lat: v.LTTD,
            lng: v.LGTD,
            rightType: "2",
            ...v
          }
        });
        this.$parent.showMapPointsJh(pointArr, 'PointLayer', 'df');
      }
    },
    async getDikeLength() {
      let res = await dikeStatisticApi({
        adcd: this.$localData("get", "userInfo").adcd,
        statistic: "length",
        superviseLevel: this.superviseLevel
      })
      if (res.success && res.data.length) {
        let num = 0
        for (let i = 0; i < res.data.length; i++) {
          num += res.data[i].DRAW_LENGTH
        }
        this.dfItemList[1].num = (num / 1000).toFixed(0)
      }
    },
    // 获取 库容 流量 长度等统计数据
    async getProStatistical() {
      let res = await resProstatistical({
        adcd: this.$localData("get", "userInfo").adcd,
      })
      if (res.success && res.rows.length) {

        this.skItemList[1].num = res.rows[0].resTcp.toFixed(0)
        this.htItemList[1].num = res.rows[0].seawallLen.toFixed(0)
        this.dfItemList[1].num = res.rows[0].dikeLen.toFixed(0)
      }
    },
  },
};
</script>

<style scoped lang="scss">
.slide_wrap {
  width: 100%;
  height: 100%;
  .secondary_title {
    padding: 0 0 0 37px;
    margin-bottom: 3%;
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
  }
  .lines_title {
    @include flexbox();
    @include flexAI(center);
    ::before {
      content: none;
    }
    font-weight: bold;
    color: #fff;
    margin-bottom: 4px;
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
  .item_wrap {
    height: calc(50% - 50px);
    margin-bottom: 3%;
    .gs_list {
      @include flexbox();
      @include flexAI(center);
      @include flexflow(row wrap);
      width: 100%;
      height: calc(100% - 50px);
      box-sizing: border-box;
      // padding-left: 16px;
      .gs_item {
        width: 48%;
        height: 50%;
        @include flexbox();
        @include flexAI(center);
        box-sizing: border-box;

        &.pl {
          width: 52%;
        }
        img {
          width: 90px;
          height: 60px;
        }
        &:nth-child(1) {
          cursor: pointer;
        }
        &:nth-child(2n + 1) {
          div {
            padding: 5px;
          }
        }
        div {
          h3 {
            font-size: 16px;
            padding-top: 4px;
            padding-bottom: 8px;
            color: rgba(230, 247, 255, 0.8);
          }
          p {
            color: rgba(230, 247, 255, 0.8);
            font-size: 16px;
            span {
              vertical-align: text-bottom;
              font-family: youshe;
              color: #fff;
              text-shadow: 0 0 10px rgba(255, 255, 255, 0.3),
                0 0 20px rgba(255, 255, 255, 0.3),
                0 0 30px rgba(0, 232, 255, 0.4), 0 0 40px rgba(0, 232, 255, 0.4);
              font-size: 28px;
              margin-right: 4px;
            }
          }
        }
      }
    }
  }
  .sk_bottom {
    @include flexbox();
    @include flexAI(center);
    @include flexJC(flex-start);
    @include flexflow(row wrap);
    padding: 0 0 0 20px;
    height: 120px;
    .sk_item {
      width: 33%;
      // @include flexbox();
      // @include flexAI(center);
      // @include flexJC(center);
      // // flex-direction: column;
      padding: 10px 0px 10px 0px;
      position: relative;
      &:nth-child(1)::after,
      &:nth-child(2)::after,
      &:nth-child(4)::after {
        content: "";
        width: 2px;
        height: 36px;
        position: absolute;
        top: 20px;
        left: 100px;
        background: url($swImgUrl + "splitLine.png") no-repeat center;
        background-size: 100% auto;
      }
      h3 {
        font-size: 18px;
      }
      p {
        font-size: 22px;
        font-family: "manrope";
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
</style>
