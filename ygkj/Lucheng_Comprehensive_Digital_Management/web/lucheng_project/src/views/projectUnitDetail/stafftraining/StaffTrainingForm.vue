<!--
 * @Date: 2022-04-25 10:24:15
 * @Author: 
 * @Description: 物业管理事项-人员培训 form
 * @LastEditors: dtb
 * @LastEditTime: 2022-04-25 13:46:32
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\stafftraining\StaffTrainingForm.vue
-->
<template>
  <div class="wrap-form-box">
    <el-form ref="form" :model="form" label-width="150px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="培训人员：" prop="person">
            <el-input v-model="form.person" placeholder="请输入培训人员" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="培训时间：" prop="time">
            <el-date-picker type="datetime" placeholder="请选择培训时间" value-format="yyyy-MM-dd HH:mm:00" format="yyyy-MM-dd HH:mm:00" v-model="form.time" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="培训经费(元)：" prop="fund">
            <el-input type="number" v-model="form.fund" placeholder="请输入培训经费(元)" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="执行情况：" prop="execStatus">
            <el-input v-model="form.execStatus" placeholder="请输入执行情况" clearable></el-input>
            <!-- <el-select v-model="form.execStatus" placeholder="请选择执行情况" clearable>
              <el-option v-for="item in execStatusOption" :key="item.label" :label="item.label" :value="item.label">
              </el-option>
            </el-select> -->
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="培训证明" prop="aid">
            <upload-file @success="handleSuccess" @remove="handleRemove" accept=".jpg,.jpeg,.png,.pdf,.JPG,.JPEG,.PNG,.PDF" :size="15" :fileList="aidFiles" name="uploadFile"></upload-file>
          </el-form-item>
        </el-col>
      </el-row>

      <div class="submit-box">
        <el-button @click="()=>{this.$emit('closeEditDialog')}">取消</el-button>
        <el-button type="primary" @click="submitForm('form')">保存</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import uploadFile from "@/components/uploadFile";
import { staffTraining } from "@/api/propertyManage";
export default {
  name: "StaffTrainingForm",
  props: {
    id: {
      type: String,
      default: ''
    }
  },
  components: {
    uploadFile
  },
  data() {
    return {
      form: {},
      aidFiles: [],
      approvalUnitOption: [
        { label: '完成', value: '' },
        { label: '', value: '' },
      ],
    }
  },
  mounted() {
    if (this.id) {
      this.getDetail()
    }
  },
  methods: {
    handleSuccess(data) {
      console.log(data,'data file');
      this.aidFiles = []
      if (data.file.code == 0) {
        let files = data.file.data;
        this.form.aid = files.id;
        this.aidFiles = [
          {
            name: files.originalName,
            url: files.filePath,
          },
        ];
      } else {
        this.$message.error(res.msg);
        this.aidFiles = [];
      }
    },
    handleRemove() {
      console.log('file remove');
      this.form.aid = "";
      this.aidFiles = [];
    },
    submitForm(formName) {
      let that = this
      that.$refs[formName].validate((valid) => {
        if (valid) {
          that.form.id = that.id
          that.save(that.form)
        } else {
          return false;
        }
      });
    },
    async save(data) {
      console.log(data,'save data');
      const {
        person,
        time,
        fund,
        execStatus,
        aid,
        id,
        sourceType
      } = data;
      let temp = {
        person,
        time,
        fund,
        execStatus,
        aid,
        id,
        sourceType
      };
      let res = await staffTraining.save(temp)
      if (res.code == 0) {
        let msg = "新增成功~";
        if (this.id) {
          msg = "编辑成功~";
        }
        this.$message.success(msg);
        this.$emit("closeEditDialog", 1);
        this.$emit('getList')
      } else {
        this.$message.error(res.msg);
      }
    },
    async getDetail() {
      let res = await staffTraining.list({
        id: this.id,
      });
      if (res.code == 0) {
        this.form = res.data.list[0]
        if (this.form.files && this.form.files.length !== 0) {
          this.form.files.forEach(element => {
            this.aidFiles.push(
              {
                url: element.fileUrl,
                name: element.fileName,
              },
            )
          });
        }
      }
    },
  }
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

    .el-input__append {
      background-color: #ffffff;
    }
  }
  .submit-box {
    text-align: right;
  }
}
</style>