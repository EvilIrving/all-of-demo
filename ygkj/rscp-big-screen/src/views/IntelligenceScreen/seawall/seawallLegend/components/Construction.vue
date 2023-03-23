<!--
 * @Date: 2022-10-18 09:25:13
 * @Author: 
 * @Description: 工程施工
 * @LastEditors: dtb
 * @LastEditTime: 2022-10-18 09:28:11
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawall\seawallLegend\components\Construction.vue
-->


<template>
  <div class='wrap'>
    <el-checkbox-group v-model="checkedStatus">
      <el-checkbox v-for="item in skStatus" :label="item.scale" :key="item.scale" :checked="item.checked">
        <p class="legend_label">
        <section class="color_block" :style="{'background-color':item.color}"></section>
        <span>{{item.scale}}</span>
        </p>
      </el-checkbox>
    </el-checkbox-group>
    <div class="line"></div>
    <el-checkbox-group v-model="checkedScale">
      <el-checkbox v-for="item in skScale" :label="item.scale" :key="item.scale" :checked="item.checked">
        <p class="legend_label">
          <img :src="item.icon" alt="">
          <span>{{item.scale}}</span>
        </p>
      </el-checkbox>
    </el-checkbox-group>

  </div>
</template>

<script>
import { dealProblemInfo } from "@/api/api_seawall";
export default {
  name: 'Security',
  props: {

  },
  components: {

  },
  computed: {

  },
  watch: {

  },
  data() {
    return {
      checkedScale: [''],
      skScale: [
        {
          icon: this.$config.zhImgUrl + 'wkg.png',
          project_scale: 1,
          scale: '未开工',
          num: 0,
          color: '#193CC5'
        },
        {
          icon: this.$config.zhImgUrl + 'ykg.png',
          project_scale: 2,
          scale: '已开工',
          num: 0,
          color: '#4C71FF'
        },
        {
          icon: this.$config.zhImgUrl + 'dnqg.png',
          project_scale: 3,
          scale: '当年完工',
          num: 0,
          color: '#7692FFFF'
        },
      ],
      checkedStatus: [''],
      skStatus: [
        {
          icon: this.$config.imgUrl + 'sk-1.png',
          project_scale: 1,
          scale: '一级海塘',
          num: 0,
          color: '#193CC5'
        },
        {
          icon: this.$config.imgUrl + 'sk-2.png',
          project_scale: 2,
          scale: '二级海塘',
          num: 0,
          color: '#4C71FF'
        },
        {
          icon: this.$config.imgUrl + 'sk-3.png',
          project_scale: 3,
          scale: '三级海塘',
          num: 0,
          color: '#7692FFFF'
        },
        {
          icon: this.$config.imgUrl + 'sk-4.png',
          project_scale: 4,
          scale: '四级海塘',
          num: 0,
          color: '#ACB5FFFF'
        },
        {
          project_scale: 5,
          scale: '非标海塘',
          icon: this.$config.imgUrl + 'sk-5.png',
          num: 0,
          color: '#E3E4FFFF'
        }
      ],
      pointArr: {
        '高潮位风险': [],
        '口门风险': [],
        '施工风险': [],
      },
      //定一个状态 只允许请求一次 0：请求之前 1：请求结束
      apiOnce: false,
    };
  },
  mounted() {
  },
  methods: {


  }
}
</script>

<style lang='scss' scoped>
.wrap {
  .el-radio {
    width: 100%;
    height: 30px;
    @include flexbox();
    @include flexAI(center);
    margin-left: 5px;
    .el-radio__label {
      @include flexbox();
      @include flexAI(center);
      @include flexJC(center);
    }
    .text {
      margin-left: 5px;
      color: #fff;
    }
    img {
      width: 20px;
    }
  }
  .line {
    width: 80%;
    margin: 5px auto;
    height: 2px;
    background: #97979740;
  }
}
</style>
<style>
.el-checkbox-group {
  margin-left: 15px;
}
.el-radio-group {
  margin-left: 10px;
}
</style>