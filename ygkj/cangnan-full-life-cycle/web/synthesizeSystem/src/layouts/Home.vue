<template>
  <keep-alive>
    <div class="home">
      <main-header 
        @menuClick="menuClick"
      ></main-header>

      <div
        :class="[
          'page-content',
          'clearfix',
          secondMenuList.length > 0 ? 'has-menu' : '',
        ]"
      >
        <div class="second-menu left" v-if="secondMenuList.length > 0">
          <ul>
            <template v-for="(item, index) in secondMenuList">
              <div :key="index">
                <li @click="secondMenuClick(index, item.url, item.children)">
                  <p
                    :class="[
                      item.children.length === 0 && secondMenuSelected === index
                        ? 'active'
                        : '',
                    ]"
                  >
                    <i
                      :class="[
                        item.children.length === 0 &&
                        secondMenuSelected === index
                          ? item.iconClass + '-active'
                          : item.iconClass,
                      ]"
                    ></i>
                    <span>{{ item.name }}</span>
                  </p>
                  <el-collapse-transition>
                    <ul
                      class="third-menu"
                      v-if="item.children"
                      v-show="index === secondMenuSelected"
                    >
                      <li
                        @click.stop="thirdMenuClick(index, child.url)"
                        v-for="(child, index) in item.children"
                        :key="index"
                        :class="[thirdMenuSelected === index ? 'active' : '']"
                      >
                        <span>{{ child.name }}</span>
                      </li>
                    </ul>
                  </el-collapse-transition>
                </li>
              </div>
            </template>
          </ul>
        </div>
        <div class="router-content left">
          <router-view :key="$route.query.t"></router-view>
        </div>
      </div>
    </div>
  </keep-alive>
</template>

<script>
import { MainHeader } from "@/components";
export default {
  name: "Home",
  props: {},
  data() {
    return {
      secondMenuList: [],
      secondMenuSelected: 0,
      thirdMenuSelected: 0,
    };
  },
  components: {
    MainHeader,
  },
  mounted() {

  },
  methods: {
    menuClick(menu) {
      console.log(menu)
      if (menu.children && menu.children.length > 0) {
        this.secondMenuList = menu.children;
        this.secondMenuClick(
          0,
          menu.children[0].url,
          menu.children[0].children
        );
      } else {
        this.$router.push(menu.value);
        this.secondMenuList = [];
      }
    },
    secondMenuClick(index, url, children) {
      this.secondMenuSelected = index;
      this.thirdMenuSelected = children ? 0 : -1;
      if (children.length > 0) {
        this.$router.push(children[0].url);
      } else {
        this.$router.push(url);
      }
    },
    thirdMenuClick(index, url) {
      this.thirdMenuSelected = index;
      this.$router.push(url);
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/style/theme.scss";
$mainMenuHeight: 60px;
$secondMenuWidth: 255px;
$menuActiveBorderColor: yellow;
$headerHeight: 80px;
.home {
  width: 100%;
  height: 100%;
  position: relative;
  .router-content {
    width: 100%;
    height: 100%;
  }

  .page-content {
    width: 100%;
    height: 100%;
    background: url("../assets/images/list-bg.png") no-repeat;
    background-size: 100% 100%;
    display: flex;
    &.has-menu {
      .router-content,
      .second-menu {
        height: calc(100% - #{$headerHeight});
        margin-top: $headerHeight;
      }
    }
    .second-menu {
      width: $secondMenuWidth;
      min-width: $secondMenuWidth;
      height: 100%;
      position: relative;
      .icon-go-screen {
        position: absolute;
        bottom: 40px;
        left: 20px;
        cursor: pointer;
      }
      ul {
        height: 100%;
        overflow: auto;
        div {
          > li {
            cursor: pointer;
            > p {
              padding: 24px 0 24px 50px;
              display: flex;
              align-items: center;
              // justify-content: center;
              opacity: 0.64;
              font-family: PingFangSC-Regular;
              font-size: 18px;
              color: #ffffff;
              text-align: center;
              i {
                width: 16px;
                height: 16px;
                margin-right: 16px;
              }
              &.active {
                border-left: 5px solid $menuActiveBorderColor;
                color: $primaryListColor;
                span {
                  font-weight: bold;
                }
              }
              &:hover {
                color: $primaryListColor;
              }
            }

            .third-menu {
              > li {
                padding: 20px 0;
                opacity: 0.64;
                font-family: PingFangSC-Regular;
                font-size: 16px;
                color: #ffffff;
                text-align: center;
                &.active {
                  border-left: 5px solid $menuActiveBorderColor;
                  color: $primaryListColor;
                }
              }
            }
          }
        }
      }
    }
    .router-content {
      flex: 1;
      // width: calc(100% - #{$secondMenuWidth});
      height: 100%;
      //这里的padding影响到了别的模块的样式，放这里不合适 ---常家棋
      // padding: 25px;
      overflow: auto;
    }
  }
}
</style>
