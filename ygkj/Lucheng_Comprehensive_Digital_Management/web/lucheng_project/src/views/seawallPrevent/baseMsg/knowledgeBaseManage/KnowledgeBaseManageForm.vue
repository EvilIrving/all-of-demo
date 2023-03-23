<!--
 * @Author: hanyu
 * @LastEditTime: 2022-04-13 19:59:47
 * @Description: 知识库管理-表单
 * @FilePath: \lucheng_project\src\views\seawallPrevent\baseMsg\knowledgeBaseManage\KnowledgeBaseManageForm.vue
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
      <el-form-item label="规范名称：" prop="canonicalName">
        <el-input
          v-model="formData.canonicalName"
          placeholder="请输入规范名称"
        ></el-input>
      </el-form-item>
      <el-form-item label="上传附件：">
        <unpload-file
          @success="handleSuccess"
          @remove="handleRemove"
          accept=".jpg,.jpeg,.png,.pdf,.JPG,.JPEG,.PNG,.PDF"
          :size="15"
          :fileList="fileList"
        ></unpload-file>
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
import unploadFile from '@/components/uploadFile.vue'
import { basMsgZskApi } from '@/api/seawallPrevent'
export default {
  name: 'KnowledgeBaseManageForm',
  props: {
    id: [String, Number],
  },
  components: { unploadFile },
  computed: {
    projectDetail() {
      return this.$store.state.projectDetail
    },
  },
  data() {
    return {
      labelWidth: '25%',
      formData: {
        canonicalName: "",
        appendix: "",
        // fileurl: "",
        // filename: "",
      },
      fileList: [],
      rules: {
        canonicalName: [{ required: true, message: '必填项', trigger: 'blur' }],
      },
    }
  },
  mounted() {
    if (this.id) {
      this.getDetail()
    }
  },
  methods: {
    handleSuccess(res) {
      if (res.file.code == 0) {
        let file = res.file.data
        this.formData.appendix= file.id
        // this.formData.fileurl = file.filePath
        // this.formData.filename = file.originalName
      }
    },
    handleRemove() {
      this.formData.appendix= ''
      // this.formData.fileurl = ''
      // this.formData.filename = ''
    },
    async getDetail() {
      let res = await basMsgZskApi.list({
        id: this.id,
      })
      if (res.code == 0) {
        this.formData.canonicalName = res.data.list[0].canonicalName
        this.formData.appendix = res.data.list[0].appendix
        this.formData.id = this.id
        if (this.formData.appendix) {
          this.fileList = [
            {
              url: res.data.list[0].appendixUrl,
              name: res.data.list[0].appendixName,
            },
          ];
        }
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
      let res = await basMsgZskApi.save(data)
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