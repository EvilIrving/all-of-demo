<!--
 * @Author: dyn
 * @Date: 2021-07-01 18:25:51
 * @LastEditTime: 2021-12-27 16:54:24
 * @LastEditors: dtb
 * @Description: 水情统计卡片
 * @FilePath: \lucheng\src\views\water_disaster_custom\countCards\SluiceCount.vue
-->
<template>
  <count-wrap title="水情" class="sluice-count">
    <el-row class="has-count">
      <el-col :span="6" class="fs24">
        <p>水库水位超汛限</p>
        <p @click="online('RR')" style="cursor:pointer">
          <span class="water-yellow fs36">{{ reservoirStatistical_a.value }}</span>
          <span class="fs24"> /{{ reservoirStatistical_a.total}}</span>
        </p>
      </el-col>
      <el-col :span="6" class="fs24">
        <p>河道水位超警</p>
        <p @click="online('ZZ')" style="cursor:pointer">
          <span class="water-yellow fs36">{{ reservoirStatistical_b.value }}</span>
          <span class="fs24"> /{{ reservoirStatistical_b.total}}</span>
        </p>
      </el-col>
      <el-col :span="6" class="fs24">
        <p>堰闸水位超警</p>
        <p @click="online('DD')" style="cursor:pointer">
          <span class="water-yellow fs36">{{ reservoirStatistical_c.value }}</span>
          <span class="fs24"> /{{ reservoirStatistical_c.total}}</span>
        </p>
      </el-col>
      <el-col :span="6" class="fs24">
        <p>潮位超警</p>
        <p @click="online('TT')" style="cursor:pointer">
          <span class="water-yellow fs36">{{ reservoirStatistical_d.value }}</span>
          <span class="fs24"> /{{ reservoirStatistical_d.total}}</span>
        </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import { waterAndRain } from '@/api/bigScreenPart';
import CountWrap from '../components/CountWrap.vue';
import { cloneObj } from '@/utils/utils';
export default {
  name: 'SluiceCount',
  data() {
    return {
      //水情统计数据
      info: {
        blueLength: 0,
        orangeLength: 0,
        redLength: 0,
        yellowLength: 0
      },
      timer: null,
      reservoirList: [],
      riverList: [],
      tideList: [],
      weirgateList: [],
      legendOptions: [
        {
          value: 'reservoir',
          name: '水库',
          mark: 'wr'
        },
        {
          value: 'tide',
          name: '潮位',
          mark: 'wr'
        },
        {
          value: 'river',
          name: '河道',
          mark: 'wr'
        },
        {
          value: 'weirgate',
          name: '堰闸',
          mark: 'wr'
        }
      ],
      reservoirStatistical_a: {}, //水库
      reservoirStatistical_b: {}, //河道
      reservoirStatistical_c: {}, //闸门
      reservoirStatistical_d: {} //潮位
    };
  },
  components: { CountWrap },
  mounted() {
    this.WaterRegime('RR');
    this.WaterRegime('ZZ');
    this.WaterRegime('DD');
    this.WaterRegime('TT');
    this.timer = setInterval(() => {
      this.WaterRegime('RR');
      this.WaterRegime('ZZ');
      this.WaterRegime('DD');
      this.WaterRegime('TT');
    }, 300000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  computed: {
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },
  methods: {
    /**
     * 水情统计数据
     */
    async WaterRegime(stationType) {
      const opt = { stationType };
      let res = await waterAndRain.waterStationsList(opt);
      if (res.code == 0) {
        let data = res.data.filter((item) => item.warning == true);
        let LevelLimit = data.length;
        let levelNumber = res.data.length;
        if (stationType == 'RR') {
          this.reservoirStatistical_a = { value: LevelLimit, total: levelNumber };
        } else if (stationType == 'ZZ') {
          this.reservoirStatistical_b = { value: LevelLimit, total: levelNumber };
        } else if (stationType == 'DD') {
          this.reservoirStatistical_c = { value: LevelLimit, total: levelNumber };
        } else if (stationType == 'TT') {
          this.reservoirStatistical_d = { value: LevelLimit, total: levelNumber };
        }
      }
    },
    /**
     * 水情点位开关
     */
    online(type) {
      this.legendOptions.map((item) => {
        this.setPoint([], item.value, 'normal', false);
        this.setPoint([], item.value, 'warn', false);
      });
      this.getRightMapList(type);
    },
    /**
   * 点位list
   */
    async getRightMapList(type) {
      let opt = { stationType: type };
      let res = await waterAndRain.waterStationsList(opt);
      if (res.code == 0) {
        let arr = res.data;
        this.setMapPlace(arr, type);
      }
    },
    setMapPlace(arr, type) {
      arr.map((item) => {
        let obj = {
          name: item.stName,
          lat: item.stLat,
          lng: item.stLong,
          code: item.stCode,
          stType: item.stType,
          warning: item.warning,
          type: '水情',
          props: {
            名称: item.stName,
            水位: item.waterLevel,
            更新时间: item.time
          }
        };
        if (type === 'RR') {
          this.reservoirList.push(obj);
        } else if (type === 'ZZ') {
          this.riverList.push(obj);
        } else if (type === 'TT') {
          this.tideList.push(obj);
        } else {
          this.weirgateList.push(obj);
        }
      });
      // ['水库','河道', '堰闸','潮位',],
      // RR ZZ  DD TT
      switch (type) {
        case 'RR':
          let warnList = [];
          let normalList = [];
          this.reservoirList.map((i) => {
            if (i.warning) {
              warnList.push(i);
            } else {
              normalList.push(i);
            }
          });
          this.setPoint(warnList, 'reservoir', 'warn', true);
          this.setPoint(normalList, 'reservoir', 'normal', true);
          break;
        case 'ZZ':
          let normalRiverList = [];
          let warnRiverList = [];
          this.riverList.map((i) => {
            if (i.warning) {
              warnRiverList.push(i);
              } else {
              normalRiverList.push(i);
            }
          });
          this.setPoint(normalRiverList, 'river', 'normal', true);
          this.setPoint(warnRiverList, 'river', 'warn', true);
          break;
        case 'DD':
          let warnWeirgateList = [];
          let normalWeirgateList = [];
          this.weirgateList.map((i) => {
            if (i.warning) {
              warnWeirgateList.push(i);
            } else {
              normalWeirgateList.push(i);
            }
          });
          this.setPoint(normalWeirgateList, 'weirgate', 'normal', true);
          this.setPoint(warnWeirgateList, 'weirgate', 'warn', true);
          break;
        default:
          let warnTideList = [];
          let normalTideList = [];
          this.tideList.map((i) => {
            if (i.warning) {
              warnTideList.push(i);
              } else {
                normalTideList.push(i);
            }
          });
          this.setPoint(normalTideList, 'tide', 'normal', true);
          this.setPoint(warnTideList, 'tide', 'warn', true);
          break;
      }
    },

    /**
     * @function: 设置点位样式
     * @description: 
     * @param {*} projectType 点位类型
     * @param {*} value 点位图标后缀
     * @return {*}
   */
    getPointStyle(projectType, value) {
      let defaultStyle = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: 'pixels',
        srcs: null,
        src: null
      };
      let styleObj = cloneObj(defaultStyle);
      styleObj.src = require(`@/assets/images/MapPoint/point-${projectType}-${value}.png`);
      styleObj.srcs = require(`@/assets/images/MapPoint/group-${projectType}-${value}.png`);
      return styleObj;
    },
    /**
      * @function:  
      * @description: 设置点位
      * @param {*} pointList 点位列表
      * @param {*} type  类型
      * @param {*} status  状态，配合类型用以设置图层名
      * @param {*} flag 是否设置样式
      * @return {*}
    */
    setPoint(pointList, type, status, flag) {
      let pointStyle = flag ? this.getPointStyle(type, status) : {};
      let layer = type + status;
      this.map.drawClusterPoint(pointList, pointStyle, `${layer}Layer`);
    },
  }
};
</script>
<style lang="scss" scoped>
@import './style/CountCard';
.sluice-count .has-count {
  height: 100%;
  .el-col {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    height: 100%;
  }
  p {
    text-align: center;
  }
}
</style>
