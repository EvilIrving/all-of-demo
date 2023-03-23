<!--
 * @Date: 2022-01-14 16:02:29
 * @Author: dtb
 * @Description: 纳蓄图例
 * @LastEditors: dtb
 * @LastEditTime: 2022-04-07 18:33:30
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenLegend\components\NxypScale.vue
-->
<template>
  <div class='wrap'>
    <el-checkbox-group v-model="checkedScale" @change="changeScale">
      <el-checkbox :disabled='apiOnce' v-for="item in skScale" :label="item.scale" :key="item.scale">
        <p class="legend_label">
          <img :src="item.icon" alt="">
          <span>{{item.scale}}</span>
          <i v-show="checkedScale.includes(item.scale)">({{item.num}})</i>
        </p>
      </el-checkbox>
    </el-checkbox-group>
  </div>
</template>

<script>
import { generateProject } from "@/api/api_reservoir";
export default {
  name: 'NxypScale',
  props: {

  },
  components: {

  },
  computed: {
    sgzgSelectAdcd() {
      return this.$store.state.sgzgSelectAdcd
    },
    sgzgSelectBasin() {
      return this.$store.state.sgzgSelectBasin
    },
    rain() {
      return this.$store.state.rain
    },
  },
  watch: {
    sgzgSelectAdcd(val) {
      this.getReservoirList()
    },
    sgzgSelectBasin(val) {
      this.getReservoirList()
    },
    rain(val) {
      this.getReservoirList()
    }
  },
  data() {
    return {
      checkedScale: ['大(1)型', '大(2)型', '中型'],
      skScale: [
        {
          icon: this.$config.imgUrl + 'sk-1.png',
          scale: '大(1)型',
          num: 0
        },
        {
          icon: this.$config.imgUrl + 'sk-2.png',
          scale: '大(2)型',
          num: 0
        },
        {
          icon: this.$config.imgUrl + 'sk-3.png',
          scale: '中型',
          num: 0
        },
        {
          icon: this.$config.imgUrl + 'sk-4.png',
          scale: '小(1)型',
          num: 0
        },
        {
          scale: '小(2)型',
          icon: this.$config.imgUrl + 'sk-5.png',
          num: 0
        }
      ],
      pointArr: {
        '大(1)型': [],
        '大(2)型': [],
        '中型': [],
        '小(1)型': [],
        '小(2)型': [],
      },
      //定一个状态 只允许请求一次 0：请求之前 1：请求结束
      apiOnce: true,
    };
  },
  mounted() {
    console.log(12);
    this.getReservoirList()
  },
  methods: {
    changeScale(value) {
      this.showPoints()
    },
    async getReservoirList() {
      this.skScale.map(v=>v.num = 0);
      this.apiOnce = true
      this.pointArr = {
        '大(1)型': [],
        '大(2)型': [],
        '中型': [],
        '小(1)型': [],
        '小(2)型': [],
      }
      // rainScope   可纳雨量范围查询  a,b  ( >=a 并且 <b )
      let opt = {
        adcd: this.sgzgSelectAdcd ? this.sgzgSelectAdcd : this.$localData("get", "userInfo").adcd,
        basinCode: this.sgzgSelectBasin,
        gpType: 6,
      }
      switch (this.rain) {
        case '50,100':
        case '100,200':
          opt.rainScope = this.rain
          break;
        case '50,1':
          opt.ifUnder = 1
          opt.rain = 50
          break;
        case '200,2':
          opt.ifUnder = 2
          opt.rain = 200
          break;
        default:
          break;
      }
      this.$parent.$parent.$refs.skzgMap.removeAllLayer()
      let res = await generateProject(opt);
      if (res.success) {
        this.apiOnce = false
        if (res.data.length) {
          res.data.map((v) => {
            v.lat = v.LTTD;
            v.lng = v.LGTD;
            v.mapType = "nxypPopup";
            v.styleObj = {
              scale: 1.1
            }
            switch (v.project_scale) {
              case '1':
                v.styleObj.src = this.$config.imgUrl + "sk-1.png"
                this.pointArr['大(1)型'].push(v)
                break;
              case '2':
                v.styleObj.src = this.$config.imgUrl + "sk-2.png"
                this.pointArr['大(2)型'].push(v)
                break;
              case '3':
                v.styleObj.src = this.$config.imgUrl + "sk-3.png"
                this.pointArr['中型'].push(v)
                break;
              case '4':
                v.styleObj.src = this.$config.imgUrl + "sk-4.png"
                this.pointArr['小(1)型'].push(v)
                break;
              case '5':
                v.styleObj.src = this.$config.imgUrl + "sk-5.png"
                this.pointArr['小(2)型'].push(v)
                break;
            }
          });
          for (let i = 0; i < this.skScale.length; i++) {
            for (let key in this.pointArr){
              if(this.skScale[i].scale == key){
                this.skScale[i].num = this.pointArr[key].length
              }
            }
          }
        }
        this.showPoints()
      }
    },
    showPoints() {
      let pointArr = [];
      for (let i = 0; i < this.checkedScale.length; i++) {
        pointArr.push(...this.pointArr[this.checkedScale[i]])
      }
      this.$parent.$parent.$refs.skzgMap.drawMapPoints(pointArr, 'reservoirPopup');
    }
  }
}
</script>


<style lang='scss' scoped>
.wrap {
}
</style>