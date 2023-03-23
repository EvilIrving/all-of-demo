<!--
 * @Author: wanxin
 * @LastEditTime: 2022-05-20 21:28:46
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\engineeringSupervision\specialInspection\components\specialInspectionAddFrom.vue
-->
<!--
 * @Author: wanxin
 * @LastEditTime: 2022-05-20 19:50:23
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\engineeringSupervision\postFloodInspection\components\postFloodInspectionAddFrom.vue
-->
<template>
  <div>
    <div class="header-title">
      <span></span>
      基本信息
    </div>
    <el-form
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      label-width="150px"
      class="demo-ruleForm"
    >
      <div class="form-item">
        <el-form-item label="检查时间：">
          <el-date-picker
            class="form-item-value"
            v-model="ruleForm.checkTm"
            type="date"
            placeholder="请选择"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="组织单位：" prop="responUnit">
          <el-input
            class="form-item-value"
            v-model="ruleForm.responUnit"
          ></el-input>
        </el-form-item>
      </div>
      <el-form-item label="存在问题：" prop="checkContent">
        <el-input
          class="form-item-value"
          type="textarea"
          v-model="ruleForm.checkContent"
        ></el-input>
      </el-form-item>
      <div class="form-item">
        <el-form-item label="问题类型：" prop="problemType">
          <el-select v-model="ruleForm.problemType" placeholder="请选择">
            <el-option
              v-for="item in problemTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="检查人员：" prop="unitPrincipal">
          <el-input
            class="form-item-value"
            v-model="ruleForm.unitPrincipal"
          ></el-input>
        </el-form-item>
      </div>
      <!-- <el-form-item label="上传照片：">
        <div class="avatar-uploader">
          <div
            class="img_box"
            v-for="(item, index) in checkPicFiles"
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
            v-model="ruleForm.checkPic"
            :file-list="checkPicFiles"
            name="uploadFile"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
        </div>
      </el-form-item> -->
      <el-form-item label="上传附件：">
        <div>检查报告</div>
        <uploadFile
          accept=".pdf"
          @success="handleSuccess"
          @remove="handleRemove"
          @review="handlePreview"
          :fileList="fileList"
          :size="15"
        />
      </el-form-item>
      <el-form-item>
        <div class="btn-box">
          <el-button size="small" @click="closeDialog">取 消</el-button>
          <el-button size="small" type="primary" @click="submitForm('ruleForm')"
            >保存</el-button
          >
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
// import { operation } from "@/api/projectUnitDetail.js";
import uploadFile from '@/components/uploadFile.vue'
import { specialInspectionApi } from '@/api/projectUnitDetail.js'
export default {
  props: {
    id: String,
    options: Array,
  },
  components: { uploadFile },
  data() {
    return {
      problemTypeOptions: [
        {
          value: '一般问题',
          label: '一般问题',
        },
        {
          value: '较重问题',
          label: '较重问题',
        },
        {
          value: '严重问题',
          label: '严重问题',
        },
      ],
      ruleForm: {
        pid: '',
        id: '',
        checkTm: '',
        responUnit: '',
        checkContent:'',
        problemType: '',
        unitPrincipal: '',
        // uploadfile: '',
        // checkPic: [],
        aid: '',
      },
      rules: {
        // principalId: [
        //   { required: true, message: "请选择工作负责人", trigger: "change" },
        // ],
        // issueManId: [
        //   { required: true, message: "请选择签发负责人", trigger: "change" },
        // ],
        // issueDate: [
        //   {
        //     required: true,
        //     message: "请选择签发日期",
        //     trigger: "change",
        //   },
        // ],
      },
      // checkPicFiles: [],
      // fileList: [],
      headers: {
        Authorization: this.$sessionData('get', 'userInfo').accessToken,
      },
      submitFlag: true,
    }
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo
    },
  },
  async mounted() {
    // console.log(this.$sessionData("get", "userInfo"));
    if (this.id) {
      await this.getLoadPageDisDom()
    }
    // this.ruleForm.pid = this.projectDetail.prcd;
  },
  methods: {
    // clearImage(index) {
    //   this.ruleForm.checkPic.splice(index, 1)
    //   this.checkPicFiles.splice(index, 1)
    // },
    closeDialog() {
      this.$emit('close')
    },
    async getLoadPageDisDom() {
      let res = await specialInspectionApi.specialCheckList({
        pid: this.projectDetail.prcd,
        id: this.id,
      })
      if (res.code == 0) {
        let data = res.data.list[0]
        // console.log(data);
        this.ruleForm = {
          id: data.id,
          pid: data.pid,
          checkTm: data.checkTm,
          responUnit: data.responUnit,
          checkContent: data.checkContent,
          problemType: data.problemType,
          unitPrincipal: data.unitPrincipal,
          aid: data.aid,
          // checkPic: data.checkPic,
        }
        // this.ruleForm.checkPic = []
        // if (data.checkPic) {
        //   this.ruleForm.checkPic = data.checkPic.split(',')
        // }
        // this.checkPicFiles = data.checkPicList.map((item) => {
        //   return {
        //     name: item.fileName,
        //     url: item.fileUrl,
        //     filePath: item.fileUrl,
        //   }
        // })
        this.fileList = data.fileList.map((item) => {
          return {
            name: item.fileName,
            url: item.fileUrl,
          }
        })
        // console.log(this.fileList, "this.fileListthis.fileLists");
      }
    },
    // handleBeforeOne(file) {
    //   let obj = {
    //     num: 0,
    //     uid: file.uid,
    //     name: file.name,
    //   }
    //   this.checkPicFiles.push(obj)
    //   console.log(file, 111111)
    // },
    // handleProgressOne(e, file) {
    //   let index = this.checkPicFiles.findIndex((item) => item.uid == file.uid)
    //   this.checkPicFiles[index].num = e.percent
    // },
    // handlePictureOne(res, file) {
    //   console.log(res.data.id)
    //   if (res.code == 0) {
    //     let index = this.checkPicFiles.findIndex((item) => item.uid == file.uid)
    //     this.checkPicFiles[index] = {
    //       ...this.checkPicFiles[index],
    //       ...res.data,
    //     }
    //     this.$set(this.checkPicFiles, index, this.checkPicFiles[index])
    //     this.ruleForm.checkPic.push(res.data.id)
    //     console.log(this.checkPicFiles, 22222222)
    //   } else {
    //     let index = this.checkPicFiles.findIndex((item) => item.uid == file.uid)
    //     this.$message.error(
    //       `${this.checkPicFiles[index].name}上传失败，请重新上传`
    //     )
    //     this.checkPicFiles.splice(index, 1)
    //   }
    // },
    handleSuccess(data) {
      if (data.file.code == 0) {
        let files = data.file.data
        this.ruleForm.aid = files.id
        this.fileList = [
          {
            name: files.originalName,
            url: files.filePath,
          },
        ]
      } else {
        this.$message.error(res.msg)
        this.fileList = []
      }
    },
    handleRemove() {
      this.ruleForm.aid = ''
      this.fileList = []
    },
    handlePreview(data) {
      console.log(data)
    },
    submitForm(formName) {
      // console.log(this.ruleForm.checkPic)
      console.log(this.ruleForm)
      // if (this.ruleForm.domType == '2') {
      //   delete this.ruleForm[uploader]
      //   delete this.ruleForm[uploaderName]
      //   delete this.ruleForm[uploadfile]
      // }
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.submitFlag) {
            this.submitFlag = false
            // if (this.checkPicFiles.length !== 0) {
            //   this.ruleForm.checkPic = this.ruleForm.checkPic.join(',')
            // } else {
            //   this.ruleForm.checkPic = ''
            // }
            this.ruleForm.pid = this.projectDetail.prcd
            specialInspectionApi
              .saveOrUpdate(this.ruleForm)
              .then((res) => {
                if (res.code == 0) {
                  this.$message.success('保存成功~')
                  this.$emit('close', 1)
                } else {
                  this.$message.error(res.message)
                }
                this.submitFlag = true
              })
              .catch((err) => {
                this.submitFlag = true
                this.$message.error(err)
              })
          }
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.form-item-value {
  width: 100%;
}
.form-item {
  display: flex;
  width: 100%;
  justify-content: space-between;
  .form-item-value {
    width: 300px;
  }
}
.btn-box {
  text-align: right;
}
.header-title {
  display: flex;
  align-items: center;
  color: #666666;
  line-height: 26px;
  font-size: 16px;
  margin-bottom: 15px;
  span {
    width: 4px;
    height: 20px;
    background: #1492ff;
    border-radius: 2px;
    margin-right: 14px;
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