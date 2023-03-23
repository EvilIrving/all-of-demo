<!--
 * @Date: 2022-01-14 13:54:40
 * @Author: 
 * @Description: 风景区
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-07 17:00:02
 * @FilePath: /rscp-big-screen/src/views/skzgScreen/skzgScreenLegend/components/ScenicSpot.vue
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
import { scenicSpotApi } from "@/api/api_reservoir";
export default {
  name: 'WaterSupply',
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
    projectType(){
      let tempArr = []
      this.checkedScale.forEach(element => {
        switch (element) {
          case '大(1)型':
            tempArr.push(1)
            break;
          case '大(2)型':
            tempArr.push(2)
            break;
          case '中型':
            tempArr.push(3)
            break;
          case '小(1)型':
            tempArr.push(4)
            break;
          case '小(2)型':
            tempArr.push(5)
            break;
        }
      });
      return tempArr.join(',')
    }
  },
  watch: {
    sgzgSelectAdcd(val) {
      this.getReservoirList()
    },
    sgzgSelectBasin(val) {
      this.getReservoirList()
    }
  },
  data() {
    return {
      checkedScale: ['大(1)型', '大(2)型', '中型', '小(1)型', '小(2)型'],
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
    this.getReservoirList()
  },
  methods: {
    changeScale(value) {
      this.getReservoirList()
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
      let opt = {
        adcd: this.sgzgSelectAdcd ? this.sgzgSelectAdcd : this.$localData("get", "userInfo").adcd,
        basinCode: this.sgzgSelectBasin,
        projectScale: this.projectType,
        fetchAll: true
      }
      this.$parent.$parent.$refs.skzgMap.removeAllLayer()
      let res = await scenicSpotApi(opt);
      if (res.success) {
        this.apiOnce = false
        if (res.rows.length) {
          res.rows.map((v) => {
            v.lat = v.lttd;
            v.lng = v.lgtd;
            v.styleObj = {
              scale: 1.1
            }
            switch (v.project_scale) {
              case 1:
              case '1':
                v.styleObj.src = this.$config.imgUrl + "sk-1.png"
                this.pointArr['大(1)型'].push(v)
                break;
              case 2:
              case '2':
                v.styleObj.src = this.$config.imgUrl + "sk-2.png"
                this.pointArr['大(2)型'].push(v)
                break;
              case 3:
              case '3':
                v.styleObj.src = this.$config.imgUrl + "sk-3.png"
                this.pointArr['中型'].push(v)
                break;
              case 4:
              case '4':
                v.styleObj.src = this.$config.imgUrl + "sk-4.png"
                this.pointArr['小(1)型'].push(v)
                break;
              case 5:
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