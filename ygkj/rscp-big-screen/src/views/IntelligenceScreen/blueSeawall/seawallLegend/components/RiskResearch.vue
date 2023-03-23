<!--
 * @Date: 2022-10-11 14:03:59
 * @Author: 
 * @Description: 风险研判图例
 * @LastEditors: dtb
 * @LastEditTime: 2022-10-21 23:04:35
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\blueSeawall\seawallLegend\components\RiskResearch.vue
-->

<template>
  <div class='wrap'>
    <el-checkbox-group v-model="checkedScale" @change="showPointsScale">
      <el-checkbox :disabled='apiOnce' :class="item.className" v-for="item in skScale" :label="item.scale" :key="item.scale" :value='item.project_scale'>
        <p class="legend_label">
          <!-- <img :src="item.icon" alt=""> -->
          <!-- <section class="color_block" :style="{'background-color':item.color}"></section> -->
          <span :class="item.className" :style="{'color':item.color}">{{item.scale}}</span>
          <!-- <i v-show="checkedScale.includes(item.scale)">({{item.num}})</i> -->
        </p>
      </el-checkbox>
    </el-checkbox-group>
    <!-- <div class="line"></div>
    <el-checkbox-group v-model="checkedStatus" @change="getSeawallList">
      <el-checkbox v-for="item in skStatus" :label="item.scale" :key="item.scale" :checked="item.checked">
        <p class="legend_label">
          <span>{{item.scale}}</span>
        </p>
      </el-checkbox>
    </el-checkbox-group> -->
  </div>
</template>

<script>
import { seawallProIndo } from "@/api/api_seawall";
export default {
  name: 'RiskResearch',
  props: {

  },
  components: {

  },
  computed: {
    projectScale() {
      let tempArr = []
      this.checkedScale.forEach(element => {
        switch (element) {
          case '一级海塘':
            tempArr.push(1)
            break;
          case '二级海塘':
            tempArr.push(2)
            break;
          case '三级海塘':
            tempArr.push(3)
            break;
          case '四级海塘':
            tempArr.push(4)
            break;
          case '非标海塘':
            tempArr.push(5)
            break;
        }
      });
      return tempArr.join(',')
    },
    leftSelectProjectScale() {
      return this.$store.state.leftSelectProjectScale
    }
  },
  watch: {
    leftSelectProjectScale(val) {
      let tempArr = []
      switch (val) {
        case '1':
        case 1:
          tempArr = ['一级海塘']
          break;
        case '2':
        case '2':
          tempArr = ['二级海塘']
          break;
        case '3':
        case 3:
          tempArr = ['三级海塘']
          break;
        case '4':
        case 4:
          tempArr = ['四级海塘']
          break;
        case '5':
        case 5:
          tempArr = ['非标海塘']
          break;
      }
      this.checkedScale = tempArr
    }
  },
  data() {
    return {
      checkedScale: ['一级海塘', '二级海塘', '三级海塘', '四级海塘', '非标海塘'],
      skScale: [
        {
          project_scale: 1,
          scale: '一级海塘',
          num: 0,
          className: 'one',
          color: '#0C9800'
        },
        {
          project_scale: 2,
          scale: '二级海塘',
          num: 0,
          className: 'two',
          color: '#0DB200'
        },
        {
          project_scale: 3,
          scale: '三级海塘',
          num: 0,
          className: 'three',
          color: '#12E800'
        },
        {
          project_scale: 4,
          scale: '四级海塘',
          num: 0,
          className: 'four',
          color: '#93F28B'
        },
        {
          project_scale: 5,
          scale: '非标海塘',
          num: 0,
          className: 'five',
          color: '#E6FBE4'
        }
      ],
      //定一个状态 只允许请求一次 0：请求之前 1：请求结束
      apiOnce: true,
    };
  },
  mounted() {
  },
  methods: {
    showPointsScale(val) {
      console.log(val, 'valk');
      console.log(this.projectScale, 'projectScale');
      // this.$store.commit("SET_LEFT_SELECT_PROJECT_SCALE", this.projectScale);
    }
  }
}
</script>