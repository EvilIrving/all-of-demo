<template>
  <div class="daily-patrol">
    <div class="bg-purples">
      <el-row>
        <el-col :span="8" style="padding-right: 10px">
          <p class="item-label">派发人员</p>
          <el-input
            v-model="commonalityId.distributor"
            placeholder="-"
            type="text"
            disabled
          />
        </el-col>
        <el-col :span="8" style="padding-right: 10px">
          <p class="item-label">派发时间</p>
          <el-input
            v-model="commonalityId.createTime"
            placeholder="-"
            type="text"
            disabled
          />
        </el-col>
        <el-col :span="8" style="padding-right: 10px">
          <p class="item-label">项目阶段</p>
          <el-input :value="stage" placeholder="-" type="text" disabled />
        </el-col>
      </el-row>

      <!-- 巡查问题 -->
      <el-row class="patrol-content">
        <el-col :span="10" style="padding-right: 10px">
          <div class="grid-content">
            <el-row>
              <el-col :span="24" style="padding-right: 10px">
                <p class="item-label" style="margin-top: 0">项目状态</p>
                <el-input
                  :value="commonalityId.hiddenDanger == '0' ? '正常' : '异常'"
                  placeholder="-"
                  type="text"
                  disabled
                />
              </el-col>
              <el-col :span="24" style="padding-right: 10px">
                <p class="item-label" style="margin-top: 8px">巡查人员</p>
                <el-input
                  v-model="commonalityId.inspector"
                  placeholder="-"
                  type="text"
                  disabled
                />
              </el-col>
              <el-col :span="24" style="padding-right: 10px">
                <p class="item-label" style="margin-top: 8px">巡查日期</p>
                <el-input
                  v-model="commonalityId.rectifyBeginTime"
                  placeholder="-"
                  type="text"
                  disabled
                />
              </el-col>
              <!-- <el-col :span="24" style="padding-right: 10px">
                <p class="item-label" style="margin-top: 0">结束日期</p>
                <el-input
                  v-model="commonalityId.rectifyEndTime"
                  placeholder="-"
                  type="text"
                  disabled
                />
              </el-col> -->
            </el-row>
          </div>
        </el-col>
        <el-col :span="14">
          <div class="problem-stat">
            <span>巡查问题</span>
            <div class="status-stat">
              <el-radio class="un-deal" :value="'1'" label="1"
                >巡查中
                <span>{{ issueData.unDeal }}</span>
              </el-radio>
              <el-radio class="normal" :value="'1'" label="1"
                >正常
                <span>{{ issueData.normal }}</span>
              </el-radio>
              <el-radio class="un-normal" :value="'1'" label="1"
                >异常
                <span>{{ issueData.unNormal }}</span>
              </el-radio>
            </div>
          </div>
          <el-col :span="24" class="problem-box">
            <IssueListCollapse :list="issueList" />
          </el-col>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import IssueListCollapse from "./IssueListCollapse.vue";
import { projectSupervisionApi } from "@/api/projectSupervision";
export default {
  name: "daily-patrol",
  data() {
    return {
      issueData: {
        unDeal: 0,
        unNormal: 0,
        normal: 0,
      },
      issueList: [],
    };
  },
  components: { IssueListCollapse },
  props: {
    commonalityId: Object,
  },
  computed: {
    /** @returns {string} */
    stage() {
      return this.commonalityId.stage == "1"
        ? "开工阶段"
        : this.commonalityId.stage == "2"
        ? "施工阶段"
        : "完工阶段";
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.getList();
    });
  },
  methods: {
    // 获取问题列表根据巡查id
    async getList() {
      let res = await projectSupervisionApi.getList({
        inspectionId: this.commonalityId.id,
      });
      if (res.code == 0) {
        this.issueData = res.data;
        this.issueList = res.data.list;
      }
    },
    getTagType(item) {
      return item.status == "1" ? "success" : "danger";
    },
    getStatus(item) {
      return item.status == "1" ? "正常" : "异常";
    },
  },
};
</script>
<style lang="scss" scoped>
.daily-patrol {
  height: 100%;
  padding-left: 16px;
  .bg-purples {
    height: 100%;
    overflow: auto;
    border: 1px solid rgba(94, 91, 91, 0.2);
    padding: 10px;
    display: flex;
    flex-flow: column nowrap;

    > div {
      > p:nth-child(1) {
        font-size: 14px;
        color: rgba(0, 0, 0, 0.48);
        letter-spacing: 0;
        font-weight: 600;
        margin-bottom: 10px;
      }
    }

    .patrol-content {
      @include flex(1);
      padding-top: 16px;

      > .el-col {
        height: 100%;
    display: flex;
    flex-flow: column nowrap;
      }

      .problem-box {
        @include flex(1);
        overflow: auto;
        border: 1px solid rgba(94, 91, 91, 0.2);
        margin-top: 12px;
        padding: 12px;
      }

      .grid-content {
        height: 100%;
        padding: 10px;
        background: #fff;
        border: 1px solid rgba(94, 91, 91, 0.2);
        margin-top: 0;
      }
      .problem-stat {
        padding: 10px;
        border: 1px solid rgba(94, 91, 91, 0.2);
        display: flex;
        justify-content: space-between;
        align-items: center;
        > span {
          font-size: 20px;
          color: rgba(0, 0, 0, 0.88);
          line-height: 24px;
          font-weight: 600;
        }

        .status-stat {
          padding: 10px;

          ::v-deep {
            .el-radio {
              &.un-deal {
                .el-radio__input.is-checked .el-radio__inner {
                  border-color: #9a9a9a;
                  background: #9a9a9a;
                }
                .el-radio__label {
                  color: #9a9a9a;
                }
              }
              &.normal {
                .el-radio__input.is-checked .el-radio__inner {
                  border-color: #1492ff;
                  background: #1492ff;
                }
                .el-radio__label {
                  color: #1492ff;
                }
              }
              &.un-normal {
                .el-radio__input.is-checked .el-radio__inner {
                  border-color: #fa6400;
                  background: #fa6400;
                }
                .el-radio__label {
                  color: #fa6400;
                }
              }
            }
          }
        }
      }
    }
  }
}
.abnormal-style {
  padding: 5px 10px;
  color: #fff;
  background: #fa6400;
  border: 1px solid #fa6400;
}

.abnormal-sty {
  padding: 5px 10px;
  color: #fff;
  background: #1492ff;
  border: 1px solid #1492ff;
}
</style>
<!-- <style lang="scss" scoped>
.daily-patrol {
  height: 100%;
  padding-left: 16px;
  .bg-purples {
    height: 100%;
    overflow: auto;
    border: 1px solid rgba(94, 91, 91, 0.2);
    padding: 10px;
    display: flex;
    flex-flow: column nowrap;
    > div {
      > p:nth-child(1) {
        font-size: 14px;
        color: rgba(0, 0, 0, 0.48);
        letter-spacing: 0;
        font-weight: 600;
        margin-bottom: 10px;
      }
    }
    .patrol-content {
      flex: 1;
      padding-top: 16px;
      > .el-col {
        height: 100%;
        display: flex;
        flex-flow: column nowrap;
      }
      .problem-box {
        flex: 1;
        overflow: auto;
        border: 1px solid rgba(94, 91, 91, 0.2);
        margin-top: 12px;
        padding: 12px;
      }

      .grid-content {
        height: 100%;
        padding: 10px;
        background: #fff;
        border: 1px solid rgba(94, 91, 91, 0.2);
        > div {
          > p:nth-child(1) {
            font-size: 20px;
            color: rgba(0, 0, 0, 0.88);
            line-height: 24px;
            font-weight: 600;
          }
          > div {
            padding: 10px;
            ::v-deep.el-radio__label {
              color: #c2c2c2;
            }
            ::v-deep.el-radio__input.is-checked + .el-radio__label {
              color: #1f1f1f;
            }
            ::v-deep.el-radio__input.is-checked .el-radio__inner {
              border-color: #1492ff;
              background: #1492ff;
            }
            ::v-deep.el-checkbox__label {
              color: #1f1f1f;
            }
          }
        }
      }
    }
  }

  .radio1-sty {
    ::v-deep.el-radio__input.is-checked + .el-radio__label {
      color: #1f1f1f;
    }
    ::v-deep.el-radio__input.is-checked .el-radio__inner {
      border-color: #1492ff;
      background: #1492ff;
    }
  }
  .radio2-sty {
    ::v-deep.el-radio__input.is-checked + .el-radio__label {
      color: #1f1f1f;
    }
    ::v-deep.el-radio__input.is-checked .el-radio__inner {
      border-color: #fa6400;
      background: #fa6400;
    }
  }
  .abnormal-style {
    padding: 5px 10px;
    color: #fff;
    background: #fa6400;
    border: 1px solid #fa6400;
  }
  .abnormal-sty {
    padding: 5px 10px;
    color: #fff;
    background: #1492ff;
    border: 1px solid #1492ff;
  }
}
</style> -->