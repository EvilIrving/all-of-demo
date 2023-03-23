<template>
  <div class="project-detail-dialog map-dialog">
    <div class="tap-wrap">
      <span
        v-for="(item, index) in list"
        :key="index"
        :class="{ active: listActive == item.name }"
        @click="handleMenuClick(item.name)"
        >{{ item.name }}</span
      >
    </div>
    <div class="item-wrap common-item" v-if="listActive == '视频监控'" width="100%">
      <div class="left" v-if="listActive == '视频监控'">
        <el-tree
          class="video-tree"
          ref="tree"
          :data="videoDataList"
          :props="videoDefaultProps"
          @node-click="handleVideoClick"
          node-key="cameraIndexCode"
          icon-class
          highlight-current
          :expand-on-click-node="true"
        >
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span>
              <!-- <i :class="data.icon"></i> -->
              <!-- <i class="el-icon-folder-opened"></i> -->
              {{ node.label }}
            </span>
          </span>
        </el-tree>
      </div>
      <div class="right">
        <div class="video-content" v-loading="videoLoading">
          <primary-video
            ref="videoDialog"
            :code="cameraCode"
            v-if="cameraVisible"
          ></primary-video>
        </div>
      </div>
    </div>
    <div class="item-wrap common-item" width="100%" v-if="listActive == '建设档案'">
      <div class="left" v-if="listActive == '建设档案'">
        <ul v-if="buildDataList.length > 0">
          <li
            v-for="(item, index) in buildDataList"
            :key="index"
            :class="{ active: buildActive == index }"
            @click="handleBuildClick(item, index)"
          >
            <i :style="{ background: buildActive == index ? '#267671' : '#fff' }"></i>
            {{ item.name }}
          </li>
        </ul>
        <div style="height: 200px; text-align: center; line-height: 60px" v-else>
          暂无数据
        </div>
      </div>
      <div class="right">
        <iframe :src="buildUrl" frameborder="0" width="100%" height="100%"></iframe>
      </div>
    </div>
    <div class="item-wrap common-item" width="100%" v-if="listActive == '规划档案'">
      <div class="left" v-if="listActive == '规划档案'">
        <ul v-if="buildDataList2.length > 0">
          <li
            v-for="(item, index) in buildDataList2"
            :key="index"
            :class="{ active: buildActive2 == index }"
            @click="handleBuildClick2(item, index)"
          >
            <i
              :style="{
                background: buildActive2 == index ? '#267671' : '#fff',
              }"
            ></i>
            {{ item.name }}
          </li>
        </ul>
        <div style="height: 200px; text-align: center; line-height: 60px" v-else>
          暂无数据
        </div>
      </div>
      <div class="right">
        <iframe :src="buildUrl2" frameborder="0" width="100%" height="100%"></iframe>
      </div>
    </div>
    <iframe
      class="common-item"
      v-if="listActive == '运行管理'"
      :src="url"
      frameborder="0"
      width="100%"
    ></iframe>
    <iframe
      class="common-item"
      v-if="listActive == '洪水预报'"
      :src="floodUrl"
      frameborder="0"
      width="100%"
      >洪水预报</iframe
    >
    <!-- <iframe
      class="common-item"
      v-if="listActive == '工程巡查'"
      :src="inspectionUrl"
      frameborder="0"
      width="100%"
      >巡查</iframe
    > -->
    <iframe
      class="common-item"
      v-if="listActive == '安全监测'"
      :src="safeUrl"
      frameborder="0"
      width="100%"
      >安全监测</iframe
    >
  </div>
</template>

<script>
import { mapManageApi } from "@/api/mapManage";
import { PrimaryVideo } from "@/components";

export default {
  name: "ProjectDetailDialog",
  props: {
    projectDetailData: Object,
  },
  data() {
    return {
      list: [
        {
          name: "视频监控",
        },
        // {
        //   name: "规划档案",
        // },
        // {
        //   name: "建设档案",
        // },
        // {
        //   name: "工程运行",
        // },
        // {
        //   name: "洪水预报",
        // },
        // {
        //   name: "巡查",
        // },
        // {
        //   name: "安全监测",
        // },
      ],
      listActive: "视频监控",
      //视频模块数据
      videoDataList: [
        // {
        //   cameraName: "一级 1",
        // },
        // {
        //   cameraName: "一级 2",
        // },
      ],
      videoDefaultProps: {
        children: "children",
        label: "cameraName",
      },
      cameraCode: "",
      cameraVisible: false,
      videoLoading: false,
      //建设项目
      buildDataList: [
        // {
        //   name: "一级 1",
        //   url:
        //     "https://file.ygwjg.com/c3af28d32dad3d7009158e8bda17e9f82fbf2683.pdf",
        // },
        // {
        //   name: "一级 2",
        //   url:
        //     "https://file.ygwjg.com/103021a5b93d9aec55ed45709a24b5412c138f8e.pdf",
        // },
      ],
      buildDefaultProps: {
        children: "children",
        label: "name",
      },
      buildUrl: "",
      buildActive: 0,
      buildDataList2: [
        // {
        //   name: "32",
        // },
        // {
        //   name: "dasnkl1",
        // },
      ],
      buildUrl2: "",
      buildActive2: 0,
      //外链
      floodUrl: "",
      safeUrl: "",
      inspectionUrl: "",
      //工程运行
      url: "",
      token:
        "eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.VUIU8826jWAc8WakOvKPnH5h4BuU5QyGirtWvlO8crbvHGpQdvgvHCqQttjLy6Dw-EgqtHxKKScfQ8vEiLH123jZfCfi5WCnENx2x4Op1CRhIsdnbvxPUqHY-8DSvVcbfFpI5daCaIaPmt36xUTAgwN349b5m_hKNrh501_aZRk.vVMi3igP33kpa6gy.hamJXljPJMEIFg_WUa8J_bvmnEdO3bBulx3Sd8C_30hg20lasc7pXWZKO78OtsY82afAF5wekfv_mWJQrLHr-t043XLo5318u8LIcDK3j62VePLhmChBd6eQKgA6Sx00D5x31kcG4mKyHX6vCULVRw24CnyCroR2FD9vMWzwplTfnCNuqAVb_nMEl1LhUzSn_GJn_Hclbfh5JbcKsKGTb9xowakEhyktIIzQ4ZAlTURJoJhZ2w.EjMu-7OATSEFB4TIvwx_dA",
      // "eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.UZtExBQgI-gbfZxu-Bs3Xqbsxd1nmuh5vjRIH8A3yi-eY7riL1n1OHbMeUA0JUyTM56hqlobNfSOy1oc6B_m08QCn1Uy4HnjQJT2nGtzEQY-Ot5r8be_ECNdgh9hIeJ55ZhrZCM6wbyLM9bVu3VzbRK-xJpe-fP0gtxj6_qW3PM.AFg8OXTqrgO0fg9D.KHpedT3-vpp7m__OimsYTs9xu7EiU0svdMkPJ8fPm0oU_IJDevTOdmDeB9_XmUMjQEdYc4jRqCgGj99kClsPXAxnjrrpfueVfRb4n-e93_pz1WZyX3GsHiHPQw0sxQ8TxKEYXgjqPUpzBBuBXPGIiXM2X2aWGzKeI6uuLCfDtW10AN7eoPjHszIYRdfX8iO7AN7cIMGOXgGuV9xVWTyreJ_CHLH0c5kZdwTZHt1Xx7ehGZokIQ.G3RnNaFdgtPmeTnFd1VZrg",
    };
  },
  components: { PrimaryVideo },
  watch: {},
  async mounted() {
    this.showDetail();
    if (this.projectDetailData.resCode !== undefined) {
      await this.getLinkList();
      await this.showVideoList();
      await this.getPjBd();
      await this.getPjBd2();
    }
    // await this.getLinkList();
    // await this.showVideoList();
    // await this.getPjBd();
    // await this.getPjBd2();
  },
  methods: {
    handleMenuClick(item) {
      this.listActive = item;
      this.cameraVisible = false;
      if (item == "视频监控") {
        if (this.videoDataList.length > 0) {
          this.videoLoading = true;
          this.cameraVisible = false;
          this.$nextTick(() => {
            if (this.videoDataList.length > 0) {
              this.$refs.tree.setCurrentKey(this.videoDataList[0].cameraIndexCode);
              this.cameraCode = this.videoDataList[0].cameraIndexCode;
              this.cameraVisible = true;
              setTimeout(() => {
                this.videoLoading = false;
              }, 2000);
            }
          });
        }
      } else {
        if (this.$refs.videoDialog) {
          this.$refs.videoDialog.destroyCamera();
        }
        this.cameraVisible = false;
      }
      if (item == "建设档案") {
        this.$nextTick(() => {
          if (this.buildDataList.length > 0) {
            // this.$refs.tree2.setCurrentKey(this.buildDataList[0].url);
            this.buildUrl = this.buildDataList[0].url;
          }
        });
      }
      if (item == "规划档案") {
        this.$nextTick(() => {
          if (this.buildDataList2.length > 0) {
            this.buildUrl2 = this.buildDataList2[0].url;
          }
        });
      }
      if(item == "运行管理"){
        this.$nextTick(() => {
          this.showDetail();
        })
      }
    },
    //工程运行
    showDetail() {
      let code = this.projectDetailData.resCode;
      // console.log(this.projectDetailData, 666);
      let name = encodeURIComponent(encodeURIComponent(this.projectDetailData.name));
      this.url = `https://sk.zjwater.com/mgt/main?prcd=${code}&suspension_btn=true&token=${this.token}`;
      // this.url = `http://sk.zjwater.com/cp/skip/reservoirDetailSingle?hideback=1&prcd=${code}&name=${name}&fromMap=1&pName=undefined&cName=undefined&token=${this.token}`;
    },
    handleVideoClick(item) {
      if (this.videoDataList.length > 0) {
        this.videoLoading = true;
        this.cameraVisible = false;
        this.cameraCode = item.cameraIndexCode;
        this.$nextTick(() => {
          setTimeout(() => {
            this.cameraVisible = true;
          }, 1000);
          setTimeout(() => {
            this.videoLoading = false;
          }, 2000);
        });
      }
    },
    // 视频列表
    async showVideoList() {
      await mapManageApi.getVideoList(this.projectDetailData.resCode).then((res) => {
        if (res.code == 0) {
          this.videoDataList = res.data;
          this.$nextTick(() => {
            if (this.videoDataList.length > 0) {
              this.$refs.tree.setCurrentKey(this.videoDataList[0].cameraIndexCode);
              this.cameraCode = this.videoDataList[0].cameraIndexCode;
              this.cameraVisible = true;
            }
            setTimeout(() => {
              this.videoLoading = false;
            }, 2000);
          });
        }
      });
    },
    // 建设项目
    handleBuildClick(item, index) {
      this.buildUrl = item.url;
      this.buildActive = index;
    },
    // 规划档案
    handleBuildClick2(item, index) {
      this.buildUrl2 = item.url;
      this.buildActive2 = index;
    },
    async getLinkList() {
      await mapManageApi.getOtherLink(this.projectDetailData.resCode).then((res) => {
        if (res.code == 0) {
          res.data.map((item) => {
            if (item.linkName == "工程巡查") {
              this.inspectionUrl = item.url;
            }
            if (item.linkName == "安全监测") {
              this.safeUrl = item.url;
            }
            if (item.linkName == "洪水预报") {
              this.floodUrl = item.url;
            }
            if (item.linkName != "工程巡查")
              this.list.push({
                name: item.linkName,
              });
          });
          let arr = [
            // {
            //   name: "规划档案",
            // },
            // {
            //   name: "建设档案",
            // },
            {
              name: "运行管理",
            },
          ];
          this.list = [...this.list, ...arr];
        }
      });
    },
    async getPjBd() {
      await mapManageApi
        .getPjBdIf({
          projCode: this.projectDetailData.resCode,
          type: 1,
        })
        .then((res) => {
          if (res.code == 0) {
            this.buildDataList = res.data;
            if (this.buildDataList.length > 0) {
              // this.$refs.tree2.setCurrentKey(this.buildDataList[0].url);
              this.buildUrl = this.buildDataList[0].url;
            }
          }
        });
    },
    async getPjBd2() {
      await mapManageApi
        .getPjBdIf({
          projCode: this.projectDetailData.resCode,
          type: 2,
        })
        .then((res) => {
          if (res.code == 0) {
            this.buildDataList2 = res.data;
            if (this.buildDataList2.length > 0) {
              // this.$refs.tree2.setCurrentKey(this.buildDataList[0].url);
              this.buildUrl2 = this.buildDataList2[0].url;
            }
          }
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.project-detail-dialog {
  background: #fff;
  .tap-wrap {
    background: #fff;
    span {
      display: inline-block;
      // height: 50px;
      min-width: 80px;
      text-align: center;
      // line-height: 50px;
      padding: 4px 16px;
      cursor: pointer;
      margin: 16px 10px 16px 16px;
      &.active {
        color: #fff;
        // border-bottom: 3px solid #1b8881;
        background-image: linear-gradient(180deg, #71c1a9 1%, #07bbb9 100%);
        border-radius: 4px;
      }
    }
  }
  .item-wrap {
    width: 100%;
    height: 100%;
    background: #fff;
    .left {
      width: 300px;
      height: 100%;
      overflow: auto;
      padding: 10px 0;
      ul {
        li {
          height: 50px;
          line-height: 50px;
          cursor: pointer;
          padding-left: 30px;
          text-overflow: ellipsis;
          overflow: hidden;
          white-space: nowrap;
          i {
            display: inline-block;
            width: 10px;
            height: 10px;
            border-radius: 50%;
            vertical-align: middle;
            margin-right: 8px;
          }
        }
        // li:hover {
        //   background: #f0f7ff;
        // }
        li.active {
          // background-color: #f0f7ff;
          color: #267671;
        }
      }
    }
    .right {
      width: calc(100% - 300px);
      height: 100%;
      padding: 10px;
      .video-content {
        width: 100%;
        height: 100%;
      }
    }
  }
  .common-item {
    height: calc(100vh - 327px);
  }
}
</style>
<style lang="scss">
.video-tree.el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content,
.video-tree .el-tree-node__content:hover {
  background-color: #fff;
  color: #267671;
}
</style>
