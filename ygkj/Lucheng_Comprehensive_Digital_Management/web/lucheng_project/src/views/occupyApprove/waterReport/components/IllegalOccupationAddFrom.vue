<!--
 * @Author: wanxin
 * @LastEditTime: 2022-06-14 16:36:54
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\occupyApprove\waterReport\components\IllegalOccupationAddFrom.vue
-->
<template>
  <div class="form_wrap">
    <el-form ref="form" :model="ruleForm" label-width="170px">
      <el-row>
        <el-col :span="11">
          <el-form-item label="水域名称：" prop="reportId">
            <el-select
              v-model="ruleForm.reportId"
              placeholder="请选择"
              style="width: 300px"
              :disabled="disabled"
              @change="selectChangePeport"
            >
              <el-option
                v-for="item in nameOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="行政区划：" prop="adcds">
            <el-select
              v-model="ruleForm.adcds"
              disabled
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
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="经度：" prop="longitude">
            <el-input
              v-model="ruleForm.longitude"
              placeholder="请输入"
              style="width: 300px"
            >
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="纬度：" prop="latitude">
            <el-input
              v-model="ruleForm.latitude"
              placeholder="请输入"
              style="width: 300px"
            >
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="发现时间：" prop="findTime">
            <el-date-picker
              v-model="ruleForm.findTime"
              value-format="yyyy-MM-dd"
              type="date"
              placeholder="选择日期"
            />
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="违法占用行为：" prop="illegalOccupy">
            <el-input
              v-model="ruleForm.illegalOccupy"
              placeholder="请输入"
              style="width: 300px"
            >
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="涉事单位（个人）信息：" prop="unitsInvolved">
            <el-input
              v-model="ruleForm.unitsInvolved"
              placeholder="请输入"
              style="width: 300px"
            >
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="上传照片：">
        <div class="avatar-uploader">
          <div
            class="img_box"
            v-for="(item, index) in pictureFiles"
            :key="index"
          >
            <span
              class="el-icon-error icon-btn"
              @click.stop="clearImage(index)"
            ></span>
            <div class="preview-list" v-if="item.filePath">
              <el-image
                style="width: 100%; height: 100%"
                :src="item.filePath"
                :preview-src-list="[item.filePath]"
              >
              </el-image>
            </div>
            <div class="progress-box" v-else>
              <div :style="`width:${item.num || 0}%`"></div>
            </div>
          </div>
          <el-upload
            :action="$config.uploadUrl"
            :headers="headers"
            :on-success="handlePictureOne"
            :show-file-list="false"
            :on-progress="handleProgressOne"
            :before-upload="handleBeforeOne"
            accept=".png,.jpg,.jpeg"
            v-model="ruleForm.picturesIds"
            :file-list="pictureFiles"
            name="uploadFile"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
        </div>
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
  name: 'IllegalOccupationAddFrom',
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
    this.getList()
    if (this.id) {
      this.getDetail()
      this.disabled = true
    }
  },
  data() {
    return {
      disabled: false,
      nameOptions: [],
      ruleForm: {
        id: '',
        reportId: '',
        adcds: '',
        longitude: '',
        latitude: '',
        findTime: '',
        illegalOccupy: '',
        unitsInvolved: '',
        picturesIds: [],
      },
      pictureFiles: [],
      submitFlag: true,
      headers: {
        Authorization: this.$sessionData('get', 'userInfo').accessToken,
      },
    }
  },
  methods: {
    selectChangePeport() {
      let data = this.nameOptions.find(item => item.value == this.ruleForm.reportId)
      this.ruleForm.adcds = data.adcds.split(',')
    },
    clearImage(index) {
      this.ruleForm.picturesIds.splice(index, 1)
      this.pictureFiles.splice(index, 1)
    },
    handleBeforeOne(file) {
      let obj = {
        num: 0,
        uid: file.uid,
        name: file.name,
      }
      this.pictureFiles.push(obj)
      console.log(file, 111111)
    },
    handleProgressOne(e, file) {
      let index = this.pictureFiles.findIndex((item) => item.uid == file.uid)
      this.pictureFiles[index].num = e.percent
    },
    handlePictureOne(res, file) {
      console.log(res.data.id)
      if (res.code == 0) {
        let index = this.pictureFiles.findIndex((item) => item.uid == file.uid)
        this.pictureFiles[index] = {
          ...this.pictureFiles[index],
          ...res.data,
        }
        this.$set(this.pictureFiles, index, this.pictureFiles[index])
        this.ruleForm.picturesIds.push(res.data.id)
        console.log(this.pictureFiles, 22222222)
      } else {
        let index = this.pictureFiles.findIndex((item) => item.uid == file.uid)
        this.$message.error(
          `${this.pictureFiles[index].name}上传失败，请重新上传`
        )
        this.pictureFiles.splice(index, 1)
      }
    },
    async getList() {
      await watersReportApi
        .list({
          name: this.name,
          adcds: this.areaValue,
          pageSize: this.pageSize,
          pageNum: this.pageNum,
        })
        .then((res) => {
          if (res.code === 0) {
            console.log(res.data.list)
            res.data.list.forEach((item) => {
              this.nameOptions.push({
                value: item.id,
                label: item.name,
                ...item
              })
            })
          }
        })
    },
    async getDetail() {
      //   console.log(this.id, 11111)
      let res = await watersReportApi.occupationList({
        id: this.id,
      })
      let data = res.data.list[0]
      this.ruleForm = {
        id: this.id,
        reportId: data.reportId,
        name: data.name,
        longitude: data.longitude,
        latitude: data.latitude,
        findTime: data.findTime,
        illegalOccupy: data.illegalOccupy,
        unitsInvolved: data.unitsInvolved,
        // adcds: data.adcds,
        // checkPic: data.checkPic,
      }
      this.ruleForm.adcds = []
      console.log(this.ruleForm)
      this.ruleForm.picturesIds = []
      if (data.adcds) {
        this.ruleForm.adcds = data.adcds.split(',')
      }
      if (data.picturesIds) {
        this.ruleForm.picturesIds = data.picturesIds.split(',')
        this.pictureFiles = data.files.map((item) => {
          return {
            name: item.fileName,
            url: item.fileUrl,
            filePath: item.fileUrl,
          }
        })
      }
    },
    submitForm(formName) {
      console.log(this.ruleForm, 111111)
      console.log(this.ruleForm.picturesIds)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.submitFlag) {
            return false
          }
          this.submitFlag = false
          if (this.pictureFiles.length !== 0) {
            this.ruleForm.picturesIds = this.ruleForm.picturesIds.join(',')
          } else {
            this.ruleForm.picturesIds = ''
          }
          if (this.ruleForm.adcds.length !== 0) {
            this.ruleForm.adcds = this.ruleForm.adcds.join(',')
          } else {
            this.ruleForm.adcds = ''
          }
          //   delete this.form.creationTime
          //   delete this.form.paged
          // this.form['prcd'] = this.projectInfo.prcd
          watersReportApi
            .occupationSaveOrUpdate(this.ruleForm)
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