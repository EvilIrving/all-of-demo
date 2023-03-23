<!--
 * @Author: wanxin
 * @LastEditTime: 2022-06-22 16:26:16
 * @Descripttion: 
 * @FilePath: \lucheng-app\src\views\appView\flashFloodRisk\FlashFloodRisk.vue
-->
<template>
  <div class="tidalLevelForecast_box">
    <van-nav-bar fixed placeholder safe-area-inset-top :title="title"  :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
    <div class="search_box">
      <van-dropdown-menu>
        <van-dropdown-item
          v-model="isAlarm"
          :options="option"
          @change="getList()"
        />
      </van-dropdown-menu>
      <van-search
        v-model="naturalAdnm"
        placeholder="请输入搜索关键词"
        @change="getList()"
        @clear="getList()"
      />
    </div>
    <div class="map_box">
      <MainMap ref="mainMap" />
      <MapLegend />
    </div>
    <!-- <div class="list_box">
      <div @click="open">1111</div>
      <transition name="van-slide-up">
        <div v-show="visible">
          <VantTable :option="tableOption" :tableData="tableData" />
        </div>
      </transition>
    </div> -->
    <transition name="fade">
      <div
        class="project-detail project-detail-small"
        v-if="showModel == 'small'"
      >
        <div class="bottm_info">
          <span v-if="tableData.length !== 1"
            >村落数量：{{ tableData.length }}座</span
          >
          <span v-if="tableData.length === 1"
            >当前村落：{{ tableData[0].nvi }}</span
          >
          <div class="bottm_icon">
            <i class="map_mode"></i>
            <i
              v-show="showModel != 'big'"
              class="up_flod"
              @click="showModel = 'big'"
            ></i>
            <i
              v-show="showModel == 'big'"
              class="down_flod"
              @click="
                showModel = 'small'
                getList()
              "
            ></i>
          </div>
        </div>
      </div>
    </transition>
    <transition name="fade">
      <div class="project-detail" v-if="showModel == 'big'">
        <div class="bottm_info">
          <span v-if="tableData.length !== 1"
            >村落数量：{{ tableData.length }}座</span
          >
          <span v-if="tableData.length === 1"
            >当前村落：{{ tableData[0].nvi }}</span
          >
          <div class="bottm_icon">
            <i
              class="map_mode"
              @click="
                showModel = 'small'
                getList()
              "
            ></i>
            <i
              v-show="showModel != 'big'"
              class="up_flod"
              @click="showModel = 'big'"
            ></i>
            <i
              v-show="showModel == 'big'"
              class="down_flod"
              @click="
                showModel = 'small'
                getList()
              "
            ></i>
          </div>
        </div>
        <div class="detail-line"></div>
        <div class="scroll-box">
          <transition :name="transitionName">
            <!-- :finished="finished"
              @onLoad="onLoad" -->
            <tableList :theadList="theadList" :list="tableData">
              <template v-slot:nvi="item"
                ><span
                  v-if="item.data.isAlarm == 'true'"
                  style="color: #ff0000"
                  @click="gotoDetail(item.data)"
                  >{{ item.data.nvi }}</span
                >
                <span
                  v-else
                  style="color: #276af0"
                  @click="gotoDetail(item.data)"
                  >{{ item.data.nvi }}</span
                >
              </template>
            </tableList>
            <!-- <router-view class="page-view" /> -->
          </transition>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import { flashFloodRisk } from '@/api/projectDetail'
import MainMap from './components/MainMap.vue'
import MapLegend from './components/MapLegend.vue'
import tableList from '@/components/tableList.vue'
export default {
  components: {
    MainMap,
    MapLegend,
    tableList,
  },
  data() {
    return {
      redPointLayer: null,
      bluePointLayer: null,
      singlePointLayer: null,
      showModel: 'small',
      transitionName: 'slide-right',
      option: [
        { text: '全部', value: '' },
        { text: '正常', value: 'false' },
        { text: '报警', value: 'true' },
      ],
      isAlarm: '',
      naturalAdnm: '',
      visible: false,
      // finished: false,
      theadList: [
        {
          label: '村落名称',
          prop: 'nvi',
          slot: true,
        },
        {
          label: '转移责任人',
          prop: 'chargerName',
          slot: false,
        },
        {
          label: '联系方式',
          prop: 'chargerPhone',
          slot: false,
        },
      ],
      tableData: [],
      title:'山洪风险'
    }
  },
  computed: {
    routeDeep() {
      let routeDeep = []
      for (var i = 0; i < this.routes.length; i++) {
        routeDeep.push(this.routes[i].path)
      }
      return routeDeep
    },
  },
  watch: {
    $route(to, from) {
      const routeDeep = this.routeDeep
      const toDepth = routeDeep.indexOf(to.path)
      const fromDepth = routeDeep.indexOf(from.path)
      this.transitionName = toDepth > fromDepth ? 'slide-left' : 'slide-right'
    },
  },
  mounted() {
    if (this.$route.query.type) {
      this.isAlarm = 'true'
      this.title = '山洪超警'
      this.option = [{ text: '报警', value: 'true' }]
    }
    this.getList()
  },
  methods: {
    open() {
      this.visible = true
    },
    // onLoad() {
    //   setTimeout(() => {
    //     for (let i = 0; i < 10; i++) {
    //       this.tableData.push(this.tableData[0])
    //     }
    //     // 数据全部加载完成
    //     if (this.tableData.length >= 40) {
    //       this.finished = true
    //     }
    //   }, 1000)
    // },
    async getList() {
      this.$refs.mainMap.cleanPointLayer(this.redPointLayer)
      this.$refs.mainMap.cleanPointLayer(this.bluePointLayer)
      this.$refs.mainMap.cleanPointLayer(this.singlePointLayer)
      this.tableData = []
      let res = await flashFloodRisk.list({
        naturalAdnm: this.naturalAdnm,
        isAlarm: this.isAlarm,
      })
      //   console.log(res.data)
      if (res.code === 0 && res.data.length) {
        if (res.data.length !== 1) {
          const redPointArr = res.data.filter((item) => item.isAlarm == 'true')
          const bluePointArr = res.data.filter(
            (item) => item.isAlarm == 'false'
          )
        //   console.log(redPointArr)
        //   console.log(bluePointArr)
          let redData = []
          redPointArr.forEach((item) => {
            // console.log(item)
            // item.lat = item.latitude
            item.lng = item.lon
            redData.push({
              point: [item.lng, item.lat],
              ...item,
            })
          })
          let configRedObj = {
            src: require('@/assets/images/map/village_red.png'),
            scale: 0.9,
          }
          let blueData = []
          bluePointArr.forEach((item) => {
            // console.log(item)
            // item.lat = item.latitude
            item.lng = item.lon
            blueData.push({
              point: [item.lng, item.lat],
              ...item,
            })
          })
          let configBlueObj = {
            src: require('@/assets/images/map/village_blue.png'),
            scale: 0.9,
          }
          //   let data = []
          //   res.data.forEach((item) => {
          //     // console.log(item)
          //     // item.lat = item.latitude
          //     item.lng = item.lon
          //     data.push({
          //       point: [item.lng, item.lat],
          //       ...item,
          //     })
          //   })
          this.redPointLayer = this.$refs.mainMap._drawPoint(
            redData,
            configRedObj,
            this.redPointLayer
          )
          this.bluePointLayer = this.$refs.mainMap._drawPoint(
            blueData,
            configBlueObj,
            this.bluePointLayer
          )
          this.tableData = res.data
        //   console.log(this.tableData, 'tableData')
          this.$refs.mainMap.setView(res.data[0].lon, res.data[0].lat, 13)
        } else {
          // console.log(res.data[0]);
          if (res.data[0].lon && res.data[0].lat) {
            let configObj = {
              src: require('@/assets/images/my-position.png'),
              scale: 1.5,
            }
            this.$localData('set', 'projectDetail', res.data[0])
            let data = []
            res.data.forEach((item) => {
              //   item.lat = item.latitude
              item.lng = item.lon
              data.push({
                point: [item.lng, item.lat],
                ...item,
              })
            })
            this.singlePointLayer = this.$refs.mainMap._drawPoint(
              data,
              configObj,
              this.singlePointLayer
            )
            this.tableData = res.data
            this.$refs.mainMap.setView(res.data[0].lon, res.data[0].lat, 17)
          }
        }
      }
    },
    gotoDetail(item) {
      //   console.log(item)
      //   this.$store.commit('SET_ENFINEERINGINFO', row)
      //   this.$store.commit('SET_PROJECTCODE', row.code)
      this.$localData('set', 'flashFloodRiskInfo', item)
      //   this.$localData("set", "projectCode", row.code)
      // this.$router.push("/reservoirDetail?code=" + row.code + '&name=' + row.name + '&type=' + this.type);
      this.$router.push('/flashFloodRiskDetail?naturalAdnm=' + item.nvi)
    },
  },
}
</script>

<style lang="scss" scoped>
.fade-enter,
.fade-leave-to {
  transform: translateY(100%);
}
.fade-enter-active,
.fade-leave-active {
  transition: all 0.5s linear;
}
.tidalLevelForecast_box {
  height: 100%;
  width: 100%;
  .search_box {
    width: 100%;
    height: 44px;
    background-color: #ffffff;
    display: flex;
    ::v-deep .van-dropdown-menu {
      height: 100%;
      width: 50%;
      .van-dropdown-menu__bar {
        height: 100%;
        box-shadow: none;
      }
      //   .van-dropdown-item {
      //     width: 50%;
      //   }
      .van-overlay {
        background: transparent;
      }
    }
    .van-search {
      width: 50%;
    }
  }
  .map_box {
    width: 100%;
    height: calc(100% - 100px);
    position: relative;
  }
  .bottm_info {
    height: 50px;
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
        background: url('../../../assets/map_mode.png') no-repeat center;
        background-size: 80%;
      }
      .up_flod {
        justify-self: end;
        width: 16px;
        height: 16px;
        background: url('../../../assets/up_flod.png') no-repeat center;
        background-size: 80%;
      }
      .down_flod {
        justify-self: end;
        width: 16px;
        height: 16px;
        background: url('../../../assets/down_flod.png') no-repeat center;
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
      height: 60px;
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
      height: calc(100% - 58px);
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