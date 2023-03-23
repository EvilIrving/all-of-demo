<!--
 * @Date: 2022-04-26 10:54:04
 * @Author: 
 * @Description: 合同管理-考核管理-定期考评 表单
 * @LastEditors: dtb
 * @LastEditTime: 2022-06-22 15:04:03
 * @FilePath: \lucheng_project\src\views\propertyManage\contractManagement\assessmentMmanagement\RegularCheckForm.vue
-->
<template>
  <div class="form">
    <div class="item_box_name">
      <p>基础信息</p>
    </div>
    <el-form :model="formData" status-icon :rules="rules" ref="ruleForm" label-position="right" :label-width="labelWidth">

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="考评单位：" prop="examUnit">
            <el-select v-model="formData.examUnitId" clearable @change="getUserList" placeholder="请选择考评单位：">
              <el-option v-for="item in examUnitOptions" :key="item.name" :value="item.id" :label="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被考评单位：" prop="evaluatedUnit">
            <el-select v-model="formData.evaluatedUnitId" clearable placeholder="请选择被考评单位：">
              <el-option v-for="item in evaluatedUnitOptions" :key="item.id" :value="item.id" :label="item.companyName"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="考核人员：" prop="staffAppraisal">
            <el-select v-model="formData.staffAppraisalId" clearable placeholder="请选择考核人员：">
              <el-option v-for="item in staffAppraisalOptions" :key="item.id" :value="item.id" :label="item.username"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="综合得分：" prop="synthesisScore">
            <el-input readonly type="number" v-model="formData.synthesisScore"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div class="item_box_name">
      <p>相关负责人</p>
    </div>
    <!-- 新增 -->
    <div class="tb_wrapper" v-if="id === ''">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column type="index" align="center" label="序号" width="20">
        </el-table-column>
        <el-table-column prop="acceptanceItem" align="center" label="验收事项" width="40">
        </el-table-column>
        <el-table-column label="验收要求" width="180">
          <template slot-scope="scope">
            <el-input style="font-size:12px" readonly resize="none" v-model="scope.row.acceptanceRequirements" type="textarea" autosize></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="standardScore" align="center" label="标准分" width="35">
        </el-table-column>
        <el-table-column align="center" label="得分" width="35">
          <template slot-scope="scope">
            <el-input type="number" v-model="scope.row.score" clearable @change="changeScore(scope.$index,scope.row)"></el-input>
          </template>
        </el-table-column>
        <el-table-column align="center" label="扣分原因" width="90">
          <template slot-scope="scope">
            <el-input v-model="scope.row.markPrinciple" clearable resize="none" type="textarea" autosize></el-input>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 编辑 -->
    <div class="tb_wrapper" v-else>
      <el-table :data="formData.list" style="width: 100%">
        <el-table-column type="index" align="center" label="序号" width="20">
        </el-table-column>
        <el-table-column prop="acceptanceItem" align="center" label="验收事项" width="40">
        </el-table-column>
        <el-table-column label="验收要求" width="180">
          <template slot-scope="scope">
            <el-input style="font-size:12px" readonly resize="none" v-model="scope.row.acceptanceRequirements" type="textarea" autosize></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="standardScore" align="center" label="标准分" width="35">
        </el-table-column>
        <el-table-column align="center" label="得分" width="30">
          <template slot-scope="scope">
            <el-input type="number" v-model="scope.row.score" clearable @change="changeScore(scope.$index,scope.row)"></el-input>
          </template>
        </el-table-column>
        <el-table-column align="center" label="扣分原因" width="90">
          <template slot-scope="scope">
            <el-input v-model="scope.row.markPrinciple" clearable resize="none" type="textarea" autosize></el-input>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="footer_btn">
      <el-button size="mini" type="primary" @click="submitForm('ruleForm')">保存</el-button>
      <el-button size="mini" @click="closeEditDialog()">取消</el-button>
    </div>
  </div>
</template>

<script>
import { regularCheck, propertyUnitApi } from '@/api/propertyManage'
export default {
  name: 'RegularCheckForm',
  props: {
    id: [String, Number],
  },
  components: {
  },
  data() {
    return {
      examUnitOptions: [],
      evaluatedUnitOptions: [],
      staffAppraisalOptions: [],
      labelWidth: '150px',
      formData: { synthesisScore: '' },
      rules: {
        // examUnit: [{ required: true, message: '必填项', trigger: 'blur' }],
        // evaluatedUnit: [{ required: true, message: '必填项', trigger: 'blur' }],
        // staffAppraisal: [{ required: true, message: '必填项', trigger: 'blur' }],
        // synthesisScore: [{ required: true, message: '必填项', trigger: 'blur' }],
      },
      tableData: [],
    }
  },
  watch: {
    // tableData: function (newVal, oldVal) {
    //   console.log(newVal, 'vewvak');
    //   let score = 0
    //   newVal.forEach((item) => {
    //     if (+item.score > +item.standardScore) {
    //       this.$message.warning('得分不可大于标准分')
    //     } else {
    //       score = score + (item.score ? +item.score : 0)
    //     }
    //   })
    //   this.formData.synthesisScore = score
    // }
  },
  mounted() {
    if (this.id) {
      this.getDetail()
    } else {
      this.getList()
    }
    this.getUnitList()
    this.getProperList()

  },
  methods: {
    // 考评单位
    async getProperList() {
      let res = await propertyUnitApi.properList()
      if (res.code == 1) {
        this.examUnitOptions = res.data
      }
    },
    // 考评人员
    async getUserList(item) {
      let pid = this.examUnitOptions.filter(i => i.id == item)[0].id
      let res = await propertyUnitApi.userList({ deptId: pid })
      if (res.code == 0) {
        this.staffAppraisalOptions = res.data.list || []
      }
    },
    // 被考评单位
    async getUnitList() {
      let res = await propertyUnitApi.list({ fetchAll: true })
      if (res.code == 0) {
        this.evaluatedUnitOptions = res.data.list
      }
    },
    changeScore(index, rowData) {
      if (rowData.score > rowData.standardScore || rowData.score < 0) {
        this.$message.warning('得分不可大于标准分且不可小于0')
        if (this.id) {
          // 编辑时分数改变
          this.tableData[index].score = ''
        } else {
          // 新增时分数改变
          this.formData.list[index].score = ''
        }
      } else {
        this.formData.synthesisScore = 0
        if (this.id) {
          // 编辑时分数改变
          this.$nextTick(() => {
            this.formData.list.forEach(element => {
              this.formData.synthesisScore += element.score ? +element.score : 0
            });
          })
        } else {
          // 新增时分数改变
          this.$nextTick(() => {
            this.tableData.forEach(element => {
              this.formData.synthesisScore += element.score ? +element.score : 0
            });
          })
        }

      }
    },
    async getDetail() {
      let res = await regularCheck.getById({
        id: this.id,
      })
      if (res.code == 0) {
        this.formData = res.data
        // this.formData.examUnitId = res.data.examUnitId
        this.getUserList(res.data.examUnitId)
        // this.formData.evaluatedUnitId = + res.data.evaluatedUnitId
        // this.formData.staffAppraisalId = res.data.staffAppraisalId
      }
    },
    async getList() {
      let res = await regularCheck.mbList()
      if (res.code == 0) {
        this.tableData = res.data
      }
    },

    submitForm(formName) {
      let that = this
      that.$refs[formName].validate((valid) => {
        if (valid) {

          that.save(that.formData)
        } else {
          return false
        }
      })
    },

    async save(data) {
      if (this.id) {
        // 编辑
        data.list = this.formData.list.map(element => {
          return {
            acceptanceItem: element.acceptanceItem,
            acceptanceRequirements: element.acceptanceRequirements,
            markPrinciple: element.markPrinciple,
            standardScore: element.standardScore,
            score: element.score,
            id: element.id,
            kpId: element.kpId,
          }
        });
      } else {
        // 新增
        data.list = this.tableData.map(element => {
          return {
            // acceptanceItem: element.acceptanceItem,
            // acceptanceRequirements: element.acceptanceRequirements,
            markPrinciple: element.markPrinciple,
            standardScore: element.standardScore,
            score: element.score,
            id: element.id,
          }
        });
      }
      const { evaluatedUnitId, examUnitId, staffAppraisalId, synthesisScore, list, id } = data;
      // if (list.filter((elem) => !elem.score).length > 0) {
      //    this.$message.warning('所有分数为必填项');
      //     return;
      // }

      let temp = { evaluatedUnitId, examUnitId, staffAppraisalId, synthesisScore, list, id };
      let res = await regularCheck.save(temp)
      if (res.code == 0) {
        this.$message({
          message: '保存成功',
          type: 'success',
          duration: '1000',
          onClose: () => {
            this.closeEditDialog()
          },
        })
      } else {
        this.$message.error('保存失败！')
      }
    },

    closeEditDialog() {
      this.$emit('closeEditDialog')
    },
  },
}
</script>

<style lang="scss" scoped>
.wrap {
  width: 100%;
  .user-form {
    width: 100%;
    .el-form {
      width: 100%;
      background: #fff;
      padding: 8px 0;
      .form_item {
        margin-bottom: 8px;
        .el-form-item {
          margin-bottom: 8px;
        }
      }
    }
  }
}
.el-dialog__footer {
  border-top: 2px solid #dcdfe6;
  padding: 0;
  .el-form-item {
    margin-bottom: 0;
  }
}

.is-plain {
  background: #fff;
}
.item_box_name {
  // @includes flexbox();
  // @includes flexAI(center);
  // @includes flexJC(space-between);
  // border-bottom: 1px solid #eee;
  height: 25px;
  padding-right: 16px;
  margin-bottom: 4px;
  p {
    font-size: 16px;
    font-weight: 600;
    color: #333333;
    height: 16px;
    line-height: 16px;
    border-left: 4px solid #1492ff;
    border-radius: 2px;
    padding-left: 10px;
  }
}
.el-dialog__body {
  padding: 10px 20px 30px;
}
.lines {
  width: 100%;
  height: 1px;
  background: #f5f5f5;
  margin: 5px 0 10px;
}
.el-input.is-disabled .el-input__inner {
  background-color: #ffffff;
  color: #4e5158;
  cursor: not-allowed;
}
.upload-demo {
  .upload-btn {
    display: flex;
    align-items: center;
    img {
      width: 14px;
      height: 13px;
      margin-right: 8px;
    }
  }
  .el-upload__tip {
    margin-top: 0;
    color: #969799;
    line-height: 18px;
  }
}
</style>