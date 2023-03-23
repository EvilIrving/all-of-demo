<template>
  <div class="bigScreen" ref="bigScreen">
    <div class="bigScreen_map">
      <screen-map ref="map" class="screen_map"></screen-map>
    </div>

    <!-- 左侧边栏 -->
    <screen-silde-panel
      position="left"
      @change="leftSildeChange"
      :height="sideHeight"
    >
      <div class="sidebar" :style="{ width: '350px' }">
        <div class="catalog-title">
          <div class="catalog-title-primary">
            <div class="panel-item__title--light" />
            <span style="margin-left: 6px">图层目录</span>
            <el-input
              v-model="layerSearch"
              prefix-icon="el-icon-search"
              placeholder="输入关键词查询"
              class="layer-search myClass"
              clearable
              @input="handleLayerSearch"
            />
          </div>
        </div>
        <div class="layer-tree">
          <custom-tree
            icon-class="el-icon-arrow-right"
            ref="layerTree"
            @check-change="checkChange"
            :data="layerData"
            show-checkbox
            :default-expanded-keys="defaultExpandedNode"
            :default-checked-keys="defaultCheckedNode"
            node-key="name"
            :props="{
              children: 'children',
              label: 'name',
            }"
          >
            <span
              :class="[
                'custom-tree-node__label',
                node.disabled && node.isLeaf && 'disabled',
              ]"
              slot-scope="{ node, data }"
            >
              <div v-if="node.level == 1" class="tree-icon">
                <i :class="data.icon" />
              </div>
              {{ node.label }}
              <span v-if="data.hasOwnProperty('count')" class="tree-count"
                >({{ data.count }})</span
              >
            </span>
          </custom-tree>
        </div>
      </div>
    </screen-silde-panel>

    <!-- 右侧边栏 -->
    <screen-silde-panel
      :width="458"
      position="right"
      :height="sideHeight"
      @change="rightSildeChange"
    >
      <div class="side-tab" :style="{ width: '350px' }">
        <primary-tabs
          v-model="tabActive"
          :list="tabList"
          ref="primaryTab"
          @input="changeView"
        />
      </div>
      <div class="side-content">
        <router-view />
      </div>
    </screen-silde-panel>
  </div>
</template>
<script>
import ScreenMap from "./components/ScreenMap";
import { ScreenSildePanel, PrimaryTabs } from "@/components";
import { hydraulic } from "./data/layers";
import { CustomTree } from "@/components";
export default {
  name: "SynthesizeAmap",
  props: {},
  components: {
    ScreenMap,
    ScreenSildePanel,
    CustomTree,
    PrimaryTabs,
  },
  watch: {},
  data() {
    return {
      rightCollapse: true,
      leftCollapse: true,
      sideHeight: 0,
      layerSearch: "",
      layerData: [...hydraulic], //图层树
      defaultExpandedNode: [], //树默认展开结点
      defaultCheckedNode: [], //树默认选中结点
      tabActive: 0,
      tabList: [],
    };
  },
  mounted() {
    const selectType = this.$route.query.type;
    this.changeDefault(selectType); //设置默认图层
    this.handleWindowResize();
    window.addEventListener("resize", this.handleWindowResize);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleWindowResize);
  },
  computed: {},
  methods: {
    leftSildeChange(collapse) {
      this.leftCollapse = collapse;
    },
    rightSildeChange(collapse) {
      this.rightCollapse = collapse;
    },
    handleWindowResize() {
      this.sideHeight = this.$refs.bigScreen.offsetHeight - 70;
    },
    //图层搜索方法
    handleLayerSearch() {},
    //图层树选中改变方法
    checkChange(data, status) {},
    /**
     * 右侧tab切换视图
     */
    changeView(value) {
      const type = this.tabList[value].name;
      this.$router.push({ path: this.tabList[value].url, query: { type } });
    },
    //设置默认图层
    changeDefault(selectType) {
      let url = "";
      let recursionSearch = (arr) => {
        if (arr.length == 0) return;
        for (let i = 0; i < arr.length; i++) {
          if (selectType == arr[i].name) {
            url = arr[i].url;
            break;
          }
          if (arr[i].children) {
            recursionSearch(arr[i].children);
          }
        }
      };
      recursionSearch(hydraulic);
      if (url) {
        this.addSideTab(selectType, url);
      }
      let nodes = this.$refs.layerTree.store.nodesMap;
      if (nodes[selectType].parent) {
        this.defaultCheckedNode = [];
        this.defaultCheckedNode.push(selectType);
        this.defaultExpandedNode = [nodes[selectType].parent.label];
      }
    },
    /**
     * 添加侧边栏
     * @description 此方法会跳转至增加的视图
     */
    addSideTab(layerName, url) {
      const idx = this.tabList.length;
      let obj = {
        value: idx,
        name: layerName,
        url: url,
      };
      this.tabList.push(obj);
      this.$router.push(url);
      this.tabActive = idx;
    },
    /**
     * 移除侧边栏
     */
    removeSideTab(layerName) {
      let idx = this.tabList.findIndex((item) => {
        return item.name == layerName;
      });
      if (idx != -1) {
        this.tabList.splice(idx, 1);
        this.tabActive = this.tabList.length - 1;
        this.$router.push(
          this.tabList[this.tabList.length - 1]
            ? this.tabList[this.tabList.length - 1].url
            : ""
        );
      }
    },
  },
};
</script>
<style lang="scss" scoped>
@import "@/style/bigScreen.scss";
$sidebarHeight: 650px;
.bigScreen {
  position: relative;
  height: 100%;
  width: 100%;
  overflow: hidden;
  .layer-tree {
    overflow: auto;
    // height: 620px;
    height: calc(100% - 12px);
    padding-top: 16px;
    position: relative;
    ::v-deep .el-tree {
      background: rgba(0, 0, 0, 0);
      color: #fff;
      .el-tree-node:focus > .el-tree-node__content {
        background: rgba(0, 0, 0, 0.5);
      }
      .el-checkbox__inner {
        background: rgba(0, 0, 0, 0.1);
        border: 1px solid #308dcc;
      }
      .el-tree-node__content {
        &:hover {
          background: rgba(0, 0, 0, 0.5);
        }
      }
      .el-tree-node {
        margin: 6px 0;
      }
    }
  }
  .sidebar {
    height: 100%;
    padding: 0;
    .catalog-title {
      position: relative;
      left: -12px;
    }
    .catalog-title-primary {
      height: 35px;
      width: 100%;
      background: url(~assets/images/WaterDisastersImage/title-bg_lightless.png)
        no-repeat;
      background-size: 100% 100%;
      background-position-x: 18px;
      align-items: center;
      color: #fff;
      @include flexbox;
      .panel-item__title--light {
        height: 32px;
        width: 40px;
        background: url(~assets/images/WaterDisastersImage/title-bg_light.gif)
          no-repeat;
        background-size: 100% 100%;
      }
    }
  }
  .side-tab {
    height: 64px;
  }
  .side-content {
    height: calc(100% - 64px);
  }
}
.bigScreen_map {
  width: 100%;
  height: 100%;
}
::v-deep.myClass .el-input__suffix {
  margin-top: -4px;
  padding: 0;
}

::v-deep .collapse-panel {
  &.right {
    // width: 400px;
    .sidebar {
      width: 380px;
      .side-tab {
        width: 100%;
      }
    }
  }
}
.screen_map {
  width: 100%;
  height: 100%;
}

.minimizeBtn {
  position: absolute;
  top: 140px;
  left: 350px;
  transition: all 0.5s ease-in-out;
  padding: 4px 12px;
  font-size: 14px;
  color: #4898fb;
  border: 1px solid #4898fb;
  background: transparent;
  border-radius: 4px;
  cursor: pointer;
}

::v-deep .el-select-dropdown.el-popper.page-select-popper {
  border: 0;
  background-color: rgba(0, 0, 0, 0.4);
  .el-select-dropdown__item.selected {
    color: #409eff;
  }
  .el-select-dropdown__item {
    color: #fff;
  }
  .el-select-dropdown__item.hover,
  .el-select-dropdown__item:hover {
    background-color: transparent;
  }
}
::v-deep .el-popper[x-placement^="bottom"].page-select-popper .popper__arrow {
  border-bottom-color: transparent;
}
::v-deep
  .el-popper[x-placement^="bottom"].page-select-popper
  .popper__arrow::after {
  border-bottom-color: rgba(0, 0, 0, 0.4);
}
</style>

<style lang="scss">
.layer-search {
  width: 167px;
  margin-left: auto;
  .el-input__inner {
    width: 167px;
    height: 32px;
    background: none;
    border: 1px solid #ffffff;
    border-radius: 4px;
  }
  .el-input__prefix {
    .el-input__icon {
      line-height: 32px;
    }
  }
}
.layer-tree {
  .custom-tree {
    .custom-tree-node {
      color: #fff;
      .custom-tree-node__content {
        display: flex;
        align-items: center;
        height: 36px;
        padding-right: 24px;
        .custom-tree-node__expand-icon {
          transition: all 0.3s ease-out;
          &.expanded {
            transform: rotate(90deg);
          }
          &.is-leaf {
            display: none;
          }
        }
        .custom-tree-node__label {
          margin-left: 12px;
          display: flex;
          align-items: center;
          font-size: 16px;
          color: #ffffff;
          letter-spacing: 0;
          line-height: 24px;
          font-weight: 500;
          .tree-icon {
            display: inline-block;
            height: 28px;
            width: 28px;
            margin-right: 16px;
          }
          .tree-count {
            margin-left: 3px;
            color: #1cfff5;
          }
          &.disabled {
            color: #909090;
          }
        }
        .el-checkbox {
          .el-checkbox__inner {
            background-color: transparent;
          }
          &.is-checked {
            .el-checkbox__inner {
              border: 1px solid #1cfff5;
              &:after {
                border: 1px solid #1cfff5;
                border-left: 0;
                border-top: 0;
              }
            }
          }
        }
        &.is-top {
          // width: 371px;
          height: 48px;
          background: rgba(7, 157, 254, 0.1);
          border-radius: 2px;
          justify-content: space-between;
          .custom-tree-node__label {
            font-size: 18px;
            color: #ffffff;
            line-height: 24px;
            font-weight: 500;
          }
          .custom-tree-node__expand-icon {
            display: none;
          }
          .el-checkbox {
            order: 2;
          }
          &.has-child {
            .custom-tree-node__expand-icon {
              display: inline-block;
              order: 2;
            }
          }
        }
        &.has-child {
          .el-checkbox {
            display: none;
          }
        }
      }
    }
  }
}
</style>