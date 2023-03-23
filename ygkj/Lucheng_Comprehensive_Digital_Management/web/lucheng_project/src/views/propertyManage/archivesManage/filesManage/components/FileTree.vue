<template>
  <div class="wrap user-tree" v-loading="loading">
    <!-- 档案树 -->
    <el-tree :data="fileList" node-key="id" ref="fileTree" :props="defaultProps" :highlight-current="true" :expand-on-click-node="false" :default-expanded-keys="expandedKeys" @node-click="handleNodeClick"></el-tree>
  </div>
</template>

<script>
import { getFileTree, getUpFileListApi } from "@/api/propertyManage";
export default {
  name: "FileTree",
  data() {
    return {
      fileList: [],
      defaultProps: {
        children: "children",
        label: "name",
      },
      selectFile: "",
      selectFileId: "",
      selectFileName: "",
      expandedKeys: [],
      loading: true,
    };
  },
  components: {},
  mounted() {
    this._getFileList();
  },
  methods: {
    async _getFileList() {
      let res = await getFileTree({
        projectCode: this.$route.path === '/filesManage' ? this.$store.state.seawallDetail.prcd : '',
      })
      if (res.code === 0) {
        this.loading = false;
        if (this.selectFileId == "") {
          this.selectFile = res.data[0];
          this.selectFileId = res.data[0].id;
          this.selectFileName = res.data[0].name;
        }
        this.fileList = res.data;
        //选中默认节点
        this.$nextTick().then(() => {
          this.expandedKeys.push(res.data[0].id);
          this.$refs.fileTree.setCurrentKey(this.selectFileId);
        });
        //请求表格数据
        this.$emit("getTableData", this.selectFileId, this.selectFile);
      }
    },

    handleNodeClick(data) {
      this.selectFile = data;
      this.selectFileId = data.id;
      this.$emit("nodeClick", data.id, data);
    },
  },
};
</script>

<style lang="scss" scoped>
.user-tree .el-tree {
  ::v-deep > .el-tree-node {
    border-radius: 4px;
    border: 1px solid #e5e5e5;
    background: #f9f9f9;
    margin-top: 8px;
    .el-tree-node__children {
      overflow-x: auto;
    }
    .el-tree-node__content {
      height: 40px;
      .el-tree-node__label {
        height: 24px;
        line-height: 24px;
        padding-left: 28px;
        background: url("../../../../../assets/images/fileTree_icon.png")
          no-repeat center left;
        background-size: auto 100%;
      }
      .parent-node {
        width: 100%;
        position: relative;
        .add-round-btn {
          width: 18px;
          height: 18px;
          position: absolute;
          right: 3px;
          top: -1px;
        }
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
::v-deep
  .el-tree--highlight-current
  .el-tree-node.is-current
  > .el-tree-node__content {
  color: #189bfe;
}
</style>