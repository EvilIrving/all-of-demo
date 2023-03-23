<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2022-04-19 18:45:38
 * @LastEditors: dtb
 * @Description: In User Settings Edit
 * @FilePath: \lucheng\src\views\water_disaster_custom\countCards\VideoCount.vue
-->
<template>
  <count-wrap title="视频监控" class="video-count">
    <el-row class="has-count">
      <el-col :span="12" class="fs24">
        <p @click="online('1')">
          在线数量<span style="color: #31b5ff" class="yellow ml12 mr12 fs36">{{ onlineNum || '0' }}</span
          >个
        </p>
      </el-col>
      <el-col :span="12" class="fs24">
        <p @click="online('0')">
          离线数量<span style="color: #c6613f" class="orange fs36 ml12 mr12">{{ outlineNum || '0' }}</span
          >个
        </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import CountWrap from '../components/CountWrap.vue';
import { disasterPreventionApi } from '@/api/RightSildePanel';
export default {
  name: 'VideoCount',
  data() {
    return {
      videoList: [],
      timer: null,
      pointList: [],
      onlineNum: 0,
      outlineNum: 0
    };
  },
  components: { CountWrap },
  created() {},

  mounted() {
    this._countCameraStatus();
    this.timer = setInterval(() => {
      this._countCameraStatus();
    }, 300000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
    this.map.drawClusterPoint([], {}, 'onlineLayers');
    this.map.drawClusterPoint([], {}, 'outlineLayers');
    // this.$emit("legend-change", "视频", {});
  },
  computed: {
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },

  methods: {
    // 统计摄像头在线离线状态
    async _countCameraStatus() {
      let res = await disasterPreventionApi.countCameraStatus();
      // let res = {
      //   code: 0,
      //   message: "操作成功",
      //   data: [
      //     {
      //       status: "0", //在线状态:0为下线,1为在线
      //       num: 529, //数量
      //       total: 3025, //摄像头总数
      //     },
      //     {
      //       status: "1",
      //       num: 2496,
      //       total: 3025,
      //     },
      //   ],
      // };
      if (res.code == 0) {
        res.data.map((item) => {
          if (item.status == 1) {
            this.onlineNum = item.num;
          } else {
            this.outlineNum = item.num;
          }
        });
        this.videoList = res.data;
      }
    },
    online(val) {
      this._selectCamera(val);
    },
    // 查询所有摄像头
    async _selectCamera(val) {
      let res = await disasterPreventionApi.selectCamera({ status: val });
      this.pointList = res.data;
      this.handleVideo();
    },
    // 地图落点开关
    handleVideo() {
      let style = {
        scale: 0.7,
        anchor: [27, 30],
        distance: parseInt(10, 10),
        anchorXUnits: 'pixels',
        srcs: require('@/assets/images/MapPoint/group/video-online.png'),
        src: require('@/assets/images/MapPoint/video-online.png')
      };
      let style1 = {
        scale: 0.7,
        anchor: [27, 30],
        distance: parseInt(10, 10),
        anchorXUnits: 'pixels',
        srcs: require('@/assets/images/MapPoint/group/video-offline.png'),
        src: require('@/assets/images/MapPoint/video-offline.png')
      };
      let onlineList = [];
      let outlineList = [];
      console.log(101, this.pointList);
      for (let point of this.pointList) {
        let obj = {
          name: point.cameraName,
          type: '视频',
          lng: Number(point.longitude),
          lat: Number(point.latitude),
          code: point.cameraIndexCode,
          cameraIndexCode: point.cameraIndexCode,
          stationCode: point.cameraIndexCode || ''
        };
        if (point.status == 1) {
          onlineList.push(obj);
        } else {
          outlineList.push(obj);
        }
      }
      // if (true) {
      this.map.drawClusterPoint(outlineList, style1, 'onlineLayers');
      this.map.drawClusterPoint(onlineList, style, 'outlineLayers');
      // this.$emit("legend-change", "视频", {
      //   label: "视频",
      //   children: [
      //     {
      //       label: "在线",
      //       icon: "icon-legend-video-online",
      //     },
      //     {
      //       label: "离线",
      //       icon: "icon-legend-video-outline",
      //     },
      //   ],
      // });
      // } else {
      //   this.map.drawClusterPoint([], {}, "onlineLayers");
      //   this.map.drawClusterPoint([], {}, "outlineLayers");
      //   // this.$emit("legend-change", "视频", {});
      // }
    }
  }
};
</script>
<style lang="scss" scoped>
@import './style/CountCard';
.video-count .has-count {
  height: 100%;
  cursor: pointer;
  .el-col {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
  }
}
</style>
