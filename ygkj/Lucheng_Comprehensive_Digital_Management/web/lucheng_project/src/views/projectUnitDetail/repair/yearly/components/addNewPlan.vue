<template>
  <div class="wrap-form-box">
    <el-form  ref="form" :model="form" label-width="150px">
      <el-row>
        <el-col :span="11">
          <el-form-item label="工程名称：" prop="projectName">
            <el-input v-model="form.projectName" placeholder="请输入"  maxlength="50" style="width: 320px" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="年份：" prop="annual">
            <el-date-picker
                type="year"
                placeholder="请选择"
                format="yyyy"
                value-format="yyyy"
                v-model="form.annual"
                style="width: 320px;"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="年底计划说明：" prop="comment">
        <el-input v-model="form.comment" placeholder="请输入"  maxlength="50"  type="textarea" :row="3" style="width: 742px"></el-input>
      </el-form-item>
      <el-form-item label="维养计划文件：">
        <upload-file
            @success="handleSuccess"
            @remove="handleRemove"
            accept=".pdf"
            :size="15"
            name="uploadFile"
            :fileList="fileList"
        ></upload-file>
      </el-form-item>

      <div class="submit-box">
      <el-button @click="()=>{this.$emit('closeEditDialog')}">取消</el-button>
      <el-button type="primary" @click="submitForm('form')">保存</el-button>
    </div>
    </el-form>
  </div>
</template>

<script>
import uploadFile from "@/components/uploadFile";
import {annulMaintenance} from "@/api/projectUnitDetail";

export default {
  name: "addNewPlan",
  props:{
    id:{
      type:String,
      default:'',
    }

  },
  components:{
    uploadFile
  },
  computed: {
    projectInfo(){
      return this.$store.state.projectInfo
    }
  },
  data(){
    return{
      form: {
        projectName: '',
        annual: new Date().format('yyyy'),
        comment: "",
        file:''
      },
      submitFlag: true,
      fileList: [],
    }
  },
  mounted() {
    this.form.projectName = this.projectInfo.name
    if (this.id) {
      this.getDetail();
    }
  },
  methods: {
    handleSuccess(res) {
      console.log(res)
      if (res.file.code == 0) {
       // let file = res.file.data;
        this.form.file = res.file.data.id;
      }
    },
    handleRemove() {
      this.form.file = "";
      this.form.filename = "";
    },
    submitForm(formName) {
      console.log(this.form)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.submitFlag) {
            return false;
          }
          this.submitFlag = false;
          delete this.form.createTime
          delete this.form.paged
          this.form['prcd'] = this.projectInfo.prcd
          annulMaintenance
              .updateDmPlan(this.form)
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
      let res = await annulMaintenance.annulList({
        id: this.id,
        prcd:this.projectInfo.prcd
      });
      if (res.code == 0) {
        console.log(this.id,res)
        Object.assign(this.form, res.data.list[0])
        this.form.annual = res.data.list[0].annual.toString()
        if (this.form.fileurl) {
          this.fileList = [
            {
              url: this.form.file,
              name: this.form.filename,
            },
          ];
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
  }
  .submit-box {
    text-align: right;
  }
}
</style>