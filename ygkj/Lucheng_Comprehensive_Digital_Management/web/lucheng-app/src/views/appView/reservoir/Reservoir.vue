<!--
 * @Date: 2022-06-01 09:26:38
 * @Author: 
 * @Description: 进入{{type}}工程的首页
 * @LastEditors: dtb
 * @LastEditTime: 2022-06-24 16:54:31
 * @FilePath: \lucheng-app\src\views\appView\reservoir\Reservoir.vue
-->
<template>
  <div class="project-wrap">
    <van-nav-bar fixed placeholder safe-area-inset-top :title="$route.query.type" left-text="" :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
    <div class="select-box" ref="selectbox">
      <van-dropdown-menu z-index="2333" class="van-dropdown-menu" active-color="#276AF0">
        <van-dropdown-item get-container="body" v-if="scaleoptions.length > 0" title-class="title-class-name" v-model="scaleValue" :options="scaleoptions" @change="getProList" />
        <van-dropdown-item get-container="body" title-class="title-class-name" v-model="secureValue" :options="secureoptions" @change="getProList" />
      </van-dropdown-menu>
      <van-field class="van-field" placeholder="搜索" v-model="input" @change="getProList" left-icon="search" />
    </div>

    <keep-alive>
      <proj-map ref="projMap"> </proj-map>
    </keep-alive>
    <transition name="fade">
      <div class="project-detail project-detail-small" v-if="showModel == 'small'">
        <div class="bottm_info">
          <span v-if="tableData.length !== 1">{{type}}数量：{{tableData.length}}座</span>
          <span v-if="tableData.length === 1">当前{{type}}：{{tableData[0].name}}</span>
          <div class="bottm_icon">
            <i class="map_mode"></i>
            <i v-show="showModel != 'big'" class="up_flod" @click="showModel = 'big'"></i>
            <i v-show="showModel == 'big'" class="down_flod" @click="showModel = 'small';getProList()"></i>
          </div>
        </div>
      </div>
    </transition>
    <transition name="fade">
      <div class="project-detail" v-if="showModel == 'big'">
        <div class="bottm_info">
          <span v-if="tableData.length !== 1">{{type}}数量：{{tableData.length}}座</span>
          <span v-if="tableData.length === 1">当前{{type}}：{{tableData[0].name}}</span>
          <div class="bottm_icon">
            <i class="map_mode" @click="showModel = 'small';getProList()"></i>
            <i v-show="showModel != 'big'" class="up_flod" @click="showModel = 'big'"></i>
            <i v-show="showModel == 'big'" class="down_flod" @click="showModel = 'small';getProList()"></i>
          </div>
        </div>
        <div class="detail-line"></div>
        <div class="scroll-box">
          <transition :name="transitionName">
            <tableList :theadList="theadList" :list="tableData">
              <template v-slot:name='item'><span class="blue-color" @click="gotoDetail(item.data)">{{item.data.name}}</span></template>
            </tableList>
          </transition>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import ProjMap from "./ProjMap.vue";
import { homeIndex } from "@/api/projectDetail";
import tableList from "@/components/tableList.vue";
export default {
  name: 'Reservoir',
  props: {

  },
  components: {
    ProjMap,
    tableList
  },
  data() {
    return {
      menuIndex: 0,
      transitionName: "slide-right",
      skPointLayer: null,
      showModel: "small",
      scaleValue: "",
      secureValue: "",
      scaleoptions: [],
      secureoptions: [],
      input: '',
      tableData: [],
      type: '',//工程类型
      height: 80,
    };
  },
  computed: {
    routeDeep() {
      let routeDeep = [];
      for (var i = 0; i < this.routes.length; i++) {
        routeDeep.push(this.routes[i].path);
      }
      return routeDeep;
    },
    theadList() {
      let temp = []
      switch (this.type) {
        case '水库':
          temp = [
            {
              label: "工程名称",
              prop: "name",
              slot: true,
            },
            {
              label: "工程规模",
              prop: "engScal",
              slot: false,
            },
            {
              label: "库容",
              prop: "waterLevel",
              slot: false,
            },
            {
              label: "安全鉴定",
              prop: "safetyConclusion",
              slot: false,
            },
          ]
          break;
        case '水闸':
          temp = [
            {
              label: "工程名称",
              prop: "name",
              slot: true,
            },
            {
              label: "工程规模",
              prop: "engScal",
              slot: false,
            },
            {
              label: "最大过闸流量",
              prop: "maxFlow",
              slot: false,
            },
            // {
            //   label: "安全鉴定",
            //   prop: "safetyConclusion",
            //   slot: false,
            // },
          ]
          break;
        case '海塘':
          temp = [
            {
              label: "工程名称",
              prop: "name",
              slot: true,
            },
            {
              label: "海塘级别",
              prop: "seawallLevel",
              slot: false,
            },
            {
              label: "海塘长度(m)",
              prop: "seawallLength",
              slot: false,
            },
            {
              label: "防潮标准(年)",
              prop: "designTideStandard",
              slot: false,
            },
          ]
          break;
        case '堤防':
          temp = [
            {
              label: "工程名称",
              prop: "name",
              slot: true,
            },
            {
              label: "堤防规模",
              prop: "dikeGrad",
              slot: false,
            },
            {
              label: "堤防长度(m)",
              prop: "dikeLen",
              slot: false,
            },
            {
              label: "堤防类别",
              prop: "dikeType",
              slot: false,
            },
          ]
          break;
        default:
          temp = [
            {
              label: "工程名称",
              prop: "name",
              slot: true,
            },
            {
              label: "山塘类型",
              prop: "mnpdCate",
              slot: false,
            },
            {
              label: "总容积(万m³)",
              prop: "jhhsRj",
              slot: false,
            },
          ]
          break;
      }
      return temp
    }
  },
  watch: {
    $route(to, from) {
      const routeDeep = this.routeDeep;
      const toDepth = routeDeep.indexOf(to.path);
      const fromDepth = routeDeep.indexOf(from.path);
      this.transitionName = toDepth > fromDepth ? "slide-left" : "slide-right";
    },
    type: {
      handler: function (val) {
        switch (val) {
          case '堤防':
            this.scaleoptions = [
              { text: '全部', value: '' },
              { text: '1级堤防', value: '1级' },
              { text: '2级堤防', value: '2级' },
              { text: '3级堤防', value: '3级' },
              { text: '4级堤防', value: '4级' },
              { text: '5级堤防', value: '5级' }]
            this.secureoptions = [
              { text: '全部', value: '' },
              { text: '一类堤', value: '一类堤' },
              { text: '二类堤', value: '二类堤' },
              { text: '三类堤', value: '三类堤' },
              { text: '待鉴定', value: '待鉴定' }]
            break;
          case '水闸':
            this.scaleoptions = [
              { text: '全部', value: '' },
              { text: '大（1）型', value: 1 },
              { text: '大（2）型', value: 2 },
              { text: '中型', value: 3 },
              { text: '小（1）型', value: 4 },
              { text: '小（2）型', value: 5 }]
            this.secureoptions = [
              { text: '全部', value: '' },
              { text: '一类闸', value: '一类闸' },
              { text: '二类闸', value: '二类闸' },
              { text: '三类闸', value: '三类闸' },
              { text: '待鉴定', value: '待鉴定' }]
            break;
          case '海塘':
            this.scaleoptions = [
              { text: '全部', value: '' },
              { text: '1级海塘', value: '1级' },
              { text: '2级海塘', value: '2级' },
              { text: '3级海塘', value: '3级' },
              { text: '4级海塘', value: '4级' },
              { text: '5级海塘', value: '5级' }]
            this.secureoptions = [
              { text: '全部', value: '' },
              { text: '一类塘', value: '一类塘' },
              { text: '二类塘', value: '二类塘' },
              { text: '三类塘', value: '三类塘' },
              { text: '安澜建设', value: '安澜建设' },
              { text: '待鉴定', value: '待鉴定' }]
            break;
          case '山塘':
            this.scaleoptions = []
            this.secureoptions = [
              { text: '全部', value: '' },
              { text: '高坝山塘', value: '高坝山塘' },
              { text: '屋顶山塘', value: '屋顶山塘' },
              { text: '普通山塘', value: '普通山塘' }]
            break;
          default:
            this.scaleoptions = [
              { text: '全部', value: '' },
              { text: '大（1）型', value: 1 },
              { text: '大（2）型', value: 2 },
              { text: '中型', value: 3 },
              { text: '小（1）型', value: 4 },
              { text: '小（2）型', value: 5 }]
            this.secureoptions = [
              { text: '全部', value: '' },
              { text: '一类坝', value: '一类坝' },
              { text: '二类坝', value: '二类坝' },
              { text: '三类坝', value: '三类坝' },
              { text: '待鉴定', value: '待鉴定' }]
            break;
        }
      },
      immediate: true
    }
  },
  created() {

  },
  mounted() {
    this.addZwlog(this)
    this.type = this.$route.query.type
    this.getProList()
  },
  methods: {
    //获取工程点位列表
    async getProList() {
      let opt = {
        type: this.type,
        projectName: this.input,

      };
      switch (this.type) {
        case '堤防':
        case '海塘':
          opt.grade = this.scaleValue
          break;
        case '山塘':
          opt.mnpdCate = this.scaleValue
          break;
        default:
          opt.levels = this.scaleValue
          opt.safetyConclusion = this.secureValue
          opt.withWaterLevel = true
          break;
      }
      let res = await homeIndex.getProjectList(opt);
      if (res.code === 0 && res.data.length) {
        let configObj = {
          scale: 1.2,
        };
        if (res.data.length > 1) {
          switch (this.type) {
            case '堤防':
              configObj.src = require("@/assets/dike.png")
              break;
            case '水闸':
              configObj.src = require("@/assets/sluice.png")
              break;
            case '海塘':
              configObj.src = require("@/assets/seawall.png")
              break;
            case '山塘':
              configObj.src = require("@/assets/mountain.png")
              break;
            default:
              configObj.src = require("@/assets/reservoir.png")
              break;
          }
        }
        let data = []
        res.data.forEach((item) => {
          item.lat = item.lowLeftLat || item.stLat || item.latitude || item.startLat || item.dtMidLat
          item.lng = item.lowLeftLong || item.stLong || item.longitude || item.endLong || item.dtMidLong
          item.type = this.type;
          item.name = item.mnpdName || item.dikeName || item.wagaName || item.seawallName || item.resName
          item.code = item.mnpdCode || item.dikeCode || item.wagaCode || item.seawallCode || item.resCode
          data.push({
            point: [item.lng, item.lat],
            ...item
          })
        })
        this.skPointLayer = this.$refs.projMap._drawPoint(
          data,
          configObj,
          this.skPointLayer
        );
        this.tableData = res.data;
        if (res.data.length > 1) {
          this.$refs.projMap.setView(this.tableData[0].lng, this.tableData[0].lat, 10);
        } else {
          this.$refs.projMap.setView(this.tableData[0].lng, this.tableData[0].lat, 17);
        }
      } else {
        this.$toast("查询无数据");
      }
    },

    onClickRight() {
      // this.show = true;
    },
    /** 
     * @function: 
     * @description: 跳转到工程基础信息页面
     * @param {*} row 单击工程点位的详情
     * @return {*} 
     */
    gotoDetail(row) {
      this.$store.commit('SET_ENFINEERINGINFO', row)
      this.$store.commit('SET_PROJECTCODE', row.code)
      this.$localData("set", "engineeringInfo", row);
      this.$localData("set", "projectCode", row.code)
      this.$router.push("/reservoirDetail?code=" + row.code + '&name=' + row.name + '&type=' + this.type);
    },
  },
};
</script>

<style scoped lang="scss">
.fade-enter,
.fade-leave-to {
  transform: translateY(100%);
}
.fade-enter-active,
.fade-leave-active {
  transition: all 0.5s linear;
}
.project-wrap {
  position: relative;
  height: 100%;
  width: 100%;
  background: #fff;
  z-index: 1;
  .top_query {
    background: #fbfbfb;
    position: absolute;
    top: 0;
    left: 0;
    width: calc(100% - 40px);
    padding: 0px 20px;
    z-index: 2;
    ::v-deep .el-input__inner {
      border: none;
      padding-left: 0;
      border-radius: none;
      background: #fbfbfb;
      color: #000;
    }
    .el-input {
      background: #fbfbfb;
    }
  }
  .select-box {
    height: 43px;
    border-bottom: 1px solid #e6e6e6;
    display: flex;
    .van-dropdown-menu {
      flex: 1;
    }
    .van-field {
      width: 130px;
    }
  }
  .bottm_info {
    height: 80px;
    width: 100%;
    z-index: 2;
    box-sizing: border-box;
    padding: 0px 10px;
    @include flexbox();
    @include flexJC(space-between);
    @include flexAI(center);
    .bottm_icon {
      width: 15%;
      @include flexbox();
      @include flexJC(space-around);
      .map_mode {
        justify-self: end;
        width: 16px;
        height: 16px;
        background: url("../../../assets/map_mode.png") no-repeat center;
        background-size: 80%;
      }
      .up_flod {
        justify-self: end;
        width: 16px;
        height: 16px;
        background: url("../../../assets/up_flod.png") no-repeat center;
        background-size: 80%;
      }
      .down_flod {
        justify-self: end;
        width: 16px;
        height: 16px;
        background: url("../../../assets/down_flod.png") no-repeat center;
        background-size: 80%;
      }
    }
  }
  .project-detail {
    position: fixed;
    bottom: 0;
    width: 100%;
    height: calc(100% - 120px);
    background: #fff;
    box-shadow: 0 1px 10px 0 rgba(0, 0, 0, 0.15);
    &-small {
      height: 80px;
      .btn-box {
        @include flexbox();
        @include flexJC(space-between);
        padding-top: 16px;
        border-top: 1px solid #f2f2f2;
        width: calc(100% - 32px);
        margin: 0 16px;
        > div {
          width: 46%;
          height: 40px;
          line-height: 40px;
          text-align: center;
          border-radius: 4px;
          font-size: 16px;
          &.route-btn {
            background: #fbfbfb;
            border: 1px solid rgba(0, 0, 0, 0.16);
            color: #1e1e1f;
          }
          &.nav-btn {
            background: #036eff;
            border: 1px solid rgba(0, 0, 0, 0.16);
            color: #fff;
          }
        }
      }
    }
    .detail-line {
      background: #f3f6f9;
      width: 100%;
      height: 8px;
    }
    .scroll-box {
      height: calc(100% - 100px);
      overflow: auto;
      .page-view {
        height: 100%;
      }
    }
    .slide-right-enter-active,
    .slide-right-leave-active,
    .slide-left-enter-active,
    .slide-left-leave-active {
      will-change: transform;
      transition: all 1s;
      position: absolute;
    }
    .slide-right-enter {
      opacity: 0;
      transform: translateX(-100%);
    }
    .slide-right-leave-active {
      opacity: 0;
      transform: translateX(100%);
    }
    .slide-left-enter {
      opacity: 0;
      transform: translateX(100%);
    }
    .slide-left-leave-active {
      opacity: 0;
      transform: translateX(-100%);
    }
  }
}
</style>

<style>
.el-select {
  background: #fbfbfb;
  width: 25%;
}
</style>
