<!--
 * @Author: zg
 * @Date: 2022-02-18 10:50:25
 * @LastEditTime: 2022-03-14 17:30:50
 * @LastEditors: zg
 * @Description: 更新列别名
 * @FilePath: \project_manage_web\src\views\manage\inforShare\components\Form.vue
-->
<template>
  <div class="form_content">
    <el-form
      ref="form"
      :model="form"
      :rules="readonly ? {} : rules"
      label-width="96px"
      label-position="top"
    >
      <el-form-item
        v-for="item in columnList"
        :key="item.columnName"
        :label="item.columnName"
        :prop="item.columnName"
      >
        <el-input v-model="item.customColumnName" />
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
import { updateColumnAlias } from "_a/dataManage/dataSheetManage/dataSheetManage";
import { MessageError, MessageSuccess } from "_l/message";
export default {
  name: "dataSourceForm",
  props: {
    readonly: {
      type: Boolean,
      default: false,
    },
    dataSourceId: {
      type: String,
    },
    tableName: {
      type: String,
    },
  },
  components: {},
  data() {
    return {
      columnList: [],
      form: {},
      rules: {},
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    setForm(data) {
      // let dataArr = JSON.parse(JSON.stringify(data));
      this.columnList = JSON.parse(JSON.stringify(data));
      // dataArr.map((item) => {
      //   this.columnList.push({
      //     columnName: item.columnName,
      //     customColumnName: item.displayName,
      //     dataSourceId: item.dataSourceId,
      //     id: item.id,
      //     tableName: item.tableName,
      //   });
      // });
      // console.log(data);
    },
    reset() {
      this.$refs["form"].resetFields();
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
      //console.log(this.columnList);
      this.$refs[name].validate((valid) => {
        if (valid) {
          updateColumnAlias({
            list: this.columnList,
            dataSourceId: this.dataSourceId,
            tableName: this.tableName,
          }).then((res) => {
            if (res.data) {
              MessageSuccess("upd");
            } else {
              MessageError("upd");
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
.form_content {
  max-height: 650px;
  padding: 20px;
  overflow-y: auto;
  ::v-deep .el-select {
    width: 100%;
  }
}
.dialog-footer {
  text-align: right;
}
::v-deep .el-date-editor.el-input,
.el-date-editor.el-input__inner,
.el-input-number--medium {
  width: 100%;
}
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
