<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-22 17:16:19
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\repair\defectTreatment\DefectTreatmentInfo.vue
-->
<template>
  <div class="warp-info-box">
    <div class="wrap warp-left">
      <div class="wrap_box_title">
        <p>隐患处理工作单</p>
        <el-button type="primary" size="small" @click="goback"
          >返回上一级</el-button
        >
      </div>
      <div class="warp-left-block">
        <el-timeline>
          <el-timeline-item
            :timestamp="infoDetail.tm"
            placement="top"
            color="#1492FF"
            class="timeline-item"
          >
            <el-card class="card-box">
              <div class="first-card-box" @click="type = 1">
                <p>
                  上报人：<span>{{ infoDetail.creator }}</span>
                </p>
                <p>
                  隐患信息：
                  <span>{{ infoDetail.hiddenDanger }}</span>
                </p>
              </div>
            </el-card>
          </el-timeline-item>
          <el-timeline-item
            :timestamp="item.tm"
            placement="top"
            v-for="(item, index) in infoList"
            :key="item.id"
            :color="(index < infoList.length - 1 || lastInfo.status) ? '#1492FF' : ''"
            :class="{ 'timeline-item': (index < infoList.length - 1 || lastInfo.status) }"
          >
            <el-card class="card-box card-status-box">
              <div class="first-card-box" @click="checkTypeStatus(item)">
                <p>
                  {{ item.status | statusType }}人：<span>{{
                    item.operator
                  }}</span>
                </p>
                <p>
                  {{ item.status | statusType }}状态：<span>{{
                    item.status | status
                  }}</span>
                </p>
                <p>
                  审批意见：
                  <span>{{ item.opinion }}</span>
                </p>
              </div>
            </el-card>
          </el-timeline-item>
          <el-timeline-item v-if="lastInfo.status == 6" color="#1492FF">
            <div class="pigeonhole-box">归档 <span>{{lastInfo.createTime}}</span></div>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>
    <div class="warp-right">
      <baseInfo :infoDetail="infoDetail" v-if="type == 1" />
      <disposeInfo @goInfo="goInfo" :infoDetail="disDetail" v-if="type == 2" />
      <auditInfo @goInfo="goInfo" :infoDetail="disDetail" v-if="type == 3" />
    </div>
  </div>
</template>

<script>
import baseInfo from "./components/baseInfo.vue";
import disposeInfo from "./components/disposeInfo.vue";
import auditInfo from "./components/auditInfo.vue";
import { hiddenDanger } from "@/api/projectUnitDetail";
export default {
  components: { baseInfo, disposeInfo, auditInfo },
  data() {
    return {
      type: 1,
      infoDetail: {},
      infoList: [],
      lastInfo: {},
      disDetail: {},
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
  min-height: 100%;
  width: 100%;
  .warp-left {
    width: 436px;
    min-height: 100%;
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
          }
        }
        &.card-status-box {
          .first-card-box {
            p {
              &:nth-child(2) {
                span {
                  color: #1492ff;
                  &.no-p {
                    color: #ff9902;
                  }
                }
              }
            }
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
    min-height: 100%;
    .warp-right-content {
      padding: 20px;
    }
  }
}
</style>