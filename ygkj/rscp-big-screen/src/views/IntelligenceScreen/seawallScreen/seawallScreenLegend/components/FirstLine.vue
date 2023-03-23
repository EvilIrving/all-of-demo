<!--
 * @Date: 2022-10-12 15:03:40
 * @Author: 
 * @Description: 一线海塘
 * @LastEditors: dtb
 * @LastEditTime: 2023-01-06 11:17:12
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawallScreen\seawallScreenLegend\components\FirstLine.vue
-->


<template>
  <div class='wrap'>
    <el-checkbox-group v-model="checkedScale" @change="getSeawallList">
      <el-checkbox :disabled='apiOnce' v-for="item in skScale" :label="item.scale" :key="item.scale">
        <p class="legend_label">
          <!-- <img :src="item.icon" alt=""> -->
        <section class="color_block" :style="{'background-color':item.color}"></section>
        <span>{{item.scale}}{{item.checked}}</span>
        <i v-show="checkedScale.includes(item.scale)">({{item.num}})</i>
        </p>
      </el-checkbox>
    </el-checkbox-group>

  </div>
</template>

<script>
import { zhSeawall } from "@/api/api_seawall";
export default {
  name: 'FirstLine',
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
    leftChartClick() {
      return this.$store.state.leftChartClick
    },
    sgzgSelectBasin() {
      return this.$store.state.sgzgSelectBasin
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

  },
  watch: {
    sgzgSelectAdcd(val) {
      this.getSeawallList()
    },
    sgzgSelectBasin(val) {
      this.getSeawallList()
    },
    leftChartClick(val) {
      this.checkedScale = [val]
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
          color: '#C447D6'
        },
        {
          icon: this.$config.imgUrl + 'sk-4.png',
          project_scale: 4,
          scale: '四级海塘',
          num: 0,
          color: '#3154EB'
        },
        {
          project_scale: 5,
          scale: '非标海塘',
          icon: this.$config.imgUrl + 'sk-5.png',
          num: 0,
          color: '#00FD6D'
        }
      ],
      pointArr: {
        '一级海塘': [],
        '二级海塘': [],
        '三级海塘': [],
        '四级海塘': [],
        '非标海塘': [],
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
        '一级海塘': [],
        '二级海塘': [],
        '三级海塘': [],
        '四级海塘': [],
        '非标海塘': [],
      }
      let opt = {
        adcd: this.sgzgSelectAdcd,
        basinCode: this.sgzgSelectBasin,
        dam: 16, // ；类型
        projectScale: this.projectType, //规模
      }
      this.$parent.$parent.$refs.seawallMap.removeAllLayer()
      let res = await zhSeawall.seawallProIndo(opt);

      if (res.success) {
        this.apiOnce = false
        let pointArr = [];
        let lineArr = [];
        if (res.data.length) {
          res.data.map(v => {
            switch (v.project_scale) {
              case 1:
              case '1':
                this.pointArr['一级海塘'].push(v)
                break;
              case 2:
              case '2':
                this.pointArr['二级海塘'].push(v)
                break;
              case 3:
              case '3':
                this.pointArr['三级海塘'].push(v)
                break;
              case 4:
              case '4':
                this.pointArr['四级海塘'].push(v)
                break;
              case 5:
              case '5':
                this.pointArr['非标海塘'].push(v)
                break;
            }

            pointArr.push({
              lat: v.lttd,
              lng: v.lgtd,
              mapType: 'firstLinePopup',
              ...v
            });
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
              level: v.project_scale,
              type: 'seawallPopup'
            });
          });
        }

        for (let i = 0; i < this.skScale.length; i++) {
          for (let key in this.pointArr) {
            if (this.skScale[i].scale == key) {
              this.skScale[i].num = this.pointArr[key].length
            }
          }
        }

        this.$parent.$parent.$refs.seawallMap.newMapJh(pointArr, "seawallPopup");
        this.$parent.$parent.$refs.seawallMap._drawLines(lineArr);
      }
    },
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