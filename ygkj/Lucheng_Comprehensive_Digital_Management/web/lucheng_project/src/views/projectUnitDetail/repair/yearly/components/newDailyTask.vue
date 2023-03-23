<template>
  <div class="wrap-form-box">
    <el-form ref="form" :model="form" label-width="150px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="工程名称：" prop="projectName">
            <el-input v-model="form.projectName" placeholder="请输入" maxlength="50" style="width: 300px" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label="工程类型：" prop="type">
            <el-select v-model="form.projectType" placeholder="请选择" clearable style="width: 300px" disabled>
              <el-option v-for="item in projectTypeOption" :key="item.label" :label="item.label" :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="时间：" prop="tm">
        <el-date-picker type="datetime" placeholder="请选择" v-model="form.tm" style="width: 300px;" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
      </el-form-item>
      <el-form-item label="任务描述：" prop="taskDescribe">
        <el-input v-model="form.taskDescribe" placeholder="请输入" maxlength="50" type="textarea" style="width: 760px"></el-input>
      </el-form-item>
      <el-form-item label="具体隐患：" prop="hiddenDanger">
        <el-input v-model="form.hiddenDanger" placeholder="请输入" maxlength="50" type="textarea" style="width: 760px"></el-input>
      </el-form-item>
      <el-form-item label="隐患图片：" prop="imgIds">
        <div class="avatar-uploader">
          <div class="img_box" v-for="(item,index) in fileList" :key="index">
            <span class="el-icon-error icon-btn" @click.stop="clearImage(index, 1)"></span>
            <div class="preview-list" v-if="item.filePath">
              <el-image style="width: 100%; height: 100%" :src="item.filePath" :preview-src-list="[item.filePath]">
              </el-image>
            </div>
            <div class="progress-box" v-else>
              <div :style="`width:${item.num || 0}%`"></div>
            </div>
          </div>
          <el-upload :action="$config.uploadUrl" :headers="headers" :on-success="handlePicture" :show-file-list="false" :on-progress="handleProgress" :before-upload="handleBefore" accept=".png,.jpg,.jpeg" v-model="form.imgIds" :file-list="fileList" name="uploadFile">
            <i class="el-icon-plus"></i>

          </el-upload>
        </div>

      </el-form-item>
      <el-row>
        <el-col :span="12">
          <el-form-item label="处理方式：" prop="processWay">
            <el-select v-model="form.processWay" placeholder="请选择" clearable style="width: 300px">
              <el-option v-for="item in processWayOption" :key="item.label" :label="item.label" :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label="计划完成时间：" prop="planTm">
            <el-date-picker type="datetime" placeholder="请选择" v-model="form.planTm" style="width: 300px;" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="问题来源：" prop="problemSource">
            <el-select v-model="form.problemSource" placeholder="请选择" clearable style="width: 300px">
              <el-option v-for="item in problemSourceOption" :key="item.label" :label="item.label" :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label="问题类型：" prop="problemType">
            <el-select v-model="form.problemType" placeholder="请选择" clearable style="width: 300px">
              <el-option v-for="item in problemTypeOption" :key="item.label" :label="item.label" :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="实施单位：" prop="implementUnit">
        <el-select v-model="form.implementUnit" placeholder="请选择单位" clearable style="width: 170px" @change="getPeople">
          <el-option v-for="item in implementUnitOption" :key="item.label" :label="item.label" :value="item.label">
          </el-option>
        </el-select>
        <el-select :loading='loadingOption'  loading-text='加载中' v-model="implementUser" placeholder="人员" clearable style="width: 110px;margin-left: 20px" filterable>
          <el-option v-for="item in implementUserOption" :key="item.label" :label="item.label" :value="item">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="指导意见：" prop="instructions">
        <el-input v-model="form.instructions" placeholder="请输入" maxlength="50" type="textarea" style="width: 760px"></el-input>
      </el-form-item>

      <div class="submit-box">
        <el-button @click="()=>{this.$emit('closeEditDialog')}">取消</el-button>
        <el-button type="primary" @click="submitForm('form')">提交</el-button>
      </div>
    </el-form>

  </div>
</template>

<script>

import { annulMaintenance } from "@/api/projectUnitDetail";

export default {
  name: "newDailyTask",
  props: {
    id: {
      type: String,
      default: '',
    }
  },
  components: {
  },
  computed: {
    projectInfo() {
      return this.$store.state.projectInfo
    }
  },
  data() {
    return {
      dialogImageUrl: '',
      dialogVisible: false,
      progressPercent: 0,
      isUploading: false,
      isImg: false,
      form: {
        projectName: '',
        projectType: '',
        tm: '',
        taskDescribe: '',
        hiddenDanger: '',
        imgIds: [],
        processWay: '',
        planTm: '',
        problemSource: '',
        problemType: '',
        implementUnit: '',
        instructions: '',
      },
      implementUser: '',
      submitFlag: true,
      fileList: [],
      projectTypeOption: [
        { label: '水库', value: '水库' },
        { label: '水闸', value: '水闸' },
        { label: '海塘', value: '海塘' },
        { label: '堤防', value: '堤防' },
        { label: '泵站', value: '泵站' },

      ],
      problemTypeOption: [
        { label: '一般问题', value: '一般问题' },
        { label: '较重问题', value: '较重问题' },
        { label: '严重问题', value: '严重问题' },
      ],
      problemSourceOption: [
        { label: '日常巡查', value: '日常巡查' },
        { label: '汛前检查', value: '汛前检查' },
        { label: '汛后检查', value: '汛后检查' },
        { label: '专项检查', value: '专项检查' },
        { label: '特别检查', value: '特别检查' },
      ],
      processWayOption: [
        { label: '简单处理', value: '简单处理' },
        { label: '日常跟踪', value: '日常跟踪' },
        { label: '日常维修', value: '日常维修' },
        { label: '年度大修', value: '年度大修' },
        { label: '综合整治计划', value: '综合整治计划' },
      ],
      implementUnitOption: [
        { label: '物业单位', value: '' },
        { label: '水管单位', value: '' },
        { label: '水利局', value: '' }
      ],
      implementUserOption: [],
      loadingOption: true,
      headers: {
        Authorization: this.$sessionData("get", "userInfo").accessToken,
      },

    }
  },
  mounted() {

    this.form.projectName = this.projectInfo.name
    this.form.projectType = this.projectInfo.type
    if (this.id) {
      this.getDetail();
    }
  },
  methods: {
    clearImage(index, type) {
      this.form.imgIds.splice(index, 1)
      this.fileList.splice(index, 1);
    },
    handlePicture(res, file) {
      if (res.code == 0) {
        let index = this.fileList.findIndex((item) => item.uid == file.uid);
        console.log(index, file.uid)
        this.fileList[index] = { ...this.fileList[index], ...res.data };
        this.$set(this.fileList, index, this.fileList[index]);
        this.form.imgIds.push(res.data.id)
      } else {
        let index = this.fileList.findIndex((item) => item.uid == file.uid);
        this.$message.error(
          `${this.fileList[index].name}上传失败，请重新上传`
        );
        this.fileList.splice(index, 1);
      }
    },
    handleProgress(e, file) {
      let index = this.fileList.findIndex((item) => item.uid == file.uid);
      this.fileList[index].num = e.percent
    },
    handleBefore(file) {
      let obj = {
        num: 0,
        uid: file.uid,
        name: file.name,
      };
      this.fileList.push(obj);
    },
    async getPeople() {
      let res = null
      this.implementUserOption = []
      console.log(this.form.implementUnit, 'implementUnit');
      switch (this.form.implementUnit) {
        case '水管单位':
          res = await annulMaintenance.getWaterManageUser({
          })
          for (var [index, item] of res.data.list.entries()) {
            this.implementUserOption.push({
              label: item.username,
              value: index,
              id: item.id
            })
          }
          break;
        case '水利局':
          res = await annulMaintenance.getUserData({
            pageNum: 1,
            pageSize: 9999,
            username: '',
          })
          for (var [index, item] of res.data.list.entries()) {
            this.implementUserOption.push({
              label: item.username,
              value: index,
              id: item.id
            })
          }
          break;
        default:
          res = await annulMaintenance.getWyhUserByProject({
            pageNum: 1,
            pageSize: 9999,
            projectCode: this.projectInfo.prcd,
          })
          for (item of res.data) {
            this.implementUserOption.push({
              label: item.username,
              value: item.id,
              id: item.id
            })
          }
          break;
      }
      this.loadingOption = false
    },

    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.submitFlag) {
            return false;
          }
          this.submitFlag = false;
          delete this.form.createTime
          if (this.fileList.length !== 0) {
            this.form.imgIds = this.form.imgIds.join(',')
          }
          this.form['prcd'] = this.projectInfo.prcd
          this.form['implementUser'] = this.implementUser.label
          this.form['implementUserId'] = this.implementUser.id
          annulMaintenance
            .updateDailyTask(this.form)
            .then((res) => {
              if (res.code == 0) {
                let msg = "新增成功~";
                if (this.id) {
                  msg = "编辑成功~";
                }
                this.$message.success(msg);
                this.$emit("closeEditDialog", 1);
              } else {
                this.$message.error(res.msg);
              }
              this.submitFlag = true;
            })
            .catch((err) => {
              this.submitFlag = true;
              this.$message.error(err);
            });
        } else {
          return false;
        }
      });

    },

    async getDetail() {
      let res = await annulMaintenance.DailyTask({
        id: this.id,
        prcd: this.projectInfo.prcd
      });
      if (res.code == 0) {
        Object.assign(this.form, res.data.list[0])
      }
    },



  }
}
</script>

<style scoped lang="scss">
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

      .error {
        @include flexbox();
        @include flexAI(center);
        @include flexJC(center);
        height: 80px;
        width: 80px;
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #d40000;
      }
      .re_onload {
        @include flexbox();
        @include flexAI(center);
        @include flexJC(center);
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #155bd4;
        display: none;
      }
      .error:hover {
        display: none;
      }
      .error:hover + .re_onload {
        display: inline;
      }

      .el-icon-error {
        display: none;
        position: absolute;
        top: 0;
        left: 75px;
      }
    }
    > div:hover {
      .el-icon-error {
        display: inline;
      }
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