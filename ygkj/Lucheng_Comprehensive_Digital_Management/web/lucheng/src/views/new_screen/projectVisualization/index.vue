<template>
  <div class="visualization-box">
    <div class="visualization">
      <div class="tree-box">
        <div class="tree-title">视频监控</div>
        <div class="tree-list">
          <el-input class="tree-input" v-model="filterText" size="small" placeholder="请输入"></el-input>
          <el-tree
            ref="tree"
            class="tree-node-list"
            :data="videoList"
            node-key="adcd"
            accordion
            :props="defaultProps"
            :highlight-current="true"
            @node-click="handleNodeClick"
            :filter-node-method="filterNode"
          ></el-tree>
        </div>
      </div>
      <div class="video-box">
        <div class="no-primary-video" v-if="!videoData.adcd">
          <img src="@/assets/new_images/visualization/video-icon.png" alt="">
        </div>
        <div class="primary-video" v-else>
          <primary-video v-if="currentCameraReload" ref="videoDialog" :code="videoData.adcd" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getHikvisionTree } from '@/api/bigScreenPart';
import { PrimaryVideo } from '@/components';
export default {
  watch: {
    filterText(val) {
      console.log(val);
      this.$refs.tree.filter(val);
    }
  },
  components: { PrimaryVideo },
  data() {
    return {
      filterText: '',
      currentLivingId: '',
      currentCameraReload: false,
      videoList: [],
      defaultProps: {
        children: 'subset',
        label: 'adnm'
      },
      videoData: {}
    };
  },
  mounted() {
    this.getTree();
  },
  methods: {
    filterNode(value, data) {
      if (!value) return true;
      console.log(data, value);
      return data.adnm.indexOf(value) !== -1;
    },
    // 部门list 左侧树状图请求接口
    async getTree() {
      await getHikvisionTree.boatList().then((res) => {
        if (res.code == 0) {
          this.videoList = [res.data];
          // this.currentLivingId = res.data.subset[0].adcd;
          // this.$nextTick(function () {
          //   this.$refs.tree.setCurrentKey(this.currentLivingId);
          // });
        }
      });
    },
    // 点击摄像头
    handleNodeClick(row) {
      if (!row.level) {
        this.videoData = row;
        this.currentCameraReload = false;
        setTimeout(() => {
          this.currentCameraReload = true;
        }, 200);
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.visualization-box {
  width: 100%;
  height: 100%;
  background-image: url(~assets/new_images/kuang.png);
  background-size: 100% 100%;
  background-repeat: no-repeat;
  box-sizing: border-box;
  padding: 2px;
  .visualization {
    width: 100%;
    height: calc(100% - 75px);
    display: flex;
    justify-content: space-between;
    padding: 16px 16px 0 16px;
    .tree-box {
      width: 364px;
      height: 100%;
      background-image: url(~assets/new_images/visualization/tree-bg.png);
      background-size: 100% 100%;
      background-repeat: no-repeat;
      padding-left: 13px;
      .tree-title {
        font-size: 24px;
        font-weight: 500;
        color: #ffffff;
        padding: 18px 0 14px 8px;
        border-bottom: 2px solid #133047;
      }
      .tree-list {
        padding: 9px 15px 0 3px;
        height: calc(100% - 65px);

        .tree-input {
          margin-bottom: 13px;
          box-shadow: 0px 0px 4px 0px rgba(10, 42, 97, 0.2);
          border-radius: 2px;
          border: 1px solid #155bd4;
        }
        .tree-node-list {
          background: transparent;
          color: #ffffff;
          font-size: 20px;
          height: calc(100% - 50px);
          overflow-y: auto;
        }
      }
    }
    .video-box {
      width: calc(100% - 380px);
      height: 100%;
      box-shadow: inset 0px 6px 45px 0px rgba(35, 81, 239, 0.25);
      border: 4px solid #0454a7;
      .primary-video{
        width: 100%;
        height: calc(100% - 20px);
      }
      .no-primary-video{
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        img{
          width: 144px;
        }
      }
    }
  }
}
</style>

<style lang="scss">
.tree-list {
  .tree-input {
    .el-input__inner {
      text-align: left;
    }
  }
  .tree-node-list {
    .el-tree-node:focus > .el-tree-node__content {
      background-color: rgba(0, 186, 255, 0.2);
      color: #23e3fb;
    }
    .el-tree-node.is-current > .el-tree-node__content {
      background-color: rgba(0, 186, 255, 0.2);
      color: #23e3fb;
    }
    .el-tree-node__content {
      margin-bottom: 10px;
      height: 28px;
      line-height: 28px;
      &:hover {
        background-color: rgba(0, 186, 255, 0.2);
        color: #23e3fb;
      }
      .el-tree-node__expand-icon {
        font-size: 20px;
      }
      .el-tree-node__label {
        font-size: 20px;
      }
    }
  }
}
</style>