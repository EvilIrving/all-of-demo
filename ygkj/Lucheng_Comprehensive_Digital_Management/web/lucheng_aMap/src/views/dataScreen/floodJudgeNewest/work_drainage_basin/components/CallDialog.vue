<template>
  <el-dialog
    :visible.sync="visible"
    :before-close="handleClose"
    :close-on-click-modal="false"
    class="screen-dialog"
  >
    <div class="dialog-title" slot="title">珊溪水库可视通话</div>
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
                <div class="info-job">{{ item.job }}</div>
              </div>
            </li>
          </ul>
        </div>
        <div class="contact-info">
          <el-row type="flex" justify="center" class="call-wrap">
            <el-col v-show="!showCall" class="content-wrap call-contact">
              <div class="detail-avatar">
                <el-avatar class="avatar-large" />
              </div>
              <p class="detail-name">
                {{ contactList[contactSelect].name || "暂无" }}
              </p>
              <p class="detail-info">
                {{ contactList[contactSelect].type || "暂无" }}
              </p>
              <p class="detail-info">
                {{ contactList[contactSelect].job || "暂无" }}
              </p>
              <p class="detail-info">
                {{ contactList[contactSelect].deptName || "暂无" }}
              </p>
              <p class="detail-info">
                {{ contactList[contactSelect].phone || "暂无" }}
              </p>
              <div class="action-wrap" @click="call('test1')">
                <i class="icon-call-btn" />
                <div class="action-label">发起通话</div>
              </div>
            </el-col>
            <el-col v-show="showCall" class="content-wrap calling">
              <video-call
                :visible.sync="showCall"
                ref="videoCall"
                @close="showCall = false"
              />
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
import { VideoCall } from "@/components/";
import { reservoirRegulationApi } from "@/api/dataScreen/ReservoirRegulation";
export default {
  name: "CallDialog",
  props: { visible: Boolean, code: String },
  components: { VideoCall },
  data() {
    return {
      showCall: false,

      contactSelect: 0,
      contactList: [],
      caller: { user: "test2", pwd: "123456" },
      loginUser: null,
      historyData: [{ wrap: "wrap" }],
    };
  },
  mounted() {
    this.getContactList();
  },
  methods: {
    getContactList() {
      reservoirRegulationApi.getThreeCharge(this.code).then((res) => {
        for (let charge of res.data) {
          this.contactList.push({
            avatar:
              "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
            name: charge.username,
            phone: charge.phone,
            job: charge.job,
            type: charge.type,
            deptName: charge.deptName,
          });
        }
      });
    },
    handleClose() {
      this.$emit("close");
    },
    call(callee) {
      this.showCall = true;

      if (this.loginUser) {
        this.$refs.videoCall.call(callee);
      } else {
        this.$refs.videoCall.login(this.caller).then((ret) => {
          if (ret.code == 200) {
            console.log("登录成功" + this.caller);
            this.loginUser = ret.data.user;
            if (callee !== "") {
              this.$refs.videoCall.call(callee);
            }
          }
        });
      }
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
          .info-job {
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
          &.call-contact {
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
          &.calling {
            margin: 8px 32px;
            border: 2px solid;
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
