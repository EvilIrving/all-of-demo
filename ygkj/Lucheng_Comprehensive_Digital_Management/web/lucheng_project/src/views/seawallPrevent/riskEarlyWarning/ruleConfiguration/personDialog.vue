<template>
  <el-dialog :visible.sync="visible" width="490px" :before-close="close" top="10vh" :close-on-click-modal="false" title="选择人员" class="list-dialog" append-to-body>
    <el-row style="margin-top: 8px">
      <el-col>
        <p style="float: left; line-height: 28px; height: 28px">
          查询人员名称：
        </p>
        <el-input style="margin-left: 15px; width: 160px" size="mini" v-model="searchName" placeholder="请输入名称搜索" />
      </el-col>
    </el-row>
    <div style="height: 450px; overflow-y: auto; margin-top: 16px">
      <!-- <el-tree ref="tree" class="filter-tree" :data="deptList" :props="defaultProps" node-key="id" highlight-current auto-expand-parent :filter-node-method="filterNode" :default-expanded-keys="checkIds"
        :default-checked-keys="checkIds">
        <span class="custom-tree-node" slot-scope="{ scope, node }">
          <span>{{ node.label }}</span>
        </span>
      </el-tree> -->
      <div class="tb_wrapper">
        <el-table :data="tableData" ref="chooseTable" stripe border v-loading="tableLoading" header-row-class-name="table-header" element-loading-background="rgba(0, 0, 0, 0.5)" element-loading-text="数据正在加载中"
          element-loading-spinner="el-icon-loading" @selection-change="handleSelectionChange" style="width: 100%">
          <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
          <el-table-column prop="deptName" label="科室" align="center"></el-table-column>
          <el-table-column prop="name" label="姓名" width="70" align="center"></el-table-column>
          <el-table-column prop="phone" label="电话" align="center"></el-table-column>
          <el-table-column type="selection" width="55" align="center">
          </el-table-column>
        </el-table>
      </div>
    </div>
    <div slot="footer">
      <div class="form-footer">
        <el-button class="form-btn btn-cancel" size="mini" @click="close">取消</el-button>
        <el-button class="form-btn btn-save" size="mini" @click="setPersonVal">确定</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import {
  customDep,
  customPeople,
  getCustomPeopleWithDepartment
} from "@/api/seawallPrevent";
export default {
  props: {
    visible: Boolean,
    checkedPersons: String
  },
  watch: {
    visible(val) {
      if (val) {
        this.getPerson();
      }
    },
    searchName(val) {
      // this.$refs.tree.filter(val);
      this.getPerson();
    }
  },
  mounted() {
    // this.getTree();
    this.getPerson();
  },
  data() {
    return {
      searchName: "", //根据名字查找人员
      personalList: [], //可选人员列表
      deptList: [], //可选部门列表
      checkIds: [],
      defaultProps: {
        list: "children",
        label: "deptname"
      },
      loading: false,
      tableData: [],
      tableLoading: false,
      checkItems: [],
      num: 0
    };
  },
  methods: {
    // 左侧树状图请求接口
    async getTree() {
      this.checkIds = [];
      console.log(this.checkedPersons, "this.checkedPersons");
      if (this.checkedPersons != "") {
        this.checkIds = this.checkedPersons.split(",");
      }
      this.loading = true;
      await customDep.customTree().then(res => {
        if (res.code == 0) {
          // this.personalList = this.changeDepartData([res.data]);
          this.getPerson();
        }
      });
      this.loading = false;
    },
    async getPerson() {
      this.tableLoading = true;
      this.checkIds = [];
      if (this.checkedPersons != "") {
        this.checkIds = this.checkedPersons.split(",");
      }
      // await customPeople.list().then(res => {
      //   if (res.code == 0) {
      //     this.tableLoading = false;
      //     this.tableData = res.data.list;
      //     this.$nextTick(() => {
      //       if (this.checkIds.length > 0) {
      //         this.tableData.forEach(row => {
      //           for (var i = 0; i < this.checkIds.length; i++) {
      //             if (this.checkIds[i] === row.id) {
      //               this.$refs.chooseTable.toggleRowSelection(row, true);
      //             }
      //           }
      //         });
      //       }
      //     });
      //   }
      // });
      let params = {};
      if (this.searchName !== "") params.name = this.searchName;
      await getCustomPeopleWithDepartment(params).then(res => {
        if (res.code == 0) {
          this.tableLoading = false;
          this.tableData = res.data;
          this.$nextTick(() => {
            if (this.checkIds.length > 0) {
              this.tableData.forEach(row => {
                for (var i = 0; i < this.checkIds.length; i++) {
                  if (this.checkIds[i] === row.id) {
                    this.$refs.chooseTable.toggleRowSelection(row, true);
                  }
                }
              });
            }
          });
        }
      });
    },
    filterNode(value, data) {
      if (!value) return true;
      if (data.name != undefined || data.name != null) {
        return data.name.indexOf(value) !== -1;
      }
    },
    //  组装数据
    changeDepartData(data) {
      data.map(item => {
        if (item.list == null) {
          item.deptCustom.children = [];
          delete item.list;
        } else {
          if (item.deptCustom.deptname) {
            this.deptList.push(item.deptCustom);
            item.deptCustom.children = this.changeDepartData([...item.list]);
            delete item.list;
          }
        }
        return item;
      });
      // console.log(this.deptList, "deptList");
      return data;
    },
    handleSelectionChange(val) {
      this.checkItems = val;
      this.num = val.length;
    },
    // 设置阈值
    setPersonVal() {
      // if (this.checkItems.length == 0) {
      //   this.$message.warning("请勾选需要的人员！");
      // } else {
        this.$emit("setPersonIds", this.checkItems);
        this.close();
      // }
    },
    // 关闭
    close() {
      this.searchName = "";
      this.$emit("close");
    },
    save() {
      let checkedPerSon = this.$refs.tree.getCheckedNodes().filter(item => {
        return item.userName;
      });
      checkedPerSon.filter(item => {
        return item.userName;
      });
      this.$emit("saveEmit", checkedPerSon);
      this.close();
    }
  }
};
</script>

<style lang="scss"></style>
