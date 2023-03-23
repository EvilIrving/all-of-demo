<template>
  <el-dialog :visible="visible" width="500px" center :before-close="close" top="10vh" :close-on-click-modal="false" title="选择人员" class="list-dialog" append-to-body>
    <div style="margin-top: 16px">
      <el-input style="margin-left: 15px; width: 95%" v-model="searchName" placeholder="请输入名称搜索" />
    </div>
    <div class="tree-box" style="height: 500px; overflow-y: auto; margin-top: 10px" v-loading="loading">
      <el-tree ref="tree" class="filter-tree" show-checkbox :data="personalList" :props="{ children: 'children', label: 'title' }" node-key="id" :check-strictly="true" highlight-current :filter-node-method="filterNode"
        :default-checked-keys="selectPersonList" @check-change="treeNodeClick">
        <template #default="{ node }">
          <span class="custom-tree-node">
            <span>{{ node.label }}</span>
          </span>
        </template>
      </el-tree>
    </div>
    <div slot="footer">
      <div class="form-footer">
        <el-button class="form-btn btn-cancel" @click="close">取消</el-button>
        <el-button class="form-btn btn-save" @click="save">确定</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import { projectSupervisionApi } from "@/api/projectSupervision";
export default {
  props: {
    visible: Boolean,
    PersonnelType: String,
    /** @type {import('vue/types/options').PropValidator<{id:string}[]>} */
    selectPersonList: Array,
    specifiedArea: {
      type: String,
      default: ""
    }
  },
  watch: {
    visible(val) {
      if (val) this.getTree();
    },
    searchName(val) {
      this.$refs.tree.filter(val);
    }
  },
  data() {
    return {
      searchName: "", //根据名字查找人员
      personalList: [], //可选陪同人员列表
      loading: false
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.getTree();
    });
  },
  methods: {
    // 左侧树状图请求接口
    async getTree() {
      this.loading = true;
      await projectSupervisionApi
        .getDeptUserTree({ key: this.username })
        .then(res => {
          if (res.code == 0) {
            let data = res.data;
            if (this.specifiedArea !== "") {
              data = data.filter(e => e.title === this.specifiedArea);
            }
            this.personalList = this.changeDepartData(data);
            let currentIndex = this.personalList.findIndex(
              item => item.id == this.userDepartmentId
            );
            let currentDepart = this.personalList.splice(currentIndex, 1)[0];
            this.personalList.unshift(currentDepart);
          }
        });
      this.loading = false;
    },
    filterNode(value, data) {
      if (!value) return true;
      if (data.title != undefined || data.title != null) {
        return data.title.indexOf(value) !== -1;
      }
    },
    treeNodeClick(data, checked) {
      if (["巡查人员", "指定巡查人员"].includes(this.PersonnelType)) {
        if (checked) {
          const keys = this.$refs.tree.getCheckedKeys();
          keys.map(item => {
            if (item != data.id) {
              this.$refs.tree.setChecked(item, false);
            }
          });
        }
      }
    },
    // 组装数据
    changeDepartData(data) {
      data.map(item => {
        if (item.children) {
          if (item.children.length == 0) {
            item.children = [...item.sysUsers];
            item.children.forEach(sItem => {
              sItem.title = sItem.realName || sItem.userName || sItem.title;
            });
          } else {
            item.children = this.changeDepartData([
              ...item.children,
              ...item.sysUsers
            ]);
          }
        } else {
          item.title = item.name || item.userName || item.title;
          if (item.pid) {
            item.children = this.changeDepartData([...item.sysUsers]);
          }
        }
        if (item.sysUsers && item.sysUsers.length > 0) {
          item.sysUsers.forEach(items => {
            items.deptId = item.id;
            items.indexId = items.id + "," + items.deptId;
          });
        }
        return item;
      });
      return data;
    },
    // 关闭
    close() {
      this.searchName = "";
      this.$emit("close");
      this.$emit("update:visible", false);
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

<style lang='scss'>
.pagination-area {
  padding: 12px 0;
}
.tree-box {
  .el-tree-node {
    .is-leaf + .el-checkbox .el-checkbox__inner {
      display: inline-block;
    }
    .el-checkbox .el-checkbox__inner {
      display: none;
    }
  }
}
</style>
