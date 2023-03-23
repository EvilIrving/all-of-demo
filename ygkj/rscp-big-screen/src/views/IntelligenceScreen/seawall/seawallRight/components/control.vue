<template>
  <div class="control-box">
    <div class="control-one-box">
      <div class="select_title">
        <div>
          <img src="../../../../../assets/images/chart_item_icon.png" alt="" />
          沿塘管控
        </div>
      </div>
      <div class="control-one-list">
        <div
          class="control-one-item"
          :class="{ 'avtive-one-item': index === oneActiveItem }"
          v-for="(item, index) in oneList"
          :key="index"
          @click="oneActiveItem = index"
        >
          <img :src="item.imgUrl" alt="" />
          <div>
            <p>{{ item.name }}</p>
            <p>
              <span>{{ item.num }}</span
              >个
            </p>
          </div>
        </div>
      </div>
    </div>
    <div class="control-two-box">
      <div class="select_title">
        <div>
          <img src="../../../../../assets/images/chart_item_icon.png" alt="" />
          跨（穿/临）塘设施
        </div>
      </div>
      <div class="control-two-list">
        <div
          class="control-two-item"
          v-for="(item, index) in twoList"
          :key="index"
        >
          <div>{{ item.name }}</div>
          <div>
            总数<span>{{ item.total }}</span
            >个
          </div>
          <div>
            关闭<span>{{ item.close }}</span
            >个
          </div>
        </div>
      </div>
    </div>
    <div class="control-three-box">
      <div class="select_title">
        <div>
          <img src="../../../../../assets/images/chart_item_icon.png" alt="" />
          在建工程
        </div>
      </div>
      <div class="control-three-list">
        <div
          class="control-three-item"
          v-for="(item, index) in threeList"
          :key="index"
        >
          <img :src="item.imgUrl" alt="">
          <div>
            <p>{{item.name}}</p>
            <p><span>{{item.num}}</span>个</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      oneActiveItem: 0,
      oneList: [
        {
          imgUrl: require("@/assets/images/control-one-a.png"),
          name: "大中型闸站",
          num: 68,
        },
        {
          imgUrl: require("@/assets/images/control-one-b.png"),
          name: "水闸",
          num: 540,
        },
        {
          imgUrl: require("@/assets/images/control-one-c.png"),
          name: "泵站",
          num: 685,
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
          total: 294,
          close: 225,
        },
        {
          name: "缺口",
          total: 45,
          close: 3,
        },
        {
          name: "道口",
          total: 229,
          close: 75,
        },
        {
          name: "涵闸",
          total: 229,
          close: 75,
        },
      ],
      threeList: [
        {
          imgUrl: require("@/assets/images/control-one-a.png"),
          name: "塘顶降低",
          num: 14,
        },
        {
          imgUrl: require("@/assets/images/control-one-b.png"),
          name: "老塘未动",
          num: 2,
        },
        {
          imgUrl: require("@/assets/images/control-one-c.png"),
          name: "高空作业",
          num: 9,
        },
      ],
    };
  },
};
</script>

<style lang="scss" scoped>
.control-box {
  padding: 0 16px;
  .select_title {
    padding: 16px 0px 10px;
    @include flexbox();
    @include flexAC();
    @include flexJC(space-between);
    & > div {
      @include flexbox();
      @include flexAC();
      color: #000;
      font-weight: 500;
      font-size: 16px;
      img {
        margin-right: 5px;
      }
    }
  }
  .control-one-box {
    .control-one-list {
      display: flex;
      flex-wrap: wrap;
      justify-content: space-between;
      padding-top: 20px;
      .control-one-item {
        width: 180px;
        height: 78px;
        padding: 13px 0 13px 24px;
        box-sizing: border-box;
        display: flex;
        cursor: pointer;
        img {
          width: 48px;
          height: 48px;
        }
        div {
          padding-left: 16px;
          p {
            line-height: 20px;
            font-size: 14px;
            color: #000000;
            span {
              font-size: 20px;
              padding-right: 5px;
              font-weight: 600;
              line-height: 32px;
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
  }
  .control-two-box {
    .control-two-list {
      padding-top: 14px;
      .control-two-item {
        background: #f8f8f8;
        padding: 14px 0 13px 16px;
        margin-bottom: 8px;
        > div {
          display: inline-block;
          color: rgba(0, 0, 0, 0.48);
          font-size: 14px;
          span {
            font-size: 18px;
            color: rgba(0, 0, 0, 0.88);
            font-weight: 600;
            padding: 0 8px 0 18px;
          }
          &:nth-child(1) {
            color: #193bc4;
            line-height: 24px;
            font-weight: 600;
            padding-right: 24px;
            border-right: 1px solid #d6d6d6;
          }
          &:nth-child(2) {
            padding-left: 30px;
          }
          &:nth-child(3) {
            padding-left: 40px;
          }
        }
      }
    }
  }
  .control-three-box {
    .control-three-list {
      display: flex;
      justify-content: space-between;
      padding: 20px 25px 0 25px;
      .control-one-item {
        width: 33.333%;
        box-sizing: border-box;
        img {
          width: 48px;
          height: 48px;
        }
        div {
          padding-left: 16px;
          p {
            line-height: 20px;
            font-size: 14px;
            color: #000000;
            span {
              font-size: 20px;
              padding-right: 5px;
              font-weight: 600;
              line-height: 32px;
            }
          }
        }
      }
    }
  }
}
</style>