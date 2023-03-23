<!--
 * @Author: hanyu
 * @LastEditTime: 2022-03-22 14:04:09
 * @Description: LegendScale
 * @FilePath: /rscp-big-screen/src/views/skzgScreen/skzgScreenLegend/components/LegendScale.vue
-->
<template>
  <div class='wrap'>
    <el-checkbox-group  v-model="checkedScale" @change="getReservoirList">
      <el-checkbox :disabled='apiOnce' v-for="item in skScale" :label="item.scale" :key="item.scale">
        <p class="legend_label">
          <!-- <img :src="item.icon" alt=""> -->
          <span>{{item.scale}}{{item.checked}}</span>
          <i v-show="checkedScale.includes(item.scale)">({{item.num}})</i>
        </p>
      </el-checkbox>
    </el-checkbox-group>
    <!-- <div class="line"></div>
    <el-checkbox-group v-model="checkedStatus" @change="getReservoirList">
      <el-checkbox v-for="item in skStatus" :label="item.scale" :key="item.scale" :checked="item.checked">
        <p class="legend_label">
          <span>{{item.scale}}</span>
        </p>
      </el-checkbox>
    </el-checkbox-group> -->
  </div>
</template>

<script>
import { generateProject } from "@/api/api_reservoir";
export default {
  name: 'LegendScale',
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
    },
    projectStatus() {
      let tempStr = ''
      let color = this.$store.state.colorCode
      switch (color) {
        case 'red':
          tempStr = 19
          break;
        case 'yellow':
          tempStr = 18
          break;
        case 'green':
          tempStr = 17
          break;
      }
      return tempStr
    },
  },
  watch: {
    sgzgSelectAdcd(val) {
      this.getReservoirList()
    },
    sgzgSelectBasin(val) {
      this.getReservoirList()
    },
    colorCode(val) {
      console.log(val,'color');
      this.getReservoirList()
    },
  },
  data() {
    return {
      checkedScale: ['大(1)型', '大(2)型', '中型'],
      skScale: [
        {
          icon: this.$config.imgUrl + 'sk-1.png',
          project_scale: 1,
          scale: '大(1)型',
          num: 0
        },
        {
          icon: this.$config.imgUrl + 'sk-2.png',
          project_scale: 2,
          scale: '大(2)型',
          num: 0
        },
        {
          icon: this.$config.imgUrl + 'sk-3.png',
          project_scale: 3,
          scale: '中型',
          num: 0
        },
        {
          icon: this.$config.imgUrl + 'sk-4.png',
          project_scale: 4,
          scale: '小(1)型',
          num: 0
        },
        {
          project_scale: 5,
          scale: '小(2)型',
          icon: this.$config.imgUrl + 'sk-5.png',
          num: 0
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
    this.getReservoirList()
  },
  methods: {
    async getReservoirList() {
      this.skScale.map(v=>v.num = 0);
      // this.skScale.map((v,i)=>{
      //   v.num = 0;
      //   this.$set(this.skScale, i, v)
      // });
      this.apiOnce = true
      this.pointArr = {
        '绿码': [],
        '红码': [],
        '黄码': [],
      }
      let opt = {
        adcd: this.sgzgSelectAdcd ? this.sgzgSelectAdcd : this.$localData("get", "userInfo").adcd,
        basinCode: this.sgzgSelectBasin,
        gpType: 1,
        projectScale: this.projectType,
        screen: this.projectStatus,
      }
      this.$parent.$parent.$refs.skzgMap.removeAllLayer()
      let res = await generateProject(opt);
      if (res.success) {
        this.apiOnce = false
        if (res.data.length) {
          res.data.map((v) => {
            v.lat = v.LTTD;
            v.lng = v.LGTD;
            v.mapType = "reservoirPopup";
            v.styleObj = {
              scale: .5
            }
            switch (v.CODE) {
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
            this.skScale.map(x=>{
              if(x.project_scale == v.project_scale){
                x.num++
              }
            })
          });
        }
        this.showPoints()
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
<style>
.el-checkbox-group {
  margin-left: 15px;
}
.el-radio-group {
  margin-left: 10px;
}
</style>