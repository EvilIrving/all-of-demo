<!--
 * @Author: hanyu
 * @LastEditTime: 2021-11-05 18:09:57
 * @Description: 
 * @FilePath: \lucheng_project\src\views\seawallPrevent\trainingExercises\exercisesManage\ExercisesManageForm.vue
-->
<template>
  <div class="wrap-form-box">
    <el-form :rules="rules" ref="form" :model="form" label-width="100px">
      <div class="training-form-box">
        <el-form-item label="培训时间：" prop="tm">
          <el-date-picker
            type="datetime"
            placeholder="请选择"
            v-model="form.tm"
            style="width: 100%"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="培训地点：" prop="address">
          <el-input v-model="form.address" placeholder="请输入"></el-input>
        </el-form-item>
      </div>
      <div class="training-form-box">
        <el-form-item label="培训人员：" prop="person">
          <el-input v-model="form.person" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="培训人数：" prop="pnum">
          <el-input
            maxlength="11"
            oninput="this.value = this.value.replace(/[^0-9]/g, '')"
            v-model="form.pnum"
            placeholder="请输入"
          ></el-input>
        </el-form-item>
      </div>
      <el-form-item label="培训事由：" prop="reasons">
        <el-input
          type="textarea"
          v-model="form.reasons"
          placeholder="请输入"
        ></el-input>
      </el-form-item>
      <el-form-item label="上传附件：">
        <unpload-file
          @success="handleSuccess"
          @remove="handleRemove"
          accept=".pdf"
          :size="15"
          :fileList="fileList"
        ></unpload-file>
      </el-form-item>
      <el-form-item>
        <div class="submit-box">
          <el-button
            @click="
              () => {
                this.$emit('closeEditDialog');
              }
            "
            >取消</el-button
          >
          <el-button type="primary" @click="submitForm('form')">确定</el-button>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import unploadFile from "@/components/uploadFile.vue";
import { trainingExercisesApi } from "@/api/seawallPrevent";
export default {
  name: "TrainingManageForm",
  props: {
    id: {
      type: String,
      default: "",
    },
  },
  components: { unploadFile },
  data() {
    return {
      form: {
        tm: "",
        address: "",
        person: "",
        reasons: "",
        fileurl: "",
        filename: "",
        pnum: "",
      },
      fileList: [],
      rules: {
        tm: [
          {
            required: true,
            message: "请选择演练时间",
            trigger: "change",
          },
        ],
        address: [
          { required: true, message: "请输入演练地点", trigger: "blur" },
        ],
        reasons: [
          { required: true, message: "请输入演练是由", trigger: "blur" },
        ],
        person: [
          { required: true, message: "请输入参与人员", trigger: "blur" },
        ],
        pnum: [{ required: true, message: "请输入培训人数", trigger: "blur" }],
      },
      submitFlag: true,
    };
  },
  mounted() {
    if (this.id) {
      this.getDetail();
    }
  },
  methods: {
    handleSuccess(res) {
      if (res.file.code == 0) {
        let file = res.file.data;
        this.form.fileurl = file.filePath;
        this.form.filename = file.originalName;
      }
    },
    handleRemove() {
      this.form.fileurl = "";
      this.form.filename = "";
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.submitFlag) {
            return false;
          }
          this.submitFlag = false;
          trainingExercisesApi
            .trainingSave(this.form)
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
          console.log("error submit!!");
          return false;
        }
      });
    },
    async getDetail() {
      let res = await trainingExercisesApi.trainingDetail({ id: this.id });
      console.log(res);
      if (res.code == 0) {
        let {
          address,
          filename,
          fileurl,
          person,
          reasons,
          tm,
          id,
          pnum,
        } = res.data;
        this.form = {
          address,
          filename,
          fileurl,
          person,
          reasons,
          tm,
          id,
          pnum,
        };
        if (this.form.fileurl) {
          this.fileList = [
            {
              url: this.form.fileurl,
              name: this.form.filename,
            },
          ];
        }
      }
    },
  },
};
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
