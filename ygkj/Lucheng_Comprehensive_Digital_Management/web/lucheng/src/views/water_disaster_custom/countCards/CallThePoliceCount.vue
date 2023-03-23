<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-12-27 17:01:56
 * @LastEditors: dtb
 * @Description: In User Settings Edit
 * @FilePath: \lucheng\src\views\water_disaster_custom\countCards\CallThePoliceCount.vue
-->
<template>
  <count-wrap title="报警" class="seawall-count">
    <el-row class="has-count">
      <el-col :span="4" style="cursor:pointer" @click.native="getSeawallList('预报预警')">
        <p class="fs16">海塘预警数量</p>
        <p>
          <span class="work-green" style="font-size: 30px">{{ warnNum.seawallWarning }}</span>个
        </p>
      </el-col>
      <el-col :span="4" class="fs16" style="cursor:pointer" @click.native="getSeawallList('实时报警')">
        <p class="fs16">海塘报警数量</p>
        <p>
          <span style="font-size: 30px;color:#eec80b">{{ warnNum.seawallGiveAnAlarm }}</span>个
        </p>
      </el-col>
      <el-col :span="4" class="fs16" style="cursor:pointer" @click.native="waterRainfallRegime('water')">
        <p>水位报警数量</p>
        <p>
          <span style="font-size: 30px;color:#eec80b;">{{ warnNum.waterGiveAnAlarm }}</span>个
        </p>
      </el-col>
      <el-col :span="4" class="fs16" style="cursor:pointer" @click.native="waterRainfallRegime('rain')">
        <p>降雨报警数量</p>
        <p>
          <span style="font-size: 30px;color:#eec80b">{{ warnNum.rainGiveAnAlarm }}</span>个
        </p>
      </el-col>
      <el-col :span="4" class="fs16" style="cursor:pointer" @click.native="typhoonList">
        <p>台风数量</p>
        <p>
          <span style="font-size: 30px;color:#eec80b">{{ warnNum.typhoonGiveAnAlarm }}</span>个
        </p>
      </el-col>
      <el-col :span="4" class="fs16" style="cursor:pointer" @click.native="getSeawallList('运行报警')">
        <p>运行报警数量</p>
        <p>
          <span style="font-size: 30px;color:#eec80b">{{ warnNum.runGiveAnAlarm }}</span>个
        </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import CountWrap from '../components/CountWrap.vue';
import { alarmPeople } from '@/api/bigScreenPart';
import { cloneObj } from '@/utils/utils';
export default {
  name: 'StagnantWaterCount',
  data() {
    return {
      warnNum: {},
      drawFlag: false,
      dataList: [],
      dataSeawallList: [],
      typhoonData: [], //台风
      legendOptions: [
        {
          value: 1,
          name: '实时报警',
        },
        {
          value: 2,
          name: '预报预警',
        },
        {
          value: 3,
          name: '运行报警',
        },
      ],
      waterOptions: [
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
      waterRainData: [], //水情
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
    this._waterLoggingStats();
    this.timer = setInterval(() => {
      this._waterLoggingStats();
    }, 300000);

  },
  beforeDestroy() {
    clearInterval(this.timer);
    this.map.clearAllTyphoon();
    this.map.removeLineLayer();
    this.map.removeFocusLineLayer();
    this.$emit('legend-change', '海塘', {});
    this.map.drawClusterPoint([], {}, `dryBrakeLayer`);
    this.map.drawClusterPoint([], {}, `wharfLayer`);
    this.map.drawClusterPoint([], {}, `intersectLayer`);
    this.map.drawClusterPoint([], {}, `bridgeLayer`);
    this.map.drawClusterPoint([], {}, `stakeLayer`);
    this.map.drawClusterPoint([], {}, `culvertLayer`);
    this.map.drawClusterPoint([], {}, `sluiceLayer`);
    this.map.drawClusterPoint([], {}, `pumpLayer`);
    // this.map.drawClusterPoint([], {}, `pipeLayer`);
    this.map.drawClusterPoint([], {}, `rainbowLayer`);
    this.map.drawClusterPoint([], {}, `roomLayer`);
    this.map.drawClusterPoint([], {}, `stationLayer`);
    this.map.drawClusterPoint([], {}, `watergateLayer`);
  },
  methods: {
    async _waterLoggingStats() {
      let res = await alarmPeople.giveAnAlarmCount();
      this.warnNum = res.data;
    },
    removeLineLayer() {
      this.map.clearAllTyphoon();
      this.map.removeLineLayer();
      this.map.removeFocusLineLayer();
      this.$emit('legend-change', '海塘', {});
      this.$emit('legend-change', '实时降雨量', {});
      this.map.drawClusterPoint([], {}, `dryBrakeLayer`);
      this.map.drawClusterPoint([], {}, `wharfLayer`);
      this.map.drawClusterPoint([], {}, `intersectLayer`);
      this.map.drawClusterPoint([], {}, `bridgeLayer`);
      this.map.drawClusterPoint([], {}, `stakeLayer`);
      this.map.drawClusterPoint([], {}, `culvertLayer`);
      this.map.drawClusterPoint([], {}, `sluiceLayer`);
      this.map.drawClusterPoint([], {}, `pumpLayer`);
      // this.map.drawClusterPoint([], {}, `pipeLayer`);
      this.map.drawClusterPoint([], {}, `rainbowLayer`);
      this.map.drawClusterPoint([], {}, `roomLayer`);
      this.map.drawClusterPoint([], {}, `stationLayer`);
      this.map.drawClusterPoint([], {}, `watergateLayer`);
      this.map.loadRainfallPoint([], false);
      this.waterOptions.map((item) => {
        this.setLegend(item.name);
        this.setPoint([], item.value, 'normal', false);
        this.setPoint([], item.value, 'warn', false);
      });
    },
    //海塘数据
    async getSeawallList(type) {
      this.removeLineLayer()
      let res;
      switch (type) {
        case '实时报警':
          res = await alarmPeople.seawallGiveAnAlarm();
          break;
        case '预报预警':
          res = await alarmPeople.seawallWarning();
          break;
        default:
          res = await alarmPeople.runGiveAnAlarm();
          break;
      }
      if (res.code === 0) {
        switch (type) {
          case '运行报警':
            this.dataList = res.data.list.filter(i => i.danger === 0 || i.flood === 0 || i.patrol === 0 || i.safety === 0);
            break;
          default:
            this.dataList = res.data.list.filter(i => i.status !== 1);
            break;
        }
        this.seawallHand(this.legendOptions.filter(i => i.name === type)[0].value)
      }
    },
    //水情 雨情数据
    async waterRainfallRegime(type) {
      this.removeLineLayer()
      let res;
      switch (type) {
        case 'water':
          res = await alarmPeople.waterGiveAnAlarm();
          break;
        default:
          res = await alarmPeople.rainGiveAnAlarm();
          break;
      }
      if (res.code === 0) {
        if (type === 'water') {
          this.waterRainData = res.data.list.filter(i => i.warning);
          this.watchRainPoint()
        } else {
          this.waterRainData = res.data.list.filter(i => i.status);
          this.loadRainfallPoint()
        }
      }
    },
    //台风数据
    async typhoonList() {
      this.removeLineLayer()
      let res = await alarmPeople.typhoonGiveAnAlarm();
      if (res.code === 0) {
        if (res.data.list.length > 0) {
          this.typhoonData = res.data.list.map((item) => {
            return {
              name: item.name,
              limitLevel: item.points[item.points.length - 1].distance,
              waterLevel: item.points[item.points.length - 1].power,
              data: item
            };
          });
          this.handleStagnantTyphoon()
        }
      } else {
        this.typhoonData = res.data.list;
        this.handleStagnantTyphoon()
      }

    },
    //海塘地图点位
    seawallHand(type) {
      for (let i = 0; i < this.dataList.length; i++) {
        let seawall = this.dataList[i];
        let color = '#1CFFF5';
        if (type !== 3) {
          if (seawall.status == 1) {
            color = '#1CFFF5';
          } else if (seawall.status == 2) {
            color = '#008FE8';
          } else if (seawall.status == 3) {
            color = '#E99100';
          } else if (seawall.status == 4) {
            color = '#FF551F';
          } else if (seawall.status == 5) {
            color = '#E80000';
          }
        } else {
          if (seawall.context) {
            color = '#E99100';
          }
        }
        let style = {
          lineWidth: 6,
          lineColor: color
        };
        let lineString = (seawall.coorpot && seawall.coorpot.replace(/&quot;/g, "'")) || '[]';
        let lineList = lineString && eval(lineString);
        lineList = lineList.map((line) => {
          line = line.map((item) => {
            return [item.lng, item.lat];
          });
          return {
            lineList: line,
            data: {
              type: '海塘',
              name: seawall.seawallName,
              code: seawall.seawallCode,
              props: {
                海塘长度: seawall.seawallLength + 'm',
                防潮标准: `${seawall.preventTideStandard}年`,
                所在位置: seawall.village
              },
              ...seawall
            }
          };
        });
        if (type !== 3) {
          this.$emit('legend-change', '海塘', {
            label: '海塘',
            children: [
              {
                label: '正常',
                icon: 'seawall-type-101'
              },
              {
                label: '超蓝色警戒',
                icon: 'seawall-type-102'
              },
              {
                label: '超黄色警戒',
                icon: 'seawall-type-20'
              },
              {
                label: '超橙色警戒',
                icon: 'seawall-type-103'
              },
              {
                label: '超红色警戒',
                icon: 'seawall-type-1'
              }
            ]
          });
        } else {
          this.$emit('legend-change', '海塘', {
            label: '海塘',
            children: [
              {
                label: '正常',
                icon: 'seawall-type-101'
              },
              {
                label: '报警',
                icon: 'seawall-type-20'
              }
            ]
          });
        }
        this.map.drawLineEx(lineList, style);
      }
    },
    //水情落点
    watchRainPoint() {
      let dataList_reservoir = []; //水库
      let dataList_riverway = []; //河道
      let dataList_weir_gate = []; //堰闸
      let dataList_sea_level = []; //潮位
      this.waterRainData.map((item) => {
        let obj = {
          name: item.stName,
          lat: item.stLat,
          lng: item.stLong,
          code: item.stCode,
          warning: item.warning,
          stType: item.stType,
          type: '水情',
          props: {
            名称: item.stName,
            水位: item.waterLevel,
            更新时间: item.time
          }
        };
        // ['水库','河道', '堰闸','潮位',],
        if (item.stType == 'RR') {
          dataList_reservoir.push(obj);
        } else if (item.stType == 'ZZ') {
          dataList_riverway.push(obj);
        } else if (item.stType == 'TT') {
          dataList_sea_level.push(obj);
        } else {
          dataList_weir_gate.push(obj);
        }
      });
      this.getRightMapList(dataList_reservoir, 'reservoir');
      this.getRightMapList(dataList_riverway, 'river');
      this.getRightMapList(dataList_weir_gate, 'weirgate');
      this.getRightMapList(dataList_sea_level, 'tide');
      this.waterOptions.map((item) => {
        this.setLegend(item.name, item.mark);
      });
    },
    //水情落点处理
    async getRightMapList(arr, type) {
      let warnList = [];
      let normalList = [];
      arr.map((i) => {
        if (i.warning) {
          warnList.push(i);
        } else {
          normalList.push(i);
        }
      });
      this.setPoint(warnList, type, 'warn', true);
      this.setPoint(normalList, type, 'normal', true);
    },
    //降雨落点
    loadRainfallPoint() {
      let arrData = this.waterRainData.map((item) => {
        return {
          longitude: item.st_long,
          latitude: item.st_lat,
          stationName: item.stName,
          mgstcd: item.stcd,
          drp: item.drp == '-1' ? '-' : item.drp,
          stLoc: item.st_loc
        };
      });
      this.map.loadRainfallPoint(arrData, true);
      this.$emit('legend-change', '实时降雨量', {
        label: '实时降雨量',
        children: [
          { label: '>250', icon: 'realtime-250' },
          { label: '100~250', icon: 'realtime-100' },
          { label: '50~100', icon: 'realtime-50' },
          { label: '25~50', icon: 'realtime-25' },
          { label: '10~25', icon: 'realtime-10' },
          { label: '0.1~10', icon: 'realtime-0_1' }
        ]
      });
    },
    //台风地图落点
    handleStagnantTyphoon() {
      this.map.clearAllTyphoon();
      this.typhoonData.map((item) => {
        let points = eval(item.data.points);
        const data = {
          ...item.data,
          points
        };
        this.map.showTyphoon(data);
      });
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
      let layer = type + status;
      this.map.drawClusterPoint(pointList, pointStyle, `${layer}Layer`);
    },

    /**
     * @description: 设置图例
     * @param {String} name 显示图例名称cn
     * @param {String} value 工程类别en
     * @return {*}
     */
    setLegend(name, value) {
      if (value) {
        this.$emit('legend-change', name, {
          label: name,
          children: [
            {
              label: '正常',
              icon: 'icon-kaiqi'
            },
            {
              label: '超警',
              icon: 'icon-guanbi'
            }
          ]
        });
      } else {
        this.$emit('legend-change', name, {});
      }
    },

  }
};
</script>
<style lang="scss" scoped>
@import './style/CountCard';
.seawall-count .has-count {
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
