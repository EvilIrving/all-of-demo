<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-27 09:15:22
 * @Description: 水工智管框架
 * @FilePath: \rscp-big-screen\src\views\sgzgScreenNew\SgzgMenu.vue
-->
<template>
  <div class='wrap'>
    <div class="screen_menu_wrap" v-if="hasTop">
      <img class="title" src="../../assets/images/skzg.png" @click="goBack()">
      <ul class="screen_menu">
        <li :class="tabIndex == index ? 'active' : ''" v-for="(item, index) in tabData" :key="item.name" @click="selectTab(index)">
          <p>{{item.name}}</p>
        </li>
      </ul>
    </div>
    <router-view class="screen_wrap" />
  </div>
</template>

<script>
import { getQueryString } from '@/utils/utils'
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
          name: '浙库户籍',
          path: '/zhekuHousehold',
        },
        {
          name: '健康在线',
          path: '/zhekuHealthy',
        },
        {
          name: '蓄供分析',
          path: '/reservoirStorage',
        },
        {
          name: '智慧控运',
          path: '/reservoirControl',
        },
        {
          name: '精准维养',
          path: '/repairProtect',
        },
        {
          name: '浙库效益',
          path: '/reservoirBenefit',
        }
      ],
      hasTop: true
    };
  },
  created(){
    
  },
  mounted() {
    if(this.$route.query.pathIndex){
      let pathIndex = Number(this.$route.query.pathIndex)
      this.$router.push(this.tabData[pathIndex].path + '?hasTop=' + getQueryString('hasTop'))
    }
    // this.tabIndex = this.$route.query.pathIndex
    this.tabIndex = this.tabData.findIndex(item => { return this.$route.path == item.path })
    // console.log(this.tabIndex,'this.tabInde');
    if (getQueryString('hasTop') == '2') {
      this.hasTop = false
    } else {
      this.hasTop = true
    }
  },
  watch:{
    $route(val){
      this.tabIndex = this.tabData.findIndex(item => { return this.$route.path == item.path })
    }
  },
  methods: {
    selectTab(index) {
      //重置
      this.$store.commit("SET_LEFT_SELECT_PROJECT_SCALE", "");
      this.$store.commit("SET_LEFT_SELECT_ADCD", '');
      this.$store.commit("SET_LEFT_SELECT_ADNM", "浙江省");
      //end
      this.tabIndex = index
      this.$router.push(this.tabData[index].path + '?hasTop=' + getQueryString('hasTop'))
    },
    goBack(){
      this.$router.push('/sgzgDeive?hasTop=' + getQueryString('hasTop'))
    }
  }
}
</script>

<style lang='scss' scoped>
.wrap {
  width: 100%;
  height: 100%;
  position: relative;
  background: #fff;
  .screen_menu_wrap {
    z-index: 999;
    width: 100%;
    height: 116px;
    // position: relative;
    position: absolute;
    top: 0;
    left: 0;
    background: url("../../assets/images/sgzg_top.png") no-repeat center;
    background-size: 100% 100%;
    .title {
      cursor: pointer;
      width: 264px;
      margin: 20px 0 0 70px;
    }
    .screen_menu {
      @include flexbox();
      position: absolute;
      left: 507px;
      top: 32px;
      & > li {
        height: 43px;
        line-height: 43px;
        padding: 0 30px;
        text-align: center;
        color: #fff;
        font-size: 20px;
        cursor: pointer;
        transform: skewX(-47deg);
        p {
          font-weight: 600;
          transform: skewX(47deg);
        }
        &.active {
          background: #1e40bd;
        }
        &:hover {
          background: #4267eb;
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
<style lang='scss'>
.left_title {
  width: 100%;
  height: 35px;
  span {
    font: bold 20px italic;
    vertical-align: center;
    margin-left: 30px;
    line-height: 35px;
  }
  background: left/96% no-repeat url("../../assets/images/menu-bg.png");
}
</style>