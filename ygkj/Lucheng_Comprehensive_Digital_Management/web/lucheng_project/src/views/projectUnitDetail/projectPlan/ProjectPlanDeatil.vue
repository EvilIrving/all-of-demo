<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-27 11:27:38
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\projectPlan\ProjectPlanDeatil.vue
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
            timestamp="2021-04-20 10:00"
            placement="top"
            color="#1492FF"
            class="timeline-item"
          >
            <el-card class="card-box">
              <div class="first-card-box" @click="type = 1">
                <p>发起审批</p>
                <p>发起单位：<span>物业公司</span></p>
                <p>
                  问题描述：<span
                    >这是一条问题描述这是一条问题描述这是一条问题描述这是一条问题描述</span
                  >
                </p>
              </div>
            </el-card>
          </el-timeline-item>
          <el-timeline-item
            timestamp="2021-04-25 14:30"
            class="timeline-item"
            color="#1492FF"
            placement="top"
          >
            <el-card class="card-box card-status-box">
              <div class="first-card-box" @click="type = 1">
                <p>已通过</p>
                <p>审核单位：<span>运行管理单位</span></p>
                <p>
                  审批意见：<span
                    >这是一条问题描述这是一条问题描述这是一条问题描述这是一条问题描述</span
                  >
                </p>
              </div>
            </el-card>
          </el-timeline-item>
          <el-timeline-item
            timestamp="2021-04-26 14:30"
            class="timeline-item"
            color="#1492FF"
            placement="top"
          >
            <el-card class="card-box card-status-box">
              <div class="first-card-box" @click="type = 1">
                <p>待审批</p>
                <p>审核单位(二级)：<span>区农业农村局</span></p>
                <p>
                  审批意见：<span
                    >这是一条问题描述这是一条问题描述这是一条问题描述这是一条问题描述</span
                  >
                </p>
              </div>
            </el-card>
          </el-timeline-item>
          <el-timeline-item color="#1492FF">
            <div class="pigeonhole-box">归档 <span>2021-04-26 14:30</span></div>
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
              <div class="lable-item">工程名称</div>
              <div class="value-item">XXXXXXXXXXXXXX</div>
            </div>
            <div>
              <div class="lable-item">编制单位</div>
              <div class="value-item">XXXXXXX物业管理单位</div>
            </div>
            <div>
              <div class="lable-item">预案类型</div>
              <div class="value-item">XXXXX</div>
            </div>
          </div>
          <div>
            <div class="lable-item">详情描述</div>
            <div class="value-item">
              这是一条详情这是一条详情这是一条详情这是一条详情这是一条详情
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
            :rows="15"
            class="textarea-box"
            placeholder="请输入内容"
            v-model="formData.opinion"
            resize="none"
          >
          </el-input>
        </div>
      </div>
      <div class="wrap warp-btn-box">
        <el-button class="btn-wrap" size="small" @click="goback"
          >返回</el-button
        >
        <el-button
          class="btn-wrap"
          size="small"
          type="primary"
          @click="submit(2)"
          >通过</el-button
        >
        <el-button
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
import { hiddenDanger } from "@/api/projectUnitDetail";
export default {
  data() {
    return {
      type: 1,
      infoDetail: {},
      infoList: [],
      lastInfo: {},
      disDetail: {},
      formData: {
        opinion: "",
      },
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
        case 1:
          return "待处理";
        case 2:
          return "已通过";
        case 3:
          return "已驳回";
        case 4:
          return "已处理";
        case 5:
          return "待审核";
        default:
          return "归档";
      }
    },
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
    troubleDetail() {
      return this.$store.state.troubleDetail;
    },
  },
  mounted() {
    console.log(this.troubleDetail);
    this.getHiddenDangerDetail();
  },
  methods: {
    async getHiddenDangerDetail() {
      let res = await hiddenDanger.getHiddenDangerDetail({
        id: this.troubleDetail.id,
      });
      if (res.code == 0) {
        this.infoDetail = res.data[0].info;
        this.lastInfo =
          res.data[res.data.length - 1].status == 6
            ? res.data[res.data.length - 1]
            : {};
        this.infoList = res.data.splice(1, res.data.length - 1);
        if (this.lastInfo.status) {
          this.infoList.pop();
        }
      }
    },
    checkTypeStatus(item) {
      if (item.status == 1 || item.status == 4) {
        this.type = 2;
      } else {
        this.type = 3;
      }
      this.disDetail = item;
    },
    goback() {
      this.$emit("checkType", 1);
    },
    goInfo() {
      this.getHiddenDangerDetail();
      this.type = 1;
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
</style>