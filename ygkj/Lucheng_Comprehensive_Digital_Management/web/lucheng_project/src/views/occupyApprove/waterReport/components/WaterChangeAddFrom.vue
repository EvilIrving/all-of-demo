<!--
 * @Author: wanxin
 * @LastEditTime: 2022-05-24 17:10:09
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\occupyApprove\report\components\WaterChangeAddFrom.vue
-->
<template>
  <div class="form_wrap">
    <el-form ref="form" :model="ruleForm" label-width="150px">
      <el-row>
        <el-col :span="11">
          <el-form-item label="年份：" prop="yearTime">
            <el-date-picker
              v-model="ruleForm.yearTime"
              type="year"
              placeholder="请选择"
              format="yyyy"
              value-format="yyyy"
            >
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="水域面积：" prop="area">
            <el-input
              v-model="ruleForm.area"
              placeholder="请输入"
              style="width: 300px"
            >
              <template slot="append">㎡</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="库容量：" prop="capacity">
            <el-input
              v-model="ruleForm.capacity"
              placeholder="请输入"
              style="width: 300px"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="河底高程" prop="bottomElevation">
            <el-input
              v-model="ruleForm.bottomElevation"
              placeholder="请输入"
              style="width: 300px"
            >
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="实际岸线：" prop="shoreline">
            <el-input
              v-model="ruleForm.shoreline"
              placeholder="请输入"
              style="width: 300px"
            >
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <div class="submit-box">
        <el-button
          @click="
            () => {
              this.$emit('closeChangeDialog')
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
  name: 'configurationDetail',
  props: {
    id: {
      type: String,
      default: '',
    },
    reportId: {
      type: String,
      default: '',
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
        reportId: '',
        id: '',
        yearTime: '',
        area: '',
        capacity: '',
        bottomElevation: '',
        shoreline: '',
      },
      submitFlag: true,
    }
  },
  methods: {
    async getDetail() {
      //   console.log(this.id, 11111)
      let res = await watersReportApi.changeList({
        reportId: this.reportId,
        id: this.id,
      })
      let data = res.data[0]
      console.log(data)
      this.ruleForm = {
        id: this.id,
        reportId: this.reportId,
        // yearTime: data.yearTime,
        area: data.area,
        capacity: data.capacity,
        bottomElevation: data.bottomElevation,
        shoreline: data.shoreline,
      }
      this.$set(this.ruleForm,'yearTime',JSON.stringify(data.yearTime))
    //   this.ruleForm.yearTime = JSON.stringify(data.yearTime)
      console.log(this.ruleForm)
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.submitFlag) {
            return false
          }
          this.submitFlag = false
          this.ruleForm.reportId = this.reportId
          //   delete this.form.creationTime
          //   delete this.form.paged
          // this.form['prcd'] = this.projectInfo.prcd
          watersReportApi
            .changeSaveOrUpdate(this.ruleForm)
            .then((res) => {
              console.log(res, 11111)
              if (res.code == 0) {
                let msg = '新增成功~'
                if (this.id) {
                  msg = '编辑成功~'
                }
                this.$message.success(msg)
                this.$emit('closeChangeDialog')
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