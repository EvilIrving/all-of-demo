<template>
  <div class="main-menu-tree">
    <el-tree
      ref="singleTree"
      node-key="id"
      :data="treeData"
      :props="defaultProps"
      :highlight-current="true"
      :default-expanded-keys="expandedKeys"
      @node-click="handleNodeClick"
    >
      <span
        slot-scope="{ node, data }"
        :title="node.label"
        class="menu-tree-node"
      >
        <span>
          <i
            :class="[
              data.iconClass && data.iconClass && node.level == 1
                ? data.iconClass
                : '',
            ]"
          ></i>
          <b v-if="node.level == 3"></b>
          <!-- <i
            :class="[
              node.isLeaf && 'node-icon',
              node.isLeaf && node.isCurrent && 'el-icon-circle-check',
            ]"
          ></i> -->
          {{ node.label }}
        </span>
      </span>
    </el-tree>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: 'MenuTree',
  props: {
    treeData: Array
  },
  watch: {
    treeData(val) {
      this.$nextTick(() => {
        if (!this.WorkFlag) {
          let node = this.setFirstSelect(this.treeData[0]);
          this.handleNodeClick(node);
          this.expandedKeys = [];
          this.expandedKeys.push(node.id);
        } else {
          this.goToWork();
        }
      })
    },
    WorkId(val) {
      this.$nextTick(() => {
        this.goToWork();
      })
    }
  },
  computed: {
    ...mapGetters(["WorkName", "WorkId", "WorkFlag"]),
  },
  data() {
    return {
      defaultProps: {
        children: "children",
        label: "name",
      },
      expandedKeys: [],
      currentNode: "",
    }
  },
  mounted() {
    this.$nextTick(() => {
      if (this.WorkFlag) {
        this.$nextTick(() => {
          this.goToWork();
        })
      } else {
        let node = this.setFirstSelect(this.treeData[0]);
        this.handleNodeClick(node);
        this.expandedKeys = [];
        this.expandedKeys.push(node.id);
      }
    })
    // console.log(this.treeData,123)
    // this.handleNodeClick(this.treeData[0]);
  },
  methods: {
    goToWork() {
      this.expandedKeys = [];
      this.expandedKeys.push(this.WorkId);
      this.$refs.singleTree.setCurrentKey(this.WorkId);
    },
    handleNodeClick(data) {
      // let node = this.setFirstSelect(data);
      // this.$emit("nodeClick", node);
      this.$emit("nodeClick", data);
      this.$refs.singleTree.setCurrentKey(data.id);
      // this.expandedKeys = [];
      // this.expandedKeys.push(node.id);
    },
    //选中当前结点的第一个叶子结点
    setFirstSelect(node) {
      let childNode;
      let recursion = (data) => {
        if (data.children.length > 0) {
          recursion(data.children[0]);
        } else {
          childNode = data;
        }
      }
      recursion(node);
      return childNode;
    },
  }
}
</script>

<style lang="scss" scoped>
.main-menu-tree {
  width: 100%;
  height: calc(100% - 20px);
  padding-top: 20px;
  padding-right: 20px;
  color: rgba(0, 0, 0, 0.64);
  ::v-deep .el-tree {
    background: rgba(0, 0, 0, 0);
    .el-tree-node {
      padding: 10px;
      &:focus {
        > .el-tree-node__content {
          background: rgba(0, 0, 0, 0);
        }
      }
      &.is-current {
        > .el-tree-node__content {
          > .menu-tree-node {
            > span {
              position: relative;
              > b {
                position: absolute;
                left: 20px;
                top: 50%;
                transform: translateY(-50%);
                width: 6px;
                height: 6px;
                background: #56fefe;
                display: inline-block;
                border-radius: 50%;
                vertical-align: middle;
              }
            }
          }
        }
      }
      .el-tree-node__content {
        color: rgba(255, 255, 255, 0.64);
        text-align: left;
        height: 45px;
        line-height: 45px;
        &.active {
          color: #56fefe;
          background: rgba(255, 255, 255, 0.1);
        }
        &:hover {
          color: #56fefe;
          background: rgba(255, 255, 255, 0.1);
        }
        > .el-tree-node__expand-icon {
          position: absolute;
          right: 0;
          font-size: 22px;
        }
      }
    }

    &.el-tree--highlight-current {
      .el-tree-node {
        &.is-current {
          > .el-tree-node__content {
            color: #47e6f4;
            background-color: rgba(0, 0, 0, 0);
            background-image: linear-gradient(
              270deg,
              rgba(0, 111, 132, 0) 0%,
              rgba(18, 255, 240, 0.6) 100%
            );
          }
        }
      }
    }
  }
  .menu-tree-node {
    width: 100%;
    display: inline-block;
    > span {
      position: relative;
      padding-left: 34px;
      > i {
        position: absolute;
        display: inline-block;
        width: 18px;
        height: 18px;
        left: 8px;
        top: 50%;
        transform: translateY(-50%);
      }
    }
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
}
</style>