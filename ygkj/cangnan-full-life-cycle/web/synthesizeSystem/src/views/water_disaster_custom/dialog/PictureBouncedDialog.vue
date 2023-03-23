<template>
  <div :class="['primary-dialog', leftSide == true ? 'project-dialog-wrap' : '']">
    <el-dialog
      top="15vh"
      width="1300px"
      :visible.sync="visible"
      destroy-on-close
      :modal-append-to-body="true"
      :before-close="close"
      :close-on-click-modal="false"
    >
      <div
        class="primary-dialog__title"
        slot="title"
        :style="minimizeStatus ? 'justify-content: space-between;' : ''"
      >
        <p class="primary-dialog__title--label" style="color: #fff">
          {{ mainTitle }}
        </p>
      </div>
      <div class="primary-dialog__content">
        <p class="type-title"></p>
        <div style="height:620px;">
          <el-row>
            <el-col :span="6" style="height: 620px;overflow: auto;">
              <div v-for="(item, index) in srcList" :key="index" class="photograph-list-sty">
                <el-image style="width:300px;height:150px;border-radius: 4px;margin-bottom:5px;margin-right:10px;" :src="item.url" @click="imageList(item.url)" />
              </div>
            </el-col>
            <el-col :span="18" style="height: 620px;overflow: hidden;">
              <div style="margin-left:10px;">
                <el-image style="width:100%;height:620px;border-radius: 4px;" :preview-src-list="srcData" @click="add(imageData)" :src="imageData" />
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "PrimaryDialog",
    props: {
      visible: {
        type: Boolean,
        default: false,
      },
      width: {
        type: String,
        default: "600px",
      },
      mainTitle: {
        type: String,
        default: "图片列表",
      },
      leftSide: {
        type: Boolean,
        default: false,
      },
      minimizeStatus: {
        type: Boolean,
        default: false,
      },
      photoUrl: {
        type: String,
        default: "",
      },
    },
    data() {
      return {
        imageData: 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1631582372,4013670020&fm=11&gp=0.jpg',
        srcList: [],
        srcData: []
      }
    },
    mounted() {
      this.$nextTick(()=> {
        this.srcList = JSON.parse(this.photoUrl);
        this.imageData = this.srcList[0].url;
        this.srcData = this.srcList[0].url;
      })
    },
    methods: {
      add(val) {
          this.srcData = [val];
        },
        // img数据
        imageList(val) {
          this.imageData = val;
        },
        // 关闭
        close() {
          this.$emit("close");
        }
    },
  };
</script>

<style lang="scss" scoped>
.primary-dialog {
  ::v-deep .el-dialog {
    // border-bottom: 1px solid #00c1ff;
    background: transparent;
    .el-dialog__header {
      // background: linear-gradient(to bottom, #35b9e3, #5398f6) left top
      //     no-repeat,
      //   linear-gradient(to bottom, #5398f6, #608aff) left top no-repeat,
      //   linear-gradient(to bottom, #35b9e3, #5398f6) right top no-repeat,
      //   linear-gradient(to bottom, #5398f6, #608aff) right top no-repeat;
      // border-bottom: 1px solid #00c1ff;
      background-size: 18px 2px, 2px 18px;
      padding: 2px;
      // padding-bottom: 0;
      height: 54px;
      .primary-dialog__title {
        // border-bottom: 1px solid #00c1ff;
        // border: 1px solid #00c1ff;
        // box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        // background-image: linear-gradient(180deg, #1f375f 0%, #1b3047 100%);
        // border-bottom: 0;
        height: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-right: 40px;
        .primary-dialog__title--label {
          background: url(~assets/images/WaterDisastersImage/title-bg.png)
            no-repeat;
          // background-size: 100% 100%;
          font-size: 16px;
          color: #00c1ff;
          letter-spacing: 0;
          height: 32px;
          width: 80%;
          margin-left: 16px;
          padding-left: 56px;
          font-weight: bold;
          line-height: 32px;
        }
        .primary-dialog__title--minimize {
          cursor: pointer;
          margin-right: 48px;
        }
      }
      .el-dialog__headerbtn {
        .el-dialog__close {
          color: #00c1ff;
          font-weight: bold;
        }
      }
    }
    .el-dialog__body {
      // background: linear-gradient(to bottom, #5398f6, #35b9e3) left bottom
      //     no-repeat,
      //   linear-gradient(to bottom, #608aff, #5398f6) left bottom no-repeat,
      //   linear-gradient(to bottom, #5398f6, #35b9e3) right bottom no-repeat,
      //   linear-gradient(to bottom, #608aff, #5398f6) right bottom no-repeat;
      background-size: 18px 2px, 2px 18px;
      border-bottom: 0.0625rem solid #00c1ff;
      padding: 2px;
      padding-top: 0;
      .primary-dialog__content {
        height: 350px;
        // overflow-y: auto;
        // overflow-x: hidden;
        // border-bottom: 1px solid #00c1ff;
        box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);
        border-top: 0;
        #floodEchart {
          width: 100%;
          height: 500px;
        }
        .first {
          font-family: PingFangSC-Semibold;
          font-size: 18px;
          color: #ffffff;
          line-height: 16px;
          font-weight: 600;
          padding-left: 25px;
        }
        .second,
        .third {
          font-family: PingFangSC-Regular;
          font-size: 16px;
          color: #ffffff;
          line-height: 16px;
          font-weight: 400;
          padding-left: 25px;
          margin-top: 25px;
        }
      }
    }
  }
  &.project-dialog-wrap {
    ::v-deep .el-dialog {
      margin-left: 20px;
      margin-top: 10vh !important;
    }
  }
}
.dialog-footer {
  position: absolute;
  right: 20px;
  bottom: 20px;
  .el-button--primary.is-plain {
    color: #fff;
    border-color: #0bbbff;
    background-color: rgba($color: #0bbbff, $alpha: 0.1);
  }
}
.peripheral-analysis {
  display: inline-block;
  margin-left: 10px;
  cursor: pointer;
  background: url(~assets/images/peripheral_analysis.png) no-repeat;
  background-size: 100% 100%;
  width: 15px;
  height: 15px;
}
.minimize {
  color: #00c1ff;
  font-weight: bold;
  cursor: pointer;
}
</style>