<template>
  <div class="primary-dialog">
    <el-dialog
      class="list-dialog compare-dialog"
      :visible.sync="visible"
      center
      :before-close="close"
      top="10vh"
      append-to-body
      :close-on-click-modal="false"
      title="对比历史"
      width="1200px"
    >
      <div style="height: 700px">
        <!-- 测边框内容 -->
        <div class="result-content-sty">
          <!-- 项目信息 -->
          <p class="result-content-title-sty">
            项目信息
            <el-button
              type="primary"
              style="float: right"
              class="el-icon-download"
              size="mini"
              @click="exportExcel"
              round
            />
          </p>
          <div>
            <p style="color: #c0c4cc; padding: 10px 0 0 13px">
              项目名称:
              <span style="color: #fff">{{ detailsList.projName }}</span>
            </p>
            <p style="color: #c0c4cc; padding: 10px 0 0 13px">
              所在县(市,区):
              <span style="color: #fff">{{ detailsList.areaName }}</span>
            </p>
          </div>
          <!-- 比对说明 -->
          <p class="result-content-title-sty" style="margin: 10px 0">
            比对说明
          </p>
          <div class="comparison-shows-sty">
            <p v-if="detailsList.compareResult == 4">
              经比对，<span>{{ detailsList.projName }}</span
              >项目建设涉及
              <span
                v-for="(item, index) in detailsList.occupyRiverNameList"
                :key="index"
                >{{ item + " " }}</span
              >
              等<span>{{ detailsList.occupyRiverNameList.length }}</span
              >条河道水域和管理范围， 共计占用水域面积为<span>
                {{ detailsList.totalWaterOccupyAcreage.toFixed(2) }} </span
              >平方米。 同时，涉及在<span
                v-for="(item, index) in detailsList.crossRiverNameList"
                :key="index"
                >{{ item + " " }}</span
              >等其他 <span>{{ detailsList.crossRiverNameList.length }}</span
              >条河道管理范围内建设。
            </p>
            <p v-if="detailsList.compareResult == 3">
              经比对，<span>{{ detailsList.projName }}</span
              >项目建设涉及
              <span
                v-for="(item, index) in detailsList.occupyRiverNameList"
                :key="index"
                >{{ item + " " }}</span
              >等 <span>{{ detailsList.occupyRiverNameList.length }}</span
              >条河道水域和管理范围， 共计占用水域面积为<span>
                {{ detailsList.totalWaterOccupyAcreage.toFixed(2) }} </span
              >平方米。
            </p>
            <p v-if="detailsList.compareResult == 2">
              经比对，<span>{{ detailsList.projName }}</span
              >项目涉及在
              <span
                v-for="(item, index) in detailsList.crossRiverNameList"
                :key="index"
                >{{ item + " " }}</span
              >
              等<span>{{ detailsList.crossRiverNameList.length }}</span
              >条河道管理范围内建设，未占用河道水域。
            </p>
            <p v-if="detailsList.compareResult == 1">
              经比对，<span>{{ detailsList.projName }}</span
              >项目未占用河道水域且未涉及河道管理范围内建设。
            </p>
          </div>
          <!-- 比对列表 -->
          <p class="result-content-title-sty" style="margin: 10px 0">
            比对列表
          </p>
          <div style="height: 255px; overflow: auto">
            <div
              class="content-list-sty"
              v-for="(item, index) in detailsList.occupyCompareResultList"
              :key="index"
              @click="switchover(item)"
            >
              <p>{{ index + 1 }}. {{ item.riverName }}</p>
              <div>
                <p style="color: #c0c4cc; padding: 10px 0 0 13px">
                  是否为重要水域:
                  <span style="color: #fff">是</span>
                </p>
                <p style="color: #c0c4cc; padding: 10px 0 0 13px">
                  占用水域面积(㎡):
                  <span style="color: #fff">{{
                    item.waterOccupyAcreage.toFixed(2)
                  }}</span>
                </p>
              </div>
            </div>
          </div>
        </div>
        <!-- 图例 -->
        <!-- <div class="map-legend">
          <p class="legend-sty">图例
            <i :class="'el-icon-arrow-down'" />
          </p>
          <div class="legend-content-sty" style="color: #fff;">
            <div style="margin: 10px;">
              <p class="icon-sty" style="background:#47D1FF;" /><span>水域面</span>
            </div>
            <div style="margin: 10px;">
              <p class="icon-sty" style="background:#37E200;" /><span>项目</span>
            </div>
            <div style="margin: 10px;">
              <p class="icon-sty" style="background:#52FEBF;" /><span>管理范围线</span>
            </div>
            <div style="margin: 10px;">
              <p class="icon-sty" style="background:#A923D2;" /><span>占用水域</span>
            </div>
            <div style="margin: 10px;">
              <p class="icon-sty" style="background:#FF9E58;" /><span>侵占管理范围线</span>
            </div>
          </div>
        </div> -->
        <!-- 地图图层 -->
        <ComparativeHistoryMap v-if="visible" :mapShowsList="mapShowsList" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import ComparativeHistoryMap from "../../../comparative_history_map/";
import { localData } from "@/plugins/storage";
import config from "@/config/config";
import axios from "axios";
export default {
  name: 'mapDialog',
  props: {
    visible: Boolean,
    mapShowsList: Array,
    detailsList: Object,
    gid: String
  },
  components: {
    ComparativeHistoryMap
  },
  data() {
    return {

    }
  },
  mounted() {
    this.$nextTick(() => {
      let str = this.detailsList.occupyCompareResultList[0];
      this.mapShowsList.push(str.projSurfGeom, str.relevancyGeom, str.waterSurfGeom)
    })
  },
  methods: {
    // 导出
    async exportExcel() {
      let userInfo = localData("get", "userInfo");
      let { accessToken } = userInfo ? userInfo : { accessToken: "" };
      axios({
        url: `${config.baseUrl}plan-digitization/compare/exportCompareResult?gid=${this.gid}`,
        method: "get",
        data: {},
        responseType: "blob",
        headers: {
          authorization: accessToken,
        },
      }).then((res) => {
        const content = res.data;
        const blob = new Blob([content], { type: "application/msword" });
        const link = document.createElement("a"); // 创建a标签
        let filename = res.headers["content-disposition"];
        filename = filename ? filename.substr(filename.indexOf("=") + 1) : "";
        filename = decodeURIComponent(filename);
        link.download = filename;
        link.style.display = "none";
        link.href = URL.createObjectURL(blob);
        document.body.appendChild(link);
        link.click(); // 执行下载
        URL.revokeObjectURL(link.href); // 释放url
        document.body.removeChild(link); // 释放标签
      })
    },
    // 列表切换
    switchover(val) {
      this.mapShowsList.push(val.projSurfGeom, val.relevancyGeom, val.waterSurfGeom)
    },
    // 关闭
    close() {
      this.$emit("close");
    }
  }
}
</script>

<style lang="scss" scoped>
.list-dialog {
  ::v-deep.el-dialog--center .el-dialog__body {
    text-align: initial;
    padding: 0;
    position: relative;
  }
  .map-legend {
    border: 0.0625rem solid #328173;
    border-radius: 0.25rem;
    width: 15.7rem;
    height: 200px;
    position: absolute;
    left: 10px;
    bottom: 0;
    z-index: 1900;
    .legend-sty {
      height: 40px;
      line-height: 40px;
      display: flex;
      justify-content: space-between;
      color: rgba(255, 255, 255, 0.88);
      font-size: 1.25rem;
      padding: 0 1.125rem;
      background: #267671;
      box-shadow: 0 0.125rem 0.5rem 0 rgb(0 0 0 / 50%);
      > i {
        line-height: 40px;
      }
    }
    .legend-content-sty {
      height: 160px;
      overflow: auto;
      padding: 7px;
      background: rgba(38, 118, 113, 0.24);
      box-shadow: inset 0 0 1.5rem 0 #267671;
      .icon-sty {
        width: 10px;
        height: 10px;
        float: left;
        margin: 2px 15px;
        border-radius: 50%;
      }
    }
  }
  .result-content-sty {
    position: absolute;
    padding: 10px;
    top: 10px;
    left: 10px;
    z-index: 1000;
    width: 250px;
    height: 480px;
    color: #fff;
    border-radius: 10px;
    background-color: rgba(0, 0, 0, 0.8);
    background-image: linear-gradient(180deg, rgba(53, 149, 227, 0.4) 0%, rgba(25, 59, 197, 0.4) 100%);
    // background: rgba($color: #267671, $alpha: 0.8);
    border: 1px solid #00c1ff;
    .result-content-title-sty {
      border-left: 3px solid #00c1ff;
      padding-left: 10px;
    }
    .comparison-shows-sty {
      padding: 5px;
      height: 70px;
      overflow: auto;
      border-radius: 5px;
      border: 1px solid #00c1ff;
      > p {
        letter-spacing: 1px;
        line-height: 20px;
        font-weight: 100;
      }
    }
    .content-list-sty {
      margin-bottom: 10px;
      > p {
        height: 40px;
        padding: 10px 5px;
        line-height: 20px;
        border-radius: 5px 5px 0 0;
        background: #409EFF;
      }
      > div {
        border-radius: 0 0 5px 5px;
        border: 1px solid #00c1ff;
        > p:nth-child(1) {
          height: 40px;
          padding: 10px 5px;
          line-height: 20px;
          background: #3B5A98;
        }
        > p:nth-child(2) {
          height: 40px;
          padding: 10px 5px;
          line-height: 20px;
          background: rgba($color: #212122, $alpha: 0.3);
        }
      }
    }
  }
  // ::v-deep.el-dialog__header {
  //   text-align: left;
  //   background: #328173;
  //   .el-dialog__title {
  //     line-height: 1.5rem;
  //     font-size: 1.125rem;
  //     color: #fff;
  //   }
  // }
  ::v-deep.el-dialog__headerbtn .el-dialog__close {
    color: #fff;
  }
  .spacing-top {
    margin-top: 16px;
  }
  .dialog-content {
    height: 500px;
    ::v-deep .primary-dialog {
      .el-dialog__body {
        padding: 0;
      }
    }
  }
  .col-left {
    width: unset !important;
    margin-left: auto;
  }
  .action-label {
    font-weight: bold;
    font-size: 22px;
    border-left: 0.25rem solid #328173;
    padding-left: 15px;
  }
}
</style>
<style lang="scss">
.flat {
  .el-input__inner {
    height: 23px !important;
    padding-left: 50px !important;
    background: rgba($color: #fff, $alpha: 0) !important;
    border-bottom: unset !important ;
  }
  .el-input__prefix {
    i.el-input__icon {
      line-height: unset !important;
    }
  }
  .el-input__suffix {
    i.el-input__icon {
      line-height: unset !important;
    }
  }
}
.compare-dialog {
  .el-dialog{
    background:rgba(0,0,0,0);
  }
  .el-dialog__header {
    padding: 2px;
    height: 3.375rem;
    padding: 1.125rem;
    border: 0.0625rem solid #00c1ff;
    box-shadow: inset 0 0.25rem 1.875rem 0 #2351ef40;
    background-image: linear-gradient(180deg, #1f375f 0%, #1b3047 100%);
    border-bottom: 0;
    margin: 0.125rem 0.125rem 0;
    .el-dialog__title{
      color: #fff;
    }
  }
  .el-dialog__body {
    padding: 10px;
    padding-top: 0;
    border-bottom: 0.0625rem solid #00c1ff;
    .primary-dialog__content {
      @include flexbox;
      @include flexflow(column, nowrap);
      height: 680px;
      border-top: 0;
      padding: 0 16px;
      box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
      background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);

      // 统计样式
      .statistics-sty {
        padding: 10px 0;
        width: 100%;
        display: flex;
        justify-content: left;
        border-bottom: 1px solid rgba($color: #409eff, $alpha: 0.4);
        > div {
          // margin-right: 50px;
          padding: 10px 0;
          margin-right: 20px;
          display: flex;
          justify-content: space-between;
          > p {
            padding-top: 5px;
            margin-right: 15px;
          }
          > div {
            > p:nth-child(1) {
              font-family: PingFangSC-Regular;
              font-size: 15px;
              color: #ffffff;
              letter-spacing: 0;
              line-height: 24px;
              font-weight: 400;
              margin-bottom: 5px;
            }
            > p:nth-child(2) {
              font-family: PingFangSC-Medium;
              font-size: 14px;
              color: #fff;
              letter-spacing: 0;
              line-height: 24px;
              font-weight: 500;
              > span {
                font-size: 24px;
                color: #56fefe;
              }
            }
          }
        }
      }
      // 查询样式
      .query-sty {
        padding: 15px 0;
        color: #fff;
        .derive-sty {
          padding: 0 10px;
          text-align: center;
          border-radius: 4px;
          line-height: 30px;
          float: right;
          color: #fff;
          border-color: linear-gradient(
            180deg,
            #36c0e4 20%,
            #0639ff 100%
          ) !important;
          background: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
        }
        .aa {
          margin-right: 20px;
        }
        .demo-input-suffix {
          display: flex;
          justify-content: left;
          align-items: center;
        }
      }

      .tab-list-sty {
        @include flex(1);
        padding-bottom: 20px;
      }
    }
  }
}
</style>