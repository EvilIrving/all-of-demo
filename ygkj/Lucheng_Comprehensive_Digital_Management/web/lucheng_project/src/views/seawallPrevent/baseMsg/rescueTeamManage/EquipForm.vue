<!--
 * @Author: hanyu
 * @LastEditTime: 2022-03-04 10:03:38
 * @Description: 队伍设备管理-表单
 * @FilePath: /lucheng_project/src/views/seawallPrevent/baseMsg/rescueTeamManage/EquipForm.vue
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
      <el-form-item label="设备名称：" prop="name">
        <el-input
          v-model="formData.name"
          placeholder="请输入设备名称"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="设备类型：" prop="type">
        <el-select
          v-model="formData.type"
          clearable
          placeholder="请选择设备类型"
        >
          <el-option
            v-for="item in typeOptions"
            :key="item.label"
            :value="item.label"
            :label="item.label"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="数量：" prop="num">
        <el-input
          type="number"
          v-model="formData.num"
          placeholder="请输入数量"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="单位：" prop="measure">
        <el-input
          v-model="formData.measure"
          placeholder="请输入单位"
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
import { rescueTeamEquipApi } from '@/api/seawallPrevent'
export default {
  name: 'EquipForm',
  props: {
    teamId: [String, Number],
    id: [String, Number]
  },
  components: {
  },
  data() {
    return {
      typeOptions: [
        { label: '抽水设备', value: 1 },
        { label: '防汛设备', value: 1 },
        { label: '照明设备', value: 1 }
      ],
      labelWidth: '25%',
      formData: {},
      rules: {
        name: [{ required: true, message: '必填项', trigger: 'blur' }],
        type: [{ required: true, message: '必填项', trigger: 'blur' }],
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
      let res = await rescueTeamEquipApi.list({
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
      let res = {}
      if(this.id){
        res = await rescueTeamEquipApi.update(data)
      }else{
        res = await rescueTeamEquipApi.insert(data)
      }
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