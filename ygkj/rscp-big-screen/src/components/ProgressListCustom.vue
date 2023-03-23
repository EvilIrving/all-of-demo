<!--
 * @Author: hanyu
 * @LastEditTime: 2021-08-09 17:46:49
 * @Description: 进度条列表
 * @FilePath: /rscp-big-screen/src/components/ProgressListCustom.vue
-->
<template>
  <div class="progress_wrap">
    <div class="chart_title">
      <img src="../assets/images/chart_item_icon.png" alt="">
      {{title}}
      <span v-if="unit" class="right unit">{{unit}}</span>
    </div>
    <div class="progress_box" v-if="progressData.length">
      <div class="progress_item" v-for="(item, index) in progressData" :key="index">
        <p>{{item.title}}</p>
        <div class="progress_bg">
          <div class="progress_content" :style="{ width: item.percentage ? item.percentage + '%' : 0 }">
            <span v-if="item.percentage > 10" class="progress_fz1"> {{ item.percentage ? item.allNum : ''}}</span>
            <span v-else class="progress_fz2"> {{ item.percentage ? item.allNum : ''}}</span>
          </div>
        </div>
        <!-- <el-progress :percentage="item.percentage"></el-progress> -->
        <span>{{item.nowNum != 0 ? item.nowNum : '有误'}}</span>
      </div>
    </div>
    <div v-else class="noData">
      <img src="../assets/images/noData.png" alt="" />
    </div>
  </div>
</template>

<script>
export default {
  name: '',
  props: {
    title: {
      type: String,
      required: true
    },
    unit: {
      type: String,
      default: ''
    },
    height: {
      type: String,
      default: '300px'
    },
    progressData: {
      type: Array,
      required: true,
      default: [
        {
          title: '市本级',
          allNum: 200,
          nowNum: 120,
          percentage: 60
        }
      ]
    }
  },
  components: {
    
  },
  data () {
    return {
      
    };
  },
  mounted() {
    // this.progressData[1].percentage = 10
  },
  methods: {
    
  }
}
</script>

<style lang='scss' scoped>
.progress_wrap{
  // background: #6F9DF2;
  height: 100%;
  width: 100%;
}
.progress_box{
  height: calc(100% - 42px);
  box-sizing: border-box;
  padding: 0 16px;
  overflow: auto;
  width: 100%;
  .progress_item{
    width: 100%;
    box-sizing: border-box;
    padding: 3px 16px;
    background: #F8F8F8;
    @include flexbox();
    @include flexAC();
    @include flexJC(space-between);
    .progress_bg{
      @include flex(4);
      position: relative;
      background: #e3e8f5;
      height: 14px;
      border-radius: 7px;
      .progress_content{
        height: 14px;
        border-radius: 7px;
        position: absolute;
        left: 0;
        top: 0;
        background: #6F9DF2;
        color: #fff;
        .progress_fz1{
          position: absolute;
          right: 5px;
          line-height: 14px;
        }
        .progress_fz2{
          position: absolute;
          right: -20px;
          line-height: 14px;
          color: #666;
        }
      }
    }
    &>p{
      @include flex(1);
      line-height: 24px;
      color: #999;
      font-size: 14px;
    }
    
    &>span{
      @include flex(.8);
      font-size: 16px;
      color: #000;
      line-height: 24px;
      font-weight: 500;
      text-align: right;
    }
    &:nth-child(2n) {
      background: #fff;
    }
  }
}
.noData{
    @include flexbox();
    @include flexAC();
    @include flexJC(center);
    width: 100%;
    height: 100%;
    img{
      width: 35%;
    }
  }
</style>