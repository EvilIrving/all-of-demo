<!--
 * @Date: 2022-08-24 16:34:28
 * @Author: 
 * @Description: 智慧工程 驾驶舱页面
 * @LastEditors: dtb
 * @LastEditTime: 2022-08-24 16:39:19
 * @FilePath: \rscp-big-screen\src\views\sgzgOtherScreen\Menu.vue
-->

<template>
  <div class="drive_wrap">
    <div class="drive_box">
      <div class="drive_box_left">
        <div class="drive_item" v-for="(item, index) in tabDataLeft" :key="index" @mouseover="mouseOverLeft(index)" @mouseleave="mouseLeaveLeft(index)" @click="selectMenu(item)">
          <img v-if="item.isSelect" :src="item.selectedUrl" alt="">
          <img v-else :src="item.selectUrl" alt="">
        </div>
      </div>
      <div class="drive_box_middle">
        <div class="middle_title"></div>
        <div class="middle_map"></div>
        <div class="middle_btn" @click="goBasinScreen()"></div>
      </div>
      <div class="drive_box_right">
        <div class="drive_item" v-for="(item, index) in tabDataRight" :key="index" @mouseover="mouseOverRight(index)" @mouseleave="mouseLeaveRight(index)" @click="selectMenu(item)">
          <img v-if="item.isSelect" :src="item.selectedUrl" alt="">
          <img v-else :src="item.selectUrl" alt="">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Intor',
  props: {
    
  },
  components: {
    
  },
  data () {
    return {
      phone: this.$localData("get", "userInfo").phone,
      tabDataLeft: [
        {
          name: '水库风险',
          selectUrl: require('../../assets/images/menu/sk0.png'),
          selectedUrl: require('../../assets/images/menu/sk1.png'),
          path: '/skzgScreen',
          canGo: true, //非管理员是否才能看
          isSelect: false
        },
        {
          name: '堤防工程',
          selectUrl: require('../../assets/images/menu/df0.png'),
          selectedUrl: require('../../assets/images/menu/df1.png'),
          path: '/skzgDikeScreen',
          canGo: false,
          isSelect: false
        },
        {
          name: '泵站工程',
          selectUrl: require('../../assets/images/menu/bz0.png'),
          selectedUrl: require('../../assets/images/menu/bz1.png'),
          path: '/skzgPumpScreen',
          canGo: false,
          isSelect: false
        }
      ],
      tabDataRight: [
        {
          name: '海塘防潮',
          selectUrl: require('../../assets/images/menu/ht0.png'),
          selectedUrl: require('../../assets/images/menu/ht1.png'),
          path: '/skzgSeawallScreen',
          canGo: true,
          isSelect: false
        },
        {
          name: '水闸工程',
          selectUrl: require('../../assets/images/menu/sz0.png'),
          selectedUrl: require('../../assets/images/menu/sz1.png'),
          path: '/skzgSluiceScreen',
          canGo: false,
          isSelect: false
        },
        {
          name: '闸站工程',
          selectUrl: require('../../assets/images/menu/zz0.png'),
          selectedUrl: require('../../assets/images/menu/zz1.png'),
          path: '/skzgGateScreen',
          canGo: false,
          isSelect: false
        }
      ]
    };
  },
  created(){
    
  },
  methods: {
    mouseOverLeft(index){
      this.tabDataLeft[index].isSelect = true
      this.$set(this.tabDataLeft, index, this.tabDataLeft[index])
    },
    mouseLeaveLeft(index){
      this.tabDataLeft[index].isSelect = false
      this.$set(this.tabDataLeft, index, this.tabDataLeft[index])
    },
    mouseOverRight(index){
      this.tabDataRight[index].isSelect = true
      this.$set(this.tabDataRight, index, this.tabDataRight[index])
    },
    mouseLeaveRight(index){
      this.tabDataRight[index].isSelect = false
      this.$set(this.tabDataRight, index, this.tabDataRight[index])
    },
    selectMenu(item){
      // if(this.phone == '张菲' || this.phone == '13958108695' || this.phone == '15988133616'){
      //   if(item.path){
          this.$router.push(item.path)
      //   }else{
      //     this.$message("功能开发中，敬请期待！")
      //   }
      // }else{
      //   if(item.canGo){
      //     this.$router.push(item.path)
      //   }else{
      //     this.$message("功能开发中，敬请期待！")
      //   }
      // }
    },
    goBasinScreen(){
      // if(this.phone == '张菲' || this.phone == '13958108695' || this.phone == '15988133616'){
        this.$router.push('/basinScreen')
      // }else{
      //   this.$message("功能开发中，敬请期待！")
      // }
    }
  }
}
</script>

<style lang='scss' scoped>
.drive_wrap{
  width: 100%;
  height: 100%;
  @include flexbox();
  @include flexAI(center);
  @include flexJC(center);
  background: #1B3C74 url('../../assets/images/menu/bg.png') no-repeat center;
  background-size: 100% 100%;
  box-sizing: border-box;
  padding: 55px 21px 15px 21px;
  .drive_box{
    @include flexbox();
    @include flexJC(space-between);
    width: 100%;
    height: 100%;
    background: url('../../assets/images/menu/bg_box.png') no-repeat center;
    background-size: 100% 100%;
    box-sizing: border-box;
    padding: 0 90px;
    .drive_box_left{
      width: 371px;
      margin-top: 100px;
    }
    .drive_box_middle{
      width: calc(100% - 742px);
      position: relative;
      .middle_title{
        width: 872px;
        height: 96px;
        margin: 20px auto 0 auto;
        background: url('../../assets/images/menu/title.png') no-repeat center;
        background-size: 100% 100%;
      }
      .middle_map{
        width: 983px;
        height: 824px;
        margin: 0 auto;
        background: url('../../assets/images/menu/map.png') no-repeat center;
        background-size: 100% 100%;
      }
      .middle_btn{
        width: 480px;
        height: 137px;
        text-align: center;
        position: absolute;
        left: 50%;
        margin-left: -240px;
        bottom: 110px;
        background: url('../../assets/images/menu/zonghe.png') no-repeat center;
        background-size: 100% 100%;
        cursor: pointer;
      }
    }
    .drive_box_right{
      width: 371px;
      margin-top: 100px;
    }
    .drive_item{
      width: 100%;
      cursor: pointer;
      img{
        width: 375px;
        height: 257px;
      }
    }
  }
}
  .loading_bg{
    width: 100%;
    height: 100%;
    background: url('../../assets/images/first_bg.jpg') no-repeat center;
    background-size: cover;
    position: relative;
    p{
      position: absolute;
      bottom: 200px;
      width: 100%;
      text-align: center;
      color: #fff;
      font-size: 22px;
    }
    .btn_box{
      width: 100%;
      position: absolute;
      bottom: 180px;
      // left: 50%;
      // margin-left: -200px;
      text-align: center;
      @include flexbox();
      @include flexJC(center);
      .button{
        background: transparent;
        width: 180px;
        height: 60px;
        display: inline-block;
        margin: 0 20px;
        border-radius: 4px;
        text-align: center;
        line-height: 60px;
        font-size: 32px;
        color: #fff;
        border: 1px solid #eee;
        box-sizing: border-box;
        cursor: pointer;
        letter-spacing: 4px;
        &:hover{
          // background: #d4e0fa;
          background: #fff;
          border: 1px solid #89adfa;
          color: #2467f6;
        }
      }
    }
  }
</style>