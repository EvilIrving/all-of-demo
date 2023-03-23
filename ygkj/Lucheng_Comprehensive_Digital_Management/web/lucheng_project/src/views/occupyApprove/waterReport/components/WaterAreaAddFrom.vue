<!--
 * @Author: wanxin
 * @LastEditTime: 2022-05-31 16:47:00
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\occupyApprove\waterReport\components\WaterAreaAddFrom.vue
-->
<template>
  <div class="form_wrap">
    <el-form ref="form" :model="ruleForm" label-width="120px">
      <el-form-item label="水域名称：" prop="name">
        <el-input
          v-model="ruleForm.name"
          placeholder="请输入"
          style="width: 300px"
        >
        </el-input>
      </el-form-item>
      <el-form-item label="行政区划：" prop="adcds">
        <el-select
          v-model="ruleForm.adcds"
          multiple
          placeholder="请选择"
          style="width: 300px"
          @change="$forceUpdate()"
        >
          <el-option
            v-for="item in areaOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="水域面积：" prop="area">
        <el-input
          v-model="ruleForm.area"
          placeholder="请输入"
          style="width: 300px"
        >
          <template slot="append">㎡</template>
        </el-input>
      </el-form-item>

      <div class="submit-box">
        <el-button
          @click="
            () => {
              this.$emit('close')
            }
          "
          >取消</el-button
        >
        <el-button type="primary" @click="submitForm('form')">保存</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { watersReportApi } from '@/api/occupyApprove.js'

export default {
  name: 'WaterAreaAddFrom',
  props: {
    id: {
      type: String,
      default: '',
    },
    areaOptions: {
      type: Array,
    },
  },
  components: {},
  mounted() {
    if (this.id) {
      this.getDetail()
    }
  },
  data() {
    return {
      ruleForm: {
        id: '',
        name: '',
        adcds: '',
        area: '',
      },
      submitFlag: true,
    }
  },
  methods: {
    async getDetail() {
      let res = await watersReportApi.list({
        id: this.id,
      })
      let data = res.data.list[0]
      this.ruleForm = {
        id: this.id,
        name: data.name,
        area: data.area,
      }
      this.ruleForm.adcds = []
      if (data.adcds) {
        this.ruleForm.adcds = data.adcds.split(',')
      }
    },
    submitForm(formName) {
      console.log(this.ruleForm, 111111)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.submitFlag) {
            return false
          }
          this.submitFlag = false
          if (this.ruleForm.adcds.length !== 0) {
            this.ruleForm.adcds = this.ruleForm.adcds.join(',')
          } else {
            this.ruleForm.adcds = ''
          }
          watersReportApi
            .saveOrUpdate(this.ruleForm)
            .then((res) => {
              console.log(res, 11111)
              if (res.code == 0) {
                let msg = '新增成功~'
                if (this.id) {
                  msg = '编辑成功~'
                }
                this.$message.success(msg)
                this.$emit('close')
              } else {
                //this.$message.error(res.msg);
                console.log(res.msg)
                this.$message.error({ message: res.msg || '新增失败' })
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
  },
}
</script>

<style scoped lang="scss">
.form_wrap {
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
    margin-right: 50px;
  }
}
.img_box {
  cursor: pointer;
  float: left;

  @include flexbox();
  flex-direction: row;
  margin-right: 3px;
  img {
    width: 80px;
    height: 80px;
    margin: 0 5px;
  }

  > div {
    width: 80px;
    height: 80px;
    margin-right: 3px;
    padding: 2px;
    position: relative;
    @include flexbox();
    @include flexAI(center);
    @include flexJC(center);

    .active {
      display: none;
    }
  }
}
</style>

<style lang="scss">
.avatar-uploader {
  width: 90%;
  @include flexbox();
  .el-upload {
    height: 80px;
    width: 80px;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    @include flexbox();
    @include flexJC(center);
    @include flexAI(center);
    .img_box {
      .el-progress--without-text .el-progress-bar {
        display: block;
      }
      .el-progress-bar {
        width: 60%;
        margin-top: 30px;
      }
    }
  }
}
</style>