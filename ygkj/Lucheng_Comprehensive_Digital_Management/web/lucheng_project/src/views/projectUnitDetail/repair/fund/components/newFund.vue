<template>
  <div class="wrap-form-box">
    <el-form ref="form" :model="form" label-width="150px">
      <el-row>
        <el-col :span="11">
          <el-form-item label="年份：" prop="year">
            <el-date-picker
              type="year"
              placeholder="请选择"
              v-model="form.year"
              format="yyyy"
              value-format="yyyy"
              style="width: 320px"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="申报单位：" prop="declarationUnit">
            <el-select
              v-model="form.declarationUnit"
              placeholder="请选择"
              style="width: 300px"
            >
              <el-option
                v-for="item in declarationUnitOption"
                :key="item.id"
                :label="item.companyName"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="申报时间：" prop="declarationTime">
            <el-date-picker
              type="date"
              placeholder="请选择"
              value-format="yyyy-MM-dd"
              v-model="form.declarationTime"
              style="width: 320px"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="批复单位：" prop="approvalUnit">
            <el-select
              v-model="form.approvalUnit"
              placeholder="请选择"
              style="width: 300px"
            >
              <el-option
                v-for="item in approvalUnitOption"
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
          <el-form-item label="批复时间：" prop="approvalTime">
            <el-date-picker
              type="date"
              placeholder="请选择"
              v-model="form.approvalTime"
              value-format="yyyy-MM-dd"
              style="width: 320px"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="申报资金：" prop="dclrFunds">
            <el-input
              v-model="form.dclrFunds"
              placeholder="请输入"
              style="width: 300px"
            >
              <div slot="append">万元</div>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="上级下达资金：" prop="superiorfunds">
            <el-input
              v-model="form.superiorfunds"
              placeholder="请输入"
              maxlength="50"
              style="width: 320px"
            >
              <div slot="append">万元</div>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="自筹资金：" prop="selffunds">
            <el-input
              v-model="form.selffunds"
              placeholder="请输入"
              maxlength="50"
              style="width: 300px"
            >
              <div slot="append">万元</div>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="已使用资金：" prop="usedamount">
            <el-input
              v-model="form.usedamount"
              placeholder="请输入"
              maxlength="50"
              style="width: 320px"
            >
              <div slot="append">万元</div>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="已支付资金：" prop="paidamount">
            <el-input
              v-model="form.paidamount"
              placeholder="请输入"
              maxlength="50"
              style="width: 300px"
            >
              <div slot="append">万元</div>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="申报文件" prop="aid">
        <upload-file
          @success="handleSuccess"
          @remove="handleRemove"
          accept=".pdf"
          :size="15"
          :fileList="aidFiles"
          name="uploadFile"
        ></upload-file>
      </el-form-item>
      <el-form-item label="计划文件" prop="mtnPlan">
        <upload-file
          @success="handleSuccessPlan"
          @remove="handleRemovePlan"
          accept=".pdf"
          :size="15"
          :fileList="mtnPlanFiles"
          name="uploadFile"
        ></upload-file>
      </el-form-item>
      <div class="submit-box">
        <el-button
          @click="
            () => {
              this.$emit('closeEditDialog');
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
import uploadFile from "@/components/uploadFile";
import { annulMaintenance } from "@/api/projectUnitDetail";
export default {
  name: "newFund",
  props: {
    id: {
      type: String,
      default: "",
    },
  },
  components: {
    uploadFile,
  },
  computed: {
    projectInfo() {
      return this.$store.state.projectInfo;
    },
  },
  data() {
    return {
      form: {
        year: "",
        pid: "",
        aid: "",
        dclrFunds: "",
        mtnPlan: "",
        superiorfunds: "",
        selffunds: "",
        usedamount: "",
        paidamount: "",
        declarationUnit: "",
        declarationTime: "",
        approvalUnit: "67011533",
        approvalTime: "",
      },
      aidFiles: [],
      mtnPlanFiles: [],
      submitFlag: true,
      projectType: "",
      approvalUnitOption: [{ label: "鹿城区水利局", value: "67011533" }],
      declarationUnitOption: [],
    };
  },
  async mounted() {
    await this.getWaterManageUserList()
    if (this.id) {
      this.getDetail();
    }
  },
  methods: {
    async getWaterManageUserList() {
      let res = await annulMaintenance.getWaterManageUserList();
      if (res.code == 0) {
        this.declarationUnitOption = res.data;
      }
    },
    handleSuccess(res) {
      if (res.file.code == 0) {
        this.form.aid = res.file.data.id;
      }
    },
    handleSuccessPlan(res) {
      if (res.file.code == 0) {
        this.form.mtnPlan = res.file.data.id;
      }
    },
    handleRemove() {
      this.form.aid = "";
      this.form.filename = "";
    },
    handleRemovePlan() {
      this.form.aid = "";
      this.form.filename = "";
    },
    submitForm(formName) {
      //console.log(this.form)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.submitFlag) {
            return false;
          }
          this.submitFlag = false;
          delete this.form.createTime;
          delete this.form.paged;
          this.form["pid"] = this.projectInfo.prcd;
          annulMaintenance
            .updateFund(this.form)
            .then((res) => {
              console.log(res, 11111111);
              if (res.code == 0) {
                let msg = "新增成功~";
                if (this.id) {
                  msg = "编辑成功~";
                }
                this.$message.success(msg);
                this.$emit("closeEditDialog", 1);
              } else {
                this.$message.error({
                  message: res.msg || "标化同步数据无法修改",
                });
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
      let res = await annulMaintenance.fundList({
        id: this.id,
        pid: this.projectInfo.prcd,
      });
      if (res.code == 0) {
        Object.assign(this.form, res.data.list[0]);
        this.form.year = res.data.list[0].year.toString();
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

    .el-input__append {
      background-color: #ffffff;
    }
  }
  .submit-box {
    text-align: right;
  }
}
</style>