<!--
 * @Author: dyn
 * @Date: 2021-07-01 18:25:51
 * @LastEditTime: 2022-04-15 15:26:07
 * @LastEditors: Please set LastEditors
 * @Description: 极简模式卡片
 * @FilePath: \lucheng\src\views\water_disaster_custom\components\simple\SimpleCount.vue
-->
<template>
  <div class="center-list">
    <div style="position:relative;height:200px">
      <simple-count-wrap title="风险隐患" class="seawall-count" :class="{ 'drag-risk-card': riskIcon }">
        <el-row class="has-count">
          <el-col :span="5" class="fs16" :class="{'active': alarmChecked_a}">
            <p class="fs16">海塘报警</p>
            <p @click="checkbox(1)">
              <span style="font-size: 30px;color:#eec80b;">{{ seawallAlarm.bj  }}</span>个
            </p>
          </el-col>
          <el-col :span="5" class="fs16" :class="{'active': alarmChecked_b}">
            <p>海塘预警</p>
            <p @click="checkbox(2)">
              <span style="font-size: 30px;color:#eec80b;">{{ seawallWarningData.bj }}</span>个
            </p>
          </el-col>
          <el-col :span="5" class="fs16" :class="{'active': alarmChecked_c}">
            <p>位移报警</p>
            <p @click="checkbox(3)">
              <span style="font-size: 30px;color:#eec80b;">{{ displacementList.length }}</span>个
            </p>
          </el-col>
          <el-col :span="5" class="fs16" :class="{'active': alarmChecked_d}">
            <p>沉降报警</p>
            <p @click="checkbox(4)">
              <span style="font-size: 30px;color:#eec80b;">{{  sedimentationList.length }}</span>个
            </p>
          </el-col>
          <el-col :span="4" class="fs16" :class="{'active': alarmChecked_e}">
            <p>台风影响</p>
            <p @click="checkbox(5)">
              <span style="font-size: 30px;color:#eec80b;">{{typhoonData.length }}</span>个
            </p>
          </el-col>
        </el-row>
        <MapPoint ref="point" />
      </simple-count-wrap>
      <div class="risk-left-icon" :class="{ 'risk-icon': riskIcon }" @click="riskChange"></div>
    </div>
  </div>

</template>

<script>
import { disasterPreventionApi } from "@/api/RightSildePanel";
import MapPoint from './components/MapPoint.vue';
import { mapActions, mapGetters } from 'vuex';
import { alarmPeople } from '@/api/bigScreenPart';
import SimpleCountWrap from "../SimpleCountWrap.vue";
export default {
  name: "SluiceCount",
  data() {
    return {
      riskIcon: false,
      alarmChecked_a: false,
      alarmChecked_b: false,
      alarmChecked_c: false,
      alarmChecked_d: false,
      alarmChecked_e: false,
      seawallAlarm: {},
      seawallWarningData: {},
      displacementList: [],
      sedimentationList: [],
      typhoonData: [],
      wagaData: {},
      timer: null,
    };
  },
  components: { SimpleCountWrap, MapPoint },
  computed: {
    ...mapGetters(['TempZIndex', 'isBigScreen', 'simpleItem', 'simpleRangTime']),
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  watch: {
    simpleItem: function (val) {
      if (!val) {
        this.alarmChecked_a = false;
        this.alarmChecked_b = false;
        this.alarmChecked_c = false;
        this.alarmChecked_d = false;
        this.alarmChecked_e = false;
      }
    },
    simpleRangTime: async function (val) {
      if (this.alarmChecked_a) {
        await this.seawallGiveAnAlarm();
        await this.seawallHand(this.seawallAlarm.list);
        await this.setSimplePotenData(this.seawallAlarm);
      } else if (this.alarmChecked_b) {
        await this.seawallWarning();
        await this.seawallHand(this.seawallWarningData.list);
        await this.setSimplePotenData(this.seawallWarningData);
      } else if (this.alarmChecked_c) {
        await this.displacement(1, true);
        await this.setSimplePotenData(this.displacementList);
      } else if (this.alarmChecked_d) {
        await this.displacement(2, true);
        await this.setSimplePotenData(this.sedimentationList);
      } else {
        await this.typhoonInfluence();
        await this.setSimplePotenData(this.typhoonData);
      }
    }
  },
  mounted() {
    this.SetTempZindex();
    this.seawallGiveAnAlarm();
    this.seawallWarning();
    this.typhoonInfluence();
    this.displacement(1);
    this.displacement(2);
    this.timer = setInterval(() => {
      this.SetTempZindex();
      this.seawallGiveAnAlarm();
      this.seawallWarning();
      this.typhoonInfluence();
      this.displacement(1);
      this.displacement(2);
    }, 300000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    ...mapActions(['SetTempZindex', 'setSimpleRightType', 'setSimpleItem', 'setSimpleTimes', 'setSimplePotenData']),
    riskChange() {
      this.riskIcon = !this.riskIcon;
    },
    /* 
     * @function: 
     * @description: 
     * @param {*} dataList
     * @param {*} type
     * @return {*}
     */
    seawallHand(dataList, type) {
      this.map.removeLineLayer();
      this.map.removeFocusLineLayer();
      for (let i = 0; i < dataList.length; i++) {
        let seawall = dataList[i];
        let color = '';
        if (!type) {
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
          if (seawall.jclxType) {
            color = '#E80000';
          } else {
            color = '#1CFFF5';
          }
        }
        let style = {
          lineWidth: 4,
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
        this.map.drawLineEx(lineList, style);
      }
    },
    checkbox(num) {
      switch (num) {
        case 1:
          this.alarmChecked_a = !this.alarmChecked_a
          this.alarmChecked_b = false;
          this.alarmChecked_c = false;
          this.alarmChecked_d = false;
          this.alarmChecked_e = false;
          if (this.alarmChecked_a) {
            this.setSimpleItem('海塘报警');
            this.setSimplePotenData(this.seawallAlarm);
            this.$refs.point.removeLayer();
            this.seawallHand(this.seawallAlarm.list);
          } else {
            this.$refs.point.getData();
            this.setSimpleItem('');
          }
          break;
        case 2:
          this.alarmChecked_b = !this.alarmChecked_b
          this.alarmChecked_a = false;
          this.alarmChecked_c = false;
          this.alarmChecked_d = false;
          this.alarmChecked_e = false;
          if (this.alarmChecked_b) {
            this.setSimpleItem('海塘预警');
            this.setSimplePotenData(this.seawallWarningData);
            this.$refs.point.removeLayer();
            this.seawallHand(this.seawallAlarm.list);
          } else {
            this.$refs.point.getData();
            this.setSimpleItem('');
          }
          break;
        case 3:
          this.alarmChecked_c = !this.alarmChecked_c
          this.alarmChecked_a = false;
          this.alarmChecked_b = false;
          this.alarmChecked_d = false;
          this.alarmChecked_e = false;
          if (this.alarmChecked_c) {
            this.setSimpleItem('位移报警');
            this.setSimplePotenData(this.displacementList);
            this.$refs.point.removeLayer();
            this.displacement(1);
          } else {
            this.$refs.point.getData();
            this.setSimpleItem('');
          }
          break;
        case 4:
          this.alarmChecked_d = !this.alarmChecked_d
          this.alarmChecked_a = false;
          this.alarmChecked_b = false;
          this.alarmChecked_c = false;
          this.alarmChecked_e = false;
          if (this.alarmChecked_d) {
            this.$refs.point.removeLayer();
            this.setSimpleItem('沉降报警');
            this.setSimplePotenData(this.sedimentationList);
            this.displacement(2);
          } else {
            this.$refs.point.getData();
            this.setSimpleItem('');
          }
          break;
        case 5:
          this.alarmChecked_e = !this.alarmChecked_e
          this.alarmChecked_a = false;
          this.alarmChecked_b = false;
          this.alarmChecked_c = false;
          this.alarmChecked_d = false;
          if (this.alarmChecked_e) {
            this.setSimpleItem('台风影响');
            this.setSimplePotenData(this.typhoonData);
          } else {
            this.$refs.point.getData();
            this.setSimpleItem('');
          }
          break;
      }
      this.setSimpleRightType(true);
    },
    handleChangeTimes(val) {
      this.setSimpleTimes(val);
    },
    async seawallGiveAnAlarm() {
      let res = await alarmPeople.seawallGiveAnAlarm({ time: this.simpleRangTime });
      if (res.code === 0) {
        res.data.list.map(i => {
          i.leftClick = false
        })
        this.seawallAlarm = res.data;
      }
    },
    async seawallWarning() {
      let res = await alarmPeople.seawallWarning({ time: this.simpleRangTime });
      if (res.code == 0) {
        this.seawallWarningData = res.data;
      }
    },
    async typhoonInfluence() {
      let res = await alarmPeople.typhoonInfluence({ date: this.simpleRangTime });
      if (res.code == 0) {
        this.typhoonData = res.data;
      }
    },
    /* 
     * @function: 
     * @description: 
     * @param {*} jclx 1位移，2沉降
     * @param {*} type
     * @return {*}
     */
    async displacement(jclx, type) {
      let res = await alarmPeople.displacement({ date: this.simpleRangTime, jclx });
      if (res.code == 0) {
        let list = [];
        if (jclx == 1) {
          this.displacementList = res.data;
          list = res.data;
        } else {
          this.sedimentationList = res.data;
          list = res.data;
        }
        if (type) {
          let data = this.seawallAlarm.list.map((item) => {
            let itemData = list.find((element) => element.gcbm == item.seawallCode);
            if (itemData) {
              item['jclxType'] = true;
            } else {
              item['jclxType'] = false;
            }
            return item;
          });
          this.seawallHand(data, true);
        }
      }
      
    },
    async wagaProjStats() {
      let obj = {
        hours: this.num,
      };
      await disasterPreventionApi.wagaProjStats(obj).then((res) => {
        if (res.code == 0) {
          this.wagaData = res.data;
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.seawall-count .has-count {
  display: flex;
  justify-content: space-around;
  align-items: center;
  width: 100%;
  height: 100%;
  position: relative;
  .el-col {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    width: 100px;
    height: 100px;
    > p:nth-child(2) {
      cursor: pointer;
    }
  }
  .el-col.active {
    background: center/100% url(~assets/images/simple_checked.png) no-repeat;
  }
}
.center-list {
  position: absolute;
  top: 140px;
  left: calc((100% - 1000px) / 2);
  width: 1000px;
  overflow: hidden;
  display: flex;
  align-items: center;
  z-index: 11;
  justify-content: center;
  &:hover {
    > div {
      i.icon-left-count,
      i.icon-right-count,
      i.icon-left-count-red,
      i.icon-right-count-red {
        visibility: visible;
      }
    }
  }
  > div {
    position: relative;
    i {
      position: absolute;
      &.icon-left-count {
        left: -70px;
        top: 50%;
        transform: translateY(-50%);
        visibility: hidden;
        &.big-screen {
          left: 128px;
        }
      }
      &.icon-left-count-red {
        left: -50px;
        top: 50%;
        transform: translateY(-50%);
        visibility: hidden;
        &.big-screen {
          left: 128px;
        }
      }
      &.icon-right-count {
        right: -70px;
        top: 50%;
        transform: translateY(-50%);
        visibility: hidden;
        &.big-screen {
          right: 128px;
        }
      }
      &.icon-right-count-red {
        right: -50px;
        top: 50%;
        transform: translateY(-50%);
        visibility: hidden;
        &.big-screen {
          right: 128px;
        }
      }
    }
  }
  .icon-left-count,
  .icon-right-count {
    cursor: pointer;
  }
}
.risk-left-icon {
  position: absolute;
  width: 60px;
  height: 60px;
  background: url(~assets/images/shousuo.png) no-repeat;
  background-size: 100% 100%;
  left: calc(50% - 25px);
  top: 70%;
  cursor: pointer;
  &.risk-icon {
    background: url(~assets/images/zhankai.png) no-repeat;
    background-size: 100% 100%;
    top: 5%;
  }
}
.drag-risk-card {
  visibility: hidden;
}
</style>
