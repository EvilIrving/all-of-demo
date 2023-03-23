<!--
 * @Author: wanxin
 * @LastEditTime: 2022-10-24 15:34:36
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\occupyApprove\approval-project\components\ApprovalProjectFrom.vue
-->
<template>
  <div class="form_wrap">
    <el-form
      ref="form"
      label-position="right"
      :model="form"
      label-width="150px"
    >
      <!-- 涉河水域、占用水域面积㎡、涉河时长 经纬度 -->
      <el-row>
        <el-col :span="11">
          <el-form-item label="涉河水域：" prop="wadingWaters">
            <el-input
              :disabled="isLook"
              v-model="form.wadingWaters"
              placeholder="请输入"
            >
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="占用水域面积㎡：" prop="occupiedWaterArea">
            <el-input
              :disabled="isLook"
              v-model="form.occupiedWaterArea"
              placeholder="请输入"
            >
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <!--        <el-col :span="11">
          <el-form-item label="经度：" prop="prLat">
            <el-input :disabled='isLook' v-model="form.prLat" placeholder="请输入" style="width: 300px">
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="纬度：" prop="prLong">
            <el-input :disabled='isLook' v-model="form.prLong" placeholder="请输入" style="width: 300px">
            </el-input>
          </el-form-item>
        </el-col>-->
        <el-col :span="11">
          <el-form-item label="经度" prop="prLong">
            <el-input
              v-model="form.prLong"
              readonly
              placeholder="请选择"
              :disabled="isLook"
            >
              <i
                slot="suffix"
                class="el-icon-location-outline cp"
                @click="openMap"
              ></i>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="纬度" prop="prLat">
            <el-input
              v-model="form.prLat"
              placeholder="请选择"
              :disabled="isLook"
            >
              <i
                slot="suffix"
                class="el-icon-location-outline cp"
                @click="openMap"
              ></i>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="涉河时长：" prop="wadingTime">
            <el-input
              :disabled="isLook"
              v-model="form.wadingTime"
              placeholder="请输入"
            >
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <div class="submit-box">
        <el-button
          @click="
            () => {
              this.$emit('close');
            }
          "
          >取消</el-button
        >
        <el-button type="primary" v-if="!isLook" @click="submitForm('form')"
          >保存</el-button
        >
      </div>
    </el-form>

    <el-dialog
      class="dialog_box map_dialog_box"
      title="地图选点"
      :visible="selectMapVisible"
      :close-on-click-modal="false"
      append-to-body
      @close="closeMap"
      width="1200px"
      top="15vh"
    >
      <div style="height: 600px">
        <select-map
          v-if="selectMapVisible"
          :pointLng="pointArr[0]"
          :pointLat="pointArr[1]"
          isDraw
          @selectPoint="saveData"
          @closeMapSelectDialog="selectMapVisible = false"
        ></select-map>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { approvalProject } from "@/api/projectSupervision";
import SelectMap from "@/components/mapSelectPoint";

export default {
  name: "ApprovalProjectFrom",
  props: {
    id: {
      type: String,
      default: "",
    },
    isLook: {
      type: Boolean,
      default: false,
    },
  },
  components: { SelectMap },
  create() {},
  mounted() {
    if (this.id) {
      this.getList();
    }
  },
  data() {
    return {
      disabled: false,
      form: {
        projectCode: "",
        wadingWaters: " ",
        occupiedWaterArea: " ",
        prLong: " ",
        prLat: " ",
        wadingTime: " ",
      },
      submitFlag: true,

      selectMapVisible: false,
      pointArr: [],
    };
  },
  methods: {
    async getList() {
      await approvalProject
        .list({
          projectCode: this.id,
        })
        .then((res) => {
          if (res.code === 0) {
            // const {
            //   projectCode,
            //   wadingWaters,
            //   occupiedWaterArea,
            //   prLong,
            //   prLat,
            //   wadingTime,
            // }
            let data = res.data.list[0];
            this.form = {
              projectCode: data.projectCode,
              wadingWaters: data.wadingWaters,
              occupiedWaterArea: data.occupiedWaterArea,
              prLong: data.prLong,
              prLat: data.prLat,
              wadingTime: data.wadingTime,
            };
            this.pointArr = [this.form.prLong, this.form.prLat];
          }
        });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.submitFlag) {
            return false;
          }
          this.submitFlag = false;
          this.form.projectCode = this.id;
          approvalProject
            .edit(this.form)
            .then((res) => {
              console.log(this.form, "form");

              if (res.code == 0) {
                this.$message.success("编辑成功");
                this.$emit("close");
              } else {
                this.$message.error({ message: res.message || "编辑失败" });
              }
              this.submitFlag = true;
            })
            .catch((err) => {
              this.submitFlag = true;
              this.$message.error(err);
            });
        } else {
          return false;
        }
      });
    },
    openMap() {
      if (this.isLook) {
        return false;
      }
      this.selectMapVisible = true;
    },
    closeMap() {
      this.selectMapVisible = false;
    },
    saveData(prLong, prLat) {
      if (prLong && prLat) {
        this.form.prLong = prLong;
        this.form.prLat = prLat;
        this.pointArr = [this.form.prLong, this.form.prLat];
      } else {
        this.form.prLong = "";
        this.form.prLat = "";
      }
      this.selectMapVisible = false;
    },
  },
};
</script>

<style scoped lang="scss">
.form_wrap {
  .training-form-box {
    display: flex;
    width: 100%;
    justify-content: space-between;
    > div {
      width: 48%;
    }
  }
  .submit-box {
    text-align: right;
    margin-right: 50px;
  }
}
.img_box {
  cursor: pointer;
  float: left;

  @include flexbox();
  flex-direction: row;
  margin-right: 3px;
  img {
    width: 80px;
    height: 80px;
    margin: 0 5px;
  }

  > div {
    width: 80px;
    height: 80px;
    margin-right: 3px;
    padding: 2px;
    position: relative;
    @include flexbox();
    @include flexAI(center);
    @include flexJC(center);

    .active {
      display: none;
    }
  }
}
</style>

<style lang="scss">
.avatar-uploader {
  width: 90%;
  @include flexbox();
  .el-upload {
    height: 80px;
    width: 80px;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    @include flexbox();
    @include flexJC(center);
    @include flexAI(center);
    .img_box {
      .el-progress--without-text .el-progress-bar {
        display: block;
      }
      .el-progress-bar {
        width: 60%;
        margin-top: 30px;
      }
    }
  }
}
</style>