<template>
  <el-dialog
    :visible="visible"
    width="1800px"
    center
    append-to-body
    :close-on-click-modal="false"
    :before-close="close"
    title="任务管理"
    top="10vh"
    class="list-dialog scroll-dialog"
    fullscreen
  >
    <!-- 内容区域 -->
    <div class="content-box">
      <DetailDescription
        :project="basicsData"
        @edit="onEditDetails"
        @view="onViewDetails"
      />

      <!-- 监管统计 -->
      <el-row style="margin: 10px 0" class="statistics-card-box">
        <el-col :span="4" class="statistics-card-style" style="border-right: 0">
          <i class="icon-total-degree" />
          <div>
            <p>监管总次数</p>
            <p>
              <span style="color: #2fbeb3">
                {{ detailData.inspection.length || "-" }}
              </span>
              次
            </p>
          </div>
        </el-col>
        <el-col :span="4" class="statistics-card-style" style="border-right: 0">
          <i class="icon-problem-data" />
          <div>
            <p>发现问题数</p>
            <p>
              <span style="color: #f7b500">{{ detailData.findProblem }}</span
              >个
            </p>
          </div>
        </el-col>
        <el-col :span="4" class="statistics-card-style" style="border-right: 0">
          <i class="icon-rectify-reform" />
          <div>
            <p>未整改问题数</p>
            <p>
              <span style="color: #fa6400">{{ detailData.unDealProblem }}</span
              >个
            </p>
          </div>
        </el-col>
        <el-col :span="4" class="statistics-card-style" style="margin-right: 0">
          <i style="width: 64px; height: 64px" class="icon-overdodifi-number" />
          <div>
            <p>逾期未整改</p>
            <p>
              <span style="color: #e02020">{{ detailData.outTimeProblem }}</span
              >次
            </p>
          </div>
        </el-col>
        <el-col :span="4" class="statistics-card-style" style="margin-right: 0">
          <i class="icon-department-number" />
          <div>
            <p>移交执法部门数</p>
            <p>
              <span style="color: #e02020">{{
                detailData.hiddenDangerNum
              }}</span
              >次
            </p>
          </div>
        </el-col>
      </el-row>

      <el-tabs
        v-model="activeName"
        class="detail-item"
        v-if="activeTapName == 'first'"
      >
        <el-tab-pane
          v-for="(item, i) in inspectionList"
          :key="i"
          :label="getTabTitle(item, i)"
          :name="`${i + 1}`"
          lazy
        >
          <DetailContent
            :inspection="item"
            :time="inspectionList.length - i"
            :index="i + 1"
            @detail="detail"
          />
        </el-tab-pane>
        <el-tab-pane name="new" label="新增监管" :disabled="newDisabled" lazy>
          <DailyConfiguration
            :defaultRadio="radio"
            :data="detailData"
            @update="onNewUpdate"
            @close="close"
          />
        </el-tab-pane>
      </el-tabs>
      <div class="tb_wrapper" v-else>
        <!-- 列表 -->
        <el-table :data="tableData" stripe border style="width: 100%">
          <el-table-column
            type="index"
            label="序号"
            width="80"
            align="center"
          />
          <el-table-column prop="initiator" label="举报人员" align="center" />
          <el-table-column prop="createTime" label="日期" align="center" />
          <el-table-column
            prop="informContent"
            label="举报内容"
            align="center"
          />
        </el-table>
        <!-- 分页查询 -->
        <div class="pagination-box">
          <el-pagination
            class="right"
            background
            layout="total,sizes, prev, pager, next"
            :page-sizes="[5, 10, 20, 50]"
            :page-size="pageSize"
            @current-change="pageChange"
            :total="total"
            @size-change="handleSizeChange"
          />
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script>
//ts-check
import DetailDescription from "./components/DetailDescription.vue";
import DetailContent from "./components/DetailContent.vue";
import DailyConfiguration from "./components/DailyConfiguration.vue";
import { projectSupervisionApi } from "@/api/projectSupervision";

export default {
  props: { visible: Boolean, projectData: Object, activeTapName: String },
  components: {
    DailyConfiguration,
    DetailDescription,
    DetailContent,
  },
  data() {
    return {
      /**
       * 详情数据
       * @type {inspection:string[];findProblem:string;unDealProblem:string;outTimeProblem:string}
       */
      detailData: {
        inspection: [],
      }, //
      reviewResultShow: false,
      basicsData: {},
      /** 当前项目的巡查列表  */
      inspectionList: [],
      activeName: "1",
      radio: "1",
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
    };
  },
  computed: {
    /** @returns {boolean} */
    newDisabled() {
      if (this.inspectionList.length === 0) {
        return false;
      }
      const inspection = this.inspectionList[0];
      const currentInspectionIsDone =
        (inspection.patrolResult == 0) |
        (inspection.reviewResult === 0) |
        (inspection.resultStatus == 1) |
        (this.projectData.regulatoryStatus === 7);
      return !currentInspectionIsDone;
    },
  },
  watch: {
    projectData: {
      handler() {
        this.$nextTick(() => {
          this.detail(); //项目监管详情接口
        });
      },
      deep: true,
      immediate: true,
    },
  },
  mounted() {
    if (this.activeTapName != "first") {
      this.getInformList();
    }
  },
  methods: {
    pageChange(val) {
      this.pageNum = val;
      this.getInformList();
    },
    handleSizeChange(val) {
      this.pageNum = 1
      this.pageSize = val;
      this.getInformList();
    },
    async getInformList() {
      let res = await projectSupervisionApi.getInformList({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        projectId: this.projectData.id,
      });
      console.log(res);
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.total = res.data.totalRows
      }
    },
    getTabTitle(item, index) {
      if (item.createTime) {
        const date = this.$moment(item.createTime).format("YYYY-MM-DD");
        return `第${this.inspectionList.length - index}次监管 (${date})`;
      }
      return `第${this.inspectionList.length - index}次监管`;
    },
    // 查看详情
    onEditDetails() {
      this.$emit("details", { type: "edit", data: this.projectData });
    },
    // 查看详情
    onViewDetails() {
      this.$emit("details", { type: "view", data: this.projectData });
    },
    // 项目详情接口
    async detail() {
      const res = await projectSupervisionApi.detail({
        projectId: this.projectData.id,
      });
      if (res.code == 0) {
        this.detailData = res.data;
        this.basicsData = res.data.project;
        if (res.data.inspection.length > 0) {
          this.radio = "2";
          this.inspectionList = res.data.inspection.reverse();
        } else {
          this.activeName = "new";
        }
      }
    },
    onNewUpdate() {
      this.detail().then(() => {
        this.activeName = "1";
      });
    },
    // 关闭
    close() {
      this.$emit("close");
      this.$emit("update:visible", false);
    },
  },
};
</script>
<style lang="scss" scoped>
@import "@/style/listDialog.scss";
.list-dialog {
  ::v-deep .el-dialog {
    .el-dialog__body {
      padding-top: 12px;
      height: calc(100% - 4rem);
      .tb_wrapper {
        padding: 0;
        .el-table--border {
          border-bottom: 1px solid #ebeef5;
        }
        .el-input__inner {
          height: 28px;
          line-height: 28px;
          border: 1px solid #dcdfe6;
          background-color: #fff;
        }
      }
    }
  }
}
.content-box {
  height: 100%;
  overflow: hidden;
  display: flex;
  flex-flow: column nowrap;
  .detail-box {
    flex: 1;
    position: relative;
    overflow: hidden;
    .left-title {
      height: 100%;
      overflow-y: auto;
      background: #fff;
      border: 1px solid rgba(94, 91, 91, 0.2);
      > p {
        padding: 0 15px 15px 15px;
        font-size: 16px;
        font-weight: 600;
      }
      > div {
        padding-left: 25px;
        .el-timeline {
          padding-top: 24px;
          ::v-deep.el-timeline-item__wrapper {
            top: -12px;
          }
          ::v-deep.el-timeline-item__node--primary {
            background-color: #1492ff;
            width: 15px;
            height: 15px;
          }
          ::v-deep.el-timeline-item__tail {
            position: absolute;
            left: 0.25rem;
            height: 100%;
            border-left: 0.125rem solid #1492ff;
          }
        }
      }
    }
    .right-box {
      height: 100%;
    }
  }
}
.pagination-box {
  padding-top: 24px;
}
.basic-information-sty {
  border: 1px solid rgba(94, 91, 91, 0.2);
  background: #fff;
  padding: 10px;
  > div:nth-child(1) {
    display: flex;
    justify-content: space-between;
    padding-bottom: 10px;
    border-bottom: 2px solid rgba(94, 91, 91, 0.48);
    > p:nth-child(1) {
      font-family: PingFangSC-Medium;
      font-size: 18px;
      color: rgba(0, 0, 0, 0.4);
      line-height: 34px;
      font-weight: 600;
    }
    > p:nth-child(2) {
      color: #fff;
      letter-spacing: 0;
      font-weight: 600;
      background: #1492ff;
      border-radius: 4px;
      padding: 10px 12px;
    }
  }
}
.statistics-card-box {
  display: flex;
}
.statistics-card-style {
  background: #fff;
  display: flex;
  justify-content: center;
  padding: 10px;
  margin-bottom: 15px;
  border: 1px solid rgba(94, 91, 91, 0.2);
  flex: 1;
  > i {
    width: 64px;
    height: 64px;
    &.supervision-icon-01 {
      background: url("../../../../assets/images/supervision-icon-01.png")
        no-repeat;
      background-size: 100% 100%;
    }
    &.supervision-icon-02 {
      background: url("../../../../assets/images/supervision-icon-02.png")
        no-repeat;
      background-size: 100% 100%;
    }
    &.supervision-icon-03 {
      background: url("../../../../assets/images/supervision-icon-03.png")
        no-repeat;
      background-size: 100% 100%;
    }
    &.supervision-icon-04 {
      background: url("../../../../assets/images/supervision-icon-04.png")
        no-repeat;
      background-size: 100% 100%;
    }
    &.supervision-icon-05 {
      background: url("../../../../assets/images/supervision-icon-05.png")
        no-repeat;
      background-size: 100% 100%;
    }
  }
  > div {
    margin-left: 10px;
    margin-top: 7px;
    > p:nth-child(2) {
      margin-top: 15px;
      > span {
        font-size: 24px;
        margin-right: 10px;
      }
    }
  }
}

.el-timeline-item__content {
  height: 40px;
  line-height: 40px;
  color: #fff;
  background: #1492ff;
  width: 160px;
  padding-left: 10px;
  border-radius: 5px;
}
.el-timeline-item__contents {
  height: 40px;
  line-height: 40px;
  color: rgba(0, 0, 0, 0.88);
  background: #fff;
  width: 160px;
  padding-left: 10px;
  border-radius: 5px;
}
</style>
