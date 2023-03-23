<!--
 * @Date: 2022-01-04 16:52:32
 * @Author: dtb
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2022-06-07 19:22:19
 * @FilePath: \lucheng\src\views\WatershedSchedule\DistributionMap.vue
-->
<template>
  <div class="distribution_map">
    <div class="sulice_map">
      <img :src="imgSrc" class="tab_lenged" alt="" />
      <div class="sulice_box">
        <ul class="stname_list">
          <li
            v-for="(item, index) in scheduleList"
            :key="index"
            :class="{ active: item.active === scheduleIndex ? true : false }"
            @click="switchTab(item.name, index)"
          >
            <span>{{ item.name }}</span>
          </li>
        </ul>
        <div class="wrapper">
          <img ref="oujiangBox" class="oujiang" src="../../assets/images/distributionMap/Oujiang.png" alt="" />
          <img ref="wrthBox" class="wrth" src="../../assets/images/distributionMap/wrth.png" alt="" />
          <img ref="qdnBox" class="qdn" src="../../assets/images/distributionMap/qdn.png" alt="" />
          <div class="ul_wrapper">
            <ul class="river_list_one">
              <li
                v-for="(item, index) in latestGatageOne"
                :style="{
                  height:
                    index === 0 || index === 1 || index === 2 || index === 3 || index === 5
                      ? topPos + 'px'
                      : topPos - 26 + 'px'
                }"
                :key="item.name"
              >
                <img
                  v-if="Number(item.ovs) === 0"
                  :src="require(`@/assets/images/distributionMap/${index}.png`)"
                  alt=""
                />
                <img
                  v-else-if="index === 0 || index === 1 || index === 2 || index === 5 || index === 3"
                  :class="[item.ovs? 'flow' :'']"
                  :src="require('@/assets/images/distributionMap/flow-long.png')"
                  alt=""
                />
                <img v-else class="flow" :src="require('@/assets/images/distributionMap/flow-long.png')" alt="" />
                <div class="river_item">
                  <!-- 0 正常、1 接近满负荷、2 满负荷、-1 无数据 -->
                  <img
                    v-if="item.color == 'yellow'"
                    :class="[Number(item.ovs) !== 0 ? 'rotate' :'']"
                    :src="require('@/assets/images/distributionMap/yellow_sluice.png')"
                    alt=""
                  />
                  <img
                    v-if="item.color == 'gray'"
                    :class="[Number(item.ovs) !== 0 ? 'rotate' :'']"
                    :src="require('@/assets/images/distributionMap/gray_sluice.png')"
                    alt=""
                  />
                  <img
                    v-if="item.color == 'red'"
                    :class="[Number(item.ovs) !== 0 ? 'rotate' :'']"
                    :src="require('@/assets/images/distributionMap/red_sluice.png')"
                    alt=""
                  />
                  <img
                    v-if="item.color == 'purple'"
                    :class="[Number(item.ovs) !== 0 ? 'rotate' :'']"
                    :src="require('@/assets/images/distributionMap/purple-sulice.png')"
                    alt=""
                  />
                  <img
                    v-if="item.color == 'blue'"
                    :class="[Number(item.ovs) !== 0 ? 'rotate' :'']"
                    :src="require('@/assets/images/distributionMap/blue-sulice.png')"
                    alt=""
                  />
                  <div class="second_box">
                    <img
                      v-if="index === 0 || index === 1 || index === 2 || index === 5 || index === 3"
                      :src="require(`@/assets/images/distributionMap/sulicename-purple.png`)"
                      alt=""
                    />
                    <img v-else :src="require(`@/assets/images/distributionMap/sulicename-blue.png`)" alt="" />
                    <span @click="openWagaDialog(item)">{{ item.wagaName }} {{ item.gaorNum }}孔</span>
                  </div>
                  <div class="third_box">
                    <img
                      v-if="index === 0 || index === 1 || index === 2 || index === 5 || index === 3"
                      :src="require('@/assets/images/distributionMap/rivername-short.png')"
                      alt=""
                    />
                    <img v-else :src="require(`@/assets/images/distributionMap/rivername-blue.png`)" alt="" />
                    <span>{{ item.riverName }}</span>
                  </div>
                </div>
              </li>
            </ul>
            <ul class="river_list_one river_list_two">
              <li v-for="(item, index) in latestGatageTwo" :style="{ height: topPos + 2 + 'px' }" :key="item.name">
                <img
                  v-if="Number(item.ovs) === 0"
                  :src="require(`@/assets/images/distributionMap/0${index}.png`)"
                  alt=""
                />
                <img v-else class="flow" :src="require('@/assets/images/distributionMap/flow-long.png')" alt="" />
                <div class="river_item" >
                  <!-- <img class="rotate" :src="require('@/assets/images/distributionMap/yellow_sluice.png')" alt="" />
                  <img class="rotate" :src="require('@/assets/images/distributionMap/gray_sluice.png')" alt="" />
                  <img class="rotate" :src="require('@/assets/images/distributionMap/red_sluice.png')" alt="" />
                  <img :src="require('@/assets/images/distributionMap/purple-sulice.png')" alt="" /> -->
                  <img :class="[Number(item.ovs) !== 0 ? 'rotate' :'']" :src="require('@/assets/images/distributionMap/blue-sulice.png')" alt="" />
                  <div class="second_box">
                    <img :src="require(`@/assets/images/distributionMap/sulicename-blue.png`)" alt="" />
                    <span @click="openWagaDialog(item)">{{ item.wagaName }} {{ item.gaorNum }}孔</span>
                  </div>
                  <div class="third_box">
                    <img :src="require('@/assets/images/distributionMap/rivername-blue.png')" alt="" />
                    <span>{{ item.riverName }}</span>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <PrimaryDialog
      :mainTitle="activeWagaName"
      :hasAnalysis="false"
      :visible="AddCardDialog"
      @handleClose="HandleClose"
      :appendToBody="true"
      :modal="true"
      width="1000px"
    >
      <div class="dialog-content" v-if="AddCardDialog">
        <water-disaster-distribution-map-dialog :pointData="pointData" />
      </div>
    </PrimaryDialog>
  </div>
</template>

<script>
import { SluiceDistributionMap } from '@/api/bigScreenPart';
import { PrimaryDialog } from '@/components';
import WaterDisasterDistributionMapDialog from '../WatershedSchedule/components/WaterDisasterDistributionMapDialog.vue';
export default {
  name: 'DistributionMap',
  data() {
    return {
      latestDataOne: [], // 列表
      latestDataTwo: [], // 列表
      latestDataThree: [], // 列表
      latestGatageOne: [], // 闸口孔数A
      latestGatageTwo: [], // 闸口孔数B
      topPos: 0,
      scheduleIndex: 0,
      imgSrc: require('@/assets/images/distributionMap/control.png'),
      scheduleList: [
        { active: 0, name: '控运分析', imgSrc: require('@/assets/images/distributionMap/control.png') },
        { active: 1, name: '负荷分析', imgSrc: require('@/assets/images/distributionMap/load.png') },
        { active: 2, name: '排水分析', imgSrc: require('@/assets/images/distributionMap/drain.png') }
      ],
      AddCardDialog: false, //
      activeWagaName: '',
      pointData: {}
    };
  },
  components: {
    PrimaryDialog,
    WaterDisasterDistributionMapDialog
  },
  computed: {},
  async mounted() {
    await this.getLatestData();
    await this.getLatestGatage();
    this.handleWindowResize();
    window.addEventListener('resize', this.handleWindowResize);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleWindowResize);
  },
  methods: {
    openWagaDialog(item) {
      this.activeWagaName = item.wagaName;
      this.AddCardDialog = true;
      this.pointData = item;
    },
    switchTab(name, index) {
      this.scheduleIndex = index;
      this.imgSrc = this.scheduleList[index].imgSrc;
      this.setColor(this.latestGatage)
    },
    handleWindowResize() {
      let one = document.getElementsByClassName('oujiang')[0].clientHeight;
      let two = document.getElementsByClassName('wrth')[0].offsetTop;
      this.topPos = two - one + 40;
    },
    HandleClose() {
      this.AddCardDialog = false;
    },
    async getLatestData() {
      let res = await SluiceDistributionMap.latestData();
      if (res.code === 0) {
        this.latestDataOne = res.data.slice(0, 2);
        this.latestDataTwo = res.data.slice(2, 4);
        this.latestDataThree = res.data.slice(4, 6);
      }
    },
    setColor(list) {
      switch (this.scheduleIndex) {
        case 0:
          list.forEach((item, index) => {
            if (item.kyStatus == 0) {
              if (index < 4 || index == 5) {
                item['color'] = 'purple';
              } else {
                item['color'] = 'blue';
              }
            } else if (item.kyStatus == 1) {
              item['color'] = 'red';
            } else {
              item['color'] = 'gray';
            }
          });
          break;
        case 1:
          list.forEach((item, index) => {
            if (item.llStatus == 0) {
              if (index < 4 || index == 5) {
                item['color'] = 'purple';
              } else {
                item['color'] = 'blue';
              }
            } else if (item.llStatus == 1) {
              item['color'] = 'yellow';
            } else if (item.llStatus == 2) {
              item['color'] = 'red';
            } else {
              item['color'] = 'gray';
            }
          });
          break;
        default:
          list.forEach((item, index) => {
            if (item.psStatus == 0) {
              if (index < 4 || index == 5) {
                item['color'] = 'purple';
              } else {
                item['color'] = 'blue';
              }
            } else{
              item['color'] = 'red';
            }
          });
          break;
      }
      this.latestGatageOne = list.slice(0, 13);
      this.latestGatageTwo = list.slice(13, 16);
    },
    async getLatestGatage() {
      let res = await SluiceDistributionMap.latestGatage();
      if (res.code === 0) {
        this.latestGatage = res.data;
        this.setColor(this.latestGatage);
      }
    }
  }
};
</script>

<style scoped lang="scss">
.distribution_map {
  position: fixed;
  top: 0;
  width: 100%;
  height: 100%;
  background: url('../../assets/images/bgblack.png');
  z-index: 0;
  .sulice_map {
    @include flexbox;
    @include flexAC;
    width: 100%;
    height: 100%;
    background: 50% 70%/95% no-repeat url('../../assets/images/bglittle.png');
    img.tab_lenged {
      width: 80%;
      position: absolute;
      top: 125px;
      right: 80px;
    }
    .sulice_box {
      position: absolute;
      top: 142px;
      left: 0;
      right: 0;
      width: 90%;
      height: calc(100% - 250px);
      margin: 0 auto;
      .stname_list {
        width: 24%;
        height: 10%;
        margin: 0 30px;
        // float: left;
        @include flexbox;
        @include flexJC(space-between);
        @include flexAI(center);
        @include flexflow(row, nowrap);
        position: relative;
        li {
          @include flexbox;
          @include flexJC(flex-start);
          @include flexAI(center);
          list-style: none;
          width: 25%;
          height: 50%;
          cursor: pointer;
          background: url('../../assets/images/distributionMap/scheduled.png') no-repeat;
          background-size: 100%;
          span {
            font-size: 15px;
            color: #fff;
            // background: linear-gradient(180deg, #36c0e4 50%, #0639ff 100%);
            // background-clip: text;
            // -webkit-background-clip: text;
            // color: transparent;
            margin-left: 26px;
          }
          &.active {
            background: url('../../assets/images/distributionMap/scheduled_ac.png') no-repeat;
            background-size: 100%;
            span {
              color: #23e3fb;
            }
          }
        }
      }
      .wrapper {
        width: 100%;
        height: 86%;
        position: relative;
        .oujiang {
          //   position: absolute;
          //   top: 0;
          //   left: 0;
          //   right: 0;
          width: 100%;
          margin: 0 auto;
        }
        .wrth {
          width: 70%;
          position: absolute;
          right: 0;
          bottom: 0;
          left: 2%;
          margin: 0;
        }
        .qdn {
          width: 25%;
          position: absolute;
          right: 2%;
          bottom: 7%;
        }
        .ul_wrapper {
          width: 85%;
          height: 55%;
          margin: -5px auto;
          position: relative;
          .river_list_one {
            float: left;
            width: 70%;
            height: 100%;
            // position: absolute;
            // left: 0;
            // right: 0;
            margin: 0 auto;
            // top: 110px;
            // bottom: 120px;
            @include flexbox;
            @include flexJC(space-around);
            @include flexAI(start);
            @include flexflow(row, nowrap);
            li {
              //   width: 86px;
              height: 100%;
              position: relative;
              & > img {
                //   position: absolute;
                //   top: 0px;
                //   left: 0;
                //   right: 0;
                margin: 0 auto;
                width: 40px;
                height: 100%;
              }
              .river_item {
                position: absolute;
                top: 1%;
                left: -16%;
                height: 100%;
                @include flexbox;
                @include flexJC(space-between);
                @include flexAI(center);
                @include flexflow(column, nowrap);
                & > img {
                  width: 50px;
                  margin-bottom: 100px;
                  &.rotate {
                    animation: rotate 3s linear infinite;
                  }
                  @keyframes rotate {
                    from {
                      transform: rotate(0deg);
                    }
                    to {
                      transform: rotate(360deg);
                    }
                  }
                }
                .second_box {
                  //   width: 100%;
                  height: 90px;
                  position: absolute;
                  top: 25%;
                  cursor: pointer;
                  &.second_box_higher {
                    height: 160px;
                  }
                  img {
                    width: 100%;
                    height: 100%;
                  }
                  span {
                    width: 90%;
                    height: 100%;
                    color: white;
                    font: normal bold 16px/1 sans-serif;
                    position: absolute;
                    top: 4px;
                    left: 2px;
                    text-align: center;
                    overflow: hidden;
                    // writing-mode: vertical-lr; /*从左向右 从右向左是 writing-mode: vertical-rl;*/
                    // writing-mode: tb-lr; /*IE浏览器的从左向右 从右向左是 writing-mode: tb-rl；*/
                  }
                }
                .third_box {
                  //   width: 50%;
                  height: 130px;
                  position: absolute;
                  bottom: 5%;
                  position: relative;
                  @include flexbox;
                  @include flexAC();
                  img {
                    width: 100%;
                    height: 100%;
                  }
                  span {
                    width: 100%;
                    height: 100%;
                    color: white;
                    font: normal bold 16px/1 sans-serif;
                    position: absolute;
                    top: 0;
                    right: 0;
                    bottom: 0;
                    left: 15px;
                    margin: auto;
                    text-align: center;
                    writing-mode: vertical-lr; /*从左向右 从右向左是 writing-mode: vertical-rl;*/
                    // writing-mode: tb-lr; /*IE浏览器的从左向右 从右向左是 writing-mode: tb-rl；*/
                  }
                }
              }
            }
          }
          .river_list_two {
            float: right;
            width: 19%;
            margin: 0 auto;
            @include flexbox;
            @include flexJC(space-around);
            @include flexAI(start);
            @include flexflow(row, nowrap);
            li {
              height: 100%;
            }
          }
        }
      }
    }
  }
}
</style>
