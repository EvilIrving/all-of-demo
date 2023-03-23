<!--
 * @Date: 2022-01-13 14:16:12
 * @Author: dtb
 * @Description: 问题处置 图例
 * @LastEditors: dtb
 * @LastEditTime: 2022-10-13 20:26:56
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawallScreen\seawallScreenLegend\components\KeyProblem.vue
-->
<template>
  <div class='wrap'>
    <el-radio-group v-model="checkedScale" @change="getSeawallList">
      <el-radio :disabled='apiOnce' v-for="item in skScale" :label="item.scale" :key="item.scale" :checked="item.checked">
        <p class="legend_label_radio">
          <!-- <img :src="item.icon" alt=""> -->
        <section class="color_block" :style="{'background-color':item.color}"></section>
        <span>{{item.scale}}</span>
        <!-- <i v-show="checkedScale == item.scale">({{num}})</i> -->
        </p>
      </el-radio>
    </el-radio-group>
    <!-- <div class="line"></div>
    <el-checkbox-group v-model="checkedStatus" @change="getSeawallList">
      <el-checkbox :disabled='apiOnce' v-for="item in skStatus" :label="item.scale" :key="item.scale">
        <p class="legend_label">
          <img :src="item.icon" alt="">
          <span>{{item.scale}}</span>
        </p>
      </el-checkbox>
    </el-checkbox-group> -->
  </div>
</template>

<script>
import {zhSeawall, dealProblemInfo } from "@/api/api_seawall";
export default {
  name: 'KeyProblem',
  props: {
    defultSelected: {
      type: String,
      default: ''
    }
  },
  components: {

  },
  data() {
    // 已处置、处置中、未处置（单选）/一般隐患、较重隐患、严重隐患（复选） 按顺序展示
    return {
      checkedScale: '已处置',
      skScale: [
        {
          icon: this.$config.imgUrl + 'readyRectified.png',
          scale: '已处置',
          color: '#1BC57E',
          num: 0
        },
        {
          icon: this.$config.imgUrl + 'Rectifiing.png',
          scale: '处置中',
          color: '#F7C739',
          num: 0
        },
        // {
        //   icon: this.$config.imgUrl + 'notRectified.png',
        //   scale: '未处置',
        //   num: 0
        // },
      ],
      checkedStatus: ['一般隐患'],
      skStatus: [
        {
          // checked: true,
          icon: this.$config.imgUrl + 'yiban.png',
          scale: '一般隐患',
          num: 0
        },
        {
          // checked: false,
          icon: this.$config.imgUrl + 'jiaozhong.png',
          scale: '较重隐患',
          num: 0
        },
        {
          // checked: false,
          icon: this.$config.imgUrl + 'yanzhong.png',
          scale: '严重隐患',
          num: 0
        },
      ],
      num: 0,
      pointArr: {
        '处置中': [],
        '已处置': [],
      },
      //定一个状态 只允许请求一次 0：请求之前 1：请求结束
      apiOnce: true,
    };
  },
  computed: {
    sgzgSelectAdcd() {
      return this.$store.state.sgzgSelectAdcd
    },
    sgzgSelectBasin() {
      return this.$store.state.sgzgSelectBasin
    },
    disposeStauts() {
      let tempNum = []
      switch (this.checkedScale) {
        case '处置中':
          tempNum = 14
          break;
        case '已处置':
          tempNum = 15
          break;
        default:
          tempNum = 16
          break;
      }
      return tempNum
    },
    projectType() {
      let temp = []
      this.checkedStatus.forEach(element => {
        //  extractPram    1 2 3   一般  较重  严重
        switch (element) {
          case '一般隐患':
            temp.push(1)
            break;
          case '较重隐患':
            temp.push(2)
            break;
          case '严重隐患':
            temp.push(3)
            break;
        }
      });
      return temp.join(',')
    },
  },
  watch: {
    sgzgSelectAdcd(val) {
      this.getSeawallList()
    },
    sgzgSelectBasin(val) {
      this.getSeawallList()
    },
    defultSelected(val) {
      this.setSelect()
      this.getSeawallList()
    }
  },
  mounted() {
    if (this.defultSelected !== '') {
      this.setSelect()
    }
    this.getSeawallList()
  },
  methods: {
    setSelect() {
      this.checkedScale = this.defultSelected
    },
    changeScale(value) {
      this.showPoints()
    },
    async getSeawallList() {
      this.num = 0
      this.apiOnce = true
      this.pointArr = {
        '处置中': [],
        '已处置': [],
      }
      let opt = {
        adcd: this.sgzgSelectAdcd ? this.sgzgSelectAdcd : this.$localData("get", "userInfo").adcd,
        basinCode: this.sgzgSelectBasin,
        screen: this.disposeStauts,
        // extractPram: this.projectType,
        dealType: this.checkedScale,
        interType: 1,
      }
      this.$parent.$parent.$refs.seawallMap.removeAllLayer()
      let res = await dealProblemInfo(opt);

      if (res.success) {
        this.apiOnce = false
        let lineArr = [];
        if (res.data.length) {
          res.data.map(v => {
            v.lat = v.lttd;
            v.lng = v.lgtd;
            v.mapType = 'zdyhwtPopup'
            v.styleObj = {
              scale: 1
            }
            switch (v.problemStatus) {
              case '已处置':
                v.styleObj.src = this.$config.imgUrl + "greencode.png"
                this.pointArr['已处置'].push(v)
                break;
              case '处置中':
                v.styleObj.src = this.$config.imgUrl + "redcode.png"
                this.pointArr['处置中'].push(v)
                break;
            }
            lineArr.push({
              line: v.coorpot
                ? JSON.parse(
                  v.coorpot.replace(new RegExp("&quot;", "gm"), '"')
                )[0]
                : "",
              level: v.problemStatus,
              type: 'zdyhwtPopup'
            });
            v.lineArr = [];
            v.lineArr.push({
              line: v.coorpot
                ? JSON.parse(
                  v.coorpot.replace(new RegExp("&quot;", "gm"), '"')
                )[0]
                : "",
              level: v.problemStatus,
              type: 'zdyhwtPopup'
            });
          });
        }
        this.showPoints()
        this.$parent.$parent.$refs.seawallMap._drawLines(lineArr);
      }

    },
    showPoints() {
      let pointArr = [];
      pointArr.push(...this.pointArr[this.checkedScale])
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
      width: 15px;
      height: 15px;
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

<style lang='scss'  >
.wrap {
  .el-radio__label {
    @include flexbox();
    @include flexAI(center);
  }
}
</style>