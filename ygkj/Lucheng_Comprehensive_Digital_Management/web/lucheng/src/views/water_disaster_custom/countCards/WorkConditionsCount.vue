<!--
 * @Author: dyn
 * @Date: 2021-07-01 18:25:51
 * @LastEditTime: 2021-12-08 14:54:51
 * @LastEditors: dtb
 * @Description:工情统计卡片
 * @FilePath: \lucheng\src\views\water_disaster_custom\countCards\WorkConditionsCount.vue
-->
<template>
  <count-wrap title="工情" class="sluice-count">
    <el-row class="has-count">
      <el-col :span="8" class="fs24">
        <p>水闸开启数量</p>
        <p @click="online('水闸')" style="cursor:pointer">
          <span class="work-green fs36">{{ info.wagaOpenNum }}</span>
          <span class="fs24"> /{{ info.wagaNum }}</span>
        </p>
      </el-col>
      <el-col :span="8" class="fs24">
        <p>水库开启数量</p>
        <p @click="online('水库')" style="cursor:pointer">
          <span class="work-green fs36">{{ info.rsvrOpenNum }}</span>
          <span class="fs24"> /{{ info.rsvrNum }}</span>
        </p>
      </el-col>
      <el-col :span="8" class="fs24">
        <p>泵站开启数量</p>
        <p @click="online('泵站')" style="cursor:pointer">
          <span class="work-green fs36">{{ info.pustOpenNum }}</span>
          <span class="fs24"> /{{ info.pustNum }}</span>
        </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import { WorkingConditionsApi } from '@/api/bigScreenPart';
import CountWrap from '../components/CountWrap.vue';
import { cloneObj } from '@/utils/utils';
export default {
  name: 'SluiceCount',
  layerName: ['workSluiceLayer', 'workReservoirLayer', 'workPumpLayer'],
  legendName: ['水库', '水闸', '泵站'],
  data() {
    return {
      //工情统计数据
      info: {
        blueLength: 0,
        orangeLength: 0,
        yellowLength: 0
      },
      reservoirList: [],
      sluiceList: [],
      pumpList: [],
      timer: null
    };
  },
  components: { CountWrap },
  computed: {
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  mounted() {
    this.WaterRegime();
    this.timer = setInterval(() => {
      this.WaterRegime();
    }, 300000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    /**
     * 工情统计数据
     */
    async WaterRegime() {
      await WorkingConditionsApi.workingConditionsTotal().then((res) => {
        if (res.code == 0) {
          this.info = res.data;
        }
      });
    },
    /**
     * 工情点位开关
     */
    online(type) {
      this.setPoint([], 'reservoir', 'normal', false);
      this.setPoint([], 'reservoir', 'warn', false);
      this.setPoint([], 'pump', 'normal', false);
      this.setPoint([], 'pump', 'warn', false);
      this.setPoint([], 'sluice', 'normal', false);
      this.setPoint([], 'sluice', 'warn', false);
      this.getRightMapList(type);
    },
    /**
    * 工情list  根据type获取不同类型
    */
    async getRightMapList(type) {
      let opt = { pjtp: type };
      let res = await WorkingConditionsApi.workingConditionsList(opt);
      if (res.code === 0) {
        if (res.data && res.data.length > 0) {
          this.setMapPlace(res.data, type);
        }
      }
    },
    setMapPlace(arr, type) {
      let sluiceList = [];
      let reservoirList = [];
      let pumpList = [];
      arr.map((item) => {
        let obj;
        if (type == '水闸') {
          obj = {
            name: item.wagaName,
            lat: item.stLat || item.startLat,
            lng: item.stLong || item.startLong,
            type: '工情',
            code: item.wagaCode ? item.wagaCode : item.stCode,
            stCode: item.stCode,
            isOpened: item.gateOpened,
            stType: 'DD',
            projectType: 'sluice',
            props: {
              水闸名称: item.wagaName,
              闸上水位: item.upz,
              闸下水位: item.dwz,
              过闸流量: item.ll,
              闸门开度: item.ovs
            }
          };
          sluiceList.push(obj);
        } else if (type == '水库') {
          obj = {
            name: item.resName,
            lng: item.lowLeftLong,
            lat: item.lowLeftLat,
            isOpened: item.rsvrOpened,
            type: '工情',
            code: item.mainStcd,
            stType: 'RR',
            projectType: 'reservoir',
            props: {
              水库名称: item.resName,
              水位: item.waterLevel,
              下泄流量: item.xxll
            }
          };
          reservoirList.push(obj);
        } else {
          obj = {
            name: item.projectName,
            isOpened: item.pustOpened,
            lat: item.latitude,
            lng: item.longitude,
            type: '工情',
            code: item.projectId,
            stType: 'PT',
            projectType: 'pump',
            props: {
              泵站名称: item.wagaName,
              进水池水位: item.jscZ,
              出水池水位: item.cscZ,
              流量: item.sum,
              水泵启闭: item.pustOpened ? '开启' : "关闭"
            }
          };
          pumpList.push(obj);
        }
      });
      if (type == '水闸') {
        let openlist = []
        let closedlist = []
        sluiceList.forEach(i => {
          // 开启用绿色 关闭用黄色
          if (i.isOpened) {
            openlist.push(i)
          } else {
            closedlist.push(i)
          }
        })
        console.log(openlist, 'openlist');
        console.log(closedlist, 'closedlist');
        this.setPoint(openlist, 'sluice', 'normal', true);
        this.setPoint(closedlist, 'sluice', 'warn', true);
      } else if (type == '水库') {
        let openlist = []
        let closedlist = []
        reservoirList.forEach(i => {
          if (i.isOpened) {
            openlist.push(i)
          } else {
            closedlist.push(i)
          }
        })
        this.setPoint(openlist, 'reservoir', 'normal', true);
        this.setPoint(closedlist, 'reservoir', 'warn', true);
      } else {
        let openlist = []
        let closedlist = []
        pumpList.forEach(i => {
          if (i.isOpened) {
            openlist.push(i)
          } else {
            closedlist.push(i)
          }
        })
        this.setPoint(openlist, 'pump', 'normal', true);
        this.setPoint(warnList, 'pump', 'warn', true);
      }
    },
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
      let typeUpper = type.charAt(0).toUpperCase() + type.slice(1);
      let layer = typeUpper + status;
      this.map.drawClusterPoint(pointList, pointStyle, `work${layer}Layer`);
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
