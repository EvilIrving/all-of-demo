<!--
 * @Author: dyn
 * @Date: 2022-02-18 10:50:25
 * @LastEditTime: 2022-03-14 17:30:50
 * @LastEditors: dyn
 * @Description: 信息分享表单
 * @FilePath: \project_manage_web\src\views\manage\inforShare\components\Form.vue
-->
<template>
  <div>
    <el-form
      ref="form"
      :model="form"
      :rules="readonly ? {} : rules"
      label-width="80px"
    >
      <el-form-item label="资源名称" prop="resourceName">
        <el-input
          v-model="form.resourceName"
          placeholder="请输入资源名称"
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
  addDirectory,
  updateDirectory,
} from "_a/dataManage/resourceDirectory/resourceDirectory";
import { MessageError, MessageSuccess } from "_l/message";
export default {
  name: "directoryForm",
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
        resourceName: [this.$ruler("资源名称")],
      },
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
          if (this.dialogTitle === "新增目录树") {
            addDirectory(this.form).then((res) => {
              if (res.code) {
                MessageSuccess("add");
              } else {
                MessageError("add");
              }
              this.$emit("closeDialogAndRefresh");
            });
          } else {
            updateDirectory(this.form).then((res) => {
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
