<!--
 * @Author: hanyu
 * @LastEditTime: 2022-04-29 11:09:04
 * @Description: 
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenLegend\components\SafeAppraisal.vue
-->
<template>
  <div class='wrap'>
    <el-radio-group v-model="checkedScale" @change="getList">
      <el-radio :disabled='apiOnce' v-for="item in skScale" :label="item.scale" :key="item.scale" :checked="item.checked">
        <p class="legend_label_radio">
          <img :src="item.icon" alt="">
          <span>{{item.scale}}</span>
          <i v-show="checkedScale == item.scale">({{num}})</i>
        </p>
      </el-radio>
    </el-radio-group>
  </div>
</template>

<script>
import { safeAppraisalApi } from "@/api/api_reservoir";
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
    return {
      checkedScale: '三类坝',
      skScale: [
        {
          icon: this.$config.imgUrl + 'aqjd-3.png',
          scale: '三类坝',
        },
        {
          icon: this.$config.imgUrl + 'aqjd-2.png',
          scale: '二类坝',
        },
        {
          icon: this.$config.imgUrl + 'aqjd-1.png',
          scale: '近一年到期',
        },
      ],
      num: 0,
      pointArr: {
        '三类坝': [],
        '二类坝': [],
        '近一年到期': [],
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
    }
  },
  watch: {
    sgzgSelectAdcd(val) {
      this.getList()
    },
    sgzgSelectBasin(val) {
      this.getList()
    },
    defultSelected(val) {
      this.setSelect()
      this.getList()
    }
  },
  mounted() {
    if(this.defultSelected !== ''){
      this.setSelect()
    }
    this.getList()
  },
  methods: {
    setSelect(){
      this.checkedScale = this.defultSelected
    },
    changeScale(value) {
      this.showPoints()
    },
    async getList() {
      this.num = 0
      this.apiOnce = true
      this.pointArr = {
        '三类坝': [],
        '二类坝': [],
        '近一年到期': [],
      }
      let opt = {
        adcd: this.sgzgSelectAdcd ? this.sgzgSelectAdcd : this.$localData("get", "userInfo").adcd,
        basinCode: this.sgzgSelectBasin,
        interType: 2,
        dam: this.checkedScale
      }
      this.$parent.$parent.$refs.skzgMap.removeAllLayer()
      let res = await safeAppraisalApi(opt);
      if (res.success) {
        this.apiOnce = false
        if (res.data.length) {
          let src = ''
          switch (this.checkedScale) {
            case '三类坝':
              src = this.$config.imgUrl + "aqjd-3.png"
              break;
            case '二类坝':
              src = this.$config.imgUrl + "aqjd-2.png"
              break;
            case '近一年到期':
              src = this.$config.imgUrl + "aqjd-1.png"
              break;
          }
          res.data.map((v) => {
            v.lat = v.LTTD;
            v.lng = v.LGTD;
            v.mapType = "aqjdPopup";
            v.styleObj = {
              scale: .5,
              src: src
            }
            this.pointArr[this.checkedScale].push(v)
          });
        }
        this.showPoints()
      }
    },
    showPoints() {
      let pointArr = this.pointArr[this.checkedScale];
      this.num = pointArr.length
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
    img {
      width: 15px;
      height: 15px;
    }
  }
}
</style>

<style lang='scss'  >
.wrap {
  .legend_label_radio{
    @include flexbox();
    @include flexAI(center);
    span{
      margin-left: 5px;
      color: #fff;
    }
  }
}
</style>