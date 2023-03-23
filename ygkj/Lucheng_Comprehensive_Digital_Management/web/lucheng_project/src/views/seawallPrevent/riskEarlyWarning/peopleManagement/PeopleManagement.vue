<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-07-06 14:17:46
 * @Description: 
 * @FilePath: \lucheng_project\src\views\seawallPrevent\riskEarlyWarning\peopleManagement\PeopleManagement.vue
-->
<template>
  <div class="people-box">
    <div class="people-left">
      <div class="item_box_title">
        <p>部门列表</p>
      </div>
      <div class="list-box">
        <div class="add-department">
          <el-button
            @click="addDepartment"
            class="btn-box"
            type="primary"
            size="small"
            v-permission="'peopleManagement:add'"
            >新增部门</el-button
          >
        </div>
        <div class="tree-list">
          <el-tree
            ref="popularTree"
            class="tree-box-list"
            :data="deplList"
            node-key="id"
            @node-click="handleNodeClick"
            :current-node-key="currentLivingId"
            :default-checked-keys="[2]"
            :props="defaultProps"
            :default-expanded-keys="[1]"
            :expand-on-click-node="false"
            :highlight-current="true"
          >
            <span class="custom-tree-node" slot-scope="{ node, data }">
              <div class="tree-box">
                <div class="tree-name" :title="node.label">
                  {{ node.label }}
                </div>
                <div class="tree-btn">
                  <img
                    src="../../../../assets/images/add-btn.png"
                    class="add-span"
                    v-permission="'peopleManagement:add'"
                    alt="添加部门"
                    @click="() => append(data)"
                  />
                  <img
                    src="../../../../assets/images/c_del.png"
                    class="sel-span"
                    v-permission="'peopleManagement:del'"
                    alt="删除部门"
                    @click="() => remove(node, data)"
                  />
                </div>
              </div>
            </span>
          </el-tree>
        </div>
      </div>
    </div>
    <div class="people-right">
      <div class="item_box_title">
        <p>预警人员列表</p>
      </div>
      <div
        class="info_option"
        style="
          width: 100%;
          padding: 25px 16px 16px 16px;
          box-sizing: border-box;
          margin-bottom: 20px;
        "
      >
        <!-- <el-input
          placeholder="请输入名称"
          prefix-icon="el-icon-search"
          @change="getPerson()"
          v-model="searchText"
          style="width: 240px; margin-right: 15px"
          clearable
        /> -->
        <el-button
          type="primary"
          class="right"
          v-permission="'peopleManagement:add'"
          @click="newPerson('新增')"
          >新增</el-button
        >
        <el-button
          style="margin-right: 10px"
          class="right"
          v-permission="'peopleManagement:export'"
          @click="exportTable()"
          >导出</el-button
        >
      </div>
      <div class="tb_wrapper">
        <el-table :data="personalList" border style="width: 100%">
          <el-table-column
            type="index"
            align="center"
            width="50"
            label="序号"
          ></el-table-column>
          <el-table-column
            prop="name"
            label="姓名"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="phone"
            label="电话"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="duty"
            label="职务"
            align="center"
          ></el-table-column>
          <el-table-column align="center" label="操作" width="150">
            <template slot-scope="scope">
              <div class="deal_box">
                <span
                  class="deal_btn deal_btn_look"
                  @click="editTable('编辑', scope.row.id)"
                  v-permission="'peopleManagement:edit'"
                  >编辑</span
                >
                <span
                  class="deal_btn deal_btn_del"
                  @click="del(scope.row.id)"
                  v-permission="'peopleManagement:del'"
                  >删除</span
                >
              </div>
            </template>
          </el-table-column>
        </el-table>
        <Pagination
          :total="pageTotal"
          :page.sync="pageNum"
          :limit.sync="pageSize"
          :page-sizes="[5, 10, 15, 20]"
          @pagination="changePage"
        />
      </div>
    </div>
    <el-dialog
      title="新增部门"
      :visible.sync="showAddPersonDialog"
      width="500px"
    >
      <div class="form-box">
        <div class="form-lable-item">
          <div class="label">部门名称：</div>
          <div class="item">
            <el-input
              maxlength="15"
              v-model="departmentValue"
              placeholder="请输入"
            ></el-input>
          </div>
        </div>
        <div class="btn-box">
          <el-button size="small" @click="showAddPersonDialog = false"
            >取消</el-button
          >
          <el-button size="small" type="primary" @click="saveDepartment"
            >保存</el-button
          >
        </div>
      </div>
    </el-dialog>
    <el-dialog :title="title" :visible.sync="showEditDialog" width="60%">
      <PeopleFrom
        v-if="showEditDialog"
        :isAdd="isAdd"
        :id="isAdd ? currentLivingId : namedId"
        @closeEditDialog="closeEditDialog"
        @getPerson="getPerson"
      ></PeopleFrom>
    </el-dialog>
  </div>
</template>

<script>
import PeopleFrom from "./PeopleFrom.vue";
import { exportPersonList } from "@/utils/export.js";
import { customDep, customPeople } from "@/api/seawallPrevent";
export default {
  data() {
    return {
      showEditDialog: false,
      showAddPersonDialog: false,
      departmentValue: "",
      parentDepPid: "", // 新增部门时的父级pid
      namedId: "1", // 编辑时人员id
      currentLivingId: 1, // 当前所在部门列表id,
      data: [],
      deplList: [], //可选部门列表
      defaultProps: {
        children: "list",
        label: "deptname",
      },
      searchText: "", //搜索人员
      personalList: [], //可选人员列表
      loading: false,
      tableLoading: false,
      title: "编辑",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      isAdd: false,
    };
  },
  mounted() {
    this.getTree();
    this.getPerson();
  },
  components: { PeopleFrom },
  methods: {
    //添加部门
    addDepartment() {
      this.parentDepPid = "1";
      this.showAddPersonDialog = true;
    },
    //添加子部门
    append(data) {
      this.parentDepPid = data.id;
      this.showAddPersonDialog = true;
    },
    //保存部门
    async saveDepartment() {
      const opt = { deptname: this.departmentValue, pid: this.parentDepPid };
      let res = await customDep.insert(opt);
      if (res.code === 0) {
        this.$message({
          type: "success",
          message: "新增成功",
        });
        this.getTree();
        this.getPerson();
      } else {
        this.$message({
          type: "error",
          message: "新增失败",
        });
      }
      this.showAddPersonDialog = false;
    },
    //删除子部门
    remove(node, data) {
      if (data.pid === "0") {
        this.$message.error("顶层部门不可删除！");
        return;
      }
      this.$confirm("此操作将删除该文件夹下的所有文件, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          await customDep.delete({ id: data.id }).then(() => {
            this.$message({
              type: "success",
              message: "删除成功!",
            });
            this.getTree();
            this.getPerson();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    // 部门list 左侧树状图请求接口
    async getTree() {
      this.loading = true;
      await customDep.customTree().then((res) => {
        if (res.code == 0) {
          this.deplList = [res.data];
          this.loading = false;
          this.currentLivingId = res.data.id;
          this.$nextTick(function () {
            this.$refs.popularTree.setCurrentKey(this.currentLivingId);
          });
        }
      });
    },
    // 点击部门 获取人员信息
    handleNodeClick(row) {
      this.currentLivingId = row.id;
      this.getPerson(row.id);
    },
    // 人员list 右侧表格
    async getPerson() {
      const opt = {
        type: 2,
        deptid: this.currentLivingId,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      };
      this.tableLoading = true;
      await customPeople.list(opt).then((res) => {
        if (res.code == 0) {
          this.tableLoading = false;
          this.personalList = res.data.list;
          this.pageTotal = res.data.totalRows;
        }
      });
    },
    //修改或者新增
    newPerson(title) {
      this.isAdd = this.showEditDialog = true;
      this.title = title;
    },
    //修改或者新增
    editTable(title, id) {
      this.isAdd = false;
      this.title = title;
      this.showEditDialog = true;
      this.namedId = id;
    },
    //删除
    del(id) {
      this.$confirm("此操作将删除该人员, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          await customPeople
            .delete({
              id: id,
            })
            .then((res) => {
              if (res.code == 0) {
                this.$message({
                  message: "删除成功",
                  type: "success",
                });
                this.getPerson();
              } else {
                this.$message.error(res.msg);
              }
            });
        })
        .catch(() => {});
    },
    changePage(pager) {
      this.pageNum = pager.page;
      this.pageSize = pager.limit;
      this.getPerson();
    },
    //导出
    async exportTable() {
      let obj = {
        deptid: this.currentLivingId,
        type: 2,
      };
      exportPersonList(obj);
    },
    closeEditDialog() {
      this.showEditDialog = false;
    },
  },
};
</script>

<style lang="scss" scoped>
.people-box {
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
  .people-left {
    @include contentBox();
    width: 320px;
    min-height: 655px;
    overflow-x: auto;
    .list-box {
      padding-top: 8px;
      .add-department {
        padding: 0 20px;
        .btn-box {
          width: 100%;
          font-size: 14px;
        }
      }
      .tree-list {
        width: 100%;
        padding-top: 8px;
        .tree-box {
          display: flex;
          align-items: center;
          justify-content: space-between;
          .tree-name {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            padding-right: 8px;
          }
          .tree-btn {
            width: 50px;
            .add-span {
              width: 20px;
              height: 20px;
              margin-right: 12px;
            }
            .del-span {
              width: 18px;
              height: 17px;
            }
          }
        }
      }
    }
  }
  .people-right {
    @include contentBox();
    width: calc(100% - 340px);
  }
  .form-box {
    .form-lable-item {
      display: flex;
      align-items: center;
      padding-bottom: 30px;
      .label {
        width: 80px;
      }
      .item {
        width: calc(100% - 80px);
      }
    }
    .btn-box {
      text-align: right;
    }
  }
}
</style>

<style lang="scss">
.tree-list .el-tree .custom-tree-node {
  width: 100%;
}
.tree-list .el-tree .el-tree-node__content {
  height: 48px;
  padding-right: 18px;
}
.tree-list
  .el-tree.el-tree--highlight-current
  .el-tree-node.is-current
  > .el-tree-node__content {
  background: url("../../../../assets/images/tree-active-icon.png") no-repeat;
  background-size: 100% 100%;
}
</style>
