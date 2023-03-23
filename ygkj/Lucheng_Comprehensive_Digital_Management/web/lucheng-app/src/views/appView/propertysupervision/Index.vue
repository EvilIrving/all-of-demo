<!--
 * @Date: 2022-06-08 14:22:55
 * @Author: 
 * @Description: 督办首页tab切换
 * @LastEditors: dtb
 * @LastEditTime: 2022-06-22 16:27:14
 * @FilePath: \lucheng-app\src\views\appView\propertysupervision\Index.vue
-->

<template>
<div class="wuye_wrap">
  <van-nav-bar fixed placeholder safe-area-inset-top :title="'物业督办'" :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
  <div class="maintenance" style="padding-top: 21px">
    <div class="title">
      <span>物业督办</span>
    </div>
    <div class="content">
      <van-grid :gutter="2" direction="horizontal" center>
        <van-grid-item @click="gotoPgae('/propertySupervision/inspection')">
          <div class="content_left">
            <span class="left_title">督查检查</span>
            <span class="num">{{countInfo.inspectNum}}个</span>
          </div>
          <img :src="imgsrc.relayTask"/>
        </van-grid-item>
        <van-grid-item @click="gotoPgae('/propertySupervision/supervise')">
          <div class="content_left">
            <span class="left_title">督查督办</span>
            <span class="num">{{countInfo.superviseNum}}个</span>
          </div>
          <img :src="imgsrc.businessApproval"/>
        </van-grid-item>
        <van-grid-item style="margin-top: 15px" @click="gotoPgae('/propertySupervision/schedule')">
          <div class="content_left">
            <span class="left_title">进度查询</span>
            <span class="num">{{countInfo.progressNum}}个</span>
          </div>
          <img :src="imgsrc.progressQuery"/>
        </van-grid-item>
        <van-grid-item style="margin-top: 15px" @click="gotoPgae('/propertySupervision/archive')">
          <div class="content_left">
            <span class="left_title">归档成果</span>
            <span class="num">{{countInfo.filedNum}}个</span>
          </div>
          <img :src="imgsrc.acceptanceApproval"/>
        </van-grid-item>
      </van-grid>
    </div>
  </div>
</div>
</template>

<script>
import { superviseApi } from "@/api/rainfall";
export default {
  name: "Index",
  props:{},
  components:{},
  data(){
    return{
      imgsrc:{
        relayTask:require("@/assets/images/officeInformation/relay_task.png"),
        businessApproval:require("@/assets/images/officeInformation/business_approval.png"),
        progressQuery:require("@/assets/images/officeInformation/progress_qiery.png"),
        acceptanceApplication:require("@/assets/images/officeInformation/acceptanceApplication.png"),
        acceptanceApproval:require("@/assets/images/officeInformation/acception_approval.png")

      },
      countInfo: {}
    }
  },
  mounted() {
    this.addZwlog(this)
    this.getCounts()
  },
  methods:{
    gotoPgae(path,type){
      this.$router.push(path)
    },
    async getCounts() {
      let res = await superviseApi.counts()
      if (res.code == 0) {
        this.countInfo = res.data
      }
    }
  }
}
</script>

<style scoped lang="scss">
.wuye_wrap{
  height: 80%;
  width: 100%;
  background-color: #ffffff;
  .title{
    width: 90px;
    font-size: 15px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 250;
    color: #000000;
    line-height: 21px;
    margin-left: 1.5px;
    //margin-top: 21px;
    text-align: center;
  }
  .content{
    margin-top: 18px;
    padding: 0 16px;
    .content_left{
      width: 60px;
      .left_title{
        font-size: 15px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 250;
        color: #333333;
        line-height: 21px;
      }
      .num{
        font-size: 13px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 200;
        color: #666666;
        line-height: 18px;
        margin-top: 4.5px;
      }
    }
    img{
      width: 33px;
      height: 33px;
      margin-left: 34px;
    }
  }

}
</style>
<style lang="scss">
.wuye_wrap{
.content{
  .van-grid-item__content{
    width: 165px;
    background: #F8F8F7;
    border-radius: 10px;
  }
}
}
</style>
