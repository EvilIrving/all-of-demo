<template>
  <div class="project-construction">
    <div class="form-box">
      <el-form
        ref="form"
        label-position="right"
        :model="form"
        label-width="120px"
      >
        <el-row>
          <el-col :span="12">
            <el-form-item label="项目编号：" prop="wadingWaters">
              <el-input
                :disabled="isLook"
                v-model="form.projectCode"
                placeholder="请输入"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总金额：" prop="occupiedWaterArea">
              <el-input
                :disabled="isLook"
                v-model="form.totalMoney"
                placeholder="请输入"
                type="number"
                min="0"
              >
                <template slot="append">万元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="资金来源：" prop="wadingTime">
              <el-input
                :disabled="isLook"
                v-model="form.fundsSourcr"
                placeholder="请输入"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用途：" prop="wadingTime">
              <el-input
                :disabled="isLook"
                v-model="form.purpose"
                placeholder="请输入"
              >
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="已用金额：" prop="wadingTime">
              <el-input
                :disabled="isLook"
                v-model="form.amountUserd"
                placeholder="请输入"
                type="number"
                min="0"
              >
                <template slot="append">万元</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="剩余金额：" prop="wadingTime">
              <el-input
                :disabled="isLook"
                v-model="form.amountRemain"
                placeholder="请输入"
                type="number"
                min="0"
              >
                <template slot="append">万元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="维护人：" prop="wadingTime">
              <el-input
                :disabled="isLook"
                v-model="form.maintainer"
                placeholder="请输入"
              >
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="submit-box">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" v-if="!isLook" @click="submitForm('form')">保存</el-button>
    </div>
  </div>
</template>

<script>
import { approvalProject } from "@/api/projectSupervision";

export default {
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
  data() {
    return {
      form: {
        projectCode: "",
        planName: "",
        totalMoney: "",
        fundsSourcr: "",
        purpose: "",
        amountUserd: "",
        amountRemain: "",
        maintainer: "",
      },
      submitFlag: true
    };
  },
  mounted(){
    this.queryFinance()
  },
  methods: {
    close() {
      this.$emit("close");
    },
    async queryFinance(){
      let res = await approvalProject.queryFinance({
        projectBaseCode: this.id
      })
           if (res.code === 0) {
        if (res.data.length > 0) {
          this.form = res.data[0];
        }
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.submitFlag) {
            return false;
          }
          this.submitFlag = false;
          this.form.projectBaseCode = this.id;
          approvalProject
            .editFinance(this.form)
            .then((res) => {
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
  },
};
</script>

<style lang="scss" scoped>
.form-box {
  padding-top: 20px;
}
.submit-box {
  text-align: right;
}
</style>