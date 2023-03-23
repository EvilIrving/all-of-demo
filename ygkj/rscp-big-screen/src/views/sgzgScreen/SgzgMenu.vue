<template>
  <div class='wrap'>
    <ul class="screen_menu">
      <li :class="tabIndex == index ? 'active' : ''" v-for="(item, index) in tabData" :key="item.name" @click="selectTab(index)">
        <img v-if="tabIndex == index" :src="item.srcs" alt="">
        <img v-else :src="item.src" alt="">
        <p>{{item.name}}</p>
      </li>
    </ul>
    <router-view class="screen_wrap" />
  </div>
</template>

<script>
export default {
  name: 'SgzgMenu',
  props: {

  },
  components: {

  },
  data() {
    return {
      tabIndex: 0,
      tabData: [
        {
          name: '健康在线',
          path: '/healthyOnline',
          src: require("@/assets/images/jkzx-d.png"),
          srcs: require("@/assets/images/jkzx-s.png"),
        },
        {
          name: '蓄供分析',
          path: '/storageSupply',
          src: require("@/assets/images/xgfx-d.png"),
          srcs: require("@/assets/images/xgfx-s.png"),
        },
        {
          name: '防潮研判',
          path: '/tideAnalysis',
          src: require("@/assets/images/fcyp-d.png"),
          srcs: require("@/assets/images/fcyp-s.png"),
        },
        {
          name: '智慧运用',
          path: '/intelligenceUse',
          src: require("@/assets/images/zhyy-d.png"),
          srcs: require("@/assets/images/zhyy-s.png"),
        },
      ]
    };
  },
  mounted() {
    this.tabIndex = this.tabData.findIndex(item => { return this.$route.path == item.path })
  },
  methods: {
    selectTab(index) {
      //重置
      this.$store.commit("SET_LEFT_SELECT_PROJECT_SCALE", "");
      this.$store.commit("SET_LEFT_SELECT_ADCD", '');
      this.$store.commit("SET_LEFT_SELECT_ADNM", "浙江省");
      //end
      this.tabIndex = index
      this.$router.push(this.tabData[index].path)
    },
  }
}
</script>

<style lang='scss' scoped>
.wrap {
  width: 100%;
  height: 100%;
  position: relative;
  .screen_menu {
    width: 832px;
    position: absolute;
    top: 25px;
    left: 50%;
    margin-left: -416px;
    z-index: 999;
    @include flexbox();
    @include flexJC(space-between);
    & > li {
      width: 196px;
      height: 76px;
      background: #edeff8;
      text-align: center;
      border-radius: 19px;
      color: #3b59cd;
      font-size: 20px;
      border: 4px solid #c3cbea;
      cursor: pointer;
      @include flexbox();
      @include flexJC(center);
      @include flexAI(center);
      img {
        margin-right: 25px;
      }
      p {
        font-weight: 600;
      }
      &.active {
        color: #fff;
        background: #193cc5;
        border: 4px solid #fff;
        cursor: pointer;
        @include flexbox();
        @include flexJC(center);
        @include flexAI(center);
        img {
          margin-right: 25px;
        }
        p {
          font-weight: 600;
        }
        &.active {
          color: #fff;
          background: #193cc5;
        }
      }
    }
  }
  .screen_wrap {
    width: 100%;
    height: 100%;
  }
}
</style>