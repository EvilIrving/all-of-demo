<script>
import { projectSupervisionApi } from "@/api/projectSupervision";

import IssueListCollapse from "./IssueListCollapse.vue";
export default {
  props: {
    /** @type {import('vue/types/options').PropValidator<{id:string;stage:number;projectName:string;inspector:string;patrolEndTime:string;appraisalResult:null|string}>} */
    inspection: Object,
  },
  components: { IssueListCollapse },
  data() {
    return {
      issueList: [],
      appraisalResult: "",
    };
  },
  mounted() {
    this.getIssueList();
  },
  methods: {
    /** @param {boolean} flag */
    onConfirm(flag) {
      const opt = {
        inspectionId: this.inspection.id,
        flag,
        appraisalResult: this.appraisalResult,
      };
      projectSupervisionApi.hiddenDanger(opt).then((res) => {
        if (res.code === 0) {
          this.$emit("detail");
        }
      });
    },
    async getIssueList() {
      if (this.inspection.appraisalResult !== null) {
        this.appraisalResult = this.inspection.appraisalResult;
      }
      const opt = { inspectionId: this.inspection.id, rectify: 1, status: 2 };
      projectSupervisionApi.getList(opt).then((res) => {
        if (res.code == 0) {
          this.issueList = res.data.list;
        }
      });
    },
  },
};
</script>
<template>
  <div class="supervise-identification">
    <div class="supervise-identification__content">
      <div class="content-left">
        <el-descriptions title="项目信息" direction="vertical" :column="1">
          <el-descriptions-item label="项目状态">
            {{ inspection.stage == 0 ? "正常" : "异常" }}
          </el-descriptions-item>
          <el-descriptions-item label="巡查人员">
            {{ inspection.inspector }}
          </el-descriptions-item>
          <el-descriptions-item label="复核日期">
            {{ inspection.patrolEndTime }}
          </el-descriptions-item>
          <el-descriptions-item label="鉴定结果">
            <template v-if="inspection.appraisalResult !== null">
              {{ inspection.appraisalResult }}
            </template>
            <el-input
              v-else
              v-model="appraisalResult"
              type="textarea"
              :rows="4"
              placeholder="文本输入"
              resize="none"
            />
          </el-descriptions-item>
        </el-descriptions>
        <div class="confirm-buttons" v-if="inspection.appraisalResult == null">
          <el-button @click="onConfirm(false)"> 不是隐患 </el-button>
          <el-button @click="onConfirm(true)"> 确认隐患 </el-button>
        </div>
      </div>
      <div class="content-right">
        <p class="title">复核内容</p>
        <IssueListCollapse :list="issueList" />
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.supervise-identification {
  margin-left: 16px;
  height: 100%;
  border: 1px solid #eee;
  padding: 16px;
}
.supervise-identification__content {
  width: 100%;
  height: 100%;
  display: flex;
  .content-left {
    width: 40%;
    display: flex;
    flex-direction: column;
    .el-descriptions {
      flex: 1;
      border: 1px solid #eee;
      padding: 16px;
    }
    .confirm-buttons {
      margin-top: 8px;
      margin-left: auto;
    }
  }
  .content-right {
    width: 60%;
    border: 1px solid #eee;
    padding: 16px;
    margin-left: 16px;
    .title {
      font-size: 18px;
      padding: 8px 0;
    }
  }
}
</style>
