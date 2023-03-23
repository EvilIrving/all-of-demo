<!--
 * @Date: 2022-01-13 14:16:12
 * @Author: dtb
 * @Description:除险加固图例
 * @LastEditors: dtb
 * @LastEditTime: 2022-12-20 20:49:31
 * @FilePath: \rscp-big-screen\src\views\intelligentModule\skzgScreenLegend\components\Reinforce.vue
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
    <div class="line"></div>
    <el-checkbox-group v-model="checkedScale" @change="getList">
      <el-checkbox :disabled='apiOnce' v-for="item in skScale" :label="item.scale" :key="item.scale">
        <p class="legend_label">
          <img :src="item.icon" alt="">
          <span>{{item.scale}}</span>
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
      checkedScale: [],
      skScale: [
        {
          scale: '二类坝',
          icon: this.$config.imgUrl + 'classDam.png',
        },
        {
          scale: '三类坝',
          icon: this.$config.imgUrl + 'classDam.png',
        },
      ],
      checkedStatus: ['未开工'],
      skStatus: [
        {
          icon: this.$config.zhImgUrl + 'notStart001.png',
          scale: '未开工',
          num: 0
        },
        {
          icon: this.$config.zhImgUrl + 'started001.png',
          scale: '已开工',
          num: 0
        },
        {
          icon: this.$config.zhImgUrl + 'currentYear001.png',
          scale: '当年完工',
          num: 0
        },
      ],
      pointArr: {
        '未开工': [],
        '已开工': [],
        '当年完工': [],
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
    sgzgCxjgCl() {
      return this.$store.state.sgzgCxjgCl
    },
    projectType() {
      let tempArr = []
      let checkedArr = [...this.checkedStatus, ...this.checkedScale]
      checkedArr.forEach(element => {
        switch (element) {
          case '未开工':
            tempArr.push(6)
            break;
          case '已开工':
            tempArr.push(7)
            break;
          case '当年完工':
            tempArr.push(8)
            break;
          case '二类坝':
            tempArr.push(9)
            break;
          case '三类坝':
            tempArr.push(10)
            break;
        }
      });
      return tempArr.join(',')
    },
  },
  watch: {
    sgzgSelectAdcd(val) {
      this.getList()
    },
    sgzgSelectBasin(val) {
      this.getList()
    },
    sgzgCxjgCl(val){
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
      let defultSelected1 = this.defultSelected.split(',')[1]
      let defultSelected2 = this.defultSelected.split(',')[0]
      this.checkedScale = [defultSelected1]
      if( defultSelected2== '总数'){
        this.checkedStatus = ['未开工', '已开工', '当年完工']
      }else{
        this.checkedStatus = [defultSelected2]
      }

    },
    changeScale(value) {
      this.showPoints()
    },
    async getList() {
      this.skStatus.map(v=>v.num = 0);
      this.apiOnce = true
      this.pointArr = {
        '未开工': [],
        '已开工': [],
        '当年完工': [],
      }
      let opt = {
        adcd: this.sgzgSelectAdcd ? this.sgzgSelectAdcd : this.$localData("get", "userInfo").adcd,
        basinCode: this.sgzgSelectBasin,
        gpType: 3,
        screen: this.projectType,
        extractPram: this.sgzgCxjgCl
      }
      this.$parent.$parent.$refs.skzgMap.removeAllLayer()
      let res = await generateProject(opt);
      if (res.success) {
        this.apiOnce = false
        if (res.data.length) {
          res.data.map((v) => {
            v.lat = v.LTTD;
            v.lng = v.LGTD;
            v.mapType = "cxjgPopup";
            v.styleObj = {
              scale: .6
            }
            switch (v.status) {
              case '未开工':
                v.styleObj.src = this.$config.zhImgUrl + 'notStart001.png'
                this.pointArr['未开工'].push(v)
                break;
              case '已开工':
                v.styleObj.src = this.$config.zhImgUrl + 'started001.png'
                this.pointArr['已开工'].push(v)
                break;
              case '当年完工':
                v.styleObj.src = this.$config.zhImgUrl + 'currentYear001.png'
                this.pointArr['当年完工'].push(v)
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