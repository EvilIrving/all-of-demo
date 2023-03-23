<template>
  <div class="wrap">
    <div class="content">
      <div class="item_box_title">
        <p>单位用户管理</p>
      </div>
      <div class="user-list-box">
        <div>
          <el-tree
            ref="tree"
            :data="unitList"
            :props="defaultProps"
            node-key="id"
            :current-node-key="defaultCheckedKeys"
            @node-click="handleNodeClick"
            highlight-current
          >
          </el-tree>
        </div>
        <div>
          <div class="tb_wrapper">
            <el-table :data="tableData" border style="width: 100%">
              <el-table-column
                type="index"
                align="center"
                width="50"
                label="序号"
              ></el-table-column>
              <el-table-column
                prop="username"
                label="用户名"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="phone"
                label="电话"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="jobName"
                label="绑定角色"
                align="center"
              ></el-table-column>
              <el-table-column prop="accountState" label="状态" align="center">
                <template slot-scope="scope">
                  <div class="deal_box">
                    <span
                      class="deal_btn deal_btn_look"
                      v-if="scope.row.status == 1"
                      >正常</span
                    >
                    <span
                      class="deal_btn deal_btn_del"
                      v-if="scope.row.status == 2"
                      >锁定</span
                    >
                    <span
                      class="deal_btn deal_btn_del"
                      v-if="scope.row.status == 3"
                      >停用</span
                    >
                  </div>
                </template>
              </el-table-column>
              <el-table-column align="center" label="操作" width="200">
                <template slot-scope="scope">
                  <div class="deal_box">
                    <span
                      class="deal_btn deal_btn_look"
                      @click="bindUserJob(scope.row)"
                      >角色绑定</span
                    >
                    <span
                      class="deal_btn deal_btn_look"
                      v-if="scope.row.status == 3"
                      @click="enableUser(scope.row.status, scope.row.id)"
                      >启用</span
                    >
                    <span
                      class="deal_btn deal_btn_del"
                      v-if="scope.row.status !== 3"
                      @click="enableUser(scope.row.status, scope.row.id)"
                      >停用</span
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
              @pagination="getUserList"
            />
          </div>
        </div>
      </div>
    </div>
    <el-dialog title="绑定角色" width="500px" :visible.sync="showEditDialog">
      <user-form
        v-if="showEditDialog"
        :id="editId"
        :jobId="jobId"
        @closeEditDialog="closeEditDialog"
      ></user-form>
    </el-dialog>
  </div>
</template>

<script>
import {
  userUnitListApi,
  queryByUnitApi,
  enableUserApi,
} from "@/api/systemManagement.js";
import UserForm from "./components/UserForm";
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
        label: "name",
      },
      defaultCheckedKeys: "",
      showEditDialog: false,
      editId: "",
      jobId: "",
    };
  },
  components: { UserForm },
  mounted() {
    this.getUnitList();
  },
  methods: {
    closeEditDialog(type) {
      this.showEditDialog = false;
      if (type) {
        this.getUserList();
      }
    },
    bindUserJob(data) {
      console.log(data);
      this.jobId = data.jobId;
      this.editId = data.id;
      this.showEditDialog = true;
    },
    handleNodeClick(data) {
      if (this.defaultCheckedKeys !== data.id) {
        this.pageNum = 1;
        this.defaultCheckedKeys = data.id;
        this.getUserList();
      }
    },
    async getUnitList() {
      let res = await userUnitListApi();
      if (res && res.length > 0) {
        this.unitList = res;
        if (this.unitList.length > 0) {
          if (this.$refs.tree) {
            this.$nextTick(() => {
              this.$refs.tree.setCurrentKey(this.unitList[0].id);
            });
          }
          this.defaultCheckedKeys = this.unitList[0].id;
          this.getUserList();
        }
      }
    },
    async getUserList() {
      let res = await queryByUnitApi({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        deptId: this.defaultCheckedKeys,
      });
      if (res) {
        this.pageTotal = res.totalRows;
        this.tableData = res.list;
      }
    },
    enableUser(accountState, id) {
      let str = `是否${accountState != 3 ? "停用" : "启用"}该用户?`;
      this.$confirm(str, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(async () => {
        let res = await enableUserApi({
          status: accountState != 3 ? 3 : 1,
          id,
        });
        if (res.code == 0) {
          this.$message.success(`${accountState != 3 ? "停用" : "启用"}成功`);
          this.getUserList();
        }
      });
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
  }
}
.user-list-box {
  display: flex;
  justify-content: space-between;
  height: calc(100% - 56px);
  > div:nth-child(1) {
    width: 400px;
    padding: 16px;
    height: 100%;
    border-right: 1px solid #eee;
    box-sizing: border-box;
    overflow: auto;
    .custom-tree-node {
      span {
        font-size: 16px;
        font-weight: 500;
      }
    }
  }
  > div:nth-child(2) {
    width: calc(100% - 420px);
  }
}
</style>

<style lang="scss">
.user-list-box {
  .el-tree {
    .el-tree-node {
      .el-tree-node__content {
        height: 40px;
        line-height: 40px;
        &:hover {
          background-color: #edf6fe;
          color: #008dfe;
        }
      }
      &.is-current {
        .el-tree-node__content {
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