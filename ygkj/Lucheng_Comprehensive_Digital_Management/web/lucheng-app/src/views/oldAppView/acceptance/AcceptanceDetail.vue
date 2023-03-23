<template>
  <view-page>
    <div class="project-wrap">
      <van-cell-group>
        <van-cell
          value-class="cell-value"
          title-class="cell-title"
          title="合同名称"
          :value="detail.contractName"
        />
        <van-cell
          value-class="cell-value"
          title-class="cell-title"
          title="物业管理单位"
          :value="detail.propertyUnitName"
        />
        <van-cell
          value-class="cell-value"
          title-class="cell-title"
          title="运行管理单位"
          :value="detail.runUnitName"
        />
        <van-cell
          value-class="cell-value"
          title-class="cell-title"
          title="审核人"
          :value="detail.runUnitReviewerName"
        />
        <van-cell
          value-class="cell-value"
          title-class="cell-title"
          title="服务期限"
          :value="detail.serviceTimes"
        />
        <van-cell
          value-class="cell-value"
          title-class="cell-title"
          title="合同(附件)"
        >
          <template #default>
            <div
              class="default-box"
              v-if="detail.contractFile"
              @click="lookFile(detail.contractFiles[0])"
            >
              <img
                class="img-box"
                src="../../../assets/images/pdf_icon.png"
                alt=""
              />
              <span>{{ detail.contractFiles[0].fileName }}</span>
            </div>
          </template>
        </van-cell>
        <van-cell
          value-class="cell-value"
          title-class="cell-title"
          title="验收资料(附件)"
        >
          <template #default>
            <div
              class="default-box"
              v-if="detail.acceptanceFile"
              @click="lookFile(detail.acceptanceFiles[0])"
            >
              <img
                class="img-box"
                src="../../../assets/images/pdf_icon.png"
                alt=""
              />
              <span>{{ detail.acceptanceFiles[0].fileName }}</span>
            </div>
          </template>
        </van-cell>
        <van-cell
          value-class="cell-value"
          title-class="cell-title"
          title="问题描述"
          :value="detail.problem"
        />
        <van-cell
          value-class="cell-value"
          title-class="cell-title"
          title="提交时间"
          :value="detail.createTime"
        />
        <van-cell
          value-class="cell-value"
          title-class="cell-title"
          title="审核状态"
          :value="detail.statusType"
        />
      </van-cell-group>
      <div class="worksheet">
        <div class="worksheet-title">流程状态</div>
        <div class="block">
          <el-timeline>
            <el-timeline-item
              :timestamp="infoDetail.updateTime"
              color="#036EFF"
              class="active-timeline"
              placement="top"
            >
              <el-card class="card-box">
                <p>发起单位： {{ infoDetail.unitName }}</p>
                <p>问题描述： {{ infoDetail.comments }}</p>
              </el-card>
            </el-timeline-item>
            <el-timeline-item
              :timestamp="item.updateTime"
              :class="{
                'active-timeline':
                  item.id == detail.currentnode || item.status == 3,
              }"
              :color="
                item.id == detail.currentnode || item.status == 3
                  ? '#1492FF'
                  : ''
              "
              placement="top"
              v-for="item in infoList"
              :key="item.id"
            >
              <el-card class="card-box">
                <p>
                  {{ infoList.length == 1 ? "审核单位" : "审核单位(二级)" }}：
                  {{ item.unitName }}
                </p>
                <p>
                  处理状态：
                  <span
                    :class="{
                      'blue-color': item.status == 3,
                      'red-color': item.status == 2,
                      'orange-color': item.status == 1,
                    }"
                  >
                    {{ item.status | status }}
                  </span>
                </p>
                <p>审批意见：{{ item.comments }}</p>
                <div
                  class="card-btn"
                  @click="remind(item)"
                  v-if="
                    item.id == detail.currentnode &&
                    item.status != 3 &&
                    item.unitReviewerId == userInfo.id
                  "
                >
                  {{ item.reminderStatus == 1 ? "提醒" : "已提醒" }}
                </div>
              </el-card>
            </el-timeline-item>
            <el-timeline-item
              color="#1492FF"
              v-if="
                infoList[infoList.length - 1] &&
                infoList[infoList.length - 1].status == 3
              "
            >
              <div class="pigeonhole-box">
                归档 <span>{{ infoList[infoList.length - 1].updateTime }}</span>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
      <div
        class="btn-box"
        v-if="detail.status == 1 && detail.currentPeopleId == userInfo.id"
      >
        <van-button type="danger" block @click="goLink">审核</van-button>
      </div>
      <van-popup
        v-model="showPopup"
        position="bottom"
        :style="{ height: '100%' }"
      >
        <div class="files-look">
          <div class="files-look-title">
            <h3>
              {{ iframeTitle }}
            </h3>
            <van-icon
              class="close-box"
              @click="showPopup = false"
              name="close"
            />
          </div>
          <iframe class="iframe-box" :src="iframeSrc" frameborder="0"></iframe>
        </div>
      </van-popup>
    </div>
  </view-page>
</template>
<script>
import { contractApi } from "@/api/rainfall.js";
import { localData } from "@/plugins/storage";
export default {
  data() {
    return {
      detail: {},
      infoDetail: {},
      infoList: [],
      showPopup: false,
      iframeSrc: "",
      iframeTitle: "",
    };
  },
  computed: {
    userInfo() {
      return localData("get", "userInfo");
    },
  },
  mounted() {
    let id = this.$route.query.id;
    this.getList(id);
    this.getHiddenDangerDetail(id);
  },
  filters: {
    status(value) {
      switch (value) {
        case "1":
          return "审核中";
        case "2":
          return "已驳回";
        default:
          return "已通过";
      }
    },
  },
  methods: {
    goLink() {
      let link = `/businessApprovalSuggestion?id=${this.detail.id}&type=Acceptance`;
      this.$router.push(link);
    },
    async getList(id) {
      let res = await contractApi.list({
        id,
      });
      if (res.code == 0) {
        let detail = res.data.list[0];
        detail["serviceTimes"] = this.serviceTimes(detail);
        detail["statusType"] =
          detail.status == 1
            ? "审核中"
            : detail.status == 2
            ? "已驳回"
            : "已通过";
        this.detail = detail;
      }
    },
    serviceTimes(data) {
      if (data.serviceStartTime) {
        let startTime = data.serviceStartTime.split(" ")[0];
        let endTime = data.serviceEndTime.split(" ")[0];
        return `${startTime}至${endTime}`;
      } else {
        return "-";
      }
    },
    async getHiddenDangerDetail(id) {
      let res = await contractApi.nodeList({
        applicationid: id,
      });
      if (res.code == 0) {
        this.infoDetail = res.data[0];
        this.infoList = res.data.splice(1, res.data.length - 1);
      }
    },
    async remind(data) {
      if (data.reminderStatus == 2) {
        return false;
      }
      let res = await contractApi.reminderApplicationNode({
        nodeId: data.id,
      });
      if (res.code == 0) {
        let index = this.infoList.findIndex((item) => item.id == data.id);
        data.reminderStatus = 2;
        this.$set(this.infoList, index, data);
        this.$toast("提醒成功~");
      } else {
        this.$toast(res.message);
      }
    },
    lookFile(data) {
      if (data) {
        this.iframeSrc = data.fileUrl;
        this.iframeTitle = data.fileName;
        this.showPopup = true;
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.project-wrap {
  width: 100%;
  min-height: 100%;
  background: #f2f6f9;
  .cell-value {
    color: #333333;
    font-size: 20px;
    font-weight: 500;
    flex: 1.5;
  }
  .cell-title {
    font-weight: 500;
    color: #666666;
    font-size: 20px;
  }
  .default-box {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    height: 100%;
    .img-box {
      width: 13px;
      margin-right: 5px;
    }
    span {
      max-width: calc(100% - 18px);
      white-space: nowrap;
      text-overflow: ellipsis;
      overflow: hidden;
    }
  }

  .worksheet {
    margin-top: 6px;
    background-color: #ffffff;
    .worksheet-title {
      padding: 15px 0 0 11px;
      font-size: 22px;
      font-weight: 600;
      color: #333333;
    }
    .block {
      padding: 25px 12px 0px 27px;
      .card-box {
        box-shadow: none;
        border: none;
        background-color: #e9f4ff;
        position: relative;
        p {
          font-size: 20px;
          color: #333333;
          padding-bottom: 6px;
          &:last-child {
            padding-bottom: 0;
          }
        }
        .card-btn {
          position: absolute;
          font-size: 20px;
          color: #036eff;
          font-weight: 500;
          right: 15px;
          top: 50%;
          margin-top: -8px;
        }
      }
    }
  }
  .btn-box {
    padding: 0 10px 20px;
  }
  .files-look {
    height: 100%;
    .files-look-title {
      height: 44px;
      background-color: #036eff;
      color: #ffffff;
      position: relative;
      > h3 {
        line-height: 44px;
        text-align: center;
        font-size: 16px;
        padding: 0 30px;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
      }
      .close-box {
        position: absolute;
        right: 10px;
        top: 12px;
        font-size: 20px;
      }
    }
    .iframe-box {
      width: 100%;
      height: calc(100% - 48px);
    }
  }
}
</style>

<style lang="scss">
.van-cell {
  padding-left: 10px;
  padding-right: 10px;
  &::after {
    left: 0;
    right: 0;
    border-color: #e7e7ec;
  }
}
.block {
  .active-timeline {
    .el-timeline-item__tail {
      border-color: #036eff;
    }
  }
  .el-timeline-item__timestamp {
    color: #333333;
    font-size: 20px;
  }
  .card-box {
    .el-card__body {
      padding: 8px 15px 8px 10px;
    }
  }
  .pigeonhole-box {
    font-size: 20px;
    color: #333333;
    line-height: 22px;
    span {
      padding-left: 10px;
    }
  }
}
</style>