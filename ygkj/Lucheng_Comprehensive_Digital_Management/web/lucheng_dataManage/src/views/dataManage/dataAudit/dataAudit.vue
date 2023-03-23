<!--
 * @Author: zg
 * @Date: 2022-03-23 16:17:48
 * @LastEditTime: 2022-03-23 17:13:45
 * @LastEditors: zg
 * @Description: 数据申请
 * @FilePath: \web-data-center\src\views\dataManage\resourceDirectory\resourceDirectory.vue
-->
<template>
  <div class="module_content">
    <div class="mc_main">
      <div class="table_title">
        <p>数据申请</p>
      </div>
      <div class="table_head">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="我发起的" name="1"></el-tab-pane>
          <el-tab-pane label="待我审批" name="2"></el-tab-pane>
          <el-tab-pane label="我已处理" name="3"></el-tab-pane>
        </el-tabs>
        <div class="search_item">
          <span>申请类型：</span>
          <el-select
            v-model="queryParams.applyType"
            placeholder="请选择申请类型"
            clearable
            @change="refresh()"
          >
            <el-option
              v-for="item in applyTypeOption"
              :key="item.value"
              :value="item.value"
              :label="item.label"
            />
          </el-select>
        </div>
        <div class="search_item" v-show="this.activeName != '2'">
          <span>申请结果：</span>
          <el-select
            v-model="queryParams.applyStatus"
            placeholder="请选择申请结果"
            clearable
            @change="refresh()"
          >
            <el-option
              v-for="item in applyStatusOption"
              :key="item.value"
              :value="item.value"
              :label="item.label"
            />
          </el-select>
        </div>
      </div>

      <div class="table_content">
        <div ref="tableBox" class="table-box">
          <!-- 表格 -->
          <table-page
            ref="tablePage"
            :methodType="methodType"
            :url="tableUrl"
            :columns="columns"
            :query-params="queryParams"
            :checkBox="false"
            :height="tableHeight"
          >
            <template slot-scope="{ index }" slot="order">{{
              index + 1
            }}</template>
            <template slot-scope="{ row }" slot="applyType">
              <span v-if="row.applyType == 1">新增</span>
              <span v-else-if="row.applyType == 2">删除</span>
              <span v-else-if="row.applyType == 3">修改</span>
            </template>
            <template slot-scope="{ row }" slot="applyStatus">
              <span v-if="row.applyStatus == -1">驳回</span>
              <span v-else-if="row.applyStatus == 0">待处理</span>
              <span v-else-if="row.applyStatus == 1">通过</span>
            </template>
            <template slot-scope="{ row }" slot="action">
              <el-button
                v-if="activeName != '2'"
                @click="aduitData(row, true)"
                type="text"
                >详情</el-button
              >
              <el-button
                v-if="activeName == '1' && row.applyStatus == 0"
                @click="deleteAduitData(row)"
                type="text"
                >撤回</el-button
              >
              <el-button
                v-if="activeName == '2'"
                @click="aduitData(row, false)"
                type="text"
                >审核</el-button
              >
            </template>
          </table-page>
        </div>
      </div>
    </div>

    <!-- 审核弹窗 -->
    <el-dialog
      v-if="auditDialogShow"
      :title="dialogTitle"
      :visible.sync="auditDialogShow"
      :close-on-click-modal="false"
      width="80%"
      top="10vh"
      class="auditDialog"
      @close="closeAuditDialog"
    >
      <AuditDialog
        ref="auditDialog"
        :isLook="isLook"
        @close="closeAuditDialog"
        @closeDialogAndRefresh="closeAuditDialogAndRefresh"
      ></AuditDialog>
    </el-dialog>
  </div>
</template>

<script>
import { deleteAduitDataById } from "_a/dataManage/dataAudit/dataAudit";
import { ConfirmCustom, MessageError, MessageSuccess } from "_l/message";
import TablePage from "_c/CommonForm/table-page";
import AuditDialog from "./components/auditDialog";

export default {
  name: "dataAudit",
  components: {
    TablePage,
    AuditDialog,
  },
  data() {
    return {
      methodType: "get",
      tableUrl: "/data-manage/dataApply/page",
      columns: [
        { label: "序号", slot: "order", width: 50 },
        { label: "数据源id", prop: "dataSourceId" },
        { label: "表名", prop: "tableName" },
        { label: "申请类型", slot: "applyType" },
        { label: "申请结果", slot: "applyStatus" },
        { label: "备注", prop: "remark" },
        { label: "创建id", prop: "createId" },
        { label: "创建时间", prop: "createTime" },
        { label: "操作", slot: "action", width: 120 },
      ],
      queryParams: {
        page: true,
        type: "1",
      },
      tableHeight: 0,
      activeName: "1",
      applyTypeOption: [
        {
          label: "新增",
          value: 1,
        },
        {
          label: "修改",
          value: 3,
        },
        {
          label: "删除",
          value: 2,
        },
      ],
      applyStatusOption: [
        {
          label: "驳回",
          value: -1,
        },
        {
          label: "待处理",
          value: 0,
        },
        {
          label: "通过",
          value: 1,
        },
      ],
      auditDialogShow: false,
      dialogTitle: "审核",
      isLook: true,
    };
  },
  watch: {},
  methods: {
    handleClick() {
      this.queryParams = {};
      this.queryParams.type = this.activeName;
      this.$nextTick(function () {
        this.refresh();
      });
    },
    /**
     * 刷新
     */
    refresh() {
      this.$refs.tablePage.refresh();
    },
    /**
     * 重置搜索条件
     */
    reset() {
      this.queryParams = {};
      this.$nextTick(function () {
        this.refresh();
      });
    },

    // 删除我发起的数据修改请求
    deleteAduitData(data) {
      ConfirmCustom({ type: "warning" }).then(() => {
        deleteAduitDataById(data.id).then((res) => {
          if (res.code) {
            MessageSuccess("del");
          } else {
            MessageError("del");
          }
          // 刷新表格
          this.refresh();
        });
      });
    },

    /**
     * 打开审核弹窗
     */
    aduitData(row, isShow) {
      if (isShow) {
        this.dialogTitle = "详情";
      } else {
        if (row.applyType == 1) {
          this.dialogTitle = "新增审核";
        } else if (row.applyType == 2) {
          this.dialogTitle = "删除审核";
        } else if (row.applyType == 3) {
          this.dialogTitle = "修改审核";
        }
      }
      this.isLook = isShow;
      this.auditDialogShow = true;
      this.$nextTick(() => {
        this.$refs.auditDialog.getColumnData(row);
      });
    },
    /**
     * 关闭审核弹窗
     */
    closeAuditDialog() {
      this.auditDialogShow = false;
    },
    closeAuditDialogAndRefresh() {
      this.closeAuditDialog();
      this.refresh();
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = this.$refs.tableBox.offsetHeight - 42;
    });
  },
};
</script>

<style scoped lang="scss">
@import "@/styles/customizeHandle.scss";
.module_content {
  width: 100%;
  height: 100%;
  padding: 16px;
  @include themeify {
    background-color: themed("background_color1");
  }
  .mc_main {
    width: 100%;
    height: 100%;
    @include themeify {
      background-color: themed("background_color2");
    }
    @include themeify {
      box-shadow: 0px 1px 4px 0px themed("box_shadow_color1"),
        0px 0px 2px 0px themed("box_shadow_color2");
    }
    border-radius: 2px;
    float: left;

    .table_title {
      width: 100%;
      height: 56px;
      padding: 16px 10px;
      border-bottom: 1px solid #eeeeee;
      p {
        font-size: 18px;
        font-weight: 600;
        color: #333;
        line-height: 20px;
        padding-left: 10px;
        border-left: 4px solid #1492ff;
      }
    }
    .table_head {
      padding: 0 24px;
      .search_item {
        float: left;
        margin-bottom: 16px;
        margin-right: 24px;
        > span {
          font-size: 14px;
        }
        ::v-deep .el-select,
        ::v-deep .el-input {
          width: unset;
          height: 32px;
          .el-input__inner {
            width: 160px;
            height: 100%;
            background: transparent;
            border: 1px solid #d9d9d9;
            border-radius: 4px;
            font-size: 14px;
            color: #333;
          }
          .el-input__icon {
            line-height: 32px;
          }
        }
        ::v-deep .el-date-editor.el-input__inner {
          width: unset;
          height: 32px;
          background: transparent;
          border: 1px solid #d9d9d9;
          border-radius: 4px;
          font-size: 14px;
          color: #333;
        }
        ::v-deep .el-date-editor .el-input__icon {
          line-height: 24px;
        }
        ::v-deep .el-date-editor .el-range-separator {
          line-height: 24px;
        }
      }
    }
    .table_content {
      width: 100%;
      height: calc(100% - 168px);
      padding: 0 24px 16px 24px;
      .table-box {
        height: 100%;
      }
    }
  }
}
::v-deep .el-button--medium {
  height: 2rem;
  line-height: 2rem;
  padding: 0 0.9375rem;
}
::v-deep .el-button--text {
  padding: 0;
}
</style>
