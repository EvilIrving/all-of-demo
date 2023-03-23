<template>
  <div class="home">
    <div class="layout_top">
      <div class="layout_top-title">鹿城区水利全域数字化</div>
      <div class="layout_top-first_menu">
        <el-menu :default-active="firstDefaultActive" class="el-menu-demo" mode="horizontal" @select="handleSelect">
          <el-menu-item :index="item.path" v-for="(item, index) in firstMenuList" :key="index">{{
            item.title
          }}</el-menu-item>
        </el-menu>
      </div>
      <div class="layout_top-person">
        <div class="line"></div>
        <div class="layout_top-img">
          <img @click="goDataManage" src="@/assets/images/data-manage.png" alt="" />
        </div>
        <div class="layout_top-img">
          <img src="@/assets/images/open_msg.png" alt="" />
        </div>
        <el-popover placement="bottom" width="60" trigger="hover">
          <div class="userMenu">
            <div class="out-info" @click="dialogPassVisible = true">
              修改密码
            </div>
            <div class="out-info" @click="logout()">退出登录</div>
          </div>
          <div slot="content"></div>
          <div class="person_head" slot="reference">
            <span :title="userName">{{ userName }}</span>
          </div>
        </el-popover>
      </div>
    </div>
    <div class="second_menu" v-if="showSecond">
      <div class="menu">
        <div
          class="menu_parent"
          v-for="item in secondMenuList"
          :key="item.name"
          :class="isShowMenu == item.path ? '' : 'hide'"
        >
          <div class="menu_root" :class="defaultActive == item.path ? 'active' : ''" @click="selectSecondMenu(item)">
            <img class="left_ico" :src="defaultActive == item.path ? item.icon_selected : item.icon" />
            <span>{{ item.title }}</span>
            <i
              v-if="item.children && item.children.length"
              class="el-icon-caret-bottom arrow"
              style="color: #999"
              :class="isShowMenu == item.path ? 'close' : ''"
              @click.stop="actionMenu(item)"
            ></i>
          </div>
          <template v-if="item.children && item.children.length">
            <div v-for="itemChild in item.children" :key="itemChild.name">
              <div
                class="menu_item"
                @click="selectThirdMenu(itemChild)"
                v-if="!itemChild.hide"
                :class="itemDefaultActive == itemChild.path ? 'active' : ''"
              >
                <span class="menu_item_word" :title="itemChild.title">
                  <span>
                    <i
                      v-if="
                        itemChild.children &&
                          itemChild.children.length &&
                          !itemChild.hideFourth &&
                          itemDefaultActive == itemChild.path
                      "
                      class="el-icon-caret-bottom"
                    ></i>
                    <i
                      v-if="
                        itemChild.children &&
                          itemChild.children.length &&
                          !itemChild.hideFourth &&
                          itemDefaultActive != itemChild.path
                      "
                      class="el-icon-caret-right"
                    ></i>
                  </span>
                  {{ itemChild.title }}
                </span>
              </div>
              <div
                v-if="
                  itemChild.children &&
                    itemChild.children.length &&
                    !itemChild.hideFourth &&
                    itemDefaultActive == itemChild.path
                "
              >
                <div v-for="ite in itemChild.children" :key="ite.name">
                  <span
                    class="four_menu_item"
                    :class="itemChildrenDefaultActive == ite.path ? 'active' : ''"
                    @click="selectFourthMenu(ite)"
                  >
                    {{ ite.title }}
                  </span>
                </div>
              </div>
            </div>
          </template>
        </div>
      </div>
      <div class="page_wrap">
        <div class="page_title">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>{{ pageTitle0 }}</el-breadcrumb-item>
            <el-breadcrumb-item v-if="pageTitle1 !== ''">{{ pageTitle1 }}</el-breadcrumb-item>
            <el-breadcrumb-item v-if="pageTitle2 !== ''">{{ pageTitle2 }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="page_box">
          <router-view class="page" />
        </div>
      </div>
    </div>
    <router-view v-else />

    <el-dialog title="修改密码" :visible.sync="dialogPassVisible" width="600px" custom-class="msg-dialog-box">
      <password-form v-if="dialogPassVisible" @closeEditDialog="dialogPassVisible = false"></password-form>
    </el-dialog>
  </div>
</template>

<script>
  import { logout } from '@/api/index';
  import { mapGetters } from 'vuex';
  import store from '@/store/index';
  import PasswordForm from '@/components/PasswordForm';
  export default {
    name: 'Home',
    props: {},
    data() {
      return {
        firstDefaultActive: '/intelligentWarning',
        firstMenuList: [],
        showSecond: false,
        secondMenuList: [],
        pageTitle0: '',
        pageTitle1: '',
        pageTitle2: '',
        menuSearch: '',
        userName: '',
        isShowMenu: '',
        defaultActive: '', // 二级菜单选中状态
        itemDefaultActive: '', // 三级菜单选中状态
        itemChildrenDefaultActive: '', // 四级菜单选中状态
        dialogPassVisible: false
      };
    },
    components: { PasswordForm },
    computed: {
      ...mapGetters(['allRoutesList'])
    },
    beforeCreate() {},
    created() {},
    watch: {
      $route: {
        handler: function(val) {
          this.firstDefaultActive = '/' + val.path.split('/')[1];
          // this.defaultActive = val.path;
          // 切换一级路由时，获取二级路由
          if (val.matched.length > 2) {
            for (let i = 0; i < this.firstMenuList.length; i++) {
              if (this.firstMenuList[i].path == val.redirectedFrom) {
                this.secondMenuList = this.firstMenuList[i].children;
                this.secondMenuList.map((v, i) => {
                  v.icon = require('@/assets/images/select.png');
                  v.icon_selected = require('@/assets/images/selected.png');
                  this.$set(this.secondMenuList, i, v);
                });
                if (this.firstMenuList[i].hideSecond) {
                  this.showSecond = false;
                } else {
                  this.showSecond = true;
                }
                if (this.secondMenuList.length && this.secondMenuList[0].children.length !== 0) {
                  this.selectSecondMenu(this.secondMenuList[0]);
                }
              }
            }
          } else {
            this.showSecond = false;
          }
        },
        deep: true
      }
    },
    mounted() {
      this.userName = this.$sessionData('get', 'userInfo').username || '游客';
      this.userType = this.$sessionData('get', 'userInfo').userType;
      this.firstDefaultActive = '/' + this.$route.path.split('/')[1];
      let routers = this.allRoutesList.filter((item) => item.name == 'home')[0];
      this.firstMenuList = routers.children;
      // 临时权限判断（物业人员and游客）
      // for (let i = 0; i < this.$router.options.routes.length; i++) {
      //   if (this.$router.options.routes[i].name == "home") {
      //     if (this.userType == "3") {
      //       //物业人员
      //       for (
      //         let j = 0;
      //         j < this.$router.options.routes[i].children.length;
      //         j++
      //       ) {
      //         if (
      //           this.$router.options.routes[i].children[j].title !==
      //             "水域占用审批" &&
      //           this.$router.options.routes[i].children[j].title !==
      //             "灾害防御调度" &&
      //           this.$router.options.routes[i].children[j].title !==
      //             "浙政钉应用" &&
      //           this.$router.options.routes[i].children[j].title !== "数字大屏"
      //         ) {
      //           if (
      //             this.$router.options.routes[i].children[j].title ==
      //             "海塘防潮评估"
      //           ) {
      //             this.$router.options.routes[i].children[j].children =
      //               this.$router.options.routes[i].children[j].children.slice(
      //                 0,
      //                 1
      //               );
      //           }
      //           this.firstMenuList.push(
      //             this.$router.options.routes[i].children[j]
      //           );
      //         }
      //       }
      //     } else {
      //       if (this.userName !== "游客") {
      //         //正常人员
      //         this.firstMenuList = this.$router.options.routes[i].children;
      //       } else {
      //         //游客
      //         for (
      //           let j = 0;
      //           j < this.$router.options.routes[i].children.length;
      //           j++
      //         ) {
      //           if (
      //             this.$router.options.routes[i].children[j].title !==
      //               "工程物业管理" &&
      //             this.$router.options.routes[i].children[j].title !==
      //               "水域占用审批" &&
      //             this.$router.options.routes[i].children[j].title !==
      //               "灾害防御调度" &&
      //             this.$router.options.routes[i].children[j].title !==
      //               "浙政钉应用"
      //           ) {
      //             this.firstMenuList.push(
      //               this.$router.options.routes[i].children[j]
      //             );
      //           }
      //         }
      //       }
      //     }
      //   }
      // }
      // 临时权限判断 end

      // 首次进入/刷新页面时，获取二级路由
      if (this.$route.matched.length > 2) {
        for (let i = 0; i < this.firstMenuList.length; i++) {
          if (this.firstMenuList[i].path == '/' + this.$route.path.split('/')[1]) {
            this.secondMenuList = this.firstMenuList[i].children;
            this.secondMenuList.map((v, i) => {
              v.icon = require('@/assets/images/select.png');
              v.icon_selected = require('@/assets/images/selected.png');
              this.$set(this.secondMenuList, i, v);
            });
            if (this.firstMenuList[i].hideSecond) {
              this.showSecond = false;
            } else {
              this.showSecond = true;
            }
            if (this.secondMenuList.length && this.secondMenuList[0].children.length !== 0) {
              this.selectSecondMenu(this.secondMenuList[1]);
            }
          }
        }
        this.pageTitle0 = this.secondMenuList.length ? this.secondMenuList[0].title : '';
        this.defaultActive = this.secondMenuList.length ? this.secondMenuList[0].path : '';
        this.isShowMenu = this.secondMenuList.length ? this.secondMenuList[0].path : '';
      } else {
        this.showSecond = false;
      }
    },
    methods: {
      handleSelect(key, keyPath) {
        this.secondMenuList = [];
        // 若点击数字大屏 直接跳转
        if (key == '/bigScreen') {
          window.open('https://slgh.lucheng.gov.cn/synthesizeSystem/');
          return;
        }
        // 若点击数据管理系统 直接跳转
        if (key == '/dataManage') {
          window.open('https://slgh.lucheng.gov.cn/dataManage/dataSheetManage');
          return;
        }
        this.$router.push(keyPath[0]);
        this.pageTitle1 = '';
        this.pageTitle2 = '';
        console.log(key);
        //切换一级目录时获取二级目录
        for (let i = 0; i < this.firstMenuList.length; i++) {
          if (this.firstMenuList[i].redirect) {
            if (this.firstMenuList[i].path == key) {
              this.secondMenuList = this.firstMenuList[i].children;
              this.pageTitle0 = this.firstMenuList[i].title;
            }
          }
        }
        this.defaultActive = this.secondMenuList.length ? this.secondMenuList[0].path : '';
        this.isShowMenu = this.secondMenuList.length ? this.secondMenuList[0].path : '';
        if (this.secondMenuList.length && this.secondMenuList[0].children.length !== 0) {
          // this.itemDefaultActive = this.secondMenuList[0].redirect;
          // this.selectSecondMenu(this.secondMenuList[0])
        } else {
          this.itemDefaultActive = '';
        }
      },

      selectSecondMenu(item) {
        if (item.name === 'none') {
          try {
            const { phone } = JSON.parse(sessionStorage.getItem('userInfo_obj'));
            window.open(`https://slgh.lucheng.gov.cn/waterSupervision/login?from=lc&phone=${phone}`);
          } catch (e) {
            //TODO
          }
          return;
        }
        this.pageTitle1 = '';
        this.pageTitle2 = '';
        this.defaultActive = item.path;
        this.isShowMenu = item.path;
        this.$router.push(item.path);
        this.pageTitle0 = item.title;
        if (item.children && item.children.length) {
          this.pageTitle1 = item.children[0].title;
          this.itemDefaultActive = item.redirect;
          if (item.children[0].children && item.children[0].children.length && !item.children[0].hideFourth) {
            this.pageTitle2 = item.children[0].children[0].title;
            this.itemChildrenDefaultActive = item.children[0].redirect;
          }
        }
      },

      selectThirdMenu(item) {
        this.pageTitle2 = '';
        this.itemDefaultActive = item.path;
        this.$router.push(item.path);
        this.pageTitle1 = item.title;
        if (item.children && item.children.length && !item.hideFourth) {
          this.pageTitle2 = item.children[0].title;
          this.itemChildrenDefaultActive = item.redirect;
        }
      },

      selectFourthMenu(item) {
        this.itemChildrenDefaultActive = item.path;
        let path = item.routeType ? item.fullPath : item.path;
        this.$router.push(path);
        this.pageTitle2 = item.title;
      },
      goDataManage() {
        window.open('https://slgh.lucheng.gov.cn/dataManage/dataSheetManage');
      },
      actionMenu(item) {
        if (this.isShowMenu == item.path) {
          //如果已展开那么收缩
          this.isShowMenu = '';
        } else {
          this.isShowMenu = item.path;
        }
      },
      async logout() {
        let res = await logout({
          accessToken: this.$sessionData('get', 'userInfo').accessToken,
          refreshToken: this.$sessionData('get', 'userInfo').refreshToken
        });
        if (res.code == 0) {
          store.commit('ADD_ROUTES', []);
          this.$sessionData('clean', 'userInfo');
          this.$router.push('/login');
          window.location.reload();
        }
      }
    }
  };
</script>

<style lang="scss" scoped>
  .home {
    width: 100%;
    height: 100%;
  }
</style>

<style lang="scss">
  .out-info {
    padding: 0 10px 10px;
    cursor: pointer;
    &:last-child {
      padding-bottom: 0;
    }
  }
</style>
