<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-05-31 14:13:13
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\components\RenderTabControl.vue
-->
<template>
  <div class="tab-box">
    <div v-show="!sluiceDistributionMap" class="tab-control tab-control-00" @click="handleSimple()">{{ Simple ? '正常模式' : '极简模式' }}</div>
    <div class="tab-control tab-control-01" v-if="!Simple" @click="handleConfirm(1)">{{analyseButton}}</div>
    <div class="tab-control tab-control-02" v-if="!Simple" @click="SelectionTips(rescue)">{{rescue}}</div>
    <div class="tab-control tab-control-03" v-if="!Simple" @click="handleConfirm(2)">降雨预报</div>
    <div class="tab-control tab-control-04" v-if="!Simple" @click="openMapClick">三维地图</div>

    <PrimaryMapDialog mainTitle="三维地图" ref="multipleDialog" :hasAnalysis="false" :visible="AddCardDialog" @handleClose="HandleClose" :appendToBody="true" :fullscreenFlag="fullscreen" @changeFullscreen="changeFullscreen" :modal="true" width="1400px">
      <div class="dialog-content">
        <div class="switch_btn">
          <div class="data-detail-button" style="width:50px; margin-left: auto;margin-bottom:10px" @click="showPoint('TT')">
            潮位
          </div>
          <div class="data-detail-button" style="width:50px; margin-left: auto;margin-bottom:10px" @click="showPoint('RR')">
            降雨
          </div>
          <div class="data-detail-button" style="width:50px; margin-left: auto;margin-bottom:10px" @click="showPoint('VV')">
            视频
          </div>
          <div class="data-detail-button" style="width:50px; margin-left: auto;margin-bottom:10px" @click="showPoint('HZ')">
            旱闸
          </div>
        </div>
        <iframe name="iframe" :src="iframeUrl" ref="iframeBoxiframe" frameborder="0"></iframe>
      </div>
    </PrimaryMapDialog>
    <ThreeDimensionalMapDialog ref="threedimensionmap" :title="pointTitle" :pointData="dialogInfo" v-if="showPointDialog" :visible.sync="showPointDialog" @closeDialog="handlePointClose" :appendToBody="true"></ThreeDimensionalMapDialog>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
import { PrimaryMapDialog } from '@/components';
import ThreeDimensionalMapDialog from '../dialog/ThreeDimensionalMapDialog.vue';
import { waterAndRain, alarmPeople } from '@/api/bigScreenPart';
import { disasterPreventionApi } from '@/api/RightSildePanel';
export default {
  computed: {
    ...mapGetters(['IsAction', 'DispatchCarousel', 'Simple', 'sluiceDistributionMap']),
    map() {
      return this.$store.state.screen.mapComponent;
    },
    rescue() {
      if (this.DispatchCarousel) {
        return '关闭抢险'
      } else {
        return '应急抢险'
      }
    },
    // analysisBtn() {
    //   if (this.DispatchAnalysisBtn) {
    //     return '关闭抢险'
    //   } else {
    //     return '应急抢险'
    //   }
    // }
  },
  mounted() {
    this.simpleType = this.Simple;
    this.$nextTick(() => {
      window.addEventListener('message', (event) => {
        if (event?.data?.data?.pointData) {
          this.handlePointOpen(event.data.data.pointData);
        }
      });
    });
  },
  data() {
    return {
      iframeUrl: 'https://slgh.lucheng.gov.cn/qidu3d/static/qidu/index.html',
      id: 0, // 点位 父级div的id 添加点击事件用
      arr: [], // 点位集合
      fullscreen: false, //是否全屏
      tideList: [], //潮位列表
      rainList: [], //降雨列表
      cameraList: [], //摄像头列表
      drygateList: [],
      pointTitle: '三维地图', // 点位弹窗标题
      dialogInfo: [], //弹窗返回的数据
      showPointDialog: false, //是否显示点位弹窗
      AddCardDialog: false, //
      loading: false,
      simpleType: false,
      vedioData: [],
      analyseButton: '周边分析'
    };
  },
  components: {
    PrimaryMapDialog,
    ThreeDimensionalMapDialog
  },
  methods: {
    ...mapActions(['SetIsAction', 'SetSpotsFlag', 'SetAddcardDialog', 'SetSpotsFlagType', 'setSimple']),
    handleSimple() {
      this.simpleType = !this.simpleType;
      this.setSimple(this.simpleType);
    },
    // 三维地图展示点位
    async showPoint(type) {
      this.removeAll();
      this.AddCardDialog = true;
      if (type == 'TT') {
        await this.getTideView('TT');
        for (let index = 0; index < this.tideList.length; index++) {
          this.add('tide', this.tideList[index]);
        }
      } else if (type == 'RR') {
        await this.getRainView();
        for (let index = 0; index < this.rainList.length; index++) {
          this.add('rain', this.rainList[index]);
        }
      } else if (type == 'VV') {
        await this.getCameraView();
        for (let index = 0; index < this.cameraList.length; index++) {
          this.add('camera', this.cameraList[index]);
        }
      } else {
        await this.getDryGateView();
        for (let index = 0; index < this.drygateList.length; index++) {
          this.add('drygate', this.drygateList[index]);
        }
      }
    },
    // 潮位点位弹窗曲线数据和列表数据
    async getRainStationInfo() {
      this.loading = true;
      let opt = {
        stationCode: this.dialogInfo.code,
        stationType: this.dialogInfo.stType,
        days: 1
      };
      waterAndRain.waterLevels(opt).then((res) => {
        if (res.code == 0) {
          this.loading = false;
          this.dataList = res.data.data.map((item) => {
            item.rz = item.rz || item.z || item.tdz;
            return item;
          });
          let info = JSON.parse(JSON.stringify(res.data.data));
          let arrs = JSON.parse(JSON.stringify(res.data.data)).reverse();
          if (res.data.astroTdz) {
            if (res.data.astroTdz.length > 0) {
              let astroTdz = JSON.parse(JSON.stringify(res.data.astroTdz)).reverse();
              const arr = arrs.map((item) => {
                const str = astroTdz.find((i) => {
                  if (item.tm == i.tm) {
                    return i;
                  }
                });
                item.twc = str.tdz;
                return item;
              });
              info = arr;
            } else {
              info = JSON.parse(JSON.stringify(res.data.data)).reverse();
            }
          } else {
            info = JSON.parse(JSON.stringify(res.data.data)).reverse();
          }
          this.chartWaterData.columns = ['时间', '潮位'];
          if (this.pointData.stType == 'DD') {
            this.chartWaterData.columns = ['时间', '潮位', '闸上水位', '闸下水位'];
            this.chartWorkData.columns = ['时间', '潮位', '闸上水位', '闸下水位'];
          } else if (this.pointData.stType == 'RR') {
            this.chartWorkData.columns = ['时间', '水位'];
          }
          info.map((item) => {
            item['时间'] = item.tm;
            item['潮位'] = item.rz || item.z || item.tdz;
            item['水位'] = item.rz || item.z || item.tdz;
            item['天文潮'] = item.twc;
            item['闸上水位'] = item.upz;
            item['保证水位'] = res.data.guaranteeLevel;
            item['警戒水位'] = res.data.warningLevel;
            item['闸下水位'] = item.dwz;
            item['过闸流量'] = item.ll;
            return item;
          });
          this.chartWaterData.rows = info;
          this.waterMarkLine[0].data.yAxis = res.data.warningLevel;
        }
      });
    },
    // 三维地图点位弹窗关闭
    handlePointClose() {
      this.showPointDialog = false;
    },
    // 三维地图点位弹窗打开
    handlePointOpen(data) {
      this.dialogInfo = data;
      if (data.type == 'camera') {
        this.pointTitle = data.name + '-' + data.cameraName;
      } else {
        this.pointTitle = data.name + '-' + data.stName;
      }
      this.$nextTick(() => {
        this.showPointDialog = true;
      });
    },
    async openMapClick() {
      this.AddCardDialog = true;
    },
    // 获取潮位列表
    async getTideView(stationType) {
      const opt = { stationType };
      await waterAndRain.waterStationsList(opt).then((res) => {
        if (res.code == 0) {
          res.data.map((i) => {
            i.type = 'tide';
            i.name = '潮位';
          });
          res.data.push({
            st_loc: '鹿城区',
            status: 0,
            tm: null,
            type: 'tide',
            name: '潮位',
            stName: '老涂北',
            upz: 3.5,
            stLong: 120.761,
            stLat: 28.018
          });
          this.tideList = res.data;
          this.$forceUpdate();
        }
      });
    },
    // 获取降雨列表
    async getRainView() {
      await alarmPeople.rainGiveAnAlarm().then((res) => {
        if (res.code == 0) {
          res.data.list.map((i) => {
            i.type = 'rain';
            i.name = '降雨';
          });

          this.rainList = res.data.list;
          this.$forceUpdate();
        }
      });
    },
    // 查询所有摄像头
    async getCameraView() {
      let res0 = await disasterPreventionApi.listCamera({ fetchAll: true });
      let totalRows = res0.data.totalRows;
      let res = await disasterPreventionApi.listCamera({ pageSize: totalRows });
      res.data.list.map((i) => {
        i.type = 'camera';
        i.name = '视频';
      });
      this.cameraList = res.data.list;
    },
    // 查询旱闸
    async getDryGateView() {
      let list = [{
        lat: 28.014576,
        lng: 120.771680,
        high: 6.46,
        proName: "老涂北旱闸",
        proType: "旱闸",
      }]
      list.map((i) => {
        i.type = 'drygate';
        i.name = '旱闸';
      });
      this.drygateList = list;
    },
    // 是否全屏
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
      this.$nextTick(() => {
        this.$refs.multipleDialog.doLayout();
      });
    },
    HandleClose() {
      this.AddCardDialog = false;
      this.showPointDialog = false;
      this.tideList = [];
      this.rainList = [];
      this.cameraList = [];
    },
    SelectionTips() {
      if (this.rescue === '应急抢险') {
        this.map.clearDispatch();
        this.SetIsAction(!this.IsAction);
        localStorage.setItem('tabControl', 0);
        if (this.IsAction == true) {
          this.$parent.SelectionTips();
        } else {
          this.SetSpotsFlag(false);
        }
      } else {
        this.map.clearDispatch();
        this.SetIsAction(!this.IsAction);
        localStorage.setItem('tabControl', 0);
        this.rescue = '应急抢险'
      }
    },
    handleConfirm(num) {
      if (num === 1 && this.analyseButton !== '关闭分析') {
        this.analyseButton = '关闭分析'
      } else if(num === 1 && this.analyseButton === '关闭分析') {
        this.analyseButton = '周边分析'
        this.map.clearAnalysisPointsLayer();
        this.map.clearCirlceLayer();
        return;
      }
      this.SetSpotsFlag(true);
      this.SetSpotsFlagType(num);
      localStorage.setItem('tabControl', num);
    },
    lookMap() {
      this.SetAddcardDialog(true);
    },
    add(type, pointGeo) {
      // pointGeo 是点位数据 包含 点位id等
      // tide rain camera
      this.id++;
      this.arr.push(this.id);
      const contentWindow = document.querySelector('iframe').contentWindow;
      if (type == 'tide') {
        contentWindow.postMessage(
          {
            eventType: `addGraphic`, //添加标注
            data: {
              id: this.id,
              pointData: pointGeo,
              position: [pointGeo.stLong, pointGeo.stLat, 10], //经纬度+高度
              offset: [0, 0], //水平偏移 潮位
              style: {
                html: `<div style='cursor: pointer'>
                    <span class='drawpoint' style= 'color: #ffffff;
                                                    width: 200px;
                                                    position: absolute;
                                                    top: -10px;
                                                    left: 0;
                                                    right: 0;'>${pointGeo.stName ? pointGeo.stName : '无'}:${pointGeo.tdz ? pointGeo.tdz : 0
                  }m</span>
              <img src="https://phplt.ygwjg.com/ph-file/polder/202111/73234124630261760.png" alt="" >
              </div>`
              }
            }
          },
          this.iframeUrl
        );
      } else if (type == 'rain') {
        contentWindow.postMessage(
          {
            eventType: `addGraphic`, //添加标注
            data: {
              id: this.id,
              pointData: pointGeo,
              position: [pointGeo.st_long, pointGeo.st_lat, 10], //经纬度+高度
              offset: [0, 0], //水平偏移 降雨
              style: {
                html: `<div  style='cursor: pointer'>
              <span class='drawpoint' style= 'color: #ffffff;
                                                    width: 200px;
                                                    position: absolute;
                                                    top: -10px;
                                                    left: 0;
                                                    right: 0;'>${pointGeo.stName ? pointGeo.stName : '无'}:${pointGeo.dpz ? pointGeo.dpz : 0
                  }mm</span>
              <img src="https://phplt.ygwjg.com/ph-file/polder/202111/73234326627942400.png" alt="" >
              </div>`
              }
            }
          },
          this.iframeUrl
        );
      } else if (type === 'camera') {
        contentWindow.postMessage(
          {
            eventType: `addGraphic`, //添加标注
            data: {
              id: this.id,
              pointData: pointGeo,
              position: [pointGeo.longitude, pointGeo.latitude, 10], //经纬度+高度
              offset: [0, 0], //水平偏移 视频
              style: {
                html: `<div style='cursor: pointer'>
                 <span class='drawpoint' style= 'color: #ffffff;
                                                    width: 200px;
                                                    position: absolute;
                                                    top: -10px;
                                                    left: -20px;
                                                    right: 0;'>${pointGeo.cameraName ? pointGeo.cameraName : '无'
                  }</span>
              <img src="https://phplt.ygwjg.com/ph-file/polder/202111/73234399529140224.png" alt="" >
              </div>`
              }
            }
          },
          this.iframeUrl
        );
      } else {
        contentWindow.postMessage(
          {
            eventType: `addGraphic`, //添加标注
            data: {
              id: this.id,
              pointData: pointGeo,
              position: [pointGeo.lng, pointGeo.lat, pointGeo.high + 15], //经纬度+高度
              offset: [0, 0], //水平偏移 旱闸
              style: {
                html: `<div style='cursor: pointer'>
                 <div class='drawpoint' style= 'color: #ffffff;
                                                    width:200px;
                                                    position: absolute;
                                                    top: -50px;
                                                    left: -10px;
                                                    right: 0;'>经度：${pointGeo.lng} <br>
                  纬度：${pointGeo.lat}<br>高程：${pointGeo.high + 15}</div>
              <img style= 'width:60px;'
                           src="https://phplt.ygwjg.com/ph-file/archiveStatis/202201/98587676709879808.png" alt="" >
              </div>`
              }
            }
          },
          this.iframeUrl
        );
      }
      // setTimeout(() => {
      //   contentWindow.document.getElementById(this.id).addEventListener('click', (data) => {
      //     window.addEventListener('message', (data) => {
      //       if (data.data.data) {
      //         this.handlePointOpen(data.data.data.pointData);
      //       }
      //     });
      //   });
      // }, 1500);
    },
    removeAll() {
      const contentWindow = document.querySelector('iframe').contentWindow;
      contentWindow.postMessage(
        {
          eventType: `removeGraphicAll` //添加标注
        },
        this.iframeUrl
      );
      this.arr = [];
    }
  }
};
</script>

<style lang="scss" scoped>
.tab-box {
  position: absolute;
  top: 140px;
  left: 50%;
  margin-left: 480px;
  .tab-control {
    width: 128px;
    height: 43px;
    background: url(~assets/images/ScreenAsset/tab-control-icon.png) no-repeat;
    background-size: 100% 100%;
    padding-left: 16px;
    box-sizing: border-box;
    line-height: 43px;
    position: relative;
    font-size: 16px;
    color: #ffffff;
    margin-bottom: 24px;
    cursor: pointer;
    &:last-child {
      margin-bottom: 0px;
    }

    &::after {
      content: '';
      position: absolute;
      width: 60px;
      height: 60px;

      right: -10px;
      top: 50%;
      margin-top: -30px;
    }
    &.tab-control-00 {
      &::after {
        background: url(~assets/images/ScreenAsset/tab-control-icon-00.png) no-repeat;
        background-size: 100% 100%;
      }
    }
    &.tab-control-01 {
      &::after {
        background: url(~assets/images/ScreenAsset/tab-control-icon-01.png) no-repeat;
        background-size: 100% 100%;
      }
    }
    &.tab-control-02 {
      &::after {
        background: url(~assets/images/ScreenAsset/tab-control-icon-02.png) no-repeat;
        background-size: 100% 100%;
      }
    }
    &.tab-control-03 {
      &::after {
        background: url(~assets/images/ScreenAsset/tab-control-icon-03.png) no-repeat;
        background-size: 100% 100%;
      }
    }
    &.tab-control-04 {
      &::after {
        background: url(~assets/images/ScreenAsset/tab-control-icon-04.png) no-repeat;
        background-size: 100% 100%;
      }
    }
  }
}
.dialog-content {
  padding: 16px 0 0;
  width: 100%;
  height: 80vh;
  box-sizing: border-box;
  iframe {
    width: 100%;
    height: 100%;
  }
}
.switch_btn {
  display: flex;
  flex-direction: column;
  position: absolute;
  left: 6%;
  top: 40%;
  transform: translate(-50%, 0%); /* 50%为自身尺寸的一半 */
  .el-button {
    margin: 10px 0;
  }
}
</style>
