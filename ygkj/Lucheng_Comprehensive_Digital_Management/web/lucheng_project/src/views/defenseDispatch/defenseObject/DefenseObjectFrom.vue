<!--
 * @Author: wanxin
 * @LastEditTime: 2022-05-18 10:01:36
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\defenseDispatch\defenseObject\DefenseObjectFrom.vue
-->
<template>
  <div class="wrap-form-box">
    <el-form :rules="rules" ref="form" :model="form" label-width="165px">
      <div class="training-form-box">
        <el-form-item label="乡（镇、街道）：" prop="town">
          <el-input v-model="form.town" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="行政村：" prop="adminAdnm">
          <el-input v-model="form.adminAdnm" placeholder="请输入"></el-input>
        </el-form-item>
      </div>
      <div class="training-form-box">
        <el-form-item label="自然村：" prop="naturalAdnm">
          <el-input v-model="form.naturalAdnm" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="风险区等级：" prop="lev">
          <!-- <el-input v-model="form.lev" placeholder="请输入"></el-input> -->
          <el-select v-model="form.lev" placeholder="请选择" style="width: 100%">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </div>
      <div class="training-form-box">
        <el-form-item label="防御对象：" prop="defenObject">
          <el-input v-model="form.defenObject" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="类型：" prop="type">
          <!-- <el-input v-model="form.type" placeholder="请输入"></el-input> -->
          <el-select v-model="form.type" placeholder="请选择" style="width: 100%">
            <el-option
              v-for="item in typeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </div>
      <div class="training-form-box">
        <el-form-item label="联系人：" prop="contact">
          <el-input v-model="form.contact" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="联系方式：" prop="contactTel">
          <el-input
            maxlength="11"
            oninput="this.value = this.value.replace(/[^0-9]/g, '')"
            v-model="form.contactTel"
            placeholder="请输入"
          ></el-input>
        </el-form-item>
      </div>
      <div class="training-form-box">
        <el-form-item label="转移责任人：" prop="tcharger">
          <el-input v-model="form.tcharger" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="转移责任人联系方式：" prop="tchargertel">
          <el-input
            maxlength="11"
            oninput="this.value = this.value.replace(/[^0-9]/g, '')"
            v-model="form.tchargertel"
            placeholder="请输入"
          ></el-input>
        </el-form-item>
      </div>
      <div class="training-form-box">
        <el-form-item label="人口数量：" prop="pcount">
          <el-input v-model="form.pcount" placeholder="请输入"></el-input>
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
import { defenseObjectApi } from '@/api/defenseDispatch.js'
export default {
  name: 'DefenseObjectFrom',
  props: {
    id: {
      type: String,
      default: '',
    },
  },
  components: {},
  data() {
    return {
      options: [
        {
          value: '高',
          label: '高',
        },
        {
          value: '低',
          label: '低',
        }
      ],
      typeOptions: [
        {
          value: '居民户',
          label: '居民户',
        },
        {
          value: '学校',
          label: '学校',
        },
        {
          value: '工矿企业',
          label: '工矿企业',
        },
        {
          value: '旅游景点',
          label: '旅游景点',
        },
        {
          value: '农家乐',
          label: '农家乐',
        }
      ],
      form: {
        town: '',
        adminAdnm: '',
        naturalAdnm: '',
        lev: '',
        defenObject: '',
        type: '',
        contact: '',
        contactTel: '',
        tcharger: '',
        tchargertel: '',
        pcount: '',
      },
      rules: {
        town: [{ required: true, message: '必填项', trigger: 'blur' }],
        adminAdnm: [{ required: true, message: '必填项', trigger: 'blur' }],
        naturalAdnm: [{ required: true, message: '必填项', trigger: 'blur' }],
        defenObject: [{ required: true, message: '必填项', trigger: 'blur' }],
        type: [{ required: true, message: '必填项', trigger: 'blur' }],
        contact: [{ required: true, message: '必填项', trigger: 'blur' }],
        contactTel: [{ required: true, message: '必填项', trigger: 'blur' }],
        tcharger: [{ required: true, message: '必填项', trigger: 'blur' }],
        tchargertel: [{ required: true, message: '必填项', trigger: 'blur' }],
        pcount: [{ required: true, message: '必填项', trigger: 'blur' }],
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
          defenseObjectApi
            .saveOrUpdate(this.form)
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
      let res = await defenseObjectApi.defenseObjectList({ id: this.id })
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
