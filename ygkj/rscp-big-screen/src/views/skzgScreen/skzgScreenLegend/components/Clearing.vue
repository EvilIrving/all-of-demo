<!--
 * @Date: 2022-01-13 14:16:12
 * @Author: dtb
 * @Description:存量清零图例
 * @LastEditors: dtb
 * @LastEditTime: 2022-04-15 16:59:00
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenLegend\components\Clearing.vue
-->
<template>
  <div class='wrap'>
    <el-checkbox-group v-model="checkedStatus" @change="getList">
      <el-checkbox :disabled='apiOnce' v-for="item in skStatus" :label="item.scale" :key="item.scale" :checked="item.checked">
        <p class="legend_label">
          <img :src="item.icon" alt="">
          <span>{{item.scale}}</span>
        </p>
      </el-checkbox>
    </el-checkbox-group>
  </div>
</template>

<script>
import { cyjgNumApi } from "@/api/api_reservoir";
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
      checkedStatus: [],
      skStatus: [
        {
          checked: true,
          icon: this.$config.imgUrl + 'earlyStage.png',
          scale: '项目前期',
        },
        {
          checked: false,
          icon: this.$config.imgUrl + 'reinforcing.png',
          scale: '加固中',
        },
        {
          checked: false,
          icon: this.$config.imgUrl + 'completed.png',
          scale: '已完工',
        },
        {
          checked: false,
          icon: this.$config.imgUrl + 'completedAgain.png',
          scale: '已竣工',
        }
      ],
      //定一个状态 只允许请求一次 0：请求之前 1：请求结束
      apiOnce: true,
      pointArr: {
        '项目前期': [],
        '加固中': [],
        '已完工': [],
        '已竣工': [],
      },
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
          case '项目前期':
            temp.push(1)
            break;
          case '加固中':
            temp.push(2)
            break;
          case '已完工':
            temp.push(3)
            break;
          case '已竣工':
            temp.push(4)
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
    this.getList()
  },
  methods: {
    setSelect() {
      this.checkedStatus = this.defultSelected.split(',')
    },
    changeScale(value) {
      this.showPoints()
    },
    async getList() {
      this.apiOnce = true
      this.pointArr = {
        '项目前期': [],
        '加固中': [],
        '已完工': [],
        '已竣工': [],
      }
      let opt = {
        adcd: this.sgzgSelectAdcd ? this.sgzgSelectAdcd : this.$localData("get", "userInfo").adcd,
        basinCode: this.sgzgSelectBasin,
        interType: 2, //列表
        extractPram: this.projectType
      }
      this.$parent.$parent.$refs.skzgMap.removeAllLayer()
      let res = await cyjgNumApi(opt);
      if (res.success) {
        this.apiOnce = false
        let pointArr = []
        if (res.data.length) {
          res.data.map((v) => {
            v.lat = v.LTTD;
            v.lng = v.LGTD;
            v.mapType = "cxjgPopup";
            v.styleObj = {
              scale: .85
            }
            pointArr.push(v)
            if (v.status.indexOf('未加固') !== -1 || v.status.indexOf('拟降等报废') !== -1 || v.status.indexOf('待鉴定') !== -1 ) {
              v.styleObj.src = this.$config.imgUrl + 'earlyStage.png'
              this.pointArr['项目前期'].push(v)
            } else if (v.status.indexOf('加固中') !== -1) {
              v.styleObj.src = this.$config.imgUrl + 'reinforcing.png'
              this.pointArr['加固中'].push(v)
            } else if (v.status.indexOf('已完工') !== -1) {
              v.styleObj.src = this.$config.imgUrl + 'completed.png'
              this.pointArr['已完工'].push(v)
            } else if (v.status.indexOf('已竣工') !== -1 || v.status.indexOf('一类坝') !== -1) {
              v.styleObj.src = this.$config.imgUrl + 'completedAgain.png'
              this.pointArr['已竣工'].push(v)
            }

            // switch (v.status) {
            //   case '三类坝未加固':
            //   case 'dd':
            //     v.styleObj.src = this.$config.imgUrl + 'earlyStage.png'
            //     this.pointArr['项目前期'].push(v)
            //     break;
            //   case '三类坝加固中':
            //     v.styleObj.src = this.$config.imgUrl + 'reinforcing.png'
            //     this.pointArr['加固中'].push(v)
            //     break;
            //   case '三类坝已完工':
            //     console.log(v.status, 'v.status');
            //     v.styleObj.src = this.$config.imgUrl + 'completed.png'
            //     this.pointArr['已完工'].push(v)
            //     break;
            //   case '三类坝已竣工':
            //     v.styleObj.src = this.$config.imgUrl + 'completedAgain.png'
            //     this.pointArr['已竣工'].push(v)
            //     break;
            // }
          });
        }
        this.showPoints()
        // this.$parent.$parent.$refs.skzgMap.drawMapPoints(pointArr, 'reservoirPopup');
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