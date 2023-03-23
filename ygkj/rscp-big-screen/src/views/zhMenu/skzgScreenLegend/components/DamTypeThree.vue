<!--
 * @Date: 2022-01-13 14:16:12
 * @Author: dtb
 * @Description: 三类坝图例
 * @LastEditors: dtb
 * @LastEditTime: 2022-04-07 18:36:15
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenLegend\components\DamTypeThree.vue
-->
<template>
  <div class='wrap'>
    <el-checkbox-group v-model="checkedStatus" @change="getList">
      <el-checkbox :disabled='apiOnce' v-for="item in skStatus" :label="item.scale" :key="item.scale">
        <p class="legend_label">
          <img :src="item.icon" alt="">
          <span>{{item.scale}}</span>
          <i v-show="checkedStatus.includes(item.scale)">({{item.num}})</i>
        </p>
      </el-checkbox>
    </el-checkbox-group>
  </div>
</template>

<script>
import { generateProject } from "@/api/api_reservoir";
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
      checkedStatus: ['超限运行'],
      skStatus: [
        {
          icon: this.$config.imgUrl + 'limitLevel.png',
          scale: '超限运行',
          num: 0
        }, {
          icon: this.$config.imgUrl + 'emptyOperation.png',
          scale: '空库运行',
          num: 0
        },
      ],
      pointArr: {
        '超限运行': [],
        '空库运行': [],
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
      this.checkedStatus.forEach(element => {
        switch (element) {
          case '超限运行':
            temp.push(11)
            break;
          case '空库运行':
            temp.push(122)
            break;
        }
      })
      return temp.join(',')
    },
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
    if (this.defultSelected !== '') {
      this.setSelect()
    }
    // let nowTime = new Date(new Date().format('MM-dd'))
    // let startTime = new Date('04-15')
    // let endTime = new Date('10-14')
    this.getList()
  },
  methods: {
    setSelect() {
      console.log('0000000000');
      this.checkedStatus = [this.defultSelected]
    },
    async getList() {
      this.skStatus.map(v=>v.num = 0);
      this.apiOnce = true
      this.pointArr = {
        '空库运行': [],
        '超限运行': [],
      }
      let opt = {
        adcd: this.sgzgSelectAdcd ? this.sgzgSelectAdcd : this.$localData("get", "userInfo").adcd,
        basinCode: this.sgzgSelectBasin,
        gpType: 4,
        screen: this.projectType,
        dam: 3,
        name: '',
        extractPram: '1'
      }
      this.$parent.$parent.$refs.skzgMap.removeAllLayer()
      let res = await generateProject(opt);
      if (res.success) {
        this.apiOnce = false
        if (res.data.length) {
          res.data.map((v, index) => {
            v.lat = v.LTTD;
            v.lng = v.LGTD;
            v.mapType = "cxzyxPopup";
            v.styleObj = {
              scale: 1.1
            }
            switch (v.Ltstatus) {
              case 11:
                v.styleObj.src = this.$config.imgUrl + 'limitLevel.png'
                this.pointArr['超限运行'].push(v)
                break;
              case 122:
                v.styleObj.src = this.$config.imgUrl + 'emptyOperation.png'
                this.pointArr['空库运行'].push(v)
                break;

            }
          });
          for (let i = 0; i < this.skStatus.length; i++) {
            for (let key in this.pointArr){
              if(this.skStatus[i].scale == key){
                this.skStatus[i].num = this.pointArr[key].length
              }
            }
          }
        }
        this.showPoints()
      }
    },
    changeScale(value) {
      this.showPoints()
    },
    showPoints() {
      let pointArr = [];
      for (let i = 0; i < this.checkedStatus.length; i++) {
        pointArr.push(...this.pointArr[this.checkedStatus[i]])
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

<style lang='scss'  >
.wrap {
  .el-radio__label {
    @include flexbox();
    @include flexAI(center);
  }
}
</style>