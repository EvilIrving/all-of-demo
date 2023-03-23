<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-05-20 16:42:11
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\components\simple\components\MapPoint.vue
-->
<template>
  <div></div>
</template>

<script>
import { WorkingConditionsApi } from '@/api/bigScreenPart';
import { disasterPreventionApi } from '@/api/RightSildePanel';
import { waterAndRain } from '@/api/bigScreenPart';
import { cloneObj } from '@/utils/utils';
export default {
  name: 'MapPoint',
  computed: {
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  mounted() {
    this.getData();
  },
  beforeDestroy() {
    this.removeLayer()
  },
  methods: {
    async getData() {
      await this.getSluiceList();
      await this.getSeaWallList();
      await this.getWaterList("TT");
      await this.getWaterList("ZZ");
    },
    //潮位 && 河道
    async getWaterList(type) {
      let opt = { stationType: type };
      let res = await waterAndRain.waterStationsList(opt);
      if (res.code === 0) {
        let tideList = [];
        let riverList = [];
        let arr = res.data;
        // type === 'ZZ' ? res.data.filter((item) => item.warning) : res.data
        arr.map((item) => {
          let obj = {
            name: item.stName,
            lat: item.stLat,
            lng: item.stLong,
            code: item.stCode,
            stType: item.stType,
            warning: item.warning,
            type: '水情',
          }
          obj['props'] = {
            名称: item.stName,
            水位: item.waterLevel ? item.waterLevel : '-',
            更新时间: item.time ? item.time : '-'
          }
          type === 'ZZ' ? riverList.push(obj) : tideList.push(obj)
        });
        if (type === 'ZZ') {
          let normalRiverList = [];
          let warnlRiverList = [];
          riverList.map((i) => {
            i.warning ? warnlRiverList.push(i) : normalRiverList.push(i)
          });
          this.setPoint(normalRiverList, 'river', 'normal', true);
          this.setPoint(warnlRiverList, 'river', 'warn', true);
        } else {
          let warnTideList = [];
          let normalTideList = [];
          tideList.map((i) => {
            i.warning ? warnTideList.push(i) : normalTideList.push(i)
          });
          this.setPoint(normalTideList, 'tide', 'normal', true);
          this.setPoint(warnTideList, 'tide', 'warn', true);
        }
      }

    },
    //海塘线
    async getSeaWallList() {
      let opt = {
        type: '海塘'
      };
      let res = await disasterPreventionApi.rightMapPoints(opt);
      if (res.code === 0 && res.data && res.data.length > 0) {
        for (let i = 0; i < res.data.length; i++) {
          let seawall = res.data[i];
          let color = '#E80000';
          if (seawall.designTideStandard < 10) {
            color = '#E80000';
          } else if (seawall.designTideStandard == 10) {
            color = '#DD00DB';
          } else if (seawall.designTideStandard == 20) {
            color = '#E89100';
          } else if (seawall.designTideStandard == 50) {
            color = '#EEC80B';
          } else if (seawall.designTideStandard == 100) {
            color = '#19AF47';
          } else if (seawall.designTideStandard > 100) {
            color = '#4CEBC7';
          } else if (seawall.designTideStandard == null) {
            color = '#8D0A0A';
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
          this.map.drawLineEx(lineList, style);
        }
      }
    },
    //水闸
    async getSluiceList() {
      let opt = { pjtp: '水闸' };
      let res = await WorkingConditionsApi.workingConditionsList(opt);
      if (res.code === 0 && res.data && res.data.length > 0) {
        let sluiceList = [];
        res.data.map((item) => {
          let obj = {
            name: item.wagaName,
            lat: item.stLat || item.startLat,
            lng: item.stLong || item.startLong,
            type: '极简模式',
            code: item.wagaCode ? item.wagaCode : item.stCode,
            stCode: item.stCode ? item.stCode : item.wagaCode,
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
        });
        let normalSluiceList = [];
        let warnlSluiceList = [];
        sluiceList.map((i) => {
          i.warning ? warnlSluiceList.push(i) : normalSluiceList.push(i)
        });
        this.setPoint(normalSluiceList, 'sluice', 'normal', true);
        this.setPoint(warnlSluiceList, 'sluice', 'warn', true);
      }
    },
    /* 
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
    /* 
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

    removeLayer() {
      this.map.removeLineLayer();
      this.map.removeFocusLineLayer();
      this.setPoint([], 'pump', 'normal', false);
      this.setPoint([], 'pump', 'warn', false);
      this.setPoint([], 'sluice', 'normal', false);
      this.setPoint([], 'sluice', 'warn', false);
      this.setPoint([], 'tide', 'normal', false);
      this.setPoint([], 'tide', 'warn', false);
      this.setPoint([], 'river', 'normal', false);
      this.setPoint([], 'river', 'warn', false);
    }
  }
};
</script>
