<!--
 * @Author: zg
 * @Date: 2022-03-23 16:17:48
 * @LastEditTime: 2022-03-23 17:13:45
 * @LastEditors: zg
 * @Description: 数据考核
 * @FilePath: \web-data-center\src\views\dataManage\resourceDirectory\resourceDirectory.vue
-->
<template>
  <div class="module_content">
    <div class="mc_left">
      <div class="left_tit">
        <p>数据源</p>
      </div>
      <div class="left_main">
        <div class="left_search">
          <el-input
            v-model="searchDataSource"
            placeholder="请输入关键字"
            @change="getDataSourceListData"
          />
        </div>
        <div class="left_tree">
          <ul>
            <li
              v-for="(item, index) in dataSourceList"
              :key="item.id"
              :class="activeIndex === index ? 'active' : ''"
              @click="dataSourceClick(item.id, index)"
            >
              <span class="name">
                {{ item.dataBaseName }}
              </span>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <div class="mc_right">
      <primary-table
        title="数据表列表"
        searchText="名称"
        @searchAction="searchAction"
        :hideAdd="true"
      >
        <!-- 操作按钮 -->
        <template slot="operateBtn">
          <span class="_btn" @click="updateDepart()">批量添加所属部门</span>
          <span class="_btn" @click="updateFrequency()">批量更新频次</span>
        </template>
        <div ref="tableBox" class="table-box">
          <!-- 表格 -->
          <table-page
            ref="tablePage"
            :manualLoad="true"
            :url="tableUrl"
            :columns="columns"
            :query-params="queryParams"
            :checkBox="true"
            :pageIngType="false"
            :height="tableHeight"
            @table-select-val="selectVal"
          >
            <template slot-scope="{ index }" slot="order">{{
              index + 1
            }}</template>
            <template slot-scope="{ row }" slot="action">
              <el-button
                v-if="row.deptList && row.deptList.length > 0"
                @click="deleteDepart(row)"
                type="text"
                >删除所属部门</el-button
              >
              <el-button
                v-if="row.dataAssessFrequency"
                @click="deleteFrequency(row)"
                type="text"
                style="margin-left: 0"
                >删除更新频次</el-button
              >
            </template>
          </table-page>
        </div>
      </primary-table>
    </div>

    <!-- 更新频次表单 -->
    <el-dialog
      title="更新频次"
      :visible.sync="frequencyDialogShow"
      :close-on-click-modal="false"
      @close="closeFrequencyDialog"
    >
      <FrequencyForm
        ref="frequencyForm"
        :selectRows="selectTableData"
        :dataSourceId="dataSourceId"
        @close="closeFrequencyDialog"
        @closeDialogAndRefresh="closeFrequencyDialogAndRefresh"
      ></FrequencyForm>
    </el-dialog>

    <!-- 所属部门表单 -->
    <el-dialog
      title="所属部门"
      :visible.sync="departDialogShow"
      :close-on-click-modal="false"
      @close="closeDepartDialog"
    >
      <DepartForm
        ref="departForm"
        :selectRows="selectTableData"
        :dataSourceId="dataSourceId"
        @close="closeDepartDialog"
        @closeDialogAndRefresh="closeDepartDialogAndRefresh"
      ></DepartForm>
    </el-dialog>
  </div>
</template>

<script>
import PrimaryTable from "_c/primary_table/index";
import { getDataSourceList } from "_a/dataManage/dataSheetManage/dataSheetManage";
import {
  deleteFrequency,
  deleteDataAccessDept,
} from "_a/dataManage/dataAssessment/dataAssessment";
import { ConfirmCustom, MessageError, MessageSuccess } from "_l/message";
import TablePage from "_c/CommonForm/table-page";
import FrequencyForm from "./components/frequencyForm";
import DepartForm from "./components/departForm";
export default {
  name: "dataAssessment",
  components: {
    PrimaryTable,
    TablePage,
    FrequencyForm,
    DepartForm,
  },
  data() {
    return {
      tableUrl: "/data-manage/dataAssessDept/list",
      columns: [
        { label: "序号", slot: "order", width: 50 },
        { label: "表名称", prop: "tableName" },
        { label: "表别名", prop: "customTableName" },
        { label: "所属部门", prop: "deptList", formatter: this.formatDept },
        {
          label: "更新频次",
          prop: "updateFrequency",
          formatter: this.formatFrequency,
        },
        { label: "操作", slot: "action", width: 160 },
      ],
      queryParams: {},
      tableHeight: 0,

      updateFrequencyArr: ["天", "月", "季度", "年"],

      activeIndex: 0,
      dataSourceId: "",

      searchDataSource: "",
      dataSourceList: [],
      selectTableData: [],
      frequencyDialogShow: false,
      departDialogShow: false,
    };
  },
  watch: {},
  methods: {
    // 获取左侧数据源数据
    getDataSourceListData() {
      getDataSourceList({
        page: false,
      }).then((res) => {
        if (res.data) {
          this.dataSourceList = res.data.records;
          if (this.dataSourceList.length > 0) {
            this.dataSourceId = this.dataSourceList[0].id;
            this.queryParams.dataSourceId = this.dataSourceId;
            this.activeIndex = 0;
            this.refresh();
          }
        }
      });
    },

    searchAction(name) {
      this.queryParams.tableName = name;
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

    // 左侧目录树点击查询目录资源列表
    dataSourceClick(id, index) {
      this.dataSourceId = id;
      this.queryParams.dataSourceId = this.dataSourceId;
      this.activeIndex = index;
      this.refresh();
    },

    /**
     * 处理表格更新频次数据
     */
    formatFrequency(row, column, cellValue) {
      if (row.dataAssessFrequency && row.dataAssessFrequency.frequencyUnit) {
        return (
          "每" +
          this.updateFrequencyArr[row.dataAssessFrequency.frequencyUnit - 1]
        );
      } else {
        return "";
      }
    },

    /**
     * 处理表格所属部门数据
     */
    formatDept(row, column, cellValue) {
      let deptText = "";
      if (row.deptList && row.deptList.length > 0) {
        let deptArr = [];
        row.deptList.map((item) => {
          deptArr.push(item.deptName);
        });
        deptText = deptArr.join(",");
      }
      return deptText;
    },

    /**
     * 点击每一行的checkbox
     */
    selectVal(data) {
      this.selectTableData = data;
    },

    /**
     * 打开数据表所属部门弹窗
     */
    updateDepart() {
      if (this.selectTableData.length == 0) {
        this.$message({ message: "请至少选择一条数据！", type: "error" });
      } else {
        this.departDialogShow = true;
        this.$nextTick(() => {
          this.$refs.departForm.reset();
        });
      }
    },
    closeDepartDialog() {
      this.$refs.departForm.reset();
      this.departDialogShow = false;
    },
    /**
     * 关闭数据表所属部门弹出框和刷新
     */
    closeDepartDialogAndRefresh() {
      this.closeDepartDialog();
      this.refresh();
    },

    /**
     * 删除数据表所属部门
     */
    deleteDepart(data) {
      let idList = [];
      if (data.deptList && data.deptList.length > 0) {
        data.deptList.map((item) => {
          idList.push(item.id);
        });
      } else {
        return false;
      }
      ConfirmCustom({ type: "warning" }).then(() => {
        deleteDataAccessDept(idList).then((res) => {
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
     * 打开数据表更新频次弹窗
     */
    updateFrequency() {
      if (this.selectTableData.length == 0) {
        this.$message({ message: "请至少选择一条数据！", type: "error" });
      } else {
        this.frequencyDialogShow = true;
        this.$nextTick(() => {
          this.$refs.frequencyForm.reset();
        });
      }
    },
    closeFrequencyDialog() {
      this.$refs.frequencyForm.reset();
      this.frequencyDialogShow = false;
    },
    /**
     * 关闭数据表更新频次弹出框和刷新
     */
    closeFrequencyDialogAndRefresh() {
      this.closeFrequencyDialog();
      this.refresh();
    },

    /**
     * 删除数据表更新频次
     */
    deleteFrequency(data) {
      let idList = [];
      if (data.dataAssessFrequency && data.dataAssessFrequency.id) {
        idList.push(data.dataAssessFrequency.id);
      } else {
        return false;
      }
      ConfirmCustom({ type: "warning" }).then(() => {
        deleteFrequency(idList).then((res) => {
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
  },
  mounted() {
    this.getDataSourceListData();
    this.$nextTick(() => {
      this.tableHeight = this.$refs.tableBox.offsetHeight;
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
  .mc_left {
    width: 260px;
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
    .left_tit {
      width: 100%;
      height: 56px;
      padding: 16px 10px;
      @include themeify {
        border-bottom: 1px solid themed("border_color1");
      }
      p {
        font-size: 18px;
        font-weight: 600;
        @include themeify {
          color: themed("font_color2");
        }
        line-height: 20px;
        padding-left: 10px;
        @include themeify {
          border-left: 4px solid themed("border_color2");
        }
      }
    }
    .left_main {
      width: 100%;
      height: calc(100% - 56px);
      padding: 16px 10px;
      .left_tree {
        width: 100%;
        height: calc(100% - 46px);
        margin-top: 10px;
        overflow-y: auto;
        ul {
          li {
            width: 100%;
            display: flex;
            align-items: center;
            height: 32px;
            padding: 0 16px;
            border-radius: 4px;
            cursor: pointer;
            span.name {
              display: inline-block;
              width: 100%;
              padding-right: 10px;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
            &.active {
              // @include themeify {
              //   color: themed("font_color1");
              //   background-color: themed("background_color3");
              // }
              background-color: #edf6ff;
            }
          }
        }
      }
    }
  }
  .mc_right {
    width: calc(100% - 276px);
    height: 100%;
    margin-left: 16px;
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

.tree_btn {
  i {
    display: block;
    font-size: 14px;
    font-style: normal;
    @include font_color("font_color2");
    line-height: 28px;
    text-align: center;
    cursor: pointer;
  }
  i:hover {
    @include themeify {
      background-color: themed("background_color4");
    }
  }
}
.columnDetailsDialog {
  ::v-deep .el-dialog__body {
    padding: 0;
  }
}
</style>
<style lang="scss">
.el-popover {
  min-width: 88px !important;
  padding: 12px 0;
}
</style>
