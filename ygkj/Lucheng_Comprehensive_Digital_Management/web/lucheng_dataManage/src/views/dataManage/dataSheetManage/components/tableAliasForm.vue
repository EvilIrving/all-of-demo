<!--
 * @Author: zg
 * @Date: 2022-02-18 10:50:25
 * @LastEditTime: 2022-03-14 17:30:50
 * @LastEditors: zg
 * @Description: 表别名表单
 * @FilePath: \project_manage_web\src\views\manage\inforShare\components\Form.vue
-->
<template>
  <div class="link-box">
    <el-form ref="form" :model="form" :rules="rules" label-width="96px">
      <el-form-item label="表别名" prop="customTableName">
        <el-input v-model="form.customTableName" placeholder="请输入表别名" />
      </el-form-item>
    </el-form>
    <!-- 自定义按钮组 -->
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleReset('form')">取 消</el-button>
      <el-button type="primary" @click="handleSubmit('form')">保存</el-button>
    </div>
  </div>
</template>

<script>
import { updateTableAlias } from "_a/dataManage/dataSheetManage/dataSheetManage";
import { MessageCustom } from "_l/message";
export default {
  name: "tableAliasForm",
  props: {
    dataSourceId: {
      type: String,
    },
  },
  components: {},
  data() {
    return {
      form: {
        customTableName: "",
      },
      rules: {},
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    setForm(row) {
      this.form = row;
    },

    reset() {
      this.$refs["form"].resetFields();
    },
    change(e) {
      this.$forceUpdate();
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
          let list = [];
          list.push(this.form);
          let data = {
            dataSourceId: this.dataSourceId,
            list: list,
          };
          updateTableAlias(data).then((res) => {
            if (res.code) {
              MessageCustom("success", "保存成功");
            } else {
              MessageCustom("error", "保存失败");
            }
            this.$emit("closeDialogAndRefresh");
          });
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
