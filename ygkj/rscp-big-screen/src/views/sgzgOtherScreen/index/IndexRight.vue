<template>
  <div ref="slide_wrap" class="slide_wrap">
    <section class="left_top">
      <p class="secondary_title" @click="typeToGo('skzgSluiceScreen')"> 智慧水闸 </p>
      <div class="item_wrap">
        <ul class="gs_list">
          <li class="gs_item" v-for="(item, index) in szItemList" :key="index">
            <img :src="item.src" alt="" />
            <div @click="getList('水闸')">
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
        <div class="sk_item" v-for="item in szNumList" :key="item.name">
          <h3>{{item.name}}</h3>
          <p>
            {{ item.value || 0 }}<span>座</span>
          </p>
        </div>
      </div>
    </section>
    <section class="left_top">
      <p class="secondary_title" @click="typeToGo('skzgPumpScreen')"> 智慧泵站 </p>
      <div class="item_wrap">
        <ul class="gs_list">
          <li class="gs_item" v-for="(item, index) in bzItemList" :key="index">
            <img :src="item.src" alt="" />
            <div @click="getList('泵站')">
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
        <div class="sk_item" v-for="item in bzNumList" :key="item.name">
          <h3>{{item.name}}</h3>
          <p>
            {{ item.value || 0 }}<span>座</span>
          </p>
        </div>
      </div>
    </section>
    <section class="left_top">
      <p class="secondary_title" @click="typeToGo('skzgGateScreen')"> 智慧闸站 </p>
      <div class="item_wrap">
        <ul class="gs_list">
          <li class="gs_item" @click="getList('闸站')" v-for="(item, index) in zzItemList" :key="index">
            <img :src="item.src" alt="" />
            <div>
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
        <div class="sk_item" v-for="item in zzNumList" :key="item.name">
          <h3>{{item.name}}</h3>
          <p>
            {{ item.value || 0 }}<span>座</span>
          </p>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { basinStatisticApi, resProstatistical } from "@/api/api_basin";
export default {
  name: 'IndexLeft',
  props: {

  },
  components: {

  },
  data() {
    return {
      szItemList: [
        {
          name: "数量",
          num: 3318,
          unit: "座",
          src: this.$config.swImgUrl + "szsl.png",
        },
        {
          name: "总最大过闸流量",
          num: 2018,
          unit: "m³/s",
          src: this.$config.swImgUrl + "zsjll.png",
        },
      ],
      bzItemList: [
        {
          name: "数量",
          num: 766,
          unit: "座",
          src: this.$config.swImgUrl + "bzsl.png",
        },
        {
          name: "总装机流量",
          num: 2081,
          unit: "m³/s",
          src: this.$config.swImgUrl + "zsjll.png",
        },
      ],
      zzItemList: [
        {
          name: "数量",
          num: 1703,
          unit: "座",
          src: this.$config.swImgUrl + "zzsl.png",
        },
        {
          name: "总过闸流量",
          num: 2081,
          unit: "m³/s",
          src: this.$config.swImgUrl + "zsjll.png",
        },
      ],

      szNumList: [
        {
          name: "大(1)型",
          value: "1",
        },
        {
          name: "大(2)型",
          value: "1",
        },
        {
          name: "中型水库",
          value: "1",
        },
        {
          name: "小(1)型",
          value: "1",
        },
        {
          name: "小(2)型",
          value: "1",
        },
      ],
      bzNumList: [
        {
          name: "大(1)型",
          value: "1",
        },
        {
          name: "大(2)型",
          value: "1",
        },
        {
          name: "中型水库",
          value: "1",
        },
        {
          name: "小(1)型",
          value: "1",
        },
        {
          name: "小(2)型",
          value: "1",
        },
      ],
      zzNumList: [
        {
          name: "大(1)型",
          value: "1",
        },
        {
          name: "大(2)型",
          value: "1",
        },
        {
          name: "中型水库",
          value: "1",
        },
        {
          name: "小(1)型",
          value: "1",
        },
        {
          name: "小(2)型",
          value: "1",
        },
      ],
      tabDataRight: [
        {
          name: '水闸工程',
          path: '/skzgSluiceScreen',
        },
        {
          name: '泵站工程',
          path: '/skzgPumpScreen',
        },
        {
          name: '闸站工程',
          path: '/skzgGateScreen',
        }
      ],
      height: '120px',
      superviseLevel: '',
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
    if (window.performance.navigation.type == 1) this.height = this.$refs.slide_wrap.offsetHeight < 900 ? '115px' : '165px';
    window.addEventListener('resize', this.handleWindowResize);
    this.getStatisticData('水闸')
    this.getStatisticData('泵站')
    this.getStatisticData('闸站')
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
      window.open('/screen?type=' + path)
    },
    async getStatisticData(type) {
      this.scaleLoading = true;
      let res = await basinStatisticApi({
        statistic: "typeScale",
        adcd: this.$localData("get", "userInfo").adcd,
        projectCategory: type,
        superviseLevel: this.superviseLevel
      })
      if (res.success & res.data.length) {
        let totalNum = 0
        for (let i = 0; i < res.data.length; i++) {
          totalNum += res.data[i].num
          if (type === '水闸') {
            switch (Number(res.data[i].projectScale)) {
              case 1:
                this.szNumList[0].value = res.data[i].num
                break;
              case 2:
                this.szNumList[1].value = res.data[i].num
                break;
              case 3:
                this.szNumList[2].value = res.data[i].num
                break;
              case 4:
                this.szNumList[3].value = res.data[i].num
                break;
              case 5:
                this.szNumList[4].value = res.data[i].num
                break;
            }
          } else if (type === '泵站') {
            switch (Number(res.data[i].projectScale)) {
              case 1:
                this.bzNumList[0].value = res.data[i].num
                break;
              case 2:
                this.bzNumList[1].value = res.data[i].num
                break;
              case 3:
                this.bzNumList[2].value = res.data[i].num
                break;
              case 4:
                this.bzNumList[3].value = res.data[i].num
                break;
              case 5:
                this.bzNumList[4].value = res.data[i].num
                break;
            }
          } else {
            switch (Number(res.data[i].projectScale)) {
              case 1:
                this.zzNumList[0].value = res.data[i].num
                break;
              case 2:
                this.zzNumList[1].value = res.data[i].num
                break;
              case 3:
                this.zzNumList[2].value = res.data[i].num
                break;
              case 4:
                this.zzNumList[3].value = res.data[i].num
                break;
              case 5:
                this.zzNumList[4].value = res.data[i].num
                break;
            }
          }
        }
        switch (type) {
          case '水闸':
            this.szItemList[0].num = totalNum
            break;
          case '泵站':
            this.bzItemList[0].num = totalNum
            break;
          default:
            this.zzItemList[0].num = totalNum
            break;
        }

      }
    },
    // 列表
    async getList(type) {
      this.$parent.$refs.skzgMap.removeAllLayer();
      let opt = {
        statistic: "mapList",
        itemCategory: "",
        itemValue: "",
        basin: "",
        projectCategory: type,
        adcd: this.$localData("get", "userInfo").adcd,
        superviseLevel: this.superviseLevel,
      }
      let res = await basinStatisticApi(opt);
      if (res.success && res.data.length) {
        let pointArr = res.data.map((v) => {
          return {
            lat: +v.LTTD,
            lng: +v.LGTD,
            rightType: "2",
            ...v
          }
        });
        this.$parent.showMapPointsJh(pointArr, 'PointLayer', type === '水闸' ? 'sz' : type === '泵站' ? 'bz' : 'zz');
      }
    },
    // 获取 库容 流量 长度等统计数据
    async getProStatistical() {
      let res = await resProstatistical({
        adcd: this.$localData("get", "userInfo").adcd,
      })
      if (res.success && res.rows.length) {
        this.szItemList[1].num = res.rows[0].sluiceFlow.toFixed(0)
        this.bzItemList[1].num = res.rows[0].pumpFlow.toFixed(0)
        this.zzItemList[1].num = res.rows[0].gaFlow.toFixed(0)
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
    margin-bottom: 2%;
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
        width: 50%;
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
            // color: rgb(15, 146, 255);
            color: rgba(230, 247, 255, 0.8);
            font-size: 16px;
            span {
              vertical-align: text-bottom;
              font-family: youshe;
              color: #fff;
              font-size: 28px;
              margin-right: 4px;
              text-shadow: 0 0 10px rgba(255, 255, 255, 0.3),
                0 0 20px rgba(255, 255, 255, 0.3),
                0 0 30px rgba(0, 232, 255, 0.4), 0 0 40px rgba(0, 232, 255, 0.4);
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
    padding: 5px 0 0 20px;
    height: 120px;
    .sk_item {
      width: 33%;
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
