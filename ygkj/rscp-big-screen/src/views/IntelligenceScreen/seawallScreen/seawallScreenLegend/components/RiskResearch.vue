<!--
 * @Date: 2022-10-11 14:03:59
 * @Author: 
 * @Description: 风险研判图例
 * @LastEditors: dtb
 * @LastEditTime: 2023-01-05 16:37:23
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawallScreen\seawallScreenLegend\components\RiskResearch.vue
-->

<template>
  <div class='wrap'>
    <el-checkbox-group v-model="checkedScale" @change="getSeawallList">
      <el-checkbox :disabled='apiOnce' v-for="item in skScale" :label="item.scale" :key="item.scale">
        <p class="legend_label">
          <!-- <img :src="item.icon" alt=""> -->
          <!-- <section class="color_block" :style="{'background-color':item.color}"></section> -->
          <span>{{item.scale}}{{item.checked}}</span>
          <!-- <i v-show="checkedScale.includes(item.scale)"></i> -->
          <i v-show="checkedScale.includes(item.scale)">({{item.num}})</i>
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
    sgzgSelectAdcd() {
      let localAdcd = ''
      this.$store.state.sgzgSelectAdcd === '' ? localAdcd = this.$localData("get", "userInfo").adcd : localAdcd = this.$store.state.sgzgSelectAdcd
      return localAdcd
    },
    sgzgSelectBasin() {
      return this.$store.state.sgzgSelectBasin
    },
    colorCode() {
      return this.$store.state.colorCode
    },
    projectType() {
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
    projectStatus() {
      let tempStr = ''
      let color = this.$store.state.colorCode
      switch (color) {
        case 'red':
          tempStr = '红色'
          break;
        case 'yellow':
          tempStr = '黄色'
          break;
        case 'green':
          tempStr = '绿色'
          break;
      }
      return tempStr
    },
  },
  watch: {
    sgzgSelectAdcd(val) {
      this.getSeawallList()
    },
    sgzgSelectBasin(val) {
      this.getSeawallList()
    },
    colorCode(val) {
      this.getSeawallList()
    },
  },
  data() {
    return {
      checkedScale: ['一级海塘', '二级海塘', '三级海塘'],
      skScale: [
        {
          icon: this.$config.imgUrl + 'sk-1.png',
          project_scale: 1,
          scale: '一级海塘',
          num: 0,
          color: '#E02021'
        },
        {
          icon: this.$config.imgUrl + 'sk-2.png',
          project_scale: 2,
          scale: '二级海塘',
          num: 0,
          color: '#EB7E63'
        },
        {
          icon: this.$config.imgUrl + 'sk-3.png',
          project_scale: 3,
          scale: '三级海塘',
          num: 0,
          color: '#D75CFB'
        },
        {
          icon: this.$config.imgUrl + 'sk-4.png',
          project_scale: 4,
          scale: '四级海塘',
          num: 0,
          color: '#549EFF'
        },
        {
          project_scale: 5,
          scale: '非标海塘',
          icon: this.$config.imgUrl + 'sk-5.png',
          num: 0,
          color: '#00FD6D'
        }
      ],
      // checkedStatus: [],
      skStatus: [
        {
          checked: false,
          icon: this.$config.imgUrl + 'controlLevel.png',
          scale: '绿码',
          num: 159
        },
        {
          checked: false,
          icon: this.$config.imgUrl + 'limitLevel.png',
          scale: '黄码',
          num: 29
        },
        {
          checked: false,
          icon: this.$config.imgUrl + 'normalLevel.png',
          scale: '红码',
          num: 5
        },
      ],
      pointArr: {
        '绿码': [],
        '红码': [],
        '黄码': [],
      },
      //定一个状态 只允许请求一次 0：请求之前 1：请求结束
      apiOnce: true,
    };
  },
  mounted() {
    this.getSeawallList()
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
        let scaleArr = {
          '一级海塘': [],
          '二级海塘': [],
          '三级海塘': [],
          '四级海塘': [],
          '非标海塘': [],
        };
        if (res.data.length) {
          res.data.map(v => {
            v.lat = v.lttd;
            v.lng = v.lgtd;
            v.mapType = 'seawallPopup'
            switch (v.threeYards) {
              case '绿色':
                this.pointArr['绿码'].push(v)
                break;
              case '红色':
                this.pointArr['红码'].push(v)
                break;
              case '黄色':
                this.pointArr['黄码'].push(v)
                break;
            }

            switch (v.project_scale) {
              case '1':
                scaleArr['一级海塘'].push(v)
                break;
              case '2':
                scaleArr['二级海塘'].push(v)
                break;
              case '3':
                scaleArr['三级海塘'].push(v)
                break;
              case '4':
                scaleArr['四级海塘'].push(v)
                break;
              default:
                scaleArr['非标海塘'].push(v)
                break;
            }


            lineArr.push({
              line: v.coorpot
                ? JSON.parse(
                  v.coorpot.replace(new RegExp("&quot;", "gm"), '"')
                )[0]
                : "",
              level: v.threeYards,
              type: 'seawallPopup'
            });
            v.lineArr = [];
            v.lineArr.push({
              line: v.coorpot
                ? JSON.parse(
                  v.coorpot.replace(new RegExp("&quot;", "gm"), '"')
                )[0]
                : "",
              level: v.threeYards,
              type: 'seawallPopup'
            });
          });

          for (let i = 0; i < this.skScale.length; i++) {
            for (let key in scaleArr) {
              if (this.skScale[i].scale == key) {
                this.skScale[i].num = scaleArr[key].length
              }
            }
          }
        }
        this.showPoints()
        this.$parent.$parent.$refs.seawallMap._drawLines(lineArr);
      }
    },
    showPoints() {
      let color = this.$store.state.colorCode
      switch (color) {
        case 'green':
          this.$parent.$parent.$refs.seawallMap.aggregationPointer(this.pointArr['绿码'], "seawallGreenLayer");
          break;
        case 'yellow':
          this.$parent.$parent.$refs.seawallMap.aggregationPointer(this.pointArr['黄码'], "seawallYellowLayer");
          break;
        case 'red':
          this.$parent.$parent.$refs.seawallMap.aggregationPointer(this.pointArr['红码'], "seawallRedLayer");
          break;
        case 'all':
        case '':
          this.$parent.$parent.$refs.seawallMap.aggregationPointer(this.pointArr['绿码'], "seawallGreenLayer");
          this.$parent.$parent.$refs.seawallMap.aggregationPointer(this.pointArr['黄码'], "seawallYellowLayer");
          this.$parent.$parent.$refs.seawallMap.aggregationPointer(this.pointArr['红码'], "seawallRedLayer");
          break;
      }
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
.legend_label .el-checkbox__label {
  margin-left: 12px;
}
</style>