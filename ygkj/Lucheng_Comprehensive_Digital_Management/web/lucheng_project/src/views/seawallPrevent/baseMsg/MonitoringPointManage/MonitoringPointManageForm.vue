<!--
 * @Author: hanyu
 * @LastEditTime: 2022-03-07 14:07:14
 * @Description: 监测点管理-表单
 * @FilePath: \lucheng_project\src\views\seawallPrevent\baseMsg\MonitoringPointManage\MonitoringPointManageForm.vue
-->
<template>
  <div class="form">
    <el-form :model="formData" :rules="rules" status-icon ref="ruleForm" :label-width="labelWidth">
      <el-form-item label="摄像头：" prop="cameraName">
        <el-input disabled v-model="formData.cameraName" placeholder="请输入经度"></el-input>
      </el-form-item>
      <el-form-item label="经度：" prop="longitude">
        <el-input v-model="formData.longitude" placeholder="请输入经度"></el-input>
      </el-form-item>
      <el-form-item label="纬度：" prop="latitude">
        <el-input v-model="formData.latitude" placeholder="请输入经度"></el-input>
      </el-form-item>
      <el-form-item label="高程：" prop="altitude">
        <el-input v-model="formData.altitude" placeholder="请输入高程"></el-input>
      </el-form-item>
      <div class="footer_btn">
        <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
        <el-button @click="closeEditDialog()">取消</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { disasterPreventionApi } from '@/api/seawallPrevent'
export default {
  name: 'MonitoringPointManageForm',
  props: {
    id: [String, Number],
  },
  computed: {

  },
  data() {
    return {
      labelWidth: '25%',
      formData: {},
      loading: false,
      rules: {
        longitude: [{ required: false, message: '必填项', trigger: 'blur' }],
      },
    }
  },
  mounted() {
    if (this.id) {
      this.getDetail()
    }
  },
  methods: {
    async getDetail() {
      this.loading = true;
      let obj = {
        cameraIndexCode: this.id,
      };
      await disasterPreventionApi
        .listCamera(obj)
        .then((res) => {
          if (res.code == 0) {
            this.formData = res.data.list[0];
          }
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    submitForm(formName) {
      let that = this
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.save(this.formData)
        } else {
          return false
        }
      })
    },
    async save(data) {
      let res = await basMsgZjkApi.save(data)
      if (res.code == 0) {
        this.$message({
          message: '保存成功',
          type: 'success',
          duration: '1000',
          onClose: () => {
            this.closeEditDialog()
          },
        })
      } else {
        this.$message.error('保存失败！')
      }
    },
    closeEditDialog() {
      this.$emit('closeEditDialog')
    },
  },
}
</script>

<style lang='scss' scoped>
.wrap {
}
</style>