<!--
 * @Author: dyn
 * @Date: 2021-07-12 09:47:17
 * @LastEditTime: 2021-07-15 18:12:26
 * @LastEditors: Please set LastEditors
 * @Description: 美丽河湖 河长巡查 落点弹窗
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCard.vue
-->
<template>
  <div class="detail-table">
    <el-dialog
      width="60%"
      destroy-on-close
      :visible.sync="visible"
      :before-close="handleClose"
      append-to-body
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      class="primary-dialog"
    >
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">{{ name }}</p>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <!-- 美丽河湖 -->
        <div v-if="type == '美丽河湖'">
          <div class="content-panel-item">
            <div class="title-box">
              <div class="title-panel">
                <i
                  class="icon-diamonds"
                  style="margin-right: 10px; cursor: pointer"
                ></i>
                <span>基础信息</span>
              </div>
              <i class="qsl-ld-dialog-line"></i>
            </div>
            <div class="content-box">
              <div class="row">
                <div class="col">
                  <div class="label">工程状态</div>
                  <div class="value">{{ obj.projectStatus }}</div>
                </div>
                <div class="col" v-if="activeTab != 4">
                  <div class="label">河段名称</div>
                  <div class="value">{{ obj.sectionName }}</div>
                </div>
                <div class="col" v-if="activeTab == 4">
                  <div class="label">池塘名称</div>
                  <div class="value">{{ obj.poudName }}</div>
                </div>
              </div>
              <div class="row" v-if="activeTab != 4">
                <div class="col">
                  <div class="label">起点位置</div>
                  <div class="value">{{ obj.startLocation }}</div>
                </div>
                <div class="col">
                  <div class="label">终点位置</div>
                  <div class="value">{{ obj.endLocation }}</div>
                </div>
              </div>
              <div class="row">
                <div class="col">
                  <div class="label">项目概况</div>
                  <div class="value">{{ obj.projectOverview }}</div>
                </div>
              </div>
            </div>
          </div>
          <div class="content-panel-item">
            <div class="title-box">
              <div class="title-panel">
                <i
                  class="icon-diamonds"
                  style="margin-right: 10px; cursor: pointer"
                ></i>
                <span>投资建设信息</span>
              </div>
              <i class="qsl-ld-dialog-line"></i>
            </div>
            <div class="content-box">
              <div class="row" v-if="activeTab != 2">
                <div class="col">
                  <div class="label">计划投资</div>
                  <div class="value">
                    <span>{{
                      activeTab == 4 || activeTab == 3
                        ? obj.planInvestment
                        : obj.planInvest
                    }}</span>
                    <span class="unit">万元</span>
                  </div>
                </div>
                <div class="col">
                  <div class="label">完成投资</div>
                  <div class="value">
                    <span>{{ obj.completeInvestment }}</span>
                    <span class="unit">万元</span>
                  </div>
                </div>
                <div class="col">
                  <div class="label">比例</div>
                  <div class="value">
                    <span>{{ obj.investmentProportion }}</span>
                    <span class="unit">%</span>
                  </div>
                </div>
              </div>
              <div class="row" v-if="activeTab == 1">
                <div class="col">
                  <div class="label">计划规模</div>
                  <div class="value">
                    <span>{{ obj.planScale }}</span>
                    <span class="unit">km</span>
                  </div>
                </div>
                <div class="col">
                  <div class="label">完成规模</div>
                  <div class="value">
                    <span>{{ obj.completeScale }}</span>
                    <span class="unit">km</span>
                  </div>
                </div>
                <div class="col">
                  <div class="label">比例</div>
                  <div class="value">
                    <span>{{ obj.scaleRatio }}</span>
                    <span class="unit">%</span>
                  </div>
                </div>
              </div>
              <div class="row" v-if="activeTab == 2">
                <div class="col">
                  <div class="label">项目总投资</div>
                  <div class="value">
                    <span>{{ obj.totalProjectInvestment }}</span>
                    <span class="unit">万元</span>
                  </div>
                </div>
                <div class="col">
                  <div class="label">完成总投资</div>
                  <div class="value">
                    <span>{{ obj.completeProjectInvestment }}</span>
                    <span class="unit">万元</span>
                  </div>
                </div>
                <div class="col">
                  <div class="label">比例</div>
                  <div class="value">
                    <span>{{ obj.investmentProportion }}</span>
                    <span class="unit">%</span>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col">
                  <div class="label">总体形象进度</div>
                  <div class="value">
                    <span>{{ obj.overallProgress }}</span>
                    <span class="unit">%</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 河长巡查 -->
        <div v-if="type == '河长巡查'">
          <div class="content-panel-item">
            <div class="title-box">
              <div class="title-panel">
                <i
                  class="icon-diamonds"
                  style="margin-right: 10px; cursor: pointer"
                ></i>
                <span>基本信息</span>
              </div>
              <i class="qsl-ld-dialog-line"></i>
            </div>
            <div class="content-box">
              <div class="row">
                <div class="col">
                  <div class="label">河段名</div>
                  <div class="value">{{ obj.riverName }}</div>
                </div>
                <div class="col">
                  <div class="label">行政区划</div>
                  <div class="value">{{ obj.areaName }}</div>
                </div>
              </div>
              <div class="row">
                <div class="col">
                  <div class="label">发现人</div>
                  <div class="value">{{ obj.realName }}</div>
                </div>
                <div class="col">
                  <div class="label">发现地址</div>
                  <div class="value">{{ obj.address }}</div>
                </div>
              </div>
              <div class="row">
                <div class="col">
                  <div class="label">开始时间</div>
                  <div class="value">{{ obj.createDate }}</div>
                </div>
                <div class="col">
                  <div class="label">截止时间</div>
                  <div class="value">{{ obj.deadline }}</div>
                </div>
              </div>
            </div>
            <div class="content-panel-item">
              <div class="title-box">
                <div class="title-panel">
                  <i
                    class="icon-diamonds"
                    style="margin-right: 10px; cursor: pointer"
                  ></i>
                  <span>问题信息</span>
                </div>
                <i class="qsl-ld-dialog-line"></i>
              </div>
              <div class="content-box">
                <div class="row">
                  <div class="col">
                    <div class="label">问题程度</div>
                    <div class="value">{{ getProblemType(obj.level) }}</div>
                  </div>
                  <div class="col">
                    <div class="label">处置状态</div>
                    <div class="value">{{ getProblemStatu(obj.state) }}</div>
                  </div>
                </div>
                <div class="row">
                  <div class="col">
                    <div class="label">问题详情</div>
                    <div class="value">{{ obj.content }}</div>
                  </div>
                </div>
              </div>
            </div>
            <div class="content-panel-item">
              <div class="title-box">
                <div class="title-panel">
                  <i
                    class="icon-diamonds"
                    style="margin-right: 10px; cursor: pointer"
                  ></i>
                  <span>现场照片</span>
                  <p class="img-num-panel">
                    <span>{{ activeImgInd }}</span
                    >/{{ obj.imgList.length }}
                  </p>
                </div>
                <i class="qsl-ld-dialog-line"></i>
              </div>
              <div class="content-box">
                <el-carousel
                  indicator-position="none"
                  class="img-list"
                  :autoplay="false"
                  @change="imgChange"
                >
                  <el-carousel-item v-for="(item, i) in obj.imgList" :key="i">
                    <img
                      :src="'http://wzhz.wzsl.com:8090/image' + item"
                      class="img-item"
                    />
                  </el-carousel-item>
                </el-carousel>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { weatherApi } from "@/api/weatherApi";

export default {
  name: "beautifulRiverDialog",
  data() {
    return {
      type: "",
      name: "",
      obj: {},
      activeTab: 0,

      activeImgInd: 1,
    };
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  components: {},
  created() {},
  mounted() {},
  computed: {},
  methods: {
    init(val) {
      // console.log(900000, val);
      this.type = val.type;
      this.name = val.name;
      this.obj = val.obj || {};
      this.activeTab = val.activeTab;
      this.obj.imgList = val.obj.picPath.split(",");
      this.obj.imgList = this.obj.imgList.splice(
        0,
        this.obj.imgList.length - 1
      );
    },
    // 图片切换
    imgChange(val) {
      this.activeImgInd = val + 1;
    },
    handleClose() {
      this.$emit("closeDialog");
    },
    // 河道问题程度转换
    getProblemType(val) {
      switch (val) {
        case 0:
          return "轻微";
        case 1:
          return "一般问题";
        case 2:
          return "重大问题";
      }
    },
    // 河道问题状态转换
    getProblemStatu(val) {
      switch (val) {
        case 1:
          return "未受理";
        case 2:
          return "处理中";
        case 3:
          return "已处理待评价";
        case 4:
          return "已处理已评价";
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.primary-dialog {
  .content-panel-item {
    margin-bottom: 25px;
    &:last-child {
      margin-bottom: 0;
    }
  }
  .title-panel {
    color: #fff;
    font-size: 18px;
    display: flex;
    align-items: center;
    .img-num-panel {
      margin-left: 20px;
      span {
        color: #33ffff;
        font-size: 18px;
      }
    }
  }
  .content-box {
    margin-top: 15px;
    .row {
      display: flex;
      justify-content: space-between;
      color: #fff;
      font-size: 16px;
      line-height: 28px;
      margin-bottom: 20px;
      .col {
        display: flex;
        flex: 1;
        margin-right: 20px;
        &:last-child {
          margin-right: 0;
        }
        .label,
        .value {
          border: 1px solid #0093c4;
        }
        .label {
          width: 120px;
          color: #56fefe;
          display: flex;
          align-items: center;
          justify-content: center;
          background-image: linear-gradient(
            90deg,
            rgba(44, 158, 191, 0.3) 3%,
            rgba(41, 147, 180, 0.1) 100%
          );
        }
        .value {
          display: flex;
          justify-content: space-between;
          padding: 10px 15px;
          width: calc(100% - 120px);
          .unit {
            color: #56fefe;
          }
        }
      }
    }
  }

  .img-list {
    ::v-deep .el-carousel__container {
      height: 400px;
    }
    .img-item {
      display: inline-block;
      width: 100%;
      height: 100%;
    }
  }

  .qsl-ld-dialog-line {
    display: inline-block;
    height: 3px;
    width: 100%;
    background-image: url(~assets/images/qsl-ld-dialog-line.png);
    background-size: 100% 100%;
  }

  ::v-deep .el-dialog {
    .el-dialog__header {
      padding: 2px;
      .primary-dialog__title {
        .primary-dialog__title--label {
          width: 80%;
          height: 32px;
          color: #fff;
          font-size: 16px;
          letter-spacing: 0;
          margin-left: 15px;
          font-weight: bold;
          line-height: 35px;
          padding-left: 56px;
        }
      }
    }
    .el-dialog__body {
      // height: 300px;
      padding: 2px;
      padding-top: 0;
      border-bottom: 0.0625rem solid #00c1ff;
      position: relative;
      .primary-dialog__content {
        height: 100%;
        padding: 20px;
      }
    }
  }
}
</style>