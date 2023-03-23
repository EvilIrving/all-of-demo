<template>
  <div class="single-select-tree">
    <el-input v-model="searchVal"></el-input>
    <div class="tree-box">
      <el-tree
        ref="singleTree"
        node-key="id"
        :data="treeData"
        :props="defaultProps"
        :highlight-current="true"
        :default-expanded-keys="expandedKeys"
        @node-click="handleNodeClick"
        :filter-node-method="filterNode"
      >
        <span
          :title="node.label + '(' + (data.count ? data.count : 0 )+')'"
          class="custom-tree-node"
          slot-scope="{ node, data }"
        >
          <span>
            <i
              :class="[
                node.isLeaf && 'node-icon',
                node.isLeaf && node.isCurrent && 'el-icon-circle-check',
              ]"
            ></i>
            {{ node.label + '(' + (data.count ? data.count : 0) +')'}}
          </span>
        </span>
      </el-tree>
    </div>
  </div>
</template>

<script>
import { dutyManage, dataManage } from "@/api/dataMaintenance";
import { getDataCategoryNoTable, getBusDataTable, initTableColumns, addBusDataTable } from '@/api/dataTable.js';
import { mapActions } from 'vuex';
export default {
  name: 'SingleSelectTree',
  data() {
    return {
      searchVal: "",
      treeData: [],
      defaultProps: {
        children: "children",
        label: "categoryName",
      },
      expandedKeys: [],
      currentNode: "",
    }
  },
  computed:{
  },
  watch: {
    searchVal(val) {
      this.$refs.singleTree.filter(val);
    }
  },
  mounted() {
    this.getTreeData().then(() => {
      let node = this.$refs.singleTree.getNode(this.treeData[0].id);
      this.handleNodeClick(this.treeData[0],node);
    });
  },
  methods: {
    ...mapActions(["SetDataType","SetObjectType","SetManageBtnFlag"]),
    async getTreeData() {
      let res = await dutyManage.categoryTree("基础数据");
      if (res.code == 0) {
        this.treeData = res.data;
      }
      let result = await dutyManage.categoryTree("实时数据");
      if (result.code == 0) {
        if (result.data.length > 0) {
          this.treeData.push(...result.data);
        }
      }
    },
    handleNodeClick(data,obj) {
      let node = this.setFirstSelect(obj);
      const nodeData = node.data;
      this.SetDataType(node.parent.parent.data.categoryName);
      this.SetObjectType(node.parent.data.categoryName);
      //基础数据表没有管理数据按钮 数据类型id为1开头的是基础表
      if(node.parent.data.id[0] == 1){
        this.SetManageBtnFlag(true);
      }else{
        this.SetManageBtnFlag(false);
      }
      this.$emit("nodeClick", nodeData);
      this.$refs.singleTree.setCurrentKey(nodeData.id);
      this.expandedKeys = [];
      this.expandedKeys.push(nodeData.id);
    },
    //选中当前结点的第一个叶子结点
    setFirstSelect(node) {
      let childNode;
      let recursion = (data) => {
        if (data.childNodes.length > 0) {
          recursion(data.childNodes[0]);
        } else {
          childNode = data;
        }
      }
      recursion(node);
      return childNode;
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.categoryName.indexOf(value) !== -1;
    },
  }
}
</script>

<style lang="scss" scoped>
.single-select-tree {
  width: 100%;
  height: 100%;
  @include flexbox;
  @include flexflow(column, nowrap);
  ::v-deep .el-tree {
    background: transparent;
    color: #fff;
    .el-tree-node {
      &.is-focusable {
        .el-tree-node__content {
          color: #fff;
          background: transparent;
        }
      }
    }
    &.el-tree--highlight-current
      .el-tree-node.is-current
      > .el-tree-node__content {
      color: #56fefe;
      background: #2993b41a;
    }
    .el-tree-node__content {
      &:hover {
        color: #56fefe;
        background: #2993b41a;
      }
    }
  }
  .custom-tree-node {
    width: 100%;
    display: inline-block;
    @include text-ellipsis;
    .node-icon {
      display: inline-block;
      width: 14px;
      height: 14px;
      border-radius: 50%;
      border: 1px solid #a4a8aa;
      position: relative;
      top: 1px;
      &.el-icon-circle-check {
        color: #008a83;
        border: none;
      }
    }
  }
  ::v-deep .el-input {
    .el-input__inner {
      height: 28px;
      line-height: 28px;
    }
  }
  .tree-box {
    height: calc(100% - 16px);
    margin-top: 16px;
    padding: 0px 8px 16px 0;
    overflow: auto;
  }
}
</style>