<!--
 * @Author: hanyu
 * @LastEditTime: 2022-09-08 10:14:30
 * @Description: 地图右侧-工程视频
 * @FilePath: \rscp-big-screen\src\views\outsideScreen\seawallBlack\seawallRight\SeawallRightVideo.vue
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
      <div class="chart_title_black">
        工程列表
      </div>
      <div class="table_list_diy">
        <table>
          <thead>
            <tr>
              <td style="width:33.33%">摄像头名称</td>
              <td style="width:33.33%">工程名称</td>
              <td style="width:33.33%">摄像头状态</td>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(item, index) in tableData"
              :key="index"
              @click="clickVideo(item)"
            >
              <td style="width:33.33%">{{ item.cameraname }}</td>
              <td style="width:33.33%">
                {{ item.projectName ? item.projectName : "-" }}
              </td>
              <td style="width:33.33%">
                <span v-if="item.state == 'ON'">在线</span>
                <span v-if="item.state == 'OFF'" style="color:#ff0000">离线</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <!-- <div class="table_list" ref="tableContainer">
        <el-table :height="tableHeight" v-loading="tableLoading" :data="tableData" stripe style="width: 100%" @row-click="clickVideo">
          <template slot="empty">
            <div class="table_nodata">
              <img src="../../../../assets/images/noData.png" alt="" />
            </div>
          </template>
          <el-table-column prop="cameraname" label="摄像头名称" align="left"></el-table-column>
          <el-table-column prop="projectName" label="工程名称" align="left"></el-table-column>
          <el-table-column label="摄像头状态" align="center">
            <template slot-scope="scope">
              <div v-if="scope.row.state == 'ON'">在线</div>
              <div v-else-if="scope.row.state == 'OFF'" style="color:#ff0000">离线</div>
              <div v-else>-</div>
            </template>
          </el-table-column>
        </el-table> 
      </div> -->
    </section>
  </div>
</template>

<script>
import {
  seawallLeftStatisticApi,
  videoListApi,
  videoSelectApi,
  getVideoUrlApi,
} from "@/api/api_seawall";
import ProgressList from "../components/ProgressList.vue";
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
      let res = await seawallLeftStatisticApi({
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
          maxarr.push(res.data[i].cameraCnt);
        }
        let max = Math.max(...maxarr);
        for (let i = 0; i < res.data.length; i++) {
          let percentage = ((res.data[i].cameraCnt / max) * 100).toFixed(0);
          this.progressData.push({
            title: res.data[i].adnm,
            allNum: max,
            nowNum: res.data[i].cameraCnt,
            percentage: Number(percentage),
          });
        }
      }
      this.progressLoading = false;
    },
    async getVideoListData() {
      this.$parent.$refs.seawallMap.removeAllLayer();
      this.$parent.$refs.seawallMap.closePopup();
      this.$parent.$refs.seawallMap.initView();
      this.tableData = [];
      this.tableLoading = true;
      this.$store.commit("SET_SEAWALL_RIGTH_SELECT_FLAG", false);
      let opt = {
        projectCategory: "海塘",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      };
      if (this.leftSelectAdcd == "33") {
        opt.superviseLevel = 1;
      }
      let res = await videoListApi(opt);

      if (res.success) {
        if (res.rows.length) {
          this.tableData = res.rows;
          let pointArr = [];
          res.rows.map((v) => {
            pointArr.push({
              lat: v.lttd,
              lng: v.lgtd,
              cameraname: v.cameraname,
              projectName: v.projectName,
              cameraid: v.cameraid,
              rightType: "5",
              state: v.state,
              src: require("@/assets/images/map/seawall.png"),
            });
          });
          this.$parent.showMapPointJh(pointArr, "video");
        }
      }
      this.tableLoading = false;
      this.$store.commit("SET_SEAWALL_RIGTH_SELECT_FLAG", true);
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
  height: 260px;
}
.photo_list {
  height: calc(100% - 260px);
  .table_list {
    height: calc(100% - 42px);
    box-sizing: border-box;
    padding-bottom: 16px;
    // height: 500px;
    overflow: auto;
  }
}
</style>
