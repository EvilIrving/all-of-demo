<!--
 * @Author: wanxin
 * @LastEditTime: 2022-05-09 14:05:35
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\defenseDispatch\personLiable\PersonLiableForm.vue
-->
<template>
  <div class="wrap-form-box">
    <el-form :rules="rules" ref="form" :model="form" label-width="150px">
      <div class="training-form-box">
        <el-form-item label="行政村：" prop="adminAdnm">
          <el-input v-model="form.adminAdnm" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="自然村：" prop="naturalAdnm">
          <el-input v-model="form.naturalAdnm" placeholder="请输入"></el-input>
        </el-form-item>
      </div>
      <div class="training-form-box">
        <el-form-item label="责任人类型：" prop="chargeType">
          <el-input v-model="form.chargeType" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="责任人级别：" prop="chargeLevel">
          <el-input v-model="form.chargeLevel" placeholder="请输入"></el-input>
        </el-form-item>
      </div>
      <div class="training-form-box">
        <el-form-item label="责任人姓名：" prop="pchargeName">
          <el-input v-model="form.pchargeName" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="手机号码：" prop="mobile">
          <el-input
            maxlength="11"
            oninput="this.value = this.value.replace(/[^0-9]/g, '')"
            v-model="form.mobile"
            placeholder="请输入"
          ></el-input>
        </el-form-item>
      </div>
      <div class="training-form-box">
        <el-form-item label="所在单位：" prop="unit">
          <el-input v-model="form.unit" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="职务：" prop="position">
          <el-input v-model="form.position" placeholder="请输入"></el-input>
        </el-form-item>
      </div>
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
import { PersonLiableApi } from '@/api/defenseDispatch.js'
export default {
  name: 'TrainingManageForm',
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
        adminAdnm: '',
        naturalAdnm: '',
        chargeType: '',
        chargeLevel: '',
        pchargeName: '',
        mobile: '',
        unit: '',
        position: '',
      },
      rules: {
        adminAdnm: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
        naturalAdnm: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
        chargeType: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
        chargeLevel: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
        pchargeName: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
        mobile: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
        unit: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
        position: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
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
          PersonLiableApi.saveOrUpdate(this.form)
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
      let res = await PersonLiableApi.pvPchargeList({ id: this.id })
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
