<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-26 20:00:25
 * @Description: 地图左侧-统计
 * @FilePath: /rscp-big-screen/src/views/sgzgScreenNew/reservoirControl/reservoirControlLeft/ReservoirControlLeftNew.vue
-->
<template>
  <div class="map_left" >
    <div class="sgzg_title">
      <div class="sgzg_title_name">
        运行预警
      </div>
      <img src="@/assets/images/qtj-arr.png" alt="" />
    </div>
    <div class="flexbox">

      <div class="left_box left_top">
        <div class="select_square" style="height:100%">
          <div class='square_item'> 
            <div class="square_center_box">
              <div class="square_img_box">
                <div class="square_img">
                  <img style="width:56px;height:56px;margin-bottom:20px;" class="square_img_center" src="../../../../assets/images/csj.png" alt=""/>
                </div>
              </div>
              <div class="square_bottom">
                <span style="color: #FF3E09">0<i>座</i></span>
              </div>
            </div>
          </div>
          <div class='square_item'> 
            <div class="square_center_box">
              <div class="square_img_box">
                <div class="square_img">
                  <img style="width:56px;height:56px;margin-bottom:20px;" class="square_img_center" src="../../../../assets/images/czc.png" alt=""/>
                </div>
              </div>
              <div class="square_bottom">
                <span style="color: #FF7509">0<i>座</i></span>
              </div>
            </div>
          </div>
          <div class='square_item'> 
            <div class="square_center_box">
              <div class="square_img_box">
                <div class="square_img">
                  <img style="width:56px;height:56px;margin-bottom:20px;" class="square_img_center" src="../../../../assets/images/cxz.png" alt=""/>
                </div>
              </div>
              <div class="square_bottom">
                <span style="color: #FF9D09">0<i>座</i></span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="left_box left_bottom">
        <div class="second_title_2">
          <div class="name">
            <p>正常坝<span>(一类坝)</span></p>
          </div>
          <!-- <div class="solid"></div> -->
          <!-- <div class="dashed"><div></div><div></div></div> -->
        </div>
        <div class="select_square">
          <div class='square_item'> 
            <div class="square_center_box">
              <div class="square_img_box">
                <div class="square_img">
                  <img class="square_img_center" src="../../../../assets/images/csj.png" alt=""/>
                </div>
              </div>
              <div class="square_bottom">
                <p>超设计水位</p>
                <span style="color: #FF3E09">0<i>座</i></span>
              </div>
            </div>
          </div>
          <div class='square_item'> 
            <div class="square_center_box">
              <div class="square_img_box">
                <div class="square_img">
                  <img class="square_img_center" src="../../../../assets/images/czc.png" alt=""/>
                </div>
              </div>
              <div class="square_bottom">
                <p>超正常水位</p>
                <span style="color: #FF7509">0<i>座</i></span>
              </div>
            </div>
          </div>
          <div class='square_item'> 
            <div class="square_center_box">
              <div class="square_img_box">
                <div class="square_img">
                  <img class="square_img_center" src="../../../../assets/images/cxz.png" alt=""/>
                </div>
              </div>
              <div class="square_bottom">
                <p>超限制水位</p>
                <span style="color: #FF9D09">0<i>座</i></span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="left_box left_bottom">
        <div class="second_title_2">
          <div class="name">
            <p>病害坝<span>(二类坝)</span></p>
          </div>
          <!-- <div class="solid"></div> -->
          <!-- <div class="dashed"><div></div><div></div></div> -->
        </div>
        <div class="select_square">
          <div class='square_item'> 
            <div class="square_center_box">
              <div class="square_img_box">
                <div class="square_img">
                  <img class="square_img_center" src="../../../../assets/images/csj.png" alt=""/>
                </div>
              </div>
              <div class="square_bottom">
                <p>超设计水位</p>
                <span style="color: #FF3E09">0<i>座</i></span>
              </div>
            </div>
          </div>
          <div class='square_item'> 
            <div class="square_center_box">
              <div class="square_img_box">
                <div class="square_img">
                  <img class="square_img_center" src="../../../../assets/images/czc.png" alt=""/>
                </div>
              </div>
              <div class="square_bottom">
                <p>超正常水位</p>
                <span style="color: #FF7509">0<i>座</i></span>
              </div>
            </div>
          </div>
          <div class='square_item'> 
            <div class="square_center_box">
              <div class="square_img_box">
                <div class="square_img">
                  <img class="square_img_center" src="../../../../assets/images/cxz.png" alt=""/>
                </div>
              </div>
              <div class="square_bottom">
                <p>超限制水位</p>
                <span style="color: #FF9D09">0<i>座</i></span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="left_box left_bottom">
        <div class="second_title_2">
          <div class="name">
            <p>危险坝<span>(三类坝)</span></p>
          </div>
          <!-- <div class="solid"></div> -->
          <!-- <div class="dashed"><div></div><div></div></div> -->
        </div>
        <div class="select_square">
          <div class='square_item'> 
            <div class="square_center_box">
              <div class="square_img_box">
                <div class="square_img">
                  <img class="square_img_center" src="../../../../assets/images/csj.png" alt=""/>
                </div>
              </div>
              <div class="square_bottom">
                <p>超设计水位</p>
                <span style="color: #FF3E09">0<i>座</i></span>
              </div>
            </div>
          </div>
          <div class='square_item'> 
            <div class="square_center_box">
              <div class="square_img_box">
                <div class="square_img">
                  <img class="square_img_center" src="../../../../assets/images/czc.png" alt=""/>
                </div>
              </div>
              <div class="square_bottom">
                <p>超正常水位</p>
                <span style="color: #FF7509">0<i>座</i></span>
              </div>
            </div>
          </div>
          <div class='square_item'> 
            <div class="square_center_box">
              <div class="square_img_box">
                <div class="square_img">
                  <img class="square_img_center" src="../../../../assets/images/cxz.png" alt=""/>
                </div>
              </div>
              <div class="square_bottom">
                <p>超限制水位</p>
                <span style="color: #FF9D09">0<i>座</i></span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
export default {
  name: "",
  props: {},
  components: {
  },
  data() {
    return {
    };
  },
  
  mounted() {
    
  },
  methods: {
   
  }
};
</script>

<style lang="scss" scoped>
.map_left {
  height: 100%;
  z-index: 1002;
  .flexbox{
    height: calc(100% - 48px);
    @include flexbox();
    flex-direction: column;
    box-sizing: border-box;
    &>div{
      &.left_box{
        &.left_top{
          @include flex(.8);
        }
        &.left_bottom{
          @include flex(1);
          
        }
        height: 100%;
        .select_square {
          height: calc(100% - 42px);
          box-sizing: border-box;
          padding: 0 16px 16px 16px;
          @include flexbox();
          @include flexAI(center);
          @include flexJC(space-between);
          .square_item {
            @include flexbox();
            @include flexAI(center);
            @include flexJC(center);
            box-sizing: border-box;
            width: 140px;
            height: 80%;
            text-align: center;
            background-color: #F8F8F8;
            background-image: url('../../../../assets/images/c-bg.png');
            background-repeat: no-repeat;
            background-position: right bottom;
            background-size: 40% 40%;
            border: 1px solid #FF9D09;
            cursor: pointer;
            border-radius: 4px;
            // margin-bottom: 8px;
            &:nth-child(2){
              &.active {
                background: #FF3E09 !important;
                border: 1px solid #FF3E09 !important;
              }
            }
            &:nth-child(3){
              &.active {
                background: #FF7509 !important;
                border: 1px solid #FF7509 !important;
              }
            }
            &:nth-child(4){
              &.active {
                background: #FF9D09 !important;
                border: 1px solid #FF9D09 !important;
              }
            }
            &.active {
              .square_bottom {
                & > p {
                  color: #fff !important;
                }
                & > span {
                  color: #fff !important;
                }
                i {
                  color: #fff !important;
                }
              }
            }
            .square_center_box{
              .square_img_box {
                padding-top: 5px;
                .square_img {
                  margin: 0 auto;
                  position: relative;
                  .square_img_center {
                    width: 48px;
                    height: 48px;
                  }
                }
              }
              .square_bottom {
                & > p {
                  line-height: 24px;
                  font-size: 18px;
                  color: #000;
                  padding: 12px 0 8px 0;
                }
                i {
                  padding-left: 4px;
                  font-size: 16px;
                  font-weight: 400;
                  color: #333333;
                  line-height: 24px;
                }
                & > span{
                  font-size: 34px;
                  font-weight: 500;
                  color: #FF7509;
                  line-height: 24px;
                }
              
              }
            }
            
          }
        }
      }
    }
  }
  .second_title_2{
    @include flexbox();
    @include flexAI(center);
    padding: 16px;
    .name{
      width: 150px;
      box-sizing: border-box;
      padding-left: 6px;
      border-left: 4px solid #2D58FF;
      border-right: 4px solid #2D58FF;
      p{
        font-size: 18px;
        font-weight: 600;
        color: #2D58FF;
        span{
          font-size: 18px;
          color: #999;
          font-weight: 600;
        }
      }
    }
    .solid{
      width: calc(100% - 150px);
      height: 2px;
      background: #2D58FF;
    }
    .dashed{
      width: 22px;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(space-between);
      &>div{
        height: 2px;
        width: 8px;
        background: #2D58FF;
        margin-left: 4px;
      }
    }
  }
}
</style>
