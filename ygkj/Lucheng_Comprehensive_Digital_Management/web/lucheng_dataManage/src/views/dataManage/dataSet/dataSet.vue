<!--
 * @Author: zg
 * @Date: 2022-03-23 16:17:48
 * @LastEditTime: 2022-03-23 17:13:45
 * @LastEditors: zg
 * @Description: 数据集
 * @FilePath: \web-data-center\src\views\dataManage\resourceDirectory\resourceDirectory.vue
-->
<template>
  <div class="module_content">
    <div class="mc_main">
      <primary-table
        title="数据集"
        searchText="名称"
        @searchAction="searchAction"
        :hideAdd="true"
      >
        <!-- 操作按钮 -->
        <template slot="operateBtn">
          <el-dropdown @command="handleCommand">
            <el-button type="primary">
              新 增<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item :command="1">sql</el-dropdown-item>
              <el-dropdown-item :command="2">http接口</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <!--  -->
        </template>
        <div ref="tableBox" class="table-box">
          <!-- 表格 -->
          <table-page
            ref="tablePage"
            :url="tableUrl"
            :columns="columns"
            :query-params="queryParams"
            :checkBox="false"
            :height="tableHeight"
          >
            <template slot-scope="{ index }" slot="order">{{
              index + 1
            }}</template>
            <template slot-scope="{ row }" slot="dataSetType">
              <span v-if="row.dataSetType == 1">sql</span>
              <span v-else-if="row.dataSetType == 2">http接口</span>
            </template>
            <template slot-scope="{ row }" slot="action">
              <el-button @click="columnDetails(row)" type="text"
                >修改</el-button
              >
              <el-button @click="delDataSet(row)" type="text">删除</el-button>
            </template>
          </table-page>
        </div>
      </primary-table>
    </div>

    <!-- 添加目录树表单 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogShow"
      :close-on-click-modal="false"
      width="1280px"
      top="10vh"
      @close="closeDialog"
    >
      <TabForm
        ref="tabForm"
        :dialogTitle="dialogTitle"
        :dialogShow="dialogShow"
        :setType="setType"
        :setId="setId"
        @close="closeDialog"
        @closeDialogAndRefresh="closeDialogRefresh"
      ></TabForm>
    </el-dialog>
  </div>
</template>

<script>
import PrimaryTable from "_c/primary_table/index";
import { deleteDataSetById } from "_a/dataManage/dataSet/dataSet";
import { ConfirmCustom, MessageError, MessageSuccess } from "_l/message";
import TablePage from "_c/CommonForm/table-page";
import TabForm from "./components/tabForm";

export default {
  name: "dataSet",
  components: {
    PrimaryTable,
    TablePage,
    TabForm,
  },
  data() {
    return {
      tableUrl: "/data-manage/dataSet/page",
      columns: [
        { label: "序号", slot: "order", width: 50 },
        { label: "数据集名称", prop: "dataSetName" },
        { label: "数据集类型", slot: "dataSetType" },
        { label: "创建id", prop: "createId" },
        { label: "创建时间", prop: "createTime" },
        { label: "操作", slot: "action", width: 180 },
      ],
      queryParams: {},
      tableHeight: 0,
      form: {},
      dialogShow: false,
      dialogTitle: "新增",
      setType: null,
      setId: "",
    };
  },
  watch: {},
  methods: {
    searchAction(name) {
      this.queryParams.dataSetName = name;
      this.refresh();
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

    // 新增
    handleCommand(command) {
      this.setId = "";
      this.setType = command;
      this.dialogTitle = "新增数据集";
      this.dialogShow = true;
    },
    // 数据集修改
    columnDetails(data) {
      this.setId = data.id;
      this.setType = data.dataSetType;
      this.dialogTitle = "编辑数据集";
      this.dialogShow = true;
      // this.$nextTick(() => {
      //   this.$refs.tabForm.getDetail();
      // });
    },
    // 数据集删除
    delDataSet(data) {
      ConfirmCustom({ type: "warning" }).then(() => {
        deleteDataSetById(data.id).then((res) => {
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
     * 关闭数据集弹框
     */
    closeDialog() {
      // this.$refs.form.reset();
      this.dialogShow = false;
      this.$refs.tabForm.reset();
    },
    /**
     * 关闭数据集弹出框和刷新
     */
    closeDialogRefresh() {
      this.closeDialog();
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
    .wrap {
      width: 100%;
      height: 100%;
      ::v-deep .table_content {
        width: 100%;
        height: calc(100% - 120px);
        padding-bottom: 16px;
        .table-box {
          height: 100%;
        }
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
