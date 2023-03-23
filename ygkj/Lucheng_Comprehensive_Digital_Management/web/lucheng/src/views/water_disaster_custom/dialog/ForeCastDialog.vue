<!--
 * @Author: 降雨预报弹框
 * @Date: 2021-07-13 18:40:45
 * @LastEditTime: 2021-07-16 15:58:29
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\dialog\EmergencySchemeDialog.vue
-->
<template>
  <div class="detail-table emergency-scheme">
    <el-dialog
      top="15vh"
      width="800px"
      destroy-on-close
      :visible.sync="visible"
      :before-close="handleClose"
      append-to-body
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      class="primary-dialog emergency-scheme-dialog"
    >
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">{{ title }}</p>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <div class="real-dialog">
          <!-- 最后更新时间 -->
          <el-row style="width: 100%">
            <el-col :span="15">
              <p style="font-size: 16px; color: #ffffff">未来降雨预报</p>
            </el-col>
            <el-col :span="9">
              <p style="font-size: 16px; color: #ffffff">
                最后更新于：<span>{{ myDate }}</span>
              </p>
            </el-col>
          </el-row>
          <!-- 降雨量（6h） -->
          <p class="rainfallStyle">降雨量（6h）</p>
          <div class="contentBox">
            <el-row style="padding: 17px">
              <el-col :span="8">
                <p style="font-size: 16px; color: #ffffff">未来1h</p>
                <div class="oneh"></div>
                <p style="color: #fff">{{ rainfallData.rainfall_1 }} mm</p>
              </el-col>
              <el-col :span="8" class="centerSty">
                <p style="font-size: 16px; color: #ffffff">未来3h</p>
                <div>
                  <div class="oneh oneh1"></div>
                </div>
                <p style="color: #fff">
                  {{
                    rainfallData.rainfall_3 == null
                      ? "-"
                      : rainfallData.rainfall_3
                  }}
                  mm
                </p>
              </el-col>
              <el-col :span="8">
                <p style="font-size: 16px; color: #ffffff">未来6h</p>
                <div class="oneh oneh2"></div>
                <p style="color: #fff">{{ rainfallData.rainfall_6 }} mm</p>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import { weatherApi } from "@/api/weatherApi/";
export default {
  name: "ForeCastDialog",
  data() {
    return {
      myDate: "",
      title: "",
      rainfallData: {}
    };
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    lat: String,
    lng: String,
  },

  computed: {
    ...mapGetters([]),
  },
  components: {},
  created() { },
  mounted() {
    this.$nextTick(() => {
      this.getMapPointDetail(this.lat, this.lng);
      this.myDate = new Date().format("yyyy-MM-dd hh:mm:ss");
      this.getList(this.lat, this.lng);
    })
  },
  methods: {
    ...mapActions([

    ]),
    handleClose() {
      this.$emit("closeDialog");
    },
    //获取弹框表头信息
    async getMapPointDetail(latitude, longitude) {
      const opt = {
        latitude,
        longitude
      }
      await weatherApi.getMapPointDetail(opt).then(res => {
        this.title = res.data.address;
      })
    },
    //雨量预测（1、3、6h）
    async getList(latitude, longitude) {
      const obj = {
        latitude,
        longitude
      }
      await weatherApi.caculateRainfall(obj).then(res => {
        if (res.code == 0) {
          this.rainfallData = res.data;
        }
      })
    },
  },
};
</script>
<style lang="scss" scoped>
.emergency-scheme-dialog.primary-dialog {
  .module-title {
    color: #1ef8f2;
    padding-left: 4px;
    line-height: 40px;
  }

  ::v-deep .el-radio__input.is-checked .el-radio__inner {
    border-color: #1ef8f2;
    background-color: #1ef8f2;
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
      padding: 2px;
      padding-top: 0;
      border-bottom: 0.0625rem solid #00c1ff;
      position: relative;
      .primary-dialog__content {
        border-top: 0;
        padding: 0 16px;
        box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);
        .real-dialog {
          padding: 16px 0;
          .rainfallStyle {
            padding: 8px 16px;
            color: #fff;
            margin: 12px 0;
            background: rgba($color: #888, $alpha: 0.3);
          }
          .contentBox {
            p {
              margin: 12px 0;
              text-align: center;
            }
          }
          .oneh {
            margin: 13px 0 14px 45%;
            height: 20px;
            width: 20px;
            text-align: center;
            background: url(~assets/images/1h.png) no-repeat center center;
            background-size: 100% 100%;
            color: #fff;
          }
          .oneh1 {
            background: url(~assets/images/3h.png) no-repeat center center;
          }
          .oneh2 {
            background: url(~assets/images/6h.png) no-repeat center center;
          }
          .centerSty {
            border-left: 2px solid #ffffff44;
            border-right: 2px solid #ffffff44;
          }
        }
      }
      .el-radio__label {
        visibility: hidden;
      }
    }
  }
}
</style>