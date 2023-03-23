<!--
 * @Author: zg
 * @Date: 2022-03-23 16:17:48
 * @LastEditTime: 2022-03-23 17:13:45
 * @LastEditors: zg
 * @Description: 资源目录
 * @FilePath: \web-data-center\src\views\dataManage\resourceDirectory\resourceDirectory.vue
-->
<template>
  <div class="module_content">
    <div class="mc_left">
      <div class="left_tit">
        <p>目录树</p>
      </div>
      <div class="left_main">
        <div class="left_add">
          <span @click="append(null)">新增</span>
        </div>
        <div class="left_search">
          <el-input v-model="filterText" placeholder="请输入关键字" />
        </div>
        <div class="left_tree">
          <el-tree
            ref="directoryTree"
            :data="treeData"
            :props="defaultProps"
            @node-click="handleNodeClick"
            node-key="id"
            default-expand-all
            :expand-on-click-node="false"
            :highlight-current="true"
            :filter-node-method="filterNode"
          >
            <span class="custom-tree-node" slot-scope="{ node, data }">
              <span class="name">
                <!-- <i class="el-icon-folder-opened"></i> -->
                {{ node.label }}
              </span>
              <el-popover placement="bottom" trigger="hover" class="myPopover">
                <span class="tree_btn">
                  <i @click="append(data.id)">新增</i>
                  <i @click="depotEdit(data)">修改</i>
                  <i @click="remove(data)">删除</i>
                </span>
                <span slot="reference" class="icon-tree-btn"></span>
              </el-popover>
            </span>
          </el-tree>
        </div>
      </div>
    </div>
    <div class="mc_right">
      <primary-table
        title="资源目录列表"
        searchText="名称"
        @searchAction="searchAction"
        :hideAdd="true"
      >
        <div ref="tableBox" class="table-box">
          <!-- 表格 -->
          <table-page
            ref="tablePage"
            :manualLoad="true"
            :url="tableUrl"
            :columns="columns"
            :query-params="queryParams"
            :checkBox="false"
            :pageIngType="false"
            :height="tableHeight"
          >
            <template slot-scope="{ index }" slot="order">{{
              index + 1
            }}</template>
          </table-page>
        </div>
      </primary-table>
    </div>

    <!-- 添加目录树表单 -->
    <el-dialog
      :title="directoryDialogTitle"
      :visible.sync="directoryDialogShow"
      :close-on-click-modal="false"
      @close="closeDirectoryDialog"
    >
      <DirectoryForm
        ref="directoryForm"
        :dialogTitle="directoryDialogTitle"
        @close="closeDirectoryDialog"
        @closeDialogAndRefresh="closeDirectoryDialogRefresh"
      ></DirectoryForm>
    </el-dialog>
  </div>
</template>

<script>
import PrimaryTable from "_c/primary_table/index";
import {
  getDirectoryTreeTableList,
  deleteDirectoryById,
} from "_a/dataManage/resourceDirectory/resourceDirectory";
import checkPermission from "_l/permission";
import { ConfirmCustom, MessageError, MessageSuccess } from "_l/message";
import TablePage from "_c/CommonForm/table-page";
import DirectoryForm from "./components/directoryForm";

export default {
  name: "resourceDirectory",
  components: { PrimaryTable, TablePage, DirectoryForm },
  data() {
    return {
      tableUrl: "/data-manage/resourceSourceRel/page",
      columns: [
        { label: "序号", slot: "order", width: 50 },
        { label: "表名称", prop: "tableName" },
        { label: "表别名", prop: "customTableName" },
        { label: "资源目录名称", prop: "resourceName" },
        { label: "注释信息", prop: "remarks" },
      ],
      dataSourceAdd: false,
      form: {},
      queryParams: {},
      tableHeight: 0,

      selectDirectoryId: "",
      directoryDialogShow: false,
      directoryDialogTitle: "新增",
      filterText: "",
      treeData: [],
      defaultProps: {
        children: "subList",
        label: "resourceName",
      },
    };
  },
  watch: {
    filterText(val) {
      this.$refs.directoryTree.filter(val);
    },
  },
  methods: {
    // 获取左侧目录树数据
    getDirectoryTreeList() {
      getDirectoryTreeTableList(true).then((res) => {
        if (res.data) {
          this.treeData = res.data;
          if (this.treeData.length > 0) {
            this.selectDirectoryId = this.treeData[0].id;
            this.queryParams.dataResourceId = this.selectDirectoryId;
            this.$nextTick(() => {
              this.$refs.directoryTree.setCurrentKey(this.selectDirectoryId); //一定要加这个选中了否则样式没有出来
            });
          }
          this.refresh();
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
    handleNodeClick(data) {
      this.selectDirectoryId = data.id;
      this.queryParams.dataResourceId = this.selectDirectoryId;
      this.refresh();
    },
    // 过滤目录节点树
    filterNode(value, data) {
      if (!value) return true;
      return data.resourceName.indexOf(value) !== -1;
    },
    // 左侧目录树新增
    append(id) {
      let form = {
        parentId: id,
      };
      this.directoryDialogTitle = "新增目录树";
      this.directoryDialogShow = true;
      this.$nextTick(() => {
        this.$refs.directoryForm.reset();
        this.$refs.directoryForm.setForm({ ...form });
      });
    },
    // 左侧目录树修改
    depotEdit(data) {
      this.directoryDialogTitle = "编辑目录树";
      this.directoryDialogShow = true;
      this.$nextTick(() => {
        this.$refs.directoryForm.reset();
        this.$refs.directoryForm.setForm({ ...data });
      });
    },
    // 左侧目录树删除
    remove(data) {
      ConfirmCustom({ type: "warning" }).then(() => {
        deleteDirectoryById(data.id).then((res) => {
          if (res.code) {
            MessageSuccess("del");
          } else {
            MessageError("del");
          }
          // 刷新表格
          this.getDirectoryTreeList();
        });
      });
    },
    /**
     * 关闭目录树弹框
     */
    closeDirectoryDialog() {
      this.$refs.directoryForm.reset();
      this.directoryDialogShow = false;
    },
    /**
     * 关闭目录树弹出框和刷新
     */
    closeDirectoryDialogRefresh() {
      this.closeDirectoryDialog();
      this.getDirectoryTreeList();
    },
  },
  mounted() {
    this.dataSourceAdd = checkPermission(["resourceDirectory:save"]);
    this.getDirectoryTreeList();
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
  @include background_color("background_color1");
  // background: #f7f8fa;
  .mc_left {
    width: 260px;
    height: 100%;
    background: #fff;
    box-shadow: 0px 1px 4px 0px rgba(14, 31, 53, 0.06),
      0px 0px 2px 0px rgba(14, 31, 53, 0.12);
    border-radius: 2px;
    float: left;
    .left_tit {
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
    .left_main {
      width: 100%;
      height: calc(100% - 56px);
      padding: 16px 10px;
      .left_add {
        width: 100%;
        span {
          display: inline-block;
          width: 100%;
          height: 32px;
          background: #1492ff;
          color: #ffffff;
          border-radius: 0.25rem;
          text-align: center;
          line-height: 32px;
          cursor: pointer;
          margin-bottom: 16px;
          border: 0.0625rem solid #1492ff;
        }
      }
      .left_tree {
        width: 100%;
        height: calc(100% - 94px);
        margin-top: 10px;
        overflow-y: auto;
        &.noAdd {
          height: calc(100% - 46px);
        }
        ::v-deep .el-tree-node__content {
          height: 32px;
        }
        .custom-tree-node {
          width: calc(100% - 40px);
          margin-right: 16px;

          span.name {
            display: inline-block;
            width: calc(100% - 16px);
            padding-right: 10px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
        }
      }
    }
  }
  .mc_right {
    width: calc(100% - 276px);
    height: 100%;
    margin-left: 16px;
    background: #fff;
    box-shadow: 0px 1px 4px 0px rgba(14, 31, 53, 0.06),
      0px 0px 2px 0px rgba(14, 31, 53, 0.12);
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
    color: #155bd4;
    line-height: 28px;
    text-align: center;
    cursor: pointer;
  }
  i:hover {
    background: #e8effa;
  }
}
</style>
<style lang="scss">
.el-popover {
  min-width: 88px !important;
  padding: 12px 0;
}
</style>
