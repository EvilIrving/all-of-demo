<template>
  <div class="register-unit">
    <div class="form-item">
      <el-input placeholder="请输入企业名称" v-model="registerForm.companyName">
        <i slot="prefix" class="input-icon icon-input-unitname"></i>
      </el-input>
    </div>
    <div class="form-item">
      <el-input placeholder="请输入社会信用代码" v-model="registerForm.socialCreditCode">
        <i slot="prefix" class="input-icon icon-input-code"></i>
      </el-input>
    </div>
    <div class="form-item">
      <el-input placeholder="请输入所在地" v-model="registerForm.areaAddr">
        <i slot="prefix" class="input-icon icon-position-icon"></i>
      </el-input>
    </div>
    <!-- <div class="form-item">
      <el-select style="width:100%" v-model="registerForm.areaCode" placeholder="请选择行政区划" clearable>
        <el-option
          v-for="item in areaList"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        ></el-option>
      </el-select>
    </div>-->
    <div class="form-item">
      <el-input placeholder="请输入行业类型" v-model="registerForm.industryCategory">
        <i slot="prefix" class="input-icon icon-position-icon"></i>
      </el-input>
      <!-- <el-select style="width:100%" v-model="registerForm.industryCategory" placeholder="请选择行业类型">
        <el-option
          v-for="item in typeList"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        ></el-option>
      </el-select>-->
    </div>
    <div class="form-item">
      <div class="primary-long-btn" @click="nextStep()">下一步</div>
    </div>
  </div>
</template>

<script>
import {checkCompany} from "@/api/system"
export default {
  name: "RegisterUnitForm",
  props: {},
  data() {
    return {
      registerForm: {
        companyName: "",
        socialCreditCode: "",
        areaAddr: "",
        areaCode: "333000",
        industryCategory: "",
      },
      areaList: [
        {
          value: '333000',
          label: "浙江省"
        }
      ],
      typeList: [
        {
          value: 1,
          label: "行业1"
        }
      ]
    };
  },
  components: {},
  mounted() {},
  methods: {
    async nextStep(){
      if(!this.registerForm.socialCreditCode){
        this.$message.error("请填写社会信用代码")
      }else{
        await checkCompany(this.registerForm.socialCreditCode).then(res => {
          if(res.code === 0){
            this.$message({
              message: res.msg,
              type: 'success'
            })
            this.$emit("buttonClick")
          }
        })
      }
    },
    retsetForm(){
      this.registerForm = {
        companyName: "",
        socialCreditCode: "",
        areaAddr: "",
        areaCode: "333000",
        industryCategory: 1
      }
    }
  }
};
</script>

<style lang='scss' scoped>
.form-item {
  margin-top: 22px;
}
</style>