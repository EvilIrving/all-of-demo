<template>
  <div class="people-box">
    <div class="people-left">
      <div class="item_box_title">
        <p>视频监控</p>
      </div>
      <div class="list-box">
        <div class="tree-list">
          <el-tree ref="popularTree" class="tree-box-list" node-key="id" @node-click="handleNodeClick" :data="videoList" accordion :current-node-key="currentLivingId" :default-expanded-keys="[1,2,3]" :default-checked-keys="[3]" :props="defaultProps" :highlight-current="true">
            <span class="custom-tree-node" slot-scope="{ node }">
              <div class="tree-box">
                <div class="tree-name" :title="node.label">
                  {{ node.label }}
                </div>
              </div>
            </span>
          </el-tree>
        </div>
      </div>
    </div>
    <div class="people-right">
      <primary-video v-if="currentCameraReload" ref="videoDialog" :code="videoData.adcd" />
    </div>
  </div>
</template>

<script>
import PrimaryVideo from "@/components/VideoPlayer.vue";
import { getHikvisionTree } from "@/api/seawallPrevent";
export default {
  data() {
    return {
      currentCameraReload: false,
      currentLivingId: '1aad3b3481b44b38ae4613e68fd3bc8c', 
      videoList: [],
      defaultProps: {
        children: "subset",
        label: "adnm",
      },
      videoData: {},
    };
  },
  components: { PrimaryVideo },
  mounted() {
    this.getTree();
    // this.getVideo();
  },
  methods: {
    // 部门list 左侧树状图请求接口
    async getTree() {
      await getHikvisionTree.boatList().then((res) => {
        if (res.code == 0) {
          this.videoList = [res.data];
          this.currentLivingId = res.data.subset[0].adcd;
          this.$nextTick(function () {
            this.$refs.popularTree.setCurrentKey(this.currentLivingId);
          });
        }
      });
    },
    // 点击摄像头
    handleNodeClick(row) {
      this.currentLivingId = row.adcd
      if (!row.level) {
        this.videoData = row
        this.currentCameraReload = false
        setTimeout(() => {
          this.currentCameraReload = true;
        }, 200);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.people-box {
  width: 100%;
  height: calc(100% - 85px);
  display: flex;
  justify-content: space-between;
  box-sizing: border-box;
  .people-left {
    @include contentBox();
    width: 260px;
    height: 100%;
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
    width: calc(100% - 280px);
    height: 98%;
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
  height: 220x;
  padding-right: 18px;
}
.tree-list
  .el-tree.el-tree--highlight-current
  .el-tree-node.is-current
  > .el-tree-node__content {
  background: url("../../assets/images/tree-active-icon.png") no-repeat;
  background-size: 100% 100%;
}
</style>
