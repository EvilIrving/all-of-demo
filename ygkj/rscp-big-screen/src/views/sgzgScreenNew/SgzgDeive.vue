<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-26 18:35:56
 * @Description: 水工智管驾驶舱
 * @FilePath: /rscp-big-screen/src/views/sgzgScreenNew/SgzgDeive.vue
-->
<template>
  <div class="drive_wrap">
    <div class="drive_top">
      <div class="drive_top_title"></div>

    </div>
    <div class="drive_bottom">
      <div class="drive_bottom_btn" @click="selectMenu(0)"></div>
    </div>
    <div class="drive_box">
      <div class="drive_box_left">
        <div class="drive_item" @click="selectMenu(0)">
          <img src="../../assets/images/sg1.png" alt="">
          <!-- <div class="item_box">
            <div class="status_right">
              <p>水库数量</p>
              <span>4296<i>座</i></span>
            </div>
            <div class="status_right">
              <p>水利部门注册</p>
              <span>4279<i>座</i></span>
            </div>
            <div class="status_right">
              <p>水库数量</p>
              <span>17<i>座</i></span>
            </div>
          </div> -->
        </div>
        <div class="drive_item" @click="selectMenu(1)">
          <img src="../../assets/images/sg2.png" alt="">
          <!-- <div class="item_box" style="top:120px">
            <div class="status_right">
              <p>正常坝
                <span>(一类坝)</span>
              </p>
              <span>3478<i>座</i></span>
            </div>
            <div class="status_right">
              <p>病害坝
                <span>(二类坝)</span>
              </p>
              <span>484<i>座</i></span>
            </div>
            <div class="status_right">
              <p>危险坝
                <span>(三类坝)</span>
              </p>
              <span>138<i>座</i></span>
            </div>
          </div> -->
        </div>
        <div class="drive_item" @click="selectMenu(4)">
          <img src="../../assets/images/sg3.png" alt="">
          <!-- <div class="item_box" style="top:100px;padding: 0 50px;">
            <div class="status_right">
              <p>精准维养项
              </p>
              <span style="display: block;margin-bottom:16px;">1426<i>项</i></span>
              <span style="display: block;">2103<i>万元</i></span>
            </div>
            <div class="status_right">
              <p>已完成维养项
              </p>
              <span style="display: block;margin-bottom:16px;">1230<i>项</i></span>
            </div>
          </div> -->
        </div>
      </div>
      <div class="drive_box_middle">
        <div class="middle_map"></div>
      </div>
      <div class="drive_box_right">
        <div class="drive_item" @click="selectMenu(2)">
          <img src="../../assets/images/sg4.png" alt="">
        </div>
        <div class="drive_item" @click="selectMenu(3)">
          <img src="../../assets/images/sg5.png" alt="">
          <!-- <div class="item_box" style="top:90px;padding: 0 50px;">
            <div class="status_right">
              <p>防洪(溢洪)
              </p>
              <span>26<i>座</i></span>
            </div>
            <div class="status_right">
              <p>防洪(泄洪)
              </p>
              <span>283<i>座</i></span>
            </div>
            <div class="status_right">
              <p>发电
              </p>
              <span>98<i>座</i></span>
            </div>
          </div>
          <div class="item_box" style="top:170px;padding: 0 50px;">
            <div class="status_right">
              <p>供水
              </p>
              <span>76<i>座</i></span>
            </div>
            <div class="status_right">
              <p>灌溉
              </p>
              <span>108<i>座</i></span>
            </div>
            <div class="status_right">
              <p>生态
              </p>
              <span>27<i>座</i></span>
            </div>
          </div> -->
        </div>
        <div class="drive_item" @click="selectMenu(5)">
          <img src="../../assets/images/sg6.png" alt="">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getQueryString } from '@/utils/utils'
export default {
  name: 'Intor',
  props: {
    
  },
  components: {
    
  },
  data () {
    return {
    };
  },
  mounted(){
    
    
  },
  methods: {
    selectMenu(pathIndex){
      this.$router.push('/sgzgMenuNew' + '?hasTop=' + getQueryString('hasTop') + '&pathIndex=' + pathIndex)
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
  background: url("../../assets/images/sgzg-bg.png") no-repeat center;
  box-sizing: border-box;
  position: relative;
  .drive_top{
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 118px;
    background: url("../../assets/images/sgzg-bg-top.png") no-repeat center;
    background-size: 100% 100%;
    .drive_top_title{
      margin: 0 auto;
      position: relative;
      bottom: 35px;
      width: 533px;
      height: 155px;
      background: url("../../assets/images/sgzg-title.png") no-repeat center;
      background-size: 100% auto;
    }
  }
  .drive_bottom{
    z-index: 99;
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 124px;
    background: url("../../assets/images/sgzg-bg-bottom.png") no-repeat center;
    background-size: 100% 100%;
    .drive_bottom_btn{
      cursor: pointer;
      margin: 0 auto;
      position: relative;
      bottom: 33px;
      width: 480px;
      height: 155px;
      background: url("../../assets/images/sgzg-bottom-btn.png") no-repeat center;
      background-size: 100% auto;
    }
  }
  .drive_box{
    @include flexbox();
    @include flexJC(space-between);
    width: 100%;
    height: 100%;
    background-size: 100% 100%;
    box-sizing: border-box;
    padding: 0 90px;
    .drive_box_left{
      width: 371px;
      margin-top: 70px;
    }
    .drive_box_middle{
      width: calc(100% - 742px);
      position: relative;
      .middle_map{
        margin-top: 110px;
        width: 1020px;
        height: 957px;
        // margin: 0 auto;
        background: url('../../assets/images/sgzg-map.png') no-repeat center;
        background-size: 100% 100%;
      }
    }
    .drive_box_right{
      width: 371px;
      margin-top: 70px;
    }
    .drive_item{
      width: 100%;
      width: 371px;
      height: 300px;
      cursor: pointer;
      position: relative;
      img{
        width: 100%;
        height: 100%;
        position: absolute;
        z-index: 9;
      }
      .item_box{
        width: 100%;
        box-sizing: border-box;
        position: absolute;
        z-index: 9999;
        top: 140px;
        padding: 0 32px;
        color: #fff;
        @include flexbox();
        @include flexJC(space-between);
        .status_right {
          & > p {
            line-height: 18px;
            font-size: 18px;
            color: #fff;
            padding: 24px 0 16px 0;
            &>span{
              display: block;
              font-size: 14px;
              padding-top: 5px;
            }
          }
          
          & > span {
            // line-height: 24px;
            font-size: 28px;
            color: #fff;
            font-weight: 500;
            i {
              font-size: 16px;
              padding-left: 4px;
              color: #fff;
            }
          }
        }
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