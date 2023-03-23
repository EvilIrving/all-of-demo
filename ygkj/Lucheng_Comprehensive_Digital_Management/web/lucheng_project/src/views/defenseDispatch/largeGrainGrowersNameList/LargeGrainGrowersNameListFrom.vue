<!--
 * @Author: wanxin
 * @LastEditTime: 2022-05-09 14:02:50
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\defenseDispatch\largeGrainGrowersNameList\LargeGrainGrowersNameListFrom.vue
-->
<template>
  <div class="wrap-form-box">
    <el-form :rules="rules" ref="form" :model="form" label-width="150px">
        <el-form-item label="大户姓名：" prop="name">
          <el-input v-model="form.name" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="街道：" prop="city">
          <el-input v-model="form.city" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="村：" prop="village">
          <el-input v-model="form.village" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="联系电话：" prop="phone">
          <el-input
            maxlength="11"
            oninput="this.value = this.value.replace(/[^0-9]/g, '')"
            v-model="form.phone"
            placeholder="请输入"
          ></el-input>
        </el-form-item>
      <div class="submit-box">
        <el-button
          @click="
            () => {
              this.$emit('closeEditDialog')
            }
          "
          >取消</el-button
        >
        <el-button type="primary" @click="submitForm('form')">确定</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { LargeGrainGrowersNameListApi } from '@/api/defenseDispatch.js'
export default {
  name: 'LargeGrainGrowersNameListFrom',
  props: {
    id: {
      type: String,
      default: '',
    },
  },
  components: {},
  data() {
    return {
      form: {
        name: '',
        city: '',
        village: '',
        phone: '',
      },
      rules: {
        // adminAdnm: [
        //   { required: true, message: "必填项", trigger: "blur" },
        // ],
        // naturalAdnm: [
        //   { required: true, message: "必填项", trigger: "blur" },
        // ],
        // pchargeName: [
        //   { required: true, message: "必填项", trigger: "blur" },
        // ]
      },
      submitFlag: true,
    }
  },
  mounted() {
    if (this.id) {
      this.getDetail()
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.submitFlag) {
            return false
          }
          this.submitFlag = false
          delete this.form.delFlag
          delete this.form.createTime
          delete this.form.createId
          delete this.form.updateTime
          delete this.form.updateId
          LargeGrainGrowersNameListApi.saveOrUpdate(this.form)
            .then((res) => {
              if (res.code == 0) {
                let msg = '新增成功~'
                if (this.id) {
                  msg = '编辑成功~'
                }
                this.$message.success(msg)
                this.$emit('closeEditDialog')
              } else {
                this.$message.error(res.msg)
              }
              this.submitFlag = true
            })
            .catch((err) => {
              this.submitFlag = true
              this.$message.error(err)
            })
        } else {
          return false
        }
      })
    },
    async getDetail() {
      let res = await LargeGrainGrowersNameListApi.growGrainList({ id: this.id })
      if (res.code == 0) {
        Object.assign(this.form, res.data.list[0])
      }
    },
  },
}
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
