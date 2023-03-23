<template>
  <el-dialog
    :visible.sync="visible"
    :before-close="handleClose"
    :close-on-click-modal="false"
    class="screen-dialog"
  >
    <div class="dialog-title" slot="title">工程特性</div>
    <div class="dialog-content">
      <div class="video-content">
        <div class="video-list">
          <ul>
            <li
              v-for="(item, index) in videoList"
              :key="index"
              :class="['video-list-item', videoSelect == index && 'active']"
              @click="videoSelect = index"
            >
              <div class="list-item-info-wrap">
                <div
                  :class="[
                    'info-status',
                    item.status == 1 ? 'online' : 'offline',
                  ]"
                />
                <div class="info-name">{{ item.name }}</div>
              </div>
            </li>
          </ul>
        </div>
        <div class="video-wrap">
          <primary-video :code="reservoirCode"></primary-video>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import { PrimaryVideo } from "@/components";
export default {
  name: "VideoDialog",
  props: { visible: Boolean },
  components: { PrimaryVideo },
  data() {
    return {
      reservoirCode: "33038200581314000135",
      videoSelect: 0,
      videoList: [
        { name: "DV-822", status: 1 },
        { name: "DV-822", status: 1 },
        { name: "DV-822", status: 0 },
        { name: "DV-822", status: 1 },
        { name: "DV-822", status: 1 },
        { name: "DV-822", status: 1 },
        { name: "DV-822", status: 1 },
      ],
    };
  },
  methods: {
    handleClose() {
      this.$emit("close");
    },
  },
};
</script>


<style lang="scss" scoped>
@import "@/style/bigScreen.scss";
.screen-dialog {
  ::v-deep .el-dialog {
    width: 1260px;
    height: 770px;
    background: url(~assets/images/screen-dialog-bg.png) no-repeat center center;
    background-size: 100% 100%;
  }
  ::v-deep .el-dialog__header {
    padding: 14px 35px 16px;
    .dialog-title {
      font-family: PingFangSC-Semibold;
      font-size: 18px;
      color: #50bcea;
      font-weight: 600;
    }
    .el-dialog__headerbtn {
      top: 24px;
      right: 26px;
      .el-dialog__close {
        color: #50bcea;
        font-size: 18px;
      }
    }
  }
  .dialog-content {
    margin-top: 16px;
    .video-content {
      margin-top: 16px;
      .video-list {
        float: left;
        width: 240px;
        padding-right: 2px;
        height: 668px;
        overflow-x: hidden;
        overflow-y: scroll;
        li {
          margin-bottom: 16px;
        }
        .video-list-item {
          cursor: pointer;
          user-select: none;
          width: 236px;
          height: 38px;
          &.active {
            background: #239def1e;
            .list-item-info-wrap {
              color: #50bcea;
            }
          }
          .list-item-info-wrap {
            height: 38px;
            color: #fff;
            line-height: 38px;
            .info-status {
              width: 6px;
              height: 6px;
              margin-left: 25px;
              border-radius: 50%;
              display: inline-block;
              &::before {
                content: "";
                display: block;
              }
              &.online {
                background-color: #43dc00;
                color: #43dc00;
              }
              &.offline {
                background-color: red;
                color: red;
              }
            }
            .info-name {
              line-height: 38px;
              height: 38px;
              margin-left: 18px;
              font-size: 16px;
              display: inline-block;
            }
          }
        }
      }
      .video-wrap {
        margin-left: 236px;
        width: 985px;
        height: 668px;
        ::v-deep .playWnd {
          img {
            display: none;
          }
        }
      }
    }
  }
}
</style>