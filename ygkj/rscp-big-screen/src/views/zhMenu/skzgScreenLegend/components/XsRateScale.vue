<!--
 * @Date: 2022-01-14 16:02:29
 * @Author: dtb
 * @Description: 纳蓄图例
 * @LastEditors: dtb
 * @LastEditTime: 2022-04-17 15:59:27
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenLegend\components\XsRateScale.vue
-->
<template>
  <div class='wrap'>
    <el-checkbox-group v-model="checkedScale" @change="changeScale">
      <el-checkbox :disabled='apiOnce' v-for="item in skScale" :label="item.scale" :key="item.scale" :checked="item.checked">
        <p class="legend_label">
          <img :src="item.icon" alt="">
          <span>{{item.scale}}</span>
        </p>
      </el-checkbox>
    </el-checkbox-group>
  </div>
</template>


<script>
export default {
  name: 'XsRateScale',
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
    judgeType(){
      let type = 1
      if(this.sgzgSelectAdcd && !this.sgzgSelectBasin){
        type = 1
      }else if(this.sgzgSelectBasin && !this.sgzgSelectAdcd){
        type = 2
      }else{
        type = 1
      }
      return type
    },
    projectType() {
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
      if (this.skzgSwitchXsl) {
        this.getMapOverlay()
      }
      this.getMapOverlay()
    },
    sgzgSelectBasin(val) {
      this.getMapOverlay()
    },
  },
  data() {
    return {
      checkedScale: [],
      skScale: [
        {
          checked: true,
          icon: this.$config.imgUrl + 'sk-1.png',
          scale: '大(1)型',
          num: 29
        },
        {
          checked: true,
          icon: this.$config.imgUrl + 'sk-2.png',
          scale: '大(2)型',
          num: 5
        },
        {
          checked: true,
          icon: this.$config.imgUrl + 'sk-3.png',
          scale: '中型',
          num: 159
        },
        {
          checked: false,
          icon: this.$config.imgUrl + 'sk-4.png',
          scale: '小(1)型',
          num: 727
        },
        {
          checked: false,
          scale: '小(2)型',
          icon: this.$config.imgUrl + 'sk-5.png',
          num: 3376
        }
      ],
      //定一个状态 只允许请求一次 0：请求之前 1：请求结束
      apiOnce: true,
      userAdcd: this.$localData("get", "userInfo").adcd,
    };
  },
  mounted() {
    this.getMapOverlay()
  },
  methods: {
    changeScale(value) {
      let tempArr = []
      value.forEach(element => {
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
      })
      this.$store.commit('SET_WATER_STORAGE_RATE', tempArr.join(','))
      this.getMapOverlay()
    },
    async getMapOverlay() {
      this.$store.commit('SET_SKZG_SWITCH_XSL', true)
      this.$parent.$parent.$refs.skzgMap.removeAllLayer()
      this.apiOnce = true
      let opt = {
        adcd: this.sgzgSelectAdcd ? this.sgzgSelectAdcd : this.userAdcd,
        basin: this.sgzgSelectBasin,
        type: this.judgeType,
        projectScale: this.projectType
      }
      this.$parent.$parent.$refs.skzgMap.getMapFloowData(2, opt, this.apiOnce = false) // 2:蓄水率
    },
  }
}
</script>


<style lang='scss' scoped>
.wrap {
}
</style>