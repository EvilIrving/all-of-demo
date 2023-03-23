<!--
 * @Author: dtb
 * @LastEditTime: 2021-10-12 17:37:54
 * @Description: 进度条列表
 * @FilePath: src\components\ProgressList.vue
-->
<template>
  <div class="progress_wrap">
    <div class="chart_title" v-if="!noTitle">
      <img src="../assets/images/chart_item_icon.png" alt="">
      {{title}}
      <span v-if="unit" class="right unit">{{unit}}</span>
    </div>
    <div class="progress_box" v-if="progressData.length">
      <div class="progress_item" v-for="(item, index) in progressData" :key="index" @click="selectAdcd(item, index)">
        <p>{{item.title}}</p>
        <el-progress :class="item.isSelected ? 'isSelected' : ''" :percentage="item.percentage"></el-progress>
        <span>{{item.nowNum}}</span>
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
    },
    noTitle: {
      type: Boolean,
      default: false
    }
  },
  components: {
    
  },
  data () {
    return {
      
    };
  },
  mounted() {
    for (let i = 0; i < this.progressData.length; i++) {
      this.progressData[i].isSelected = false
    }
  },
  methods: {
    selectAdcd(item,index){
      if(!item.isSelected){
        for (let i = 0; i < this.progressData.length; i++) {
          this.progressData[i].isSelected = false
        }
        this.progressData[index].isSelected = true
        this.$set(this.progressData, index, this.progressData[index])
        this.$emit('selectAdcd', item)
      }else{
        this.progressData[index].isSelected = false
        this.$set(this.progressData, index, this.progressData[index])
        this.$emit('selectAdcd', {
          adcd: this.$localData('get', 'userInfo').adcd,
          title: this.$localData('get', 'userInfo').adnm
        })
      }
    }
  }
}
</script>

<style lang='scss' scoped>
.progress_wrap{
  // background: #6F9DF2;
  height: 100%;
}
.progress_box{
  height: calc(100% - 42px);
  box-sizing: border-box;
  padding: 0 16px;
  overflow: auto;
  .progress_item{
    padding: 3px 16px;
    background: #F8F8F8;
    @include flexbox();
    @include flexAC();
    @include flexJC(space-between);
    &>p{
      @include flex(1);
      line-height: 24px;
      color: #999;
      font-size: 14px;
    }
    
    .el-progress{
      @include flex(4);
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
    &:hover{
      background: #dee2f5;
      cursor: pointer;
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
<style lang="scss">
  .isSelected{
    .el-progress-bar__inner{
      background: #f37e69!important;
    }
  }
</style>