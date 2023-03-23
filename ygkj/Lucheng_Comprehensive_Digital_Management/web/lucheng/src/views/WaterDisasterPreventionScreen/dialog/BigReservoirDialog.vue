<template>
  <div
    :class="['primary-dialog', leftSide == true ? 'project-dialog-wrap' : '']"
  >
    <el-dialog
      :visible.sync="visible"
      :width="width"
      destroy-on-close
      :modal-append-to-body="true"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
      <div class="primary-dialog__title" slot="title" :style="minimizeStatus ? 'justify-content: space-between;' : ''" >
        <p class="primary-dialog__title--label" style="color: #fff;">{{ mainTitle }} </p>
      </div>
      <div class="primary-dialog__content">
        <el-row>
            <el-col :span="5" style="margin: 10px 0 20px 20px; color:#fff;" v-for="(item,index) in lookDetailsData" :key="index">
              <el-checkbox-group v-model="checkList" :min="0" :max="6">
                <el-checkbox :label="item" :key="index">{{item.projectName}}</el-checkbox>
              </el-checkbox-group>
            </el-col>
        </el-row>
        <!-- 底部按钮 -->
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" size="small" plain @click="handleClose">取 消</el-button>
          <el-button style="background-color: #409EFF;" type="primary" size="small" plain @click="handleConfirm">确 定</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { dispatchApi } from "@/api/dataScreen/jointDispatch/JointDispatch";
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
      default: "大中型水库",
    },
    leftSide: {
      type: Boolean,
      default: false,
    },
    minimizeStatus: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
        checkList: [],
        lookDetailsData: [],
    };
  },
  components: {},
  mounted() {
    this.getLeftList();
    
  },
  methods: {
    handleConfirm() {
      if (this.checkList.length > 6) {
        this.$message({ type: "warning", message: "最多只能选中六条数据！" });
      } else if (this.checkList.length < 1) {
        this.$message({ type: "warning", message: "请至少选中一条数据！" });
      } else {
        this.$emit("sendValue",this.checkList);
      }
    },
    //19座大中型水库
    getLeftList() {
      dispatchApi.mapPoint().then((res) => {
        if (res.code == 0) {
          this.lookDetailsData = res.data;
        }
      });
    },
    handleClose() {
      this.$emit("closeDialog");
    },
  },
}
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
        height: 300px;
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
        .first-line {
            .left-side {
                padding-left:20px;
                color: #4898fb;
                .el-input {
                    width: 195px;
                    border: 1px solid #4898fb;
                    ::v-deep.el-input__icon {
                        line-height: 1.5rem;
                    }
                    input::-webkit-input-placeholder {
                        color: #4898fb;
                    }
                    .el-input__prefix, .el-input__suffix {
                        position: absolute;
                        top: -6px;
                        height: 100%;
                        color: #4898fb;
                        text-align: center;
                    }
                    .el-input__inner {
                        height: 28px;
                        color: #4898fb;
                        background: rgba(0,0,0,0);
                    }
                    .el-input__prefix {
                        left: 0.3125rem;
                        color: #4898fb;
                        top: -6px;
                        -webkit-transition: all .3s;
                        transition: all .3s;
                    }
                }
                b {
                    padding: 0 5px;
                    font-weight: normal;
                }
                .active {
                    color: #fff;
                    padding:7px 10px;
                    border-radius: 5px;
                    background: #3283FF;
                }
            }
        }
      }
    }
  }
  &.project-dialog-wrap{
    ::v-deep .el-dialog{
      margin-left: 20px;
      margin-top: 10vh!important;
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