<!--
 * @Author: zg
 * @Date: 2022-03-23 16:17:48
 * @LastEditTime: 2022-03-23 17:13:45
 * @LastEditors: zg
 * @Description: 日志查询
 * @FilePath: \web-data-center\src\views\dataManage\resourceDirectory\resourceDirectory.vue
-->
<template>
  <div class="module_content">
    <div class="mc_main">
      <primary-table title="日志查询" :hideInput="true" :hideAdd="true">
        <!-- 操作按钮 -->
        <template slot="operateBtn">
          <div class="search_item">
            <span>日志类型：</span>
            <el-select
              v-model="queryParams.logEnum"
              placeholder="请选择日志类型"
              clearable
              @change="refresh()"
            >
              <el-option
                v-for="item in logEnumOption"
                :key="item.value"
                :value="item.value"
                :label="item.label"
              />
            </el-select>
          </div>
          <div class="search_item">
            <span>时间选择：</span>
            <el-date-picker
              v-model="timeRange"
              type="datetimerange"
              :picker-options="pickerOptions"
              value-format="yyyy-MM-dd HH:mm:ss"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              align="right"
              @change="timeSelect"
            >
            </el-date-picker>
          </div>
          <!--  -->
        </template>
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
            <template slot-scope="{ row }" slot="logEnum">
              <span v-if="row.logEnum == 'BUSINESS'">业务日志</span>
              <span v-else-if="row.logEnum == 'LOGIN'">登陆登出日志</span>
            </template>
            <template slot-scope="{ row }" slot="logStatus">
              <span v-if="row.logStatus == '0'">正常</span>
              <span v-else-if="row.logStatus == '1'">异常</span>
            </template>
          </table-page>
        </div>
      </primary-table>
    </div>
  </div>
</template>

<script>
import PrimaryTable from "_c/primary_table/index";
import { deleteDataSetById } from "_a/dataManage/dataSet/dataSet";
import { ConfirmCustom, MessageError, MessageSuccess } from "_l/message";
import TablePage from "_c/CommonForm/table-page";

export default {
  name: "dataSet",
  components: {
    PrimaryTable,
    TablePage,
  },
  data() {
    return {
      methodType: "get",
      tableUrl: "/log-center/operationalLog/page",
      columns: [
        { label: "序号", slot: "order", width: 50 },
        { label: "服务名称", prop: "appName" },
        { label: "业务模块名称", prop: "businessName" },
        { label: "日志类型", slot: "logEnum" },
        { label: "ip地址", prop: "ip" },
        { label: "请求地址", prop: "url" },
        // { label: "方法名", prop: "method" },
        // { label: "请求参数", prop: "operParam" },
        { label: "操作状态", slot: "logStatus" },
        // { label: "返回结果", prop: "result" },
        { label: "错误信息", prop: "error" },
        { label: "操作人员", prop: "operName" },
        { label: "创建时间", prop: "createDate" },
      ],
      queryParams: {},
      tableHeight: 0,

      logEnumOption: [
        {
          label: "业务日志",
          value: "BUSINESS",
        },
        {
          label: "登陆登出日志",
          value: "LOGIN",
        },
      ],
      pickerOptions: {
        shortcuts: [
          {
            text: "最近一周",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近三个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
      },
      timeRange: "",
    };
  },
  watch: {},
  methods: {
    timeSelect() {
      if (this.timeRange && this.timeRange.length > 0) {
        this.queryParams.startTime = this.timeRange[0];
        this.queryParams.endTime = this.timeRange[1];
      } else {
        this.queryParams.startTime = "";
        this.queryParams.endTime = "";
      }
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
