<!--
 * @Author: hanyu
 * @LastEditTime: 2022-09-08 09:30:42
 * @Description: 地图右侧-工程视频
 * @FilePath: \rscp-big-screen\src\views\rscpDriveScreen\reservoir\reservoirRight\ReservoirRightVideo.vue
-->
<template>
  <div class="wrap">
    <progress-list
      v-loading="progressLoading"
      title="工程视频分布情况"
      class="progress"
      :progressData="progressData"
    ></progress-list>
    <section class="photo_list">
      <div class="chart_title">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        工程列表
      </div>
      <div class="table_list" ref="tableContainer">
        <el-table
          :height="tableHeight"
          v-loading="tableLoading"
          :data="tableData"
          stripe
          style="width: 100%"
          @row-click="clickVideo"
        >
          <template slot="empty">
            <div class="table_nodata">
              <img src="../../../../assets/images/noData.png" alt="" />
            </div>
          </template>
          <el-table-column
            prop="cameraname"
            label="摄像头名称"
            align="left"
          ></el-table-column>
          <el-table-column
            prop="projectName"
            label="工程名称"
            align="left"
          ></el-table-column>
          <el-table-column label="摄像头状态" align="center">
            <template slot-scope="scope">
              <div v-if="scope.row.state == 'ON'">在线</div>
              <div v-else-if="scope.row.state == 'OFF'" style="color:#ff0000">
                离线
              </div>
              <div v-else>-</div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </section>
  </div>
</template>

<script>
import {
  videoListApi,
  videoSelectApi,
  getVideoUrlApi,
} from "@/api/api_seawall";
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
import ProgressList from "@/components/ProgressList";
export default {
  name: "",
  props: {},
  components: {
    ProgressList,
  },
  data() {
    return {
      progressLoading: true,
      progressData: [],
      tableData: [],
      tableLoading: true,
      tableHeight: 0,
    };
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
  },
  watch: {
    leftSelectAdcd(val) {
      this.getVideoStatisticData();
      this.getVideoListData();
    },
  },
  mounted() {
    this.$nextTick(function() {
      const height = this.$refs.tableContainer.clientHeight;
      this.tableHeight = height - 20;
      let _this = this;
      window.onresize = function() {
        const height = this.$refs.tableContainer.clientHeight;
        this.tableHeight = height - 20;
      };
    });
    this.getVideoStatisticData();
    this.getVideoListData();
  },
  methods: {
    async getVideoStatisticData() {
      this.progressLoading = true;
      this.progressData = [];
      let res = await reservoirLeftStatisticApi({
        statistic: "adcd",
        itemCategory: "camera",
        itemValue: 1,
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      });
      if (res.success) {
        let maxarr = [];
        for (let i = 0; i < res.data.length; i++) {
          maxarr.push(res.data[i].num);
        }
        let max = Math.max(...maxarr);
        for (let i = 0; i < res.data.length; i++) {
          let percentage = ((res.data[i].num / max) * 100).toFixed(0);
          this.progressData.push({
            title: res.data[i].adnm,
            allNum: max,
            nowNum: res.data[i].num,
            percentage: Number(percentage),
          });
        }
      }
      this.progressLoading = false;
    },
    async getVideoListData() {
      this.$parent.$refs.reservoirMap.removeAllLayer();
      this.$parent.$refs.reservoirMap.closePopup();
      this.$parent.$refs.reservoirMap.initView();
      this.tableData = [];
      this.tableLoading = true;
      this.$store.commit("SET_RESERVOIR_RIGTH_SELECT_FLAG", false);
      let opt = {
        projectCategory: "水库",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      };
      if (this.leftSelectAdcd == "33") {
        opt.superviseLevel = 1;
      }
      let res = await videoListApi(opt);
      if (res.success) {
        let pointArr = [];
        if (res.rows.length) {
          this.tableData = res.rows;
          res.rows.map((v) => {
            v.lat = v.lttd;
            v.lng = v.lgtd;
            v.rightType = "5";
            v.src = require("@/assets/images/map/reservoir.png");
            pointArr.push(v);
          });
        }
        // this.$parent.showMapPointJh(pointArr, 'video')
        this.$parent.showMapPoints(pointArr);
      }
      this.tableLoading = false;
      this.$store.commit("SET_RESERVOIR_RIGTH_SELECT_FLAG", true);
    },
    clickVideo(row) {
      this.$parent.setView(row.lgtd, row.lttd);
      setTimeout(() => {
        row.rightType = "5";
        row.LGTD = row.lgtd;
        row.LTTD = row.lttd;
        this.$parent.setPopup(row);
      }, 500);
      if (row.state == "ON") {
        this.$parent.openLookVideoPopup();
        this.getVideoToken(row.cameraid);
      } else {
        this.$message.warning("设备已离线");
      }
    },
    async getVideoToken(cameraid) {
      let res = await videoSelectApi({
        cameraid: cameraid,
      });
      if (res.success) {
        let liveAddress = res.data.liveaddress;
        let apiUrlType = 3;
        if (liveAddress == "211.lives9.top:30443") {
          apiUrlType = 1;
        } else if (liveAddress == "170.lives9.top:30443") {
          apiUrlType = 2;
        }
        this.getVideoUrl(
          apiUrlType,
          res.data.serial,
          res.data.code,
          res.data.token
        );
      } else {
        this.$message.error("获取摄像头数据失败！");
      }
    },
    async getVideoUrl(type, serial, code, token) {
      let res = await getVideoUrlApi["url" + type]({
        serial: serial,
        code: code,
        token: token,
      });
      console.log(res, "------");
      if (res && res.WS_FLV) {
        let videoUrl = res.WS_FLV;
        // videoUrl = videoUrl.replace(
        //   "ws://124.160.167.211:10000",
        //   "wss://sk.zjwater.com/vid-qtj-ws1"
        // );
        // videoUrl = videoUrl.replace(
        //   "ws://103.219.33.170:10000",
        //   "wss://sk.zjwater.com/vid-qtj-ws2"
        // );
        // videoUrl = videoUrl.replace(
        //   "ws://60.12.107.70:8224",
        //   "wss://sk.zjwater.com/vid-qtj-ws3"
        // );
        this.$parent.showVideo(videoUrl);
      } else {
        this.$message.error("加载视频失败,请稍后再试！");
        this.$parent.closeLookVideoPopup();
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  height: 100%;
}
.progress {
  height: 285px;
}
.photo_list {
  height: calc(100% - 280px);
  .table_list {
    height: calc(100% - 42px);
    box-sizing: border-box;
    padding-bottom: 16px;
    // height: 500px;
    overflow: auto;
  }
}
</style>
