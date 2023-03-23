<template>
  <div class="person-wrap sidebar-box">
    <div class="sidebar-search">
      <el-input
        class="search-inp"
        v-model="name"
        placeholder="请输入关键字进行搜索"
      >
        <i slot="prefix" class="el-input__icon el-icon-search"></i>
      </el-input>
    </div>

    <div v-for="(item, index) in list" :key="index">
      <div class="person-item">
        <img :src="item.icon ? item.icon : personDefault" alt="" />
        <div>
          <p class="top-p">
            <span>
              <strong>{{ item.realName }}</strong>
              <span>{{ item.job ? item.job : "-" }}</span>
            </span>
            <i class="icon-video-call"></i>
          </p>
          <p><i class="icon-telphone"></i>{{ item.phone }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "PersonCard",
  props: {
    list: {
      type: Array,
    },
  },
  data() {
    return {
      name: "",
      personDefault: require("@/assets/images/user-default.png"),
    };
  },
  components: {},
  mounted() {
    this.$nextTick(() => {});
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
@import "@/style/bigScreenSidebar.scss";
.person-wrap.sidebar-box {
  width: 100%;
  color: #4898fb;
  padding-left: 0;
  .person-title {
    font-size: 14px;
    color: #3f9bfc;
    letter-spacing: 1.33px;
    line-height: 22px;
  }
  .person-item {
    @include flexbox;
    width: 100%;
    height: 90px;
    margin: 16px 0;
    font-size: 16px;
    color: #4898fb;
    img {
      height: 90px;
      width: 88px;
      margin-right: 16px;
    }
    > div {
      width: calc(100% - 110px);
      @include flexbox;
      @include flexJC(space-around);
      flex-direction: column;
      .icon-telphone {
        vertical-align: middle;
        margin-right: 10px;
      }
      .top-p {
        @include flexbox;
        @include flexJC(space-between);
        > i {
          margin-right: 6px;
          cursor: pointer;
        }
      }
      strong {
        font-size: 24px;
        font-weight: 400;
        margin-right: 16px;
      }
      p:first-child {
        color: #4898fb;
      }
      span {
        font-size: 16px;
      }
    }
  }
}
.person-wrap > div:last-child .person-item {
  margin-bottom: 0;
}
</style>