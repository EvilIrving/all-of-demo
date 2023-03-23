<!--
 * @Author: zhongg
 * @LastEditTime: 2021-10-25 10:44:01
 * @Descripttion: 
 * @FilePath: /lucheng_project/src/views/propertyManage/archivesManage/authorityManage/components/SelectDepart.vue
-->
<template>
  <div class="user_tree" v-loading="loading">
    <!-- 部门 -->
    <el-tree
      :data="fileList"
      show-checkbox
      node-key="id"
      ref="fileTree"
      :check-strictly="true"
      :props="defaultProps"
      :default-expanded-keys="expandedKeys"
      :default-checked-keys="checkedKeys"
    ></el-tree>

    <div slot="footer">
      <el-row :gutter="20">
        <div class="form-footer">
          <el-button class="form-btn save-btn" size="mini" @click="sure"
            >确定</el-button
          >
        </div>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getDeptList } from "@/api/propertyManage";
export default {
  name: "SelectDepart",
  props: {},
  data() {
    return {
      fileList: [],
      defaultProps: {
        children: "children",
        label: "title",
      },
      selectFileId: "",
      selectFileName: "",
      checkedKeys: [],
      expandedKeys: [],
      loading: true,
      checkDepart: [],
      checkDepartIds: [],
    };
  },
  components: {},
  mounted() {
    //this._getFileList();
  },
  methods: {
    async _getFileList(chooseDepartIds) {
      await getDeptList().then((res) => {
        this.loading = false;
        this.fileList = res[0].children;
        //选中默认节点
        this.$nextTick().then(() => {
          if (chooseDepartIds.length == 0) {
            this.expandedKeys.push(res[0].children[0].id);
          } else {
            this.expandedKeys = chooseDepartIds;
            this.checkedKeys = chooseDepartIds;
          }
        });
      });
    },

    sure() {
      this.checkDepart = this.$refs.fileTree.getCheckedNodes();
      if (this.checkDepart.length > 0) {
        for (var i = 0; i < this.checkDepart.length; i++) {
          this.checkDepartIds.push(this.checkDepart[i].id);
        }
      } else {
        this.checkDepartIds = [];
      }
      //console.log(this.checkDepart);
      //console.log(this.checkDepartIds);
      this.$emit("getDeparts", this.checkDepart, this.checkDepartIds);
    },
    clearDepart() {
      this.checkedKeys = [];
      this.expandedKeys = [];
    },
  },
};
</script>

<style lang="scss" scoped>
.user_tree .el-tree {
  padding: 0 8px 8px;
  max-height: 500px;
  overflow-y: auto;
  > .el-tree-node {
    border-radius: 4px;
    border: 1px solid #e5e5e5;
    background: #f9f9f9;
    margin-top: 8px;
    .el-tree-node__content {
      height: 40px;
      .parent-node {
        width: 100%;
        position: relative;
      }
      .tree-none1 {
        font-weight: bold;
      }
      .second-node {
        width: 100%;
        position: relative;
        .tree-node2 {
          display: inline-block;
          max-width: 180px;
          @include text-ellipsis;
        }
      }
    }
  }
}
.form-footer {
  border-top: 2px solid #dcdfe6;
  padding: 16px 0 0 0;
  text-align: right;
}
</style>