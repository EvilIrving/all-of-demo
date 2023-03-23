<!--
 * @Date: 2022-10-11 14:03:59
 * @Author: 
 * @Description: 风险研判图例
 * @LastEditors: dtb
 * @LastEditTime: 2022-10-17 20:48:00
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawall\seawallLegend\components\RiskResearch.vue
-->

<template>
  <div class='wrap'>
    <el-checkbox-group v-model="checkedScale">
      <el-checkbox :disabled='apiOnce' v-for="item in skScale" :label="item.scale" :key="item.scale">
        <p class="legend_label">
          <!-- <img :src="item.icon" alt=""> -->
        <section class="color_block" :style="{'background-color':item.color}"></section>
        <span>{{item.scale}}{{item.checked}}</span>
        <i v-show="checkedScale.includes(item.scale)"></i>
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

  },
  watch: {

  },
  data() {
    return {
      checkedScale: ['一级海塘', '二级海塘', '三级海塘', '四级海塘', '非标海塘'],
      skScale: [
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
      //定一个状态 只允许请求一次 0：请求之前 1：请求结束
      apiOnce: false,
    };
  },
  mounted() {
  },
  methods: {
    async getSeawallList() {
      this.skScale.map(v => v.num = 0);
      this.apiOnce = true
      this.pointArr = {
        '绿码': [],
        '红码': [],
        '黄码': [],
      }
      let opt = {
        adcd: this.sgzgSelectAdcd ? this.sgzgSelectAdcd : this.$localData("get", "userInfo").adcd,
        basinCode: this.sgzgSelectBasin,
        projectScale: this.projectType,
        extractPram: this.projectStatus,
      }

      this.$parent.$parent.$refs.seawallMap.removeAllLayer()
      let res = await seawallProIndo(opt);
      if (res.success) {
        this.apiOnce = false
        let lineArr = [];
        if (res.data.length) {
          res.data.map(v => {
            v.lat = v.lttd;
            v.lng = v.lgtd;
            v.styleObj = {
              scale: 1
            }
            v.mapType = 'seawallPopup'
            switch (v.threeYards) {
              case '绿色':
                v.styleObj.src = this.$config.imgUrl + "greencode.png"
                this.pointArr['绿码'].push(v)
                break;
              case '红色':
                v.styleObj.src = this.$config.imgUrl + "redcode.png"
                this.pointArr['红码'].push(v)
                break;
              case '黄色':
                v.styleObj.src = this.$config.imgUrl + "yellowcode.png"
                this.pointArr['黄码'].push(v)
                break;
            }
            lineArr.push({
              line: v.coorpot
                ? JSON.parse(
                  v.coorpot.replace(new RegExp("&quot;", "gm"), '"')
                )[0]
                : "",
              level: v.project_scale,
              type: 'seawallPopup'
            });
            v.lineArr = [];
            v.lineArr.push({
              line: v.coorpot
                ? JSON.parse(
                  v.coorpot.replace(new RegExp("&quot;", "gm"), '"')
                )[0]
                : "",
              level: v.seawallPopup,
              type: 'seawallPopup'
            });
          });

          // for (let i = 0; i < this.skStatus.length; i++) {
          //   for (let key in this.pointArr) {
          //     if (this.skStatus[i].scale == key) {
          //       this.skStatus[i].num = this.pointArr[key].length
          //     }
          //   }
          // }
        }
        this.showPoints()
        this.$parent.$parent.$refs.seawallMap._drawLines(lineArr);
      }
    },
    showPoints() {
      let pointArr = [];
      let tempArr = []
      let color = this.$store.state.colorCode
      switch (color) {
        case 'green':
          tempArr = ['绿码']
          break;
        case 'yellow':
          tempArr = ['黄码']
          break;
        case 'red':
          tempArr = ['红码']
          break;
        case 'all':
        case '':
          tempArr = ['红码', '黄码', '绿码']
          break;
      }
      for (let i = 0; i < tempArr.length; i++) {
        pointArr.push(...this.pointArr[tempArr[i]])
      }
      this.$parent.$parent.$refs.seawallMap.newMapJh(pointArr, "seawallPopup");
    }
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