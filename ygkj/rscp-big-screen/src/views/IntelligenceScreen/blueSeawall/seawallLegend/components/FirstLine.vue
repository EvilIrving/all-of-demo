<!--
 * @Date: 2022-10-12 15:03:40
 * @Author: 
 * @Description: 闭环管控
 * @LastEditors: dtb
 * @LastEditTime: 2022-10-18 23:47:40
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\blueSeawall\seawallLegend\components\FirstLine.vue
-->


<template>
  <div class='wrap'>
    <el-checkbox-group v-model="checkedStatus">
      <el-checkbox v-for="item in skStatus" :label="item.scale" :key="item.scale" :checked="item.checked">
        <p class="legend_label">
          <img :src="item.icon" alt="">
          <span>{{item.scale}}</span>
        </p>
      </el-checkbox>
    </el-checkbox-group>
  </div>
</template>

<script>
import { generateProject } from "@/api/api_seawall";
export default {
  name: 'FirstLine',
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
      checkedScale: ['一级海塘', '二级海塘', '三级海塘', '四级海塘', '非标海塘'],
     skScale: [
        {
          project_scale: 1,
          scale: '一级海塘',
          num: 0,
          color: '#327dff'
        },
        {
          project_scale: 2,
          scale: '二级海塘',
          num: 0,
          color: '#5a5eff'
        },
        {
          project_scale: 3,
          scale: '三级海塘',
          num: 0,
          color: '#787bff'
        },
        {
          project_scale: 4,
          scale: '四级海塘',
          num: 0,
          color: '#afb1ff'
        },
        {
          project_scale: 5,
          scale: '非标海塘',
          num: 0,
          color: '#dae3ff'
        }
      ],
      checkedStatus: [],
      skStatus: [
        {
          checked: true,
          icon: this.$config.zhImgUrl + 'climaxRisk.png',
          scale: '高潮位风险',
          num: 159
        },
        {
          checked: false,
          icon: this.$config.zhImgUrl + 'kouMenRisk.png',
          scale: '口门风险',
          num: 29
        },
        {
          checked: false,
          icon: this.$config.zhImgUrl + 'shigong_risk.png',
          scale: '施工风险',
          num: 5
        },
      ],
      pointArr: {
        '高潮位风险': [],
        '口门风险': [],
        '施工风险': [],
      },
      pointArr: {
        '一级海塘': [],
        '二级海塘': [],
        '三级海塘': [],
        '四级海塘': [],
        '非标海塘': [],
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
    width: 90%;
    margin: 5px;
    height: 2px;
    background: #979797;
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