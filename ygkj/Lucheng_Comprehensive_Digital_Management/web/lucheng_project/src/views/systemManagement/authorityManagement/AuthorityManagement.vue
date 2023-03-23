<template>
  <div class="wrap">
    <div class="content">
      <div class="item_box_title">
        <p>权限管理</p>
      </div>
      <div class="menu-list-box">
        <div>
          <div class="menu-title">
            <h3>角色列表</h3>
            <el-button type="primary" size="small" @click="editJob('')"
              >添加</el-button
            >
          </div>
          <div class="month_list">
            <div
              :class="['month_item', index == jobIndex ? 'active' : '']"
              v-for="(item, index) in listData"
              :key="index"
              @click="clickJob(index)"
            >
              <div>
                <i class="el-icon-s-custom"></i>
                <span>{{ item.jobName }}</span>
              </div>
              <div v-if="item.id != 'admin'">
                <div class="c_icon c_edit" @click.stop="editJob(item.id)"></div>
                <div
                  class="c_icon c_del"
                  @click.stop="del(item.id, index)"
                ></div>
              </div>
            </div>
          </div>
        </div>
        <div>
          <div class="menu-title">
            <h3>角色菜单权限</h3>
            <el-button type="primary" size="small" @click="save()"
              >保存</el-button
            >
          </div>
          <div class="tree_box" v-loading="treeLoading">
            <el-tree
              ref="tree"
              :data="treeData"
              :props="defaultProps"
              show-checkbox
              default-expand-all
              node-key="value"
            >
              <div class="custom-tree-node" slot-scope="{ node, data }">
                <div>{{ node.label }}</div>
                <div
                  class="btn-box"
                  v-if="data.subset && data.subset.length > 0"
                >
                  <span
                    :class="setClassName(item.sysRes.id)"
                    @click="ckeckedIds(item.sysRes.id)"
                    v-for="item in data.subset"
                    :key="item.sysRes.id"
                  >
                    {{ item.sysRes.resName }}
                  </span>
                </div>
              </div>
            </el-tree>
          </div>
        </div>
      </div>
    </div>
    <el-dialog :title="title" :visible.sync="showEditDialog">
      <job-form
        v-if="showJob"
        :id="editId"
        @closeEditDialog="closeEditDialog"
      ></job-form>
    </el-dialog>
  </div>
</template>

<script>
import JobForm from "./components/JobForm";
import {
  sysJobListApi,
  sysJobResIdsApi,
  sysResTreeApi,
  sysBindResJobApi,
  sysJobDelApi,
} from "@/api/systemManagement";
export default {
  name: "JurisdictionManageList",
  props: {},
  components: {
    JobForm,
  },
  data() {
    return {
      treeData: [],
      defaultProps: {
        children: "children",
        label: "label",
      },
      listLoading: true,
      treeLoading: true,
      listData: [],
      jobIndex: 0,
      editId: "",
      showEditDialog: false,
      title: "新增",
      showJob: false,
      checkedJobIds: [],
      checkedIdsList: [],
    };
  },
  computed: {
    setClassName() {
      let _this = this;
      return (id) => {
        if (_this.checkedJobIds.includes(id)) {
          // if (!_this.checkedIdsList.includes(id)) {
          //   _this.checkedIdsList.push(id);
          // }
          return "success";
        } else {
          return "";
        }
      };
    },
  },
  mounted() {
    // await this.getList();
    // await this.getResTree();
    Promise.all([this.getList(), this.getResTree()]).then(() => {
      this.getJobIds();
      // this.getJobTree()
    });
  },
  methods: {
    async getList() {
      let res = await sysJobListApi();
      if (res.code == 0) {
        this.listData = res.data;
      } else {
        this.$message.error("获取角色列表失败！");
      }
    },
    async getResTree() {
      this.treeLoading = true;
      let res = await sysResTreeApi();
      if (res.code == 0) {
        let arr = [];
        let userInfo = this.$sessionData("get", "userInfo")
        if (userInfo.jobId != 'admin') {
          res.data.wbTree.forEach(element => {
            if (element.sysRes.resName == '系统管理') {
              element.subset.splice(1,1)
            }
          });
        }
        if (res.data.wbTree.length) {
          this.setTreeList(res.data.wbTree, arr);
        }
        this.treeData = arr;
      } else {
        this.$message.error("获取菜单失败！");
      }
      this.treeLoading = false;
    },
    setTreeList(list, arr) {
      list.map((item) => {
        let obj = {
          value: item.sysRes.id,
          label: item.sysRes.resName,
          type: item.sysRes.resType,
          children: [],
        };
        if (item.sysRes.resType == 2 && item.subset && item.subset.length) {
          obj["subset"] = item.subset;
        }
        arr.push(obj);
        if (item.subset && item.subset.length && item.sysRes.resType == 1) {
          this.setTreeList(item.subset, obj.children);
        }
      });
    },
    async getJobIds() {
      let res = await sysJobResIdsApi({
        id: this.listData[this.jobIndex].id,
      });
      this.checkedJobIds = res.data;
      this.$refs.tree && this.$refs.tree.setCheckedKeys(res.data);
    },
    ckeckedIds(id) {
      let index = this.checkedJobIds.findIndex((item) => item === id);
      if (index != -1) {
        this.checkedJobIds.splice(index, 1);
        // let indexIds = this.checkedIdsList.findIndex((item) => item === id);
        // this.checkedIdsList.splice(indexIds, 1);
      } else {
        this.checkedJobIds.push(id);
        // this.checkedIdsList.push(id);
      }
    },
    clickJob(index) {
      this.jobIndex = index;
      this.getJobIds();
    },
    editJob(id) {
      if (id) {
        this.title = "编辑角色";
        this.editId = id;
      } else {
        this.title = "新增角色";
        this.editId = "";
      }
      this.showEditDialog = true;
      this.showJob = false;
      this.$nextTick(() => {
        this.showJob = true;
      });
    },
    del(id, index) {
      if (!id) {
        this.$message.error("数据id不存在！");
      } else {
        this.$confirm("此操作将删除该条数据, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }).then(() => {
          if (this.jobIndex === index) {
            this.jobIndex = 0;
          }
          sysJobDelApi({
            id: id,
          }).then(async (response) => {
            if (response.code == 0) {
              await this.getList();
              await this.getJobIds();
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
    async closeEditDialog(type) {
      this.showEditDialog = false;
      this.showJob = false;
      if (type) {
        await this.getList();
        await this.getJobIds();
      }
    },
    async save() {
      let ids = [];
      let checkArr = this.$refs.tree
        ? this.$refs.tree.getCheckedNodes(false, true)
        : [];
      if (checkArr.length) {
        checkArr.map((v) => {
          ids.push(v.value);
        });
      }
      ids = [...ids, ...this.checkedJobIds];
      let res = await sysBindResJobApi({
        id: this.listData[this.jobIndex].id,
        nbIds: "",
        wbIds: ids.join(","),
      });
      if (res.code == 0) {
        this.$message.success("保存成功");
        this.getJobIds();
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
    .month_list {
      height: calc(100% - 68px);
      overflow: auto;
      &::-webkit-scrollbar {
        display: none;
      }
      .c_icon {
        width: 16px;
        height: 16px;
        margin-left: 8px;
        cursor: pointer;
        display: inline-block;
        vertical-align: -3px;
      }
      .month_item {
        padding: 0 18px;
        height: 70px;
        box-sizing: border-box;
        border-top: 1px solid #eeeff0;
        cursor: pointer;
        position: relative;
        display: flex;
        align-items: center;
        justify-content: space-between;
        & i {
          color: #0089f0;
          margin-right: 8px;
        }
        &:last-child {
          border-bottom: 1px solid #eeeff0;
        }
        div {
          display: flex;
          align-items: center;
          span {
            font-size: 15px;
            color: #333333;
            font-weight: 500;
          }
          img {
            margin-right: 9px;
          }
        }
        p {
          font-size: 12px;
          color: #666666;
          padding: 10px 0 0 27px;
        }
        &.active {
          background: #f0f9ff;
          // border-right: 4px solid #008DFE;
        }
      }
    }
  }
  > div:nth-child(2) {
    width: calc(100% - 420px);
    padding: 16px;
    .tree_box {
      background: #f5f5f5;
      height: calc(100% - 40px);
      overflow: auto;
      padding: 10px;
      box-sizing: border-box;
      .el-tree {
        background: #f5f5f5;
      }
    }
  }
}
.custom-tree-node {
  display: flex;
  align-items: center;
  justify-content: space-between;
  .btn-box {
    display: flex;
    margin-left: 50px;
    span {
      padding: 5px 10px;
      color: #fff;
      border-radius: 4px;
      font-size: 12px;
      margin-right: 10px;
      background-color: #909399;
      &.success {
        background-color: #409eff;
      }
    }
  }
}
</style>
