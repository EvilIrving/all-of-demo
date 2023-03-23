<!--
 * @Author: hanyu
 * @LastEditTime: 2022-03-04 09:40:16
 * @Description: 抢险队伍管理-表单
 * @FilePath: /lucheng_project/src/views/seawallPrevent/baseMsg/rescueTeamManage/PeopleForm.vue
-->
<template>
  <div class="form">
    <el-form
      :model="formData"
      status-icon
      :rules="rules"
      ref="ruleForm"
      :label-width="labelWidth"
    >
      <el-form-item label="姓名：" prop="personName">
        <el-input
          v-model="formData.personName"
          placeholder="请输入姓名"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="职务：" prop="personJob">
        <el-input
          v-model="formData.personJob"
          placeholder="请输入职务"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="办公电话：" prop="officePhone">
        <el-input
          type="number"
          v-model="formData.officePhone"
          placeholder="请输入办公电话"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="移动电话：" prop="personPhone">
        <el-input
          type="number"
          v-model="formData.personPhone"
          placeholder="请输入移动电话"
          clearable
        ></el-input>
      </el-form-item>
      <div class="footer_btn">
        <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
        <el-button @click="closeEditDialog()">取消</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { rescueTeamPeopleApi } from '@/api/seawallPrevent'
export default {
  name: 'PeopleForm',
  props: {
    teamId: [String, Number],
    id: [String, Number]
  },
  components: {
  },
  data() {
    return {
      labelWidth: '25%',
      formData: {},
      rules: {
        personName: [{ required: true, message: '必填项', trigger: 'blur' }],
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
      let res = await rescueTeamPeopleApi.list({
        id: this.id,
      })
      if (res.code == 0) {
        this.formData = res.data.list[0]
      }
    },

    submitForm(formName) {
      let that = this
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.formData.teamId = this.teamId
          this.save(this.formData)
        } else {
          return false
        }
      })
    },

    async save(data) {
      let res = await rescueTeamPeopleApi.save(data)
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