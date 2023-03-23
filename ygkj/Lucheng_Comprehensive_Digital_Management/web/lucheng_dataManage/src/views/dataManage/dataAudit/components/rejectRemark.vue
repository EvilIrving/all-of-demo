<!--
 * @Author: zg
 * @Date: 2022-02-18 10:50:25
 * @LastEditTime: 2022-03-14 17:30:50
 * @LastEditors: zg
 * @Description: 驳回备注
 * @FilePath: \project_manage_web\src\views\manage\inforShare\components\Form.vue
-->
<template>
  <div class="link-box">
    <el-form ref="form" :model="form" :rules="rules" label-width="60px">
      <el-form-item label="备注" prop="remark">
        <el-input
          type="textarea"
          :autosize="{ minRows: 4 }"
          placeholder="请输入备注"
          v-model="form.remark"
        >
        </el-input>
      </el-form-item>
    </el-form>
    <!-- 自定义按钮组 -->
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose()">取消</el-button>
      <el-button type="primary" @click="handleSubmit()">确定</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "rejectRemark",
  props: {
    selectRows: {
      type: Array,
    },
    dataSourceId: {
      type: String,
    },
  },
  components: {},
  data() {
    return {
      form: {
        remark: "",
      },
      rules: {},
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    handleClose() {
      this.$emit("close");
    },
    /**
     * 确定
     */
    handleSubmit() {
      this.$emit("closeDialogAndRefresh", this.form.remark);
    },
  },
};
</script> 

<style lang='scss' scoped>
::v-deep .el-select {
  width: 100%;
}
.dialog-footer {
  text-align: right;
}
</style>
