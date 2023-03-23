<!--
 * @Author: hanyu
 * @LastEditTime: 2021-11-23 16:24:33
 * @Description: 物业管理-公司表单
 * @FilePath: /lucheng_project/src/views/propertyManage/propertyList/PropertyUnitForm.vue
-->
<template>
  <div class="wrap-form-box">
    <el-form :rules="rules" ref="form" :model="form" label-width="150px">
      <el-form-item label="物业公司名称：" prop="companyName">
        <el-input v-model="form.companyName" placeholder="请输入"  maxlength="50"></el-input>
      </el-form-item>
      <el-form-item label="负责人：" prop="principal">
        <el-input v-model="form.principal" placeholder="请输入" maxlength="10"></el-input>
      </el-form-item>
      <el-form-item label="负责人电话：" prop="phone">
        <el-input
          maxlength="11"
          oninput="this.value = this.value.replace(/[^0-9]/g, '')"
          v-model="form.phone"
          placeholder="请输入"
        ></el-input>
      </el-form-item>
      <div class="submit-box">
        <el-button @click="()=>{this.$emit('closeEditDialog')}">取消</el-button>
        <el-button type="primary" @click="submitForm('form')">确定</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { propertyUnitApi } from '@/api/propertyManage'
export default {
  name: "TrainingManageForm",
  props: {
    id: {
      type: String,
      default: "",
    },
  },
  components: { 
    
  },
  data() {
    return {
      form: {
        companyName: "",
        principal: "",
        phone: ""
      },
      rules: {
        companyName: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
        principal: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
        phone: [
          { required: true, message: "必填项", trigger: "blur" },
        ]
      },
      submitFlag: true,
    };
  },
  mounted() {
    if (this.id) {
      this.getDetail();
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.submitFlag) {
            return false;
          }
          this.submitFlag = false;
          delete this.form.createTime
          delete this.form.paged
          propertyUnitApi
            .save(this.form)
            .then((res) => {
              if (res.code == 0) {
                let msg = "新增成功~";
                if (this.id) {
                  msg = "编辑成功~";
                }
                this.$message.success(msg);
                this.$emit("closeEditDialog", 1);
              } else {
                this.$message.error(res.msg);
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
    async getDetail() {
      let res = await propertyUnitApi.list({ id: this.id });
      if (res.code == 0) {
        Object.assign(this.form, res.data.list[0])
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap-form-box {
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
  }
}
</style>
