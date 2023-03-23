<template>
  <div class="workplace-message">
    <div class="workplace-main__left">
      <div class="user-info">
        <div class="avatar">
          <el-avatar shape="square" style="width: 125px; height: 138px">
            暂无头像</el-avatar
          >
        </div>
        <div class="information">
          <div class="username">
            <span>{{ userInfo.username }}</span>
          </div>
          <div class="dept">{{ userInfo.department }}</div>
        </div>
      </div>
      <div class="role-info">
        <div class="dept-roles">
          <p class="department">内控数字化</p>
          <el-row class="role-list" :gutter="20">
            <el-col :span="8" v-for="(item, index) in new Array(6)" :key="index">
              <div class="role-item">XXXX角色</div>
            </el-col>
          </el-row>
        </div>
        <div class="dept-roles">
          <p class="department">XXXXXXX</p>
          <el-row class="role-list" :gutter="20">
            <el-col :span="8" v-for="(item, index) in new Array(6)" :key="index">
              <div class="role-item">XXXX角色</div>
            </el-col>
          </el-row>
        </div>
        <div class="dept-roles">
          <p class="department">XXXXXXX</p>
          <el-row class="role-list" :gutter="20">
            <el-col :span="8" v-for="(item, index) in new Array(6)" :key="index">
              <div class="role-item">XXXX角色</div>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
    <div class="workplace-main__content">
      <el-tabs v-model="tabActive" type="card">
        <el-tab-pane :label="`我发起的(${countData[0]})`" name="send">
          <SendTab />
        </el-tab-pane>
        <el-tab-pane :label="`我收到的(${countData[1]})`" name="received">
          <Received />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>
<script>
import { messageApi } from "@/api/message.js";
import SendTab from "./pages/send.vue";
import Received from "./pages/received.vue";
export default {
  name: "Message",
  components: { SendTab, Received },
  data() {
    return {
      tabActive: "send",
      countData: [0, 0],
      userInfo: { username: "", department: "'" },
    };
  },
  mounted() {
    this.getUserInfo();
    this.getMyMessageCount();
  },
  methods: {
    getUserInfo() {
      let user = this.$localData("get", "userInfo");
      this.userInfo = user;
    },
    getMyMessageCount() {
      messageApi.getMyMessageCount().then((res) => {
        if (res.code == 0) {
          this.countData = res.data;
        }
      });
    },
  },
};
</script>
<style lang="scss">
.workplace-message {
  display: flex;
  height: 570px;
  .workplace-main__left {
    width: 372px;
    .user-info,
    .role-info {
      border: #3dc5f0 2px solid;
      box-shadow: 0 4px 30px 0 #2351ef40;
      .dept-roles {
        p.department {
          font-size: 18px;
          color: #ffffff;
          padding-left: 8px;
          margin-bottom: 6px;
          position: relative;
          &::before {
            content: "";
            position: absolute;
            left: 0;
            top: 5%;
            height: 90%;
            border-left: 2px #f3ab49 solid;
          }
        }
        .role-item {
          margin: 6px 0;
          background-image: linear-gradient(180deg, #0094c305 2%, #103567 100%);
          border: 1px solid #069bcb;
          box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
          padding: 6px 6px 6px 8px;
          color: #56fefe;
          font-size: 14px;
          position: relative;
          &::before {
            content: "";
            position: absolute;
            left: 0;
            top: 0;
            height: 100%;
            border-left: 1px #069bcb solid;
          }
        }
      }
    }
    .user-info {
      position: relative;
      height: 158px;
      .avatar {
        position: absolute;
        border: #3dc5f0 2px solid;
        border-radius: 8px;
        width: 129px;
        height: 142px;
        left: 24px;
        top: -16px;
      }
      .information {
        margin-left: 170px;
        display: flex;
        flex-direction: column;
        .username {
          font-family: "YouSheBiaoTiHei";
          font-size: 36px;
          padding: 12px 0 14px 0;

          > span {
            position: relative;
            &::after {
              content: "";
              position: absolute;
              left: 0;
              bottom: -8px;
              width: 110%;
              border-bottom: #5398f6 1px solid;
            }
          }
        }
        .dept {
          padding: 12px 0 12px 0;
        }
      }
    }
    .role-info {
      margin: 16px 0 32px 0;
      height: 360px;
      padding: 24px;
    }
  }
  .workplace-main__content {
    width: calc(100% - 320px);
    padding-left: 32px;
    .el-table th {
      background: transparent;
      color: #56fefe;
    }
    .el-table__header {
      background-image: linear-gradient(90deg, #2c9ebf4d 3%, #2993b41a 100%);
    }
    .el-tabs {
      .el-tabs__header {
        border-bottom: none;
        padding-bottom: 17px;
        &::after {
          content: "";
          position: absolute;
          height: 2px;
          width: 100%;
          bottom: 0;
          background-image: linear-gradient(90deg, #1e8a8d 3%, #0000 100%);
        }
        .el-tabs__nav {
          border: 0;
          .el-tabs__item {
            height: 32px;
            width: 94px;
            padding: 0;
            text-align: center;
            line-height: 32px;
            font-size: 14px;
            color: #ffffff;
            border: 1px solid rgba(255, 255, 255, 0.3);
            margin-right: 10px;
            &.is-active {
              background-image: url("../../../../assets/images/ScreenAsset/workplace-nav_active.png");
              background-position: -1px -1px;
              border: 1px solid #0000;
            }
          }
        }
      }
    }

    .pagination-area {
      .el-pagination {
        color: #fff;
        .el-pagination__total,
        .el-pagination__jump {
          color: #fff;
        }
        .el-pager {
          .number {
            background: #0000;
            color: #fff;
            &.active {
              background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
              border-radius: 2px;
            }
          }
        }
      }
    }
  }
}
</style>
