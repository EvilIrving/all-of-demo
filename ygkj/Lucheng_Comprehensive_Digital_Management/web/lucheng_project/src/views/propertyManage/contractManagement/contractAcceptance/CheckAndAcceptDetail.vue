<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-08-17 10:30:18
 * @Description: 
 * @FilePath: \lucheng_project\src\views\propertyManage\contractManagement\contractAcceptance\CheckAndAcceptDetail.vue
-->
<template>
  <div class="warp-info-box">
    <div class="wrap warp-left">
      <div class="wrap_box_title">
        <p>流程状态</p>
        <el-button type="primary" size="small" @click="goback"
          >返回上一级</el-button
        >
      </div>
      <div class="warp-left-block">
        <el-timeline>
          <el-timeline-item
            :timestamp="infoDetail.updateTime"
            placement="top"
            color="#1492FF"
            class="timeline-item"
          >
            <el-card class="card-box">
              <div class="first-card-box" @click="type = 1">
                <p>发起审批</p>
                <p>
                  发起单位：<span>{{ infoDetail.unitName }}</span>
                </p>
                <p>
                  问题描述：<span>{{ infoDetail.comments }}</span>
                </p>
              </div>
            </el-card>
          </el-timeline-item>
          <el-timeline-item
            :timestamp="item.updateTime"
            :class="{
              'timeline-item':
                item.id == detailData.currentnode || item.status == 3,
            }"
            :color="
              item.id == detailData.currentnode || item.status == 3
                ? '#1492FF'
                : ''
            "
            placement="top"
            v-for="item in infoList"
            :key="item.id"
          >
            <el-card class="card-box card-status-box">
              <div class="first-card-box" @click="type = 1">
                <p>{{ item.status | status }}</p>
                <p>
                  {{
                    infoList.length == 1 ? "审核单位" : "审核单位(二级)"
                  }}：<span>{{ item.unitName }}</span>
                </p>
                <p>
                  审批意见：<span>{{ item.comments }}</span>
                </p>
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
    <div class="warp-right">
      <div class="wrap warp-right-info">
        <div class="wrap_box_title">
          <p>基本信息</p>
        </div>
        <div>
          <div class="info-one padding-22">
            <div>
              <div class="lable-item">合同名称</div>
              <div class="value-item">{{ detailData.contractName || "-" }}</div>
            </div>
            <div>
              <div class="lable-item">服务期限</div>
              <div class="value-item" v-if="detailData.serviceStartTime">
                {{ detailData.serviceStartTime }}至{{
                  detailData.serviceEndTime
                }}
              </div>
              <div class="value-item" v-else>-</div>
            </div>
            <div>
              <div class="lable-item">审核人</div>
              <div class="value-item">
                {{ reviewerName || "-" }}
              </div>
            </div>
          </div>
          <div>
            <div class="lable-item">问题描述</div>
            <div class="value-item">
              {{ detailData.problem || "-" }}
            </div>
          </div>
        </div>
      </div>
      <div class="wrap warp-right-info">
        <div class="wrap_box_title">
          <p>附件列表</p>
        </div>
        <div class="flis-list">
          <div class="flis-list-item" v-if="detailData.contractFile.length > 0">
            <div>1.{{detailData.contractFiles[0].fileName}}</div>
            <div>
              <span @click="lookFiles(detailData.contractFiles)">查看</span>
              <a :href="detailData.contractFiles[0].fileUrl" download target="_blank">下载</a>
            </div>
          </div>
          <div class="flis-list-item" v-if="detailData.acceptanceFile.length > 0">
            <div>2.{{detailData.acceptanceFiles[0].fileName}}</div>
            <div>
              <span  @click="lookFiles(detailData.acceptanceFiles)">查看</span>
              <a :href="detailData.acceptanceFiles[0].fileUrl" download target="_blank">下载</a>
            </div>
          </div>
        </div>
      </div>
      <div class="wrap warp-right-info">
        <div class="wrap_box_title">
          <p>审批意见</p>
        </div>
        <div>
          <el-input
            type="textarea"
            :rows="5"
            class="textarea-box"
            placeholder="请输入内容"
            v-model="formData.comments"
            resize="none"
            :readonly="detailData.status == 3 && !operationType"
          >
          </el-input>
        </div>
      </div>
      <div class="wrap warp-btn-box">
        <el-button class="btn-wrap" size="small" @click="goback"
          >返回</el-button
        >
        <el-button
          v-if="detailData.status != 3 && operationType"
          class="btn-wrap"
          size="small"
          type="primary"
          @click="submit(2)"
          >通过</el-button
        >
        <el-button
          v-if="detailData.status != 3 && operationType"
          class="btn-wrap"
          size="small"
          type="danger"
          @click="submit(3)"
          >驳回</el-button
        >
      </div>
    </div>
  </div>
</template>

<script>
import { contractApi } from "@/api/projectUnitDetail.js";
import { cloneObj } from "@/utils/utils.js";
import LookFile from "@/components/LookFile.vue";
export default {
  props: {
    id: String,
    detailData: Object,
  },
  components:{
    LookFile
  },
  data() {
    return {
      type: 1,
      infoDetail: {},
      infoList: [],
      lastInfo: {},
      disDetail: {},
      formData: {
        comments: "",
      },
      currIndex: 0,
      submitType: true,
      operationType: true,
      reviewerName: '',
    };
  },
  filters: {
    statusType(value) {
      if (value == 1 || value == 4) {
        return "处理";
      } else if (value == 2 || value == 3 || value == 5) {
        return "审核";
      }
    },
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
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
    userInfo() {
      return this.$sessionData("get", "userInfo");
    },
  },
  mounted() {
    this.getHiddenDangerDetail();
  },
  methods: {
    lookFiles(file){
      this.$emit('lookFile', file[0].fileUrl)
    },
    async getHiddenDangerDetail() {
      let res = await contractApi.nodeList({
        applicationid: this.id,
      });
      if (res.code == 0) {
        this.infoDetail = res.data[0];
        this.infoList = res.data.splice(1, res.data.length - 1);
        this.currIndex = this.infoList.findIndex(
          (item) => item.id == this.detailData.currentnode
        );
        this.reviewerName = this.infoList[this.currIndex].unitReviewerName
        if(this.infoList[this.currIndex].unitReviewerId != this.userInfo.id){
          this.operationType = false
        }
        if(this.detailData.status == 3){
          this.formData.comments = this.infoList[this.infoList.length - 1].comments
        }
      }
    },
    submit(num) {
      if (!this.submitType) {
        return false;
      }
      this.submitType = false;
      let formData = cloneObj(this.formData);
      formData["applicationid"] = this.detailData.id;
      formData["nodeId"] = this.detailData.currentnode;
      if (num == 2) {
        contractApi
          .passApplicationNode(formData)
          .then((res) => {
            if (res.code == 0) {
              this.$message.success("通过成功~");
              this.goback();
            } else {
              this.$message.error(res.message);
            }
            this.submitType = true;
          })
          .catch((err) => {
            this.$message.error(err);
            this.submitType = true;
          });
      } else {
        contractApi
          .rejectApplicationNode(formData)
          .then((res) => {
            if (res.code == 0) {
              this.$message.success("驳回成功~");
              this.goback();
            } else {
              this.$message.error(res.message);
            }
            this.submitType = true;
          })
          .catch((err) => {
            this.$message.error(err);
            this.submitType = true;
          });
      }
    },
    goback() {
      this.$emit("checkType", 1);
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  @include contentBox();
  padding-top: 16px;
  .wrap_box_title {
    position: relative;
    line-height: 25px;
    padding: 0 10px 16px 24px;
    border-bottom: 1px solid #eeeeee;
    display: flex;
    justify-content: space-between;
    align-content: center;
    &::after {
      content: "";
      position: absolute;
      width: 4px;
      height: 20px;
      background: #1492ff;
      left: 10px;
      top: 2px;
    }
    > p {
      font-size: 18px;
      font-weight: bold;
      color: #333333;
    }
  }
}
.warp-info-box {
  display: flex;
  justify-content: space-between;
  width: 100%;
  .warp-left {
    width: 436px;
    .warp-left-block {
      padding: 20px;
      .timeline-item {
        ::v-deep .el-timeline-item__tail {
          border-left-color: #1492ff;
        }
      }
      .card-box {
        cursor: pointer;
        ::v-deep .el-card__body {
          padding: 16px;
        }
        .first-card-box {
          p {
            color: #999999;
            font-size: 14px;
            line-height: 22px;
            span {
              font-size: 16px;
              color: #333333;
            }
            &:first-child {
              color: #1492ff;
              font-size: 16px;
              padding-bottom: 10px;
            }
          }
        }
        &.card-status-box {
          .first-card-box {
          }
        }
      }
      .pigeonhole-box {
        font-size: 16px;
        color: #999999;
        line-height: 22px;
        span {
          padding-left: 10px;
          font-size: 14px;
        }
      }
    }
  }
  .warp-right {
    width: calc(100% - 456px);
    > div {
      margin-top: 20px;
      &:first-child {
        margin-top: 0;
      }
    }
  }
}

.warp-right-info {
  padding: 16px 0 4px;
  > div:nth-child(2) {
    padding: 20px;
  }
  .lable-item {
    font-size: 16px;
    color: #999999;
    padding-bottom: 10px;
  }
  .value-item {
    font-weight: 500;
    color: #333333;
    line-height: 18px;
    font-size: 16px;
  }
  .padding-22 {
    padding-bottom: 22px;
  }
  .info-one {
    display: flex;
    > div {
      flex: 1;
    }
  }
  .imgae-item {
    display: flex;
    flex-wrap: wrap;
    .image-box {
      width: 80px;
      height: 80px;
      margin: 0 20px 20px 0;
    }
  }
}
.warp-btn-box {
  padding-bottom: 16px;
  text-align: center;
  .btn-wrap {
    width: 84px;
  }
}

.flis-list {
  padding: 20px;
  box-sizing: border-box;
  .flis-list-item {
    background-color: #f4f8fe;
    height: 48px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px 0 16px;
    margin-bottom: 20px;
    > div:nth-child(1) {
      color: #666666;
      font-size: 16px;
      width: calc(100% - 120px);
    }
    > div:nth-child(2) {
      color: #1492ff;
      font-size: 16px;
      >span{
        cursor: pointer;
      }
      > span:nth-child(1) {
        margin-right: 20px;
      }
      >a{
        color: #1492ff;
      }
    }
    &:last-child{
      margin-bottom: 0;
    }
  }
}
</style>