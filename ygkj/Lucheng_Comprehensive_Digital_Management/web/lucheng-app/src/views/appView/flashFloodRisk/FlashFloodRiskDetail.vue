<template>
  <div class="wrap-page">
    <van-nav-bar fixed placeholder safe-area-inset-top :title="projectDetail.nvi"  :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
    <mapViewPoint v-if="point.length" :point="point" />
    <!--此处样式已被提取成公共样式 在style/util.css  start-->
    <div class="detail-page-title">
      <div class="page-title">
        <div class="title-text">{{ projectDetail.nvi }}</div>
        <div class="tip-text">影响人口：{{ projectDetail.tpo }}人</div>
      </div>
      <!-- <div class="detail-page">预报高潮位：<span>6.4m</span></div> -->
      <div class="detail-people">
        <span>{{ projectDetail.chargerName }}</span
        ><span>{{ projectDetail.chargerPhone }}</span>
      </div>
    </div>
    <!------end------->
    <div class="page-detail-box">
      <wrapTitle title="负责人清单" />
      <div class="person_list" v-if="personList.length > 0">
        <div
          class="person_list_item"
          v-for="(item, index) in personList"
          :key="index"
        >
          <div class="person_left">
            <img src="../../../assets/images/person-head.png" alt="" />
            <div>
              <h3>{{ item.chargeType }}</h3>
              <p>
                {{ item.pchargeName }}<span>{{ item.position }}</span>
              </p>
            </div>
          </div>
          <div class="person_right">
            <img src="../../../assets/phome.png" alt="" />
            <span>{{ item.mobile }}</span>
          </div>
        </div>
      </div>
      <noData v-else />
      <div>
        <wrapTitle title="影响人员清单" />
        <div>
          <tableList :theadList="theadList" :list="tableData"> </tableList>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { flashFloodRisk } from '@/api/projectDetail'
import mapViewPoint from '@/components/mapViewPoint.vue'
import wrapTitle from '@/components/wrapTitle.vue'
import EchartsLine from '@/components/EchartsLine.vue'
import tableList from '@/components/tableList.vue'
import noData from '@/components/noData.vue'
export default {
  components: {
    mapViewPoint,
    wrapTitle,
    EchartsLine,
    tableList,
    noData
  },
  computed: {
    // routes() {
    //   var routes = {
    //     children: this.$router.options.routes,
    //   };
    //   var route = this.$route.matched;
    //   for (var i = 0; i < route.length - 1; i++) {
    //     routes = routes.children.find((e) => e.name == route[i].name);
    //   }
    //   if (this.type != '水闸') {
    //     console.log('不等于水闸');
    //     routes.children = routes.children.slice(0, 2)
    //   }
    //   return routes.children;
    // },
    // routeDeep() {
    //   let routeDeep = [];
    //   for (var i = 0; i < this.routes.length; i++) {
    //     routeDeep.push(this.routes[i].path);
    //   }
    //   return routeDeep;
    // },
    // type() {
    //   return this.$localData('get', 'engineeringInfo').type;
    // },
    projectDetail() {
      return this.$localData('get', 'flashFloodRiskInfo')
    },
    // tpo() {
    //   return this.$localData('get', 'flashFloodRiskInfo').tpo
    // },
    // naturalAdnm() {
    //   return this.$localData('get', 'flashFloodRiskInfo').nvi
    // },
    // baseId() {
    //   return this.$localData('get', 'flashFloodRiskInfo').id
    // },
    // chargerName() {
    //   return this.$localData('get', 'flashFloodRiskInfo').chargerName
    // },
    // chargerPhone() {
    //   return this.$localData('get', 'flashFloodRiskInfo').chargerPhone
    // },
  },
  watch: {
    // $route(to, from) {
    //   const routeDeep = this.routeDeep;
    //   const toDepth = routeDeep.indexOf(to.path);
    //   const fromDepth = routeDeep.indexOf(from.path);
    //   this.transitionName = toDepth > fromDepth ? "slide-left" : "slide-right";
    // },
  },
  data() {
    return {
      defaultIndex: 0,
      personList: [
        // {
        //   id: null,
        //   username: '张崇波',
        //   phone: '13780197779',
        //   job: '区长',
        //   deptName: null,
        //   type: '政府负责人',
        // },
        // {
        //   id: null,
        //   username: '方学优',
        //   phone: '13857730689',
        //   job: '局长',
        //   deptName: null,
        //   type: '水行政部门负责人',
        // },
        // {
        //   id: null,
        //   username: '方学优',
        //   phone: '13857730689',
        //   job: '局长',
        //   deptName: null,
        //   type: '主管部门负责人',
        // },
        // {
        //   id: null,
        //   username: '张瞻阳',
        //   phone: '13957704117',
        //   job: '所长',
        //   deptName: null,
        //   type: '管理单位责任人',
        // },
        // {
        //   id: null,
        //   username: '夏琼',
        //   phone: '13868885281',
        //   job: '技术负责人',
        //   deptName: null,
        //   type: '技术责任人',
        // },
        // {
        //   id: null,
        //   username: '施胜颂',
        //   phone: '13868885281',
        //   job: '技术负责人',
        //   deptName: null,
        //   type: '巡查责任人',
        // },
      ],
      theadList: [
        {
          label: '姓名',
          prop: 'contact',
          slot: false,
        },
        {
          label: '联系电话',
          prop: 'contacttel',
          slot: false,
        },
      ],
      tableData: [
        {
          name: 'XXX',
          tel: 'XXX',
        },
        {
          name: 'XXX',
          tel: 'XXX',
        },
        {
          name: 'XXX',
          tel: 'XXX',
        },
        {
          name: 'XXX',
          tel: 'XXX',
        },
        {
          name: 'XXX',
          tel: 'XXX',
        },
        {
          name: 'XXX',
          tel: 'XXX',
        },
        {
          name: 'XXX',
          tel: 'XXX',
        },
        {
          name: 'XXX',
          tel: 'XXX',
        },
        {
          name: 'XXX',
          tel: 'XXX',
        },
      ],
      point: [],
      finished: false,
    }
  },
  mounted() {
    this.addZwlog(this)
    this.responsible()
    this.affectPopulationList()
    if (this.projectDetail.lng && this.projectDetail.lat) {
      this.point = [{point: [this.projectDetail.lng, this.projectDetail.lat]}]
    }
  },
  methods: {
    // onLoad() {
    //   setTimeout(() => {
    //     for (let i = 0; i < 10; i++) {
    //       this.list.push(this.list[0])
    //     }
    //     // 数据全部加载完成
    //     if (this.list.length >= 40) {
    //       this.finished = true
    //     }
    //   }, 1000)
    // },
    // 负责人列表
    async responsible() {
      let res = await flashFloodRisk.pvPchargeList({
        naturalAdnm: this.projectDetail.nvi,
      })
    //   console.log(res)
      if (res.code == 0) {
        // res.data.forEach((element, index) => {
        //   if (index < 3) {
        //     element.phone = element.phone.substring(0, 3) + '****' + element.phone.substring(7)
        //   }
        // });
        this.personList = res.data.list
        // console.log(this.personList)
      }
    },
    // 影响人员列表
    async affectPopulationList() {
      let res = await flashFloodRisk.affectPopulationList({
        baseId: this.projectDetail.id,
        chargerName: this.projectDetail.chargerName,
        chargerPhone: this.projectDetail.chargerPhone,
      })
    //   console.log(res)
      if (res.code == 0) {
        // res.data.forEach((element, index) => {
        //   if (index < 3) {
        //     element.phone = element.phone.substring(0, 3) + '****' + element.phone.substring(7)
        //   }
        // });
        this.tableData = res.data.list
        // console.log(res.data.list)
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.page-detail-box {
  margin-top: 6px;
  background-color: #fff;
  .picker-box {
    padding: 0 8px;
    > div {
      border-top: 1px solid #e9f0fd;
      padding: 17px 8px;
      span {
        color: #666666;
        font-size: 12px;
        &:first-child {
          padding-right: 10px;
        }
        &:last-child {
          font-size: 14px;
          color: #aaaaaa;
        }
      }
    }
  }
  .van-grid-box {
    display: flex;
    border-top: 1px solid #e6e6e6;
    border-bottom: 1px solid #e6e6e6;
    .van-grid-item {
      flex: 1;
      padding: 11px 0;
      text-align: center;
      border-right: 1px solid #e6e6e6;
      &:last-child {
        border-right: none;
      }
      > div:first-child {
        font-size: 12px;
        color: #666666;
        font-weight: 600;
      }
      > div:last-child {
        padding-top: 5px;
        > span {
          &:first-child {
            font-size: 16px;
            font-weight: 500;
            line-height: 25px;
          }
          &:last-child {
            font-size: 10px;
          }
        }
      }
    }
  }
  .echarts-box {
    padding-top: 13px;
    .echarts-bar-box {
      height: 180px;
      width: 100%;
      padding: 0 12px;
      box-sizing: border-box;
    }
  }
}
.detail-people {
  margin-top: 14px;
  span {
    color: #666666;
    font-size: 12px;
    &:first-child {
      padding-right: 10px;
      border-right: 1px solid #eee;
    }
    &:last-child {
      padding-left: 10px;
      font-size: 14px;
      color: #aaaaaa;
    }
  }
}
.tip-text {
  color: #036eff !important;
}
.person_list {
  padding: 0 12px;
  box-sizing: border-box;
  .person_list_item {
    @include flexbox();
    @include flexAI(center);
    @include flexJC(space-between);
    padding: 16px 0;
    border-bottom: 1px solid #e5e5e5;
    .person_left {
      @include flexbox();
      @include flexAI(center);
      @include flexJC(center);
      img {
        margin-right: 16px;
      }
      & > div {
        h3 {
          font-size: 14px;
          color: rgba(31, 31, 31, 0.48);
          font-weight: 400;
          margin-bottom: 12px;
        }
        p {
          font-size: 18px;
          color: #1f1f1f;
          font-weight: 500;
          span {
            font-size: 14px;
            font-weight: 400;
            margin-left: 8px;
          }
        }
      }
    }
    .person_right {
      @include flexbox();
      @include flexAI(center);
      @include flexJC(flex-end);
      img {
        width: 20px;
      }
      font-size: 14px;
      color: #1f1f1f;
      font-weight: 400;
    }
  }
}
</style>