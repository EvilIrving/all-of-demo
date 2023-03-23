<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-22 16:19:55
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\repair\defectTreatment\components\auditInfo.vue
-->
<template>
  <div class="dis-box">
    <div class="wrap">
      <div class="wrap_box_title">
        <p>审批意见</p>
      </div>
      <div>
        <el-input
          type="textarea"
          :rows="26"
          class="textarea-box"
          placeholder="请输入内容"
          v-model="formData.opinion"
          :readonly="formData.status != 5"
          resize="none"
        >
        </el-input>
      </div>
    </div>
    <div class="wrap">
      <el-button class="btn-wrap" size="small" @click="goback">返回</el-button>
      <el-button class="btn-wrap" v-if="formData.status == 5" size="small" type="primary" @click="submit(2)"
        >通过</el-button
      >
      <el-button class="btn-wrap" v-if="formData.status == 5" size="small" type="danger" @click="submit(3)"
        >驳回</el-button
      >
    </div>
  </div>
</template>

<script>
import { cloneObj } from "@/utils/utils.js";
import { hiddenDanger } from "@/api/projectUnitDetail";
export default {
  props: {
    infoDetail: Object,
  },
  data() {
    return { 
      formData: {}, 
      submitType: true
    };
  },
  mounted() {
    console.log(this.infoDetail)
    this.formData = cloneObj(this.infoDetail);
  },
  methods: {
    submit(type) {
      if (!this.submitType) {
        return false;
      }
      this.submitType = false;
      let formData = cloneObj(this.formData);
      formData.status = type;
      hiddenDanger
        .submitProgressInfo(formData)
        .then((res) => {
          if (res.code == 0) {
            if (type == 2) {
              this.$message.success("审核通过成功~");
            } else {
              this.$message.success("审核驳回成功~");
            }
            this.goback();
          } else {
            this.$message.error(res.message);
          }
          this.submitType = true;
        })
        .catch((err) => {
          this.$message.error(err);
          this.submitType = true;
        });
    },
    goback() {
      this.$emit("goInfo");
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  @include contentBox();
  padding-top: 16px;
  .wrap_box_title {
    position: relative;
    line-height: 25px;
    padding: 0 10px 16px 24px;
    border-bottom: 1px solid #eeeeee;
    &::after {
      content: "";
      position: absolute;
      width: 4px;
      height: 20px;
      background: #1492ff;
      left: 10px;
      top: 2px;
    }
    > p {
      font-size: 18px;
      font-weight: bold;
      color: #333333;
    }
  }
  > div:nth-child(2) {
    padding: 24px 8px 8px;
    .textarea-box {
      ::v-deep .el-textarea__inner {
        background-color: rgba(216, 216, 216, 0.19);
        border: none;
      }
    }
  }
}
.dis-box {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  > div:nth-child(1) {
    min-height: 640px;
  }
  > div:nth-child(2) {
    padding: 19px 0;
    text-align: center;
    margin-top: 20px;
    .btn-wrap {
      width: 84px;
    }
  }
}
</style>