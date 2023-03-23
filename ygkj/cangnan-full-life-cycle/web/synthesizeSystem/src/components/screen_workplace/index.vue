<template>
  <el-dialog
    append-to-body
    width="1600px"
    :visible.sync="visiable"
    :before-close="handleClose"
    :close-on-click-modal="false"
    class="workplace-dialog"
  >
    <template slot="title">
      <div class="workplace-title">
        <div class="workplace-title__label">
          <span>我的工作台</span>
        </div>
        <div class="workplace-menu">
          <ul class="workplace-menu__list">
            <li
              v-for="(item, index) in nav"
              :key="index"
              :class="['workplace-menu__list-item', activeIndex == index && 'active']"
              @click="activeIndex = index"
            >
              <i :class="item.icon" />
              <span>{{ item.title }}</span>
            </li>
          </ul>
        </div>
      </div>
    </template>

    <div class="workplace-main">
      <Message v-if="activeIndex == 0" />
      <div v-else style="height: 570px"></div>
    </div>
  </el-dialog>
</template>
<script>
import Message from "./components/message/index.vue";
export default {
  name: "Workplace",
  props: { visiable: Boolean },
  components: { Message },
  data() {
    return {
      activeIndex: 0,
      nav: [
        { title: "消息列表", icon: "icon-workplace-msg" },
        { title: "我的数据", icon: "icon-workplace-data" },
        { title: "我的收藏", icon: "icon-workplace-fav" },
        { title: "发起工作", icon: "icon-workplace-work" },
        { title: "个人信息", icon: "icon-workplace-info" },
        { title: "修改密码", icon: "icon-workplace-pwd" },
      ],
    };
  },
  methods: {
    handleClose() {
      this.$emit("close");
    },
  },
};
</script>
<style lang="scss" scoped>
.workplace-dialog {
  .workplace-title {
    position: relative;
    color: #fff;
    display: flex;
    .workplace-title__label {
      width: 480px;
      height: 40px;
      padding: 5px 0 0 68px;
      font-size: 20px;
      color: #ffffff;
      line-height: 32px;
      background-image: url("../../assets/images/ScreenAsset/workplace-title.png");
    }
  }
  .workplace-menu {
    display: inline-block;
    position: absolute;
    left: 410px;
    .workplace-menu__list {
      display: flex;
      .workplace-menu__list-item {
        display: flex;
        align-items: center;
        cursor: pointer;
        width: 195px;
        height: 40px;
        margin: 0 -5px;
        padding: 8px 16px 8px 40px;
        background-image: url("../../assets/images/ScreenAsset/workplace-menu-bg.png");
        &.active {
          background-image: url("../../assets/images/ScreenAsset/workplace-menu-bg_active.png");
        }
        > i {
          margin-right: 8px;
        }
      }
    }
  }
  .workplace-main {
    height: 100%;
    color: #fff;
    padding-top: 32px;
  }
  ::v-deep.el-dialog {
    background-color: #0000;
    .el-dialog__header {
      height: 54px;
      padding: 18px;
      border: 1px solid #00c1ff;
      box-shadow: inset 0 4px 30px 0 #2351ef40;
      background-image: linear-gradient(180deg, #1f375f 0%, #1b3047 100%);
      border-bottom: 0;
      margin: 2px 2px 0;
    }
    .el-dialog__body {
      min-height: 100px;
      padding: 0 18px;
      background-image: linear-gradient(180deg, #1c3357 0%, #0b2045 100%);
      border: 1px solid #00c1ff;
      border-top: 0;
      margin: 0 2px;
    }
    &:after {
      content: "";
      position: absolute;
      height: calc(100% + 4px);
      width: 100%;
      top: -2px;
      left: 0;
      background: linear-gradient(to bottom, #35b9e3, #5398f6) left top no-repeat,
        linear-gradient(to bottom, #5398f6, #608aff) left top no-repeat,
        linear-gradient(to bottom, #35b9e3, #5398f6) right top no-repeat,
        linear-gradient(to bottom, #5398f6, #608aff) right top no-repeat,
        linear-gradient(to bottom, #5398f6, #35b9e3) left bottom no-repeat,
        linear-gradient(to bottom, #608aff, #5398f6) left bottom no-repeat,
        linear-gradient(to bottom, #5398f6, #35b9e3) right bottom no-repeat,
        linear-gradient(to bottom, #608aff, #5398f6) right bottom no-repeat;
      /*设置大小*/
      background-size: 18px 2px, 2px 18px;
      z-index: -1;
    }
  }
}
</style>
