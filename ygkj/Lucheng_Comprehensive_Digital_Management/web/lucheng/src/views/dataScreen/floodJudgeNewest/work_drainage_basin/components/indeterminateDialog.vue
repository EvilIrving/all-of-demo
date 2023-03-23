<template>
  <el-dialog
    :visible.sync="visible"
    :before-close="handleClose"
    :close-on-click-modal="false"
    class="screen-dialog"
  >
    <div class="dialog-title" slot="title">待定弹框</div>
    <div class="dialog-content">
      <div class="call-content">
        <div class="contact-list">
          <ul>
            <li
              v-for="(item, index) in contactList"
              :key="index"
              :class="['contact-list-item', contactSelect == index && 'active']"
              @click="contactSelect = index"
            >
              <div class="list-item-avatar-wrap">
                <el-avatar class="contact-avatar" :src="item.avatar" />
              </div>
              <div class="list-item-info-wrap">
                <div class="info-name">{{ item.name }}</div>
                <div class="info-phone">{{ item.phone }}</div>
                <div class="info-position">{{ item.position }}</div>
              </div>
            </li>
          </ul>
        </div>
        <div class="contact-info">
          <el-row type="flex" justify="center" class="call-wrap">
            <el-col class="content-wrap">
              <div class="detail-avatar">
                <el-avatar class="avatar-large" />
              </div>
              <p class="detail-name">李青霞</p>
              <p class="detail-info">行政负责人</p>
              <p class="detail-info">镇长</p>
              <p class="detail-info">温州市瑞溪办事处</p>
              <p class="detail-info">18729382873</p>
              <div class="action-wrap">
                <i class="icon-call-btn" />
                <div class="action-label">发起通话</div>
              </div>
            </el-col>
          </el-row>
          <div class="history-list">
            <el-table
              :data="historyData"
              :height="195"
              class="screen-dialog-table"
            >
              <el-table-column
                prop="wrap"
                align="left"
                label="序号"
                width="65"
              />
              <el-table-column prop="wrap" align="left" label="通话时间" />
              <el-table-column
                prop="wrap"
                align="left"
                label="通话时长"
                width="265"
              />
              <el-table-column
                prop="wrap"
                align="left"
                label="发起人"
                width="135"
              />
            </el-table>
          </div>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: "CallDialog",
  props: { visible: Boolean },
  data() {
    return {
      contactSelect: 0,
      contactList: [
        {
          avatar:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          name: "李青霞",
          phone: "18729382873",
          position: "行政负责人",
        },
        {
          avatar:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          name: "李青霞",
          phone: "18729382873",
          position: "行政负责人",
        },
        {
          avatar:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          name: "李青霞",
          phone: "18729382873",
          position: "行政负责人",
        },
        {
          avatar:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          name: "李青霞",
          phone: "18729382873",
          position: "行政负责人",
        },
        {
          avatar:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          name: "李青霞",
          phone: "18729382873",
          position: "行政负责人",
        },
        {
          avatar:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          name: "李青霞",
          phone: "18729382873",
          position: "行政负责人",
        },
        {
          avatar:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          name: "李青霞",
          phone: "18729382873",
          position: "行政负责人",
        },
        {
          avatar:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          name: "李青霞",
          phone: "18729382873",
          position: "行政负责人",
        },
        {
          avatar:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          name: "李青霞",
          phone: "18729382873",
          position: "行政负责人",
        },
        {
          avatar:
            "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          name: "李青霞",
          phone: "18729382873",
          position: "行政负责人",
        },
      ],

      historyData: [{ wrap: "wrap" }],
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
    .contact-list {
      float: left;
      width: 240px;
      padding-right: 2px;
      height: 668px;
      overflow-x: hidden;
      overflow-y: scroll;
      li {
        margin-bottom: 16px;
      }
      .contact-list-item {
        cursor: pointer;
        user-select: none;
        height: 60px;
        width: 238px;
        &.active {
          background: #239def1e;
          .list-item-info-wrap {
            color: #50bcea;
          }
        }
        .list-item-avatar-wrap {
          float: left;
          height: 100%;
          width: 66px;
          .el-avatar {
            margin-top: 15px;
            margin-left: 26px;
            width: 32px;
            height: 32px;
          }
        }
        .list-item-info-wrap {
          height: 100%;
          color: #fff;
          .info-name {
            font-size: 16px;
            display: inline-block;
            margin-top: 10px;
          }
          .info-phone {
            display: inline-block;
            font-size: 14px;
            margin-left: 17px;
            margin-top: 10px;
            padding: 3px;
          }
          .info-position {
            margin-top: 3px;
            font-size: 14px;
          }
        }
      }
    }
    .contact-info {
      margin-left: 238px;
      width: 985px;
      height: 668px;
      border: 1px solid #229def;
      display: flex;
      flex-direction: column;
      .call-wrap {
        width: 100%;
        height: 470px;

        .content-wrap {
          .detail-avatar {
            margin: 0 auto;
            width: 72px;
            .avatar-large {
              margin-top: 69px;
              width: 72px;
              height: 72px;
            }
          }

          p {
            text-align: center;
            padding: 3px 0;
          }
          .detail-name {
            margin-top: 12px;
            margin-bottom: 24px;
            font-size: 20px;
            color: #ffffff;
          }
          .detail-info {
            font-size: 14px;
            color: #ffffff;
            margin-bottom: 12px;
          }
          .action-wrap {
            cursor: pointer;
            margin: 8px auto;
            width: 120px;
            display: flex;
            .action-label {
              margin-left: 16px;
              display: inline-block;
              font-size: 16px;
              color: #ffffff;
              font-weight: 400;
              line-height: 40px;
              height: 40px;
            }
          }
        }
      }
      .history-list {
        width: 100%;
        height: 174px;
        padding: 0 24px;
      }
      ::v-deep .screen-dialog-table {
        th {
          background: #239def;
          .cell {
            padding-left: 16px;
            font-size: 14px;
            color: #ffffff;
          }
        }
        td {
          .cell {
            padding-left: 16px;
          }
        }
      }
    }
  }
}
</style>