<template>
  <div class="wrap">
    <div class="content">
      <div class="item_box_title">
        <p>菜单管理</p>
      </div>
      <div class="menu-list-box">
        <div>
          <div class="menu-title">
            <h3>系统菜单</h3>
            <el-button type="primary" size="small" @click="addFirstMenu"
              >添加</el-button
            >
          </div>
          <el-tree
            accordion
            ref="tree"
            :data="unitList"
            :props="defaultProps"
            node-key="value"
            @node-click="handleNodeClick"
            highlight-current
          >
          </el-tree>
        </div>
        <div>
          <div class="menu-title">
            <h3>当前菜单目录</h3>
            <el-button type="primary" size="small" @click="addTable"
              >添加</el-button
            >
          </div>
          <div class="tb_wrapper">
            <el-table :data="tableData" stripe border style="width: 100%">
              <el-table-column
                type="index"
                align="center"
                width="50"
                label="序号"
              ></el-table-column>
              <el-table-column
                prop="resName"
                align="center"
                label="资源名称"
              ></el-table-column>
              <el-table-column align="center" label="路径" width="380">
                <template slot-scope="scope">
                  <div>{{ scope.row.resUrl ? scope.row.resUrl : "-" }}</div>
                </template>
              </el-table-column>
              <el-table-column align="center" label="资源类型">
                <template slot-scope="scope">
                  <div v-if="scope.row.resType == 1">目录</div>
                  <div v-if="scope.row.resType == 2">菜单</div>
                  <div v-if="scope.row.resType == 3">功能</div>
                </template>
              </el-table-column>
              <el-table-column
                prop="name"
                align="center"
                label="标识"
                width="180"
              ></el-table-column>
              <el-table-column
                prop="sort"
                align="center"
                label="排序"
              ></el-table-column>
              <el-table-column prop="remark" align="center" label="备注">
                <template slot-scope="scope">
                  <div>{{ scope.row.remark ? scope.row.remark : "-" }}</div>
                </template>
              </el-table-column>
              <el-table-column align="center" label="操作" width="130">
                <template slot-scope="scope">
                  <div class="deal_box">
                    <span
                      class="deal_btn deal_btn_edit"
                      @click="editTable(scope.row.id)"
                      >编辑</span
                    >
                    <span
                      class="deal_btn deal_btn_del"
                      @click="delTable(scope.row.id)"
                      >删除</span
                    >
                  </div>
                </template>
              </el-table-column>
            </el-table>
            <pagination
              :total="pageTotal"
              :page.sync="pageNum"
              :limit.sync="pageSize"
              :page-sizes="[10, 20, 50, 100]"
            />
          </div>
        </div>
      </div>
    </div>
    <el-dialog
      :title="title"
      :visible.sync="showEditDialog"
      :close-on-click-modal="false"
    >
      <menu-form
        v-if="showEditDialog"
        :id="editId"
        :pId="pId"
        :flag="flag"
        @closeEditDialog="closeEditDialog"
      ></menu-form>
    </el-dialog>
  </div>
</template>

<script>
import {
  sysResTreeApi,
  sysResListApi,
  sysResDelApi,
} from "@/api/systemManagement.js";
import MenuForm from "./components/MenuForm";
export default {
  data() {
    return {
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      unitList: [],
      defaultProps: {
        children: "children",
        label: "label",
      },
      showEditDialog: false,
      editId: "",
      pId: 0,
      title: "",
      flag: 1,
    };
  },
  components: { MenuForm },
  mounted() {
    this.sysResTree();
    this.sysResList();
  },
  methods: {
    handleNodeClick(data) {
      if (this.pId !== data.value) {
        this.pageNum = 1;
        this.pId = data.value;
        this.sysResList();
      }
    },
    addFirstMenu() {
      this.title = "新增一级菜单";
      this.editId = "";
      this.pId = 0;
      this.showEditDialog = true;
    },
    addTable() {
      if (!this.pId) {
        this.$message.warning("请先选择左侧菜单");
        return false;
      }
      this.title = "新增";
      this.editId = "";
      this.showEditDialog = true;
    },
    editTable(id) {
      this.title = "编辑";
      this.editId = id;
      this.showEditDialog = true;
    },
    async sysResTree() {
      let res = await sysResTreeApi();
      if (res.code == 0) {
        let arr = [];
        if (res.data.wbTree.length) {
          this.setTreeList(res.data.wbTree, arr);
        }
        this.unitList = arr;
        if (this.pId) {
          this.$nextTick(() => {
            this.$refs.tree.setCurrentKey(this.pId);
          });
        }
      }
    },
    setTreeList(list, arr) {
      list.map((item) => {
        let obj = {
          value: item.sysRes.id,
          label: item.sysRes.resName,
          children: [],
        };
        arr.push(obj);
        if (item.subset && item.subset.length) {
          this.setTreeList(item.subset, obj.children);
        }
      });
    },
    closeEditDialog(type) {
      if (type) {
        this.sysResTree();
        this.sysResList();
      }
      this.showEditDialog = false;
    },
    async sysResList() {
      let res = await sysResListApi({
        pId: this.pId,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      });
      if (res.code == 0) {
        this.pageTotal = res.data.totalRows;
        this.tableData = res.data.list;
      }
    },
    delTable(delid) {
      if (!delid) {
        this.$message.error("数据id不存在！");
      } else {
        this.$confirm("此操作将删除该条数据, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }).then(() => {
          sysResDelApi({
            id: delid,
          }).then((response) => {
            if (response.code == 0) {
              this.sysResTree();
              this.sysResList();
              this.$message({
                message: "删除成功！",
                type: "success",
                duration: "1000",
              });
            } else {
              this.$message.error("删除失败！");
            }
          });
        });
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  width: 100%;
  height: 100%;
  padding: 25px;
  box-sizing: border-box;
  .content {
    @include contentBox();
    height: 100%;
    box-sizing: border-box;
    .menu-title {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-bottom: 16px;
      h3 {
        font-weight: 600;
      }
    }
  }
}
.menu-list-box {
  display: flex;
  justify-content: space-between;
  height: calc(100% - 56px);
  > div:nth-child(1) {
    width: 400px;
    padding: 16px;
    height: 100%;
    border-right: 1px solid #eee;
    box-sizing: border-box;
  }
  > div:nth-child(2) {
    width: calc(100% - 420px);
    padding: 16px;
    .tb_wrapper {
      padding: 0;
    }
  }
}
</style>

<style lang="scss">
.menu-list-box {
  .el-tree {
    .el-tree-node {
      .el-tree-node__content {
        height: 30px;
        &:hover {
          background-color: #edf6fe;
          color: #008dfe;
        }
      }
      &.is-current {
        > .el-tree-node__content {
          background-color: #edf6fe;
          color: #008dfe;
          position: relative;
          &::after {
            content: "";
            position: absolute;
            left: 0;
            top: 0;
            width: 2px;
            height: 100%;
            background-color: #008dfe;
          }
        }
      }
    }
  }
}
</style>