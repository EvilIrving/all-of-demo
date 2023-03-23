<template>
  <div>
    <el-dialog
      :modal="false"
      :visible.sync="visible"
      width="1000px"
      top="10vh"
      :before-close="handleClose"
      :append-to-body="false"
      :fullscreen="false"
      :close-on-click-modal="false"
      class="primary-dialog"
    >
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">{{ title }}</p>
      </div>
      <div
        class="primary-dialog__content"
        style="overflow-x: clip; overflow-y: auto; height: 620px"
      >
        <div class="info-box">
          <div class="info-wrap">
            <div v-for="(item, index) in arr" :key="index">
              <p class="mt16">{{ item.label }}</p>
              <div class="mt16 info-text">{{ form[item.prop] }}</div>
            </div>
            <div class="file-wrap">
              <p class="mt16">
                方案报告书：
                <span
                  v-if="!reportArr || reportArr.length == 0"
                  class="temp-no"
                >
                  暂无
                </span>
              </p>
              <template v-if="reportArr && reportArr.length > 0">
                <div
                  v-for="(item, index) in reportArr"
                  :key="index"
                  class="file-item"
                >
                  <i class="el-icon-document"></i>
                  <a :href="item.url" target="_blank">{{ item.name }}</a>
                </div>
              </template>
            </div>
            <div class="file-wrap">
              <p class="mt16">
                批复文件：
                <span v-if="!replyArr || replyArr.length == 0" class="temp-no">
                  暂无
                </span>
              </p>
              <template v-if="replyArr && replyArr.length > 0">
                <div
                  v-for="(item, index) in replyArr"
                  :key="index"
                  class="file-item"
                >
                  <i class="el-icon-document"></i>
                  <a :href="item.url" target="_blank">{{ item.name }}</a>
                </div>
              </template>
            </div>
            <div class="file-wrap">
              <p class="mt16">
                工程特性表：
                <span
                  v-if="!characterArr || characterArr.length == 0"
                  class="temp-no"
                >
                  暂无
                </span>
              </p>
              <template v-if="characterArr && characterArr.length > 0">
                <div
                  v-for="(item, index) in characterArr"
                  :key="index"
                  class="file-item"
                >
                  <i class="el-icon-document"></i>
                  <a :href="item.url" target="_blank">{{ item.name }}</a>
                </div>
              </template>
            </div>
            <div class="file-wrap">
              <p class="mt16">
                项目图片：
                <span v-if="!imgArr || imgArr.length == 0" class="temp-no">
                  暂无
                </span>
              </p>
              <div class="img-box" v-if="imgArr && imgArr.length > 0">
                <img
                  v-for="(item, index) in imgArr"
                  :key="index"
                  :src="item"
                  alt=""
                  @click="handlePreview(item)"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";

export default {
  name: "ProductionProjDialog",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      id: "",
      title: "",
      arr: [],
      form: {
        loss: "",
        name: "",
      },
      reportArr: [], // 1方案报告书
      replyArr: [], // 2批复文件
      characterArr: [], // 3工程特性表
      imgArr: [], // 项目图片
    };
  },
  components: {},
  mounted() {},
  methods: {
    init(val) {
      // console.log("init val: ", val);
      this.title = val.name;
      this.id = val.id || val.obj.id;
      this.$nextTick(() => {
        this.getInfo();
      });
    },
    // 获取信息
    async getInfo() {
      this.setColumn();
      await hydraulicApi
        .getProjs({
          fetchAll: true,
          id: this.id,
        })
        .then((res) => {
          if (res.code == 0) {
            let info = res.data.list[0];
            // 项目基本信息
            this.form = info;
            if (info.imgIds) {
              this.imgArr =
                info.imgIds && info.imgIds.length > 0
                  ? info.imgIds.split(",")
                  : [];
              const fileArr = info.soilErosionKeeps;
              if (fileArr && fileArr.length > 0) {
                fileArr.map((item) => {
                  const index = item.fileUrl.lastIndexOf("/");
                  let obj = {
                    url: item.fileUrl,
                    name: item.fileUrl.substring(index + 1),
                  };
                  if (item.fileType == 1) {
                    // 1方案报告书
                    this.reportArr.push(obj);
                  } else if (item.fileType == 2) {
                    // 2批复文件
                    this.replyArr.push(obj);
                  } else if (item.fileType == 3) {
                    // 3工程特性表
                    this.characterArr.push(obj);
                  }
                });
              }
            }
          }
        });
    },
    setColumn() {
      this.arr = [
        {
          label: "项目名称：",
          prop: "projName",
        },
        {
          label: "土地总面积（km²）：",
          prop: "landArea",
        },
        {
          label: "所属行政区划：",
          prop: "adnm",
        },
        {
          label: "水土流失面积（km²）：",
          prop: "soilErosionArea",
        },
        {
          label: "涉及乡镇：",
          prop: "involveTown",
        },
        {
          label: "年土壤侵蚀量（万t）：",
          prop: "erosion",
        },
        {
          label: "涉及自然村组：",
          prop: "involveVillage",
        },
        {
          label: "治理面积：",
          prop: "governArea",
        },
        {
          label: "实施年度：",
          prop: "implementYear",
        },

        {
          label: "审查时间：",
          prop: "checkTime",
        },
        {
          label: "批复单位：",
          prop: "approvalUnit",
        },
      ];
    },
    handleClose() {
      this.$emit("handleClose");
    },
  },
};
</script>

<style lang="scss">
.primary-dialog {
  .el-dialog {
    background-image: linear-gradient(
      180deg,
      rgba(8, 42, 53, 1) 2%,
      rgba(5, 14, 48, 1) 100%
    );
    .el-dialog__header {
      height: 54px;
      padding: 18px;
      border: 1px solid #00c1ff;
      // box-shadow: inset 0 4px 30px 0 #2351ef40;
      background: transparent;
      border-bottom: 0;
      margin: 2px 2px 0;
      .primary-dialog__title {
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
          color: #fff;
          letter-spacing: 0;
          height: 32px;
          width: 80%;
          // margin-left: 16px;
          padding-left: 56px;
          font-weight: bold;
          line-height: 32px;
        }
      }
    }
    .el-dialog__body {
      min-height: 100px;
      padding: 0 18px;
      background: transparent;
      border: 1px solid #00c1ff;
      border-top: 0;
      border-bottom: 0;
      margin: 0 2px;
      .info-box {
        width: 100%;
        height: 100%;
        .info-wrap {
          width: 100%;
          @include flexbox;
          flex-wrap: wrap;
          margin-top: 16px;
          > div:not(.file-wrap) {
            width: calc(50% - 8px);
            &:nth-child(odd) {
              margin-right: 16px;
            }
          }
        }
        .mt16 {
          margin-bottom: 16px;
          color: #fff;
        }
        .info-text {
          @include flexbox;
          @include flexJC(space-between);
          @include flexAC;
          width: 100%;
          height: 44px;
          background: rgba(0, 0, 0, 0.4);
          border-radius: 4px;
          border-radius: 4px;
          color: #fff;
          border: 0;
          padding: 0 16px;
        }
        .file-wrap {
          width: 100%;
          .file-item {
            line-height: 20px;
            color: #fff;
            margin-bottom: 16px;
            .el-icon-document {
              margin-right: 4px;
            }
            > a {
              color: #fff;
            }
          }
        }
        .temp-no {
          color: #fff;
        }
      }
      .img-box {
        width: 100%;
        display: flex;
        padding-bottom: 16px;
        > img {
          height: 100px;
          width: 100px;
          margin-right: 10px;
          cursor: pointer;
        }
      }
    }
    .el-dialog__footer {
      padding: 0 18px 24px;
      background: transparent;
      border: 1px solid #00c1ff;
      border-top: 0;
      margin: 0 2px 4px;
      .dialog-footer {
        .el-button {
          padding: 6px 16px;
          &.cancel {
            color: #1ef8f2;
            border: 1px solid #1ef8f2;
            background-color: transparent;
            border-radius: 4px;
          }
          &.confirm {
            color: #fff;
            border: 0;
            background-image: linear-gradient(
              180deg,
              #36c0e4 20%,
              #0639ff 100%
            );
          }
        }
      }
    }
    &:after {
      content: "";
      position: absolute;
      height: calc(100% + 4px);
      width: 100%;
      top: -2px;
      left: 0;
      background: linear-gradient(to bottom, #35b9e3, #5398f6) left top
          no-repeat,
        linear-gradient(to bottom, #5398f6, #608aff) left top no-repeat,
        linear-gradient(to bottom, #35b9e3, #5398f6) right top no-repeat,
        linear-gradient(to bottom, #5398f6, #608aff) right top no-repeat,
        linear-gradient(to bottom, #5398f6, #35b9e3) left bottom no-repeat,
        linear-gradient(to bottom, #608aff, #5398f6) left bottom no-repeat,
        linear-gradient(to bottom, #5398f6, #35b9e3) right bottom no-repeat,
        linear-gradient(to bottom, #608aff, #5398f6) right bottom no-repeat;
      /*设置大小*/
      background-size: 18px 2px, 2px 18px;
      z-index: -1;
    }
  }
}
</style>
