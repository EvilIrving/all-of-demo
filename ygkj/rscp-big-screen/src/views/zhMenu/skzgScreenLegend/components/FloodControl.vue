<!--
 * @Date: 2022-01-13 14:16:12
 * @Author: dtb
 * @Description:防洪图例
 * @LastEditors: dtb
 * @LastEditTime: 2022-07-22 09:38:33
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenLegend\components\FloodControl.vue
-->
<template>
  <div class='wrap'>

    <el-radio-group v-model="checkedStatus">
      <el-radio :disabled='apiOnce' v-for="item in skStatus" :label="item.scale" :key="item.scale">
        <p class="legend_label_radio">
          <img :src="item.icon" alt="">
          <span>{{item.scale}}</span>
          <i v-show="checkedStatus == item.scale">({{item.num}})</i>
        </p>
      </el-radio>
    </el-radio-group>
    <div class="line"></div>
    <el-checkbox-group v-model="checkedScale">
      <el-checkbox :disabled='apiOnce' v-for="item in skScale" :label="item" :key="item">
        <p class="legend_label">
          <!-- <img :src="item.icon" alt=""> -->
          <span>{{item}}</span>
        </p>
      </el-checkbox>
    </el-checkbox-group>
    <!-- <ul class="" style="margin-left:16px">
      <li v-for="item in skStatus" :label="item.scale" :key="item.scale">
        <p class="legend_label">
          <img :src="item.icon" alt="">
          <span>{{item.scale}}</span>
          <i>({{item.num}})</i>
        </p>
      </li>
    </ul> -->
  </div>
</template>

<script>
import { floodListJson, floodListYihong } from "@/api/api_reservoir";
export default {
  name: 'FloodControl',
  props: {
    defultSelected: {
      type: String,
      default: ''
    }
  },
  components: {

  },
  data() {
    return {
      checkedScale: ['大(1)型', '大(2)型', '中型'],
      skScale: ['大(1)型', '大(2)型', '中型'],
      checkedStatus: '泄洪',
      skStatus: [
        {
          icon: this.$config.imgUrl + 'flooddischarge.png',
          scale: '泄洪',
          num: 0
        },
        {
          icon: this.$config.imgUrl + 'spillover2.png',
          scale: '溢洪',
          num: 0
        },
      ],
      pointArr: {
        '溢洪': [],
        '泄洪': [],
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
    projectType() {
      let temp = []
      this.checkedScale.forEach(element => {
        switch (element) {
          case '大(1)型':
            temp.push(1)
            break;
          case '大(2)型':
            temp.push(2)
            break;
          case '中型':
            temp.push(3)
            break;
          case '小(1)型':
            temp.push(4)
            break;
          case '小(2)型':
            temp.push(5)
            break;
        }
      });
      return temp.join(',')
    },
  },
  watch: {
    sgzgSelectAdcd() {
      this.getList()
    },
    sgzgSelectBasin() {
      this.getList()
    },
    checkedStatus(val) {
      if (val == '泄洪') {
        this.checkedScale = ['大(1)型', '大(2)型', '中型']
        this.skScale = ['大(1)型', '大(2)型', '中型']
      } else {
        this.checkedScale = ['大(1)型', '大(2)型', '中型', '小(1)型', '小(2)型']
        this.skScale = ['大(1)型', '大(2)型', '中型', '小(1)型', '小(2)型']
      }
      // this.getList()
    },
    checkedScale: {
      handler(newVal, oldVal) {
        this.getList()
      },
      immediate: true
    },
    defultSelected(val) {
      if (val == '泄洪' || val == '溢洪') {
        this.checkedStatus = val
      } else {
        this.checkedStatus = '泄洪'
      }
      if (this.checkedStatus == '泄洪') {
        this.checkedScale = ['大(1)型', '大(2)型', '中型']
        this.skScale = ['大(1)型', '大(2)型', '中型']
      } else {
        this.checkedScale = ['大(1)型', '大(2)型', '中型', '小(1)型', '小(2)型']
        this.skScale = ['大(1)型', '大(2)型', '中型', '小(1)型', '小(2)型']
      }
      // this.getList()
    }
  },
  mounted() {
    if (this.defultSelected == '泄洪' || this.defultSelected == '溢洪') {
      this.checkedStatus = this.defultSelected
    } else {
      this.checkedStatus = '泄洪'

    }
    // this.getList()
  },
  methods: {
    changeScale(value) {
      this.showPoints()
    },
    async getList(val) {
      console.log(val, 'xxx');
      this.$parent.$parent.$refs.skzgMap.removeAllLayer()
      this.skStatus.map(v => v.num = 0);
      this.apiOnce = true
      this.pointArr = {
        '溢洪': [],
        '泄洪': [],
      }
      if (this.checkedStatus == '泄洪') {
        let opt = {
          adcd: this.sgzgSelectAdcd ? this.sgzgSelectAdcd : this.$localData("get", "userInfo").adcd,
          basinCode: this.sgzgSelectBasin,
          projectScale: this.projectType,
          year: 2022,
          fetchAll: true,
        }
        let res = await floodListJson(opt);
        if (res.success) {
          this.apiOnce = false
          if (res.rows.length) {
            res.rows.map((v) => {
              v.lat = +v.lttd;
              v.lng = +v.lgtd;
              v.mapType = "floodControlPopup";
              v.styleObj = {
                scale: 1
              }
              if (+ v.gateflow > 0) {
                v.styleObj.src = this.$config.imgUrl + 'flooddischarge.png'
                this.pointArr['泄洪'].push(v)
              }
            });
            for (let i = 0; i < this.skStatus.length; i++) {
              for (let key in this.pointArr) {
                if (this.skStatus[i].scale == key) {
                  this.skStatus[i].num = this.pointArr[key].length
                }
              }
            }
          }
          this.showPoints()
        }
      } else {
        let opt = {
          adcd: this.sgzgSelectAdcd ? this.sgzgSelectAdcd : this.$localData("get", "userInfo").adcd,
          basinCode: this.sgzgSelectBasin,
          projectScale: this.projectType,
        }
        let res = await floodListYihong(opt);
        if (res.success) {
          this.apiOnce = false
          console.log(res.rows, ' res.rows');
          if (res.rows.length) {
            res.rows.map((v) => {
              v.lat = +v.LTTD;
              v.lng = +v.LGTD;
              v.mapType = "floodControlYihongPopup";

              v.styleObj = {
                scale: 1,
                src: this.$config.imgUrl + 'spillover2.png'
              }
              this.pointArr['溢洪'].push(v)
            });

            for (let i = 0; i < this.skStatus.length; i++) {
              for (let key in this.pointArr) {

                if (this.skStatus[i].scale == key) {
                  this.skStatus[i].num = this.pointArr[key].length
                }
              }
            }
          }
          this.showPoints()
        }
      }
      console.log(this.skStatus, this.pointArr['溢洪'], 'skStatus');

    },
    showPoints() {
      let pointArr = [];
      let tempArr = ['泄洪', '溢洪']
      for (let i = 0; i < tempArr.length; i++) {
        pointArr.push(...this.pointArr[tempArr[i]])
      }
      this.$parent.$parent.$refs.skzgMap.drawMapPoints(pointArr, 'reservoirPopup');
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