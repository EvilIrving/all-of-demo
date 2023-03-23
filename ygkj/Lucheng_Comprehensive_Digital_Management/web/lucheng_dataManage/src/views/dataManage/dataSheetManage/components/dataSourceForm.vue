<!--
 * @Author: dyn
 * @Date: 2022-02-18 10:50:25
 * @LastEditTime: 2022-03-14 17:30:50
 * @LastEditors: dyn
 * @Description: 数据源表单
 * @FilePath: \project_manage_web\src\views\manage\inforShare\components\Form.vue
-->
<template>
  <div>
    <el-form
      ref="form"
      :model="form"
      :rules="readonly ? {} : rules"
      label-width="96px"
    >
      <el-form-item label="数据库名称" prop="dataBaseName">
        <el-input
          v-model="form.dataBaseName"
          placeholder="请输入数据库名称"
          :readonly="readonly"
        />
      </el-form-item>
      <el-form-item label="数据库地址" prop="dataBaseAddress">
        <el-input
          v-model="form.dataBaseAddress"
          placeholder="请输入数据库地址"
          :readonly="readonly"
        />
      </el-form-item>
      <el-form-item label="数据库类型" prop="dataBaseType">
        <el-select
          v-model="form.dataBaseType"
          placeholder="请选择数据库类型"
          style="width: 100%"
          :readonly="readonly"
        >
          <el-option
            v-for="item in typeList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="数据库账号" prop="accountNumber">
        <el-input
          v-model="form.accountNumber"
          placeholder="请输入数据库账号"
          :readonly="readonly"
        />
      </el-form-item>
      <el-form-item label="数据库密码" prop="accountPwd">
        <el-input
          v-model="form.accountPwd"
          placeholder="请输入数据库密码"
          :readonly="readonly"
        />
      </el-form-item>
    </el-form>
    <!-- 自定义按钮组 -->
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleReset('form')">取 消</el-button>
      <el-button type="primary" @click="handleSubmit('form')" v-if="!readonly"
        >保存</el-button
      >
    </div>
  </div>
</template>

<script>
import {
  addDataSource,
  updateDataSource,
} from "_a/dataManage/dataSheetManage/dataSheetManage";
import { MessageError, MessageSuccess } from "_l/message";
export default {
  name: "dataSourceForm",
  props: {
    readonly: {
      type: Boolean,
      default: false,
    },
    dialogTitle: {
      type: String,
    },
  },
  components: {},
  data() {
    return {
      form: {},
      rules: {
        dataBaseName: [this.$ruler("数据库名称")],
      },
      typeList: [
        {
          label: "MySQL",
          value: 1,
        },
        {
          label: "ORACLE",
          value: 2,
        },
        {
          label: "PostgreSQL",
          value: 3,
        },
        {
          label: "SQLServer",
          value: 4,
        },
        {
          label: "DM",
          value: 5,
        },
      ],
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    reset() {
      this.$refs["form"].resetFields();
    },
    setForm(row) {
      this.form = row;
    },
    /**
     * 重置表单
     * @param name
     */
    handleReset(name) {
      this.$emit("close");
      this.$refs[name].resetFields();
    },
    /**
     * 提交表单
     * @param name
     */
    handleSubmit(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          if (this.dialogTitle === "新增数据源") {
            addDataSource(this.form).then((res) => {
              if (res.code) {
                MessageSuccess("add");
              } else {
                MessageError("add");
              }
              this.$emit("closeDialogAndRefresh");
            });
          } else {
            updateDataSource(this.form).then((res) => {
              if (res.code) {
                MessageSuccess("upd");
              } else {
                MessageError("upd");
              }
              this.$emit("closeDialogAndRefresh");
            });
          }
        }
      });
    },
  },
};
</script> 

<style lang='scss' scoped>
.dialog-footer {
  text-align: right;
}
</style>
