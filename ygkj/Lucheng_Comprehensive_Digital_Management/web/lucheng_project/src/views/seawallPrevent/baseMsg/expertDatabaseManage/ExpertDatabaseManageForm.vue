<!--
 * @Author: hanyu
 * @LastEditTime: 2021-11-02 19:30:31
 * @Description: 专家库管理-表单
 * @FilePath: /lucheng_project/src/views/seawallPrevent/baseMsg/refugeManage/RefugeManageForm.vue
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
      <el-form-item label="姓名：" prop="name">
        <el-input
          v-model="formData.name"
          placeholder="请输入姓名"
        ></el-input>
      </el-form-item>
      <el-form-item label="专家类型：" prop="type">
        <el-select
          v-model="formData.type"
          placeholder="请选择专家类型"
          @change="expertTypeChange"
        >
          <el-option
            v-for="item in expertTypeOptions"
            :key="item.value"
            :value="item.value"
            :label="item.label"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="级别：" prop="level">
        <el-select
          v-model="formData.level"
          placeholder="请选择专家级别"
          @change="expertLevelChange"
        >
          <el-option
            v-for="item in expertLevelOptions"
            :key="item.value"
            :value="item.value"
            :label="item.label"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="擅长方向：" prop="goodAt">
        <el-select
          v-model="formData.goodAt"
          placeholder="请选择擅长方向"
          @change="directionTypeChange"
        >
          <el-option
            v-for="item in directionTypeOptions"
            :key="item.value"
            :value="item.value"
            :label="item.label"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="所在单位：" prop="department">
        <el-input
          v-model="formData.department"
          placeholder="请输入所在单位"
        ></el-input>
      </el-form-item>
      <el-form-item label="职称：" prop="title">
        <el-input
          v-model="formData.title"
          placeholder="请输入职称"
        ></el-input>
      </el-form-item>
      <el-form-item label="联系方式：" prop="mobile">
        <el-input
          type="number"
          v-model="formData.mobile"
          placeholder="请输入联系电话"
        ></el-input>
      </el-form-item>
      <div class="footer_btn">
        <el-button type="primary" @click="submitForm('ruleForm')"
          >保存</el-button
        >
        <el-button @click="closeEditDialog()">取消</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { basMsgZjkApi } from '@/api/seawallPrevent'
export default {
  name: 'ExpertDatabaseManageForm',
  props: {
    id: [String, Number],
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectDetail
    },
  },
  data() {
    return {
      expertTypeOptions: [
        {
          label: '堤防海塘抢险',
          value: 1,
        },
        {
          label: '水闸泵站抢险',
          value: 2,
        },
      ],
      expertLevelOptions: [
        {
          label: '省级专家',
          value: 1,
        },
        {
          label: '市级专家',
          value: 2,
        },
        {
          label: '县级专家',
          value: 3,
        },
      ],
      directionTypeOptions: [
        {
          label: '防汛抢险专家',
          value: 1,
        },
        {
          label: '防汛调度专家',
          value: 2,
        },
      ],
      labelWidth: '25%',
      formData: {},
      rules: {
        name: [{ required: true, message: '必填项', trigger: 'blur' }],
      },
    }
  },
  mounted() {
    if (this.id) {
      this.getDetail()
    }
  },
  methods: {
    expertTypeChange(val) {},
    expertLevelChange(val) {},
    directionTypeChange(val) {},
    async getDetail() {
      let res = await basMsgZjkApi.details({
        dtsCmfId: this.id,
      })
      if (res.code == 0) {
        this.formData = res.data
      }
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