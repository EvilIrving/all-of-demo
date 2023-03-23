<template>
  <div class="wrap-form-box">
    <el-form  ref="form" :model="form" label-width="150px">
      <el-row>
        <el-col :span="11">
          <el-form-item label="日期：" prop="materialDate">
            <el-date-picker
                type="date"
                placeholder="请选择"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                v-model="form.materialDate"
                style="width: 320px;"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="负责人：" prop="projectLeader">
            <el-input v-model="form.projectLeader" placeholder="请输入"  maxlength="50" style="width: 320px" ></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="维护单位：" prop="maintainUnit">
        <el-input v-model="form.maintainUnit" placeholder="请输入"  maxlength="50" style="width: 740px" ></el-input>
      </el-form-item>

      <el-form-item label="问题详情：" prop="particulars">
        <el-input v-model="form.particulars" placeholder="请输入"  type="textarea" style="width: 740px" ></el-input>
      </el-form-item>
      <div class="submit-box">
        <el-button @click="()=>{this.$emit('closeEditDialog')}">取消</el-button>
        <el-button type="primary" @click="submitForm('form')">保存</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { databasedM} from "@/api/projectUnitDetail";

export default {
  name: "dataMtForm",
  props:{
    id:{
      type:String,
      default:'',
    }
  },
  components:{},
  computed:{
    projectInfo(){
      return this.$store.state.projectInfo
    }
  },
  data(){
    return{
      form:{
        materialDate:'',
        projectLeader:'',
        maintainUnit:'',
        particulars:'',
      },
      submitFlag:true
    }
  },
  mounted() {
    if (this.id) {
      this.getDetail();
    }
  },
  methods:{
    submitForm(formName){
      console.log(this.form,111111)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.submitFlag) {
            return false;
          }
          this.submitFlag = false;
          delete this.form.createTime
          delete this.form.paged
          this.form['prcd'] = this.projectInfo.prcd
          databasedM
              .saveDataMtList(this.form)
              .then((res) => {
                console.log(res,11111)
                if (res.code == 0) {
                  let msg = "新增成功~";
                  if (this.id) {
                    msg = "编辑成功~";
                  }
                  this.$message.success(msg);
                  this.$emit("closeEditDialog", 1);
                } else {
                  //this.$message.error(res.msg);
                  console.log(res.msg)
                  this.$message.error({message:res.msg|| "新增失败"});
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
      let res = await databasedM.getDataMtDetail({
        id: this.id,
       // prcd:this.projectInfo.prcd
      });
      if (res.code == 0) {
        console.log(this.id,res)
        Object.assign(this.form, res.data)
        //this.form.materialDate = res.data.list[0].materialDate.toString()
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
}
</style>