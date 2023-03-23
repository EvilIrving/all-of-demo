<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-22 18:00:41
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\operation\dispatch\components\tunnelSpillwayFrom.vue
-->
<template>
  <div>
    <div class="search_left">
      <span style="font-size: 14px">实际开启时间：</span>
      <el-date-picker
        style="width: 220px; margin: 0 20px 0 15px"
        v-model="actlOpenTm"
        value-format="yyyy-MM-dd"
        type="date"
        placeholder="请选择"
        @change="getLoadSluiceway"
      >
      </el-date-picker>
    </div>
    <div class="tb_wrapper">
      <el-table :data="tableData" border style="width: 100%" height="250">
        <el-table-column type="selection" width="50"> </el-table-column>
        <el-table-column
          type="index"
          align="center"
          width="50"
          label="序号"
        ></el-table-column>
        <el-table-column
          prop="name"
          label="泄洪洞名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="actlOpenTm"
          label="实际开启时间"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="actlCloseTm"
          label="实际关闭时间"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="opening"
          label="开度（m）"
          align="center"
        ></el-table-column>
        <el-table-column
          label="流量（m³/s）"
          prop="flow"
          align="center"
        >
        </el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="del(scope.row.id)"
                >删除</span
              >
            </div>
          </template>
        </el-table-column>
      </el-table>
      <div class="add-btn">
        <div>
          <span class="el-icon-plus"></span>
          <span @click="editTable('新增')">添加</span>
        </div>
      </div>
    </div>
    <el-dialog
      :title="title"
      :visible.sync="dialogxhdVisible"
      width="30%"
      append-to-body
      :before-close="handleClose">
      <el-form
      :model="ruleForm"
      ref="ruleForm"
      label-width="150px"
      class="demo-ruleForm"
    >
      <div class="form-item">
        <el-form-item label="闸门名称：" prop="name">
          <el-input
            class="form-item-value"
            v-model="ruleForm.name"
          ></el-input>
        </el-form-item>
        <el-form-item label="实际开启时间：" prop="actlOpenTm">
          <el-date-picker
            class="form-item-value"
            v-model="ruleForm.actlOpenTm"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetime"
            placeholder="请选择"
          >
          </el-date-picker>
        </el-form-item>
      </div>
      <div class="form-item">
        <el-form-item label="实际关闭时间：" prop="actlCloseTm">
          <el-date-picker
            class="form-item-value"
            v-model="ruleForm.actlCloseTm"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetime"
            placeholder="请选择"
          >
          </el-date-picker>
        </el-form-item>
      </div>
      <div class="form-item">
        <el-form-item label="开度（m）：" prop="opening">
          <el-input
            class="form-item-value"
            v-model="ruleForm.opening"
          ></el-input>
        </el-form-item>
        <el-form-item label="流量（m³/s）：" prop="flow">
          <el-input
            class="form-item-value"
            v-model="ruleForm.flow"
          ></el-input>
        </el-form-item>
      </div>
    </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogxhdVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm('ruleForm')">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { matterApi } from '@/api/propertyManage.js'
export default {
  props: {
    soId: String,
  },
  data() {
    return {
      actlOpenTm: '',
      dialogxhdVisible: false,
      title: '新增',
      id: '',
      tableData: [],
      ruleForm: {
        id: '',
        soId: '',
        name: '',
        actlOpenTm: '',
        actlCloseTm: '',
        opening: '',
        flow: '',
      },
      submitFlag: true,
    };
  },
  mounted() {
    this.getLoadSluiceway()
  },
  methods: {
    async getLoadSluiceway() {
      await matterApi
        .queryBySpillwayTunnel({
          soId: this.soId,
          actlOpenTm: this.actlOpenTm
        })
        .then((res) => {
          if (res.code === 0) {
            console.log(res);
            this.tableData = res.data
          }
        })
    },
    async getSingleLoadSluiceway() {
      await matterApi
        .queryBySpillwayTunnel({
          soId: this.soId,
          id: this.ruleForm.id
        })
        .then((res) => {
          if (res.code === 0) {
            console.log(res);
            let data = res.data[0];
            if (this.ruleForm.id) {
              this.ruleForm = {
                id: data.id,
                soId: data.soId,
                name: data.name,
                actlOpenTm: data.actlOpenTm,
                actlCloseTm: data.actlCloseTm,
                opening: data.opening,
                flow: data.flow,
              };
            }
          }
        })
    },
    editTable(type, id) {
      this.ruleForm.id = id
      this.title = type
      this.dialogxhdVisible = true
      if (id) {
        this.getSingleLoadSluiceway();
      }
    },
    submitForm(formName) {
      this.ruleForm.soId = this.soId
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.submitFlag) {
            this.submitFlag = false;
            // this.ruleForm.issueMan = issueMan.label;
            matterApi
              .spillwayTunnelSave(this.ruleForm)
              .then((res) => {
                if (res.code == 0) {
                  this.$message.success("保存成功~");
                  this.dialogxhdVisible = false
                  this.getLoadSluiceway()
                  // this.handleClose()
                } else {
                  this.$message.error(res.message);
                }
                this.submitFlag = true;
              })
              .catch((err) => {
                this.submitFlag = true;
                this.$message.error(err);
              });
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    handleClose(formName) {
      console.log(1111);
      this.$refs.ruleForm.resetFields();
      this.dialogxhdVisible = false
    },
    del(id) {
      this.$confirm('确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        matterApi.spillwayTunnelDelete({
          id: id
        }).then((res) => {
          if (res.code == 0) {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            this.getLoadSluiceway()
          } else {
            this.$message({
              type: 'error',
              message: '删除失败！',
            })
          }
        })
      })
    },
  }
};
</script>

<style lang="scss" scoped>
.tb_wrapper {
  padding: 16px 0;
}
.add-btn {
  display: flex;
  justify-content: center;
  height: 36px;
  border: 1px solid #dddddd;
  border-top: none;
  > div {
    line-height: 36px;
    color: #1492ff;
    font-size: 16px;
    cursor: pointer;
    > span:nth-child(1) {
      font-size: 14px;
    }
  }
}
.btn-box{
  text-align: right;
  padding-top: 20px;
}
.form-item {
  .el-input {
    width: 360px;
  }
}
</style>