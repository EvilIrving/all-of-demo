<!--
 * @Author: wqn
 * @Date: 2022-09-23 14:19:25
 * @LastEditors: wqn
 * @LastEditTime: 2022-09-23 14:19:25
 * @Description: 三维地图弹窗内容
-->
<template>
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
    <ThreeDimensionalMapDialog ref="threedimensionmap" :title="pointTitle" :pointData="dialogInfo" v-if="showPointDialog" :visible.sync="showPointDialog" @closeDialog="handlePointClose" :appendToBody="true" />
  </div>
</template>

<script>
import { waterAndRain, alarmPeople } from '@/api/bigScreenPart';
import { disasterPreventionApi } from '@/api/RightSildePanel';
import ThreeDimensionalMapDialog from '@/views/water_disaster_custom/dialog/ThreeDimensionalMapDialog.vue';
export default {
  name: 'map3D',
  props: {},
  components: {
    ThreeDimensionalMapDialog
  },
  data() {
    return {
      iframeUrl: 'https://slgh.lucheng.gov.cn/qidu3d/static/qidu/index.html',
      tideList: [], //潮位列表
      rainList: [], //降雨列表
      cameraList: [], //摄像头列表
      drygateList: [],
      id: 0, // 点位 父级div的id 添加点击事件用
      arr: [], // 点位集合
      showPointDialog: false, // 是否打开3D地图内点位弹窗
      pointTitle: '三维地图', // 点位弹窗标题
      dialogInfo: [] //弹窗返回的数据
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.$nextTick(() => {
      window.addEventListener('message', (event) => {
        if (event?.data?.data?.pointData) {
          this.handlePointOpen(event.data.data.pointData);
        }
      });
    });
  },
  destroyed() {},
  methods: {
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
    removeAll() {
      const contentWindow = document.querySelector('iframe').contentWindow;
      contentWindow.postMessage(
        {
          eventType: `removeGraphicAll` //添加标注
        },
        this.iframeUrl
      );
      this.arr = [];
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
      let list = [
        {
          lat: 28.014576,
          lng: 120.77168,
          high: 6.46,
          proName: '老涂北旱闸',
          proType: '旱闸'
        }
      ];
      list.map((i) => {
        i.type = 'drygate';
        i.name = '旱闸';
      });
      this.drygateList = list;
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
                        <span class='drawpoint' style= 'color: #ffffff;width: 200px;position: absolute;top: -10px;left: 0;right: 0;'>
                          ${pointGeo.stName ? pointGeo.stName : '无'}:${pointGeo.tdz ? pointGeo.tdz : 0}m
                        </span>
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
                        <span class='drawpoint' style= 'color: #ffffff;width: 200px;position: absolute;top: -10px;left: 0;right: 0;'>
                          ${pointGeo.stName ? pointGeo.stName : '无'}:${pointGeo.dpz ? pointGeo.dpz : 0}mm
                        </span>
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
                        <span class='drawpoint' style='color: #ffffff;width: 200px;position: absolute;top: -10px;left: -20px;right: 0;'>
                          ${pointGeo.cameraName ? pointGeo.cameraName : '无'}
                        </span>
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
                        <div class='drawpoint' style='color: #ffffff;width:200px;position: absolute;top: -50px;left: -10px;right: 0;'>
                          经度：${pointGeo.lng} <br>
                          纬度：${pointGeo.lat}<br>高程：${pointGeo.high + 15}</div>
                          <img style= 'width:60px;' src="https://phplt.ygwjg.com/ph-file/archiveStatis/202201/98587676709879808.png" alt="" >
                      </div>`
              }
            }
          },
          this.iframeUrl
        );
      }
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
    }
  }
};
</script>
<style lang='scss' scoped>
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