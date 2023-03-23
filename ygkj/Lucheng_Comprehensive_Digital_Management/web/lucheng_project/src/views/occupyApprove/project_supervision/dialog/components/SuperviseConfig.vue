<template>
  <div class="supervise-config">
    <!-- 复核监管项鉴定 -->
    <el-row>
      <el-col :span="10" class="left-view">
        <div class="grid-content">
          <div class="part-item">
            <p class="item-label" style="margin-top: 0">巡查项目</p>
            <el-input
              :value="
                inspection.projectName == '' ? '-' : inspection.projectName
              "
              placeholder="-"
              type="text"
              disabled
            />
          </div>
          <div class="part-item">
            <p class="item-label" style="margin-top: 0">正常情况</p>
            <el-input
              :value="inspection.stage == '0' ? '正常' : '异常'"
              placeholder="-"
              type="text"
              disabled
            />
          </div>
          <div class="part-item">
            <p class="item-label" style="margin-top: 0">巡查人员</p>
            <el-input
              v-model="inspection.inspector"
              placeholder="-"
              type="text"
              disabled
            />
          </div>
          <!-- <div class="part-item">
            <p class="item-label" style="margin-top: 0">开始日期</p>
            <el-input
              v-model="inspection.createTime"
              placeholder="-"
              type="text"
              disabled
            />
          </div> -->
          <div class="part-item">
            <p class="item-label" style="margin-top: 0">复核日期</p>
            <el-input
              v-model="inspection.patrolEndTime"
              placeholder="-"
              type="text"
              disabled
            />
          </div>
        </div>
      </el-col>
      <el-col :span="14" class="right-view">
        <el-col
          :span="24"
          class="grid-content"
          style="height: 40px; margin-bottom: 12px"
        >
          <div>
            <span>复核内容</span>
          </div>
        </el-col>
        <el-col :span="24" class="grid-content">
          <p
            class="problem-item"
            v-for="(item, index) in issueList"
            :key="index"
          >
            {{ index + 1 + "、" + item.entryName }}
          </p>
        </el-col>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { projectSupervisionApi } from "@/api/projectSupervision";

export default {
  name: "supervise-config",
  props: {
    inspection: Object,
  },
  data() {
    return {
      /** @type {{urls:string[];status:string;entryName:string;problemDescrip:string;}[]} */
      issueList: [],
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    // 获取问题列表根据巡查id
    async getList() {
      let res = await projectSupervisionApi.getList({
        inspectionId: this.inspection.id,
        rectify: 1,
        status: 2,
      });
      if (res.code == 0) {
        this.issueList = res.data.list;
      }
    },
  },
};
</script>


<style lang="scss" scoped>
.supervise-config {
  height: 100%;
  display: flex;
  flex-flow: column nowrap;
  .btn-bottom {
    margin-top: 12px;
    width: 100%;

    display: flex;
    flex-flow: row-reverse nowrap;
    align-items: flex-end;
  }
  > .el-row {
    @include flex(1);
    padding-left: 12px;
    overflow-y: auto;
    .grid-content {
      height: 100%;
      padding: 10px;
      background: #fff;
      border: 1px solid rgba(94, 91, 91, 0.2);
      overflow: auto;
      position: relative;
      .float-right-btn {
        position: absolute;
        right: 20px;
        top: 5px;
      }
    }
    .left-view {
      height: 100%;
      padding-right: 10px;
      .grid-content {
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
        ::v-deep .el-input-number {
          .el-input .el-input__inner {
            height: 40px;
          }
        }
      }
      .depart-left {
        display: flex;
        flex-flow: column nowrap;
        height: 100%;
        .depart-left-content {
          flex: 1;
          overflow: auto;
        }
      }
    }
    .right-view {
      height: 100%;
      display: flex;
      flex-flow: column nowrap;
      .grid-content {
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
      .problem-descrip {
        padding-left: 18px;
      }
      .problem-input {
        margin-top: 12px;
      }
    }
    .problem-item {
      margin: 10px 0;
      line-height: 30px;
    }
  }
  .inspectors-sty {
    background: #1492ff;
    padding: 5px 12px;
    color: #fff;
    margin-right: 10px;
  }
  .preserve-sty {
    border: 0;
    background: #1492ff;
    color: #fff;
    float: right;
    margin-right: 10px;
    border-radius: 4px;
  }
}
</style>