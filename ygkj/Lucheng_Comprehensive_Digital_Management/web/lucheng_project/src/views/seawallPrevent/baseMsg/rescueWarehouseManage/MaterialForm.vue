<!--
 * @Author: hanyu
 * @LastEditTime: 2022-03-04 17:23:34
 * @Description: 抢险队伍管理-表单
 * @FilePath: /lucheng_project/src/views/seawallPrevent/baseMsg/rescueWarehouseManage/MaterialForm.vue
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
      <el-form-item label="物资名称：" prop="name">
        <el-input
          v-model="formData.name"
          placeholder="请输入物资名称"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="物资类型：" prop="type">
        <el-select
          v-model="formData.type"
          clearable
          placeholder="请选择物资类型"
        >
          <el-option
            v-for="item in typeOptions"
            :key="item.value"
            :value="item.value"
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
      <el-form-item label="规格：" prop="measure">
        <el-input
          type="number"
          v-model="formData.measure"
          placeholder="请输入规格"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="物资价值：" prop="materialPrice">
        <el-input
          v-model="formData.materialPrice"
          placeholder="请输入物资价值"
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
import { rescueMaterialApi } from '@/api/seawallPrevent'
export default {
  name: 'MaterialForm',
  props: {
    warehouseId: [String, Number],
    id: [String, Number]
  },
  components: {
  },
  data() {
    return {
      typeOptions: [
        { label: '袋类', value: '袋类' },
        { label: '布（膜）类', value: '布（膜）类' },
        { label: '救生器材', value: '救生器材' },
        { label: '舟类', value: '舟类' },
        { label: '物料', value: '物料' },
      ],
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
      let res = await rescueMaterialApi.list({
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
          this.formData.warehouseId = this.warehouseId
          this.save(this.formData)
        } else {
          return false
        }
      })
    },

    async save(data) {
      let res = await rescueMaterialApi.save(data)
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