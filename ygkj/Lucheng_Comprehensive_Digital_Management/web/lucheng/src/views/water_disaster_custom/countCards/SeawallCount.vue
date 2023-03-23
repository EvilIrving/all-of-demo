<!--
 * @Author: dyn
 * @Date: 2021-07-01 18:29:11
 * @LastEditTime: 2022-05-23 16:37:34
 * @LastEditors: dtb
 * @Description: 海塘统计卡片
 * @FilePath: \lucheng\src\views\water_disaster_custom\countCards\SeawallCount.vue
-->

<template>
  <count-wrap title="海塘" class="seawall-count">
    <el-row class="has-count">
      <el-col :span="6" class="fs24">
        <p>蓝色</p>
        <p style="cursor:pointer" @click="seawallLine('blue')">
          <span class="blue fs36">{{ info.blueLen }}</span>km
        </p>
      </el-col>
      <el-col :span="6" class="fs24">
        <p>黄色</p>
        <p style="cursor:pointer" @click="seawallLine('yellow')">
          <span class="yellow fs36">{{ info.yellowLen }}</span>km
        </p>
      </el-col>
      <el-col :span="6" class="fs24">
        <p>橙色</p>
        <p style="cursor:pointer" @click="seawallLine('orange')">
          <span class="orange fs36">{{ info.orangeLen }}</span>km
        </p>
      </el-col>
      <el-col :span="6" class="fs24">
        <p>红色</p>
        <p style="cursor:pointer" @click="seawallLine('red')">
          <span class="red fs36">{{ info.redLen }}</span>km
        </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { alarmPeople } from '@/api/bigScreenPart';
export default {
  name: "SeawallCount",
  data() {
    return {
      info: {
        blueLength: 0,
        orangeLength: 0,
        redLength: 0,
        yellowLength: 0,
      },
      timer: null,
      seawallList: [],
      blinkFlag: false,
      highLightLines: [],
      style: {},
    };
  },
  components: { CountWrap },
  computed: {
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  mounted() {
    this.seawallWarnStatistic();
    this.timer = setInterval(() => {
      this.seawallWarnStatistic();
    }, 300000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
    clearInterval(this.blinkTimer)
    // this.highLightLines = []
    // this.map.drawLineEx([], style);
  },
  methods: {
    async seawallWarnStatistic() {
      await alarmPeople.seawallWarning().then((res) => {
        if (res.code == 0) {
          this.info = res.data;
        }
      });
    },
    /**
   * 海塘点位开关
   */
    seawallLine(type) {
      this.highLightLines = []
      clearInterval(this.blinkTimer)
      this.map.drawPoint([], {}, `dryBrakeLayer`);
      this.map.drawPoint([], {}, `wharfLayer`);
      this.map.drawPoint([], {}, `intersectLayer`);
      this.map.drawPoint([], {}, `bridgeLayer`);
      this.map.drawPoint([], {}, `stakeLayer`);
      this.map.drawClusterPoint([], {}, `culvertLayer`);
      this.map.drawClusterPoint([], {}, `sluiceLayer`);
      this.map.drawClusterPoint([], {}, `pumpLayer`);
      // this.map.drawClusterPoint([], {}, `pipeLayer`);
      this.map.drawClusterPoint([], {}, `rainbowLayer`);
      this.map.drawClusterPoint([], {}, `roomLayer`);
      this.map.drawClusterPoint([], {}, `stationLayer`);
      this.map.drawClusterPoint([], {}, `watergateLayer`);
      this.map.removeLineLayer();
      this.map.removeFocusLineLayer();
      let seawallList = this.seawallList.filter(i => i.warnState === type)
      for (let i = 0; i < seawallList.length; i++) {
        let seawall = seawallList[i];
        let color = '';
        if (type == 'red') {
          color = '#ef1313';
        } else if (type === 'yellow') {
          color = '#eec80b';
        } else if (type === 'orange') {
          color = '#ff551f';
        } else if (type === 'blue') {
          color = '#00bdf8';
        }
        this.style = {
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
                防潮标准: seawall.preventTideStandard + '年',
                所在位置: seawall.village
              },
              ...seawall
            }
          };
        });
        // console.log(lineList,'lineList');
        // this.map.drawLineEx(lineList, this.style);
        // 切换海塘线颜色
        this.highLightLines.push(lineList)
      }
      console.log(this.highLightLines, type, 'this.highLightLines');
      if (this.info[type + 'Length'] !== 0) {
        this.blinkTimer = setInterval(() => {
          this.drawSeaWallLinesInMap()
        }, 1000);
      }
    },
    drawSeaWallLinesInMap() {
      let style = {
        lineWidth: 6,
      }
      if (this.blinkFlag) {
        style.lineColor = '#fff'
        for (let index = 0; index < this.highLightLines.length; index++) {
          this.map.drawLineEx(this.highLightLines[index], style);
        }
        this.blinkFlag = !this.blinkFlag
      } else {
        for (let index = 0; index < this.highLightLines.length; index++) {
          this.map.drawLineEx(this.highLightLines[index], this.style);
        }
        this.blinkFlag = !this.blinkFlag
      }
    },
  },
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
