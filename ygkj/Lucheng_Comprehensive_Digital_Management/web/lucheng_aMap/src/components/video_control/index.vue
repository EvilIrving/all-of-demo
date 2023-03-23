<template>
  <div class="video-control">
    <el-dialog
      :visible.sync="visible"
      :fullscreen="true"
      :before-close="handleClose"
      :modal="false"
      :close-on-click-modal="false"
    >
      <div slot="title">
        <span>珊溪水库</span>
      </div>
      <div class="dialog-content clearfix">
        <div class="left-list">
          <primary-subhead text="视频列表"></primary-subhead>
          <div class="search-area">
            <el-input v-model="searchVal" placeholder="请输入内容"></el-input>
          </div>
          <div class="tree-area">
            <el-tree
              :data="treeData"
              :props="defaultProps"
              @node-click="handleNodeClick"
            ></el-tree>
          </div>
        </div>
        <div class="middle-video">
          <div class="video-content">
            <primary-video ref="videoDialog" v-if="visible"></primary-video>
          </div>
          <div class="video-buttons">
            <div class="button-area">
              <i class="icon-carousel"></i>
              <span>设为轮播</span>
            </div>
            <div class="button-area">
              <i class="icon-lock"></i>
              <span>锁定</span>
            </div>
            <div class="button-area">
              <i class="icon-candid"></i>
              <span>抓拍</span>
            </div>
            <div class="button-area">
              <i class="icon-lock"></i>
              <span>锁定</span>
            </div>
            <div class="button-area">
              <i class="icon-one"></i>
              <span>1*1</span>
            </div>
            <div class="button-area">
              <i class="icon-nine"></i>
              <span>3*3</span>
            </div>
            <div class="button-area">
              <i class="icon-sixteen"></i>
              <span>4*4</span>
            </div>
          </div>
        </div>
        <div class="right-control">
          <primary-subhead text="云镜控制"></primary-subhead>
          <div class="control-box">
            <i class="center-button"></i>
            <i class="up-button icon-button-up"></i>
            <i class="down-button icon-button-down"></i>
            <i class="left-button icon-button-left"></i>
            <i class="right-button icon-button-right"></i>
          </div>
          <div class="func-area">
            <div class="slide-item">
              <p><span>调焦</span></p>
              <el-slider v-model="focusValue"></el-slider>
            </div>
            <div class="slide-item">
              <p><span>聚焦</span></p>
              <el-slider v-model="focusValue1"></el-slider>
            </div>
            <div class="slide-item">
              <p><span>光圈</span></p>
              <el-slider v-model="lightCircle"></el-slider>
            </div>
            <div class="slide-item">
              <p><span>速度</span></p>
              <el-slider v-model="speed"></el-slider>
            </div>
            <div class="slide-item">
              <p><span>亮度</span></p>
              <el-slider v-model="lightning"></el-slider>
            </div>
            <div class="slide-item">
              <p><span>对比度</span></p>
              <el-slider v-model="contrast"></el-slider>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { PrimarySubhead, PrimaryVideo } from "@/components";
export default {
  name: "VideoControl",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      searchVal: "",
      treeData: [
        {
          label: "珊溪上游",
          children: [
            {
              label: "水库上游",
              children: [
                {
                  label: "视频一(在线)",
                },
              ],
            },
          ],
        },
        {
          label: "珊溪下游",
          children: [
            {
              label: "水闸上游",
              children: [
                {
                  label: "视频一(在线)",
                },
              ],
            },
          ],
        },
      ],
      defaultProps: {
        children: "children",
        label: "label",
      },
      focusValue: 10,
      focusValue1: 0,
      contrast: 0,
      lightning: 0,
      speed: 0,
      lightCircle: 0,
    };
  },
  components: {
    PrimarySubhead,
    PrimaryVideo,
  },
  watch: {},
  mounted() {},
  methods: {
    handleClose() {
      this.$emit("handleClose");
    },
    handleNodeClick(data) {
      console.log(data);
    },
  },
};
</script>

<style scoped lang="scss">
@import "@/style/theme.scss";
.video-control {
  ::v-deep .el-dialog {
    padding: 12px 24px;
    &.is-fullscreen {
      background: rgba(6, 23, 20, 0.8);
    }
    .el-dialog__header {
      font-size: 24px;
    }
  }
  .dialog-content {
    width: 100%;
    height: 100%;
    padding: 12px;
    ::v-deep .subhead-wrap {
      .subhead {
        border-left: 0.25rem solid #00877a;
        color: #00877a;
        font-size: 22px;
        height: auto;
      }
    }
    .left-list {
      @include flexbox;
      @include flexflow(column, wrap);
      width: 320px;
      height: 100%;
      float: left;
      opacity: 0.8;
      background: #f7f8f8;
      border-radius: 20px;
      padding: 20px;
      .search-area {
        padding: 16px 0;
        ::v-deep .el-input {
          .el-input__inner {
            background: rgba(255, 255, 255, 0.4);
            border: 1px solid #e5e5e5;
            border-radius: 20px;
            color: #95969b;
          }
        }
      }
      .tree-area {
        @include flex(1);
        ::v-deep .el-tree {
          .el-tree-node__content {
            height: 40px;
          }
        }
      }
    }
    .middle-video {
      width: calc(100% - 704px);
      height: 100%;
      margin: 0 32px;
      float: left;
      opacity: 0.8;
      background: #f7f8f8;
      border-radius: 20px;
      @include flexbox;
      @include flexflow(column, wrap);
      .video-content {
        @include flex(1);
      }
      .video-buttons {
        height: 50px;
        width: 100%;
        @include flexbox;
        @include flexJC(center);
        @include flexAC;
        .button-area {
          @include flexbox;
          @include flexAC;
          margin-right: 10px;
          cursor: pointer;
          span {
            padding: 0 6px;
          }
        }
      }
    }
    .right-control {
      width: 320px;
      height: 100%;
      float: left;
      opacity: 0.8;
      background: #f7f8f8;
      border-radius: 20px;
      padding: 20px;
      @include flexbox;
      @include flexflow(column, wrap);

      .control-box {
        width: 240px;
        height: 240px;
        background: $primarySliderColor;
        border-radius: 50%;
        margin: 40px auto;
        position: relative;
        i {
          display: inline-block;

          position: absolute;
          cursor: pointer;
          &.center-button {
            width: 46px;
            height: 46px;
            border-radius: 50%;
            background: #fff;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
          }
          &.up-button {
            width: 30px;
            height: 20px;
            left: 50%;
            transform: translateX(-50%);
            top: 20px;
          }
          &.down-button {
            width: 30px;
            height: 20px;
            left: 50%;
            transform: translateX(-50%);
            bottom: 20px;
          }
          &.left-button {
            width: 20px;
            height: 30px;
            top: 50%;
            transform: translateY(-50%);
            left: 20px;
          }
          &.right-button {
            width: 20px;
            height: 30px;
            top: 50%;
            transform: translateY(-50%);
            right: 20px;
          }
        }
      }
      .func-area {
        @include flex(1);
        .slide-item {
          margin-bottom: 20px;
          ::v-deep .el-slider {
            .el-slider__bar {
              background-color: $primarySliderColor;
            }
            .el-slider__button {
              border: 0.125rem solid $primarySliderColor;
              background: $primarySliderColor;
            }
          }
        }
      }
    }
  }
}
</style>