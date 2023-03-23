<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-26 20:09:17
 * @Description: 地图左侧-统计
 * @FilePath: /rscp-big-screen/src/views/sgzgScreenNew/repairProtect/repairProtectLeft/RepairProtectLeft.vue
-->
<template>
  <div class="map_left" >
    <div class="sgzg_title">
      <div class="sgzg_title_name">
        维养计划
      </div>
      <img src="@/assets/images/qtj-arr.png" alt="" />
    </div>
    <div class="flexbox">
      <div class="left_box1">
        <div
          class="scale_item"
          v-for="(item, index) in jhAllList"
          :key="index"
          :style="{backgroundImage: 'url(' + item.bg + ')'}"
        >
          <div class="scale_item_name">
            <img :src="item.img" alt="" />
            <p>{{ item.name }}</p>
          </div>
          <div class="scale_item_num">{{ item.value }}<span>{{item.unit}}</span></div>
        </div>
      </div>
      <div class="left_box2">
        <div class="wxyh_left" style="background:#6FAB41">
          <img src="@/assets/images/wy-ba.png" alt="">
          <p>正常坝</p>
          <span>(一类坝)</span>
        </div>
        <div class="wxyh_right">
          <div
            class="scale_item"
            v-for="(item, index) in jhOneList"
            :key="index"
          >
            <div class="scale_item_name">
              <img :src="item.img" alt="" />
              <p>{{ item.name }}</p>
            </div>
            <div class="scale_item_num">{{ item.value }}<span>{{item.unit}}</span></div>
          </div>
        </div>
      </div>
      <div class="left_box2">
        <div class="wxyh_left" style="background:#E08700">
          <img src="@/assets/images/wy-ba.png" alt="">
          <p>病害坝</p>
          <span>(二类坝)</span>
        </div>
        <div class="wxyh_right">
          <div
            class="scale_item"
            v-for="(item, index) in jhTwoList"
            :key="index"
          >
            <div class="scale_item_name">
              <img :src="item.img" alt="" />
              <p>{{ item.name }}</p>
            </div>
            <div class="scale_item_num">{{ item.value }}<span>{{item.unit}}</span></div>
          </div>
        </div>
      </div>
      <div class="left_box2">
        <div class="wxyh_left" style="background:#DE5C0A">
          <img src="@/assets/images/wy-ba.png" alt="">
          <p>危险坝</p>
          <span>(三类坝)</span>
        </div>
        <div class="wxyh_right">
          <div
            class="scale_item"
            v-for="(item, index) in jhThreeList"
            :key="index"
          >
            <div class="scale_item_name">
              <img :src="item.img" alt="" />
              <p>{{ item.name }}</p>
            </div>
            <div class="scale_item_num">{{ item.value }}<span>{{item.unit}}</span></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "",
  props: {},
  components: {
  },
  data() {
    return {
      jhAllList: [
        {
          name: "计划项目",
          value: 6426,
          unit: '项',
          img: require("../../../../assets/images/jhwy.png"),
          bg: require("../../../../assets/images/jhwy-bg.png"),
        },
        {
          name: "资金预算",
          value: 21030,
          unit: '万元',
          img: require("../../../../assets/images/wxys.png"),
          bg: require("../../../../assets/images/wxys-bg.png"),
        },
        {
          name: "合同个数",
          value: 1750,
          unit: '个',
          img: require("../../../../assets/images/htgs.png"),
          bg: require("../../../../assets/images/htgs-bg.png"),
        },
        {
          name: "合同金额",
          value: 18250,
          unit: '万元',
          img: require("../../../../assets/images/htje.png"),
          bg: require("../../../../assets/images/htje-bg.png"),
        }
      ],
      jhOneList: [
        {
          name: "计划项目",
          value: 5641,
          unit: '项',
          img: require("../../../../assets/images/jhwy-icon.png"),
        },
        {
          name: "资金预算",
          value: 18446,
          unit: '万元',
          img: require("../../../../assets/images/htje-icon.png"),
        },
        {
          name: "合同个数",
          value: 1577,
          unit: '个',
          img: require("../../../../assets/images/htgs-icon.png"),
        },
        {
          name: "合同金额",
          value: 16445,
          unit: '万元',
          img: require("../../../../assets/images/htje-icon.png"),
        }
      ],
      jhTwoList: [
        {
          name: "计划项目",
          value: 568,
          unit: '项',
          img: require("../../../../assets/images/jhwy-icon.png"),
        },
        {
          name: "资金预算",
          value: 1859,
          unit: '万元',
          img: require("../../../../assets/images/htje-icon.png"),
        },
        {
          name: "合同个数",
          value: 127,
          unit: '个',
          img: require("../../../../assets/images/htgs-icon.png"),
        },
        {
          name: "合同金额",
          value: 1324,
          unit: '万元',
          img: require("../../../../assets/images/htje-icon.png"),
        }
      ],
      jhThreeList: [
        {
          name: "计划项目",
          value: 217,
          unit: '项',
          img: require("../../../../assets/images/jhwy-icon.png"),
        },
        {
          name: "资金预算",
          value: 725,
          unit: '万元',
          img: require("../../../../assets/images/htje-icon.png"),
        },
        {
          name: "合同个数",
          value: 46,
          unit: '个',
          img: require("../../../../assets/images/htgs-icon.png"),
        },
        {
          name: "合同金额",
          value: 481,
          unit: '万元',
          img: require("../../../../assets/images/htje-icon.png"),
        }
      ],
    };
  },
  
  mounted() {
    
  },
  methods: {
    
  },
};
</script>

<style lang="scss" scoped>
.map_left {
  height: 100%;
  z-index: 1002;
  .flexbox{
    height: calc(100% - 48px);
    @include flexbox();
    flex-direction: column;
    padding: 24px 16px;
    box-sizing: border-box;
    &>div{
      @include flex(1);
      &.left_box1{
        @include flexbox();
        @include flexJC(space-between);
        @include flexflow(row wrap);
        .scale_item {
          color: #fff;
          height: 92px;
          cursor: pointer;
          padding: 14px 0;
          box-sizing: border-box;
          margin-right: 5px;
          width: calc(50% - 5px);
          background-size: 100% 100%;
          .scale_item_name {
            @include flexbox();
            @include flexAC();
            padding-left: 16px;
            font-size: 16px;
            img {
              margin-right: 10px;
              width: 22px;
              height: 22px;
            }
          }
          .scale_item_num {
            font-size: 26px;
            font-weight: 500;
            // text-align: center;
            margin-top: 13px;
            padding-left: 45px;
            span {
              font-size: 22px;
              padding-left: 5px;
            }
          }
        }
      }
      &.left_box2{
        @include flexbox();
        margin-top: 20px;
        .wxyh_left{
          width: 130px;
          color: #fff;
          text-align: center;
          img{
            width: 72px;
            height: 72px;
            margin: 35px 0 16px 0;
          }
          p{
            font-size: 20px;
            margin-bottom: 5px;
          }
          span{
            font-size: 12px;
          }
        }
        .wxyh_right{
          width: calc(100% - 130px);
          @include flexbox();
          @include flexJC(space-between);
          @include flexflow(row wrap);
          border: 1px solid #B9D6F3;
          box-sizing: border-box;
          background: #E2ECFF;
          .scale_item {
            color: #333;
            height: 92px;
            cursor: pointer;
            padding: 14px 0;
            box-sizing: border-box;
            width: 50%;
            height: 50%;
            background-size: 100% 100%;
              // border: 1px solid #d3d3d3;
            &:nth-child(1){
              border-right: 1px solid #d3d3d3;
              border-bottom: 1px solid #d3d3d3;
            }
            &:nth-child(4){
              border-left: 1px solid #d3d3d3;
              border-top: 1px solid #d3d3d3;
              position: relative;
              bottom: 1px;
              left: -1px;
            }
            .scale_item_name {
              @include flexbox();
              @include flexAC();
              padding-left: 16px;
              font-size: 16px;
              img {
                margin-right: 10px;
                width: 22px;
                height: 22px;
              }
            }
            .scale_item_num {
              font-size: 24px;
              font-weight: 600;
              text-align: right;
              margin-top: 13px;
              padding-right: 23px;
              span {
                font-size: 16px;
                padding-left: 5px;
              }
            }
          }
        }
      }
    }
  }
}
</style>
