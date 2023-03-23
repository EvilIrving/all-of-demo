<!--
 * @Date: 2022-01-04 16:52:32
 * @Author: dtb
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2022-05-13 16:17:23
 * @FilePath: \lucheng\src\views\water_disaster_custom\components\DistributionMap.vue
-->
<template>
  <div class="distribution_map">
    <div class="sulice_map">
      <div class="sulice_box">
        <p class="tide_title">实时潮位水位：</p>
        <div class="stname_list">
          <ul>
            <li v-for="item in latestDataOne" :key="item.stName">
              <span>{{item.stName}}：{{ item.level?item.level:0}}m </span>
            </li>
          </ul>
          <ul>
            <li v-for="item in latestDataTwo" :key="item.stName">
              <span>{{item.stName}}：{{ item.level?item.level:0}}m </span>
            </li>
          </ul>
          <ul>
            <li v-for="item in latestDataThree" :key="item.stName">
              <span>{{item.stName}}：{{ item.level?item.level:'-'}}m </span>
            </li>
          </ul>
        </div>
        <div class="wrapper">
          <ul class="st_list">
            <li v-for="item in latestDatafour" :key="item.stName">
              <span>{{item.stName}}：{{ item.level?item.level:0}}m </span>
            </li>
          </ul>
          <ul class="st_list">
            <li v-for="item in latestDatafive" :key="item.stName">
              <span>{{item.stName}}：{{ item.level?item.level:0}}m </span>
            </li>
          </ul>
          <ul class="st_list">
            <li v-for="item in latestDatasix" :key="item.stName">
              <span>{{item.stName}}：{{ item.level?item.level:0}}m </span>
            </li>
          </ul>
          <img ref="oujiangBox" class="oujiang" src="../../../assets/images/distributionMap/Oujiang.png" alt="">
          <img ref="wrthBox" class="wrth" src="../../../assets/images/distributionMap/wrth.png" alt="">
          <img ref="qdnBox" class="qdn" src="../../../assets/images/distributionMap/qdn.png" alt="">
          <div class="ul_wrapper">
            <ul class="river_list_one">
              <li v-for="(item,index) in latestGatageOne" :style="{height:index===0||index===1||index===2||index===3||index===5?topPos+'px':topPos - 26 +'px'}" :key="item.name">
                <img v-if="Number(item.ovs) === 0" :src="require(`@/assets/images/distributionMap/${index}.png`)" alt="">
                <img v-else-if="index === 0|| index === 1||index === 2||index === 5 || index === 3" class="flow" :src="require('@/assets/images/distributionMap/flow-long.png')" alt="">
                <img v-else class="flow" :src="require('@/assets/images/distributionMap/flow-long.png')" alt="">
                <div :class="['river_item']">
                  <img v-if="index === 0|| index === 1||index === 2||index === 5 || index === 3" :src="require('@/assets/images/distributionMap/purple-sulice.png')" alt="">
                  <img v-else-if="Number(item.ovs) === 0" :src="require('@/assets/images/distributionMap/blue-sulice.png')" alt="">
                  <img v-else class="rotate" :src="require('@/assets/images/distributionMap/sulice-open.png')" alt="">
                  <div :class="['second_box']">
                    <img v-if="index === 0|| index === 1||index === 2||index === 5 || index === 3" :src="require(`@/assets/images/distributionMap/sulicename-purple.png`)" alt="">
                    <img v-else :src="require(`@/assets/images/distributionMap/sulicename-blue.png`)" alt="">
                    <el-popover placement="top" width="200" trigger="hover">
                      <div>
                        <h3>{{ item.wagaName }}</h3>
                        <p>闸上水位：<span>{{item.upz?item.upz:'-'}}m</span></p>
                        <p>闸下水位：<span>{{item.dwz?item.dwz:'-'}}m</span></p>
                        <p>过闸流量：<span>{{item.mxgtq?item.mxgtq:'-'}}m³/s</span></p>
                        <p>闸门开度：<span>{{item.ovs?item.ovs:'-'}}</span></p>
                      </div>
                      <div slot="reference">
                        <span>{{item.wagaName}} {{item.gaorNum}}孔</span>
                      </div>
                    </el-popover>
                  </div>
                  <div class="third_box">
                    <img v-if="index === 0|| index === 1||index === 2||index === 5 || index === 3" :src="require('@/assets/images/distributionMap/rivername-short.png')" alt="">
                    <img v-else :src="require(`@/assets/images/distributionMap/rivername-blue.png`)" alt="">
                    <span>{{item.riverName}}</span>
                  </div>
                </div>
              </li>
            </ul>
            <ul class="river_list_one river_list_two">
              <li v-for="(item,index) in latestGatageTwo" :style="{height:topPos + 2 +'px'}" :key="item.name">
                <img v-if="Number(item.ovs) === 0" :src="require(`@/assets/images/distributionMap/0${index}.png`)" alt="">
                <img v-else class="flow" :src="require('@/assets/images/distributionMap/flow-long.png')" alt="">
                <div :class="['river_item']">
                  <img v-if="Number(item.ovs) === 0" :src="require('@/assets/images/distributionMap/blue-sulice.png')" alt="">
                  <img v-else class="rotate" :src="require('@/assets/images/distributionMap/sulice-open.png')" alt="">
                  <div :class="['second_box']">
                    <img :src="require(`@/assets/images/distributionMap/sulicename-blue.png`)" alt="">
                    <el-popover placement="top" width="200" trigger="hover">
                      <div>
                        <h3>{{ item.wagaName }}</h3>
                        <p>闸上水位：<span>{{item.upz?item.upz:'-'}}m</span></p>
                        <p>闸下水位：<span>{{item.dwz?item.dwz:'-'}}m</span></p>
                        <p>过闸流量：<span>{{item.mxgtq?item.mxgtq:'-'}}m³/s</span></p>
                        <p>闸门开度：<span>{{item.ovs?item.ovs:'-'}}</span></p>
                      </div>
                      <div slot="reference">
                        <span>{{item.wagaName}} {{item.gaorNum}}孔</span>
                      </div>
                    </el-popover>
                  </div>
                  <div class="third_box">
                    <img :src="require('@/assets/images/distributionMap/rivername-blue.png')" alt="">
                    <span>{{item.riverName}}</span>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { SluiceDistributionMap } from '@/api/bigScreenPart';
export default {
  props: {

  },
  data() {
    return {
      latestDataOne: [],// 列表
      latestDataTwo: [],// 列表
      latestDataThree: [],// 列表
      latestDatafour: [],// 列表
      latestDatafive: [],// 列表
      latestDatasix: [],// 列表
      latestGatageOne: [], // 闸口孔数A
      latestGatageTwo: [], // 闸口孔数B
      topPos: 0,
    };
  },
  computed: {},
  async mounted() {
    await this.getLatestData()
    await this.getLatestGatage()
    this.handleWindowResize()
    window.addEventListener('resize', this.handleWindowResize)
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleWindowResize);
  },
  methods: {
    handleWindowResize() {
      let one = document.getElementsByClassName('oujiang')[0].clientHeight
      let two = document.getElementsByClassName('wrth')[0].offsetTop
      this.topPos = two - one + 40
    },
    async getLatestData() {
      let res = await SluiceDistributionMap.latestData();
      if (res.code === 0) {
        this.latestDataOne = res.data.slice(0, 2)
        this.latestDataTwo = [res.data[2],res.data[9]]
        this.latestDataThree = [res.data[10],res.data[11]]
        this.latestDatafour = [res.data[4],res.data[5]]
        this.latestDatafive = [res.data[6],res.data[7]]
        this.latestDatasix = [res.data[3],res.data[8]]
      }
    },
    async getLatestGatage() {
      let res = await SluiceDistributionMap.latestGatage();
      if (res.code === 0) {
        this.latestGatageOne = res.data.slice(0, 13)
        this.latestGatageTwo = res.data.slice(13, 16)
      }
    },
  },
};
</script>

<style scoped lang="scss">
.distribution_map {
  position: fixed;
  top: 0;
  width: 100%;
  height: 100%;
  background: url('../../../assets/images/bgblack.png');
  z-index: 0;
  .sulice_map {
    @include flexbox;
    @include flexAC;
    width: 100%;
    height: 100%;
    background: 50% 70%/95% no-repeat url('../../../assets/images/bglittle.png');
    .sulice_box {
      position: absolute;
      top: 142px;
      left: 0;
      right: 0;
      width: 90%;
      height: calc(100% - 250px);
      margin: 0 auto;
      p.tide_title {
        width: 80%;
        font: normal bold 20px/1 sans-serif;
        margin: 0 50px 5px;
        color: #36c0e4;
        background: linear-gradient(180deg, #36c0e4 50%, #0639ff 100%);
        background-clip: text;
        -webkit-background-clip: text;
        color: transparent;
      }
      .stname_list {
        width: 100%;
        height: 10%;
        margin: 0 20px;
        @include flexbox;
        @include flexJC(space-between);
        @include flexAI(center);
        @include flexflow(row, nowrap);
        position: relative;
        ul {
          height: 100%;
          &:nth-of-type(1) {
            width: 20%;
            margin-left: 25px;
          }
          &:nth-of-type(2) {
            width: 36%;
            height: 25px;
            position: absolute;
            bottom: 0px;
            left: 100px;
            @include flexbox;
            @include flexJC(space-between);
            @include flexflow(row, nowrap);
          }
          &:nth-of-type(3) {
            width: 20%;
            @include flexbox;
            @include flexJC(flex-start);
            @include flexflow(row, nowrap);
          }
          li {
            display: flex;
            align-items: end;
            span {
              width: 100%;
              font: normal bold 18px/1.5 sans-serif;
              color: #36c0e4;
              background: linear-gradient(180deg, #36c0e4 50%, #0639ff 100%);
              background-clip: text;
              -webkit-background-clip: text;
              color: transparent;
              margin-right: 10px;
            }
          }
        }
      }
      .wrapper {
        width: 100%;
        height: 86%;
        position: relative;
        .st_list {
          z-index: 200;
          position: absolute;
          bottom: 90px;
          &:nth-of-type(1) {
            width: 32%;
            left: 120px;
            @include flexbox;
            @include flexJC(space-between);
            @include flexflow(row, nowrap);
          }
          &:nth-of-type(2) {
            width: 38%;
            left: 1160px;
            @include flexbox;
            @include flexJC(space-between);
            @include flexflow(row, nowrap);
          }
          &:nth-of-type(3) {
            bottom: 200px;
            width: 10%;
            height: 100px;
            left: -5px;
            @include flexbox;
            @include flexJC(space-between);
            @include flexflow(row, wrap);
          }
          li {
            display: flex;
            align-items: end;
            span {
              width: 100%;
              font: normal bold 18px/1.5 sans-serif;
              color: #36c0e4;
              background: linear-gradient(180deg, #36c0e4 50%, #0639ff 100%);
              background-clip: text;
              -webkit-background-clip: text;
              color: transparent;
              margin-right: 10px;
            }
          }
        }
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
