<template>
  <div class="wrap">
    <div class="card">
      <div class="item_box_title">
        <p>数据维护</p>
      </div>
      <el-row type="flex" class="container">
        <el-col :span="4" class="container-left">
          <div class="tree-content">
            <el-input
              v-model="filterTree"
              placeholder="请输入"
              suffix-icon="el-icon-search"
            />
            <el-tree
              class="tree-box"
              ref="tree"
              node-key="id"
              :expand-on-click-node="false"
              check-on-click-node
              :props="{ children: 'list', label: 'directoryName' }"
              :data="treeData"
              :filter-node-method="filterNode"
              @node-click="onTreeNodeClick"
              :default-expanded-keys="['1']"
              current-node-key="1"
            >
              <template #default="{ node, data }">
                <div class="custom-el-tree">
                  <div class="label-box" :title="node.label">
                    {{ node.label }}
                  </div>
                  <el-dropdown size="small" @command="onTreeCommand">
                    <el-button circle size="mini" icon="el-icon-more" />
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item :command="{ data, type: '新增同级' }"
                        >新增同级</el-dropdown-item
                      >
                      <el-dropdown-item :command="{ data, type: '新增下级' }"
                        >新增下级</el-dropdown-item
                      >
                      <el-dropdown-item :command="{ data, type: '修改' }"
                        >修改</el-dropdown-item
                      >
                    </el-dropdown-menu>
                  </el-dropdown>
                </div>
              </template>
            </el-tree>
          </div>
        </el-col>
        <el-col :span="20">
          <div type="flex" class="table-header">
            <el-form inline :show-message="false" @submit.native.prevent="">
              <el-form-item label="名称">
                <el-input
                  v-model="dataItem"
                  placeholder="请输入"
                  suffix-icon="el-icon-search"
                  @change="getList"
                />
              </el-form-item>
            </el-form>
            <div>
              <el-button @click="exportData">导出</el-button>
              <el-button
                v-if="
                  editData.id != '5e753258ab6be1b500e586a84b59c8a1' &&
                  editData.pid != '5e753258ab6be1b500e586a84b59c8a1'
                "
                type="primary"
                @click="openDialog('新增')"
                >新增</el-button
              >
            </div>
          </div>
          <div class="table-wrapper tb_wrapper">
            <el-table :data="tableData" height="100%" border>
              <el-table-column
                type="index"
                align="center"
                label="序号"
                width="55"
              />
              <el-table-column
                label="目录名称"
                prop="directoryName"
                align="center"
              ></el-table-column>
              <el-table-column
                label="数据项"
                prop="dataItem"
                align="center"
              ></el-table-column>
              <el-table-column
                label="详情描述"
                prop="description"
                align="center"
              ></el-table-column>
              <el-table-column
                label="当前数据来源"
                align="center"
                prop="source"
              ></el-table-column>
              <el-table-column
                label="行政负责人-主管部门（产权人）责任人"
                align="center"
              >
                <el-table-column
                  label="单位名称"
                  prop="competentPeople.unitName"
                  align="center"
                ></el-table-column>
                <el-table-column
                  label="负责人"
                  align="center"
                  prop="competentPeople.unitDirector"
                ></el-table-column>
                <el-table-column
                  label="职务"
                  align="center"
                  prop="competentPeople.unitJob"
                ></el-table-column>
                <el-table-column
                  label="联系方式"
                  align="center"
                  prop="competentPeople.unitDirectorPhone"
                ></el-table-column>
              </el-table-column>
              <el-table-column label="物业单位管理负责人" align="center">
                <el-table-column
                  label="单位名称"
                  align="center"
                  prop="propertyPeople.unitName"
                ></el-table-column>
                <el-table-column
                  label="负责人"
                  align="center"
                  prop="propertyPeople.unitDirector"
                ></el-table-column>
                <el-table-column
                  label="职务"
                  align="center"
                  prop="propertyPeople.unitJob"
                ></el-table-column>
                <el-table-column
                  label="联系方式"
                  align="center"
                  prop="propertyPeople.unitDirectorPhone"
                ></el-table-column>
              </el-table-column>
              <el-table-column label="操作" width="160" align="center">
                <template #default="{ row }">
                  <div class="deal_box">
                    <span
                      class="deal_btn deal_btn_look"
                      @click="openDialog('编辑', row)"
                      >编辑</span
                    >
                    <span class="deal_btn deal_btn_look" @click="sendInfo(row)"
                      >发送</span
                    >
                    <span
                      class="deal_btn deal_btn_del"
                      @click="onDeleteClick(row.id)"
                      >删除</span
                    >
                  </div>
                </template>
              </el-table-column>
            </el-table>
            <pagination
              :total="total"
              :page.sync="pageNum"
              :limit.sync="pageSize"
              :page-sizes="[10, 20, 50, 100]"
              @pagination="getList"
            />
          </div>
        </el-col>
      </el-row>
    </div>
    <DataDialog
      ref="dataDialog"
      v-if="visible"
      :visible.sync="visible"
      :title="title"
      @change="getList"
    />
    <el-dialog
      width="310px"
      :visible.sync="nodeDialogVisible"
      :title="nodeDialogTitle"
      append-to-body
    >
      <el-form inline @submit.native.prevent="">
        <el-form-item required label="名称">
          <el-input v-model="directoryName"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="nodeDialogVisible = false"
          >取 消</el-button
        >
        <el-button
          size="small"
          :loading="nodeDialogLoading"
          type="primary"
          @click="onTreeSubmit"
          >确 定</el-button
        >
      </div>
    </el-dialog>
    <SendInfoDialog
      v-if="sendDialogVisible"
      ref="sendDialog"
      :visible.sync="sendDialogVisible"
    />
  </div>
</template>
<script>
import {
  queryDirectory,
  insertDirectory,
  updateDirectory,
  queryInfo,
  deleteInfo,
} from "@/api/dataMaintenance";
import DataDialog from "./components/DataDialog.vue";
import SendInfoDialog from "./components/SendInfoDialog.vue";
import { exportMaintainInfo } from "../../../utils/export";
export default {
  name: "DataMaintenance",
  components: { DataDialog, SendInfoDialog },
  data() {
    return {
      treeData: [],
      dataItem: "",
      filterTree: "",
      tableData: [],
      pageSize: 10,
      pageNum: 1,
      total: 0,
      visible: false,
      title: "新增",
      editData: {},
      directoryName: "",
      nodeDialogVisible: false,
      nodeDialogTitle: "",
      nodeDialogLoading: false,

      currentNodeData: {},
      currentSelectNode: {},

      sendDialogVisible: false,

      mergeObj: {},
    };
  },
  watch: {
    filterTree(val) {
      console.log(this.$refs.tree, "this.$refs.treethis.$refs.tree");
      this.$refs.tree.filter(val);
    },
  },
  mounted() {
    this.getTree();
    this.getList();
  },
  methods: {
    sendInfo(data) {
      this.sendDialogVisible = true;
      this.$nextTick(() => {
        const { directoryName, dataItem, description, source, ...rest } = data;
        Object.assign(this.$refs.sendDialog.form, {
          directoryName,
          dataItem,
          description,
          source,
          content: "",
        });
        if (data.thirdPartyUnitInfos && data.thirdPartyUnitInfos.length > 0) {
          data.thirdPartyUnitInfos.forEach((item, index) => {
            this.$refs.sendDialog.thirdPartyUnitChecked.push(index);
          });
        }
        if (data.transManageDataType == 1) {
          this.$refs.sendDialog.detail = {
            patrolPeople: data.patrolPeople,
            technologyPeople: data.technologyPeople,
            propertyPeople: data.propertyPeople,
            competentPeople: data.competentPeople,
          };
          let checked = ["competent", "property", "technology", "patrol"];
          this.$refs.sendDialog.checked = checked;
        }
        this.$refs.sendDialog.data = rest;
      });
    },
    onTreeNodeClick(data, node, el) {
      this.editData = data;
      this.currentSelectNode = data;
      this.getList();
    },
    onTreeCommand(payload) {
      this.nodeDialogVisible = true;
      this.currentNodeData = payload;
      this.nodeDialogTitle = payload.type;
      if (payload.type === "修改") {
        this.directoryName = payload.data.directoryName;
      } else {
        this.directoryName = "";
      }
    },
    async onTreeSubmit() {
      let res;
      const { type, data } = this.currentNodeData;
      this.nodeDialogLoading = true;
      if (type === "修改") {
        res = await updateDirectory({
          id: data.id,
          directoryName: this.directoryName,
        });
      } else {
        const pid = type === "新增下级" ? data.id : data.pid;
        res = await insertDirectory({ pid, directoryName: this.directoryName });
      }

      this.nodeDialogLoading = false;
      if (res.code === 0) {
        this.$message.success("操作成功!");
        this.nodeDialogVisible = false;
        this.getTree();
      }
    },
    async getTree() {
      try {
        const res = await queryDirectory();
        this.treeData = [res.data];
      } catch (e) {
        // deal by axios interceptors
      }
    },
    async getList() {
      try {
        const res = await queryInfo({
          directoryId: this.currentSelectNode.id,
          pageSize: this.pageSize,
          pageNum: this.pageNum,
          dataItem: this.dataItem,
        });
        if (res.code === 0) {
          this.total = res.data.totalRows;
          this.tableData = res.data.list;
        }
      } catch (e) {}
    },
    onDeleteClick(id) {
      this.$confirm("此操作将永久删除该条数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        deleteInfo({ id }).then((res) => {
          if (res.code === 0) {
            this.$message.success("删除成功!");
            this.getList();
          }
        });
      });
    },
    openDialog(type, payload) {
      this.title = type;
      this.visible = true;
      if (type === "编辑") {
        const {
          id,
          dataItem,
          description,
          directoryId,
          directoryName,
          source,
          unitName,
          unitDirector,
          unitDirectorPhone,
          dataMaintainName,
          dataMaintainPhone,
          thirdPartyUnitInfos,
          transManageDataType,
          ...form
        } = payload;
        this.$nextTick(() => {
          this.$refs.dataDialog.detail = {
            patrolPeople: payload.patrolPeople || {},
            technologyPeople: payload.technologyPeople || {},
            propertyPeople: payload.propertyPeople || {},
            competentPeople: payload.competentPeople || {},
          };
          console.log(
            transManageDataType,
            " transManageDataTypetransManageDataTypetransManageDataTypetransManageDataType"
          );
          this.$refs.dataDialog.form = {
            dataItem,
            description,
            directoryId,
            directoryName,
            source,
            unitName,
            unitDirector,
            unitDirectorPhone,
            dataMaintainName,
            dataMaintainPhone,
          };
          this.$refs.dataDialog.transManageDataType = transManageDataType;
          this.$refs.dataDialog.unitInfos = thirdPartyUnitInfos ?? [
            {
              unitDirector: "",
              unitDirectorPhone: "",
              unitName: "",
              unitJob: "",
            },
          ];
          this.$refs.dataDialog.id = id;
        });
      } else {
        const { id, directoryName } = this.currentSelectNode;
        this.$nextTick(() => {
          this.$refs.dataDialog.id = "";
          this.$refs.dataDialog.form.directoryId = id ?? "1";
          this.$refs.dataDialog.form.directoryName =
            directoryName ?? "鹿城区水利全域数字化";
        });
      }
    },
    // 合并第三方责任单位单元格
    spanMethod({ row, column, rowIndex, columnIndex }) {
      return { rowspan: 0, colspan: 0 };
    },
    filterNode(value, data) {
      if (!value) return true;
      console.log(data, "dasdsdadsadsad");
      return data.directoryName.indexOf(value) !== -1;
    },
    exportData() {
      let obj = {
        directoryId: this.currentSelectNode.id,
        // dataItem: this.dataItem,
      };
      exportMaintainInfo(obj)
    },
  },
};
</script>
<style lang="scss" scoped>
.container {
  height: calc(100% - 60px);
  .container-left {
    height: 100%;
  }
  .tree-content {
    padding: 16px;
    overflow: auto;
    height: 100%;
    box-sizing: border-box;
    .tree-box {
      padding-top: 16px;
    }
  }
  .table-header {
    padding: 16px 16px 0 16px;
    display: flex;
    justify-content: space-between;
  }
  .table-wrapper {
    height: calc(100% - 150px);
    .button {
      cursor: pointer;
      &:hover {
        color: #111;
      }
      &.danger {
        color: #ee2233;
        &:hover {
          color: #ee0000;
        }
      }
    }
  }
}
.custom-el-tree {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  .label-box {
    max-width: 150px;
    /*1、先强制一行显示文本*/
    white-space: nowrap;
    /*2、超出的部分隐藏*/
    overflow: hidden;
    /*3、文字用省略号替代超出的部分*/
    text-overflow: ellipsis;
  }
  .el-dropdown {
    transform: scale(0.8);
  }
}
.wrap {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  .card {
    @include contentBox();
    height: 100%;
    box-sizing: border-box;
  }
}
</style>
