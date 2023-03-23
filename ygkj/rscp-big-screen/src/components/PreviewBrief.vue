<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-20 10:22:38
 * @Description: 专题简报
 * @FilePath: /rscp-big-screen/src/components/PreviewBrief.vue
-->
<template>
  <div class='preview_bg' element-loading-background="rgba(0, 0, 0, .4)" @click="closeLookBriefPopup()">
    <div class="preview_box" @click.stop>
      <i class="el-icon-close" @click="closeLookBriefPopup()"></i>
      <div class="chart_title">
        <img src="../assets/images/chart_item_icon.png" alt="">
        专题简报
      </div>
      <div class="words_box">
        <p>截至{{year}}年{{month}}月，全省共有水库{{reservoirTabs[0].num}}座</p>
        <h3>一、按区域分</h3>
        <p>
          <span v-for="(item, index) in reservoirLeftAdcdData" :key="index">
            {{item.adnm}} {{item.num}}座
            <i v-if="index != reservoirLeftAdcdData.length - 1">、</i>
            <i v-else>。</i>
          </span>
        </p>
        <h3>二、按规模分</h3>
        <p>
          <span v-for="(item, index) in reservoirLeftScaleData" :key="index">
            {{item.name}} {{item.value}}座
            <i v-if="index != reservoirLeftScaleData.length - 1">、</i>
            <i v-else>。</i>
          </span>
        </p>
        <h3>三、区域库容</h3>
        <p>
          <span v-for="(item, index) in reservoirLeftKrData" :key="index">
            {{item.adnm}} {{((item.currenty / 10000).toFixed(1))}}亿m³
            <i v-if="index != reservoirLeftKrData.length - 1">、</i>
            <i v-else>。</i>
          </span>
        </p>
        <h3>四、实时预警情况</h3>
        <p>
          <span v-for="(item, index) in squareList" :key="index">
            {{item.name}} {{item.value}}座
            <i v-if="index != squareList.length - 1">、</i>
            <i v-else>。</i>
          </span>
        </p>
        <!-- <h3>五、实时预警分布情况</h3>
        <p>
          <span>杭州市</span>73座<i>、</i>
          <span>杭州市</span>73座<i>、</i>
          <span>杭州市</span>73座<i>。</i>
        </p> -->
      </div>
    </div>
  </div>
</template>

<script>

import { reservoirLeftStatisticApi } from '@/api/api_reservoir'
export default {
  name: 'PreviewBrief',
  props: {
  
  },
  components: {
    
  },
  computed: {
    reservoirLeftAdcdData(){
      return this.$store.state.reservoirLeftAdcdData
    },
    reservoirLeftScaleData(){
      return this.$store.state.reservoirLeftScaleData
    },
    reservoirLeftKrData(){
      return this.$store.state.reservoirLeftKrData
    },
    reservoirTabs(){
      return this.$store.state.reservoirTabs
    }
  },
  data () {
    return {
      year: new Date().format('yyyy'),
      month: new Date().format('MM'),
      squareList: []
    };
  },
  watch: {
    
  },
  mounted() {
    // console.log(this.reservoirLeftAdcdData,'----');
    this.getTideAlertData()
    // this.getLengthData()
  },
  methods: {
    // 实时预警统计
    async getTideAlertData(){
      let res = await reservoirLeftStatisticApi({
        statistic: 'overCnt',
        adcd: this.$localData('get', 'userInfo').adcd,
      })
      if(res.success){
        this.squareList = [
          {
            type: 'DSFLZ',
            name: '超设计水位',
            value: res.data[0]['DSFLZ'],
          },
          {
            type: 'NWL',
            name: '超正常水位',
            value: res.data[0]['NWL'],
          },
          {
            type: 'limitWaterLevel',
            name: '超汛限水位',
            value: res.data[0]['limitWaterLevel'],
          }
        ]
      }
    },
    closeLookBriefPopup(){
      this.$emit('closeLookBriefPopup')
    },
  }
}
</script>

<style lang='scss' scoped>
  .preview_bg{
    position: absolute;
    top: 0;
    left: 0;
    z-index: 1001; //大于地图
    width: 100%;
    height: 100%;
    background: rgba(0,0,0,.6);
    .el-icon-close{
      position: absolute;
      top: 10px;
      right: 10px;
      color: #999;
      font-size: 30px;
      cursor: pointer;
      z-index: 10;
    }
    .preview_box{
      width: 50%;
      height: 50%;
      position: absolute;
      top:50%;
      left:50%;
      transform: translate(-50%,-50%);
      border-radius: 4px;
      overflow: hidden;
      background: #fff;
      .chart_title{
        font-weight: bold;
        font-size: 18px;
      }
      .words_box{
        padding: 4px 16px;
        h3{
          font-weight: bold;
          font-size: 14px;
          color: #333;
          line-height: 36px;
          margin-top: 8px;
        }
        p{
          font-size: 14px;
          color: #666;
          line-height: 30px;
        }
      }
    }
  }
</style>